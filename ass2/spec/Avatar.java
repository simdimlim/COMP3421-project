package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
    private double[] position;
    private double rotY = 0;
    private double stepDistance = 0.1;

    public Avatar() {
        position = new double[3];
        position[0] = 0;
        position[1] = 0;
        position[2] = 0;
    }

    public double getRotY() {
        return rotY;
    }

    public double getX() {
        return position[0];
    }

    public double getY() {
        return position[1];
    }

    public double getZ() {
        return position[2];
    }

    public void rotate(double r) {
        rotY += r;
    }

    public void setY(double y) {
        position[1] = y;
    }

    public void tpsMoveUp() {
        position[0] += stepDistance * Math.sin(Math.toRadians(rotY));
        position[2] += stepDistance * Math.cos(Math.toRadians(rotY));
    }

    public void tpsMoveDown() {
        position[0] -= stepDistance * Math.sin(Math.toRadians(rotY));
        position[2] -= stepDistance * Math.cos(Math.toRadians(rotY));
    }


    public void changeFpsPos(double x, double z) {
        position[0] = x;
        position[2] = z;
    }


    public void drawAvatar(GL2 gl) {
        GLUT glut = new GLUT();

        //Get and set material coefficients
//        float[] rhoA = {0.8f,0,0};
//        float[] rhoD = {0.8f,0,0};
//        float[] rhoS = {1f,1f,1f,1f};
//
//        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rhoA, 0);
//        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, rhoD, 0);
//        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rhoS, 0);
//
//        // Set the shininess (i.e. the Phong exponent)
//
//        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 75);

        // Draw the model
        gl.glTranslated(position[0], position[1], position[2]);
        gl.glRotated(rotY, 0, 1, 0);
        gl.glScaled(0.25,0.25,0.25);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glFrontFace(GL2.GL_CW);
        glut.glutSolidTeapot(1);
        gl.glFrontFace(GL2.GL_CCW);

    }

}
