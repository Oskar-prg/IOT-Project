from machine import Pin
from time import sleep

buzzer = Pin(16,Pin.OUT)
for d in range(5):
    buzzer.on()
    sleep(0.5)
    buzzer.off()
    sleep(0.5)