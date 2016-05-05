package ApplicationGUI;

import com.sun.javafx.perf.PerformanceTracker;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    public static int CurrentResolutionH;
    public static int CurrentResolutionW;

    private static FXMLLoader fxmlLoader;
    private static Parent root;
    private static Scene scene;


    public void playWorld() {
        try {
            root = FXMLLoader.load(getClass().getResource("/World/World.fxml"));
            scene.setRoot(root);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        fxmlLoader = new FXMLLoader(getClass().getResource("/ApplicationGUI/Application.fxml"));
        root = fxmlLoader.load();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        CurrentResolutionH = (int)primaryScreenBounds.getMaxY();
        CurrentResolutionW = (int)primaryScreenBounds.getMaxX();

        scene = new Scene(root, CurrentResolutionW, CurrentResolutionH);

        primaryStage.setTitle("Tower Defence");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setResizable(false);
        primaryStage.show();


        Controller controller = (Controller)fxmlLoader.getController();
        controller.play_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playWorld();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
