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

public class ControllerUpdate implements Initializable {

    @FXML
    private TextField oldName, webName, username, password;

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
        Design.setCredentials(oldName, webName, username, password);
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
    private void updateCredential() throws MqttException {
        if (oldName.getText() != null && !oldName.getText().isBlank())
            if (webName.getText() != null && !webName.getText().isBlank())
                if (password.getText() != null && !password.getText().isBlank())
                    if (username.getText() != null && !username.getText().isBlank()) {
                        errorMsg.setOpacity(0);
                        CredentialsList credentialsList = new CredentialsList();
                        int pos = credentialsList.searchCredential(oldName.getText());

                        if (pos != -1){
                            Credential credential = new Credential(webName.getText(), username.getText(), password.getText());
                            credentialsList.setList(pos, credential);
                            Design.fillTextArea(txtArea);

                            MQTT_comunication.publish("01" + oldName.getText() + ","
                                    + webName.getText() + "," + Crypto.encode(username.getText(), "2005202209") + "," +
                                    Crypto.encode(password.getText(), "2005202209"));

                            errorMsg.setOpacity(0);
                        } else {
                            errorMsg.setText("Credenziale non trovata.");
                            errorMsg.setOpacity(1);
                        }
                        return;
                    }
        errorMsg.setText("Riempi tutti i campi.");
        errorMsg.setOpacity(1);
    }
}
