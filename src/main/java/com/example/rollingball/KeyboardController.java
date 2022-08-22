package com.example.rollingball;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.HashMap;

public class KeyboardController {
    private HashMap<KeyCode, Boolean> currentlyActiveKeys = new HashMap<>();
    private static KeyboardController instance = null;

    public static KeyboardController getInstance() {
        return instance;
    }

    public static void createInstance(Stage stage) {
        instance = new KeyboardController(stage);
    }

    private KeyboardController(Stage stage)  {

        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            String codeString = event.getCode().toString();
            if (!currentlyActiveKeys.containsKey(codeString)) {
                currentlyActiveKeys.put(event.getCode(), true);
            }
        });
        stage.addEventHandler(KeyEvent.KEY_RELEASED, event ->
                currentlyActiveKeys.remove(event.getCode())
        );

    }

    public boolean isKeyActive(KeyCode code) {
        Boolean isActive = currentlyActiveKeys.get(code);

        if (isActive != null && isActive) {
            //currentlyActiveKeys.put(code, false);
            return true;
        } else {
            return false;
        }
    }
}
