package Classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import Classes.TileMap.Cell;

@SuppressWarnings("serial") // Eclipse was giving me "warnings" for not having this...
public class Grid extends JPanel implements MouseListener {
		
	public final int COLS;
	public final int ROWS;
	final int CELL_W;
	final int CELL_H;
	public Player player;
	public TileMap map;
	private boolean mouseEnabled;
	
	/**
	 * Constructor
	 * 
	 * @param cols	Number of columns
	 * @param rows	Number of rows
	 */
	public Grid(int cols, int rows){
		COLS = cols;
		ROWS = rows;
		CELL_W = this.getHeight()/COLS;
		CELL_H = this.getWidth()/ROWS;
		map = new TileMap(COLS,ROWS);
		player = new Player(this);
		mouseEnabled = true;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param g		Grid to copy
	 */
	public Grid(Grid g){
		COLS = g.COLS;
		ROWS = g.ROWS;	
		CELL_W = this.getWidth()/COLS;
		CELL_H = this.getWidth()/ROWS;	
		player = new Player(this);
		map.copy(g.map);
		mouseEnabled = true;
		
	}	
	
	
	/**
	 * Extends the constructor JPanel(cols,rows,x,y,w,h) and adds necessary initializations/instantiations for this class
	 * 
	 * @param cols
	 * @param rows
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public Grid(int cols, int rows, int x, int y, int w, int h) {
		super.setBounds(x,y,w,h);
		COLS = cols;
		ROWS = rows;
		CELL_W = w/COLS;
		CELL_H = h/ROWS;
		map = new TileMap(COLS,ROWS);
		player = new Player(this);
		mouseEnabled = true;
		scramble();			
	}
	
	/**
	 * Scrambles the TileMap values. This ensures that there is always a solution.
	 */
	public void scramble(){
		for(int x = 0;x<200;x++)
			map = player.move(map);
	}
	
	public void paintComponent(Graphics g){
		render(g);
	}
	
	/**
	 * Render method
	 * 
	 * @param g
	 */
	public void render(Graphics g){
		Image img=createImage(getWidth(),getHeight());
		Graphics offg=img.getGraphics();
		Graphics2D p=(Graphics2D)offg;		
		
		for(Cell[] cols: map.cells){
			for(Cell cell: cols){
				drawCell(cell,p);
			}
		}
		
		g.drawImage(img,0,0,this);
	}
	
	/**
	 * Method used to draw a Cell object on the grid
	 * 
	 * @param c
	 * @param g
	 */
	public void drawCell(Cell c,Graphics g){
		g.setColor(Color.BLACK);
		g.drawRect(c.col*CELL_W, c.row*CELL_H, CELL_W, CELL_H);
		g.setFont(new Font("TimesRoman", Font.BOLD, 22));
		if(c.value!=0){
			g.setColor(new Color(0,0,0,100));
			g.fillRect(c.col*CELL_W+10, c.row*CELL_H+10, CELL_W-20, CELL_H-20);

			g.setColor(Color.white);
			g.drawString(Integer.toString(c.value), c.col*CELL_W+CELL_W/2-5 , c.row*CELL_H+CELL_H/2);
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) { // Detects clicks and move cell when click event happens in its area.
		if(!mouseEnabled)
			return; // only allow click when enabled
		
		for(Cell[] cols: map.cells)
			for(Cell cell: cols){
				if(e.getX() > cell.col*CELL_W && e.getX() < cell.col*CELL_W+CELL_W 
						&& e.getY() > cell.row*CELL_H && e.getY()< cell.row*CELL_H+CELL_H){
					map = player.move(cell,map);					
					repaint();
				}
			}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return True if enabled
	 */
	public boolean isMouseEnabled() {
		return mouseEnabled;
	}

	/**
	 * @param mouseEnabled 	Value to set
	 */
	public void setMouseEnabled(boolean mouseEnabled) {
		this.mouseEnabled = mouseEnabled;
	}
}
