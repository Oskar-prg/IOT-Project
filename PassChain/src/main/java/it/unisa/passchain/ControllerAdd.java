package it.unisa.passchain;

import it.unisa.passchain.utils.*;
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

public class ControllerAdd implements Initializable {

    @FXML
    private TextField webName, username, password;

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
        Design.setCredentials(null, webName, username, password);
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
    private void addCredential() throws MqttException {
        if (webName.getText() != null && !webName.getText().isBlank())
            if (password.getText() != null && !password.getText().isBlank())
                if (username.getText() != null && !username.getText().isBlank()) {
                    Credential credential = new Credential(webName.getText(), username.getText(), password.getText());
                    CredentialsList credentialsList = new CredentialsList();
                    credentialsList.addCredential(credential);

                    Design.fillTextArea(txtArea);
                    MQTT_comunication.publish("00" + credential.getName() + ","
                            + Crypto.encode(credential.getUsername(), "2005202209") + "," +
                            Crypto.encode(credential.getPassword(), "2005202209"));

                    errorMsg.setOpacity(0);
                    return;
                }
        errorMsg.setOpacity(1);
    }
}
