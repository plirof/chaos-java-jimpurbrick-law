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

//-----------------------------------------------------------------------------
//	AlignOMeter
//
//	Project: Law
//	Author:  Jim Purbrick (jcp95c@cs.nott.ac.uk)
//	Last Revision: 2/12/97
//
//	Combines the GUI and game facilities for keeping track of and displaying
//	the world's alignment. Crikey - GUI and functionality, it could almost be a
//	bean.
//-----------------------------------------------------------------------------

package law;

import java.awt.*;

class AlignOMeter extends Canvas
{
	private static int worldAlign;
  private static int tempWorldAlign;
  private static final int CENTERING_FACTOR = 10;
  private static int cheatSpell;
	private Image world;
	private Image pentagram;
	private Image scales;
	private Image chain;
	
	AlignOMeter(Image w, Image p, Image s, Image c)
	{
		MediaTracker it = new MediaTracker(this);
		world = w;
		pentagram = p;
		scales = s;
		chain = c;
		it.addImage(w,0);
		it.addImage(p,0);
		it.addImage(s,0);
		it.addImage(c,0);
		try
		{
			it.waitForAll();
		}
		catch(InterruptedException e){;}
	}
	
	public void paint(Graphics g)
	{
		int counter;
		int value = 115 + worldAlign;
		
		g.drawImage(pentagram,0,1,this);
		g.drawImage(scales,230,1,this);
		g.setColor(Color.gray);
		g.drawRect(24,0,202,21);
		g.setColor(Color.black);
		g.clipRect(26,1,199,20);
		g.drawImage(world,value,1,this);
		for(counter = 1; counter < 13; counter++)
		{
			g.drawImage(chain, value - counter * 18,6,this);
		}
		for(counter = 0; counter < 13; counter++)
		{
			g.drawImage(chain, value + 20 + counter * 18,6,this);
		}
	}
	
	public Dimension minimumSize()
	{
		return new Dimension(250,22);
	}
	
	public Dimension preferredSize()
	{
		return minimumSize();
	}
	
	int getAlignment()
  {
      return worldAlign;
  }
  
  void resetAlignment()
  {
  		worldAlign = 0;
  		
  		// Update the GUI.
      repaint();
  }
  
  void adjustAlignment(int i)
  {
      tempWorldAlign += i;
  }
    
  void commitAlignment()
  {
      if (tempWorldAlign != 0) worldAlign += tempWorldAlign;
      else if (worldAlign >= 10) worldAlign -= 10;
      else if (worldAlign <= -10) worldAlign += 10;
      if (worldAlign > 100) worldAlign = 100;
      if (worldAlign < -100) worldAlign = -100;
      tempWorldAlign = 0;
      
      // Update the GUI.
      repaint();
  }
}
