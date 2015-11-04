package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.Controller;
import controller.QueryResult;

public class GUI {
	private final static String CRAWLER_NAME = "PREPOCHECKER";
	private final static String EMPTY_SEARCH_STRING = "Please input a key word";
	private final static String NO_RESULT_MSG = "No result matching with key word";

	private JFrame frame;
	private JTextField textField;
	private JButton btnSearch;
	private JPanel panel;
	private JPanel prepocheckerPanel;
	private JPanel panel_1;
	private JPanel resultPanel;
	private JLabel result1;

	static ExecutionGUI executionGUI;
	static GUI window;
	static Controller controller;
	private JLabel result2;
	private JLabel result3;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					window = new GUI();
					controller = new Controller();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(CRAWLER_NAME);
		frame.setBounds(100, 100, 608, 367);
		frame.setVisible(true);
		frame.getContentPane().setLayout(
				new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		/**
		 * Action when clicking the close button
		 */
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            System.exit(0);
		        }
		    }
		});

		prepocheckerPanel = new JPanel();
		prepocheckerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
		frame.getContentPane().add(prepocheckerPanel);
		prepocheckerPanel.setLayout(new BoxLayout(prepocheckerPanel,
				BoxLayout.Y_AXIS));

		panel = new JPanel();
		prepocheckerPanel.add(panel);
		panel.setBorder(new EmptyBorder(30, 20, 20, 20));

		JLabel lblPrepochecker = new JLabel(CRAWLER_NAME);
		lblPrepochecker.setToolTipText("Prepochecker");
		panel.add(lblPrepochecker);
		lblPrepochecker.setFont(new Font(".VnClarendonH", Font.PLAIN, 35));
		lblPrepochecker.setForeground(Color.BLUE);

		textField = new JTextField();
		prepocheckerPanel.add(textField);
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setToolTipText("Search");

		// Create the border for textField
		textField.setBorder(new LineBorder(new Color(60, 179, 113), 2));

		panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(15, 20, 10, 20));
		prepocheckerPanel.add(panel_1);

		btnSearch = new JButton("Prepochecker Search");
		btnSearch.setToolTipText("Search button");
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_1.add(btnSearch);

		resultPanel = new JPanel();
		resultPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		prepocheckerPanel.add(resultPanel);
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(5, 0, 5, 5));
		resultPanel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		result1 = new JLabel();
		result1.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(result1);
		result1.setToolTipText("Third best answer");
		result1.setFont(new Font("Tahoma", Font.BOLD, 14));
		result1.setForeground(new Color(220, 20, 60));

		panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(5, 0, 5, 5));
		resultPanel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		result2 = new JLabel("");
		result2.setToolTipText("Second best answer");
		result2.setForeground(new Color(0, 128, 0));
		result2.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(result2);

		panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(5, 0, 5, 5));
		resultPanel.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		result3 = new JLabel("");
		result3.setForeground(new Color(0, 191, 255));
		result3.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_4.add(result3);

		/**
		 * Adjust the result and warning
		 */
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = textField.getText();

				if (input.isEmpty()) {
					result1.setHorizontalAlignment(SwingConstants.CENTER);
					result1.setText(EMPTY_SEARCH_STRING);
				} else {
					executionGUI = new ExecutionGUI();
					QueryResult[] ansArr = controller
							.query(input, executionGUI);

					if (ansArr.length < 1) {
						result1.setHorizontalAlignment(SwingConstants.CENTER);
						result1.setText(NO_RESULT_MSG);
					} else {
						String output1 = String.format("1. %s - %.1f pts",
								ansArr[0].bestAnswer,
								ansArr[0].getScore() / 1000000);
						result1.setHorizontalAlignment(SwingConstants.LEFT);
						result1.setText(output1);

						String output2 = String.format("2. %s - %.1f pts",
								ansArr[1].bestAnswer,
								ansArr[1].getScore() / 1000000);
						result2.setText(output2);

						String output3 = String.format("3. %s - %.1f pts",
								ansArr[2].bestAnswer,
								ansArr[2].getScore() / 1000000);
						result3.setText(output3);
					}
				}
			}
		});	
	}	
}
