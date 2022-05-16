package it.unisa.passchain;

import it.unisa.passchain.utils.Design;
import it.unisa.passchain.utils.MQTT_comunication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPinCode implements Initializable {
    private final char[] chart = {'D', 'E', 'F', 'G', 'H', 'I', 'L', 'M', 'N', 'O', 'P',
    'Q', 'R', 'S', 'T', 'U', 'V', 'Z', 'J', 'K', 'Y', 'X', 'W', 'd', 'e', 'f', 'g', 'h',
    'i', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'z', 'j', 'k', 'y', 'x',
    'w', '|', '\'', '\\', '!', '?', 'ì', '(', '^', '(', '"', '£', '$', '%', '&', '/', '=',
    '#', 'à', '+', '*', '-', 'è', 'é', 'ç', 'ò', '@', '°', 'ù', '§', ':', '.', ',', ';',
    '_'};
    @FXML
    private TextField oldpin, newpin;

    @FXML
    private TextArea txtArea;

    @FXML
    private AnchorPane panel1, panel2, rootPane;

    @FXML
    private ImageView menu;

    @FXML
    private Text errorMsg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Design.setSidebar(panel1, panel2, menu);
        Design.setCredentials(oldpin, newpin, null, null);
        Design.textAreaNotEditable(txtArea);
        Design.fillTextArea(txtArea);
        errorMsg.setOpacity(0);
    }

    @FXML
    private void loadHomePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/homepage.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void loadAddPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/add.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void loadUpdatePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/update.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void loadRemovePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/remove.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void loadPinCodePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/pinCode.fxml"));
        AnchorPane pane = fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void updatePin() throws MqttException {
        if (oldpin.getText() != null && !oldpin.getText().isBlank())
            if (newpin.getText() != null && !newpin.getText().isBlank()){
                errorMsg.setOpacity(0);
                if (oldpin.getText().length() == 10 && !oldpin.getText().isBlank()) {
                    for (char c : chart) {
                        if (oldpin.getText().contains(String.valueOf(c))) {
                            errorMsg.setOpacity(1);
                            return;
                        }
                    }
                    MyCallback.updatePin(oldpin.getText(), newpin.getText());
                    MQTT_comunication.publish("03" + newpin.getText());
                }
                else
                    errorMsg.setOpacity(1);
            }
        errorMsg.setOpacity(1);
    }
}
