package ApplicationGUI;

import World.World;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.tk.*;
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

import java.awt.*;
import java.awt.Toolkit;

public class Main extends Application {
    public static int CurrentResolutionH;
    public static int CurrentResolutionW;

    private static FXMLLoader fxmlLoader;
    private static Parent root;
    private static Scene scene;


    public void playWorld() {
        try {
            fxmlLoader.setRoot(null);
            fxmlLoader.setController(null);
            fxmlLoader.setLocation(getClass().getResource("/World/World.fxml"));
            root = fxmlLoader.load();
            scene.setRoot(root);

            World controller = (World)fxmlLoader.getController();
            controller.getPauseMenu().get_ExitToMenuLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    playMenu();
                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void playMenu() {
         try {
             fxmlLoader.setRoot(null);
             fxmlLoader.setController(null);
             fxmlLoader.setLocation(getClass().getResource("/ApplicationGUI/Application.fxml"));
             root = fxmlLoader.load();
             scene.setRoot(root);

             Controller controller = (Controller)fxmlLoader.getController();
             controller.play_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                 @Override
                 public void handle(MouseEvent event) {
                     playWorld();
                 }
             });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        fxmlLoader = new FXMLLoader(getClass().getResource("/ApplicationGUI/Application.fxml"));
        root = fxmlLoader.load();


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CurrentResolutionH = (int)screenSize.getHeight();
        CurrentResolutionW = (int)screenSize.getWidth();

        scene = new Scene(root, CurrentResolutionW, CurrentResolutionH);

        primaryStage.setTitle("Tower Defence");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setResizable(false);
        primaryStage.show();
        playMenu();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
