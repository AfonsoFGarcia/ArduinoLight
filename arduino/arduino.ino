int led = 0;

char incomingByte = 0;
char message[5];
int index = 0;

void setup() {
  Serial.begin(9600);
  pinMode(led, OUTPUT);
}

void readMessage() {
  while(Serial.available() > 0) {
    incomingByte = Serial.read();
    message[index++] = incomingByte;
    message[index] = '\0';
  }
}

void cleanMessage() {
  message[(index = 0)] = '\0';
}

void loop() {
  readMessage();
  if(strcmp(message,"REQ")  == 0) {
    Serial.print(analogRead(A0));
  } else if (strcmp(message,"ON")  == 0) {
    digitalWrite(led, HIGH);
    Serial.print("OK");
  } else if (strcmp(message,"OFF")  == 0) {
    digitalWrite(led, LOW);
    Serial.print("OK");
  } else if(message[0] != '\0') {
    Serial.print("NOK");
  }
  cleanMessage();
}

