package it.unisa.passchain;

import it.unisa.passchain.utils.Credential;
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

    return object.getString("pin");
  }

  public static Credential getCredential(int pos){
    if (object.isNull("credentials") || object.isNull("pin"))
      return null;

    Credential credential = new Credential();
    credential.setName(object.getJSONArray("credentials").getJSONObject(pos).getString("name"));
    credential.setUsername(object.getJSONArray("credentials").getJSONObject(pos).getString("username"));
    credential.setPassword(object.getJSONArray("credentials").getJSONObject(pos).getString("password"));
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
    newObj.put("username", credential.getUsername());
    newObj.put("password", credential.getPassword());

    object.getJSONArray("credentials").put(newObj);
    System.out.println(object.getJSONArray("credentials"));
  }

  public static void setCredential(int pos, Credential credential){
    object.getJSONArray("credentials").getJSONObject(pos).put("name", credential.getName());
    object.getJSONArray("credentials").getJSONObject(pos).put("username", credential.getUsername());
    object.getJSONArray("credentials").getJSONObject(pos).put("password", credential.getPassword());
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
        object.put("pin", newpin);
        return true;
      }
    }
    return false;
  }

}