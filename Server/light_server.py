#!/usr/bin/env python

import socket
import sys
from thread import *

if len(sys.argv) < 2:
	print 'Usage python light_server.py [LIGHT THRESHOLD]'
	sys.exit(-1)

def writeLog(message):
	log = open('interactions.log', 'a')
	log.write(message)
	log.close()

MY_TCP_IP = ''
MY_TCP_PORT = 5000
BUFFER_SIZE = 4

ARD_TCP_IP = '192.168.1.10'
ARD_TCP_PORT = 5001

THRS = int(sys.argv[1])

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((MY_TCP_IP, MY_TCP_PORT))
s.listen(1)

writeLog('\n[INF-S] Light Server started on port 5000\n')

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
	writeLog('[ARD-S] Sending REQ to arduino\n')
	s.send('REQ')
	data = s.recv(BUFFER_SIZE)
	writeLog('[ARD-R] Received '+data+' from arduino\n')
	s.close()
	return num(data)

def setlight(val):
	global ARD_TCP_IP
	global ARD_TCP_PORT
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((ARD_TCP_IP, ARD_TCP_PORT))
	writeLog('[ARD-S] Sending '+val+' to arduino\n')
	s.send(val)
	data = s.recv(BUFFER_SIZE)
	writeLog('[ARD-R] Received '+data+' from arduino\n')
	s.close()
	return data

def recvfromclient(conn, addr):
	global THRS
	while True:
		data = conn.recv(BUFFER_SIZE)
		if not data: break
		data = data.rstrip()
		writeLog('\n[CLI-R] Received '+data+' from client\n')
		if data == 'OFF':
			data = setlight(data)
			writeLog('[CLI-S] Sending OK to client\n');
			conn.send('OK\n')
		elif data == 'ON':
			if getlight() < THRS:		
				data = setlight(data)
				writeLog('[CLI-S] Sending OK to client\n');	
				conn.send('OK\n')
			else:
				writeLog('[CLI-S] Sending NOP to client\n');
				conn.send('NOP\n')
		else:
			writeLog('[CLI-S] Sending NOK to client\n');
			conn.send('NOK\n')
		break
	conn.close()

while 1:
	conn, addr = s.accept()
	recvfromclient(conn,addr)
