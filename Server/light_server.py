#!/usr/bin/env python

import socket
import sys
from thread import *

if len(sys.argv) < 3:
	print 'Usage python server.py [ARDUINO IP] [LIGHT THRESHOLD]'
	sys.exit(-1)

MY_TCP_IP = ''
MY_TCP_PORT = 5000
BUFFER_SIZE = 3

ARD_TCP_IP = sys.argv[1]
ARD_TCP_PORT = 5001

THRS = int(sys.argv[2])

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((MY_TCP_IP, MY_TCP_PORT))
s.listen(1)

def num(s):
	try:
		return int(s)
	except ValueError:
		return float(s)

def getlight():
	global ARD_TCP_IP
	global ARD_TCP_PORT
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((ARD_TCP_IP, ARD_TCP_PORT))
	s.send('REQ')
	data = s.recv(10)
	s.close()
	return num(data)

def setlight(val):
	global ARD_TCP_IP
	global ARD_TCP_PORT
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((ARD_TCP_IP, ARD_TCP_PORT))
	s.send(val)
	data = s.recv(2)
	s.close()
	return data

def recvfromclient(conn, addr):
	global THRS
	while True:
		data = conn.recv(BUFFER_SIZE)
		if not data: break
		data = data.rstrip()
		if data == 'OFF':
			data = setlight(data);
			conn.send('OK\n')
		elif data == 'ON':
			if getlight() > THRS:		
				data = setlight(data)
				conn.send('OK\n')
			else:
				conn.send('NOP\n')
		else:
			conn.send('NOK\n')
		break
	conn.close()

while 1:
	conn, addr = s.accept()
	recvfromclient(conn,addr)
