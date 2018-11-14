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

interface DialogListener
{
	public void handleDialog(String choice);
}

class YesNoDialog extends Dialog implements Data
{
	DialogListener listener;
	
	public YesNoDialog(String question, DialogListener d, Frame parent)
	{
		super(parent,"Law",true);
		listener = d;
    ColourLabel l = new ColourLabel(question);
    l.setForeground(FOREGROUND);
    setLayout(new BorderLayout(5,5));
    Panel p = new Panel();
    Button yes = new Button("Yes");
    Button no = new Button("No");
    p.add(yes);
    p.add(no);
    add("South",p);
    add("Center",l);
    
    // Position the dialog in the center of the parent window.
    Point parentLoc = getParent().location();
    Dimension parentSize = getParent().size();
    // System.err.println( "YesNoDialog: x:" + (parentLoc.x + (parentSize.width - 200) / 2) + " y:" + (parentLoc.y + (parentSize.height - 100) / 2) + " width:" + 200 + " height: " + 100);
    reshape(parentLoc.x + (parentSize.width - 250) / 2, parentLoc.y + (parentSize.height - 120) / 2,250,120);
    
    setResizable(false);
    show();         
  }
  
  public boolean action(Event e,Object arg)
  {
	System.out.println("YesNoDialog.action: Result " + ((Button) e.target).getLabel());
  	listener.handleDialog(((Button) e.target).getLabel());
	hide();
	dispose();
  	return true;
  }
  
  public void finalise()
  {
  	dispose();
  }
}
