package it.unisa.passchain;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MyCallback implements MqttCallback
{

 public void connectionLost(Throwable arg0) {
  // TODO Auto-generated method stub
  
 }

 public void deliveryComplete(IMqttDeliveryToken arg0) {
  // TODO Auto-generated method stub
  
 }

 public void messageArrived(String topic, MqttMessage message) throws Exception {
	 
	 //App.myClient.publish("noification", message.getPayload(), 0, false);
	 System.out.println("Topic: " + topic + "      Messaggio: " + message);
  
 }
 
}