import machine
from machine import Pin, SoftI2C
import time
from time import sleep
from LCD_i2c_driver import I2cLcd
from simpleKeyboard import Device

I2C_ADDR = 0x27
totalRows = 2
totalColumns = 16

i2c = SoftI2C(scl=Pin(22), sda=Pin(21), freq=10000)     #initializing the I2C method for ESP32
lcd = I2cLcd(i2c, I2C_ADDR, totalRows, totalColumns)
d = Device()

def mainLoop():
    fromConnecting = True
    lcd.putstr("Welcome to \nPassChain")
    sleep(2)
    while True:
        print(fromConnecting)
        if d.isConnected():
            if fromConnecting:
                connectedBle()
                fromConnecting = False
            sleep(2)
            lcd.clear()
#             for i in range(11):
#                 lcd.putstr(str(10 - i))
#                 d.send_string(str(10 - i))
#                 sleep(1)
#                 lcd.clear()
        else:
            d.advertise()
            connectingBle()
            fromConnecting = True
            sleep(2)
            
        
def connectingBle():
    lcd.clear()
    lcd.putstr("Connecting.")
    print("connecting.")
    time.sleep_ms(250)
    lcd.putstr(".")
    print(".")
    time.sleep_ms(250)
    lcd.putstr(".")
    print(".")
    time.sleep_ms(250)
    lcd.putstr(".")
    print(".")
    time.sleep_ms(250)
    return

def connectedBle():
    lcd.clear()
    lcd.putstr("The device is\n")
    print("The device is\n")
    print("connected!")
    lcd.putstr("connected !")
    time.sleep_ms(250)
    return

if __name__ == "__main__":
    mainLoop()