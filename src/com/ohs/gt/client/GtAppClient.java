package com.ohs.gt.client;

//The external class importing zone
import dmive.core3d.app.AppClient3D;
import dmive.core3d.app.ClientScene3D; // 5.2) Set background colour and viewing behaviour
import dmive.core3d.shape.ViewPlatform; 

import dmive.core3d.shape.TexHex; // 5.3) Build walls for the room
import dmive.core.utils.DVUtils; // 5.5) Add people to the scene


public class GtAppClient extends AppClient3D {

    //The parameter declaration zone
    private ClientScene3D cs; // 5.2) Set background colour and viewing behaviour
    
    private Door[] doors; // 5.4) Fit doors to the room
    
    private Person person0;// 5.5) Add people to the scene
    private Person person1; 
    
    private int animStep = -1; // 5.7) Open and close doors
    

    @Override
    public void initApp() {
        //The scene initialization zone
        cs = getCs(); // 5.2) Set background colour and viewing behaviour
        cs.setBgColor(0.12, 0.3, 0.3); //5.2
        
        cs.addShape(new Wall(-6.0, 4.5, -5.0, -4.5)); // 5.3) Build walls for the room
        cs.addShape(new Wall(5.5, 6.0, -5.0, 3.5));
        cs.addShape(new Wall(-6.0, 6.0, 4.5, 5.0));
        cs.addShape(new Wall(-6.0, -5.5, -3.5, 4.5));
        
        doors = new Door[3]; // 5.4) Fit doors to the room
        doors[0] = new Door(-90.0, -5.9, -3.5);
        doors[1] = new Door(180.0, 5.5, -5.0);
        doors[2] = new Door(-90.0, 6.0, 4.5);
        cs.addShape(doors[0]);
        cs.addShape(doors[1]);
        cs.addShape(doors[2]);
        
        person0 = new Person(0); // 5.5) Add people to the scene
        person1 = new Person(1);
        cs.addShape(person0);
        cs.addShape(person1);
      
        TexHex floor = new TexHex(-6.0, 6.0, -5.0, 5.0, -0.002, 0.0); // 5.6) Add floor for the room
        floor.setHighTex(3, 1.0, 1.0, 0.0, 0.0);
        floor.setLowCol(0.3, 0.3, 0.3, 0.75);
        floor.buildShape(cs);
        cs.addShape(floor);
        
        person0.setActive(false); // 5.8) People entering and exiting the room
        person1.setActive(false);
        
    }
    

    @Override
    public void onAspectRatioChange() {
        //The view configuration zone
        ViewPlatform vp = getVp(); // 5.2) Set background colour and viewing behaviour
        vp.setLookUp(-9.0, -15.0, 14.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0);
        vp.setNearZ(0.1);
        vp.setFarZ(200.0);
        vp.setViewDisMin(0.2);
        vp.setViewDisMax(150.0);
        vp.setvZmin(-2.0);
        vp.setViewingBehavior(1);
        vp.setTouchZoomMul(50.0);
    }

    @Override
    public void pickAction(int pickX, int pickY, String keyCmd) {
        //The user interaction zone
        animStep = (animStep + 1) % 3; // 5.7) Open and close doors
        doors[animStep].startAnimation("", 1000);
        
        person0.startAnimation("", 1000); // 5.8 People entering and exiting the room
        person1.startAnimation("", 1000);
    }

    @Override
    public void preUpdate() {
        //The drawing cycle pre-updating zone
        doors[0].preUpdate(); // 5.7) Open and close doors
        doors[1].preUpdate();
        doors[2].preUpdate();
        
        person0.preUpdate(); // 5.8) People entering and exiting the room
        person1.preUpdate();
    }

    //The internal class definition zone
        private class Wall extends TexHex { // 5.3) Build walls for the room

        private Wall(double x0, double x1, double y0, double y1) {
            super(x0, x1, y0, y1, 0.0, 2.0);
            setWestTex(0, 0.64, 0.32, 0.0, 0.0);
            setEastTex(0, 0.64, 0.32, 0.0, 0.0);
            setSouthTex(0, 0.64, 0.32, 0.0, 0.0);
            setNorthTex(0, 0.64, 0.32, 0.0, 0.0);
            buildShape(cs); 
        }

    }
        private class Door extends TexHex { // 5.4) Fit doors to the room

        private Door(double rot, double x0, double y0) {
            TexHex doorBody = new TexHex(0.0, 1.0, -0.1, 0.0, 0.0, 2.0);
            doorBody.setSouthTex(1, 1, 1);
            doorBody.setNorthTex(2, 1, 1);
            doorBody.buildShape(cs);
            addChild(doorBody);
            setRotation(rot, 0.0, 0.0, 1.0);
            setTranslation(x0, y0, 0.0);
        }

        //Set door animation
        private void preUpdate() { // 5.7) Open and close doors
            if (isAnimationOn()) {
                double openFrac = 0.0;
                double alpha = (double) getAnimElapsedTime()
                        / (double) getAnimDuration();
                if (isAnimStarting()) {
                } else if (isAnimStopping()) {
                } else if (!isAnimStopped()) {
                    openFrac = alpha < 0.2 ? alpha / 0.2
                            : (alpha < 0.8 ? 1.0 : (1.0 - alpha) / 0.2);
                }
                getChild(0).setRotation(85.0 * openFrac, 0.0, 0.0, 1.0);
            }
        }
    }
        
        private class Person extends TexHex {

        private final int pId;
        private double rpx;
        private double rpy;

        private Person(int pId) {
            super(-0.1, 0.1, -0.1, 0.1, 0.0, 1.5);
            setWestCol(0.9, 0.9, 0.0, 1.0);
            setEastCol(0.8, 0.8, 0.0, 1.0);
            setSouthCol(0.7, 0.7, 0.0, 1.0);
            setNorthCol(0.6, 0.6, 0.0, 1.0);
            setHighCol(1.0, 1.0, 0.0, 1.0);
            buildShape(cs);
            this.pId = pId;
            setPosition(-1.0);
        }

        private void setPosition(double alpha1) { // 5.5) Add people to the scene
            if (alpha1 < -0.9) {
                rpx = -4.0 + 8.0 * DVUtils.getRandomInt(1001) / 1000.0;
                rpy = -3.0 + 6.0 * DVUtils.getRandomInt(1001) / 1000.0;
                setTranslation(rpx, rpy, 0.0);
            } else {
                //Set animated position
                double px; // 5.8) People entering and exiting the room
                double py;
                if (alpha1 < 0.0) {
                    px = -7.0;
                    py = -4.0;
                } else if (alpha1 < 0.3) {
                    px = -7.0 + 2.0 * alpha1 / 0.3;
                    py = -4.0;
                } else if (alpha1 < 0.8) {
                    px = -5.0 + (rpx + 5.0) * (alpha1 - 0.3) / 0.5;
                    py = -4.0 + (rpy + 4.0) * (alpha1 - 0.3) / 0.5;
                } else if (alpha1 < 1.0) {
                    px = rpx;
                    py = rpy;
                } else if (alpha1 < 1.6) {
                    px = rpx + (5.0 - rpx) * (alpha1 - 1.0) / 0.6;
                    py = rpy + (-4.0 + 8.0 * pId - rpy) * (alpha1 - 1.0) / 0.6;
                } else {
                    px = 5.0 + pId * 2.5 * (alpha1 - 1.6) / 0.4;
                    py = 4.0 + (1 - pId) * (-8.0 - 2.5 * (alpha1 - 1.6) / 0.4);
                }
                setTranslation(px, py, 0.0);
            }
        }

        //Set person animation
        private void preUpdate() { // 5.8) People entering and exiting the room
            if (isAnimationOn()) {
                if (animStep == 0 || animStep == pId + 1) {
                    double alpha = (double) getAnimElapsedTime()
                            / (double) getAnimDuration();
                    if (isAnimStarting()) {
                        if (animStep == 0) {
                            alpha = 0.0;
                            setActive(true);
                            setPosition(-1.0);
                        }
                    } else if (isAnimStopping()) {
                        alpha = 1.0;
                        setActive(animStep == 0);
                    } else if (!isAnimStopped()) {
                    }
                    setPosition(alpha + (animStep == 0 ? -0.2 * pId : 1.0));
                }
            }
        }
    }
}