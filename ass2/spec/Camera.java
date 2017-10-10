package ass2.spec;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Camera implements KeyListener {
    private double rotateX = 0;
    private double rotateY = 124;
    private double[] position;
    private double[] lineofsight;

    public Camera(){
        position = new double[3];
        position[0] = 2.57;
        position[1] = 1;
        position[2] = 1;

        lineofsight = new double[3];
        lineofsight[0] = 0;
        lineofsight[1] = 1;
        lineofsight[2] = -1;
    }

    public double getLineOfSightX() {
        return lineofsight[0];
    }

    public double getLineOfSightZ() {
        return lineofsight[2];
    }

    public double getRotateX() {
        return rotateX;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
    }

    public double getRotateY() { return rotateY; }

    public void setRotateY(double rotateY) {
        this.rotateY = rotateY;
    }

    public double getPosX(){
        return position[0];
    }

    public double getPosY(){
        return position[1];
    }

    public double getPosZ(){
        return position[2];
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            // i dont know why this works but lts go with it
            case KeyEvent.VK_UP:
                position[0] += lineofsight[0]*0.1;
                position[2] += lineofsight[2]*0.1;
                break;

            case KeyEvent.VK_DOWN:
                position[0] -= lineofsight[0]*0.1;
                position[2] -= lineofsight[2]*0.1;
                break;

            case KeyEvent.VK_LEFT:
                rotateY -= 0.1;
                lineofsight[0] = Math.sin(rotateY);
                lineofsight[2] = -Math.cos(rotateY);

                break;

            case KeyEvent.VK_RIGHT:
                rotateY += 0.1;
                lineofsight[0] = Math.sin(rotateY);
                lineofsight[2] = -Math.cos(rotateY);
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
