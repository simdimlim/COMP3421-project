package ass2.spec;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    private Texture terrainTexture;
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {

        // check if both doubles are actually integers
        if (x % 1 == 0 && z % 1 == 0) {
            return getGridAltitude((int) x, (int) z);
        }

        double altitude = 0;

        // closest integer < z
        int z1 = (int) Math.floor(z);
        // closest integer > z
        int z2 = (int) Math.ceil(z);

        // closest integer < x
        int x1 = (int) Math.floor(x);
        // closest integer > x
        int x2 = (int) Math.ceil(x);

        // depth at (x1, z1)
        double Dz1 = getGridAltitude(x1,z1);
        // depth at (x2, z2)
        double Dz2 = getGridAltitude(x2,z2);

        /*

        z2 _________ .
                    /|
                   / |
                  /  |
         z _____ / . |
                / x,z|
               /     |
           __ /______|
        z1    |   |  |
              x1  x  x2

        */

        // interpolation of y points
        // depth of the left side of triangle
        double DL = (((z - z1)/(z2 - z1)) * Dz2) + (((z2 - z)/(z2 - z1)) * Dz1);

        // depth at (x2, z1)
        Dz1 = getGridAltitude(x2,z1);

        // depth of the right side of triangle
        double DR = (((z - z1)/(z2 - z1)) * Dz2) + (((z2 - z)/(z2 - z1)) * Dz1);

        // interpolation of x points
        // depth at (x,z)
        altitude = (((x - x1)/(x2 - x1)) * DR) + ((x2 - x)/(x2 - x1)) * DL;

        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }

    public void createTexture(GL2 gl){
        terrainTexture = new Texture(gl, "grass.bmp", "bmp");
    }

    public Texture getTerrainTexture(){
        return terrainTexture;
    }
    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


    void drawTerrain(GL2 gl){

        for (int z = 0; z < mySize.height - 1; z++){
            for (int x = 0; x < mySize.width - 1; x++){
                gl.glBegin(GL2.GL_TRIANGLES);
                gl.glVertex3d(x+1, getGridAltitude(x+1,z), z);
                gl.glTexCoord2d(0,0);
                gl.glVertex3d(x, getGridAltitude(x,z), z);
                gl.glTexCoord2d(1,1);
                gl.glVertex3d(x, getGridAltitude(x,z+1), z+1);
                gl.glTexCoord2d(1,0);

                gl.glVertex3d(x+1, getGridAltitude(x+1,z), z);
                gl.glTexCoord2d(0,0);
                gl.glVertex3d(x, getGridAltitude(x,z+1), z+1);
                gl.glTexCoord2d(1,1);
                gl.glVertex3d(x+1, getGridAltitude(x+1,z+1), z+1);
                gl.glTexCoord2d(1,0);
                gl.glEnd();
            }
        }

        for (Tree t : myTrees) {
            t.draw(gl);
        }
    }
}
