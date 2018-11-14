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

/* Decompiled by Mocha from ZapSpell.class */
/* Originally compiled from ZapSpell.java */

package law;

import java.awt.*;
import java.util.Enumeration;

class SubversionSpell extends Spell
{
    SubversionSpell(Wizard caster)
    {
    		// Subversion does have 100% casting chance, although it may seem unlikely. 
    		// Chance is in the magic resistance test.
    		super("Subversion",100,7,0,0,caster,"Brings a target creature under your control.",1);
    }

		public boolean validTarget(Square pos)
		{
			if(! super.validTarget(pos)) return false;
			Creature target = pos.getOccupant();
    	if(target == null) 
    	{
    		caster.getGame().getFrame().getMessageArea().setText("No Target");
    		return false;
    	}
    	if(target instanceof Wizard)
    	{
    		caster.getGame().getFrame().getMessageArea().setText("Wizards cannot be subverted");
    		return false;
    	}
    	return true;
    }
			
    public boolean cast(Square pos)
    {		    		
    	  if (super.cast(pos))
        {
	        Creature target = pos.getOccupant();
	    		if (caster.getGame().getDice().d(10) >= target.getMagRes())
	    		{
	    			caster.getGame().getFrame().getMessageArea().setText("Creature Subverted");
	    			target.setLeader(caster);
	    		}
	        return true;
	      }
	      else return false;
    }
		
		/*
    public void infoPaint(Graphics g)
    {
        Font font = new Font("Ariel", 1, 24);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString("Subversion", 300 - fontMetrics.stringWidth("Subversion") / 2, 50);
        String string = "( Neutral )";
        g.drawString(string, 300 - fontMetrics.stringWidth(string) / 2, 80);
        g.drawString("Spell Range 7", 5, 360);
        g.drawString("Casting Chance 70%", 5, 390);
    }
    */
}
