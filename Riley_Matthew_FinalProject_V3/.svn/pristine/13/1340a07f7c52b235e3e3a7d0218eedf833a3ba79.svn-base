/*
 Matthew Riley
 ICS3U-1
 Final Project - Spawn Locations Class (manages the spawn location of the catapults)
 May 13, 14
 */
package riley_matthew_finalproject_v3;

/**
 *
 * @author matthewriley
 */
public class SpawnLocation {

    //The coordiantes of the spawn location
    private final int x, y;

    //The index of the current image (in the animHatch array in teh gamepanel)
    private int imgIndex;

    //The timer for the hatch animations
    Timer animTimer;

    //Positive if the hatch is opening, 0 if no animation, and -1 if the hatch is closing
    private int animDirection;

    //True if the spawn location is occupied by an alien
    private boolean occupied;

    //True if there is an activeAlien alien at the spawn location
    private boolean activeAlien;

    public SpawnLocation(int _x, int _y) {

        //Set the coordinates of the spawn location
        x = _x;
        y = _y;

        //No alien is currently occupying the spawn location, so set to false
        occupied = false;
        activeAlien = false;

        //Instantiate the animation timer
        animTimer = new Timer();

        //No animation is orginally required
        animDirection = 0;
    }

    /*
     Accessors
     */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isActiveAlien() {
        return activeAlien;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    /*
     Setters
     */
    public void setOccupied(boolean _occupied) {
        //If an animation is required...
        if (_occupied != occupied) {
            //If an alien has just spawned...
            if (!occupied) {
                animDirection = 1;
            } //if an alien has just been rescued...
            else {
                animDirection = -1;
                activeAlien = false;
            }
        }
        occupied = _occupied;
    }

    /*
     Methods
     */
    public void manage() {
        //if an animation is required...
        if (animDirection != 0) {

            //Start the animation timer (if it hasn't already been started)
            //Progress the animation
            switch (imgIndex) {
                //0 is the index for the closed hatch
                case 0:
                    //If the hatch was closing, the animation is now over
                    if (animDirection == -1) {
                        animDirection = 0;
                    } //if the hatch is opening, advance the animmation
                    else {
                        //Reset the timer
                        if (!animTimer.isActive()) {
                            animTimer.reset(Const.HATCH_CHANGE_TIME);
                        }
                        imgIndex++;
                    }
                    break;

                //1 is the index for the open hatch
                case 1:
                    //If the timer is complete...
                    if (animTimer.isComplete()) {
                        //Advance the hatch animation
                        imgIndex += animDirection;

                        //Stop the timer
                        animTimer.stop();
                    }
                    break;
                //2 is the index for the open hatch with an alien
                case 2:
                    //If the hatch is closing
                    if (animDirection == -1) {
                        //Reset the timer
                        if (!animTimer.isActive()) {
                            animTimer.reset(Const.HATCH_CHANGE_TIME);
                        }

                        //Advance the animation
                        imgIndex--;
                    } //If the hatch was opening
                    else {
                        //The animation is over
                        animDirection = 0;

                        //There is now an alien in the hatch, so set the hatch to activeAlien
                        activeAlien = true;
                    }
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
}
