package com.decide.app;

import java.util.ArrayList;
/*
Preliminary Unlocking Matrix class containing boolean values
calculated using the Conditions Met Vector and the Logical Connector Matrix
*/
public class PUM{
	private ArrayList<ArrayList<Boolean>> pum;
	private Launch launchData;

	//preliminary constructor
	public PUM(Launch launchData) throws IllegalArgumentException {
		this.launchData = launchData;
		createPum();
	}
	
	// default constructor
	public PUM() {
		// NOP
	}


	private void createPum() throws IllegalArgumentException {
		ArrayList<ArrayList<String>> LCM = launchData.getLcm();
		ArrayList<Boolean> CMV = launchData.getCmv().getCmv();
		pum = new ArrayList<ArrayList<Boolean>>();
		for (int p = 0; p < 15; p++){
			pum.add(new ArrayList<>());
		}
		for(int i = 0; i < 15; i++){
			for (int j = 0; j < 15; j++) {
				if(LCM.get(i).get(j).equals("ANDD")) {
					pum.get(i).add(CMV.get(i) && CMV.get(j));
				}
				else if(LCM.get(i).get(j).equals("ORR")) {
					pum.get(i).add(CMV.get(i) || CMV.get(j));
				}
				else if(LCM.get(i).get(j).equals("NOTUSED")){
					pum.get(i).add(true);
				}
				else {
					throw new IllegalArgumentException("Invalid data in LCM");
				}
			}
		}

	}


	public ArrayList<ArrayList<Boolean>> getPum(){
		return pum;
	}

	public void setPum(ArrayList<ArrayList<Boolean>> arg) {
		this.pum = arg;
	}

	//function for calculating PUM elements
}
