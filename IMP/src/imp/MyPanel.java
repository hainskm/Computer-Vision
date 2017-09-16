package imp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private int[] c;
	MyPanel(int[] color){
		c = color;
	}
	BufferedImage grid;
	 Graphics2D gc;
	///PaintComponent Method
	 public void paintComponent(Graphics g)
	    { 
	         super.paintComponent(g);  
	         Graphics2D g2 = (Graphics2D)g;
	         if(grid == null){
	            int w = this.getWidth();
	            int h = this.getHeight();
	            grid = (BufferedImage)(this.createImage(w,h));
	            gc = grid.createGraphics();
	            
	         }
	         g2.drawImage(grid, null, 0, 0);
	         
	    }
	 
	 public void drawHistogram(){
		 if(gc == null){
			 System.out.println("why");
		 }
		 for(int i = 0; i<c.length;i++){
			 gc.drawLine(i, grid.getHeight(), i, (grid.getHeight()-c[i]));
		 }
		 repaint();
	 }
}
