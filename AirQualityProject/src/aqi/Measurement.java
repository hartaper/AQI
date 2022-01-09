package aqi;

public class Measurement {
	private String timestamp;
	private String sensorID;
	private String attributeID;
	private String value;
	public Sensor unnamed_Sensor_;
	public AttributeType unnamed_AttributeType_;


	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String attrDate) {
		this.timestamp = attrDate;
	}

	public String getSensorID() {
		return sensorID;
	}

	public void setSensorID(String sensorID) {
		this.sensorID = sensorID;
	}

	public String getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(String attributeID) {
		this.attributeID = attributeID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String attrValue) {
		this.value = attrValue;
	}

}