module ContactListProject {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.xml;

    opens sample;   // name of the folder in src
    opens sample.datamodel;
}