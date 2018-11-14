// Law, a multi-player strategy game written as a Java applet based 
// on the classic ZX Spectrum game Chaos by Julian Gallop.
// Copyright (C) 1997-2002 Jim Purbrick
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package law;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The thread that manages animation by painting all objects on the
 * battlefield and managing a graphic effect queue.
 */

class BattleFieldAnimator extends Canvas implements Data, Threadable
{
		private static int threadID = 0;
    private Image offScreenImage;
    private Graphics offScreen;
    private Vector gfx = new Vector(10);
    private Thread animationThread;
    private Dimension imageSize;
    private GameBoard gameBoard;

    public BattleFieldAnimator(Container parent, GameBoard g)
    {
    		parent.add(this);
        offScreenImage = createImage(NOSQUARESWIDE * SQUAREWIDTH, NOSQUARESHIGH * SQUAREHEIGHT);
        imageSize = new Dimension(offScreenImage.getWidth(this), offScreenImage.getHeight(this));
				offScreen = offScreenImage.getGraphics();
				offScreen.setColor(Color.black);
				offScreen.fillRect(0,0,imageSize.width, imageSize.height);
				gameBoard = g;
    }

    public void run()
    {
        while (true)
        {
        
        	try
        	{
        	        	
		        // Draw all the creatures.
		        gameBoard.updateAndPaint(offScreen);
		        
		        // Draw three frames of effects.
	          for(int i = 0; i < 3; i++)
	          {
	            // If there are any effects, paint the first one.
			        if (gfx.size() > 0)
			        {
			          GFX gFX = (GFX) gfx.firstElement();
			          gFX.update();
			           
			   	      if(gFX.finished())
			          {
			             gfx.removeElement(gFX);
			             gFX.die(offScreen);
				     // System.out.println("BattleFieldAnimator: " + gfx.size() + " effects queued.");
			          }
			          else gFX.paint(offScreen);
			        }
	            // repaint();
	            // Point imageOffset = new Point((size().width - offScreenImage.getWidth(this)) / 2, (size().height - offScreenImage.getHeight(this)) / 2);
	            // getGraphics().drawImage(offScreenImage,imageOffset.x, (size().height - offScreenImage.getHeight(this)) / 2,this);
	            paint(getGraphics());
	            pause(90); // 70 is optimum, but 100 miliseconds is standard pause between repaints.
	            
	          } // End effects loop.
	                     
	        } // End try block.
	        catch (Exception e)
	        {
	        	// Print out diagnostics if any errors occur, but don't pass them on to the system as it freezes animation.
	        	System.out.println("BattlefieldAnimator: Exception in run method.");
	        	e.printStackTrace();
	        }
	        
	      } // End creature loop.
    }
    
    public void queEffect(GFX gFX)
    {
    		gfx.addElement(gFX);
                //System.out.println("BattleFieldAnimator: " + gfx.size() + " effects queued.");
    }

    public Graphics getBufferGraphics()
    {
    		return offScreenImage.getGraphics();
    }

    private void pause(int i)
    {
        try
        {
            Thread.sleep((long)i);
        }
        catch (InterruptedException e)
        {
        }
        return;
    }
  
  public void start()
	{
		if(animationThread == null)
		{
			animationThread = new Thread(this,"Battle Field Animator" + (++threadID));
			// animationThread = new Thread(group, this);
			// animationThread.setDaemon(true);
			animationThread.start();
		}
	}
	
	public void stop()
	{
		if(animationThread != null && animationThread.isAlive())
		{
			animationThread.stop();
		}
		animationThread = null;
	}

	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		// As this is called with getGraphics(), g might be null.
		if(g == null) 
		{
			System.out.println("BattleFieldAnimator: null graphics passed to paint call.");
			return;
		}
		
		Point imageOffset = new Point((size().width - offScreenImage.getWidth(this)) / 2, (size().height - offScreenImage.getHeight(this)) / 2);
		g.drawImage(offScreenImage,imageOffset.x, (size().height - offScreenImage.getHeight(this)) / 2,this);
		
		/*
		// Paint rectangles around borders clean up when resizing.
		g.setColor(Color.green);
		g.fillRect(0,0,imageOffset.x - 1,size().height);
		g.setColor(Color.red);
		g.fillRect(imageOffset.x,0,size().width,imageOffset.y - 1);
		g.setColor(Color.yellow);
		g.fillRect(imageOffset.x + imageSize.width + 2,imageOffset.y,imageOffset.x,size().height);
		g.setColor(Color.blue);
		g.fillRect(imageOffset.x,imageOffset.y + imageSize.height,size().width + 2,size().height);
		*/
		
		g.setColor(Color.gray);
		g.drawRect(imageOffset.x - 1, imageOffset.y - 1,imageSize.width + 2,imageSize.height + 2);
	}
	
	public Point getImageOffset()
	{
		return new Point((size().width - offScreenImage.getWidth(this)) / 2, (size().height - offScreenImage.getHeight(this)) / 2);
	}
	
	public Dimension prefferredSize()
	{
		return new Dimension(imageSize.width + 2, imageSize.height + 2);
	}	
		
	/*
	public void resize(int w, int h)
	{
		super.resize(w,h);
		imageOffset.x = (size().width - offScreenImage.getWidth(this)) / 2;
		imageOffset.y = (size().height - offScreenImage.getHeight(this)) / 2;
		
		g.setColor(Color.gray);
		g.drawRect(imageOffset.x - 1, imageOffset.y - 1,size().width < imageSize.width + 2? size().width : imageSize.width + 2, size().height <  imageSize.height + 2? size().height : imageSize.height + 2);
	}
	
	public void resize(Dimension d)
	{
		resize(d.width,d.height);
	}
	
	public void reshape(int x, int y, int w, int h)
	{
		super.reshape(x,y,w,h);
		imageOffset.x = (size().width - offScreenImage.getWidth(this)) / 2;
		imageOffset.y = (size().height - offScreenImage.getHeight(this)) / 2;
		g.setColor(Color.gray);
		g.drawRect(imageOffset.x - 1, imageOffset.y - 1,size().width < imageSize.width + 2? size().width : imageSize.width + 2, size().height <  imageSize.height + 2? size().height : imageSize.height + 2);
	}
	*/
	
	/*
	public void finalize()
	{
		dispose();
	}
	
	public void dispose()
	{
		// Stop the animation thread.
		stop();
		
		// Clean up the double buffered image.
		offScreen.dispose();
		offScreenImage.flush();
	}
	*/
	
}
