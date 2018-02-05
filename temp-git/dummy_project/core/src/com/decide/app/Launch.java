package com.decide.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/*
Launch contains the DECIDE() function that will generate a boolean signal
which determines whether an interceptor should be launched
based upon input radar tracking information
*/
public class Launch{
	private int numPoints;
	private ArrayList<Integer> xCoords;
	private ArrayList<Integer> yCoords;
	private ArrayList<ArrayList<String>> lcm;
	private ArrayList<Boolean> puv;
	private Parameters params;

	private CMV cmv;
	private PUM pum;
	private FUV fuv;

	/*
	Initial constructor for Launch class
	*/
	public Launch(String datafile){
		Parser inputData = new Parser(datafile);
		numPoints = inputData.getNumPoints();
		xCoords = inputData.getXCoordinates();
		yCoords = inputData.getYCoordinates();
		lcm = inputData.getLcm();
		puv = inputData.getPuv();
		params = inputData.getParameters();
	}

	/*
	Default constructor for Launch class
	*/
	public Launch() {
		// NOP
		initializeDefaultLCM();
	}

	private void initializeDefaultLCM() {
		try {
			lcm = new ArrayList<>();
			for (int i = 0; i < 15; i++){
				lcm.add(new ArrayList<>());
			}
			/* parse LCM */
			BufferedReader br;
			File file = new File(System.getProperty("user.dir") + "//input//lcm.txt");
			String line;
			br = new BufferedReader(new FileReader(file));
			for (int i = 0; i < 15; i++){
				line = br.readLine();
				String[] logical_operators = line.split(" ");
				for (int j = 0; j < 15; j++){
					lcm.get(i).add(logical_operators[j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean decide() throws IllegalArgumentException {
		cmv = new CMV(this);
		cmv.calculateCmvVector();
		pum = new PUM(this);
		fuv = new FUV(this);

		//decide on boolean signal
		//if not all elements in FUV is true => no launch return false
		for(boolean val : fuv.getFuv()){
			if(val == false){
				return false;
			}
		}
		return true;
	}

	/*
	Getters
	*/
	public int getNumPoints() {
		return numPoints;
	}

	public ArrayList<Integer> getXCoords() {
		return xCoords;
	}

	public ArrayList<Integer> getYCoords() {
		return yCoords;
	}

	public ArrayList<ArrayList<String>> getLcm() {
		return lcm;
	}

	public ArrayList<Boolean> getPuv() {
		return puv;
	}

	public CMV getCmv() {
		return cmv;
	}

	public PUM getPum() {
		return pum;
	}

	public FUV getFuv() {
		return fuv;
	}

	public Parameters getParams() {
		return params;
	}

	/*
	Setters
	*/
	public void setNumPoints(int arg) {
		this.numPoints = arg;
	}

	public void setXCoords(ArrayList<Integer> arg) {
		this.xCoords = arg;
	}

	public void setYCoords(ArrayList<Integer> arg) {
		this.yCoords = arg;
	}

	public void setLcm(ArrayList<ArrayList<String>> arg) {
		this.lcm = arg;
	}

	public void setPuv(ArrayList<Boolean> arg) {
		this.puv = arg;
	}

	public void setParams(Parameters arg) {
		this.params = arg;
	}

	public void setPum(PUM arg) {
		this.pum = arg;
	}

	public void setCmv(CMV arg){
		this.cmv = arg;
	}
}
