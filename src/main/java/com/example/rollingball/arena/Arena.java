package com.example.rollingball.arena;

import com.example.rollingball.KeyboardController;
import com.example.rollingball.Main;
import com.example.rollingball.Utilities;
import com.example.rollingball.timer.Timer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;

public class Arena extends Group {

	private static double angular_speed = 10.0d;

	public Rotate getRotateX() {
		return rotateX;
	}

	public void setRotateX(Rotate rotateX) {
		this.rotateX = rotateX;
	}

	public Rotate getRotateZ() {
		return rotateZ;
	}

	public void setRotateZ(Rotate rotateZ) {
		this.rotateZ = rotateZ;
	}

	private Rotate rotateX;
	private Rotate rotateZ;

	private boolean keyIsPressed = false;


	public Arena ( Node... children ) {
		super ( children );
		
		this.rotateX = new Rotate ( 0, Rotate.X_AXIS );
		this.rotateZ = new Rotate ( 0, Rotate.Z_AXIS );
		
		super.getTransforms ( ).addAll (
				this.rotateX,
				this.rotateZ
		);

		//this.addEventHandler(KeyEvent.KEY_PRESSED, event -> this.handleKeyEvent(event, Main.MAX_ANGLE_OFFSET));
		//this.addEventHandler(KeyEvent.KEY_RELEASED, event -> this.handleKeyRelease(event));
	}

	public void handleKeyEvent ( KeyEvent event, double maxOffset ) {

		System.out.println("key event arena");

	}

	public void handleKeyRelease (KeyEvent event){
		switch(event.getCode()){
			case UP:
			case DOWN:
			case LEFT:
			case RIGHT:
				keyIsPressed = false;
		}
	}

	public void update(double deltaseconds){
		double dxAngle = 0;
		double dzAngle = 0;

		if ( KeyboardController.getInstance().isKeyActive( KeyCode.UP ) ) {
			dxAngle = -angular_speed*deltaseconds;
		} else if ( KeyboardController.getInstance().isKeyActive( KeyCode.DOWN ) ) {
			dxAngle = angular_speed*deltaseconds;
		} else {
			double xAngle = this.rotateX.getAngle();
			dxAngle = - xAngle*0.9*deltaseconds;
		}

		if ( KeyboardController.getInstance().isKeyActive( KeyCode.LEFT ) ) {
			dzAngle = -angular_speed*deltaseconds;
		} else if (KeyboardController.getInstance().isKeyActive( KeyCode.RIGHT ) ) {
			dzAngle = angular_speed*deltaseconds;
		} else {
			double zAngle = this.rotateZ.getAngle();
			dzAngle = - zAngle*0.9*deltaseconds;
		}

		double newXAngle = Utilities.clamp ( this.rotateX.getAngle ( ) + dxAngle, -Main.MAX_ANGLE_OFFSET, Main.MAX_ANGLE_OFFSET );
		double newZAngle = Utilities.clamp ( this.rotateZ.getAngle ( ) + dzAngle, -Main.MAX_ANGLE_OFFSET, Main.MAX_ANGLE_OFFSET );

		this.rotateX.setAngle ( newXAngle );
		this.rotateZ.setAngle ( newZAngle );
	}
	
	public double getXAngle ( ) {
		return this.rotateX.getAngle ( );
	}
	
	public double getZAngle ( ) {
		return this.rotateZ.getAngle ( );
	}

	public void setXAngle(double angle) { this.rotateX.setAngle(angle);}
	public void setZAngle(double angle) { this.rotateZ.setAngle(angle);}
}
