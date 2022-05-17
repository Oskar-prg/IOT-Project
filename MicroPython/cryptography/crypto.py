
def convert(s):
    """converts a list into a string"""
    new = ""
    for x in s:
        new += x
    return new


def decode(encoded_text, salt):
    """decode the contents of encrypted text, using the salt"""
    plaintext = []
    count = 0
    for character in encoded_text:
        temp_string = ord(character) - ord(salt[count])
        if temp_string < 0:
            temp_string += 255
        
        plaintext.append(chr(temp_string))
        if count < len(salt)-1:
            count += 1
        else:
            count = 0
        
    return convert(plaintext)


def encode(plaintext, salt):
    """encodes plain text using a salt key"""
    secure_string = []
    count = 0
    for character in plaintext:
        num = ord(character) + ord(salt[count])
        if num > 255:
            num -= 255
        secure_string.append(chr(num))
        if count < len(salt)-1:
            count += 1
        else:
            count = 0
    return convert(secure_string)