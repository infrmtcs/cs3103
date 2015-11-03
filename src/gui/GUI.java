package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.Controller;
import controller.QueryResult;

public class GUI {
	private final static String CRAWLER_NAME = "PREPOCHECKER";
	private final static String EMPTY_SEARCH_STRING = "Please input a key word";
	
	private JFrame frame;
	private JTextField textField;
	private JButton btnSearch;
	private JPanel panel;
	private JPanel prepocheckerLayout;
	private JPanel panel_1;
	
	static ExecutionGUI executionGUI;
	static GUI window;
	private JPanel resultPanel;
	private JLabel result;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					window = new GUI();
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
		frame.setBounds(100, 100, 501, 330);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		prepocheckerLayout = new JPanel();
		prepocheckerLayout.setBounds(0, 0, 485, 240);
		prepocheckerLayout.setBorder(new EmptyBorder(20, 40, 20, 40));
		frame.getContentPane().add(prepocheckerLayout);
		prepocheckerLayout.setLayout(new BoxLayout(prepocheckerLayout, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		prepocheckerLayout.add(panel);
		panel.setBorder(new EmptyBorder(30, 20, 20, 20));
		
		JLabel lblPrepochecker = new JLabel(CRAWLER_NAME);
		lblPrepochecker.setToolTipText("Prepochecker");
		panel.add(lblPrepochecker);
		lblPrepochecker.setFont(new Font(".VnClarendonH", Font.PLAIN, 35));
		lblPrepochecker.setForeground(Color.BLUE);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setToolTipText("Search");
		
		// Create the border for textField
		textField.setBorder(new LineBorder(new Color(60, 179, 113), 2));
		prepocheckerLayout.add(textField);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(15, 20, 20, 20));
		prepocheckerLayout.add(panel_1);
		
		btnSearch = new JButton("Prepochecker Search");
		btnSearch.setToolTipText("Search button");
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_1.add(btnSearch);
		
		resultPanel = new JPanel();
		resultPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		prepocheckerLayout.add(resultPanel);
		
		result = new JLabel();
		result.setToolTipText("Result");
		result.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		result.setForeground(new Color(220, 20, 60));
		resultPanel.add(result);
		
		/**
		 * Adjust the result and warning
		 */
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			    String input = textField.getText();
			    
				if (input.isEmpty()) {
					result.setText(EMPTY_SEARCH_STRING);
				} else {
					executionGUI = new ExecutionGUI();
					Controller controller = new Controller(executionGUI);
					String output = controller.query(input);
					result.setText(output);
				}
			}
		});
		
		frame.setVisible(true);
	}
}
