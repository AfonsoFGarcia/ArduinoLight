#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>

#include <SPI.h>

char incomingByte = 0;
char message[6];
int index = 0;

byte mac[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
byte ip[] = {192, 168, 0, 10}

EthernetServer server(5001);

void setup() {
  Serial.begin(9600);
  Ethernet.begin(mac, ip);
  server.begin();
}

void readMessage(EthernetClient client) {
  while(client.available() > 0) {
    incomingByte = client.read();
    message[index++] = incomingByte;
    message[index] = '\0';
  }
}

void relayMessage(char* message, EthernetClient client) {
  Serial.print(message);
  while (Serial.available() == 0) {}
  while(Serial.available() > 0) {
    incomingByte = Serial.read();
    message[index++] = incomingByte;
    message[index] = '\0';
  }
  client.print(message);
  cleanMessage();
}

void cleanMessage() {
  message[(index = 0)] = '\0';
}

void loop() {
  EthernetClient client = server.available();
  
  if(client) {
    readMessage(client);
    if(strcmp(message,"REQ")  == 0) {
      relayMessage(message, client);
    } else if (strcmp(message,"ON")  == 0) {
      relayMessage(message, client);
    } else if (strcmp(message,"OFF")  == 0) {
      relayMessage(message, client);
    } else if(message[0] != '\0') {
      client.print("NOK");
    }
    cleanMessage();
  }
}

