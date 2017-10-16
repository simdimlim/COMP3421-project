package ass2.spec;

import com.jogamp.opengl.GL2;

public class Lighting {

    //Setting for light 0
    private float a = 0.6f; // Ambient white light intensity.
    private float d = 0.5f; // Diffuse white light intensity
    private float s = 0.2f; // Specular white light intensity.
    private float g = 0.2f;
    private int localViewer = 0;

    public Lighting() {

    }

    public void setLighting(GL2 gl, float[] sunlight){
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
        float[] lightPos0 = {sunlight[0], sunlight[1], sunlight[2], 0};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0,0);
    }
}
