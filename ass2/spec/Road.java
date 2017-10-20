package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    private Texture roadTexture;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t, Terrain terrain) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[3];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[2] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;
        p[1] = terrain.altitude(p[0], p[2]) + 0.1;

        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    /*
     * calculates the coffecient using the berstein formula for m = 2 and k = power
     * bernstein formula = C(m, k) * t^k * (1-k)^(m-k)
     * @param power, value
     * @return double
     */
    private double bernsteinCoefficient(int power, double value){
        switch (power){
            case 0:
                return Math.pow(1 - value, 2);
            case 1:
                return 2 * (1 - value) * value;
            case 2:
                return Math.pow(value, 2);
        }

        throw new IllegalArgumentException("" + value);
    }

    /*
     * Returns the 2d tangent to some point
     * this is using the formula of the derivative to a berzier curve
     * when m = 3
     * @param point
     * @return double
     */
    private double[] getTangent(double point){
        int i = (int)Math.floor(point);
        double t = point - i;

        i *= 6;

        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);

        double[] tangent = new double[2];

        tangent[0] = bernsteinCoefficient(0, t) * (x1 - x0)
                + bernsteinCoefficient(1, t) * (x2 - x1)
                + bernsteinCoefficient(2, t) * (x3 - x2);

        tangent[1] = bernsteinCoefficient(0, t) * (y1 - y0)
                + bernsteinCoefficient(1, t) * (y2 - y1)
                + bernsteinCoefficient(2, t) * (y3 - y2);
        return tangent;
    }

    private double getMagnitude(double[] vector){
        double mag = 0;
        for (int i = 0; i < vector.length; i++){
            mag += Math.pow(vector[i], 2);
        }
        return Math.sqrt(mag);
    }

    private double[] normalise(double[] tangent, double[] normal){
        double magnitude = getMagnitude(tangent);
        double[] normalised = {normal[0]/magnitude, normal[1]/magnitude};
        return normalised;
    }

    public void draw(GL2 gl, Terrain terrain){
        gl.glPushMatrix();
        roadTexture = new Texture(gl, "road2.jpg", "jpg");
        gl.glBindTexture(GL2.GL_TEXTURE_2D, roadTexture.getTextureId());

        double width = myWidth/2;
        // Material property vectors.
//        float matAmbAndDif1[] = {0.7f, 0.2f, 0.7f, 1.0f};
//        float matAmbAndDif2[] = {0f, 1f, 0f, 1.0f};
//        float matSpec1[] = {0.2f, 0.2f, 0.2f, 1f};
//
//        float matShine[] = {150.0f};
//
//        //Set front and back to have different colors to make debugging easier
//        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
//        gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
//        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec1,0);
//        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
//
//
//        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 80);	// phong
//
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        gl.glColor3f(1,1,1);
        for(double i = 0; i < this.size(); i += 0.002){
            // get a point on the spine
            double[] spinePoint = point(i, terrain);

            // find tangent at that point
            double[] tangentPoint = getTangent(i);

            // find the normal and normalise it
            double[] normalPoint = {-tangentPoint[1], tangentPoint[0]};
            normalPoint = normalise(tangentPoint, normalPoint);
            normalPoint[0] *= width;
            normalPoint[1] *= width;

            // draw the left and right points
            gl.glNormal3d(0, 1, 0);
            gl.glTexCoord2d(spinePoint[0] - normalPoint[0],
                        spinePoint[2] - normalPoint[1]);
            gl.glVertex3d(
                    spinePoint[0] - normalPoint[0],
                    spinePoint[1],spinePoint[2] - normalPoint[1]
            );
            gl.glTexCoord2d(spinePoint[0] + normalPoint[0], spinePoint[2] + normalPoint[1]);
            gl.glVertex3d(
                    spinePoint[0] + normalPoint[0],
                    spinePoint[1],spinePoint[2] + normalPoint[1]
            );

        }

        gl.glEnd();
        gl.glPopMatrix();
    }

}
