package com.decide.app;

import java.util.ArrayList;

/*
Conditional Matrix Vector class containing the CMV and the various
condition functions that decides the boolean values of each element
in the CMV
*/
public class CMV {
    private ArrayList<Boolean> cmv;
    private Launch launchData;

    //initial constructor
    public CMV(Launch launchData) {
        cmv = new ArrayList<Boolean>();
        this.launchData = launchData;
        //call calc func
    }
  
    public void calculateCmvVector() {
        initializeCMV();
    }

    //initializes cmv vector by calling each index function respectively.
    private void initializeCMV() {
        cmv.add(this.cmv0());
        cmv.add(this.cmv1());
        cmv.add(this.cmv2());
        cmv.add(this.cmv3());
        cmv.add(this.cmv4());
        cmv.add(this.cmv5());
        cmv.add(this.cmv6());
        cmv.add(this.cmv7());
        cmv.add(this.cmv8());
        cmv.add(this.cmv9());
        cmv.add(this.cmv10());
        cmv.add(this.cmv11());
        cmv.add(this.cmv12());
        cmv.add(this.cmv13());
        cmv.add(this.cmv14());
    }

    public ArrayList<Boolean> getCmv() {
        return cmv;
    }

    // ONLY to be used for testing purposes
    public void setCmv(ArrayList<Boolean> cmv) {
        this.cmv = cmv;
    }

    /*************************************************************************
     *          condition functions for calculating CMV elements
     **************************************************************************/

    //condition 0. returns true iff condition 0 under CMV 2.1 is satisfied
    public boolean cmv0() {
        for (int i = 0; i < (launchData.getNumPoints() - 1); i++) {
            if (Algebra.distBetweenIdxPoints(launchData, i, (i + 1)) > launchData.getParams().length1) {
                return true;
            }
        }
        return false;
    }

    /*
    There exists at least one set of three consecutive data points that cannot all
    be contained within or on a circle of radius RADIUS1.
    (0 ≤ RADIUS1)
    */
    public boolean cmv1() {
        //from http://keisan.casio.com/exec/system/1223429573
        double lenA = 0;
        double lenB = 0;
        double lenC = 0; //lengths between points
        double s = 1; // see link above
        double triangleCircumcircleRadius = 1;

        for (int i = 0; i < launchData.getNumPoints() - 2; i++) {
            lenA = Algebra.distBetweenIdxPoints(launchData, i, i + 1);
            lenB = Algebra.distBetweenIdxPoints(launchData, i + 1, i + 2);
            lenC = Algebra.distBetweenIdxPoints(launchData, i + 2, i);
            s = (lenA + lenB + lenC) / 2;
            double rootDiv = 4 * Math.sqrt(s * (s - lenA) * (s - lenB) * (s - lenC));
            triangleCircumcircleRadius = (lenA * lenB * lenC) / rootDiv;
            if (triangleCircumcircleRadius > launchData.getParams().radius1) {
                return true;
            }
        }
        return false;
    }

    /*
    There exists at least one set of three consecutive data points which form an angle such that:
    angle < (PI−EPSILON) or	angle > (PI+EPSILON)
    The second of the three consecutive points is always the vertex of the angle. If either the first
    point or the last point (or both) coincides with the vertex, the angle is undefined and the LIC
    is not satisfied by those three points. (0 ≤ EPSILON < PI)
    */
    public boolean cmv2() {
        // (0 ≤ EPSILON < PI)
        if (!(0 <= launchData.getParams().epsilon && launchData.getParams().epsilon < Math.PI)) {
            return false;
        }
        for (int i = 0; i < launchData.getNumPoints() - 2; i++) {
            int p1 = i;
            int p2 = i + 2;
            int vertex = i + 1;
            // p1 must not coincide with the vertex
            if (launchData.getXCoords().get(p1) == launchData.getXCoords().get(vertex) &&
                    launchData.getYCoords().get(p1) == launchData.getYCoords().get(vertex)) {
                continue;
            }
            // p2 must not coincide with the vertex
            if (launchData.getXCoords().get(p2) == launchData.getXCoords().get(vertex) &&
                    launchData.getYCoords().get(p2) == launchData.getYCoords().get(vertex)) {
                continue;
            }
            // Check the angle
            double angle = Algebra.angleOfThreePoints(
                    launchData.getXCoords().get(vertex),
                    launchData.getYCoords().get(vertex),
                    launchData.getXCoords().get(p1),
                    launchData.getYCoords().get(p1),
                    launchData.getXCoords().get(p2),
                    launchData.getYCoords().get(p2));

            if (angle < Math.PI - launchData.getParams().epsilon || angle > Math.PI + launchData.getParams().epsilon) {
                return true;
            }
        }
        return false;
    }

    /*
    There exists at least one set of three
    consecutive data points that are the vertices
    of a triangle with an area greater than AREA1.
    (0 <= AREA1)
    */
    public boolean cmv3() {
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        double area = launchData.getParams().area1;
        for (int i = 0; i < launchData.getNumPoints() - 2; i++) {
            int ax = xCoords.get(i);
            int ay = yCoords.get(i);
            int bx = xCoords.get(i + 1);
            int by = yCoords.get(i + 1);
            int cx = xCoords.get(i + 2);
            int cy = yCoords.get(i + 2);
            if (area < Math.abs((ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) / 2))
                return true;
        }
        return false;
    }

    /*
      There exists at least one set of Q PTS consecutive
      data points that lie in more than QUADS quadrants.
      Where there is ambiguity as to which quadrant contains
      a given point, priority of decision will be by
      quadrant number, i.e., I, II, III, IV. For example,
      the data point (0,0) is in quadrant I, the point (-l,0)
      is in quadrant II, the point (0,-l) is in quadrant III,
      the point (0,1) is in quadrant I and the point (1,0)
      is in quadrant I.
      (2 ≤ Q PTS ≤ NUMPOINTS), (1 ≤ QUADS ≤ 3)
      */
    public boolean cmv4() {
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        int q_pts = launchData.getParams().q_pts;
        int nquads = launchData.getParams().quads;
        for (int i = 0; i <= launchData.getNumPoints() - q_pts; i++) {
            boolean[] quads = {false, false, false, false};
            for (int j = 0; j < q_pts; j++) {
                /* Quadrant I */
                if (xCoords.get(i + j) >= 0 && yCoords.get(i + j) >= 0)
                    quads[0] = true;
                    /* Quadrant II */
                else if (xCoords.get(i + j) >= 0 && yCoords.get(i + j) <= 0)
                    quads[1] = true;
                    /* Quadrant III */
                else if (xCoords.get(i + j) <= 0 && yCoords.get(i + j) <= 0)
                    quads[2] = true;
                    /* Quadrant IV */
                else if (xCoords.get(i + j) <= 0 && yCoords.get(i + j) >= 0)
                    quads[3] = true;
            }

            int count = 0;
            for (boolean quad : quads)
                if (quad) count++;
            if (count >= nquads) return true;
        }
        return false;
    }

    /*
    There exists at least one set of two consecutive
    data points, (X[i],Y[i]) and (X[j],Y[j]), such
    that X[j] - X[i] < 0. (where i = j-1)
    */
    public boolean cmv5() {
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        for (int i = 0; i < launchData.getNumPoints() - 1; i++)
            if (xCoords.get(i + 1) - xCoords.get(i) < 0) return true;
        return false;
    }

    public boolean cmv6() {
        int N_PTS = launchData.getParams().n_pts;
        int NUMPOINTS = launchData.getNumPoints();
        double DIST = launchData.getParams().dist;
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        if (NUMPOINTS < 3 || N_PTS < 3 || N_PTS > NUMPOINTS) {
            return false;
        }
        /* Check all points within all possible intervals of N_PTS (first -> last)*/
        int first = 0, last = N_PTS - 1;    // initialization
        //Check every N_PTS interval
        while (last < NUMPOINTS) {
            // Check distance from all points in the N_PTS interval to the line between the first and last point of the N_PTS interval
            for (int i = first + 1; i < last; i++) {
                double distance;
                // No line (first and last point are same coordinate)
                if (xCoords.get(first).equals(xCoords.get(last)) && yCoords.get(first).equals(yCoords.get(last))) {
                    distance = Algebra.distBetweenIdxPoints(launchData, first, i);
                } else {
                    distance = Algebra.distanceBetweenPointAndLine(xCoords.get(i), yCoords.get(i),
                            xCoords.get(first), yCoords.get(first), xCoords.get(last), yCoords.get(last));
                }
                if (distance > DIST) {
                    return true;
                }
            }
            first++;
            last++;
        }
        /* If we come here none of the points had a distance greater than DIST to the N_PTS interval line and we return false*/
        return false;
    }

    /*
    There exists at least one set of two data points separated by exactly
    K PTS consecutive intervening points that are a distance greater than
    the length, LENGTH1, apart. The condition is not met when NUMPOINTS < 3.
    1 ≤ K PTS ≤ (NUMPOINTS−2)
    */
    public boolean cmv7() {
        // The condition is not met when NUMPOINTS < 3
        if (launchData.getNumPoints() < 3) {
            return false;
        }
        // 1 ≤ K PTS ≤ (NUMPOINTS−2)
        if (!(1 <= launchData.getParams().k_pts && launchData.getParams().k_pts <= launchData.getNumPoints() - 2)) {
            return false;
        }
        // Find at least one pair of points that the requirements above
        for (int p1 = 0; p1 < launchData.getNumPoints() - 1; p1++) {
            for (int p2 = p1 + launchData.getParams().k_pts; p2 < launchData.getNumPoints(); p2++) {
                boolean distanceOkay = true;
                for (int i = p1; i < p2 - 1; i++) {
                    if (!(Algebra.distBetweenIdxPoints(launchData, i, i + 1) > launchData.getParams().length1)) {
                        distanceOkay = false;
                        break;
                    }
                }
                if (distanceOkay) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    There exists at least one set of three data points separated by exactly A PTS and B PTS
    consecutive intervening points, respectively, that cannot be contained within or on a circle of
    radius RADIUS1. The condition is not met when NUMPOINTS < 5.
    1 ≤ A PTS, 1 ≤ B PTS
    A PTS+B PTS ≤ (NUMPOINTS−3)
    */
    public boolean cmv8() {
        //from http://keisan.casio.com/exec/system/1223429573 formula for radius
        int numPoints = launchData.getNumPoints();
        int a_pts = launchData.getParams().a_pts;
        int b_pts = launchData.getParams().b_pts;
        double radius1 = launchData.getParams().radius1;
        double lenA, lenB, lenC;
        double s = 1;
        if (numPoints < 5 || a_pts < 1 || b_pts < 1 || (a_pts + b_pts) > (numPoints - 3)) {
            return false;
        }
        for (int i = 0; i < (numPoints - (2 + a_pts + b_pts)); i++) {
            lenA = Algebra.distBetweenIdxPoints(launchData, i, i + a_pts + 1);
            lenB = Algebra.distBetweenIdxPoints(launchData, i + a_pts + 1, i + a_pts + b_pts + 2);
            lenC = Algebra.distBetweenIdxPoints(launchData, i + a_pts + b_pts + 2, i);
            s = (lenA + lenB + lenC) / 2;
            double rootDiv = 4 * Math.sqrt(s * (s - lenA) * (s - lenB) * (s - lenC));
            double triangleCircumcircleRadius = (lenA * lenB * lenC) / rootDiv;
            if (triangleCircumcircleRadius > radius1) {
                return true;
            }
        }
        return false;
    }

    /*
    There exists at least one set of three data points separated by exactly C PTS and D PTS
    consecutive intervening points, respectively, that form an angle such that:
    angle < (PI−EPSILON) or	angle > (PI+EPSILON)
    The second point of the set of three points is always the vertex of the angle. If either the first
    point or the last point (or both) coincide with the vertex, the angle is undefined and the LIC
    is not satisfied by those three points. When NUMPOINTS < 5, the condition is not met.
    1 ≤ C PTS, 1 ≤ D PTS
    C PTS+D PTS ≤ NUMPOINTS−3
    */
    public boolean cmv9() {
        // NUMPOINTS < 5
        if (launchData.getNumPoints() < 5) {
            return false;
        }
        // 1 ≤ C PTS, 1 ≤ D PTS
        if (launchData.getParams().c_pts < 1 || launchData.getParams().d_pts < 1) {
            return false;
        }
        // C PTS+D PTS ≤ NUMPOINTS−3
        if (launchData.getParams().c_pts + launchData.getParams().d_pts > launchData.getNumPoints() - 3) {
            return false;
        }
        int C_D_PTS = launchData.getParams().c_pts + launchData.getParams().d_pts;
        for (int i = 0; i <= launchData.getNumPoints() - C_D_PTS; i++) {
            int p1 = i;
            int p2 = i + launchData.getParams().d_pts;
            int vertex = i + launchData.getParams().c_pts;
            // p1 must not coincide with the vertex
            if (launchData.getXCoords().get(p1) == launchData.getXCoords().get(vertex) &&
                    launchData.getYCoords().get(p1) == launchData.getYCoords().get(vertex)) {
                continue;
            }
            // p2 must not coincide with the vertex
            if (launchData.getXCoords().get(p2) == launchData.getXCoords().get(vertex) &&
                    launchData.getYCoords().get(p2) == launchData.getYCoords().get(vertex)) {
                continue;
            }
            // Check the angle
            double angle = Algebra.angleOfThreePoints(
                    launchData.getXCoords().get(vertex),
                    launchData.getYCoords().get(vertex),
                    launchData.getXCoords().get(p1),
                    launchData.getYCoords().get(p1),
                    launchData.getXCoords().get(p2),
                    launchData.getYCoords().get(p2));

            if (angle < Math.PI - launchData.getParams().epsilon || angle > Math.PI + launchData.getParams().epsilon) {
                return true;
            }
        }
        return false;
    }

    /*
    There exists at least one set of three data points separated by exactly E PTS and F PTS con-
    secutive intervening points, respectively, that are the vertices of a triangle with area greater
    than AREA1. The condition is not met when NUMPOINTS < 5.
    1 ≤ E PTS, 1 ≤ F PTS
    E PTS+F PTS ≤ NUMPOINTS−3
     */
    public boolean cmv10() {
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        int NUMPOINTS = launchData.getNumPoints();
        int E_PTS = launchData.getParams().e_pts;
        int F_PTS = launchData.getParams().f_pts;
        double AREA1 = launchData.getParams().area1;

        /* Basic pre-conditions */
        if (NUMPOINTS < 5 || E_PTS < 1 || F_PTS < 1 || (E_PTS + F_PTS) > NUMPOINTS - 3) {
            return false;
        }

        /* Initialize the points with E_PTS and F_PTS separation */
        int p1 = 0, p2 = E_PTS + 1, p3 = E_PTS + F_PTS + 2;

        /* Try all possible triangle combinations */
        while (p3 < NUMPOINTS) {
            double triArea = Algebra.triangleArea(xCoords.get(p1), yCoords.get(p1), xCoords.get(p2),
                    yCoords.get(p2), xCoords.get(p3), yCoords.get(p3));
            if (triArea > AREA1) {
                return true;
            }

            p1++;
            p2++;
            p3++;
        }

        /* If no triangle with large enough area was found we return false */
        return false;
    }

    /*
    There exists at least one set of two data points, (X[i],Y[i]) and (X[j],Y[j]), separated by
    exactly G PTS consecutive intervening points, such that X[j] - X[i] < 0. (where i < j ) The
    condition is not met when NUMPOINTS < 3.
    1 ≤ G PTS ≤ NUMPOINTS − 2
    */
    public boolean cmv11() {
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        int numPoints = launchData.getNumPoints();
        int g_pts = launchData.getParams().g_pts;
        if (numPoints < 3) {
            return false;
        }

        //1 ≤ G PTS ≤ NUMPOINTS − 2
        if (g_pts < 1 || g_pts > (numPoints - 2)) {
            return false;
        }

        //(X[i],Y[i]) = (x1,y1), (X[j],Y[j]) = (x2,y2)
        for (int i = 0; i + g_pts + 1 < numPoints; i++) {
            int x1 = xCoords.get(i);
            int y1 = yCoords.get(i);
            int x2 = xCoords.get(i + g_pts + 1);
            int y2 = yCoords.get(i + g_pts + 1);
            if ((x2 - x1) < 0) {
                return true;
            }
        }
        return false;
    }

    /*
    There exists at least one set of two data points, separated by exactly K PTS consecutive
    intervening points, which are a distance greater than the length, LENGTH1, apart. In addi-
    tion, there exists at least one set of two data points (which can be the same or different from
    the two data points just mentioned), separated by exactly K PTS consecutive intervening
    points, that are a distance less than the length, LENGTH2, apart. Both parts must be true
    for the LIC to be true. The condition is not met when NUMPOINTS < 3.
    0 ≤ LENGTH2
    */
    public boolean cmv12() {
        int numPoints = launchData.getNumPoints();
        int k_pts = launchData.getParams().k_pts;
        double length1 = launchData.getParams().length1;
        double length2 = launchData.getParams().length2;

        //check condition NUMPOINTS < 3
        if (numPoints < 3) {
            return false;
        }

        //check 0 ≤ LENGTH2
        if (length2 < 0) {
            return false;
        }

        //check first conidtion; two data points, exactly K_pts between, all dist > length1 apart.
        boolean cond1 = false;
        //interval iterator
        for (int i = 0; i + k_pts + 1 < numPoints; i++) {
            //check that [i, i+kpts+1] all have dist > length1
            int p1 = i;
            int p2 = i + k_pts + 1;
            //loop over interval
            boolean validDist = true;
            for (int j = p1; j < p2; j++) {
                //compare dist between current j element to all other in interval j+1 to p2
                for (int k = j + 1; k <= p2; k++) {
                    if (Algebra.distBetweenIdxPoints(launchData, j, k) < length1) {
                        validDist = false;
                        break;
                    }
                }
                //no valid dist in p1 to p2 check new interval
                if (!validDist) {
                    break;
                }
            }
            //found valid interval no need to check more
            if (validDist) {
                cond1 = true;
                break;
            }
        }
        //if cond1 failed can return false without checking cond2
        if (!cond1) {
            return false;
        }

        //check second condition two data points, exactly K_pts between, all dist < length2 apart.
        boolean cond2 = false;
        for (int i = 0; i + k_pts + 1 < numPoints; i++) {
            //check that [i, i+kpts+1] all have dist > length1
            int p1 = i;
            int p2 = i + k_pts + 1;
            //loop over interval
            boolean validDist = true;
            for (int j = p1; j < p2; j++) {
                //compare dist between current j element to all other in interval j+1 to p2
                for (int k = j + 1; k <= p2; k++) {
                    if (Algebra.distBetweenIdxPoints(launchData, j, k) > length2) {
                        validDist = false;
                        break;
                    }
                }
                //no valid dist in p1 to p2 check new interval
                if (!validDist) {
                    break;
                }
            }
            //found valid interval no need to check more
            if (validDist) {
                cond2 = true;
                break;
            }
        }
        //if both conditions met return true
        //could ommit cond1 as its true by default if this clause is reached.
        if (cond1 & cond2) {
            return true;
        }

        return false;
    }

    /*
    There exists at least one set of three data points, separated by exactly A PTS and B PTS
    consecutive intervening points, respectively, that cannot be contained within or on a circle of
    radius RADIUS1. In addition, there exists at least one set of three data points (which can be
    the same or different from the three data points just mentioned) separated by exactly A PTS
    and B PTS consecutive intervening points, respectively, that can be contained in or on a
    circle of radius RADIUS2. Both parts must be true for the LIC to be true. The condition is
    not met when NUMPOINTS < 5.
    0 ≤ RADIUS2
    */
    public boolean cmv13() {
        //from http://keisan.casio.com/exec/system/1223429573 formula for radius
        int numPoints = launchData.getNumPoints();
        int a_pts = launchData.getParams().a_pts;
        int b_pts = launchData.getParams().b_pts;
        double radius1 = launchData.getParams().radius1;
        double radius2 = launchData.getParams().radius2;
        double lenA, lenB, lenC;
        double s = 1;
        boolean radCon1 = false;
        boolean radCon2 = false;
        if (numPoints < 5 || a_pts < 1 || b_pts < 1 || (a_pts + b_pts) > (numPoints - 3) || radius2 < 0) {
            return false;
        }
        for (int i = 0; i < (numPoints - (2 + a_pts + b_pts)); i++) {
            lenA = Algebra.distBetweenIdxPoints(launchData, i, i + a_pts + 1);
            lenB = Algebra.distBetweenIdxPoints(launchData, i + a_pts + 1, i + a_pts + b_pts + 2);
            lenC = Algebra.distBetweenIdxPoints(launchData, i + a_pts + b_pts + 2, i);
            s = (lenA + lenB + lenC) / 2;
            double rootDiv = 4 * Math.sqrt(s * (s - lenA) * (s - lenB) * (s - lenC));
            double triangleCircumcircleRadius = (lenA * lenB * lenC) / rootDiv;
            if (triangleCircumcircleRadius > radius1) {
                radCon1 = true;
            }

            if (triangleCircumcircleRadius <= radius2) {
                radCon2 = true;
            }
        }
        return ((radCon1 && radCon2));
    }

    public boolean cmv14() {
        ArrayList<Integer> xCoords = launchData.getXCoords();
        ArrayList<Integer> yCoords = launchData.getYCoords();
        int NUMPOINTS = launchData.getNumPoints();
        int E_PTS = launchData.getParams().e_pts;
        int F_PTS = launchData.getParams().f_pts;
        double AREA1 = launchData.getParams().area1;
        double AREA2 = launchData.getParams().area2;
        Boolean triArea1Found = false, triArea2Found = false;

        /* Basic pre-conditions */
        if (NUMPOINTS < 5 || AREA2 < 0) {
            return false;
        }

        /* Initialize the points with E_PTS and F_PTS separation */
        int p1 = 0, p2 = E_PTS + 1, p3 = E_PTS + F_PTS + 2;

        /* Try all possible triangle combinations */
        while (p3 < NUMPOINTS) {
            double triArea = Algebra.triangleArea(xCoords.get(p1), yCoords.get(p1), xCoords.get(p2),
                    yCoords.get(p2), xCoords.get(p3), yCoords.get(p3));
            if (triArea > AREA1) {
                triArea1Found = true;
            }
            if (triArea > AREA2) {
                triArea2Found = true;
            }

            /* If we found two triangle sets that are larger than AREA1 and AREA2 */
            if (triArea1Found && triArea2Found) {
                return true;
            }

            p1++;
            p2++;
            p3++;
        }

        /* If no triangle with large enough area was found we return false */
        return false;
    }

}
