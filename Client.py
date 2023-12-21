#!/usr/bin/python3
import socket

def client(host, port):
    sock = socket.socket()
    sock.connect((host, port))
    f = sock.makefile(mode="rw")
    while True:
        chaine = input("donne moi une chaine a envoyé au serveur : ")
        f.write(chaine+"\n")
        f.flush()
        print(f.readline(), end="")
        if chaine == "quit":
            print(f.readline(), end="")
            f.close()
            sock.shutdown(socket.SHUT_RDWR)
            sock.close()
            break

client("localhost", 5557)
