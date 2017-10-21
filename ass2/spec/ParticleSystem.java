package ass2.spec;

import java.util.Random;
import com.jogamp.opengl.GL2;



/**
 * Taken from NeHe Lesson #19a: Fireworks
 */
public class ParticleSystem {

    //private GLU glu;  // for the GL Utility

    private static final int MAX_PARTICLES = 10000; // max number of particles
    private Particle[] particles;

    private static boolean enabledBurst = false;
    private int zoom = -40;
    private int slowdown = 2;
    // Pull forces in each direction
    private static float gravityY = -0.0008f; // gravity

    // Global speed for all the particles
    private static float speedYGlobal = 0.1f;
    private static float z = -40.0f; //zOffset
    private static float y = 5.0f;   //yOffset

    private double rainWidth;
    private double rainHeight;

    public ParticleSystem(int width, int height){
        rainWidth = width;
        rainHeight = height;
        particles = new Particle[MAX_PARTICLES];
        createParticles();
    }

    public void createParticles() {
        // Initialize the particles
        for (int i = 0; i < MAX_PARTICLES; i++) {
            particles[i] = new Particle();
        }
    }

    public void draw(GL2 gl ) {
        // Render the particles
        gl.glPushMatrix();
        for (int i = 0; i < MAX_PARTICLES; i = i +2) {
            if (particles[i].active) {
                // Draw the particle using our RGB values

                gl.glColor3f(0.5f, 0.5f, 1.0f);

                gl.glBegin(GL2.GL_LINES);

                double px = particles[i].x;
                double py = particles[i].y;
                double pz = particles[i].z;

                gl.glVertex3d(px, py, pz);
                gl.glVertex3d(px, py+0.5, pz);
                gl.glEnd();

                // Move the particle
                // Move the particle
                particles[i].x += particles[i].speedX;
                particles[i].y += particles[i].speedY;
                particles[i].z += particles[i].speedZ;

                // Apply the gravity force on y-axis
                particles[i].speedY += gravityY;

                // Apply the gravity force on y-axis
                particles[i].speedY += gravityY;

                // Slowly kill it
                particles[i].life -= 0.002;

                if (particles[i].y <= -10){
                    particles[i].life = -1;
                }

                if (particles[i].life < 0.0){
                    particles[i] = new Particle();
                }
            }
        }
        gl.glPopMatrix();
    }

    class Particle {
        boolean active; // always active in this program
        double life;     // how alive it is
        double r, g, b;  // color
        double x, y, z;  // position
        double speedX, speedY, speedZ; // speed in the direction

        private Random rand = new Random();

        // Constructor
        public Particle() {
            active = true;
            life = 1.0;
            x = (Math.random()*10 % rainWidth);
            y = (Math.random()*10 % 10);
            z = (Math.random()*10 % rainHeight);
            float maxSpeed = 0.1f;
            float speed = 0.02f + (rand.nextFloat() - 0.5f) * maxSpeed;
            float angle = (float)Math.toRadians(rand.nextInt(360));

            speedX = speed * (float)Math.cos(angle);
            speedY = speed * (float)Math.sin(angle) + speedYGlobal;
            speedZ = (rand.nextFloat() - 0.5f) * maxSpeed;

//            System.out.println(x + " " + 5 + " " + z);
            speedY = 0.0;
        }
    }
}
