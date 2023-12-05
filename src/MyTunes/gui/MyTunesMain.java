package MyTunes.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main method to run the program.
 */

public class MyTunesMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/MyTunesView.fxml")));
        primaryStage.setTitle("Our Tunes");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
