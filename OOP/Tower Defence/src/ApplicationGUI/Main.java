package ApplicationGUI;

import World.World;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.tk.*;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class Main extends Application {
    public static int CurrentResolutionH;
    public static int CurrentResolutionW;
    public static int level_to_play = 1;

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

    public void playLevels() {
        try {
            fxmlLoader.setRoot(null);
            fxmlLoader.setController(null);
            fxmlLoader.setLocation(getClass().getResource("levels.fxml"));
            root = fxmlLoader.load();
            scene.setRoot(root);

            LevelsController controller = (LevelsController) fxmlLoader.getController();
            controller.level1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    level_to_play = 1;
                    playWorld();
                }
            });

            controller.level2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    level_to_play = 2;
                    playWorld();
                }
            });

            controller.back.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
             controller.play_label.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                 @Override
                 public void handle(MouseEvent event) {
                     playWorld();
                 }
             });

             controller.open_levels.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                 @Override
                 public void handle(MouseEvent event) {
                     playLevels();
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

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        CurrentResolutionH = (int)primScreenBounds.getHeight();
        CurrentResolutionW = (int)primScreenBounds.getWidth();

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
