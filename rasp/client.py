#!/usr/bin/python

import socket
import sys

s = socket.socket()
host = sys.argv[1]
port = int(sys.argv[2])
print "Connect: %s:%s" % (host, port)
s.connect((host, port))
print s.recv(1024)
cmd = ""
while cmd != "quit":
    cmd = raw_input("Command: ")
    s.send(cmd)
    s.close
