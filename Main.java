import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Board board = new Board();
        RanksPanel ranksPanel = new RanksPanel();

        BorderPane root = new BorderPane();
        root.setCenter(board);
        root.setRight(ranksPanel);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Stratego Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}