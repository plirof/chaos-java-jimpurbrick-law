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
import java.net.*;
import java.io.*;

/**
  * A panel only available in the application version of law that allows
  * the user to choose between running servers, connecting to servers,
  * or playing a local game.
  */
class ApplicationMenuScreen extends Panel implements Data
{
  private Panel buttonPanel;
  private Button host;
  private Button join;
  private Button local;
  private LawFrame frame;
  private static Server server = null; // OK to have this as static - only one can exist per computer, let alone virtual machine!
  
  ApplicationMenuScreen(LawFrame lf)
  { 	
  	frame = lf;
  	 	
  	// A grid bag centers the button panel but doesn't stretch it.
  	setLayout(new GridBagLayout());
  	
  	// Create and populate buttonbar.
  	buttonPanel = new Panel();
  	buttonPanel.setLayout(new GridLayout(0,1));
    host = new Button("Host network game");
    buttonPanel.add(host);    
    join = new Button("Join network game");
    buttonPanel.add(join);    
    local = new Button("Play local game");
    buttonPanel.add(local);
    
    // Add the button panel.
    add(buttonPanel);       
  }
  
  public boolean action(Event event, Object object)
  {
  	
    if(event.target == host)
    {
    	try
    	{
    		// Only ever want to create one server.
    		if(server == null) server = new Server();
    		PlayerEntryScreen screen = new PlayerEntryScreen(InetAddress.getLocalHost(),CHAOSPORT,frame,this,true);
    		frame.remove(this);
    		frame.add("Center", screen);
    		frame.pack();
    		frame.show();
     	}
     	catch(IOException e2) 
    	{
    		frame.getMessageArea().setText("Error starting local server");
    	}
    	finally
    	{
    		return true;
    	}
    }
    if(event.target == join)
    {
    	ServerChoiceDialog d = new ServerChoiceDialog(frame,this);
    	return true;
    }
    if(event.target == local)
    {
    	try
    	{
    		PlayerEntryScreen screen = new PlayerEntryScreen(null,0,frame,this,true);
    		frame.remove(this);
    		frame.add("Center", screen);
    		frame.pack();
    		frame.show();
    	}
    	catch(IOException e2) 
    	{
    		frame.getMessageArea().setText("Error starting local game");
    	}
    	finally
    	{
    		return true;
    	}
    }
    
    return false;
  }
  
  public Dimension preferredSize()
  {
      return minimumSize();
  }
    
  public Dimension minimumSize()
  {
  	return screenSize;        
  }
}

class ServerChoiceDialog extends Dialog implements Data
{
	private ApplicationMenuScreen menu;
	private Button connect;
  private TextField hostname;
  private LawFrame frame;
  private TextField port;

	ServerChoiceDialog(LawFrame lf, ApplicationMenuScreen m)
	{
		super(lf,"Law",true);
		menu = m;
		frame = lf;
    //setLayout(new BorderLayout(5,5));
    setLayout(new GridLayout(3,2,5,5));
    //setLayout(new FlowLayout());
    //add("Center", new ColourLabel("DNS name or IP address of server"));
    add(new ColourLabel("DNS name or IP address of server"));
    //Panel p = new Panel();
    add(hostname = new TextField(20));
    add(new ColourLabel("Port"));
    add(new Panel().add(port = new TextField(5)));
    port.setText(String.valueOf(CHAOSPORT));
    add(new Panel());
    add(new Panel().add(connect = new Button("Connect")));
    //add("South",p);
    //resize(420,100);
    pack();
    setResizable(false);
    show(); 
  }
  
  public boolean action(Event event, Object object)
  {
  	if(event.target == connect)
    {
    	String name = hostname.getText();	
    	try
    	{
    		hide();
    		dispose();
    		InetAddress address = InetAddress.getByName(name);
    		PlayerEntryScreen screen = new PlayerEntryScreen(address,Integer.parseInt(port.getText()),frame,menu,false);
    		frame.remove(menu);
    		frame.add("Center", screen);
    		frame.pack();
    	}
    	catch(NumberFormatException e)
    	{
    		frame.getMessageArea().setText("Port must be a number");
			}	
    	catch(UnknownHostException e1)
    	{
    		frame.getMessageArea().setText("Host " + name + " unknown");
    	}
    	catch(IOException e2) 
    	{
    		frame.getMessageArea().setText("Error connecting to " + name);
    	}
    	finally
    	{
    		return true;
    	}
    }
    return false;
  }
  
  public boolean handleEvent(Event evt)
	{
  	if(evt.id == Event.WINDOW_DESTROY)
  	{
  			dispose(); 			// Free window resources.
  			return true;
  	}
  	// Call the orignal handle event to farm the 
  	// non-window messages out to the specific handlers.
  	return super.handleEvent(evt);
  }
  
  public void finalise()
  {
  	dispose();
  }
}
  	
	
