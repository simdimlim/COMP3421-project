package ass2.spec;


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.jogamp.opengl.GL.GL_TEXTURE_2D;

/*
 *
 * This is the other object that will be placed in the world, a simple cube
 * VBO using shaders.
 */
public class Cube {

    private double[] position;
//    private float[] vertexList = {
//            0,0,0,
//            1,0,0,
//            1,1,0,
//            0,1,0,
//
//            1,1,0,
//            0,0,-1,
//            0,1,-1,
//            1,0,-1,
//
//            0,1,0,
//            1,1,0,
//            1,1,-1,
//            0,1,-1,
//
//            0,0,0,
//            0,0,-1,
//            1,0,-1,
//            1,0,0,
//
//            0,1,-1,
//            0,0,-1,
//            0,0,0,
//            0,1,0,
//
//            1,0,-1,
//            1,1,-1,
//            1,1,0,
//            1,0,0
//    };

    private float[] vertexList =  {
                0,1,-1,
                -1,-1,-1,
                1,-1,-1,
                0, 2,-4,
                -2,-2,-4,
                2,-2,-4
    };

    private float colors[] =     {
            1,0,0,
            0,1,0,
            1,1,1,
            0,0,0,
            0,0,1,
            1,1,0
    };

    private short indexes[] = {0,1,5,3,4,2};

    //These are not vertex buffer objects, they are just java containers
    private FloatBuffer  posData= Buffers.newDirectFloatBuffer(vertexList);
    private FloatBuffer colorData = Buffers.newDirectFloatBuffer(colors);
    private ShortBuffer indexData = Buffers.newDirectShortBuffer(indexes);

    //We will be using 2 vertex buffer objects
    private int bufferIds[] = new int[2];


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
        //Generate 2 VBO buffer and get their IDs
        gl.glGenBuffers(2,bufferIds,0);

        //This buffer is now the current array buffer
        //array buffers hold vertex attribute data
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

        //This is just setting aside enough empty space
        //for all our data
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,    //Type of buffer
                vertexList.length * Float.BYTES +  colors.length* Float.BYTES, //size needed
                null,    //We are not actually loading data here yet
                GL2.GL_STATIC_DRAW); //We expect once we load this data we will not modify it


        //Actually load the positions data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, //From byte offset 0
                vertexList.length*Float.BYTES,posData);

        //Actually load the color data
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
                vertexList.length*Float.BYTES,  //Load after the position data
                colors.length*Float.BYTES,colorData);


        //Now for the element array
        //Element arrays hold indexes to an array buffer
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);

        //We can load it all at once this time since there are not
        //two separate parts like there was with color and position.
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
                indexes.length *Short.BYTES,
                indexData, GL2.GL_STATIC_DRAW);


        try {
            shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void draw(GL2 gl){

        gl.glUseProgram(shaderprogram);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

        int vertexColLoc = gl.glGetAttribLocation(shaderprogram,"vertexCol");
        int vertexPosLoc = gl.glGetAttribLocation(shaderprogram,"vertexPos");

        // Specify locations for the co-ordinates and color arrays.
        gl.glEnableVertexAttribArray(vertexPosLoc);
        gl.glEnableVertexAttribArray(vertexColLoc);
        gl.glVertexAttribPointer(vertexPosLoc,3, GL.GL_FLOAT, false,0, 0); //last num is the offset
        gl.glVertexAttribPointer(vertexColLoc,3, GL.GL_FLOAT, false,0, vertexList.length*Float.BYTES);

        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);
        gl.glTranslated(position[0], position[1], position[2]);
        gl.glDrawElements(GL2.GL_TRIANGLES, 6, GL2.GL_UNSIGNED_SHORT,0);
        gl.glUseProgram(0);

        //Un-bind the buffer.
        //This is not needed in this simple example but good practice
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,0);
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
