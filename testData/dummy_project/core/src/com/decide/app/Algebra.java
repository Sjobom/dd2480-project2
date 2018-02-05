package com.decide.app;

public class Algebra {

    public static double distanceBetweenPointAndLine(int px, int py, int x1, int y1, int x2, int y2) {
        /*  Point = (px, py).
            Line is between (x1, y1) and (x2, y2).
            Formula to calculate distance from a point to a line defined by two points from Wikipedia:
		    https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
        */
        return (Math.abs((y2 - y1) * px - (x2 - x1) * py + x2 * y1 - y2 * x1)) /
                (Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2)));
    }
  
    public static double triangleArea (int x1, int y1, int x2, int y2, int x3, int y3){
        /* Formula: https://www.mathopenref.com/coordtrianglearea.html */
        return Math.abs((x1*(y2 - y3) + x2*(y3 - y1) + x3*(y1 - y2))/2);
    }

    public static double angleOfThreePoints(int vertex_x, int vertex_y, int p1_x, int p1_y, int p2_x, int p2_y) {
      double dist_v_1 = Math.sqrt(Math.pow(vertex_x - p1_x, 2) + Math.pow(vertex_y - p1_y, 2));
      double dist_v_2 = Math.sqrt(Math.pow(vertex_x - p2_x, 2) + Math.pow(vertex_y - p2_y, 2));
      double dist_1_2 = Math.sqrt(Math.pow(p1_x - p2_x, 2) + Math.pow(p1_y - p2_y, 2));
      return Math.acos((Math.pow(dist_v_1, 2) + Math.pow(dist_v_2, 2) - Math.pow(dist_1_2, 2)) /
              (2 * dist_v_1 * dist_v_2));
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        //returns distance beetween 2 points given their coordinates
        double xDiffPow = Math.pow((x2 - x1), 2);
        double yDiffPow = Math.pow((y2 - y1), 2);
        return Math.sqrt(xDiffPow + yDiffPow);
    }

    public static double distBetweenIdxPoints(Launch launchData, int idx1, int idx2) {
        //returns the distance between 2 radarpoints given as indexes
        if (idx1 < 0 || idx2 < 0 || idx1 >= launchData.getNumPoints() || idx2 >= launchData.getNumPoints()) {
            System.err.println("Illegal paramters in method CMV.distBetweenIdxPoints(int idx1, int idx2)");
            return -1;
        }
        if (idx1 == idx2) return 0;
        return Algebra.getDistance(launchData.getXCoords().get(idx1), launchData.getYCoords().get(idx1),
                launchData.getXCoords().get(idx2), launchData.getYCoords().get(idx2));
    }
}
