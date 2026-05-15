// stole this from the module 7 lab 
// please do change this if it doesn't fit, I'm just throwing stuff in here

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BudgetTrackerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                BudgetTrackerApp.class.getResource( /*have to change this later: "/view/game-client.fxml" */ )
        );

        Scene scene = new Scene(loader.load(), 760, 540);

        stage.setTitle("Budget Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}