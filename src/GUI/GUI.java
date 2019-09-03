package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Classes.Grid;
import Classes.Node;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private JPanel contentPane;

	public static Node bfs;
	public static Node aStar;
	public final int DIMS = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 471, 585);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Adding the Grid
		Grid panel = new Grid(DIMS,DIMS,12, 13, 428, 389);
		panel.setBackground(Color.WHITE);
						
		contentPane.add(panel);
		contentPane.addMouseListener(panel);
				
		JButton btnScramble = new JButton("Scramble");
		btnScramble.setBounds(12, 438, 97, 25);
		contentPane.add(btnScramble);
		
		JLabel lblNodeCount = new JLabel("A* Node Count:   0");
		lblNodeCount.setBounds(138, 438, 290, 16);
		contentPane.add(lblNodeCount);
		
		
		JButton btnSolve = new JButton("Solve");
		btnSolve.setBounds(12, 468, 97, 25);
		contentPane.add(btnSolve);
		
		JLabel lblPathCount = new JLabel("A* Path Count:   0");
		lblPathCount.setBounds(138, 456, 173, 16);
		contentPane.add(lblPathCount);
		
		JButton btnShowSolution = new JButton("A* Solution");
		btnShowSolution.setBounds(325, 438, 115, 25);
		contentPane.add(btnShowSolution);
		
		JLabel lblThinking = new JLabel("Thinking ...");		
		lblThinking.setBounds(183, 467, 87, 16);
		contentPane.add(lblThinking);
		
		JLabel lblBfsNodeCount = new JLabel("BFS Node Count:");
		lblBfsNodeCount.setBounds(138, 477, 154, 16);
		contentPane.add(lblBfsNodeCount);
		
		JLabel lblBfsPathCount = new JLabel("BFS Path Count:");
		lblBfsPathCount.setBounds(138, 495, 153, 16);
		contentPane.add(lblBfsPathCount);
		
		JButton btnBfsSolution = new JButton("BFS Solution");
		btnBfsSolution.setBounds(323, 468, 117, 25);
		contentPane.add(btnBfsSolution);
		
		btnBfsSolution.setVisible(false);
		btnShowSolution.setVisible(false);
		lblThinking.setVisible(false);
		lblNodeCount.setVisible(false);
		lblPathCount.setVisible(false);	
		lblBfsNodeCount.setVisible(false);
		lblBfsPathCount.setVisible(false);
		btnScramble.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				panel.scramble();
				panel.repaint();
				lblNodeCount.setVisible(false);
				lblPathCount.setVisible(false);
				lblBfsNodeCount.setVisible(false);
				lblBfsPathCount.setVisible(false);
				btnShowSolution.setEnabled(false);
				btnBfsSolution.setEnabled(false);
			}
			
		});
		
		
		btnShowSolution.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg){
				new Thread(){
					public void run(){
						btnSolve.setEnabled(false);
						btnScramble.setEnabled(false);
						btnBfsSolution.setEnabled(false);
						btnShowSolution.setEnabled(false);
						panel.setMouseEnabled(false); // disable any clicks from user
						
						panel.player.showSolution(aStar);
						
						panel.setMouseEnabled(true);
						btnShowSolution.setEnabled(true);
						btnSolve.setEnabled(true);
						btnScramble.setEnabled(true);
						btnBfsSolution.setEnabled(true);
					}					
				}.start();
			}
		});	
		
		btnBfsSolution.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg){
				new Thread(){
					public void run(){
						btnSolve.setEnabled(false);
						btnScramble.setEnabled(false);
						btnShowSolution.setEnabled(false);
						btnBfsSolution.setEnabled(false);
						panel.setMouseEnabled(false); // disable any clicks from user
						
						panel.player.showSolution(bfs);
						
						panel.setMouseEnabled(true);
						btnBfsSolution.setEnabled(false);
						btnSolve.setEnabled(true);
						btnScramble.setEnabled(true);
						btnShowSolution.setEnabled(true);
					}					
				}.start();
			}
		});	
		
		btnSolve.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				new Thread(){
					public void run(){
						btnScramble.setEnabled(false);
						btnBfsSolution.setEnabled(false);
						lblThinking.setVisible(true);
						lblNodeCount.setVisible(false);
						lblPathCount.setVisible(false);
						panel.setMouseEnabled(false); // disable any clicks from user
						
						Node start = new Node(panel.map,null,0); // set the starting point to the current state
						aStar = panel.player.Astar(start,false);
						int aStarCount = panel.player.closedList.size();
						int aStarCost = panel.player.current.cost;
						lblNodeCount.setText("A* Node Count:   " + aStarCount);
						lblPathCount.setText("A* Path Count:   " + aStarCost);	
						bfs = panel.player.mySolution(start,true);
							
						lblBfsNodeCount.setText("BFS Node Count:   " + panel.player.closedList.size());
						lblBfsPathCount.setText("BFS Path Count:   " + panel.player.current.cost);	
						
						panel.setMouseEnabled(true);				
						btnShowSolution.setVisible(true);
						btnShowSolution.setEnabled(true);
						btnBfsSolution.setVisible(true);
						btnBfsSolution.setEnabled(true);
						lblThinking.setVisible(false);
						lblNodeCount.setVisible(true);
						lblPathCount.setVisible(true);
						lblBfsNodeCount.setVisible(true);
						lblBfsPathCount.setVisible(true);
						
						btnScramble.setEnabled(true);
					}
				}.start();				
			}			
		});
		
	}
}
