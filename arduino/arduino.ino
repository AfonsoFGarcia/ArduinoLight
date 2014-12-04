int led = 11;
int signalLed = 11;

int lightStatus = 0;

char incomingByte = 0;
char message[5];
byte index = 0;

void setup() {
  Serial.begin(9600);
  pinMode(led, OUTPUT);
  pinMode(signalLed, OUTPUT);
  setupXbee();
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
  
  Serial.print("ATDL1234\r");
  
  while (thisByte != '\r') {
    if(Serial.available()) {
      thisByte = Serial.read();
    }
  }
  
  thisByte = 0;
  
  Serial.print("ATMY1235\r");
  
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

int readMessage() {
  if(Serial.available()) {
    delay(10); // Tweak it to not respond before receiving all bytes
    while(Serial.available() > 0) {
      if(index < 4) {
        incomingByte = Serial.read();
        message[index] = incomingByte;
        index++;
        message[index] = '\0';
      }
    }
    return 1;
  }
  return 0;
}

void cleanMessage() {
  for(index = 0; index < 5; index++)
    message[index] = 0;
  index = 0;
}

void loop() {
  if(readMessage()) {
    if(strcmp(message,"REQ")  == 0) {
      Serial.print(analogRead(A0));
    } else if (strcmp(message,"ON")  == 0) {
      digitalWrite(led, HIGH);
      lightStatus = 1;
      Serial.print("OK");
    } else if (strcmp(message,"OFF")  == 0) {
      digitalWrite(led, LOW);
      lightStatus = 0;
      Serial.print("OK");
    } else if (strcmp(message,"STS") == 0) {
      Serial.print(lightStatus);
    } else if(message[0] != '\0') {
      Serial.print("NOK");
    }
    cleanMessage();
  }
}

