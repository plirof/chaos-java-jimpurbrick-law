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

import java.applet.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class LawApplet extends Applet implements ResourceLoader, Data
{
	
	LawFrame frame;
	// boolean destroyed;
	
	int port;
        InetAddress server = null;
	
	public Image getImage(String name)
	{
		return getImage(getCodeBase(),"images/" + name);
	}

	public void init()
	{
		setBackground(BACKGROUND);
	        Panel screen = null;
		frame = new LawFrame(this);		

		try
		{
			port = Integer.parseInt(getParameter("port"));
		        server = InetAddress.getByName(getCodeBase().getHost());
		}
		catch(NumberFormatException e) {;} // No port parameter.
	        catch(UnknownHostException e2)
		{
		   frame.getMessageArea().setText("Network error, started local game");
		}
		try
		{
		        screen = new PlayerEntryScreen(server,port,frame,null,true);
                }
		catch(IOException e2) // Should never happen. 
		{
		  System.out.println("LawApplet: Exception creating PlayerEntryScreen: " + e2);
		  exit();
		}
		frame.add("Center",screen);
		frame.pack();
		frame.show();
	}
	
	public void exit()
	{
		destroy();
	}
	
	public Panel getMenu()
	{	
	        Panel screen = null;
		try
		{
			port = Integer.parseInt(getParameter("port"));
		        server = InetAddress.getByName(getCodeBase().getHost());
		}
		catch(NumberFormatException e) {;} // No port parameter.
	        catch(UnknownHostException e2)
		{
		   frame.getMessageArea().setText("Network error, started local game");
		}
		try
		{
		        screen = new PlayerEntryScreen(server,port,frame,null,true);
                }
		catch(IOException e2) // Should never happen. 
		{
		  System.out.println("LawApplet: Exception creating PlayerEntryScreen: " + e2);
		  exit();
		}
		return screen;
	}

	public String getProperty(String name)
	{
	  return getParameter(name);
	}
}
