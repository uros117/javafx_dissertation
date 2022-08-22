package com.example.rollingball.arena;

import com.example.rollingball.Main;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

import java.util.Collection;

public class Hole extends Cylinder {
	public Hole ( double radius, double height, Material material, Translate position ) {
		super ( radius, height );



		super.setMaterial ( material );

		super.getTransforms ( ).add ( position );
	}

	public Hole(double rel_x, double rel_z, boolean is_final) {
		this.setRadius(Main.HOLE_RADIUS);
		this.setHeight(Main.HOLE_HEIGHT);
		this.getTransforms().add(new Translate(
				(rel_x - 0.5) * Main.PODIUM_WIDTH,
				- Main.HOLE_HEIGHT / 2 - Main.H_OFFSET,
				(rel_z - 0.5) * Main.PODIUM_DEPTH));

		PhongMaterial mat = new PhongMaterial();
		Image image = new Image(this.getClass().getClassLoader().getResourceAsStream((is_final)?"final_hole.png":"hole.png"));
		mat.setDiffuseMap(image);
		this.setMaterial(mat);
	}
	
	public boolean handleCollision ( Sphere ball ) {
		Bounds ballBounds = ball.getBoundsInParent ( );
		
		double ballX = ballBounds.getCenterX ( );
		double ballZ = ballBounds.getCenterZ ( );
		
		Bounds holeBounds = super.getBoundsInParent ( );
		double holeX      = holeBounds.getCenterX ( );
		double holeZ      = holeBounds.getCenterZ ( );
		double holeRadius = super.getRadius ( );
		
		double dx = holeX - ballX;
		double dz = holeZ - ballZ;
		
		double distance = dx * dx + dz * dz;
		
		boolean isInHole = distance < Main.HOLE_COLLISION_RADIUS * Main.HOLE_COLLISION_RADIUS;
		
		return isInHole;
	}
	
}
