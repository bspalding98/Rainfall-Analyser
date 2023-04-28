module RainfallPackage {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens rainfallPackage.alpha to javafx.fxml;
    exports rainfallPackage.alpha;

    opens rainfallPackage.beta.visualiser to javafx.fxml;
    exports rainfallPackage.beta.visualiser;
}