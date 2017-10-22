package ass2.spec;

import com.jogamp.opengl.GL2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lighting implements KeyListener {

    //Setting for light 0
    private float a = 0.9f; // Ambient white light intensity.
    private float d = 0.9f; // Diffuse white light intensity
    private float s = 0.2f; // Specular white light intensity.
    private float g = 0.9f;
    private int localViewer = 0;
    private boolean nightMode;
    private boolean torchOn;
    private Avatar myAvatar;
    private Camera myCamera;
    private Terrain myTerrain;
    private int hour;

    public Lighting(Avatar myAvatar, Camera myCamera, Terrain myTerrain) {
        nightMode = false;
        torchOn = false;
        this.myAvatar = myAvatar;
        this.myCamera = myCamera;
        // get current time of the day as an integer
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("kk");
        hour = Integer.parseInt(sdf.format(cal.getTime()));
        System.out.println(hour);
        this.myTerrain = myTerrain;
    }

    public void setLighting(GL2 gl, float[] sunlight){

        // determine ambient and diffuse depending on time of day
        if (nightMode) {
            a = 0.3f;
            d = 0.3f;
        } else if (hour >= 4 && hour < 7) {
            a = 0.5f;
            d = 0.5f;
        } else if (hour >= 7 && hour < 17) {
            a = 0.9f;
            d = 0.9f;
        } else if (hour >= 17 && hour < 20) {
            a = 0.5f;
            d = 0.5f;
        } else {
            a = 0.3f;
            d = 0.3f;
        }

        gl.glEnable(GL2.GL_LIGHT0);

        // Light property vectors.
        float lightAmb[] = { a, a, a, 1.0f };
        float lightDif0[] = { d, d, d, 1.0f };
        float lightSpec0[] = { s, s, s, 1.0f };

        float globAmb[] = { g, g, g, 1.0f };

        // Light0 properties.
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif0,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec0,0);

        // Global light properties
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint
        drawDirectionLight(gl, sunlight);
    }

    public void drawDirectionLight(GL2 gl, float[] sunlight){

        // determine position of the sun depending on the time of day
        float[] lightPos0 = {sunlight[0], sunlight[1], sunlight[2], 0};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0,0);

        if (torchOn) {
            // Light property vectors.
            float lightAmb[] = {0.0f, 0.0f, 0.0f, 1.0f};
            float lightDifAndSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};

            // Light properties.
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDifAndSpec,0);
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightDifAndSpec,0);

            gl.glEnable(GL2.GL_LIGHT1); // Enable particular light source.

            float spotAngle = 10.0f; // Spotlight cone half-angle.

            if (myCamera.isAvatarMode()) {
                // torch points in direction avatar is facing
                float spotDirection[] = {(float)myAvatar.getDirectionX(), 0.0f, (float)myAvatar.getDirectionZ()};
                gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spotDirection,0);
            } else {
                // torch points in direction the camera is facing
                float spotDirection[] = {(float)myCamera.getCenterX(), 0.0f, (float)myCamera.getCenterZ()};
                gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spotDirection,0);
            }

            float spotExponent = 2.0f; // Spotlight exponent = attenuation factor.

            float lightPos[] = {(float) (myAvatar.getX()),
                    (float) (myAvatar.getY()+0.2),
                    (float) (myAvatar.getZ()), 1.0f}; // Spotlight position.

            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos,0);
            gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, spotAngle);
            gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, spotExponent);

        } else {
            // turn off the torch
            gl.glDisable(GL2.GL_LIGHT1);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_N:
                nightMode = !nightMode;
                break;
            case KeyEvent.VK_T:
                torchOn = !torchOn;
                break;
            case KeyEvent.VK_L:
                hour = (hour + 2) % 24;
                System.out.println(hour);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
