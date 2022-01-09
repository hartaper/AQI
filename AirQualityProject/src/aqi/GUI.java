package aqi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;

// Class for creating the GUI (not for calculate)
public class GUI {

	static final Font FONT_SANS_SERIF_BOLD = new Font("SansSerif", Font.BOLD, 20);
	static final Font FONT_SANS_SERIF_PLAIN = new Font("SansSerif", Font.PLAIN, 20);

	// here we have only controls and we use aqi to access data
	// aqi is set when we call createAndShowGUI(AQI aqi)
	AQI aqi;

	JFrame myFrame;
	JPanel mainPanel;
	JPanel calculatePanel;
	JPanel helpPanel;

	JList listSensors;
	JList listStartTimestampList;
	JList listEndTimestampList;
	JButton btnAggregateByTerritoryAndTime;
	JLabel lblAggregateByTerritoryAndTime;
	JButton btnAggregateByTerritoryAndTimespan;
	JLabel lblAggregateByTerritoryAndTimespan;

	// Create the GUI and use the instance aqi of the class AQI to fill the lists and to call the calcutation from the buttons
	// All data are in the instance aqi; here we have only controls and we use aqi to access data when necessary
	public void createAndShowGUI(AQI parameterAqi) {
		aqi = parameterAqi;
		myFrame = new JFrame("Air Quality");

		ImageIcon iconFrame = new ImageIcon("src//resources//aqi-icon.png");
		myFrame.setIconImage(iconFrame.getImage());
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);

		createMainPanel(aqi);

		myFrame.pack();
		myFrame.setResizable(false);
	}

	private void createMainPanel(AQI aqi) {
		// main panel containing the settings and the buttons
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(new Color(253, 248, 232));
		mainPanel.setPreferredSize(new DimensionUIResource(800, 500));

		myFrame.getContentPane().add(mainPanel);

		// Settings: Sensor
		JLabel lblSensor = createLabel("Sensor: ");
		JScrollPane listSensorsScroller = createSensorsList(aqi);

		// Settings: 2 Timestamp
		JLabel lblTimestampStart = createLabel("Start Timestamp: ");
		JScrollPane startTimestampList = createStartTimestampList(aqi);
		JLabel lblTimestampEnd = createLabel("End Timestamp: ");
		JScrollPane endTimestampList = createEndTimestampList(aqi);

		// Button AggregateByTerritoryAndTime and JLabel for display the result
		btnAggregateByTerritoryAndTime = createButton("Aggregate by Territory and Time");
		lblAggregateByTerritoryAndTime = createLabelForResult("...");

		// Button AggregateByTerritoryAndTimespan and JLabel for display the result
		btnAggregateByTerritoryAndTimespan = createButton("Aggregate by Territory and Timespan");
		lblAggregateByTerritoryAndTimespan = createLabelForResult("...");

		// Add all controls to mainPanel (to frame) mainPanel will be a GridBagLayout with 2 columns

		// Use one GridBagConstraints instance for all components the GridBagLayout manages
		// Just before each component is added to the container, the code sets again the appropriate instance variables in the GridBagConstraints object
		GridBagConstraints c = new GridBagConstraints();

		// row 1: lblSensor ; listSensorsScroller
		// row 2: lblTimestampStart ; startTimestampList
		// row 3: lblTimestampEnd ; lblTimestampEnd
		// row 4: btnAggregateByTerritoryAndTime ; lblAggregateByTerritoryAndTime
		// row 5: btnAggregateByTerritoryAndTimespan ;
		// lblAggregateByTerritoryAndTimespan

		// row 1: lblSensor
		c.insets = new InsetsUIResource(10, 50, 0, 0); // top=10 ; left=50
		// gridx=0 -> col 1; gridy=0 -> row 1 ; gridwidth=1 -> 1 column wide
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 20, 0.5, 0, 0, 1);
		mainPanel.add(lblSensor, c);

		// row 1: listSensorsScroller
		// gridx=1 -> col 2; gridy=0 -> row 1
		c.insets = new InsetsUIResource(10, 50, 0, 50); // top=10 ; right=50
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 20, 0.5, 1, 0, 1);
		mainPanel.add(listSensorsScroller, c);

		// row 2: lblTimestampStart
		// gridx=0 -> col 1; gridy=1 -> row 2
		c.insets = new InsetsUIResource(10, 50, 0, 0);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 0, 0.5, 0, 1, 1);
		mainPanel.add(lblTimestampStart, c);

		// row 2: startTimestampList
		// gridx=1 -> col 2; gridy=1 -> row 2
		c.insets = new InsetsUIResource(10, 50, 0, 50);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 20, 0.5, 1, 1, 1);
		mainPanel.add(startTimestampList, c);

		// row 3: lblTimestampEnd
		// gridx=0 -> col 1; gridy=2 -> row 3
		c.insets = new InsetsUIResource(10, 50, 0, 0);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 0, 0.5, 0, 2, 1);
		mainPanel.add(lblTimestampEnd, c);

		// row 3: endTimestampList
		// gridx=1 -> col 2; gridy=2 -> row 3
		c.insets = new InsetsUIResource(10, 50, 0, 50);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 20, 0.5, 1, 2, 1);
		mainPanel.add(endTimestampList, c);

		// row 4: btnAggregateByTerritoryAndTime
		// gridx=0 -> col 1; gridy=3 -> row 4
		c.insets = new InsetsUIResource(10, 50, 0, 0); // right=0
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 0, 0.5, 0, 3, 1);
		mainPanel.add(btnAggregateByTerritoryAndTime, c);

		// row 4: lblAggregateByTerritoryAndTime
		// gridx=1 -> col 2; gridy=3 -> row 4
		c.insets = new InsetsUIResource(10, 50, 0, 100);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 20, 0.5, 1, 3, 1);
		mainPanel.add(lblAggregateByTerritoryAndTime, c);

		// row 5: btnAggregateByTerritoryAndTimespan
		// gridx=0 -> col 1; gridy=4 -> row 5
		c.insets = new InsetsUIResource(10, 50, 0, 0);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 0, 0.5, 0, 4, 1);
		mainPanel.add(btnAggregateByTerritoryAndTimespan, c);

		// row 5: lblAggregateByTerritoryAndTimespan
		// gridx=1 -> col 2; gridy=4 -> row 5
		c.insets = new InsetsUIResource(10, 50, 0, 100);
		prepareGridBagConstraints(c, GridBagConstraints.HORIZONTAL, 20, 0.5, 1, 4, 1);
		mainPanel.add(lblAggregateByTerritoryAndTimespan, c);

		// add listener to buttons; we use the same listener
		btnAggregateByTerritoryAndTime.addActionListener(new AggregateButtonListener());
		btnAggregateByTerritoryAndTimespan.addActionListener(new AggregateButtonListener());
	}

	private JLabel createLabel(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(FONT_SANS_SERIF_PLAIN);
		lbl.setPreferredSize(new DimensionUIResource(80, 60));

		return lbl;
	}

	private JLabel createLabelForResult(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(FONT_SANS_SERIF_BOLD);
		lbl.setPreferredSize(new DimensionUIResource(120, 60));
		lbl.setBackground(Color.lightGray);
		lbl.setForeground(Color.red);
		lbl.setOpaque(false);

		return lbl;
	}

	private JButton createButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(FONT_SANS_SERIF_PLAIN);
		btn.setPreferredSize(new DimensionUIResource(300, 60));

		return btn;
	}

	private JTextField createTextField(String text) {
		JTextField txt = new JTextField(text);
		txt.setFont(FONT_SANS_SERIF_PLAIN);
		txt.setPreferredSize(new DimensionUIResource(300, 60));

		return txt;
	}

	private JScrollPane createSensorsList(AQI aqi) {

		DefaultListModel listModel = new DefaultListModel();
		List listValues = new ArrayList();

		List<Sensor> allSensors = aqi.getAllSensors();
		for (int i = 0; i < allSensors.size(); i++) {
			Sensor sensor = allSensors.get(i);
			String sensorId = sensor.getSensorID();
			listValues.add(sensorId);
		}

		// order the elements in list
		Collections.sort(listValues);

		// fill listModel with the ordered list
		listModel.addAll(listValues);

		// Create the list and put it in a scroll pane
		JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(10);

		// keep the list outside in our variable
		listSensors = list;

		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new DimensionUIResource(50, 50));

		return listScrollPane;
	}

	// Get a list of timestamp from the allMeasurements list
	// In allMeasurements the values are not ordered and it can contain the same
	// timestamp multiple times
	private List getTimestampList() {
		List listValues = new ArrayList();

		List<Measurement> allMeasurements = aqi.getAllMeasurements();
		for (int i = 0; i < allMeasurements.size(); i++) {
			Measurement timestamp = allMeasurements.get(i);
			String timestampM = timestamp.getTimestamp();
			listValues.add(timestampM);
		}

		// Remove the duplicates
		HashSet hset = new HashSet<>(listValues);
		List listValuesNoDuplicates = new ArrayList(hset);

		// In allMeasurements the values are not ordered: order the elements in list
		Collections.sort(listValuesNoDuplicates);

		return listValuesNoDuplicates;
	}

	private JScrollPane createStartTimestampList(AQI aqi) {

		DefaultListModel listModel = new DefaultListModel();

		List listValues = getTimestampList();

		// fill listModel with the ordered list
		listModel.addAll(listValues);

		// Create the list and put it in a scroll pane
		JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(10);

		// keep the list outside in our variable
		listStartTimestampList = list;

		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new DimensionUIResource(50, 50));

		return listScrollPane;
	}

	private JScrollPane createEndTimestampList(AQI aqi) {

		DefaultListModel listModel = new DefaultListModel();

		List listValues = getTimestampList();

		// fill listModel with the ordered list
		listModel.addAll(listValues);

		// Create the list and put it in a scroll pane
		JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(10);
		// list.addListSelectionListener(this);

		// keep the list outside in our variable
		listEndTimestampList = list;

		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new DimensionUIResource(50, 50));

		return listScrollPane;
	}

	// Set in the GridBagConstraints c all the properties with the values from the
	// received parameters
	private void prepareGridBagConstraints(GridBagConstraints c, int fill, int ipady, double weightx, int gridx, int gridy, int gridwidth) {
		c.fill = fill;
		c.ipady = ipady;
		c.weightx = weightx;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
	}

	public class AggregateButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			lblAggregateByTerritoryAndTime.setText("...");
			lblAggregateByTerritoryAndTimespan.setText("...");

			// Find which button was used
			if (e.getSource() == btnAggregateByTerritoryAndTime) {
				// the button btnAggregateByTerritoryAndTime was used: we call aggregateByTerritoryAndTime

				// we get the selected values for calculus: sensorId and startTime
				String sensorId = (String) listSensors.getSelectedValue();
				String startTime = (String) listStartTimestampList.getSelectedValue();

				// we call the calculus from aqi:
				double avg = aqi.aggregateByTerritoryAndTime(sensorId, startTime);
				// here I need to have aggregateByTerritoryAndTime defined only with 2
				// parameters

				String textAvg = String.valueOf(avg);
				lblAggregateByTerritoryAndTime.setText(textAvg);

			} else if (e.getSource() == btnAggregateByTerritoryAndTimespan) {
				// the button btnAggregateByTerritoryAndTimespan was used: we call aggregateByTerritoryAndTimespan

				// we call the calculus from aqi:

				// we get the selected values for calculus: sensorId , startTime and endTime
				String sensorId = (String) listSensors.getSelectedValue();
				String startTime = (String) listStartTimestampList.getSelectedValue();
				String endTime = (String) listEndTimestampList.getSelectedValue();

				double avg = aqi.aggregateByTimespanAndTerritory(startTime, endTime, sensorId);
				// here I need to have aggregateByTimespan defined only with 3 parameters

				String textAvg = String.valueOf(avg);
				lblAggregateByTerritoryAndTimespan.setText(textAvg);
			}
		}
	}

}
