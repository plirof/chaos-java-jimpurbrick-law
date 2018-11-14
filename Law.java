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
import java.io.*;
import java.net.*;

public class Law implements ResourceLoader
{
	LawFrame frame;
	CommandLineParser argParser;
	
	public Law(String[] args)
	{
		argParser = new CommandLineParser(args);
		Panel menu = new ApplicationMenuScreen(frame);
		frame.add("Center",menu);
		frame.pack();
		frame.show();
	}
	
	public static void main(String[] args)
	{
		Law l = new Law(args);
	}
	
	public Image getImage(String name)
	{
		String fileName = System.getProperty("user.dir") + File.separator + "images" + File.separator + name;
		System.out.println("Law: Loading image: " + fileName);
		return Toolkit.getDefaultToolkit().getImage(fileName);
	}
	
	public void exit()
	{
		System.exit(0);
	}
	
	// Used to get the appropriate menu panel for this loader.
	public Panel getMenu()
	{
		return new ApplicationMenuScreen(frame);
	}

	public String getProperty(String name)
	{
	  String parameter;

	  // Command line parameters override environment parameters.
	  if((parameter = argParser.getArgumentOption(name)) == null)
	  {
	    parameter = System.getProperty(name);
	  }

	  return parameter;
	}
}
