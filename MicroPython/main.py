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
    while True:
        if d.isConnected():
            lcd.putstr("Welcome to \nPassChain")
            sleep(2)
            lcd.clear()
            lcd.putstr("Lets Countdown")
            sleep(2)
            lcd.clear()
            for i in range(11):
                lcd.putstr(str(10 - i))
                d.send_string(str(10 - i))
                sleep(1)
                lcd.clear()
        else:
            d.advertise()
            sleep(2)
        

if __name__ == "__main__":
    mainLoop()