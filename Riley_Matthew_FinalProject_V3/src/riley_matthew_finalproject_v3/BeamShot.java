/*
 * Matthew Riley
 ICS3U-1
 Final Project - Beam Shot Class
 May 13, 2014
 */
package riley_matthew_finalproject_v3;

import java.awt.geom.Line2D;

/**
 *
 * @author matthewriley
 */
public class BeamShot {

    //The coordinates of the origin/start of the beam (aka the turret's location)
    private int orgX, orgY;

    //The coordinates of the end-point of the beam (which should be off the screen)
    private double endX, endY; //End point of the line

    //The angle of the beam
    private int angle;

    //The beam is shown as a 2D line in the graphics area
    private Line2D beam;

    //Constructor
    public BeamShot(int _orgX, int _orgY, int _angle) {

        //Set the origin/start point and angle of the beam
        orgX = _orgX;
        orgY = _orgY;
        angle = _angle;

        //Determine the end-points of the beam
        endX = 1000 * Math.cos(Math.toRadians(angle)) + orgX;
        endY = 1000 * Math.sin(Math.toRadians(angle)) + orgY;

        //Create the beam with the computed start and end points
        beam = new Line2D.Double(orgX, orgY, endX, endY);
    }

    //Update the position of the beam (to create a rotation effect)
    public void rotateTo(int newAngle) {
        //Determine the end-points of the updated beam
        endX = 1000 * Math.cos(Math.toRadians(newAngle)) + orgX;
        endY = 1000 * Math.sin(Math.toRadians(newAngle)) + orgY;

        //Update the the endpoints of the line (which produces a rotation effect)
        beam.setLine(new Line2D.Double(orgX, orgY, endX, endY));
    }

    //Accessors
    public int getStartX() {
        return orgX;
    }

    public int getStartY() {
        return orgY;
    }

    public int getEndX() {
        return (int) endX;
    }

    public int getEndY() {
        return (int) endY;
    }

    public Line2D getLine() {
        return beam;
    }
}
