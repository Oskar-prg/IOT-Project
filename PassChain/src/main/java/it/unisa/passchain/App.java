package it.unisa.passchain;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttTopic;
import java.util.Scanner;


public class App 
{
    public static MqttAsyncClient myClient;
    static String topic_sub = "ESPcredentials";
    static String topic_pub = "APPcredentials";

 public static void main( String[] args ) throws MqttException, UnknownHostException {

     //myClient = new MqttAsyncClient("tcp://192.168.1.106:1883", UUID.randomUUID().toString());
     myClient = new MqttAsyncClient("tcp://" + InetAddress.getLocalHost().getHostAddress() + ":1883",
             UUID.randomUUID().toString());

     MyCallback myCallback = new MyCallback();
     myClient.setCallback(myCallback);
     
     IMqttToken token = myClient.connect();
     token.waitForCompletion();
    	 
     System.out.println("Connected to the broker !!!");
     System.out.println("");
     
     
     myClient.subscribe(topic_sub, 0);
     
	 MqttMessage message = new MqttMessage("Ecco le password".getBytes());
	 
	 int qos = 0;
	 message.setQos(qos);     //sets qos level 1
	 message.setRetained(false); //sets retained message 
	 
	 Scanner scan = new Scanner(System.in);
	 System.out.println("Dai il via: ");
 	 String parola = scan.next(); 
 	 
	 if(parola.equals("ora"))
		 myClient.publish(topic_pub, message);    // publishes the message to the topic(test/topic)
	 
	 scan.close();
 
    }
}
