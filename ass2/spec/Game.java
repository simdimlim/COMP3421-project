package ass2.spec;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.MouseMotionListener;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, MouseMotionListener{

    private Terrain myTerrain;
    private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    private double s = 1;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
   
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

          Game s = new Game(myTerrain);
          panel.addGLEventListener(s);
          panel.addMouseMotionListener(s);
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
	public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // rotates on mouse drag
        gl.glRotated(rotateX, 1, 0, 0);
        gl.glRotated(rotateY, 0, 1, 0);

        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);

        gl.glColor4d(1, 1, 1, 1);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        drawTerrain(gl);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
//		gl.glEnable(GL2.GL_LIGHTING);
//		gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        double scale = myTerrain.size().height * 1.2;

        gl.glOrtho(-scale,scale,-scale,scale,-scale,scale);
		
	}

	private void drawTerrain(GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLES);
            for (int z = 0; z < myTerrain.size().height - 1; z++) {
                for (int x = 0; x < myTerrain.size().width - 1; x++) {
                    gl.glVertex3d(x+1, myTerrain.getGridAltitude(x+1,z), z);
                    gl.glVertex3d(x, myTerrain.getGridAltitude(x,z), z);
                    gl.glVertex3d(x, myTerrain.getGridAltitude(x,z+1), z+1);

                    gl.glVertex3d(x+1, myTerrain.getGridAltitude(x+1,z), z);
                    gl.glVertex3d(x, myTerrain.getGridAltitude(x,z+1), z+1);
                    gl.glVertex3d(x+1, myTerrain.getGridAltitude(x+1,z+1), z+1);
                }
            }
        gl.glEnd();

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

        }

        myMousePoint = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMousePoint = e.getPoint();
    }
}
