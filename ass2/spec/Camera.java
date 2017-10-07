package ass2.spec;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Camera implements MouseMotionListener, KeyListener {
    private double rotateX = 0;
    private double rotateY = 0;
    private double[] position;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    private double s = 1;

    public Camera(){
        position = new double[3];
        position[0] = 2.57;
        position[1] = -2;
        position[2] = 1;
    }

    public double getRotateX() {
        return rotateX;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
    }

    public double getRotateY() {
        return rotateY;
    }

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
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();

        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            rotateY += dx * ROTATION_SCALE;
            rotateX += dy * ROTATION_SCALE;
            System.out.println(rotateY);
        }

        myMousePoint = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                position[0] -= 0.1;
                break;
            case KeyEvent.VK_S:
                position[0] += 0.1;
                break;

            case KeyEvent.VK_A:
                position[2] += 0.1;
                break;

            case KeyEvent.VK_D:
                position[2] -= 0.1;
                break;
//
//            case KeyEvent.VK_W:
//                position[1] -= 0.1;
//                break;
//
//            case KeyEvent.VK_S:
//                position[1] += 0.1;
//                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
