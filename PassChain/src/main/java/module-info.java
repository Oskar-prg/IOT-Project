module it.unisa.passchain {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires org.eclipse.paho.client.mqttv3;


    opens it.unisa.passchain to javafx.fxml;
    exports it.unisa.passchain;
    exports it.unisa.passchain.utils;
    opens it.unisa.passchain.utils to javafx.fxml;
}