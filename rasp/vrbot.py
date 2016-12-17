#!/usr/bin/python

import socket
import sys

s = socket.socket()
#host = socket.gethostname()
#host = "127.0.0.1"
host = "192.168.1.21"
port = 30000

s.bind((host, port))
s.listen(5)
print 'Starting socket in %s:%s' % (host, port)
while True:
    connect, address = s.accept()
    print "New connection from: ", address
    connect.send("Hello from server")
    cmd = ""
    while cmd != "quit":
        cmd = (connect.recv(1024)).strip()
        print "Command: ", cmd
        if (cmd == "quit" or cmd == ""):
            print "Disconnect from ", address
            #connect.close()
            break
s.close()
