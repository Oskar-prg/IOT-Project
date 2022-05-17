package it.unisa.passchain.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import it.unisa.passchain.MyCallback;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTT_comunication {
    private static MqttAsyncClient myClient;
    private static String topic_sub = "ESPcredentials";
    private static String topic_pub = "APPcredentials";

    public static void connect() throws MqttException, UnknownHostException {
        myClient = new MqttAsyncClient("tcp://" + InetAddress.getLocalHost().getHostAddress() + ":1883",
                UUID.randomUUID().toString());

        MyCallback myCallback = new MyCallback();
        myClient.setCallback(myCallback);

        IMqttToken token = myClient.connect();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (myClient.isConnected()){
            token.waitForCompletion();
            System.out.println("Connected to the broker !\n");

            MqttMessage message = new MqttMessage("reconnect".getBytes());
            int qos = 0;
            message.setQos(qos);
            message.setRetained(false);
            myClient.publish(topic_pub, message);
            myClient.subscribe(topic_sub, 0);
        }
    }

    public static boolean isConnected(){
        return myClient.isConnected();
    }

    public static void publish(String msg) throws MqttException {
        MqttMessage message = null;
        message = new MqttMessage(msg.getBytes(StandardCharsets.UTF_8));
        int qos = 0;
        message.setQos(qos);
        message.setRetained(false);
        myClient.publish(topic_pub, message);
    }
}
