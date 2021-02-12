module ChatSystem.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
	requires org.jsoup;
	requires transitive java.desktop;
	requires java.sql;
	requires tyrus.standalone.client;
	

    opens ChatSystem.gui to javafx.fxml;
    exports ChatSystem.gui;
}