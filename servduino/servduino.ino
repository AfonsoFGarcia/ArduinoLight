#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>

#include <SPI.h>

int signalLed = 9;

char incomingByte = 0;
char message[6];
byte index = 0;

byte mac[] = {0x90, 0xA2, 0xDA, 0x0D, 0x75, 0xF6};
byte ip[] = {192, 168, 0, 10};

EthernetServer server(5001);

void setup() {
  Serial.begin(9600);
  pinMode(signalLed, OUTPUT);
  setupXbee();
  Ethernet.begin(mac, ip);
  server.begin();
}

void setupXbee() {
  digitalWrite(signalLed, HIGH);
  
  delay(1200);
  Serial.print("+++");
  char thisByte = 0;
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
 
  delay(1200);
 
  Serial.print("ATRE\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
  
  Serial.print("ATDH0\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
  
  Serial.print("ATDL1235\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
  
  Serial.print("ATMY1234\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
  
  Serial.print("ATWR\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
  
  Serial.print("ATCN\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;

  digitalWrite(signalLed, LOW);
  delay(500);
  digitalWrite(signalLed, HIGH);
  delay(500);
  digitalWrite(signalLed, LOW);
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
  cleanMessage();
  digitalWrite(signalLed, HIGH);
  while (Serial.available() == 0) {
  }
  digitalWrite(signalLed, LOW);
  delay(10);
  while(Serial.available() > 0) {
    if(index < 6) {
      incomingByte = Serial.read();
      message[index] = incomingByte;
      index++;
      message[index] = '\0';
    }
  }
  client.print(message);
  client.stop();
  cleanMessage();
}

void cleanMessage() {
  for(index = 0; index < 5; index++)
    message[index] = 0;
  index = 0;
}

void loop() {
  EthernetClient client = server.available();
  
  if(client) {
    readMessage(client);
    if(message[0] != 0) {
      relayMessage(message, client);
    }
    cleanMessage();
  }
}

