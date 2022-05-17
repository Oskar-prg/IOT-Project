package it.unisa.passchain;

import it.unisa.passchain.utils.MQTT_comunication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

public class PassChain extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/authentication.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PassChain IoT");
        stage.setScene(scene);
        stage.setResizable(false);
        InputStream in = getClass().getResourceAsStream("/it/unisa/passchain/assets/icon.png");
        stage.getIcons().add(new Image(in));
        stage.show();
    }

    public static void main(String[] args) throws UnknownHostException, MqttException {
        MQTT_comunication.connect();
        launch();
    }
}