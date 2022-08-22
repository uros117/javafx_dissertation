package com.example.rollingball.arena;

import com.example.rollingball.Main;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Obstacle extends Box {

    private static final double OBSTACLE_BOUNCE = 0.2;
    private static final PhongMaterial material = new PhongMaterial();

    {
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream("wood.png"));
        material.setDiffuseMap(image);
    }
    private Translate position;
    public Obstacle (double width, double height, double length, Translate position){
        super(width, height, length);
        this.position = position;
        super.setMaterial(material);
        super.getTransforms().add(position);
    }

    public Obstacle(double rel_width, double rel_height, double rel_depth, double rel_x, double rel_z) {
        this.setWidth(rel_width * Main.PODIUM_WIDTH);
        this.setHeight(rel_height * Main.BALL_RADIUS * 2);
        this.setDepth(rel_depth * Main.PODIUM_DEPTH);
        this.position = new Translate(
                (rel_x - 0.5) * Main.PODIUM_WIDTH,
                - rel_height * Main.BALL_RADIUS - Main.H_OFFSET,
                (rel_z - 0.5) * Main.PODIUM_DEPTH);

        this.setMaterial(material);
        this.getTransforms().add(position);
    }

    public void handleCollision(Ball ball){
        Translate ballPos = ball.getPosition();
        double xdiff = Math.max(-this.getWidth()/2, Math.min(this.getWidth()/2, ballPos.getX() - this.position.getX()));
        double zdiff = Math.max(-this.getDepth()/2, Math.min(this.getDepth()/2, ballPos.getZ() - this.position.getZ()));
        Translate P = new Translate(this.position.getX() + xdiff, 0, this.position.getZ() + zdiff);
        double ux = ballPos.getX() - this.position.getX() - xdiff;
        double uz = ballPos.getZ() - this.position.getZ() - zdiff;
        double i = Math.sqrt(ux*ux + uz*uz);

        if(i < ball.getRadius()){
            double normx = ux / i;
            double normz = uz / i;
            double rezx = normx * (ball.getRadius() - i);
            double rezz = normz * (ball.getRadius() - i);
            ball.getPosition().setX(ball.getPosition().getX() + rezx);
            ball.getPosition().setZ(ball.getPosition().getZ() + rezz);

            Point2D ball_speed = new Point2D(ball.getSpeed().getX(), ball.getSpeed().getZ());
            Point2D norm_vec = new Point2D(-normx, -normz);
            double speed_component = ball_speed.dotProduct(norm_vec);

            ball.setSpeed(
                    ball.getSpeed().getX() + (1.0 + OBSTACLE_BOUNCE) * speed_component * normx,
                    0,
                    ball.getSpeed().getZ() + (1.0 + OBSTACLE_BOUNCE) * speed_component * normz
            );
        }

    }


}
