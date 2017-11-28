const int analogInPinLeft = A0;
const int analogInPinCenter = A1;// Pressure Sensor connected to A0

int sensorValueLeft = 0;
int sensorValueCenter = 0;// value read from the pressure sensor via the amplifier stage
float outputValue = 0;      // value output to the Serial port

void setup() {
  // initialize serial communications at 9600 bps:
  Serial.begin(9600); 
  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  // read the analog in value:
  sensorValueLeft = analogRead(analogInPinLeft);
  sensorValueCenter = analogRead(analogInPinCenter);              

  // print the results to the serial monitor:
  Serial.print("sensor left = " );                       
  Serial.print(sensorValueLeft); 
  Serial.print(" sensor center = " ); 
  Serial.print(sensorValueCenter); 
  Serial.println();

  /*if(sensorValue!=1023)
  {
    digitalWrite(LED_BUILTIN, HIGH); 
  }
  else
  {
    digitalWrite(LED_BUILTIN, LOW); 
  }*/
       
  // wait 10 milliseconds before the next loop
  // for the analog-to-digital converter to settle
  // after the last reading:
  delay(1000);                     
}

