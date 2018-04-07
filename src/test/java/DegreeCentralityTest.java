package catworks;

// Project import statements.
import catworks.networks.*;
import catworks.networks.metrics.*;
import catworks.phenomena.*;
import catworks.simulations.*;

// Additional import statements.
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.*;

public class DegreeCentralityTest {

    // Test Class Data Members:
    private Network   network;
    private Integer[] expected;
    private Integer[][] matrix;
    private DegreeCentrality metric;

    @Before
    public void initialize() {
        metric = new DegreeCentrality();
    }

    @Test
    public void DegreeCentralityTest() {
        // Test Case 1.
        matrix = new Integer[][] {
            {0, 0, 1, 1, 0},
            {0, 0, 1, 1, 0},
            {1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0},
            {1, 1, 1, 1, 0},
        };
        network  = new Network(matrix);
        expected = new Integer[] {5, 4, 7, 6, 4};
        try {
            assertEquals(expected, network.getCentralities(metric));
        }
        catch (Exception e) {
            System.out.println("Log: Test failed.");
        }
    }

}
