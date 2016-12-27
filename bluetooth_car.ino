#include <SoftwareSerial.h>
#define m11 2     
#define m12 3     
#define m21 4     
#define m22 5 
#define ena 7
#define light 9

boolean sm11 = false;
boolean sm12 = false;
boolean sm21 = false;
boolean sm22 = false;
boolean rev = false;

int carspeed = 0;      //0-200,+-20
int carsteer = 5;     //0-4,5,5-10,+-1
int carrotatesteer = 5;
int i = 0;

/*protpcols for recieving command
navigation     :    #direction    :  #f, #b, #r, #l
command        :    !command     :  !fl,!fr,!bl,!br,!zsd,!zsr, !lfr          
*/

SoftwareSerial BTSerial(10, 11); // RX | TX
String instream;

void setup()
{
  Serial.begin(9600);
  Serial.println("Enter BTcar..V2");
  BTSerial.begin(38400);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(7, OUTPUT);
  digitalWrite(2,LOW);
  digitalWrite(3,LOW);
  digitalWrite(4,LOW);
  digitalWrite(5,LOW);
  digitalWrite(7,LOW);
}

void loop()
{
  if(i >= 200)
  i=0;
  if(carspeed == 0 || i > carspeed )
  {fstop();
  delayMicroseconds(250);
  }
  else if(i<carspeed && rev == 0 && carrotatesteer == 5)
  {fmove(carsteer);
  }
  else if(i<carspeed && rev == 1 && carrotatesteer == 5)
  {bmove(carsteer);
  }
  
  else if(carrotatesteer == 0 && rev == 0)
  fmove(0);
  else if(carrotatesteer == 1 && rev == 0)
  fmove(10);
  else if(carrotatesteer == 0 && rev == 1)
  bmove(0);
  else if(carrotatesteer == 1 && rev == 1)
  bmove(10);
  
  i+=10;
 

  //  Keep reading from HC-05 and send to Arduino Serial Monitor
  if (BTSerial.available())
  {
    instream = "";
    while (BTSerial.available())
    instream += char(BTSerial.read());
    Serial.println(instream);
    BTSerial.flush();
    if (instream[0] == '#')
    {      if (instream == "#r")
           carsteer+=1;
      else if (instream == "#l")
           carsteer-=1;
      else if (instream == "#f")
           carspeed+=20;
      else if (instream == "#b")
           carspeed-=20;
    }

    else if (instream[0] == '!')
    { if (instream == "!fl")
      {carrotatesteer = 0;
       rev = 0;
      }
      else if (instream == "!fr")
      {carrotatesteer = 10;
       rev = 0;
      }
      else if (instream == "!bl")
      {carrotatesteer = 0;
       rev = 1;
      }
      else if (instream == "!br")
      {carrotatesteer = 10;
       rev = 1;
      }
      else if (instream == "!zsd")
      { carspeed = 0; 
      }
      else if (instream == "!zsr")
      { carsteer = 5;
      }    
   }
}
carspeed = constrain(carspeed,0,200);
carsteer = constrain(carsteer,0,10);
analogWrite(light,carspeed);
delay(1);
}



void fstop()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,LOW);  
  digitalWrite(m12,LOW);  
  digitalWrite(m21,LOW);  
  digitalWrite(m22,LOW);
  digitalWrite(ena,HIGH);    
}

void fleft()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,LOW);  
  digitalWrite(m12,LOW);  
  digitalWrite(m21,HIGH);  
  digitalWrite(m22,LOW);
  digitalWrite(ena,HIGH);    
}

void bleft()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,LOW);  
  digitalWrite(m12,LOW);  
  digitalWrite(m21,LOW);  
  digitalWrite(m22,HIGH);
  digitalWrite(ena,HIGH);    
}

void fright()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,HIGH);  
  digitalWrite(m12,LOW);  
  digitalWrite(m21,LOW);  
  digitalWrite(m22,LOW);
  digitalWrite(ena,HIGH);    
}

void bright()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,LOW);  
  digitalWrite(m12,HIGH);  
  digitalWrite(m21,LOW);  
  digitalWrite(m22,LOW);
  digitalWrite(ena,HIGH);    
}

void ff()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,HIGH);  
  digitalWrite(m12,LOW);  
  digitalWrite(m21,HIGH);  
  digitalWrite(m22,LOW);
  digitalWrite(ena,HIGH);    
}

void bb()
{
  digitalWrite(ena,LOW);  
  digitalWrite(m11,LOW);  
  digitalWrite(m12,HIGH);  
  digitalWrite(m21,LOW);  
  digitalWrite(m22,HIGH);
  digitalWrite(ena,HIGH);    
}


void fmove(int x)
{
for(int i = 0 ; i < x ; i++)
   {fleft(); 
    delayMicroseconds(200); }
for(int i = 10 ; i > x ; i--)
   {fright();
   delayMicroseconds(200); }
}

void bmove(int x)
{
for(int i = 0 ; i < x ; i++)
   {bleft(); 
    delayMicroseconds(200); }
for(int i = 10 ; i > x ; i--)
   {bright();
   delayMicroseconds(200); }
}

