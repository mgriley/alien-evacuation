/*
 Matthew Riley
 ICS3U-1
 Final Project - Bullet Class
 May 17, 2014
 */
package riley_matthew_finalproject_v3;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.IOException;

/**
 *
 * @author matthewriley
 */
public class Bullet {

    //The index of the bullet's image (in the imgBullets array in the GamePanel class)
    private int imgIndex;
    private boolean active;

    //The coordinates of the centre of the bullet
    private double posX;
    private double posY;

    //The speed of the bullet
    private final int speed;

    //The damage that the bullet deals the UFO upon a hit
    private final int damage;

    //The change in x and change in y of the bullet (used for bullet movement)
    private double deltaX;
    private double deltaY;

    //The coordinates of the destination of the bullet
    private int desX, desY;

    //Boolean to track whether or not it is the bullet's first update
    private boolean firstUpdate;

    //Tracks whether or not the bullet has hit the UFO
    private boolean hit;

    //Whether or not the bullet is heatseeking
    private boolean heatSeeking;

    //The bullet's rotation angle (Only applicable for heat-seeking bullets)
    private double rotationAngle;

    //The bullet's bearing (Only applicable for heat-seeking bullets)
    private double bearing;

    //The angle between the UFO's coordinates and the bullet's bearing (Only applicable for heat-seeking bullets)
    private double bulletToUFOAngle;

    //The timer for the brief explosion animation of the bullet
    private Timer explosionTimer;

    //The timer for the life-span of a heat-seeking bullet (they seek the UFO for x seconds, then they explode)
    private Timer heatSeekingTimer;

    //Reference to the UFO object
    private static UFO myUFO;

    //Constructor for firing to the UFO's changing destination (for heatseeking bullets)
    public Bullet(int orgX, int orgY, int _damage, int _speed, int _imgIndex) throws IOException {

        //Set the image index
        imgIndex = _imgIndex;

        //Set active
        active = true;

        //Set spawn position
        posX = orgX;
        posY = orgY;

        //Set bullet speed
        speed = _speed;

        //the bullet hasn't been updated yet, so set first update to true
        firstUpdate = true;

        //Set bullet damage
        damage = _damage;

        //The bullet hasn't hit anything yet
        hit = false;

        //Initalize the explosion timer
        explosionTimer = new Timer();

        //Set the bullet to heatseeking
        heatSeeking = true;

        //For heatseeking bullets, initialize the heat-seeking bullet timer
        if (heatSeeking) {
            heatSeekingTimer = new Timer();
        }

    }

    //Constructor for firing to a static destination (for non-heatseeking bullets)
    public Bullet(int orgX, int orgY, int _desX, int _desY, int _damage, int _speed, int _imgIndex) throws IOException {

        //Set the image index
        imgIndex = _imgIndex;

        //Set active
        active = true;

        //Set spawn position
        posX = orgX;
        posY = orgY;

        //Set the destination
        desX = _desX;
        desY = _desY;

        //Set bullet speed
        speed = _speed;

        //The bullet hasn't been updated yet, so set first update to true
        firstUpdate = true;

        //Set bullet damage
        damage = _damage;

        //Typical bullets aren't heat seeking
        heatSeeking = false;

        //The bullet hasn't hit anything yet
        hit = false;

        //Initalize the explosion timer
        explosionTimer = new Timer();

        //Initialize the heat-seeking bullet timer
        heatSeekingTimer = new Timer();
    }

    /*
     Accessors
     */
    public int getImgIndex() {
        return imgIndex;
    }

    public int getX() {
        return (int) posX;
    }

    public int getY() {
        return (int) posY;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isHeatSeeking() {
        return heatSeeking;
    }

    public boolean isActive() {
        return active;
    }

    //Methods
    public void update() throws IOException {

        if (!hit) {
            //Deactive the bullet if it is offscreen
            if (posX > Const.FIELD_WIDTH || posX < 0 || posY > Const.FIELD_HEIGHT || posY < 0) {
                active = false;
            } //Update the bullet's location
            else {
                //If the bullet is heatseeking...
                if (heatSeeking) {

                    //Start the lifespan timer on the first update
                    if (firstUpdate) {
                        //Set the life span of the heat-seeking bullet
                        heatSeekingTimer.reset(Const.HEAT_SEEKING_LIFE_SPAN);

                        //No longer the first update
                        firstUpdate = false;
                    }
                    //If the bullet's life-span hasn't expired...
                    if (!heatSeekingTimer.isComplete()) {

                        //Calculate change in x and y from current position to destination position
                        deltaX = myUFO.getXPos() - posX; //CHANGE HERE
                        deltaY = myUFO.getYPos() - posY; //CHANGE HEREa

                        //Calculate the angle between the bullet and the UFO
                        bulletToUFOAngle = Math.atan2(deltaY, deltaX);

                        //Calculate the bullet's path towards the UFO
                        deltaX = Math.cos(bulletToUFOAngle);
                        deltaY = Math.sin(bulletToUFOAngle);

                        //Move the bullet towards the destination
                        posX += deltaX * speed;
                        posY += deltaY * speed;

                        //Rotate the bullet towards the UFO
                        rotateTo(myUFO.getXPos(), myUFO.getYPos());

                    } //If the timer is complete
                    else {
                        //Run the bullet explosion animation
                        hit();
                    }
                } //If the bullet isn't heatseeking, move it forward
                else {
                    //Only set the scaleX and scaleY once
                    if (firstUpdate) {
                        //Calculate change in x and y from current position to destination position
                        deltaX = desX - posX;
                        deltaY = desY - posY;

                        //Calculate necessary change in x and y position using Polar Coordinate system
                        double bulletAngle = Math.atan2(deltaY, deltaX);
                        deltaX = Math.cos(bulletAngle);
                        deltaY = Math.sin(bulletAngle);

                        firstUpdate = false;
                    }
                    //Move the bullet in the direction of the destination
                    posX += deltaX * speed;
                    posY += deltaY * speed;
                }
            }
        } //If the bullet has hit the UFO, draw the explosion
        else {
            //Once the explosion is complete, remove the bullet from the turretBullets arraylist so that it isn't drawn
            if (explosionTimer.isComplete()) {
                active = false;
            }
        }
    }

    //Update the rotation angle so that it will rotate to the desired angle
    public void rotateTo(int faceX, int faceY) {

        //Calculate the polar co-ordinates of the destination
        int dX = faceX - (int) posX;
        int dY = (int) posY - faceY;
        //Math.atan2 converts Cartesian coordinates to Polar coordinates
        //tan@ = Y/X (Math.atan2 returns angle @)
        double destinationAngle = Math.atan((double) dY / dX);
        //Map desintationAngle to 0-360 degrees (http://stackoverflow.com/questions/1311049/how-to-map-atan2-to-degrees-0-360)
        //Right now it is in radians
        destinationAngle = Math.toDegrees(destinationAngle); //Convert to degrees
        //Correct discontinuities that occur in certain quadrants
        //2nd or 3rd quadrant
        if (deltaX < 0) {
            destinationAngle += 180;
        } //4th quadrant
        else if (deltaX >= 0 && deltaY < 0) {
            destinationAngle = 360 + destinationAngle;
        }
        //Calculate the required angle of rotation (from neutral image to destination angle)
        rotationAngle = Const.TURRET_BEARING - destinationAngle;
        //Make sure the most efficient path of rotation is being used
        if (Math.abs(rotationAngle) > 180) {
            if (rotationAngle < 0) {
                rotationAngle += 360;
            } //rotation angle > 0 (rotation angle cannot be 0 and be >180
            else {
                rotationAngle -= 360;
            }
        }
        //Update the bearing
        bearing = destinationAngle;
    }

    //Return the rotation operation necessary to rotate the image to the desired angle
    public AffineTransformOp getRotationOp(int imgWidth, int imgHeight) {

        //Determine the center of the image (in relation to image)
        int anchorX = imgWidth / 2;
        int anchorY = imgHeight / 2;

        //Define rotational transformation
        //Note: 
        //The rotation angle is from the neutral image bearing to the destination angle
        //It isn't from the current bearing to the destination angle
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(rotationAngle), anchorX, anchorY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);

        return op;
    }

    public void hit() throws IOException {
        //Set the bullet to hit
        hit = true;

        //Change the bullet's image index to an image of an explosion
        imgIndex = Const.EXPLODING_BULLET;

        //Start the timer that dictates the length of the explosion animation
        explosionTimer.reset(Const.EXPLOSION_LENGTH);
    }

    //Establish a reference to the UFO
    public static void setUFO(UFO _myUFO) {
        myUFO = _myUFO;
    }
}
