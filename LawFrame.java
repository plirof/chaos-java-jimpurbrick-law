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

//----------------------------------------------------------
//	LawFrame
//	
//	Project: Law
//	Author: Jim Purbrick
//	Last Revision: 12/11/97
//

/*	The law frame is a container for the things that 
 *	stay contant over the lifetime of the process - the
 *	align-o-meter, logo and message bar. It allows 
 *	unrelated parts of the program to be treated separately
 *	i.e. the player entry screen no longer has to be contained
 *	by the Chaos class.
 */

package law;

import java.awt.*;
import java.util.*;

class LawFrame extends Frame implements Data
{
	TextField messageArea;
	ResourceLoader loader;
	AlignOMeter alignOMeter;
	Vector threads; // Use a Vector instead of a ThreadGroup so browsers don't get stroppy.
	
	LawFrame(ResourceLoader l)
	{
		super("Law");
		
		setBackground(BACKGROUND);
		setForeground(FOREGROUND);
		
		threads = new Vector();
		loader = l;
		
		Image pentagram = loader.getImage("pentagram.gif");
		Image scales = loader.getImage("scales.gif");
		Image world = loader.getImage("world.gif");
		Image chain = loader.getImage("chainlink.gif");
		Image logo = loader.getImage("logo.gif");
		
		Panel topPanel = new Panel();
		alignOMeter = new AlignOMeter(world, pentagram, scales, chain);
		topPanel.add(alignOMeter);
		topPanel.add(new PicturePanel(logo,80,20));
		messageArea = new TextField(30);
		messageArea.setEditable(false);
		messageArea.setBackground(BACKGROUND);
		messageArea.setForeground(FOREGROUND);
		topPanel.add(messageArea);
		add("North",topPanel);
	}
	
	public ResourceLoader getLoader()
	{
		return loader;
	}
	
	public AlignOMeter getAlignOMeter()
	{
		return alignOMeter;
	}
	
	public TextField getMessageArea()
	{
		return messageArea;
	}
	
	public void addThread(Threadable t)
	{
		threads.addElement(t);
	}
	
  public boolean handleEvent(Event evt)
	{
  	if(evt.id == Event.WINDOW_DESTROY)
  	{
  		
  			stopThreads();	// Stop all threads.
  			removeAll();		// Ditch all components.
  			dispose(); 			// Free window resources.
  			loader.exit();	// Call exit if this was an application.
  			return true;
  	}
  	// Call the orignal handle event to farm the 
  	// non-window messages out to the specific handlers.
  	return super.handleEvent(evt);
  }
  
  public void stopThreads()
  {
  	int liveCount = 0;
  	Threadable t;
  	System.out.println("LawFrame: Testing " + threads.size() + " thread(s).");
  	for(Enumeration e = threads.elements(); e.hasMoreElements();)
  	{
  			t = (Threadable) e.nextElement();
  			liveCount++;
  			t.stop();  			
  	}
  	System.out.println("LawFrame: Stopped " + liveCount + " thread(s).");
  	threads.setSize(0);
  }
}

		
		
