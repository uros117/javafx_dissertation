package com.example.rollingball;

import com.example.rollingball.arena.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	public static final double WINDOW_WIDTH  = 800;
	public static final double WINDOW_HEIGHT = 800;
	
	public static final double PODIUM_WIDTH  = 1200;
	public static final double PODIUM_HEIGHT = 100;
	public static final double PODIUM_DEPTH  = 1200;
	
	public static final double CAMERA_FAR_CLIP = 8000;
	public static final double CAMERA_Z        = -2800;
	public static final double CAMERA_X_ANGLE  = -45;
	
	public static final double BALL_RADIUS = 50;
	
	public static final double DAMP = 0.999;
	
	public static final double MAX_ANGLE_OFFSET = 30;
	public static final double MAX_ACCELERATION = 400;
	
	public static final int    NUMBER_OF_HOLES = 4;
	public static final double HOLE_RADIUS     = 75;
	public static final double HOLE_COLLISION_RADIUS = 50;
	public static final double HOLE_HEIGHT     = 15;

	public static final double H_OFFSET		  = 10;

	public static final int OBSTACLE_NUM = 1;

	public static final double CAMERA_SENS = 15.0;

	public static final double SCROLL_SENS = 1.0;
	
	private Group root;
	private Scene scene;

	@Override
	public void start ( Stage stage ) throws IOException {
		this.root = new Group ( );
		

		StageController.createInstance(stage);
		KeyboardController.createInstance(stage);

		// Window options
		stage.setResizable(false);

		// LEVEL
		// use
		Material hole_material = new PhongMaterial(Color.BLACK);

		PhongMaterial ball_material = new PhongMaterial(Color.rgb(200, 200, 200));
		ball_material.setSpecularColor(Color.WHITE);

		Ball ball = new Ball(BALL_RADIUS, ball_material, new Translate());

		List<Hole> holes = new ArrayList<>();
		List<Obstacle> obstacles = new ArrayList<Obstacle>();

		// Holes
		holes.add(new Hole(0.75, 0.75, false));
		holes.add(new Hole(0.2083, 0.7333, false));
		holes.add(new Hole(0.5, 0.35, false));
		holes.add(new Hole(0.375, 0.1833, false));
		holes.add(new Hole(0.71667, 0.15, false));

		// Obstacles
		obstacles.add(new Obstacle(0.0333, 1, 0.667, 0.01667, 0.5));
		obstacles.add(new Obstacle(0.0333, 1, 0.125, 0.225, 0.875));
		obstacles.add(new Obstacle(0.0333, 1, 0.5, 0.225, 0.291667));
		obstacles.add(new Obstacle(0.0833, 1, 0.333, 0.875, 0.5));
		obstacles.add(new Obstacle(0.3333, 1, 0.0333, 0.6667, 0.5));

		// initial_position
		Translate initial_position = new Translate();
		initial_position.setX(-( Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS ));
		initial_position.setY(- ( Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2 ));
		initial_position.setZ(Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS);

		Level lb = new Level(
				Main.WINDOW_WIDTH,
				Main.WINDOW_HEIGHT,
				ball,
				initial_position,
				30,
				new Hole(0.91667, 1.0 - 0.91667,true),
				holes,
				obstacles
		);

		stage.setTitle ( "Rolling Ball" );
		stage.setScene ( lb );
		stage.show ( );
	}
	
	public static void main ( String[] args ) {
		launch ( );
	}
}