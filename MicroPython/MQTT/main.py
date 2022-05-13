import machine
from machine import Pin, SoftI2C, PWM
import time
from time import sleep
from simpleKeyboard import Device
from binascii import hexlify

#I2C_ADDR = 0x27
totalRows = 2
totalColumns = 16

#i2c = SoftI2C(scl=Pin(22), sda=Pin(21), freq=10000)     #initializing the I2C method for ESP32
#lcd = I2cLcd(i2c, I2C_ADDR, totalRows, totalColumns)
d = Device()

#Keypad setup
KEY_UP   = const(0)
KEY_DOWN = const(1)

keys = [['1', '2', '3', 'A'], ['4', '5', '6', 'B'], ['7', '8', '9', 'C'], ['*', '0', '#', 'D']]

# Pin names for Pico
rows = [13,12,14,27]
cols = [26,25,33,32]

# set pins for rows as outputs
row_pins = [Pin(pin_name, mode=Pin.OUT) for pin_name in rows]

# set pins for cols as inputs
col_pins = [Pin(pin_name, mode=Pin.IN, pull=Pin.PULL_DOWN) for pin_name in cols]

#Buzzer setup
buzzer = PWM(Pin(19), freq=440, duty=512)


# Complete project details at https://RandomNerdTutorials.com

#quiiiiiiiiiiiiiiiiii
def sub_cb(topic, msg):
    print(msg + "1")
    global last_message
    print((topic, msg))
    last_message = msg
    if topic == b'notification' and msg == b'received':
        print('ESP received hello message')

def connect_and_subscribe():
  global client_id, mqtt_server, topic_sub
  client = MQTTClient(client_id, mqtt_server)
  client.set_callback(sub_cb)
  client.connect()
  client.subscribe(topic_sub)
  print('Connected to %s MQTT broker, subscribed to %s topic' % (mqtt_server, topic_sub))
  return client

def restart_and_reconnect():
  print('Failed to connect to MQTT broker. Reconnecting...')
  time.sleep(10)
  machine.reset()

try:
  client = connect_and_subscribe()
except OSError as e:
  restart_and_reconnect()

while True:
  try:
    new_msg = client.check_msg()
    
    sleep(1)
        
#     if (time.time() - last_message) > message_interval:
#       msg = b'Hello #%d' % counter
#       client.publish(topic_pub, msg)
#       last_message = time.time()
#       counter += 1
  except OSError as e:
    restart_and_reconnect()


def mainLoop():
    fromConnecting = True
    askPin = True
    #lcd.putstr("Welcome to \nPassChain")
    init()
    sleep(2)
    while True:
        if d.isConnected():
            if fromConnecting:
                connectedBle()
                fromConnecting = False
                sleep(2)
                #lcd.clear()
            
            if askPin:
                #lcd.clear()
                #lcd.putstr("Inserisci pin:\n")
                askPin = False
            
            for row in range(4):
                for col in range(4):
                    key = scan(row, col)
                    if key == KEY_DOWN:
                        print("Key Pressed", keys[row][col])
                        last_key_press = keys[row][col]
                        #lcd.putstr(str(last_key_press))
                        time.sleep_ms(500)
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
            
'''      
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
'''
#Keypad functions

def init():
    for row in range(0,4):
        for col in range(0,4):
            row_pins[row].value(0)

def scan(row, col):
    """ scan the keypad """

    # set the current column to high
    row_pins[row].value(1)
    key = None

    # check for keypressed events
    if col_pins[col].value() == KEY_DOWN:
        key = KEY_DOWN
    if col_pins[col].value() == KEY_UP:
        key = KEY_UP
    row_pins[row].value(0)

    # return the key state
    return key

if __name__ == "__main__":
    mainLoop()
