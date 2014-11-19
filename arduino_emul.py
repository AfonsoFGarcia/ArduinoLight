#!/usr/bin/env python

import socket
from thread import *

MY_TCP_IP = ''
MY_TCP_PORT = 5001
BUFFER_SIZE = 3

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((MY_TCP_IP, MY_TCP_PORT))
s.listen(1)

def recvfromclient(conn, addr):
	while True:
		data = conn.recv(BUFFER_SIZE)
		if not data: break
		print 'Received', data, 'from', addr
		VAL = input('Enter the light value: ')
		conn.send(str(VAL))
		break
	conn.close()

while 1:
	conn, addr = s.accept()
	recvfromclient(conn,addr)

s.close()
