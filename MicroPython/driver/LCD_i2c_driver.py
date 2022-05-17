import utime
import gc

from machine import I2C

# Le seguenti costanti sono state prese dall'header file lcd.h
# ma sono state cambiate da bit a maschere di bit per facilitare le operazioni

# definizioni dei pin per il chip PCF8574
MASK_RS = 0x01       # P0
MASK_RW = 0x02       # P1
MASK_E  = 0x04       # P2

SHIFT_BACKLIGHT = 3  # P3
SHIFT_DATA      = 4  # P4-P7

class I2cLcd:
    
    # set di comandi del controller lcd HD44780
    LCD_CLR = 0x01              # DB0: clear display
    LCD_HOME = 0x02             # DB1: return to home position

    LCD_ENTRY_MODE = 0x04       # DB2: set entry mode
    LCD_ENTRY_INC = 0x02        # --DB1: increment
    LCD_ENTRY_SHIFT = 0x01      # --DB0: shift

    LCD_ON_CTRL = 0x08          # DB3: turn lcd/cursor on
    LCD_ON_DISPLAY = 0x04       # --DB2: turn display on
    LCD_ON_CURSOR = 0x02        # --DB1: turn cursor on
    LCD_ON_BLINK = 0x01         # --DB0: blinking cursor

    LCD_MOVE = 0x10             # DB4: move cursor/display
    LCD_MOVE_DISP = 0x08        # --DB3: move display (0-> move cursor)
    LCD_MOVE_RIGHT = 0x04       # --DB2: move right (0-> left)

    LCD_FUNCTION = 0x20         # DB5: function set
    LCD_FUNCTION_8BIT = 0x10    # --DB4: set 8BIT mode (0->4BIT mode)
    LCD_FUNCTION_2LINES = 0x08  # --DB3: two lines (0->one line)
    LCD_FUNCTION_10DOTS = 0x04  # --DB2: 5x10 font (0->5x7 font)
    LCD_FUNCTION_RESET = 0x30   # See "Initializing by Instruction" section

    LCD_CGRAM = 0x40            # DB6: set CG RAM address
    LCD_DDRAM = 0x80            # DB7: set DD RAM address

    LCD_RS_CMD = 0
    LCD_RS_DATA = 1

    LCD_RW_WRITE = 0
    LCD_RW_READ = 1

    def __init__(self, i2c, i2c_addr, num_lines, num_columns):
        self.i2c = i2c
        self.i2c_addr = i2c_addr
        self.i2c.writeto(self.i2c_addr, bytes([0]))
        utime.sleep_ms(20)   # Da il tempo all'LCD di accendersi

        # Invia il reset 3 volte (procedura standard)
        self.write_init(self.LCD_FUNCTION_RESET)
        utime.sleep_ms(5)    # Il primo delay dev'essere di almeno 4.1 msec
        self.write_init(self.LCD_FUNCTION_RESET)
        utime.sleep_ms(1)
        self.write_init(self.LCD_FUNCTION_RESET)
        utime.sleep_ms(1)

        # Inserisce la modalità 4-bit che ci permette di gestire l'LCD con soli 2 pin
        self.write_init(self.LCD_FUNCTION)
        utime.sleep_ms(1)
        
        self.num_lines = num_lines
        self.num_columns = num_columns
        self.cursor_x = 0
        self.cursor_y = 0
        self.implied_newline = False
        self.backlight = True
        self.clear()
        self.write_command(self.LCD_ENTRY_MODE | self.LCD_ENTRY_INC)
        self.hide_cursor()
        
        cmd = self.LCD_FUNCTION
        if num_lines > 1:
            cmd |= self.LCD_FUNCTION_2LINES
        self.write_command(cmd)
        gc.collect()
        
    def clear(self):
        """Pulisce lo schermo e imposta il cursore in alto a sinistra"""
        self.write_command(self.LCD_CLR)
        self.write_command(self.LCD_HOME)
        self.cursor_x = 0
        self.cursor_y = 0

    def hide_cursor(self):
        """Nasconde il cursore"""
        self.write_command(self.LCD_ON_CTRL | self.LCD_ON_DISPLAY)

    def display_on(self):
        """Accende l'LCD."""
        self.write_command(self.LCD_ON_CTRL | self.LCD_ON_DISPLAY)

    def display_off(self):
        """Spegne l'LCD."""
        self.write_command(self.LCD_ON_CTRL)

    def move_to(self, cursor_x, cursor_y):
        """
        Sposta il cursore nella posizione indicata.
        La posizione del cursore parte da 0 (es. cursor_x == 0 indica la prima colonna).
        """
        self.cursor_x = cursor_x
        self.cursor_y = cursor_y
        addr = cursor_x & 0x3f
        if cursor_y & 1:
            addr += 0x40    # Lines 1 & 3 add 0x40
        if cursor_y & 2:    # Lines 2 & 3 add number of columns
            addr += self.num_columns
        self.write_command(self.LCD_DDRAM | addr)

    def putchar(self, char):
        """
        Scrive il carattere passato sul display nella posizione del cursore attuale,
        dopodichè avanza alla posizione successiva
        """
        if char == '\n':
            if self.implied_newline:
                # self.implied_newline significa che siamo avanzati a causa di un \n,
                # quindi se riceviamo una nuova riga subito dopo, la ignoriamo.
                pass
            else:
                self.cursor_x = self.num_columns
        else:
            self.write_data(ord(char))
            self.cursor_x += 1
        if self.cursor_x >= self.num_columns:
            self.cursor_x = 0
            self.cursor_y += 1
            self.implied_newline = (char != '\n')
        if self.cursor_y >= self.num_lines:
            self.cursor_y = 0
        self.move_to(self.cursor_x, self.cursor_y)

    def putstr(self, string):
        """
        Scrive la stringa passata sul display partendo dalla posizione corrente
        e fa avanzare il cursore in modo appropriato.
        """
        for char in string:
            self.putchar(char)


    def write_init(self, setting):
        """Scrive un particolare comando ("setting") al display LCD.
        Questa funzione è usata solo durante l'iniziallizzazione dell'LCD."""
        byte = ((setting >> 4) & 0x0f) << SHIFT_DATA
        self.i2c.writeto(self.i2c_addr, bytes([byte | MASK_E]))
        self.i2c.writeto(self.i2c_addr, bytes([byte]))
        gc.collect()

        
    def write_command(self, cmd):
        """Scrive un comando sul display LCD."""
        byte = ((self.backlight << SHIFT_BACKLIGHT) |
                (((cmd >> 4) & 0x0f) << SHIFT_DATA))
        self.i2c.writeto(self.i2c_addr, bytes([byte | MASK_E]))
        self.i2c.writeto(self.i2c_addr, bytes([byte]))
        byte = ((self.backlight << SHIFT_BACKLIGHT) |
                ((cmd & 0x0f) << SHIFT_DATA))
        self.i2c.writeto(self.i2c_addr, bytes([byte | MASK_E]))
        self.i2c.writeto(self.i2c_addr, bytes([byte]))
        if cmd <= 3:
            # I comandi home e clear richiedono un ritardo nel caso peggiore di 4,1 msec.
            utime.sleep_ms(5)
        gc.collect()

    def write_data(self, data):
        """Scrive dati sull'LCD."""
        byte = (MASK_RS |
                (self.backlight << SHIFT_BACKLIGHT) |
                (((data >> 4) & 0x0f) << SHIFT_DATA))
        self.i2c.writeto(self.i2c_addr, bytes([byte | MASK_E]))
        self.i2c.writeto(self.i2c_addr, bytes([byte]))
        byte = (MASK_RS |
                (self.backlight << SHIFT_BACKLIGHT) |
                ((data & 0x0f) << SHIFT_DATA))
        self.i2c.writeto(self.i2c_addr, bytes([byte | MASK_E]))
        self.i2c.writeto(self.i2c_addr, bytes([byte]))
        gc.collect()
