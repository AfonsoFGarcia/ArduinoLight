int led = 13;

char incomingByte = 0;
char message[4];
int index = 0;

void setup() {
  Serial.begin(9600);
  pinMode(led, OUTPUT);
  randomSeed(analogRead(A0));
}

void readMessage() {
  while(Serial.available() > 0) {
    incomingByte = Serial.read();
    message[index++] = incomingByte;
    message[index] = '\0';
  }
}

void cleanMessage() {
  index = 0;
  message[0] = '\0';
}

void loop() {
  readMessage();
  if(strcmp(message,"REQ")  == 0) {
    Serial.println(random(0,1023));
  } else if (strcmp(message,"ON")  == 0) {
    Serial.println("OK");
  } else if (strcmp(message,"OFF")  == 0) {
    Serial.println("OK");
  } else if(message[0] != '\0') {
  Serial.println("NOK");
  }
  cleanMessage();

  /*int sensorValue = analogRead(A0);
    Serial.println(sensorValue);
    if(sensorValue > 800) {
      digitalWrite(led, HIGH);
    } else {
      digitalWrite(led, LOW);
    }
    delay(1000);*/
}

