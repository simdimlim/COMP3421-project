package ass2.spec;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;
    private Camera camera;
    private Lighting myLighting;
    private Avatar myAvatar;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        myAvatar = new Avatar();
        camera = new Camera(myAvatar, myTerrain);
        myLighting = new Lighting(myAvatar, camera, myTerrain);
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel panel = new GLJPanel();
        panel.addGLEventListener(this);

        // Add an animator to call 'display' at 60fps
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
        panel.addGLEventListener(this);
        panel.addKeyListener(camera);
        panel.addKeyListener(myLighting);
        panel.setFocusable(true);

        getContentPane().add(panel);
        setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        GLU glu = new GLU();

        // set up the camera
        glu.gluLookAt(	camera.getEyeX(), camera.getY(), camera.getEyeZ(),
                camera.getCenterX(), camera.getY(),  camera.getCenterZ(),
                0.0f, 1.0f,  0.0f);

        // set up lighting
        setLighting(gl);

        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        gl.glColor4d(1, 1, 1, 1);

        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTerrain.getTerrainTexture().getTextureId());

        myTerrain.draw(gl);

        camera.drawAv(gl);
	}

	public void setLighting(GL2 gl){
	    myLighting.setLighting(gl, myTerrain.getSunlight());
    }

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        gl.glEnable(GL2.GL_DEPTH_TEST);

        gl.glEnable(GL2.GL_TEXTURE_2D);

        myTerrain.createTexture(gl);
        myTerrain.setCubeVBO(gl);
        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        // turn on a light. Use default settings.
        gl.glEnable(GL2.GL_LIGHT0);

        // normalise normals
        gl.glEnable(GL2.GL_NORMALIZE);

        myTerrain.setNormals();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU glu = new GLU();
        glu.gluPerspective(70, 1, 1, 50);

	}

}
