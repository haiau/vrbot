import socket
from time import sleep
udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
address = ("", 40000)
while True:
    sleep(0.04) # Sleep 40ms (~24 FPS)
    #data = raw_input("Data: ")
    udp_socket.sendto("c", address)

    data, address = udp_socket.recvfrom(1024)
    print data
    b64 = ""
    if (data == "START"):
        b64 = ""
        while True:
            data, address = udp_socket.recvfrom(1024)
            if (data == "FINISH"):
                break
            b64 = b64 + data
        print b64
        print "LEN: " + str(len(b64))
