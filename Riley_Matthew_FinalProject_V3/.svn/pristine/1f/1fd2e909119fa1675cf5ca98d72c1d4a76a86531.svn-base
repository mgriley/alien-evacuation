/*
 Matthew Riley
 ICS3U-1
 Final Project - UFO class
 May 17, 2014
 */
package riley_matthew_finalproject_v3;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author matthewriley
 */
public class UFO {

    //Images for the UFO, tractor beam, and explosion animation
    private BufferedImage imgUFO;
    private BufferedImage imgBeam;
    private BufferedImage[] animExplosion;

    //Counter for the explosion animation
    int animCounter;

    //The UFO's max possible health
    private final int maxHealth;

    //The UFO's current health
    private int health;

    //True if the UFO is alive
    private boolean alive;

    //The coordinates of the centre of the UFO
    private int posX, posY;

    //Tracks the time of the explosion animation
    private Timer explosionTimer;

    //Tracks the time of the tractor beam animation
    private Timer tractorBeamTimer;

    //The angle required to rotate the UFO's light to create a rotation animation
    private int rotationAngle;

    //True when the tractor beam is being shown
    boolean beamActive;

    //References to other game objects
    private ArrayList<Turret> turrets;

    //The number of aliens that the UFO has rescued
    private int aliensRescued;

    //Constructor
    public UFO() throws IOException {

        //Load the UFO image
        URL imgURL = getClass().getResource(Const.MAIN_URL);
        imgUFO = ImageIO.read(imgURL);

        //Load the tractor beam image
        imgURL = getClass().getResource(Const.BEAM_URL);
        imgBeam = ImageIO.read(imgURL);

        //Load the explosion animation images into the explosion array
        animExplosion = new BufferedImage[10];
        for (int i = 1; i <= 10; i++) {
            String explosionPath = "Animations/explosion_" + i + ".png";
            imgURL = getClass().getResource(explosionPath);
            animExplosion[i - 1] = ImageIO.read(imgURL);
        }

        //Set health
        maxHealth = Const.MAIN_HEALTH;
        health = maxHealth;

        //Set alive
        alive = true;

        //The beam is inactive to start
        beamActive = false;

        //Start the rotation angle at 0
        rotationAngle = 0;

        //Set the explosion animation counter to 0
        animCounter = 0;

        //Initialize the timers
        explosionTimer = new Timer();
        tractorBeamTimer = new Timer();

        //The player's score (in aliens rescued) starts at 0
        aliensRescued = 0;
    }

    /*
     Accessors
     */
    public int getHealth() {
        return health;
    }

    public int getXPos() {
        return posX;
    }

    public int getYPos() {
        return posY;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isBeamActive() {
        return beamActive;
    }

    public BufferedImage getUFOImage() {
        return imgUFO;
    }

    public BufferedImage getBeamImage() {
        return imgBeam;
    }

    public int getScore() {
        return aliensRescued;
    }

    /*
     Setters
     */
    public void setTurrets(ArrayList<Turret> _turrets) {
        turrets = _turrets;
    }

    public void setBeamActive(boolean beamState) {
        beamActive = beamState;
    }

    //Methods
    //Call when character gains or loses health
    public void changeHealth(int change) {
        health += change;
        //If damage was dealt
        if (change < 0) {

            //Damage animation is same for all characters
            //Check if character died
            if (health <= 0) {
                alive = false;
            }

        } //If the character healed (only applicable to UFO and orb)
        else {
            //If character gained health, don't let them go over the max health
            if (health > maxHealth) {
                health = maxHealth;
            }
        }

    }

    //Revive the UFO for another round of play
    public void revive() throws IOException {

        //Reset the player's score
        aliensRescued = 0;

        //Fully heal the UFO
        health = maxHealth;

        //Change the UFO's image back to the original image
        URL imgURL = getClass().getResource(Const.MAIN_URL);
        imgUFO = ImageIO.read(imgURL);

        //Reincarnate the UFO
        if (!alive) {
            alive = true;
        }

        //Reset the explosion animation counter
        animCounter = 0;
    }

    //Move towards a destination
    public void move(int desX, int desY) {
        //Move to destination X and Y, which are mouseX and mouseY
        posX = desX;
        posY = desY;
    }

    //Animate the UFO's lights to make it appear that the UFO is spinning
    public AffineTransformOp getRotationOp() {
        //Determine the center of the image (in relation to image)
        int anchorX = imgUFO.getWidth() / 2;
        int anchorY = imgUFO.getHeight() / 2;

        //Define rotational transformation
        //Note: 
        //The rotation angle is from the neutral image bearing to the destination angle
        //It isn't from the current bearing to the destination angle
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(rotationAngle), anchorX, anchorY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);

        //Increment the rotation angle (to create a constantly spinning effect)
        rotationAngle += Const.UFO_ROTATION_SPEED;
        rotationAngle %= 360;

        return op;
    }

    //Destroy the UFO
    public void destroy() {
        alive = false;
    }

    //Control whether the UFO's tractor beam is displayed or not)
    public void controlBeam() {
        //Turn off the beam once the tracot beam timer is complete
        if (tractorBeamTimer.isComplete()) {
            beamActive = false;
        }
    }

    //Play the tractor beam animation when an alien is rescued
    public void onRescue() {
        //Increment the player's score
        aliensRescued++;

        //Activate the tractor beam
        beamActive = true;

        //Start the beam timer animation
        tractorBeamTimer.reset(Const.UFO_TRACTOR_BEAM_TIME);
    }

    //Called when the UFO dies 
    public void onDeath() {
        //If the animation hasn't started yet...
        if (animCounter == 0) {
            //Start the explosion timer
            explosionTimer.reset(Const.UFO_EXPLOSION_TIME);

            //Set the UFO's image to the first explosion image
            imgUFO = animExplosion[animCounter];

            //Increment the animation counter
            animCounter++;
        } else {
            //If the timer is complete...
            if (explosionTimer.isComplete()) {
                //Change the explosion to the next image in the animation
                imgUFO = animExplosion[animCounter];

                //Increment the animation counter
                animCounter++;

                //Reset the timer
                explosionTimer.reset(Const.UFO_EXPLOSION_TIME);
            }

            //If the animation is over...
            if (animCounter == 10) {
                //Stop the timer (so that the array doesn't go out of bounds)
                explosionTimer.stop();
            }
        }

    }
}
