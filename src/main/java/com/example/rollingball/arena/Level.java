package com.example.rollingball.arena;

import com.example.rollingball.KeyboardController;
import com.example.rollingball.Main;
import com.example.rollingball.StageController;
import com.example.rollingball.timer.Timer;
import com.example.rollingball.util.Skybox;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Level extends Scene {

    SubScene subscene3d;
    private Group root;
    private Group root2d;
    private Ball  ball;
    private Arena arena;
    private Hole final_hole;

    private List<Hole> holes = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private Camera cam, camera, camera2;
    double ballX, ballY, ballZ;
    Translate ballTranslate;

    Translate initial_position;

    Ball backupReferenceBall;

    double available_time;

    private boolean game_started = false;
    private boolean run_update = false;

    private Rotate camera_rotate_x;
    private Rotate camera_rotate_y;
    private Translate camera_translate;

    private Scale ball_scale;

    private Group skybox_root;
    private SubScene skybox_subscene3d;
    private Camera skybox_camera;
    private PerspectiveCamera skybox_camera2;

    public Level(
            double stage_w,
            double stage_h,
            Ball _ball,
            Translate initial_position,
            double available_time,
            Hole final_hole,
            List<Hole> holes,
            List<Obstacle> obstacles) {
        super(new Group(), stage_w, stage_h, true, SceneAntialiasing.BALANCED);
        root2d = (Group)this.getRoot();
        this.ball = _ball;
        this.backupReferenceBall = _ball;

        this.initial_position = initial_position;
        this.available_time = available_time;

        this.final_hole = final_hole;
        this.holes = holes;
        this.obstacles = obstacles;

        camera_rotate_x = new Rotate( Main.CAMERA_X_ANGLE, Rotate.X_AXIS );
        camera_rotate_y = new Rotate( 0, Rotate.Y_AXIS );
        camera_translate = new Translate( 0, 0, Main.CAMERA_Z );


        ballTranslate = new Translate(ballX, ballY - 5000, ballZ);

        // Add skybox subscene
        skybox_root = new Group();
        skybox_subscene3d = new SubScene(skybox_root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        skybox_root.getChildren().add(new Skybox());
        skybox_camera = new PerspectiveCamera( true );
        skybox_camera.setFarClip ( Main.CAMERA_FAR_CLIP );

        skybox_camera.getTransforms ( ).addAll (
                camera_rotate_y,
                camera_rotate_x
        );
        skybox_root.getChildren ( ).add ( skybox_camera );

        skybox_subscene3d.setCamera (skybox_camera);

        skybox_camera2 = new PerspectiveCamera(true);
        skybox_camera2.setFarClip(Main.CAMERA_FAR_CLIP);
        skybox_camera2.getTransforms().setAll(
                new Rotate(-90, Rotate.X_AXIS)
        );
        skybox_root.getChildren().add(skybox_camera2);

        // Add the 3d subscene
        root = new Group();
        subscene3d = new SubScene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

        // 3d subscene stuff
        PhongMaterial wood_material = new PhongMaterial(Color.WHITE);
        Image wood_tex = new Image(this.getClass().getClassLoader().getResourceAsStream("wood.png"));
        wood_material.setDiffuseMap(wood_tex);

        Group podium = new Group();

        Box podium_box = new Box (
                Main.PODIUM_WIDTH,
                Main.PODIUM_HEIGHT,
                Main.PODIUM_DEPTH
        );
        podium_box.setMaterial (wood_material);
        podium_box.setTranslateY(Main.PODIUM_HEIGHT / 2);
        podium.getChildren().add(podium_box);

        camera = new PerspectiveCamera( true );
        camera.setFarClip ( Main.CAMERA_FAR_CLIP );


        camera.getTransforms ( ).addAll (
                camera_rotate_y,
                camera_rotate_x,
                camera_translate
        );
        this.root.getChildren ( ).add ( camera );

        camera2 = new PerspectiveCamera(true);
        camera2.setFarClip(Main.CAMERA_FAR_CLIP);
        camera2.getTransforms().setAll(
                ballTranslate,
                new Rotate(-90, Rotate.X_AXIS)
        );
        this.root.getChildren().add(camera2);

        cam = camera;
        subscene3d.setCamera (cam);

        this.ball.setSpeed(0, 0, 0);

        ball_scale = new Scale(0.0, 0.0, 0.0);
        this.ball.getTransforms().add(ball_scale);

        this.ball.getPosition().setX(initial_position.getX());
        this.ball.getPosition().setY(- Main.BALL_RADIUS);
        this.ball.getPosition().setZ(initial_position.getZ());

        this.arena = new Arena();
        this.arena.getChildren().add(podium);
        this.arena.getChildren().add(this.ball);
        this.arena.getChildren().addAll(this.final_hole);
        this.arena.getChildren().addAll(this.holes);
        this.arena.getChildren().addAll(obstacles);

        this.root.getChildren ( ).add ( this.arena );


        // 2d scene
        this.root2d.getChildren().add(skybox_subscene3d);
        this.root2d.getChildren().add(subscene3d);

        // Timer
        Timer timer = new Timer (
                deltaSeconds -> {
                    if (!run_update) return;

                    if( game_started == false && (
                        KeyboardController.getInstance().isKeyActive(KeyCode.UP) ||
                        KeyboardController.getInstance().isKeyActive(KeyCode.DOWN) ||
                        KeyboardController.getInstance().isKeyActive(KeyCode.LEFT) ||
                        KeyboardController.getInstance().isKeyActive(KeyCode.RIGHT))) {
                        game_started = true;
                    }
                    arena.update(deltaSeconds);

                    if ( this.ball != null ) {
                        boolean outOfArena = this.ball.update (
                                deltaSeconds,
                                Main.PODIUM_DEPTH / 2,
                                -Main.PODIUM_DEPTH / 2,
                                -Main.PODIUM_WIDTH / 2,
                                Main.PODIUM_WIDTH / 2,
                                this.arena.getXAngle ( ),
                                this.arena.getZAngle ( ),
                                Main.MAX_ANGLE_OFFSET,
                                Main.DAMP
                        );
                        Translate t = this.ball.getPosition();
                        ballX = t.getX();
                        ballY = t.getY();
                        ballZ = t.getZ();
                        ballTranslate.setX(ballX);
                        ballTranslate.setY(ballY - 5000);
                        ballTranslate.setZ(ballZ);

                        boolean isInFinalHole = this.final_hole.handleCollision ( this.ball );
                        boolean isInHole = false;

                        for (Obstacle o: obstacles) {
                            o.handleCollision(this.ball);
                        }

                        for (Hole h: holes) {
                            isInHole = h.handleCollision(this.ball);
                            if(isInHole) break;
                        }

                        if(isInFinalHole) {
                            // WIN
                            this.arena.getChildren().remove(this.ball);
                            this.ball = null;

                            Text game_over_text = new Text("YOU WON!");
                            game_over_text.setFont(Font.font(50));
                            game_over_text.setTranslateX(Main.WINDOW_WIDTH / 2 - game_over_text.getBoundsInParent().getWidth() / 2);
                            game_over_text.setTranslateY(Main.WINDOW_HEIGHT / 2 - game_over_text.getBoundsInParent().getHeight() / 2);
                            game_over_text.setStroke(Color.GREEN);
                            game_over_text.setFill(Color.WHITE);
                            this.root2d.getChildren().add(game_over_text);
                            FadeTransition ft = new FadeTransition();
                            ft.setNode(game_over_text);
                            ft.setFromValue(1);
                            ft.setToValue(0);
                            ft.setDuration(Duration.seconds(2));
                            ft.setOnFinished(actionEvent -> {
                                Platform.exit();
                            });
                            ft.play();
                        }

                        if ( outOfArena || isInHole) {
                                // LOSE A LIFE
                                return_ball_to_start();
                        }
                    }
                }
        );
        timer.start ( );

        this.addEventHandler ( KeyEvent.KEY_PRESSED, event -> this.handleKeyEvent(event) );

        this.setOnMouseDragged(event -> this.handleMosueDrag(event));
        this.setOnMouseReleased(event -> this.handleMosueReleased(event));

        show_ball_animation();
    }




    double last_x = 0;
    double last_y = 0;

    private void handleMosueDrag(MouseEvent mouseEvent) {
        if(!mouseEvent.getButton().equals(MouseButton.SECONDARY))
            return;

        if(last_y == 0 && last_x ==0) {
            last_x = mouseEvent.getX();
            last_y = mouseEvent.getY();
            return;
        }
        camera_rotate_y.setAngle(camera_rotate_y.getAngle() + (mouseEvent.getX() - last_x) / Main.WINDOW_WIDTH * Main.CAMERA_SENS);
        camera_rotate_x.setAngle(camera_rotate_x.getAngle() - (mouseEvent.getY() - last_y) / Main.WINDOW_HEIGHT * Main.CAMERA_SENS);
        last_x = mouseEvent.getX();
        last_y = mouseEvent.getY();
    }

    private void handleMosueReleased(MouseEvent event) {
        if(event.getButton().equals(MouseButton.SECONDARY)) {
            last_y = 0;
            last_x = 0;
        }
    }


    private boolean is_persp_active = true;

    private void handleKeyEvent(KeyEvent e){
        switch(e.getCode()){
            case C:
                if (is_persp_active) {
                    cam = camera2;
                    subscene3d.setCamera(cam);
                    skybox_subscene3d.setCamera(skybox_camera2);
                    is_persp_active = false;
                } else {
                    cam = camera;
                    subscene3d.setCamera(cam);
                    skybox_subscene3d.setCamera(skybox_camera);
                    is_persp_active = true;
                }
                break;
            default:
                break;
        }
    }

    private void show_ball_animation() {
        Timeline t = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(
                                ball_scale.xProperty(),
                                1.0
                        ),
                        new KeyValue(
                                ball_scale.yProperty(),
                                1.0
                        ),
                        new KeyValue(
                                ball_scale.zProperty(),
                                1.0
                        )
                )
        );
        t.setOnFinished(actionEvent -> {
            run_update = true;
        });
        t.playFromStart();
    }
    private void return_ball_to_start() {
        Timeline t = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(
                                ball_scale.xProperty(),
                                0.0
                        ),
                        new KeyValue(
                                ball_scale.yProperty(),
                                0.0
                        ),
                        new KeyValue(
                                ball_scale.zProperty(),
                                0.0
                        ),
                        new KeyValue(
                                arena.getRotateX().angleProperty(),
                                0.0
                        ),
                        new KeyValue(
                                arena.getRotateZ().angleProperty(),
                                0.0
                        )
                )
        );
        Timeline t2 = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(
                                ball_scale.xProperty(),
                                1.0
                        ),
                        new KeyValue(
                                ball_scale.yProperty(),
                                1.0
                        ),
                        new KeyValue(
                                ball_scale.zProperty(),
                                1.0
                        )
                )
        );

        t.setOnFinished(actionEvent -> {
            this.ball.setSpeed(0,0,0);
            this.ball.getPosition().setX(initial_position.getX());
            this.ball.getPosition().setZ(initial_position.getZ());
            t2.playFromStart();
        });

        t2.setOnFinished(actionEvent -> {
            run_update = true;
        });

        run_update = false;
        t.playFromStart();
    }
}
