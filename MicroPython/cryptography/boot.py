# Complete project details at https://RandomNerdTutorials.com

import time
from umqttsimple import MQTTClient
import ubinascii
import machine
import micropython
import network
import esp
esp.osdebug(None)
import gc
gc.collect()

ssid = 'iPhone di Gennaro'
password = 'connettitipls'
mqtt_server = '172.20.10.10'

# ssid = 'TIM-20128173'
# password = 'KinderCards'
# mqtt_server = '192.168.1.140'
# ssid = 'FASTWEB-DPDUHC'
# password = 'J3HP2TESKF'
# mqtt_server = '192.168.1.58'
#EXAMPLE IP ADDRESS
#mqtt_server = '192.168.1.144'
client_id = ubinascii.hexlify(machine.unique_id())
topic_sub = b'APPcredentials'
topic_pub = b'ESPcredentials'

last_message = 0
message_interval = 5
counter = 0

station = network.WLAN(network.STA_IF)

station.active(True)
station.connect(ssid, password)

while station.isconnected() == False:
  pass

print('Connection successful')
print(station.ifconfig())