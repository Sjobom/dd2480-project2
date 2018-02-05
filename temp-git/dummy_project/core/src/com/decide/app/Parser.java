package com.decide.app;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

/*
Parser class for reading DATAPOINTS
Assumes that the format of the input file is as follows:
N
X_1 Y_1
X_2 Y_2
... ...
X_N Y_N
PARAMETERS_T
LCM (OP_0_0,....,OP_0_14
	 OP_1_0,....,OP_1_14
	 ...
	 OP_14_0,...,OP_14_14)
PUV (BOOL_0,...,BOOL_14)
*/
public class Parser {

	private int numPoints;
	private ArrayList<Integer> xCoords;
	private ArrayList<Integer> yCoords;
	private Parameters params;
	private ArrayList<ArrayList<String>> lcm;
	private ArrayList<Boolean> puv;

	public Parser(String datafile) {

		xCoords = new ArrayList<Integer>();
		yCoords = new ArrayList<Integer>();
		params  = new Parameters();
		puv = new ArrayList<Boolean>();
		lcm = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 15; i++){
			lcm.add(new ArrayList<String>());
		}

		BufferedReader br;

		try {
			/* parse numPoints */
			br = new BufferedReader(new FileReader(datafile));
			numPoints = Integer.parseInt(br.readLine());

			/* parse points */
			String line;
			String[] coords;
			for (int i = 0; i < numPoints; i++) {
				line = br.readLine();	
				coords = line.split(" ");
				xCoords.add(Integer.parseInt(coords[0]));
				yCoords.add(Integer.parseInt(coords[1]));
			}

			/* parse parameters */
			params.length1 = Double.parseDouble(br.readLine());
			params.radius1 = Double.parseDouble(br.readLine());
			params.epsilon = Double.parseDouble(br.readLine());
			params.area1 = Double.parseDouble(br.readLine());
			params.q_pts = Integer.parseInt(br.readLine());
			params.quads = Integer.parseInt(br.readLine());
			params.dist = Double.parseDouble(br.readLine());
			params.n_pts = Integer.parseInt(br.readLine());
			params.k_pts = Integer.parseInt(br.readLine());
			params.a_pts = Integer.parseInt(br.readLine());
			params.b_pts = Integer.parseInt(br.readLine());
			params.c_pts = Integer.parseInt(br.readLine());
			params.d_pts = Integer.parseInt(br.readLine());
			params.e_pts = Integer.parseInt(br.readLine());
			params.f_pts = Integer.parseInt(br.readLine());
			params.g_pts = Integer.parseInt(br.readLine());
			params.length2 = Integer.parseInt(br.readLine());
			params.radius2 = Integer.parseInt(br.readLine());
			params.area2 = Integer.parseInt(br.readLine());

			/* parse LCM */
			for (int i = 0; i < 15; i++){
				line = br.readLine();
				String[] logical_operators = line.split(" ");
				for (int j = 0; j < 15; j++){
					lcm.get(i).add(logical_operators[j]);
				}
			}

			/* parse PUV */
			line = br.readLine();
			String[] bools = line.split(" ");
			for (int i = 0; i < 15; i++){
				if(bools[i].equals("true")){
					puv.add(true);
				} else if (bools[i].equals("false")){
					puv.add(false);
				}
			}

			br.close();
			
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}

	public ArrayList<Integer> getXCoordinates() {
		return xCoords;
	}

	public ArrayList<Integer> getYCoordinates() {
		return yCoords;
	}

	public Parameters getParameters() {
		return params;
	}

	public int getNumPoints(){
		return numPoints;
	}

	public ArrayList<ArrayList<String>> getLcm() {
		return lcm;
	}

	public ArrayList<Boolean> getPuv() {
		return puv;
	}
}
