package ass2.spec;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import com.jogamp.opengl.*;

public class Camera implements KeyListener {
    private double rotateY = 0;
    private double[] position;
    private double[] lineofsight;
    private boolean avatarView;
    private Avatar myAvatar;
    private Terrain myTerrain;
    private double distance = 2;

    public Camera(Avatar avatar, Terrain myTerrain) {
        this.myTerrain = myTerrain;

        position = new double[3];
        position[0] = 0;
        position[1] = 0.5;
        position[2] = 0;

        lineofsight = new double[3];
        lineofsight[0] = 1;
        lineofsight[1] = 0;
        lineofsight[2] = -1;

        avatarView = false;
        myAvatar = avatar;
    }

    public double getEyeX(){
        if (avatarView) {
            return myAvatar.getX() - (distance * Math.sin(Math.toRadians(myAvatar.getRotY())));
        } else {
            return position[0];
        }
    }

    public double getY() {
        if (avatarView) {
            return myAvatar.getY() + 1;
        } else {
            return position[1] + 1.5;
        }
    }

    public double getEyeZ() {
        if (avatarView) {
           return myAvatar.getZ()-(distance * Math.cos(Math.toRadians(myAvatar.getRotY())));
        } else {
            return position[2];
        }
    }

    public double getCenterX() {
        if (avatarView) {
            return myAvatar.getX();
        } else {
            return position[0] + lineofsight[0];
        }
    }

    public double getCenterZ() {
        if (avatarView) {
            return myAvatar.getZ();
        } else {
            return position[2] + lineofsight[2];
        }
    }

    public boolean isAvatarMode() {
        return avatarView;
    }

    public void drawAv(GL2 gl) {
        myAvatar.drawAvatar(gl);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (avatarView) {
                    myAvatar.tpsMoveUp();
                    position[0] = myAvatar.getX();
                    position[2] = myAvatar.getZ();
                } else {
                    position[0] += lineofsight[0] * 0.1;
                    position[2] += lineofsight[2] * 0.1;
                    myAvatar.changeFpsPos(position[0], position[2]);
                }
                if (inMap()) {
                    position[1] = myTerrain.altitude(position[0], position[2]) + 0.5;
                    myAvatar.setY(position[1]);
                }
                break;

            case KeyEvent.VK_DOWN:
                if (avatarView) {
                    myAvatar.tpsMoveDown();
                    position[0] = myAvatar.getX();
                    position[2] = myAvatar.getZ();
                } else {
                    position[0] -= lineofsight[0] * 0.1;
                    position[2] -= lineofsight[2] * 0.1;
                    myAvatar.changeFpsPos(position[0], position[2]);
                }
                if (inMap()) {
                    position[1] = myTerrain.altitude(position[0], position[2]) + 0.5;
                    myAvatar.setY(position[1]);
                }
                break;

            case KeyEvent.VK_LEFT:
                if (avatarView) {
                    rotateY -= 0.02;
                } else {
                    rotateY -= 0.1;
                }
                lineofsight[0] = Math.sin(rotateY);
                lineofsight[2] = -Math.cos(rotateY);
                myAvatar.rotate(1.5);
                break;

            case KeyEvent.VK_RIGHT:
                if (avatarView) {
                    rotateY += 0.02;
                } else {
                    rotateY += 0.1;
                }
                lineofsight[0] = Math.sin(rotateY);
                lineofsight[2] = -Math.cos(rotateY);
                myAvatar.rotate(-1.5);
                break;

            case KeyEvent.VK_A:
                avatarView = !avatarView;
                break;
        }
    }

    public boolean inMap() {
        double x = position[0];
        double z = position[2];
        double size = myTerrain.size().width-1;
        return x >= 0 && z >= 0 && z < size && x < size;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
