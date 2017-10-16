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

        trunkHeight = 2;
        leavesRadius = 0.5;
    }

    public void createTexture(GL2 gl){
        trunkTexture = new Texture(gl, "nature_trunk.jpg", "jpg");
        leafTexture = new Texture(gl, "grass.bmp", "bmp");
    }


    public double[] getPosition() {
        return myPos;
    }

    double [] makeProfileNormals(double[] x, double []y){
        double[] n = new double[x.length*2];
        double[] v0 = new double[2];
        double[] v1 = new double[2];
        for(int i = 0; i < x.length; i++){
            if (i == 0) {
                v0[0] = x[i];
                v0[1] = y[i];
                v1[0] = x[i+1];
                v1[1] = y[i+1];

            } else if (i == x.length-1) {
                v0[0] = x[i-1];
                v0[1] = y[i-1];
                v1[0] = x[i];
                v1[1] = y[i];
            } else {
                v0[0] = x[i-1];
                v0[1] = y[i-1];
                v1[0] = x[i+1];
                v1[1] = y[i+1];
            }

            double dx = v1[0] - v0[0];
            double dy = v1[1] - v0[1];
            double mag = Math.sqrt(dx*dx + dy*dy);
            if(mag != 0){

                dx = dx/mag;
                dy = dy/mag;
            }
            //create a normal from the tangent in 2D
            n[i*2] = dy;
            n[i*2+1] = -dx;
        }
        return n;
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
        createTexture(gl);
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
                gl.glVertex3d(x1+myX,y1+myY+trunkHeight,z1+myZ);
                gl.glTexCoord2d(0,0);

                normal[0] = x2;
                normal[1] = y2;
                normal[2] = z2;

                normalize(normal);
                gl.glNormal3dv(normal,0);
                gl.glVertex3d(x2+myX,y2+myY+trunkHeight,z2+myZ);
                gl.glTexCoord2d(1,1);

            };
            gl.glEnd();
        }
    }

    public void drawTrunk(GL2 gl) {

        gl.glBindTexture(GL2.GL_TEXTURE_2D, trunkTexture.getTextureId());

        int theta,i;
        int dtheta = 5;
        double x1,x2,z1,z2;

        //Cylinder
        double[] x = {0,0.1,0.1,0};
        double[] y = {0,0,trunkHeight,trunkHeight};

        double[] n = makeProfileNormals(x,y);

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        for (i=0; i < x.length-1 ; i++) {
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            for (theta = 0; theta <= 360; theta += dtheta) {

                x1 = x[i] * Math.cos((double) theta * 2.0 * Math.PI / 360.0);
                x2 = x[i + 1] * Math.cos((double) theta * 2.0 * Math.PI / 360.0);
                z1 = x[i] * Math.sin((double) theta * 2.0 * Math.PI / 360.0);
                z2 = x[i + 1] * Math.sin((double) theta * 2.0 * Math.PI / 360.0);

                //Use same approach of revolution for the 2d normals
                double normal[] = new double[3];
                normal[0] = n[i*2]* Math.cos((double)theta*2.0*Math.PI/360.0);
                normal[1] = n[i*2+1];
                normal[2] = n[i*2]* Math.sin((double)theta*2.0*Math.PI/360.0);
                normalize(normal);

                gl.glNormal3dv(normal,0);
                //Just use the y from the profile as
                //we are revolving around the y-axis

                gl.glVertex3d(x1+myX,y[i]+myY,z1+myZ);
                gl.glTexCoord2d(0,0);

                normal[0] = n[(i+1)*2]* Math.cos((double)theta*2.0*Math.PI/360.0);
                normal[1] = n[(i+1)*2+1];
                normal[2] = n[(i+1)*2]* Math.sin((double)theta*2.0*Math.PI/360.0);

                normalize(normal);
                gl.glNormal3dv(normal,0);

                gl.glVertex3d(x2+myX,y[i+1]+myY,z2+myZ);
                gl.glTexCoord2d(1,0);

            };
            gl.glEnd();
        }
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

}
