from crypto import encode, decode

# run the decode library to get our secret back

username = "passwordDiGennaro"
salt = "2005202209"

secure_username = encode(username, salt)

print("-"*50)
print("Encrypted string")
print(secure_username)
print("-"*50)


plain_username = decode(p_username, salt)

print("-"*50)
print("Decrypted string")
print(plain_username)
print("-"*50)