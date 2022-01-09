package test.aqi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import aqi.AQI;

public class AQITest  {
	
	@Test
	public void testAggregateByTerritoryAndTime() {
		AQI aqi = new AQI();
		loadFileMeasurementsForTest(aqi);
		String sensorID = "Sensor0";
		String timestamp = "2017-01-01T00:01:20.6090000";
		
		double expected = 18.893455570387065;
		double result = aqi.aggregateByTerritoryAndTime(sensorID, timestamp);
		
		assertEquals(expected, result, 0.0);
	}
	
	
	@Test
	public void testAggregateByTimespanAndTerritory() {
		AQI aqi = new AQI();
		loadFileMeasurementsForTest(aqi);
		String sensorID = "Sensor0";	
		String startTimestamp = "2017-01-01T00:01:20.6090000";
		String endTimestamp = "2017-09-22T17:01:00.2220000";
		
		double expected = 186.37826208439918;
		double result = aqi.aggregateByTimespanAndTerritory(startTimestamp, endTimestamp, sensorID);
		
		assertEquals(expected, result, 0.0);
	}
	
	@Test
	public void testAggregateByTimespan() {
		AQI aqi = new AQI();
		loadFileMeasurementsForTest(aqi);
		String start_date = "2017-01-01";
		String start_time = "00:00";
		String end_date  = "2017-01-01";
		String end_time = "01:30";
		
		double expected = 41.141060066444005;
		double result = aqi.aggregateByTimespan(start_date, start_time, end_date, end_time);
		
		assertEquals(expected, result, 0.0);
	}
	
	@Test
	public void testLoadFileAttributeType() {
		AQI aqi = new AQI();
		loadFileAttributeTypeForTest(aqi);
		
		// test that the list is not empty
		int sizeList = aqi.getAllAttributeTypes().size();
		assertTrue(sizeList > 0);
	}
	
	private void loadFileMeasurementsForTest(AQI aqi) {
		try {
			aqi.loadFileMeasurements();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadFileAttributeTypeForTest(AQI aqi) {
		try {
			aqi.loadFileAttributeType();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
