
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

ssid = 'nome wifi'
password = 'password'
mqtt_server = 'IP Address'

#EXAMPLE IP ADDRESS
#ssid = 'iPhone di Gennaro'
#password = 'connettitipls'
#mqtt_server = '172.20.10.10'

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