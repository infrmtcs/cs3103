package gui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.Font;

import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JApplet;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import controller.Controller;

public class GUI extends JFrame{
	
	private final static String CRAWLER_NAME = "PREPOCHECKER";
	private final static String EMPTY_SEARCH_STRING = "Please input a key word";
	private final static String EMPTY_STRING = "";
	
	private JFrame frame;
	private JTextField textField;
	private JButton btnSearch;
	private JPanel panel;
	private JPanel prepocheckerLayout;
	private JPanel panel_1;
	private JLabel result;
	private JPanel resultPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		prepocheckerLayout = new JPanel();
		prepocheckerLayout.setBorder(new EmptyBorder(20, 20, 20, 20));
		frame.getContentPane().add(prepocheckerLayout, BorderLayout.NORTH);
		prepocheckerLayout.setLayout(new BoxLayout(prepocheckerLayout, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		prepocheckerLayout.add(panel);
		panel.setBorder(new EmptyBorder(10, 10, 15, 10));
		
		JLabel lblPrepochecker = new JLabel(CRAWLER_NAME);
		panel.add(lblPrepochecker);
		lblPrepochecker.setFont(new Font(".VnClarendonH", Font.PLAIN, 20));
		lblPrepochecker.setForeground(Color.BLUE);
		
		textField = new JTextField();
		prepocheckerLayout.add(textField);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(15, 10, 10, 10));
		prepocheckerLayout.add(panel_1);
		
		btnSearch = new JButton("Prepochecker Search");
		panel_1.add(btnSearch);
		
		resultPanel = new JPanel();
		prepocheckerLayout.add(resultPanel);
		
		result = new JLabel("");
		result.setForeground(Color.RED);
		resultPanel.add(result);
		
		/**
		 * Adjust the result and warning
		 */
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField.getText().isEmpty()){
					result.setText(EMPTY_SEARCH_STRING);
				}
				else{
					//Controller controller = new Controller(textField.getText());
					String output = "default";
					result.setText("RESULT: " + output);
				}
			}
		});
	}
}
