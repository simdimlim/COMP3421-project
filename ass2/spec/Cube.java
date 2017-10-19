package ass2.spec;


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.GL_TEXTURE_2D;

/*
 *
 * This is the other object that will be placed in the world, a simple cube
 * VBO using shaders.
 */
public class Cube {

    private double[] position;

    private float vertex[] = {
            1,1,1,    -1,1,1,    -1,-1,1,    1,-1,1,        // v0-v1-v2-v3
            1,1,1,     1,-1,1,    1,-1,-1,   1,1,-1,        // v0-v3-v4-v5
            1,1,1,     1,1,-1,   -1,1,-1,   -1,1,1,         // v0-v5-v6-v1
            -1,1,1,    -1,1,-1,   -1,-1,-1,  -1,-1,1,		// v1-v6-v7-v2
            -1,-1,-1,   1,-1,-1,   1,-1,1,   -1,-1,1,        // v7-v4-v3-v2
            1,-1,-1,  -1,-1,-1,  -1,1,-1,    1,1,-1
    };	    // v4-v7-v6-v5

    private float colorsCube[] =     {
            1,0,0, 1,0,0, 1,0,0, 1,0,0,
            1,0,0, 1,0,0, 1,0,0, 1,0,0,
            1,0,0, 1,0,0, 1,0,0, 1,0,0,
            1,0,0, 1,0,0, 1,0,0, 1,0,0,
            1,0,0, 1,0,0, 1,0,0, 1,0,0,
            1,0,0, 1,0,0, 1,0,0, 1,0,0
    };

    private FloatBuffer verData = Buffers.newDirectFloatBuffer(vertex);
    private FloatBuffer colData = Buffers.newDirectFloatBuffer(colorsCube);

    //We will be using 2 vertex buffer objects
    private int bufferIds[] = new int[1];


    private static final String VERTEX_SHADER = "AttributeVertex.glsl";
    private static final String FRAGMENT_SHADER = "AttributeFragment.glsl";

    private int shaderprogram;

    public Cube(double x, double y, double z){
        position = new double[3];
        position[0] = x;
        position[1] = y;
        position[2] = z;
    }

    public void init(GL2 gl){

        gl.glGenBuffers(1, bufferIds, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,
                vertex.length * Float.BYTES +  colorsCube.length* Float.BYTES,
                null,
                GL2.GL_STATIC_DRAW);
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, vertex.length*Float.BYTES, verData);
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, vertex.length*Float.BYTES, colorsCube.length*Float.BYTES, colData);



        try {
            shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void draw(GL2 gl){
        gl.glPushMatrix();

        gl.glUseProgram(shaderprogram);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

        int vertexColLoc = gl.glGetAttribLocation(shaderprogram,"vertexCol");
        int vertexPosLoc = gl.glGetAttribLocation(shaderprogram,"vertexPos");

        // Specify locations for the co-ordinates and color arrays.
        gl.glEnableVertexAttribArray(vertexPosLoc);
        gl.glEnableVertexAttribArray(vertexColLoc);

        gl.glTranslated(position[0], position[1], position[2]);
        gl.glVertexAttribPointer(vertexPosLoc,3, GL.GL_FLOAT, false,0, 0); //last num is the offset
        gl.glVertexAttribPointer(vertexColLoc,3, GL.GL_FLOAT, false,0, vertex.length*Float.BYTES);

        gl.glDrawArrays(GL2.GL_QUADS, 0, 24);
        gl.glUseProgram(0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glPopMatrix();
    }

    // draw the cube in immediate mode, just checking if it works
    public void drawCube(GL2 gl){
        gl.glDisable(GL_TEXTURE_2D);
        gl.glPushMatrix();
        gl.glTranslated(position[0], position[1], position[2]);
        gl.glBegin(GL2.GL_QUADS);
        // front

        gl.glColor3f(1, 1, 1);
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(1, 0, 0);
        gl.glVertex3d(1, 1, 0);
        gl.glVertex3d(0, 1, 0);
        // back
        gl.glColor3f(1, 1, 0);
        gl.glVertex3d(0, 0, -1);
        gl.glVertex3d(0, 1, -1);
        gl.glVertex3d(1, 1, -1);
        gl.glVertex3d(1, 0, -1);


        // top
        gl.glColor3f(1, 0, 0);
        gl.glVertex3d(0, 1, 0);
        gl.glVertex3d(1, 1, 0);
        gl.glVertex3d(1, 1, -1);
        gl.glVertex3d(0, 1, -1);

        // bottom
        gl.glColor3f(0, 1, 0);
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, 0, -1);
        gl.glVertex3d(1, 0, -1);
        gl.glVertex3d(1, 0, 0);

        //left
        gl.glColor3f(0, 1, 1);
        gl.glVertex3d(0, 1, -1);
        gl.glVertex3d(0, 0, -1);
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, 1, 0);

        //right
        gl.glColor3f(0, 0, 1);
        gl.glVertex3d(1, 0, -1);
        gl.glVertex3d(1, 1, -1);
        gl.glVertex3d(1, 1, 0);
        gl.glVertex3d(1, 0, 0);

        gl.glEnd();
        gl.glPopMatrix();
        gl.glEnable(GL_TEXTURE_2D);
    }
}
