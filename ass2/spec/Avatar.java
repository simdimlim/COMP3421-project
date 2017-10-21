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

    public double getDirectionX() {
        return Math.sin(Math.toRadians(rotY));
    }
    public double getDirectionZ() {
        return Math.cos(Math.toRadians(rotY));
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
        float[] rhoA = {0.25f,0.25f,0.25f};
        float[] rhoD = {0.4f,0.4f,0.4f};
        float[] rhoS = {0.774597f,0.774597f,0.774597f};

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rhoA, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, rhoD, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rhoS, 0);

        // Set the shininess (i.e. the Phong exponent)
        float shine = (float) (0.6 * 128.0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shine);

        // Draw the model

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glCullFace(GL2.GL_BACK);

        gl.glTranslated(position[0], position[1], position[2]);
        gl.glRotated(rotY, 0, 1, 0);
        gl.glScaled(0.25,0.25,0.25);

        gl.glFrontFace(GL2.GL_CW);
        glut.glutSolidTeapot(1);
        gl.glFrontFace(GL2.GL_CCW);
    }

}
