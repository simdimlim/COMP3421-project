package ass2.spec;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Tree
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    private double myX;
    private double myY;
    private double myZ;
    private double trunkHeight;
    private double leavesRadius;
    private Texture trunkTexture;
    private Texture leafTexture;


    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;

        myX = x;
        myY = y;
        myZ = z;

        trunkHeight = 1.75;
        leavesRadius = 0.7;
    }

    public void createTexture(GL2 gl){
        trunkTexture = new Texture(gl, "nature_trunk.jpg", "jpg", true);
        leafTexture = new Texture(gl, "leaves.jpg", "jpg", true);
    }


    public double[] getPosition() {
        return myPos;
    }

    public void normalize(double v[])
    {
        double d = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
        if (d != 0.0)
        {
            v[0]/=d;
            v[1]/=d;
            v[2]/=d;
        }
    }

    double getX(double t){
        double x  = Math.cos(2 * Math.PI * t);
        return x;
    }

    double getY(double t){

        double y  = Math.sin(2 * Math.PI * t);
        return y;
    }

    public void draw(GL2 gl) {
        drawTrunk(gl);
        drawLeaves(gl);
    }

    public void drawLeaves(GL2 gl) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, leafTexture.getTextureId());
        double deltaT;
        double radius = leavesRadius;
        int maxStacks = 10;
        int maxSlices = 20;
        //We want t to go from t = -radius to t = radius
        //as we want to revolve a semi-circle around
        //the y-axis.
        deltaT = 0.5/maxStacks;
        int ang;
        int delang = 360/maxSlices;
        double x1,x2,z1,z2,y1,y2;

        for (int i = 0; i < maxStacks; i++)
        {
            double t = -0.25 + i*deltaT;

            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            for(int j = 0; j <= maxSlices; j++)
            {
                ang = j*delang;
                x1=radius * getX(t)*Math.cos((double)ang*2.0*Math.PI/360.0);
                x2=radius * getX(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0);
                y1 = radius * getY(t);

                z1=radius * getX(t)*Math.sin((double)ang*2.0*Math.PI/360.0);
                z2= radius * getX(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);
                y2 = radius * getY(t+deltaT);

                double normal[] = {x1,y1,z1};

                normalize(normal);

                gl.glNormal3dv(normal,0);
                double tCoord = 1.0/maxStacks * i;
                double sCoord = 1.0/maxStacks * j;
                gl.glTexCoord2d(sCoord,tCoord);
                gl.glVertex3d(x1+myX,y1+myY+trunkHeight+(0.85*leavesRadius),z1+myZ);

                normal[0] = x2;
                normal[1] = y2;
                normal[2] = z2;

                normalize(normal);
                gl.glNormal3dv(normal,0);
                tCoord = 1.0/maxStacks * (i+1);
                gl.glTexCoord2d(sCoord,tCoord);
                gl.glVertex3d(x2+myX,y2+myY+trunkHeight+(0.85*leavesRadius),z2+myZ);

            };
            gl.glEnd();
        }
    }

    public void drawTrunk(GL2 gl) {

        gl.glBindTexture(GL2.GL_TEXTURE_2D, trunkTexture.getTextureId());

        int slices = 32;
        double angleIncrement = (Math.PI * 2.0) / slices;
        double yBottom = myY;
        double yTop = myY+trunkHeight;

        gl.glBegin(GL2.GL_QUAD_STRIP);{
            for(int i=0; i<= slices; i++){
                double angle0 = i*angleIncrement;
                double xPos0 = Math.cos(angle0)*0.2;
                double zPos0 = Math.sin(angle0)*0.2;
                double sCoord = 2.0/slices * i; //Or * 2 to repeat label

                gl.glNormal3d(xPos0, 0, zPos0);
                gl.glTexCoord2d(sCoord,1);
                gl.glVertex3d(xPos0+myX,yBottom,zPos0+myZ);
                gl.glTexCoord2d(sCoord,0);
                gl.glVertex3d(xPos0+myX,yTop,zPos0+myZ);

            }
        } gl.glEnd();

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

}