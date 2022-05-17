
import time
import machine
from machine import SoftSPI, Pin
from hid_services import Keyboard

class Device:
    def __init__(self):
        # Define state
        self.key0 = 0x00
        self.key1 = 0x00
        self.key2 = 0x00
        self.key3 = 0x00

        # Create our device
        self.keyboard = Keyboard("KeyboardPassChain")
        # Set a callback function to catch changes of device state
        self.keyboard.set_state_change_callback(self.keyboard_state_callback)
        # Start our device
        self.keyboard.start()

    # Function that catches device status events
    def keyboard_state_callback(self):
        if self.keyboard.get_state() is Keyboard.DEVICE_IDLE:
            return
        elif self.keyboard.get_state() is Keyboard.DEVICE_ADVERTISING:
            return
        elif self.keyboard.get_state() is Keyboard.DEVICE_CONNECTED:
            return
        else:
            return

    def keyboard_event_callback(self, bytes):
        print("Keyboard state callback with bytes: ", bytes)

    def advertise(self):
        self.keyboard.start_advertising()

    def stop_advertise(self):
        self.keyboard.stop_advertising()

    # Main loop
    def start(self):
            if self.keyboard.get_state() is Keyboard.DEVICE_CONNECTED:
                self.keyboard.notify_hid_report()
                
            elif self.keyboard.get_state() is Keyboard.DEVICE_IDLE:
                self.keyboard.start_advertising()
                i = 10
                while i > 0 and self.keyboard.get_state() is Keyboard.DEVICE_ADVERTISING:
                    time.sleep(3)
                    i -= 1
                if self.keyboard.get_state() is Keyboard.DEVICE_ADVERTISING:
                        self.keyboard.stop_advertising()

            if self.keyboard.get_state() is Keyboard.DEVICE_CONNECTED:
                time.sleep_ms(20)
            else:
                time.sleep(2)

    def send_char(self, char):
        if char == " ":
            mod = 0
            code = 0x2C
        elif ord("a") <= ord(char) <= ord("z"):
            mod = 0
            code = 0x04 + ord(char) - ord("a")
        elif ord("A") <= ord(char) <= ord("Z"):
            mod = 1
            code = 0x04 + ord(char) - ord("A")
        elif ord("0") <= ord(char) <= ord("9"):
            mod = 2
            code = 0x04 + ord(char) - ord("0")
        elif ord("!") <= ord(char) <= ord("/"):
            print("Ecco il char: "+ str(ord("/")))
            mod = 0
            code = (ord(char) - ord("!"))
        else:
            assert 0

        self.keyboard.set_keys(code)
        self.keyboard.set_modifiers(left_shift=mod)
        self.keyboard.notify_hid_report()
        time.sleep_ms(50)

        self.keyboard.set_keys()
        self.keyboard.set_modifiers()
        self.keyboard.notify_hid_report()
        time.sleep_ms(2)


    def send_string(self, st):
        for c in st:
            self.send_char(c)

    # Only for test
    def stop(self):
        self.keyboard.stop()
        
    def isConnected(self):
        return self.keyboard.get_state() is Keyboard.DEVICE_CONNECTED
    