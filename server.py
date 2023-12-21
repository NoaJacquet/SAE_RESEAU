#!/usr/bin/python3
import socket

class Server:
    def __init__(self):
        self.counter = 0

    def mainServer(self, port):
        sock = socket.socket()
        sock.bind(("0.0.0.0", port))
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        sock.listen(10)
        while True:
            cli, addr = sock.accept()
            sess = Session(self, cli)
            sess.mainSession()

class Session:
    def __init__(self, server, sock):
        self.server = server
        self.socket = sock
        self.file = sock.makefile(mode="rw")

    def mainSession(self):
        while True:
            line = self.file.readline().strip()
            if line == "quit":
                self.file.write("la session va s'eteindre \n")
                self.file.flush()
                break

            elif line=="incr":
                self.server.counter +=1
                self.file.write("le compteur est maintenant à : "+str(self.server.counter)+"\n")
                self.file.flush()

            elif line=="decr":
                self.server.counter -=1
                self.file.write("le compteur est maintenant à : "+str(self.server.counter)+"\n")
                self.file.flush()

            elif line=="get":
                self.file.write("le compteur est maintenant à : "+str(self.server.counter)+"\n")
                self.file.flush()

            elif "add" in line:

                self.server.counter += int(line.split()[1])
                self.file.write("le compteur est maintenant à : "+str(self.server.counter)+"\n")
                self.file.flush()

            else:
                self.file.write("j'ai bien reçu ton message : "+line+"\n")
                self.file.flush()
        self.file.close()
        self.socket.shutdown(socket.SHUT_RDWR)
        self.socket.close()

Server().mainServer(5557)
