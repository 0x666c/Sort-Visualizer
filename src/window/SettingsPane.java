package window;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sorts.Sorts;
import sorts.Sorts.ISort;

public class SettingsPane extends JPanel {
	
	private final ActionListener START_AL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Window.startSorting(sortList.getSelectedValue()); // Swap if bugs happen.
			Window.setDelay((Double)delaySpinner.getValue()); // 					^
			stop.setEnabled(true);
			start.setEnabled(false);
		}
	};
	private final ActionListener STOP_AL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Window.pauseSorting();
			start.setEnabled(true);
			stop.setEnabled(false);
		}
	};
	private final ActionListener RND_AL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Window.setNewArray(Window.genRandomArr(elementAmt, Window.HEIGHT-20));
			if(Window.isSorting()) {
				Window.stopSorting();
				start.setEnabled(true);
				stop.setEnabled(false);
			} else {
				start.setEnabled(true);
			}
		}
	};
	
	private JButton rnd;
	private JButton start;
	private JButton stop;
	
	private JList<ISort> sortList;
	
	private JSpinner delaySpinner;
	private JSpinner elementSpinner;
	
	private JCheckBox showMax, showMin;
	
	private int elementAmt = 100;
	
	public SettingsPane() {
		setLayout(null);
		setPreferredSize(new Dimension(150, Window.HEIGHT));
		JLabel l = new JLabel("Sort Visualizer");
		l.setBounds(35, 5, 100, 25);
		add(l);
		JSeparator s = new JSeparator(JSeparator.HORIZONTAL);
		s.setBounds(0, 35, 150, 50);
		add(s);
		
		start = new JButton("Start");
		start.setMargin(new Insets(0, 0, 0, 0));
		start.setBounds(16, 55, 50, 25);
		start.setEnabled(false);
		stop = new JButton("Stop");
		stop.setMargin(new Insets(0, 0, 0, 0));
		stop.setBounds(71+12, 55, 50, 25);
		stop.setEnabled(false);
		rnd = new JButton("Random array");
		rnd.setBounds(4+12, 90, 117, 25);
		
		start.addActionListener(START_AL);
		stop.addActionListener(STOP_AL);
		rnd.addActionListener(RND_AL);
		
		sortList = new JList<>(Sorts.getAllSortingAlgos());
		sortList.setSelectedIndex(0);
		sortList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sortList.setBounds(4, 125, 142, 120);
		add(sortList);
		
		delaySpinner = new JSpinner(new SpinnerNumberModel(100, 0.01, 1000, 0.01));
		delaySpinner.setBounds(4, 250, 50, 25);
		delaySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Window.setDelay((double) delaySpinner.getValue());
			}
		});
		add(delaySpinner);
		JLabel delspl = new JLabel("Delay (ms)");
		delspl.setBounds(63, 250, 100, 25);
		add(delspl);
		
		elementSpinner = new JSpinner(new SpinnerNumberModel(100, 10, 640, 1)); // TODO: Fix >640 bug
		elementSpinner.setBounds(4, 280, 50, 25);
		elementSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				elementAmt = (int) elementSpinner.getValue();
			}
		});
		add(elementSpinner);
		JLabel elspl = new JLabel("Elements");
		elspl.setBounds(63, 280, 100, 25);
		add(elspl);
		
		
		showMax = new JCheckBox("Show max");
		showMax.setSelected(true);
		showMax.addActionListener((ev) -> Window.setShowMax(showMax.isSelected()));
		showMax.setBounds(1, 310, 100, 25);
		add(showMax);
		
		showMin = new JCheckBox("Show min");
		showMin.setSelected(true);
		showMin.setBounds(1, 335, 100, 25);
		showMin.addActionListener((ev) -> Window.setShowMin(showMin.isSelected()));
		add(showMin);
		
		add(start);
		add(stop);
		add(rnd);
	}
	
	public void finishedCallback() {
		STOP_AL.actionPerformed(null);
	}
}
