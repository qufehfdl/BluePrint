module com.rilke.blueprint {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.rilke.blueprint to javafx.fxml;
    exports com.rilke.blueprint;
}