import base64
import socket, ssl
import cv2
import serial
import json

#SERIAL
ser = serial.Serial(
    port='/dev/ttyACM0',
    baudrate = 9600,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS,
    timeout=1
)
counter=0


#SSL
addr = '192.168.1.200'
port = 10023
bindsocket = socket.socket()
bindsocket.bind((addr, port))
bindsocket.listen(5)
print "Start server %s:%s" % (addr, port)

def do_something(connstream, data):
    print "do_something:", data
    return False

def deal_with_client(connstream):
    while True:
        data = connstream.recv(1024)
        if data:
            #PARSE JSON
            parsed_json = json.loads(data)
            print parsed_json['user']
            print parsed_json['pass']
            print parsed_json['cmd']

            ser.write(str(parsed_json['cmd']))

#            connstream.write("OK\n")

        else:
            break

while True:
    newsocket, fromaddr = bindsocket.accept()
    connstream = ssl.wrap_socket(newsocket,
                                server_side=True,
                                certfile="/home/haiau/projects/vrbot/android/server.pem",
                                keyfile="/home/haiau/projects/vrbot/android/server.key")
    try:
        deal_with_client(connstream)
    finally:
        connstream.shutdown(socket.SHUT_RDWR)
        connstream.close()
        print "Shutdown server."
