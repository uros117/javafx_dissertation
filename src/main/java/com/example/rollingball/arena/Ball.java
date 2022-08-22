package com.example.rollingball.arena;

import com.example.rollingball.Main;
import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Ball extends Sphere {
	private Translate position;
	private Point3D speed;
	protected double maxAcceleration = Main.MAX_ACCELERATION;


	protected Ball() {

	}

	public Ball ( double radius, Material material, Translate position ) {
		super ( radius );
		super.setMaterial ( material );
		
		this.position = position;
		
		super.getTransforms ( ).add ( this.position );
		
		this.speed = new Point3D ( 0, 0, 0 );
	}
	
	public boolean update (
			double deltaSeconds,
			double top,
			double bottom,
			double left,
			double right,
			double xAngle,
			double zAngle,
			double maxAngleOffset,
			double damp
	) {
		double newPositionX = this.position.getX ( ) + this.speed.getX ( ) * deltaSeconds;
		double newPositionZ = this.position.getZ ( ) + this.speed.getZ ( ) * deltaSeconds;
		
		this.position.setX ( newPositionX );
		this.position.setZ ( newPositionZ );
		
		double accelerationX = maxAcceleration * zAngle / maxAngleOffset;
		double accelerationZ = -maxAcceleration * xAngle / maxAngleOffset;
		
		double newSpeedX = ( this.speed.getX ( ) + accelerationX * deltaSeconds ) * damp;
		double newSpeedZ = ( this.speed.getZ ( ) + accelerationZ * deltaSeconds ) * damp;
		
		this.speed = new Point3D ( newSpeedX, 0, newSpeedZ );
		
		boolean xOutOfBounds = ( newPositionX > right ) || ( newPositionX < left );
		boolean zOutOfBounds = ( newPositionZ > top ) || ( newPositionZ < bottom );
		
		return xOutOfBounds || zOutOfBounds;
	}

	public Translate getPosition(){
		return this.position;
	}

	public Point3D getSpeed(){
		return speed;
	}

	public void setSpeed(double x, double y, double z){
		speed = new Point3D(x, y, z);
	}
	
}
