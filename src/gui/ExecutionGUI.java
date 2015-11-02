package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

import storage.CrawlerResult;

import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.Checkbox;

import javax.swing.JTextArea;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.SwingConstants;

import crawler.URL;

import java.awt.Font;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ExecutionGUI extends JFrame {
	private final static String GUI_NAME = "EXECUTION GUI";

	private JFrame frame;
	private JPanel panel;

	/**
	 * Create the application.
	 */
	public ExecutionGUI() {		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(GUI_NAME);
		frame.setBounds(100, 100, 519, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.WHITE);
		
		JLabel lblNewLabel = new JLabel("URL");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Latency");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("");
		panel.add(lblNewLabel_3);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		int v=ScrollPaneConstants. VERTICAL_SCROLLBAR_AS_NEEDED;
		int h=ScrollPaneConstants. HORIZONTAL_SCROLLBAR_AS_NEEDED;
		JScrollPane jsp = new JScrollPane(panel,v,h);
		
		jsp.setBorder(new EmptyBorder(20, 20, 20, 20));
		frame.getContentPane().add(jsp);
		frame.setVisible(true);
		
		for(int i = 0; i < 90; i++){
			CrawlerResult c = new CrawlerResult(new URL("http://fsdfsvdfdfdssssssssssssssssssssssssssssssaf"), "html", 10.1);
			realTimeDisplay(c);
		}
	}

	/**
	 * Display URL and Latency in the running time
	 */
	public void realTimeDisplay(CrawlerResult crawlerResult){
		String url = crawlerResult.url.path;
		double latency = crawlerResult.latency;
		String latencyText = Double.toString(latency);
		
		JLabel urlLabel = new JLabel(url);
		urlLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		urlLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(urlLabel);
		
		JLabel latencyLabel = new JLabel(latencyText);
		latencyLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		latencyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(latencyLabel);
		
		panel.add(urlLabel);
		panel.add(latencyLabel);
	}
}
