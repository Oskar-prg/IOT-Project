package it.unisa.passchain;

import it.unisa.passchain.utils.Credential;
import it.unisa.passchain.utils.Crypto;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class MyCallback implements MqttCallback {

  private static JSONObject object = new JSONObject();

  public void connectionLost(Throwable arg0) {
   // TODO Auto-generated method stub
  }

  public void deliveryComplete(IMqttDeliveryToken arg0) {
   // TODO Auto-generated method stub
  }

  public void messageArrived(String topic, MqttMessage message) {
    object = new JSONObject(message.toString());
    System.out.println(object.getJSONArray("credentials"));
  }

  public static String getPin() {
    if (object.isNull("pin"))
      return null;

    return Crypto.decode(object.getString("pin"), "2005202209");
  }

  public static Credential getCredential(int pos){
    if (object.isNull("credentials") || object.isNull("pin"))
      return null;

    Credential credential = new Credential();
    credential.setName(Crypto.decode(object.getJSONArray("credentials").getJSONObject(pos).getString("name"), "2005202209"));
    credential.setUsername(Crypto.decode(object.getJSONArray("credentials").getJSONObject(pos).getString("username"), "2005202209"));
    credential.setPassword(Crypto.decode(object.getJSONArray("credentials").getJSONObject(pos).getString("password"), "2005202209"));
    return credential;
  }

  public static int getSizeJson(){
    if (object.isNull("credentials") || object.isNull("pin"))
      return 0;
    return object.getJSONArray("credentials").length();
  }

  public static void addCredential(Credential credential){
    JSONObject newObj = new JSONObject();
    newObj.put("name", credential.getName());
    newObj.put("username", Crypto.encode(credential.getUsername(), "2005202209"));
    newObj.put("password", Crypto.encode(credential.getPassword(), "2005202209"));

    object.getJSONArray("credentials").put(newObj);
    System.out.println(object.getJSONArray("credentials"));
  }

  public static void setCredential(int pos, Credential credential){
    object.getJSONArray("credentials").getJSONObject(pos).put("name", credential.getName());
    object.getJSONArray("credentials").getJSONObject(pos).put("username", Crypto.encode(credential.getUsername(), "2005202209"));
    object.getJSONArray("credentials").getJSONObject(pos).put("password", Crypto.encode(credential.getPassword(), "2005202209"));
    System.out.println(object.getJSONArray("credentials"));
  }

  public static void remove(int pos){
    object.getJSONArray("credentials").remove(pos);
    System.out.println(object.getJSONArray("credentials"));
  }

  public static boolean updatePin(String oldpin, String newpin){
    if (getPin() != null){
      if (oldpin.compareTo(getPin()) == 0){
        object.remove("pin");
        object.put("pin", Crypto.encode(newpin, "2005202209"));
        return true;
      }
    }
    return false;
  }
}