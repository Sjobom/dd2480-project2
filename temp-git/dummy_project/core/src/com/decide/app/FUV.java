package com.decide.app;

import java.util.ArrayList;
/*
Final Unlocking Vector generated from the Prelimnary Unlocking Matrix
Containing boolean values for signaling the interceptor launch.
*/
public class FUV{
	public static final int FUV_SIZE = 15;

	private ArrayList<Boolean> fuv;
	private Launch launchData;

	//preliminary constructor
	public FUV(Launch launchData){
		fuv = new ArrayList<Boolean>(FUV_SIZE);
		this.launchData = launchData;

		//call calc func
		calculateFuv();
	}

	public ArrayList<Boolean> getFuv(){
		return fuv;
	}

	private boolean allTrue(ArrayList<Boolean> arr) {
		for (Boolean b : arr)
			if (!b) return false;
		return true;
	}

	/*
	Function for calculating the FUV array
	FUV[i] = true if PUV[i] == false
	or
	FUV[i] = true if PUM[i, 0 .. n] == true
	else
	FUV[i] = false
	*/
	public void calculateFuv() {
		ArrayList<Boolean> puv = launchData.getPuv();
		ArrayList<ArrayList<Boolean>> pum = launchData.getPum().getPum();

		for (int i = 0; i < FUV_SIZE; i++) {
			if (!puv.get(i)) {
			// PUV[i] == false
				fuv.add(true);
			} else if (allTrue(pum.get(i))) { 
			// PUM[i, 0 .. n] == true
				fuv.add(true);
			} else {
				fuv.add(false);
			}
		}
	}
}
