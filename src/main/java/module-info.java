module com.example.mypuzzl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires json.simple;
    requires java.desktop;


    opens com.example.mypuzzl to javafx.fxml;
    exports com.example.mypuzzl;
    exports com.example.mypuzzl.sharedInterface;
    exports com.example.mypuzzl.client;
    exports com.example.mypuzzl.server;
}