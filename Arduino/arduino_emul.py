#!/usr/bin/env python

import socket
import random
from thread import *

MY_TCP_IP = ''
MY_TCP_PORT = 5001
BUFFER_SIZE = 3

LIGHT = 'L'

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((MY_TCP_IP, MY_TCP_PORT))
s.listen(1)

def recvfromclient(conn, addr):
	global LIGHT
	while True:
		data = conn.recv(BUFFER_SIZE)
		if not data: break
		if data == 'REQ':
			conn.send(str(random.randint(1,1000)))
		elif data == 'ON':
			LIGHT = 'H'
			print "Light:", LIGHT
			conn.send('OK')
		elif data == 'OFF':
			LIGHT = 'L'
			print "Light:", LIGHT
			conn.send('OK')
		else:
			conn.send('NOK')
		break
	conn.close()

while 1:
	conn, addr = s.accept()
	recvfromclient(conn,addr)

s.close()
