package com.example.rollingball;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageController {
    private static StageController instance;

    public static StageController getInstance() {
        return instance;
    }

    public static void createInstance(Stage stage) {
        instance = new StageController(stage);
    }

    // Local
    Stage stage;
    private StageController(Stage stage) {
       this.stage = stage;
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getScene() {
        return stage.getScene();
    }
}
