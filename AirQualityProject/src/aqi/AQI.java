package aqi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

//Class for calculate: load data from files and aggregate information
public class AQI {

	private List<Measurement> allMeasurements = new ArrayList<Measurement>();
	private List<Sensor> allSensors = new ArrayList<Sensor>();
	private List<AttributeType> allAttributeTypes = new ArrayList<AttributeType>();

	// Create and show GUI
	void createWindow(AQI aqi) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI gui = new GUI();
				gui.createAndShowGUI(aqi);
			}
		});
	}

	public static void main(String[] args) {
		AQI aqi = new AQI();

		// First we load all csv files, when the program is started
		try {
			aqi.loadFileAttributeType();
			aqi.loadFileMeasurements();
			aqi.loadFileSensor();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// then we create and show GUI
		aqi.createWindow(aqi);
		
		//TESTING THE METHOD LATEST MEASUREMENT WITH FILES
		try { aqi.loadFileAttributeType(); aqi.loadFileMeasurements();
		  aqi.loadFileSensor();
		  
		  aqi.aggregateByTerritoryAndTime("Sensor0", "2017-01-01T00:01:20.6090000");
		  
		  aqi.aggregateByTimespan("2017-01-01", "00:00", "2017-01-01", "01:30");
		  
		  // We create a new file and put all the measurements 
		  File measurments = new File("src/resources/data_10sensors_1year.csv");
		 
		  // We create a new String for our latestMeasurementTool 
		  String latestMeasurement = aqi.latestMeasurementTool(measurments, 79);
		 
		  // We split the string and store it into an array 
		  String[] latestMeasurementBottom = latestMeasurement.split("\\n");
		  
		  // Get the latest measurements of all the sensors by reading the bottom of the file 
		  //The csv has empty lines on every other row so we need to read double the amount of rows (79) 
		  System.out.println(latestMeasurement);
		  
		  //Test to see the latest measurements for Sensor1
		   String sesnor1Test = aqi.latestMeasurementSensor(latestMeasurementBottom,"Sensor1");
		   System.out.println(sesnor1Test);
		  
		   System.out.println();
		   
		   //Printing similar values
		   System.out.println(similarValues(measurments, "PM10", 21.0, 1.0));
		  
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	
	// Calculate average for a sensor at a given timestamp
	public double aggregateByTerritoryAndTime(String sensorID, String timestamp) {
		double avg;

		// new list for our calculation
		List<Measurement> sensorMeasurements = new ArrayList<Measurement>();

		// for each element in the list we compare the properties: sensorID and
		// timestamp with the parameters
		// if they are equal we keep the element in a new list for our calculation
		for (int i = 0; i < allMeasurements.size(); i++) {
			Measurement measurement = allMeasurements.get(i);
			if (measurement.getSensorID().equals(sensorID) && measurement.getTimestamp().equals(timestamp)) {
				sensorMeasurements.add(measurement);
			}
		}

		ArrayList<Double> meas_values = new ArrayList<Double>();
		// from our calculation list we took the property: value and add it into the sum
		for (int i = 0; i < sensorMeasurements.size(); i++) {
			Measurement measurement = sensorMeasurements.get(i);
			String value = measurement.getValue();
			double dblValue = Double.valueOf(value);

			meas_values.add(dblValue);
		}

		avg = calculateAverage(meas_values);

		System.out.println("Average by sensor type and timsestamp: " + avg);
		return avg;

	}

	public double aggregateByTimespanAndTerritory(String startTimestamp, String endTimestamp, String sensorID) {

		ArrayList<Double> meas_values = new ArrayList<Double>();

		// Extract from timestamp all its components: date, time: hour, minute, second
		// timestamp : 2017-01-01T00:01:20.6090000
		String[] arrayStartTimestamp = startTimestamp.split("T"); // 2017-01-01 00:01:20.6090000
		String start_date = arrayStartTimestamp[0]; // 2017-01-01
		String start_time = arrayStartTimestamp[1]; // 00:01:20.6090000

		String[] arrayEndTimestamp = endTimestamp.split("T");
		String end_date = arrayEndTimestamp[0];
		String end_time = arrayEndTimestamp[1];

		String[] selected_start_time = start_time.split(":");
		int start_hour = Integer.parseInt(selected_start_time[0]);
		int start_minute = Integer.parseInt(selected_start_time[1]);

		String[] selected_end_time = end_time.split(":");
		int end_hour = Integer.parseInt(selected_end_time[0]);
		int end_minute = Integer.parseInt(selected_end_time[1]);

		String[] selected_start_date = start_date.split("-");
		int start_year = Integer.parseInt(selected_start_date[0]);
		int start_month = Integer.parseInt(selected_start_date[1]);
		int start_day = Integer.parseInt(selected_start_date[2]);

		String[] selected_end_date = end_date.split("-");
		int end_year = Integer.parseInt(selected_end_date[0]);
		int end_month = Integer.parseInt(selected_end_date[1]);
		int end_day = Integer.parseInt(selected_end_date[2]);


		// for each element in the list we compare the properties: sensorID and timestamp with the parameters
		// if they are equal we keep the element in a new list for our calculation
		for (int i = 0; i < allMeasurements.size(); i++) {
			Measurement measurement = allMeasurements.get(i);

			String timestampMeasurement = measurement.getTimestamp(); // 2017-01-01T00:01:20.6090000
			String[] timestamp = timestampMeasurement.split("T"); // 2017-01-01 00:01:20.6090000
			String[] date = timestamp[0].split("-"); // 2017 01 01
			String[] time = timestamp[1].split(":"); // 00 01 20.609000

			int year = Integer.parseInt(date[0]);
			int month = Integer.parseInt(date[1]);
			int day = Integer.parseInt(date[2]);

			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);

			String sensor = measurement.getSensorID();

			if (sensor.equals(sensorID) && year >= start_year && year <= end_year && month >= start_month && month <= end_month && day >= start_day && day <= end_day && hour >= start_hour
					&& hour <= end_hour && minute >= start_minute && minute <= end_minute) {

				String value = measurement.getValue();
				double dblValue = Double.valueOf(value);
				meas_values.add(dblValue);
			}
		}

		double avg = calculateAverage(meas_values);
		System.out.println("Average by Timespan and Territory: " + avg);
		return avg;
	}

	public double aggregateByTimespan(String start_date, String start_time, String end_date, String end_time) {
		ArrayList<Double> meas_values = new ArrayList<Double>();

		String[] selected_start_time = start_time.split(":");
		int start_hour = Integer.parseInt(selected_start_time[0]);
		int start_minute = Integer.parseInt(selected_start_time[1]);

		String[] selected_end_time = end_time.split(":");
		int end_hour = Integer.parseInt(selected_end_time[0]);
		int end_minute = Integer.parseInt(selected_end_time[1]);

		String[] selected_start_date = start_date.split("-");
		int start_year = Integer.parseInt(selected_start_date[0]);
		int start_month = Integer.parseInt(selected_start_date[1]);
		int start_day = Integer.parseInt(selected_start_date[2]);

		String[] selected_end_date = end_date.split("-");
		int end_year = Integer.parseInt(selected_end_date[0]);
		int end_month = Integer.parseInt(selected_end_date[1]);
		int end_day = Integer.parseInt(selected_end_date[2]);

		// new list for our calculation
		// List<Measurement> sensorMeasurements = new ArrayList<Measurement>();

		// for each element in the list we compare the properties: sensorID and timestamp with the parameters
		// if they are equal we keep the element in a new list for our calculation
		for (int i = 0; i < allMeasurements.size(); i++) {
			Measurement measurement = allMeasurements.get(i);

			String timestampMeasurement = measurement.getTimestamp(); // 2017-01-01T00:01:20.6090000
			String[] timestamp = timestampMeasurement.split("T"); // 2017-01-01 00:01:20.6090000
			String[] date = timestamp[0].split("-"); // 2017 01 01
			String[] time = timestamp[1].split(":"); // 00 01 20.609000

			int year = Integer.parseInt(date[0]);
			int month = Integer.parseInt(date[1]);
			int day = Integer.parseInt(date[2]);

			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);

			if (year >= start_year && year <= end_year && month >= start_month && month <= end_month && day >= start_day && day <= end_day && hour >= start_hour && hour <= end_hour
					&& minute >= start_minute && minute <= end_minute) {

				String value = measurement.getValue();
				double dblValue = Double.valueOf(value);
				meas_values.add(dblValue);
			}
		}

		double avg = calculateAverage(meas_values);
		System.out.println("Average by sensor type and timsespan: " + avg);
		return avg;
	}

	// Subroutine for calculating average of items in a list
	public static double calculateAverage(ArrayList<Double> list) {
		double sum = 0.0;
		if (!list.isEmpty()) {
			for (Double mark : list) {
				sum += mark;
			}
			return sum / list.size();
		}
		return sum;
	}

	// Finding the values that characterize air quality at a given place
	public double aggregateByTerritory(String sensorID) {
		double avg;

		// new list for our calculation
		List<Measurement> sensorMeasurements = new ArrayList<Measurement>();

		// for each element in the list we compare the properties: sensorID with the
		// parameters
		// if they are equal we keep the element in a new list for our calculation
		for (int i = 0; i < allMeasurements.size(); i++) {
			Measurement measurement = allMeasurements.get(i);
			if (measurement.getSensorID().equals(sensorID)) {
				sensorMeasurements.add(measurement);
			}
		}

		double sumOfValues = 0;
		// from our calculation list we took the property: value and add it into the sum
		for (int i = 0; i < sensorMeasurements.size(); i++) {
			Measurement measurement = sensorMeasurements.get(i);
			String value = measurement.getValue();
			double dblValue = Double.valueOf(value);
			sumOfValues = sumOfValues + dblValue;
		}

		double numberOfValues = sensorMeasurements.size();
		avg = sumOfValues / numberOfValues;

		System.out.println("Average by sensor type: " + avg);
		return avg;

	}

	// Load AttributeType.csv into the list with AttributeType objects:
	// allAttributeTypes
	public void loadFileAttributeType() throws URISyntaxException, IOException {
		String filePath = "src/resources/AttributeType.csv";

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
		try (CSVReader readerFile = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser).withSkipLines(1) // skip the first line, header info
				.build()) {
			List<String[]> fileLines = readerFile.readAll();

			// for each line in the fileLines we create/instantiate an attributeType, fill
			// it and we add it into the list
			for (int i = 0; i < fileLines.size(); i++) {
				String[] fileLine = fileLines.get(i);
				// System.out.println(Arrays.toString(fileLine));

				// create a new attributeType which corresponds to this file line
				AttributeType attributeType = new AttributeType();
				String attrID = fileLine[0];
				String attrUnit = fileLine[1];
				String attrDescription = fileLine[2];

				// set the properties for this attribute type
				attributeType.setAttributeID(attrID);
				attributeType.setUnit(attrUnit);
				attributeType.setDescription(attrDescription);

				// We insert this attributeType into the list
				allAttributeTypes.add(attributeType);

			}
			System.out.println(" allAttributeTypes : " + allAttributeTypes.size());
		}
	}// loadFileAttributeType

	// Load data_10sensors_1year.csv into the list with Measurement objects:
	// allMeasurements
	public void loadFileMeasurements() throws URISyntaxException, IOException {
		String filePath = "src/resources/data_10sensors_1year.csv";

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator

		// custom CSV parser
		try (CSVReader readerFile = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser).withSkipLines(1) // skip the first line, header info
				.build()) {
			List<String[]> fileLines = readerFile.readAll();

			// for each line in the fileLines we create/instantiate an attributeType, fill
			// it and we add it into the list
			for (int i = 0; i < fileLines.size(); i++) {
				String[] fileLine = fileLines.get(i);

				// create a new Measurement which corresponds to this file line
				Measurement measurement = new Measurement();
				String attrDate = fileLine[0];
				String attrSensorID = fileLine[1];
				String attrID = fileLine[2];
				String attrValue = fileLine[3];

				// set the properties for this attribute type
				measurement.setTimestamp(attrDate);
				measurement.setSensorID(attrSensorID);
				measurement.setAttributeID(attrID);
				measurement.setValue(attrValue);

				// We insert this allMeasurements into the list
				allMeasurements.add(measurement);
			}
			System.out.println(" allMeasurements : " + allMeasurements.size());
		}
	}// loadFileMeasurements

	// Load Sensors.csv into the list with Sensor objects: allSensors
	public void loadFileSensor() throws URISyntaxException, IOException {
		String filePath = "src/resources/Sensors.csv";

		CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator

		try (CSVReader readerFile = new CSVReaderBuilder(new FileReader(filePath)).withCSVParser(csvParser).withSkipLines(1) // skip the first line, header info
				.build()) {
			List<String[]> fileLines = readerFile.readAll();

			// for each line in the fileLines we create/instantiate an attributeType, fill
			// it and we add it into the list
			for (int i = 0; i < fileLines.size(); i++) {
				String[] fileLine = fileLines.get(i);

				// create a new attributeType which corresponds to this file line
				Sensor sensor = new Sensor();
				String attrSensorID = fileLine[0];
				String attrLatitude = fileLine[1];
				String attrLongitude = fileLine[2];
				String attrDescription = fileLine[3];

				// set the properties for this attribute type
				sensor.setSensorID(attrSensorID);
				sensor.setLatitude(attrLatitude);
				sensor.setLongitude(attrLongitude);
				sensor.setDescription(attrDescription);

				// We insert this allMeasurements into the list
				allSensors.add(sensor);
			}
			System.out.println(" allSensors : " + allSensors.size());
		}
	}// loadFileSensors

	public List<Measurement> getAllMeasurements() {
		return allMeasurements;
	}

	public void setAllMeasurements(List<Measurement> allMeasurements) {
		this.allMeasurements = allMeasurements;
	}

	public List<Sensor> getAllSensors() {
		return allSensors;
	}

	public void setAllSensors(List<Sensor> allSensors) {
		this.allSensors = allSensors;
	}

	public List<AttributeType> getAllAttributeTypes() {
		return allAttributeTypes;
	}

	public void setAllAttributeTypes(List<AttributeType> allAttributeTypes) {
		this.allAttributeTypes = allAttributeTypes;
	}

	// The method to get latest measurement for the Sensor
	public String latestMeasurementSensor(String[] measure, String sensor) {
		for (int i = 1; i < measure.length; i++) {
			String[] line = measure[i].split(";");
			if (line[1].equals(sensor)) {
				for (int j = 0; j < line.length; j++) {
					System.out.print(" " + line[j]);
				}
			}
		}
		return sensor;
	}

	// The tail that counts from the bottom of the file and takes a certain number
	// of lines we input
	public String latestMeasurementTool(File measurments, int lines) {
		java.io.RandomAccessFile fileHandler = null;
		try {
			fileHandler = new java.io.RandomAccessFile(measurments, "r");
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();
			int line = 0;

			for (long filePointer = fileLength; filePointer != -1; filePointer--) {
				fileHandler.seek(filePointer);
				int readByte = fileHandler.readByte();

				if (readByte == 0xA) {
					if (filePointer < fileLength) {
						line = line + 1;
					}
				} else if (readByte == 0xD) {
					if (filePointer < fileLength - 1) {
						line = line + 1;
					}
				}
				if (line >= lines) {
					break;
				}
				sb.append((char) readByte);
			}

			String lastLine = sb.reverse().toString();
			return lastLine;
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fileHandler != null)
				try {
					fileHandler.close();
				} catch (IOException e) {
				}
		}
	}

	// Find measurements with similar amounts of pollutant
	public static ArrayList<String> similarValues(File file, String pollutant, double value, double max_difference) {

		ArrayList<String> similar_meas = new ArrayList<String>();
		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(file));
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(";");
				double meas_value = Double.parseDouble(row[3]);
				if (row[2].equals(pollutant) && (value == meas_value || (value >= meas_value - max_difference && value <= meas_value + max_difference))) {
					similar_meas.add(line);
				}

			}
			return similar_meas;
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return similar_meas;

	}
}