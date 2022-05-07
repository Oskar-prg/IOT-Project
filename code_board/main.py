# I2C light sensor
# let's read some analog values from a gpio

# First, import the board module from the bsp (board support package).
# The bsp loads board specific names, variables and settings so that
# this project can be run on different Zerynth hardware
# without changing a line of code.
from bsp import board

# import the adc module
import adc
import gpio
import serial
import i2c
serial.serial()

i2c_scan_buf = i2c.scan()

for addr in i2c_scan_buf:
    print("addr", addr, "present")


#create an I2C object to comunicate with the i2c device at addr address 
#on the i2c bus number using the default i2c=I2C0, the default clock=1000000 frequency expressed in Hz

port=i2c.I2c(i2c_scan_buf[0], i2c=I2C0, clock= 400000)

'''
#write_buf = bytearray([0x80,0x03]) # powering up the device: write 80h-> COMMAND for register 0h (CONTROL);  write 3h in CONTROL
#test= port.write_read(write_buf,1) # the output of the read is the byte written in CONTROL (3)
#print('test',int(test))

light0=light1=1
dev_id=0


write_buf3 = bytearray([0x81, 0x10]) #write in the timing register (1h) the value 0x10 maximun gain (bit 4th of the register set to 1) 
port.write(write_buf3)               # and ADC integration rate at 13.7 ms

write_buf = bytearray([0x8A]) # read the device ID Ah
dev_id= port.write_read(write_buf,1)
print('dev_id=%x' %(dev_id[0]))
'''

while True:
    '''
    write_buf5 = bytearray([0x8C]) # write COMMAND (0x80) for reading ADC channel 0 (register 0x0C for LOW byte)
    data=port.write_read(write_buf5,2) # read LOW and HIGH bytes  
    light0=(data[1]<<8 | data[0]) #create a 16 bit value combining LOW (data[0]) and HIGH (data[1]) parts

    write_buf6 = bytearray([0x8E]) # write COMMAND for reading ADC channel 1 (register 0x0E)
    data=port.write_read(write_buf6,2)
    light1=(data[1]<<8 | data[0])

    print('ADC ch0 value= %d --- ADC ch1 value=%d' %(light0, light1))
    '''
    text = "hello"
    port.write(bytearray(text.encode()), ofs=0, timeout=0)

    sleep(2000)