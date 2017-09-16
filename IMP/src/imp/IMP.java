/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 * Modified by Kevin Hainsworth
 */

package imp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;

class IMP implements MouseListener{
   JFrame frame;
   JPanel mp;//,redPanel,greenPanel,bluePanel;
   MyPanel redPanel,greenPanel,bluePanel;
   JButton start;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   int rotated = 0;
   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0, savH = 0, savW = 0;
   
   //your 2D array of pixels
    int picture[][];
    int savedPic[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
              public void windowClosing(WindowEvent ev){quit();}
            });
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ handleOpen(); }
           });
      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ reset(); }
           });     
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ quit(); }
           });
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(false);
      start.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ fun2(); }
           });
      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);
   }
   
   /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu fun = new JMenu("Functions");
     JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
     JMenuItem secondItem = new JMenuItem("Rotate 90 Degrees");
     JMenuItem thirdItem = new JMenuItem("Histogram");
     JMenuItem fourthItem = new JMenuItem("Grayscale");
     JMenuItem fifthItem = new JMenuItem("3x3 Mask");
     JMenuItem sixthItem = new JMenuItem("Track");
    
     firstItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){fun1();}
           });
        
     secondItem.addActionListener(new ActionListener(){
         @Override
       public void actionPerformed(ActionEvent evt){rotate();}
        });
     
     thirdItem.addActionListener(new ActionListener(){
         @Override
       public void actionPerformed(ActionEvent evt){hist();}
        });
     
     fourthItem.addActionListener(new ActionListener(){
         @Override
       public void actionPerformed(ActionEvent evt){grayscale();}
        });
     
     fifthItem.addActionListener(new ActionListener(){
         @Override
       public void actionPerformed(ActionEvent evt){mask();}
        });
     
     sixthItem.addActionListener(new ActionListener(){
         @Override
       public void actionPerformed(ActionEvent evt){track();}
        });
      //fun.add(firstItem);
      
       
      
      fun.add(firstItem);
      fun.add(secondItem);
      fun.add(thirdItem);
      fun.add(fourthItem);
      fun.add(fifthItem);
      fun.add(sixthItem);
      
      return fun;   

  }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen()
  {  
     img = new ImageIcon();
     JFileChooser chooser = new JFileChooser();
     int option = chooser.showOpenDialog(frame);
     if(option == JFileChooser.APPROVE_OPTION) {
       pic = chooser.getSelectedFile();
       img = new ImageIcon(pic.getPath());
      }
     width = img.getIconWidth();
     height = img.getIconHeight(); 
     savW = width;
     savH = height;
     
     JLabel label = new JLabel(img);
     label.addMouseListener(this);
     pixels = new int[width*height];
     
     results = new int[width*height];
  
          
     Image image = img.getImage();
        
     PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     try{
         pg.grabPixels();
     }catch(InterruptedException e)
       {
          System.err.println("Interrupted waiting for pixels");
          return;
       }
     for(int i = 0; i<width*height; i++)
        results[i] = pixels[i];  
     turnTwoDimensional();
     mp.removeAll();
     mp.repaint();
     mp.add(label);
     
     mp.revalidate();
  }
  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          picture[i][j] = pixels[i*width+j];
     
     savedPic = picture;
      
     
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
	  
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
        /*if(rotated%2 == 1){
        	int temp = width;
        	width = height;
        	height = temp;
        }*/
        width = savW;
        height = savH;
        picture = savedPic;
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 
       
      JLabel label2 = new JLabel(new ImageIcon(img2)); 
      label2.addMouseListener(this);
       mp.removeAll();
       mp.repaint();
       mp.add(label2);
     
       mp.revalidate(); 
    }
  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          pixels[i*width+j] = picture[i][j];
      Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
      label2.addMouseListener(this);
       mp.removeAll();
       mp.repaint();
       mp.add(label2);
     
       mp.revalidate(); 
   
    }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will 
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
    */
  private void fun1()
  {
     
    for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[i][j]);
         
        
           rgbArray[1] = 0;
           //take three ints for R, G, B and put them back into a single int
           picture[i][j] = getPixels(rgbArray);
        } 
     resetPicture();
  }

  /*
   * fun2
   * This is where you will write your STACK
   * All the pixels are in picture[][]
   * Look at above fun1() to see how to get the RGB out of the int (getPixelArray)
   * and then put the RGB back to an int (getPixels)
   */  
  private void fun2()
  {
	  redPanel.drawHistogram();
	  greenPanel.drawHistogram();
	  bluePanel.drawHistogram();
  }
  //Currently flips image instead of rotate. also resetPicture doesnt work with it
  private void rotate(){
	 int[][] rotate = new int[width][height];
	 
	 for(int i = 0; i < height; i++){
		 for(int j = 0; j < width; j++){
			 //System.out.println("" + i + " "+ j);
			 rotate[(width-1)-j][i] = picture[i][j];
		 }
	 }
	 int temp = width;
	 width = height;
	 height = temp;
	 picture = rotate;
	 rotated++;
	 resetPicture();
  }
  
  private void hist(){
	  int rgbArray[] = new int[4];
	  int red[] = new int[256];
	  int green[] = new int[256];
	  int blue[] = new int[256];

	  for(int i = 0; i < height;i++){
		  for(int j = 0; j<width; j++){
	    		  //get three ints for R, G and B
			  rgbArray = getPixelArray(picture[i][j]);
			  red[rgbArray[1]]++;
			  green[rgbArray[2]]++;
			  blue[rgbArray[3]]++;
		  }
	  }
	  
	  float[] redEq = new float[256];
	  float[] greenEq = new float[256];
	  float[] blueEq = new float[256];
	  
	  int sum1 = 0;
	  int sum2 = 0;
	  int sum3 = 0;
	  for(int i = 0; i<255; i++){
		  sum1 += red[i];
		  redEq[i] = Math.round(sum1*255/pixels.length);
		  sum2 += green[i];
		  greenEq[i] = Math.round(sum2*255/pixels.length);
		  sum3 += blue[i];
		  blueEq[i] = Math.round(sum3*255/pixels.length);
	  }
	  
	  for(int i =0; i<height; i++){
		  for(int j=0; j<width; j++){
			  red[i] = (int)redEq[i];
			  green[i] = (int)greenEq[i];
			  blue[i] = (int)blueEq[i];
		  }
	  }
	  
	  JFrame redFrame = new JFrame("Red");
	  redFrame.setSize(305, 1000);
	  redFrame.setLocation(800, 0);
	  JFrame greenFrame = new JFrame("Green");
	  greenFrame.setSize(305, 1000);
	  greenFrame.setLocation(1150, 0);
	  JFrame blueFrame = new JFrame("blue");
	  blueFrame.setSize(305, 1000);
	  blueFrame.setLocation(1450, 0);
	  redPanel = new MyPanel(red);
	  greenPanel = new MyPanel(green);
	  bluePanel = new MyPanel(blue);
	  redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
	  redFrame.setVisible(true);
	  greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
	  greenFrame.setVisible(true);
	  blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
	  blueFrame.setVisible(true);
	  start.setEnabled(true);
	  
	  
  }
 
  private void grayscale(){
	  int rgbArray[] = new int[4];
      for(int i = 0; i < height;i++){
    	  for(int j = 0; j<width; j++){
    		  //get three ints for R, G and B
    		  rgbArray = getPixelArray(picture[i][j]);
    		  int avg = (int) ((int) (rgbArray[1]*0.21) + (rgbArray[2]*.72) + (rgbArray[3]*.07));
    		  rgbArray[1] = avg;
    		  rgbArray[2] = avg;
    		  rgbArray[3] = avg;
    		  picture[i][j] = getPixels(rgbArray);
    	  }
      }
      resetPicture();
     
  }

  private void mask(){
	  //grayscale();
	  int rgbArray[] = new int[4];
	  int rgbArray1[] = new int[4];
	  int rgbArray2[] = new int[4];
	  int rgbArray3[] = new int[4];
	  int rgbArray4[] = new int[4];
	  int rgbArray5[] = new int[4];
	  int rgbArray6[] = new int[4];
	  int rgbArray7[] = new int[4];
	  int rgbArray8[] = new int[4];
	  //turn to grayscale
	  for(int i = 0; i < height;i++){
    	  for(int j = 0; j<width; j++){
    		  //get three ints for R, G and B
    		  rgbArray = getPixelArray(picture[i][j]);
    		  int avg = (int) ((int) (rgbArray[1]*0.21) + (rgbArray[2]*.72) + (rgbArray[3]*.07));
    		  rgbArray[1] = avg;
    		  rgbArray[2] = avg;
    		  rgbArray[3] = avg;
    		  picture[i][j] = getPixels(rgbArray);
    	  }
      }
	  int[][] newImage = new int[height][width];
	  for(int i = 1; i<height-1; i++){
		  for(int j = 1; j < width-1; j++){
			  rgbArray = getPixelArray(picture[i][j]);
			  rgbArray1 = getPixelArray(picture[i-1][j-1]);
			  rgbArray2 = getPixelArray(picture[i-1][j]);
			  rgbArray3 = getPixelArray(picture[i-1][j+1]);
			  rgbArray4 = getPixelArray(picture[i][j-1]);
			  rgbArray5 = getPixelArray(picture[i][j+1]);
			  rgbArray6 = getPixelArray(picture[i+1][j-1]);
			  rgbArray7 = getPixelArray(picture[i+1][j]);
			  rgbArray8 = getPixelArray(picture[i+1][j+1]);

			  int temp = rgbArray1[1]+rgbArray2[1]+rgbArray3[1]+rgbArray4[1]+(rgbArray[1]*-8)+rgbArray5[1]+rgbArray6[1]+rgbArray7[1]+rgbArray8[1];
			  if(temp>255){
				  temp = 255;
			  }
			  else if(temp < 0){
				  temp = 0;
			  }
			  rgbArray[1] = temp;
			  rgbArray[2] = temp;
			  rgbArray[3] = temp;
			  newImage[i][j] = getPixels(rgbArray);
			  
		  }
	  }
	  picture = newImage;
	  resetPicture();
  }
  
  private void track(){
	  int rgbArray[] = new int[4];
	  int pix = picture[colorY][colorX];
      rgbArray = getPixelArray(pix);
      int upperRed = rgbArray[1] + 20;
      int lowerRed = rgbArray[1] - 20;
      int upperGreen = rgbArray[2] + 25;
      int lowerGreen = rgbArray[2] - 25;
      int upperBlue = rgbArray[3] + 20;
      int lowerBlue = rgbArray[3] - 20;
      
	  
	  for(int i = 0; i<height;i++){
		  for(int j = 0; j<width; j++){
			  rgbArray = getPixelArray(picture[i][j]);
			  if(rgbArray[1] > lowerRed && rgbArray[1] < upperRed){
				  if(rgbArray[2] > lowerGreen && rgbArray[2] < upperGreen){
					  if(rgbArray[3] > lowerBlue && rgbArray[3] < upperBlue){
						  rgbArray[1] = 255;
						  rgbArray[2] = 255;
						  rgbArray[3] = 255;
						  picture[i][j] = getPixels(rgbArray);
					  }
					  else{
						  rgbArray[1] = 0;
						  rgbArray[2] = 0;
						  rgbArray[3] = 0;
						  picture[i][j] = getPixels(rgbArray);
					  }
				  }
				  else{
					  rgbArray[1] = 0;
					  rgbArray[2] = 0;
					  rgbArray[3] = 0;
					  picture[i][j] = getPixels(rgbArray);
				  }
			  }
			  else{
				  rgbArray[1] = 0;
				  rgbArray[2] = 0;
				  rgbArray[3] = 0;
				  picture[i][j] = getPixels(rgbArray);
			  }
		  }
	  }
	  resetPicture();
	  
	  
  }
 
  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
 
}