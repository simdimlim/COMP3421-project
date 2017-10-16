package ass2.spec;

public class MatrixMath {

    // method to find the cross product given 2 vectors
    public static double[] crossProduct(double[] u, double[] v){
        double[] cross = new double[3];

        cross[0] = u[1]*v[2] - u[2]*v[1];
        cross[1] = u[2]*v[0] - u[0]*v[2];
        cross[2] = u[0]*v[1] - u[1]*v[0];

        return cross;
    }
}
