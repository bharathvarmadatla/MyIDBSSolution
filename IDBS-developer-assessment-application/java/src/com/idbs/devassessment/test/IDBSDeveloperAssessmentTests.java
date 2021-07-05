package com.idbs.devassessment.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.jupiter.api.Test;


public class IDBSDeveloperAssessmentTests {

	@Test
	public void getAnswer(){

		String json = "{\"xValue\" : 2,\"terms\" : [{ \"power\" : 2, \"multiplier\" : 1, \"action\" : \"add\" },{ \"power\" : 3, \"multiplier\" : 1, \"action\" : \"add\" },{ \"power\" : 1, \"multiplier\" : 4, \"action\" : \"add\" },{ \"power\" : 4, \"multiplier\" : 1, \"action\" : \"add\" },{ \"power\" : 6, \"multiplier\" : 2, \"action\" : \"add\" },{ \"power\" : 5, \"multiplier\" : 3, \"action\" : \"add\" },{ \"power\" : 0, \"multiplier\" : 5, \"action\" : \"subtract\" }]}";
		JsonReader reader = Json.createReader(new StringReader(json));
		JsonObject jsonObject = reader.readObject();reader.close();

		int xValue = jsonObject.getInt("xValue");
		assertEquals(2,xValue);
		long yValue = 0;
		JsonArray jsonArray = jsonObject.getJsonArray("terms");
		assertEquals(7, jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject objectArray = jsonArray.getJsonObject(i);
			int powerVal = objectArray.getInt("power");
			assertNotNull(powerVal);			
			long xPower = (long)Math.pow(xValue, powerVal);
			assertNotNull(xPower);			
			long multiplier = xPower * objectArray.getInt("multiplier");
			assertNotNull(multiplier);			
			if (objectArray.getString("action").contains("subtract")) {
				assertEquals("subtract", objectArray.getString("action"));
				multiplier = -(multiplier);
			}
			yValue = yValue + multiplier;
			assertNotNull(yValue);
		}		
		assertNotNull(Long.toString(yValue));
		assertEquals(255,yValue);
	}
}
