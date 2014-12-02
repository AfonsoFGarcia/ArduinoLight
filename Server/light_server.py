#!/usr/bin/env python

import socket
import sys
from thread import *
from time import sleep

if len(sys.argv) < 2:
	print 'Usage python light_server.py [LIGHT THRESHOLD]'
	sys.exit(-1)

MY_TCP_IP = ''
MY_TCP_PORT = 5000
BUFFER_SIZE = 8

ARD_TCP_IP = '192.168.1.10'
ARD_TCP_PORT = 5001

THRS = int(sys.argv[1])

def writeLog(message):
	log = open('interactions.log', 'a')
	log.write(message)
	log.close()

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

def blink(times):
	for x in range(0, times):
		setlight('ON')
		sleep(0.2)
		setlight('OFF')
		sleep(0.2)

def getval(val):
	temp = val
	if temp > 999:
		temp = 999
	u = temp%10
	temp = (temp-u)/10
	d = temp%10
	c = (temp-d)/10
	return (c,d,u)

def guessval(guess):
	writeLog('[GAM-G] Value for guess is '+str(guess)+'\n')
	global LHT_V
	blinks = 0
	for x in range (0,3):
		if LHT_V[x] == guess[x]:
			blinks = blinks + 1
	blink(blinks)

def recvfromclient(conn, addr):
	global THRS
	global LHT_V
	while True:
		data = conn.recv(BUFFER_SIZE)
		if not data: break
		tokens = data.split()
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
		elif data == 'RES':
			LHT_V = getval(getlight())
			writeLog('\n[GAM-V] Value for game is '+str(LHT_V)+'\n')
			conn.send('OK\n')
		elif tokens[0] == 'G':
			guessval((int(tokens[1]), int(tokens[2]), int(tokens[3])))
			conn.send('OK\n')
		else:
			writeLog('[CLI-S] Sending NOK to client\n');
			conn.send('NOK\n')
		break
	conn.close()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((MY_TCP_IP, MY_TCP_PORT))
s.listen(1)

writeLog('\n[INF-S] Light Server started on port 5000\n\n')

LHT_V = getval(getlight())
writeLog('\n[GAM-V] Value for game is '+str(LHT_V)+'\n')

while 1:
	conn, addr = s.accept()
	recvfromclient(conn,addr)
