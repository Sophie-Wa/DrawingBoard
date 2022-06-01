module com.example.draw {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.draw to javafx.fxml;
    exports com.example.draw;
}