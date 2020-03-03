//Author: Hemang Hirpara
//Author: Poojan Patel
package hhh23_pdp83;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SongLib extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("songlibrary.fxml"));
        primaryStage.setTitle("Song Library");
        primaryStage.setScene(new Scene(root, 450, 375));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
