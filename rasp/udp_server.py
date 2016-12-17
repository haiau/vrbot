import base64
import socket
from time import sleep
import cv2

udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
listen_addr = ("", 40000)
udp_socket.bind(listen_addr)
capture = cv2.VideoCapture(0)
while True:
    sleep(0.04)
    data = ""
    data, address = udp_socket.recvfrom(1024)
    print data.strip(), address

    if (data == "s"):
        capture.release()
        udp_socket.sendto("NULL", address)
    # SEND FRAME AS BASE64
    if (data == "c"): # Password or token
        frame = capture.read()[1]
        cnt = cv2.imencode('.jpg', frame)[1]
        b64 = base64.encodestring(cnt)
        print b64
        print "LEN: "+ str(len(b64))
        n = 1024 # max length buffer

        udp_socket.sendto('START', address)
        for i in range(0, len(b64), n):
            substring = b64[i:i+n]
            udp_socket.sendto(substring, address)
        udp_socket.sendto('FINISH', address)