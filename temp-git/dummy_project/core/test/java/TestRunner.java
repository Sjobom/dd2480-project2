import com.decide.app.*;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;


public class TestRunner {

    // (Example testcase template)
    //
    //      @Test
    //      public void testSomethingStupid() {
    //          // Contract: test if this is a stupid testcase
    //          assertTrue(true);
    //      }

    @Test
    public void testCmv0() {
      //contract: verifies that method for condition cmv0 is correct
      Launch launchData = new Launch();
      launchData.setNumPoints(4);
      Parameters params = new Parameters();
      ArrayList<Integer> xCoords = new ArrayList<Integer>();
      xCoords.add(2);
      xCoords.add(3);
      xCoords.add(5);
      xCoords.add(10);
      ArrayList<Integer> yCoords = new ArrayList<Integer>();
      yCoords.add(2);
      yCoords.add(3);
      yCoords.add(5);
      yCoords.add(10);
      launchData.setXCoords(xCoords);
      launchData.setYCoords(yCoords);

      //There are no 2 consecutive points who are further apart than length1=100
      params.length1 = 100;
      launchData.setParams(params);
      assertFalse(new CMV(launchData).cmv0());

      //There ARE 2 consecutive points who are farter than length1=1 apart
      params.length1 = 1;
      launchData.setParams(params);
      assertTrue(new CMV(launchData).cmv0());
    }

    @Test
    public void testCmv1() {
        //contract: verifies that method for condition cmv1 is correct
        Launch launchData = new Launch(); //think as area 100*100
        launchData.setNumPoints(3);
        Parameters params = new Parameters();
        ArrayList<Integer> xCoords = new ArrayList<Integer>();
        xCoords.add(0);
        xCoords.add(2);
        xCoords.add(2);
        ArrayList<Integer> yCoords = new ArrayList<Integer>();
        yCoords.add(0);
        yCoords.add(2);
        yCoords.add(0);
        launchData.setXCoords(xCoords);
        launchData.setYCoords(yCoords);

        //There are 3 consecutive datapoints which are inside/on a circle with
        // radius1 = 2
        params.radius1 = 2;
        launchData.setParams(params);
        assertFalse(new CMV(launchData).cmv1());

        //There are NO 3 consecutive datapoints which are inside/on a circle with
        // radius1 = 1
        params.radius1 = 1;
        launchData.setParams(params);
        assertTrue(new CMV(launchData).cmv1());
    }

    @Test
    public void testCmv2() {
        // Contract: there are at least one set of consecutive points such that
        // they form a triangle with the vertex < PI - epsilon or vertex < PI + epsilon
        Launch launch = new Launch();
        launch.setParams(new Parameters());
        CMV cmv = new CMV(launch);

        // verify that epsilon < 0 yields a false
        launch.getParams().epsilon = -1;
        assertFalse(cmv.cmv2());

        // verify that epsilon >= PI  yields a false
        launch.getParams().epsilon = Math.PI;
        assertFalse(cmv.cmv2());
        launch.getParams().epsilon = Math.PI * 2;
        assertFalse(cmv.cmv2());

        // verify that a correct series of points yields a true
        launch.getParams().epsilon = Math.PI / 2;
        launch.setNumPoints(3);
        launch.setXCoords(new ArrayList<>());
        launch.setYCoords(new ArrayList<>());
        // point 1
        launch.getXCoords().add(3);
        launch.getYCoords().add(0);
        // vertex
        launch.getXCoords().add(0);
        launch.getYCoords().add(0);
        // point 2
        launch.getXCoords().add(3);
        launch.getYCoords().add(2);
        assertTrue(cmv.cmv2());

        // verify that a faulty series of points yields a false; epsilon is too large
        launch.getParams().epsilon = 3 * Math.PI / 4;
        launch.setNumPoints(3);
        launch.setXCoords(new ArrayList<>());
        launch.setYCoords(new ArrayList<>());
        // point 1
        launch.getXCoords().add(1);
        launch.getYCoords().add(0);
        // vertex
        launch.getXCoords().add(0);
        launch.getYCoords().add(0);
        // point 2
        launch.getXCoords().add(0);
        launch.getYCoords().add(1);
        assertFalse(cmv.cmv2());

    }

    @Test
    public void testCmv3() {
        // Contract: There are at least one set of three
        // consecutive data points that are the vertices
        // of a triangle with an area greater than AREA1.

        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(0);
        x.add(0);
        x.add(2);
        x.add(1);
        ArrayList<Integer> y = new ArrayList<Integer>();
        y.add(0);
        y.add(0);
        y.add(0);
        y.add(3);

        Launch launch = new Launch();
        Parameters params = new Parameters();
        params.area1 = 2;

        launch.setNumPoints(4);
        launch.setXCoords(x);
        launch.setYCoords(y);
        launch.setParams(params);

        CMV cmv = new CMV(launch);
        assertTrue(cmv.cmv3());

        params.area1 = 3;
        assertFalse(cmv.cmv3());
    }

    @Test
    public void testCmv4() {
        // Contract: Verifies that there cmv4 returns true
        // if there are at least q_pts consecutive points
        // in quads different quadrants

        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(1);
        x.add(-1);
        ArrayList<Integer> y = new ArrayList<Integer>();
        y.add(1);
        y.add(-1);

        Launch launch = new Launch();
        Parameters params = new Parameters();
        params.q_pts = 2;
        params.quads = 2;

        launch.setNumPoints(2);
        launch.setXCoords(x);
        launch.setYCoords(y);
        launch.setParams(params);

        CMV cmv = new CMV(launch);
        assertTrue(cmv.cmv4());

        x.set(0, 0);
        x.set(1, 1);
        y.set(0, 1);
        y.set(1, 0);
        assertFalse(cmv.cmv4());
    }

    @Test
    public void testCmv5() {
        // Contract: cmv5 should return true whenever
        // there are two consecutive points such that
        // X[j] - X[i] < 0 && j == i+1
        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(2);
        x.add(1);
        ArrayList<Integer> y = new ArrayList<Integer>();
        y.add(0);
        y.add(0);

        Launch launch = new Launch();

        launch.setNumPoints(2);
        launch.setXCoords(x);
        launch.setYCoords(y);

        CMV cmv = new CMV(launch);
        assertTrue(cmv.cmv5());

        x.set(0, 1);
        x.set(1, 2);
        assertFalse(cmv.cmv5());
    }

    @Test
    public void testCMV6() {
        // Contract: Test LIC 6
        // This test case will have a distance of 3 between a line(2,2)-(2,5) and the point (5,5)
        Launch launchData = new Launch();
        Parameters params = new Parameters();
        launchData.setParams(params);
        params.n_pts = 3;
        launchData.setNumPoints(3);
        ArrayList<Integer> xCoords = new ArrayList<>();
        ArrayList<Integer> yCoords = new ArrayList<>();
        launchData.setXCoords(xCoords);
        launchData.setYCoords(yCoords);

        xCoords.add(2);
        yCoords.add(5);

        xCoords.add(5);
        yCoords.add(5);

        xCoords.add(2);
        yCoords.add(2);

        // Test that should be true
        params.dist = 2;
        CMV cmv = new CMV(launchData);
        assertTrue(cmv.cmv6());

        // Test that should be false
        params.dist = 4;
        cmv = new CMV(launchData);
        assertFalse(cmv.cmv6());


        /* Test case where there is no line present (the distance will also be 3 here) */
        xCoords.remove(xCoords.size() - 1);
        yCoords.remove(yCoords.size() - 1);
        xCoords.add(2);
        yCoords.add(5);

        // Test that should be true
        params.dist = 2;
        cmv = new CMV(launchData);
        assertTrue(cmv.cmv6());

        //Test that should be false
        params.dist = 4;
        cmv = new CMV(launchData);
        assertFalse(cmv.cmv6());

    }

    @Test
    public void testCmv7() {
        // Contract: verify the correctness of CMV condition number 7
        Launch launch = new Launch();
        launch.setParams(new Parameters());
        CMV cmv = new CMV(launch);

        // False if numPoints < 3
        launch.setNumPoints(2);
        assertFalse(cmv.cmv7());

        // False if K_PTS is less than 1, but numPoints >= 3
        launch.setNumPoints(4);
        launch.getParams().k_pts = 0;
        assertFalse(cmv.cmv7());

        // False if K_PTS > numPoints
        launch.setNumPoints(5);
        launch.getParams().k_pts = 6;
        assertFalse(cmv.cmv7());

        // Simple testcase for asserting true conditions
        // 5 points, k_pts=3, all are length1=10 apart
        launch.setNumPoints(5);
        launch.getParams().k_pts = 3;
        launch.getParams().length1 = 10;
        launch.setXCoords(new ArrayList<>());
        launch.getXCoords().add(0);
        launch.getXCoords().add(10);
        launch.getXCoords().add(20);
        launch.getXCoords().add(30);
        launch.getXCoords().add(40);
        launch.setYCoords(new ArrayList<>());
        launch.getYCoords().add(0);
        launch.getYCoords().add(10);
        launch.getYCoords().add(20);
        launch.getYCoords().add(30);
        launch.getYCoords().add(40);
        assertTrue(cmv.cmv7());

        // Asserting false conditions
        // No interval of points exactly k_pts large
        launch.setNumPoints(5);
        launch.getParams().k_pts = 4;
        launch.getParams().length1 = 10;
        launch.setXCoords(new ArrayList<>());
        launch.getXCoords().add(0);
        launch.getXCoords().add(10);
        launch.getXCoords().add(20);
        launch.getXCoords().add(30);
        launch.getXCoords().add(40);
        launch.setYCoords(new ArrayList<>());
        launch.getYCoords().add(0);
        launch.getYCoords().add(10);
        launch.getYCoords().add(20);
        launch.getYCoords().add(30);
        launch.getYCoords().add(40);
        assertFalse(cmv.cmv7());

        // Asserting false conditions
        // Correctly sized interval, but length is wrong
        launch.setNumPoints(5);
        launch.getParams().k_pts = 3;
        launch.getParams().length1 = 10;
        launch.setXCoords(new ArrayList<>());
        launch.getXCoords().add(0);
        launch.getXCoords().add(10);
        launch.getXCoords().add(11);
        launch.getXCoords().add(21);
        launch.getXCoords().add(31);
        launch.setYCoords(new ArrayList<>());
        launch.getYCoords().add(0);
        launch.getYCoords().add(10);
        launch.getYCoords().add(11);
        launch.getYCoords().add(21);
        launch.getYCoords().add(31);
        assertFalse(cmv.cmv7());

        // Asserting true condition
        // First a faulty interval, followed by a correct one
        launch.setNumPoints(11);
        launch.getParams().k_pts = 4;
        launch.getParams().length1 = 10;
        launch.setXCoords(new ArrayList<>());
        launch.setYCoords(new ArrayList<>());
        // Interval 1, less than k_pts consecutive points:
        launch.getXCoords().add(0);
        launch.getXCoords().add(10);
        launch.getXCoords().add(20);
        launch.getXCoords().add(30);
        launch.getXCoords().add(40);
        launch.getYCoords().add(0);
        launch.getYCoords().add(10);
        launch.getYCoords().add(20);
        launch.getYCoords().add(30);
        launch.getYCoords().add(40);
        // Interval 2, exactly k_pts consecutive points:
        launch.getXCoords().add(50);
        launch.getXCoords().add(60);
        launch.getXCoords().add(70);
        launch.getXCoords().add(80);
        launch.getXCoords().add(90);
        launch.getXCoords().add(100);
        launch.getYCoords().add(50);
        launch.getYCoords().add(60);
        launch.getYCoords().add(70);
        launch.getYCoords().add(80);
        launch.getYCoords().add(90);
        launch.getYCoords().add(100);
        assertTrue(cmv.cmv7());
    }

    @Test
    public void testCmv8() {
        //contract: verifies cmv condition 8 under 2.1 CMV in assignment description
        Launch launchData = new Launch();
        launchData.setNumPoints(6);
        Parameters params = new Parameters();
        params.a_pts = 1;
        params.b_pts = 1;

        ArrayList<Integer> xCoords = new ArrayList<Integer>();
        xCoords.add(0);
        xCoords.add(2);
        xCoords.add(2);
        xCoords.add(0);
        xCoords.add(5);
        xCoords.add(5);
        ArrayList<Integer> yCoords = new ArrayList<Integer>();
        yCoords.add(0);
        yCoords.add(2);
        yCoords.add(2);
        yCoords.add(0);
        yCoords.add(0);
        yCoords.add(0);
        launchData.setXCoords(xCoords);
        launchData.setYCoords(yCoords);

        //It is true that the condition is satisfied for radius1 and a_pts,b_pts=1
        params.radius1 = 1;
        launchData.setParams(params);
        assertTrue(new CMV(launchData).cmv8());

        //Now false since idx 0 3 and 5 lie at origo
        params.a_pts = 2;
        launchData.setParams(params);
        assertFalse(new CMV(launchData).cmv8());
    }

    @Test
    public void testCmv9() {
        // Contract: there are at least one set of consecutive points such that
        // they form a triangle with the vertex < PI - epsilon or vertex < PI + epsilon
        Launch launch = new Launch();
        launch.setParams(new Parameters());
        CMV cmv = new CMV(launch);

        // verify that c_pts < 1 and d_pts < 1 yields a false
        launch.getParams().c_pts = 0;
        assertFalse(cmv.cmv9());
        launch.setParams(new Parameters());
        launch.getParams().d_pts = -3;
        assertFalse(cmv.cmv9());

        // verify that numPoints < 5  yields a false
        launch.getParams().c_pts = 1;
        launch.getParams().d_pts = 1;
        launch.setNumPoints(4);
        assertFalse(cmv.cmv9());

        // verify that c_pts + d_pts < numPoints-3 yields a false
        launch.getParams().c_pts = 2;
        launch.getParams().d_pts = 4;
        launch.setNumPoints(8);
        assertFalse(cmv.cmv9());

        // verify that a correct series of points yields a true
        launch.getParams().epsilon = Math.PI / 2;
        launch.setNumPoints(10);
        launch.setXCoords(new ArrayList<>());
        launch.setYCoords(new ArrayList<>());
        launch.getParams().c_pts = 1;
        launch.getParams().d_pts = 2;
        // point 1
        launch.getXCoords().add(3);
        launch.getYCoords().add(0);
        // padding for c_pts
        launch.getXCoords().add(15);
        launch.getYCoords().add(15);
        // vertex
        launch.getXCoords().add(0);
        launch.getYCoords().add(0);
        // padding for d_pts
        launch.getXCoords().add(15);
        launch.getYCoords().add(15);
        // padding for d_pts
        launch.getXCoords().add(15);
        launch.getYCoords().add(15);
        // point 2
        launch.getXCoords().add(3);
        launch.getYCoords().add(2);
        // padding
        launch.getXCoords().add(100);
        launch.getYCoords().add(100);
        // padding
        launch.getXCoords().add(100);
        launch.getYCoords().add(100);
        // padding
        launch.getXCoords().add(100);
        launch.getYCoords().add(100);
        // padding
        launch.getXCoords().add(100);
        launch.getYCoords().add(100);
        assertTrue(cmv.cmv9());

    }

    @Test
    public void testCMV10() {
        // Contract: Test CMV 10
        // The area of the triangle between (0,0) (2,4) (4,0) has an area of 8.0 l.u.
        Launch launchData = new Launch();
        launchData.setNumPoints(5);
        Parameters params = new Parameters();
        launchData.setParams(params);
        params.e_pts = 1;
        params.f_pts = 1;
        ArrayList<Integer> xCoords = new ArrayList<>();
        ArrayList<Integer> yCoords = new ArrayList<>();
        launchData.setXCoords(xCoords);
        launchData.setYCoords(yCoords);
        xCoords.add(0);
        yCoords.add(0);

        xCoords.add(5);
        yCoords.add(5);

        xCoords.add(2);
        yCoords.add(4);

        xCoords.add(4);
        yCoords.add(3);

        xCoords.add(4);
        yCoords.add(0);

        // Test that should be true because 8 > 7
        params.area1 = 7.0;
        CMV cmv = new CMV(launchData);
        assertTrue(cmv.cmv10());

        // Test that should be false because 8 < 10
        params.area1 = 10.0;
        cmv = new CMV(launchData);
        assertFalse(cmv.cmv10());
    }

    @Test
    public void testCmv11() {
        // Contract: Testing requirements for cmv11 given in assignment 2.1
        //Test a positive and a negative case for the condition:
        //There exists at least one set of two data points, (X[i],Y[i]) and (X[j],Y[j]), separated by
        //exactly G PTS consecutive intervening points, such that X[j] - X[i] < 0. (where i < j )
        //Test input conditions NUMPOINTS < 3. AND 1 ≤ G PTS ≤ NUMPOINTS − 2

        Launch l = new Launch();
        Parameters params = new Parameters();
        params.g_pts = 1;
        l.setParams(params);
        l.setNumPoints(3);
        ArrayList<Integer> xc = new ArrayList<Integer>();
        ArrayList<Integer> yc = new ArrayList<Integer>();
        xc.add(10);
        xc.add(20);
        xc.add(30);
        yc.add(10);
        yc.add(20);
        yc.add(30);
        l.setXCoords(xc);
        l.setYCoords(yc);

        CMV testCmv = new CMV(l);
        boolean result = testCmv.cmv11();
        assertEquals(false, result);

        l.setNumPoints(1);
        result = testCmv.cmv11();
        assertEquals(false, result);

        l.setNumPoints(3);
        xc = new ArrayList<Integer>();
        yc = new ArrayList<Integer>();
        xc.add(30);
        xc.add(20);
        xc.add(10);
        yc.add(30);
        yc.add(20);
        yc.add(10);
        l.setXCoords(xc);
        l.setYCoords(yc);
        result = testCmv.cmv11();
        assertTrue(result);

    }

    @Test
    public void testCmv12() {
        // Contract: Testing requirement 12 in assignment 2.1
        //test positive and negative case of data points for both conditions in the requirement,
        //Set of 2 data points with K_pts consecutive interveening points that are > length1 apart
        //Set of 2 data points with K_pts consecutive interveening points that are < length2 apart
        //test input conditions given in assignment numpoints>=3 and length2>=0.

        Launch l = new Launch();
        Parameters params = new Parameters();
        params.k_pts = 1;
        params.length1 = 5.0;
        params.length2 = 30.0;
        l.setParams(params);
        l.setNumPoints(3);

        //test case = dist 14.142 between consecutive pts and 28.28 between last and first,
        //should pass both cond1 and cond2
        ArrayList<Integer> xc = new ArrayList<Integer>();
        ArrayList<Integer> yc = new ArrayList<Integer>();
        xc.add(0);
        xc.add(10);
        xc.add(20);
        yc.add(0);
        yc.add(10);
        yc.add(20);
        l.setXCoords(xc);
        l.setYCoords(yc);

        CMV testCmv = new CMV(l);
        boolean result = testCmv.cmv12();
        assertTrue(result);

        //should fail cond1 thus actualDist < length1
        l.getParams().length1 = 15.0;
        result = testCmv.cmv12();
        assertFalse(result);

        //should fail cond2 thus actualDist > length2
        l.getParams().length1 = 5.0;
        l.getParams().length2 = 10.0;
        result = testCmv.cmv12();
        assertFalse(result);

        //test numpoints < 3 -> false
        l.setNumPoints(1);
        result = testCmv.cmv12();
        assertFalse(result);

        //test length2 < 0 -> false
        l.setNumPoints(3);
        params.length2 = -1;
        result = testCmv.cmv12();
        assertFalse(result);
    }

    @Test
    public void testCmv13() {
        //contract: verifies cmv condition 13 under 2.1 CMV in assignment description
        Launch launchData = new Launch();
        launchData.setNumPoints(6);
        Parameters params = new Parameters();
        params.a_pts = 1;
        params.b_pts = 1;
        ArrayList<Integer> xCoords = new ArrayList<Integer>();
        xCoords.add(0);
        xCoords.add(2);
        xCoords.add(2);
        xCoords.add(0);
        xCoords.add(5);
        xCoords.add(5);
        ArrayList<Integer> yCoords = new ArrayList<Integer>();
        yCoords.add(0);
        yCoords.add(2);
        yCoords.add(2);
        yCoords.add(0);
        yCoords.add(0);
        yCoords.add(0);
        launchData.setXCoords(xCoords);
        launchData.setYCoords(yCoords);

        //Now there are NO 3 consecutive that are inside radius2 circle
        params.radius1 = 1;
        params.radius2 = 0.1;
        launchData.setParams(params);
        assertFalse(new CMV(launchData).cmv13());

          //Now there are 3 consecutive that are inside radius2 circle
        params.radius2 = 10;
        launchData.setParams(params);
        assertTrue(new CMV(launchData).cmv13());
    }

    @Test
    public void testCMV14() {
        // Contract: Test CMV 11
        // The area of the triangle between (0,0) (2,4) (4,0) has an area of 8.0 l.u.
        Launch launchData = new Launch();
        launchData.setNumPoints(5);
        Parameters params = new Parameters();
        launchData.setParams(params);
        params.e_pts = 1;
        params.f_pts = 1;
        ArrayList<Integer> xCoords = new ArrayList<>();
        ArrayList<Integer> yCoords = new ArrayList<>();
        launchData.setXCoords(xCoords);
        launchData.setYCoords(yCoords);
        xCoords.add(0);
        yCoords.add(0);

        xCoords.add(5);
        yCoords.add(5);

        xCoords.add(2);
        yCoords.add(4);

        xCoords.add(4);
        yCoords.add(3);

        xCoords.add(4);
        yCoords.add(0);

        // Test that should be true because 8 > 4(area1) and 8 > 7(area2)
        params.area1 = 4.0;
        params.area2 = 7.0;
        CMV cmv = new CMV(launchData);
        assertTrue(cmv.cmv14());

        // Test that should be false because 8 < 10(area1) and 8 > 7(area2)
        params.area1 = 10.0;
        params.area2 = 7.0;
        cmv = new CMV(launchData);
        assertFalse(cmv.cmv14());

        // Test that should be false because 8 > 4(area1) and 8 < 10(area2)
        params.area1 = 4.0;
        params.area2 = 10.0;
        cmv = new CMV(launchData);
        assertFalse(cmv.cmv14());

        // Test that should be false because 8 < 10(area1) and 8 < 10(area2)
        params.area1 = 10.0;
        params.area2 = 10.0;
        cmv = new CMV(launchData);
        assertFalse(cmv.cmv14());
    }

    @Test
    public void testFuv() {
        // Contract: Verifies that FUV[i] == true iff
        // PUV[i] == false or PUM[i][0 .. n] == true
        Launch launch = new Launch();
        ArrayList<Boolean> puv = new ArrayList<Boolean>();
        puv.add(false);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        puv.add(true);
        launch.setPuv(puv);

        ArrayList<ArrayList<Boolean>> pumm = new ArrayList<ArrayList<Boolean>>();
        ArrayList<Boolean> pumv1 = new ArrayList<Boolean>();
        ArrayList<Boolean> pumv2 = new ArrayList<Boolean>();
        PUM pum = new PUM();
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);
        pumv1.add(true);

        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(false);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);
        pumv2.add(true);

        pumm.add(pumv2);
        pumm.add(pumv1);
        pumm.add(pumv2);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);
        pumm.add(pumv1);

        pum.setPum(pumm);
        launch.setPum(pum);
        FUV fuv = new FUV(launch);
        assertTrue(fuv.getFuv().get(0));
        assertTrue(fuv.getFuv().get(1));
        assertFalse(fuv.getFuv().get(2));
    }

    @Test
    public void testPUM() {
        try {
            Launch launchData = new Launch();
            CMV cmv = new CMV(launchData);
            launchData.setCmv(cmv);
            ArrayList<Boolean> cmv_vector = new ArrayList<>();
            cmv.setCmv(cmv_vector);
            PUM pum_object;
            ArrayList<ArrayList<Boolean>> actualPum;

            /* Create expected PUM matrix with all cells as true */
            ArrayList<ArrayList<Boolean>> expectedPum = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                ArrayList<Boolean> expectedRow = new ArrayList<>();
                for (int j = 0; j < 15; j++) {
                    expectedRow.add(true);
                }
                expectedPum.add(expectedRow);
            }

            // Test 1 - All true CMV
            for (int i = 0; i < 15; i++) {
                cmv_vector.add(true);
            }
            pum_object = new PUM(launchData);
            actualPum = pum_object.getPum();
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    assertEquals(expectedPum.get(i).get(j), actualPum.get(i).get(j));
                }
            }

            // Test 2 - CMV[1] == false
            cmv_vector.set(1, false);
            pum_object = new PUM(launchData);
            actualPum = pum_object.getPum();
            expectedPum.get(0).set(1, false);
            expectedPum.get(1).set(1, false);
            expectedPum.get(1).set(0, false);
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    assertEquals(expectedPum.get(i).get(j), actualPum.get(i).get(j));
                }
            }


        } catch (IllegalArgumentException e) {
            assertTrue(false);
        }

    }

    @Test
    public void testDecide() {
        // Contract: Test if the Decide-function can give true and false values based on different inputs
        String fileName;
        Launch launchData;

        // Input data that should give a false response
        fileName = System.getProperty("user.dir") + "//input//fail.data";
        launchData = new Launch(fileName);
        assertEquals(false, launchData.decide());

        // Input data that should give a true response
        fileName = System.getProperty("user.dir") + "//input//pass.data";
        launchData = new Launch(fileName);
        assertEquals(true, launchData.decide());


    }

    /***************************************************
     *                      Algebra
     ***************************************************/

    @Test
    public void testDistanceBetweenPointAndLine() {
        // Contract: test if distance is correct between a point and a line
        double dist = Algebra.distanceBetweenPointAndLine(5, 5, 2, 4, 2, 2);
        assertEquals(3.0, dist, 0.000001);
    }

    @Test
    public void testAngleOfThreePoints() {
        // Contract: test if the method for calculating the angle from three points works as it is supposed to
        double epsilon = 0.0000001;
        // PI/2 rad
        double angle1 = Algebra.angleOfThreePoints(0, 0, 1, 0, 0, 1);
        double oracle1 = Math.PI/2;
        assertEquals(angle1, oracle1, epsilon);

        double angle2 = Algebra.angleOfThreePoints(0, 0, 80, 60, 80, 0);
        double oracle2 = 0.64350110791301;
        assertEquals(angle2, oracle2, epsilon);
    }

    @Test
    public void testTriangleArea() {
        // Contract: Check if the calculation of area triangle is correct
        // The area of the triangle between (0,0) (2,4) (4,0) has an area of 8.0 l.u.
        double area = Algebra.triangleArea(0, 0, 2, 4, 4, 0);
        assertEquals(8.0, area);
    }

    @Test
    public void testGetDistance() {
        // Contract: check if calculation of distance between two points yields the correct answer
        double epsilon = 0.00001;
        // verify that distance between 0;0 and 100;0 is 100
        double distance1 = Algebra.getDistance(0,0,100,0);
        double oracle1 = 100.0;
        assertEquals(oracle1, distance1, epsilon);
        // verify that distance between 0;0 and 100;100 is 141.42136
        double distance2 = Algebra.getDistance(0,0,100,100);
        double oracle2 = 141.42136;
        assertEquals(oracle2, distance2, epsilon);
        // verify that distance between -1;51 and -71;-16 is 96.89685
        double distance3 = Algebra.getDistance(-1,51,-71,-16);
        double oracle3 = 96.89685;
        assertEquals(oracle3, distance3, epsilon);
    }

    @Test
    public void testDistBetweenIdxPoints() {
        // Contract: check if calculation of distance between two points yields the correct answer
        double epsilon = 0.00001;
        Launch launchData = new Launch();
        launchData.setNumPoints(2);

        // verify that distance between 0;0 and 100;0 is 100
        launchData.setXCoords(new ArrayList<>());
        launchData.getXCoords().add(0);
        launchData.getXCoords().add(100);
        launchData.setYCoords(new ArrayList<>());
        launchData.getYCoords().add(0);
        launchData.getYCoords().add(0);
        double distance1 = Algebra.distBetweenIdxPoints(launchData, 0, 1);
        double oracle1 = 100.0;
        assertEquals(oracle1, distance1, epsilon);

        // verify that distance between 0;0 and 100;100 is 141.42136
        launchData.setXCoords(new ArrayList<>());
        launchData.getXCoords().add(0);
        launchData.getXCoords().add(100);
        launchData.setYCoords(new ArrayList<>());
        launchData.getYCoords().add(0);
        launchData.getYCoords().add(100);
        double distance2 = Algebra.distBetweenIdxPoints(launchData, 0, 1);
        double oracle2 = 141.42136;
        assertEquals(oracle2, distance2, epsilon);

        // verify that distance between -1;51 and -71;-16 is 96.89685
        launchData.setXCoords(new ArrayList<>());
        launchData.getXCoords().add(-1);
        launchData.getXCoords().add(-71);
        launchData.setYCoords(new ArrayList<>());
        launchData.getYCoords().add(51);
        launchData.getYCoords().add(-16);
        double distance3 = Algebra.distBetweenIdxPoints(launchData, 0, 1);
        double oracle3 = 96.89685;
        assertEquals(oracle3, distance3, epsilon);
    }
}
