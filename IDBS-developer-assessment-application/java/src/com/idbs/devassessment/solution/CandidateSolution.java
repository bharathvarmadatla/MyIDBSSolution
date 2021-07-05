/*
 * Copyright (C) 1993-2020 ID Business Solutions Limited
 * All rights reserved
 */
package com.idbs.devassessment.solution;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import com.idbs.devassessment.core.IDBSSolutionException;
import com.idbs.devassessment.core.DifficultyLevel;

/**
 * Example solution for the example question
 */

public class CandidateSolution extends CandidateSolutionBase {
	@Override
	public DifficultyLevel getDifficultyLevel() {
		/*
		 * 
		 * CHANGE this return type to YOUR selected choice of difficulty level to which
		 * you will code an answer to.
		 * 
		 */

		return DifficultyLevel.LEVEL_1;
	}

	@Override
	public String getAnswer() throws IDBSSolutionException {

		// first get Json as a String for the question using the inherited method.
		String json = getDataForQuestion();

		// now use the json api to read the json to give a JsonObject.
		try {
			// if the json is in sting format with numeric expression
			if (json.startsWith("numeric")) {
				json = json.replace("numeric", "").replace(":", "");
				String[] jSplit = json.split(";");
				String xVal = jSplit[0].replace("x", "").replace("=", "").trim();
				
				//form the final polynomial expression
				String yVal = jSplit[1].replace("y", "").replace("=", "").replace("x", xVal);
				long finalValue = 0;
				
			    //split the polynomial expression by + or - signs
				String[] expoArray = yVal.split("[0-9]+(?<=[-+*/()])|(?=[-+*/()])");
				for (String expression : expoArray) {
					if (!expression.trim().isEmpty()) {
						finalValue = finalValue + exponentialValue(expression, xVal);
					}
				}
				// return the final value
				return Long.toString(finalValue);
			} else {
				JsonReader reader = Json.createReader(new StringReader(json));
				JsonObject jsonObject = reader.readObject();
				reader.close();

				// start extracting the data you need from the Json.
				// get the 'X' value from the Json.
				int xValue = jsonObject.getInt("xValue");

				// set the initial value for 'Y' as 0.
				long yValue = 0;

				// read the terms array from the Json.
				JsonArray jsonArray = jsonObject.getJsonArray("terms");

				if (jsonArray.size() > 0) {
					// now run a loop on the array values
					for (int i = 0; i < jsonArray.size(); i++) {
						JsonObject objectArray = jsonArray.getJsonObject(i);
						int powerVal = objectArray.getInt("power");

						// use custom made power function to get the exponential values
						long xPower = xToPowerOfy(xValue, powerVal);

						// use custom made multiplication function to multiply the values
						long multiplier = multiplyXandY(xPower, objectArray.getInt("multiplier"));

						// if the Json action is 'subtract' then apply subtraction to the function
						if (objectArray.getString("action").contains("subtract")) {
							multiplier = -(multiplier);
						}
						// calculate the answer value for 'Y'
						yValue = yValue + multiplier;
					}
				}
				return Long.toString(yValue);
			}
		} catch (JsonParsingException ex) {
			return null;
		}

	}

	public static long xToPowerOfy(int x, int y) {
		long result = x;
		if (y == 0)
			return 1;
		for (int i = 1; i < y; i++) {
			result = multiplyXandY(result, x);
		}
		return result;
	}

	public static long multiplyXandY(long x, long y) {
		if (y > 0 && x > 0)
			return (x + multiplyXandY(x, y - 1));

		if (y < 0 || x < 0)
			return -multiplyXandY(x, -y);
		else
			return 0;
	}

	public static long exponentialValue(String expression, String xVal) {

		try {
			String mVal = expression.split("\\.")[0];
			String pVal = expression.split("\\.")[1];
			if (mVal.startsWith("+")) {
				mVal = mVal.replace("+", "");
			}
			int powerVal = Integer.parseInt(pVal.split("\\^")[1]);
			long xPower = xToPowerOfy(Integer.parseInt(xVal), powerVal);
			long multiplier = multiplyXandY(xPower, Integer.parseInt(mVal));

			return multiplier;
		} catch (NullPointerException ex) {
			throw ex;
		}
	}

}
