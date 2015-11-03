package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import crawler.URL;
import storage.CrawlerResult;

public class ExecutionGUI {
	private final static String GUI_NAME = "EXECUTION GUI";

	private JFrame frame;
	private JPanel panel;
	
	/**
	 * Launch the application
	 */
	public ExecutionGUI(){
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(GUI_NAME);
		frame.setBounds(100, 100, 550, 328);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(255, 0, 0), 2));
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.WHITE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		int v=ScrollPaneConstants. VERTICAL_SCROLLBAR_AS_NEEDED;
		int h=ScrollPaneConstants. HORIZONTAL_SCROLLBAR_AS_NEEDED;
		JScrollPane jsp = new JScrollPane(panel,v,h);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBorder(new MatteBorder(0, 0, 0, 2, (Color) new Color(255, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("URL");
		lblNewLabel.setForeground(new Color(0, 128, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JPanel panel_2 = new JPanel();
		panel_2.setForeground(Color.WHITE);
		panel_2.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(255, 0, 0)));
		panel_2.setBackground(Color.WHITE);
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Latency");
		lblNewLabel_1.setForeground(new Color(0, 128, 0));
		panel_2.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		jsp.setBorder(new EmptyBorder(20, 20, 20, 20));
		frame.getContentPane().add(jsp);
		frame.setVisible(true);
	}

	/**
	 * Display URL and Latency in the running time
	 */
	public void realTimeDisplay(CrawlerResult crawlerResult){
		String url = crawlerResult.url.path;
		double latency = crawlerResult.latency;
		String latencyText = Double.toString(latency);
		
		// URL
		JPanel urlPanel = new JPanel();
		urlPanel.setBorder(new MatteBorder(2, 0, 0, 2, (Color) new Color(255, 0, 0)));
		urlPanel.setBackground(new Color(255, 255, 255));
		urlPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel urlLabel = new JLabel(url);
		urlLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		urlLabel.setHorizontalAlignment(SwingConstants.CENTER);
		urlLabel.setVerticalAlignment(SwingConstants.CENTER);
		urlPanel.add(urlLabel);
		panel.add(urlPanel);
		
		// Latency
		JPanel latencyPanel = new JPanel();
		latencyPanel.setBorder(new MatteBorder(2, 0, 0, 0, (Color) new Color(255, 0, 0)));
		latencyPanel.setBackground(new Color(255, 255, 255));
		latencyPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel latencyLabel = new JLabel(latencyText);
		latencyLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		latencyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		latencyLabel.setVerticalAlignment(SwingConstants.CENTER);
		latencyPanel.add(latencyLabel);
		panel.add(latencyPanel);
	}
}
