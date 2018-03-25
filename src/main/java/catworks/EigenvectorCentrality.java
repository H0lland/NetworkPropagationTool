package catworks;

import org.jblas.*;
import java.util.*;
import static java.lang.System.*;
import java.util.Scanner;

public class EigenvectorCentrality implements Centrality {

    /*
     * Gets the centrality specifically for finding the Eigenvectors of the matrix
     * @param  Integer[][] matrix        original matrix
     * @return             eigenvector values
     */
    public Integer[] getCentralities(Integer[][] matrix) {

        //finds the length of the matrix for later use
        int z = matrix.length;

        //simply puts the matrix created in the main function and puts it into an array of doubles to prepare for JBLAS library
        double[][] new_matrix = new double [z][z];
        for (int i = 0; i < z; i++) {
            for (int j = 0; j < z; j++){
                new_matrix[i][j] = (double) matrix[i][j];
            }
        }

        //uses the jblas library and inserts array of doubles into array type "Double Matrix"
        DoubleMatrix double_matrix = new DoubleMatrix(new_matrix);

        //libraries way of inserting into a matrix that can compute eigenvector
        ComplexDoubleMatrix eigenvalues = Eigen.eigenvalues(double_matrix);

        //creating list that can hold normalised principal version of eigenvector
        List<Double> principalEigenvector = getPrincipalEigenvector(double_matrix);

        System.out.println("Normalised Principal Eigenvector = " + normalised(principalEigenvector));

        //instead of turning matrix back into an array of integers, it's easier to just print it here
        Integer[] centralities = {};
        return centralities;
    }

    /*
     * getPrincipalEigenvector uses the maxIndex and getEigenVector functions to calculate the eigenvectors of the matrix
     * @param  DoubleMatrix double_matrix contains original matrix
     * @return              returns eigenvector matrix
     */
    private static List<Double> getPrincipalEigenvector(DoubleMatrix double_matrix) {
        int maxIndex = getMaxIndex(double_matrix);
        ComplexDoubleMatrix eigenVectors = Eigen.eigenvectors(double_matrix)[0];
        return getEigenVector(eigenVectors, maxIndex);
    }

    /*
     * Finds the max index within the array and chooses is as its focus
     * @param  DoubleMatrix double_matrix
     * @return              returns max index
     */
    private static int getMaxIndex(DoubleMatrix double_matrix) {
        ComplexDouble[] doubleMatrix = Eigen.eigenvalues(double_matrix).toArray();
        int maxIndex = 0;
        for (int i = 0; i < doubleMatrix.length; i++){
            double newnumber = doubleMatrix[i].abs();
            if ((newnumber > doubleMatrix[maxIndex].abs())){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Gets the value of the eigenvector for all the max values obtained earlier using the library
     * @param  ComplexDoubleMatrix eigenvector   matrix containing the original values
     * @param  int                 columnId      picks which nodes to focus on based on the max
     * @return                     eigenvector values
     */
    private static List<Double> getEigenVector(ComplexDoubleMatrix eigenvector, int columnId) {
        ComplexDoubleMatrix column = eigenvector.getColumn(columnId);

        List<Double> values = new ArrayList<Double>();
        for (ComplexDouble value : column.toArray()) {
            values.add(value.abs()  );
        }
        return values;
    }

    /**
     * Normalises the values given in the Eigenvector function
     * @param  List<Double> principalEigenvector matrix with principal values of eigenvector
     * @return              normalised value
     */
    private static List<Double> normalised(List<Double> principalEigenvector) {
        double total = sum(principalEigenvector);
        List<Double> normalisedValues = new ArrayList<Double>();
        for (Double aDouble : principalEigenvector) {
            normalisedValues.add(aDouble / total);
        }
        return normalisedValues;
    }

    /**
     * Finds the total for those principal eigenvector values
     * @param  List<Double> principalEigenvector matrix with principal values of eigenvector
     * @return              total value
     */
    private static double sum(List<Double> principalEigenvector) {
        double total = 0;
        for (Double aDouble : principalEigenvector) {
            total += aDouble;
        }
        return total;
    }






}
