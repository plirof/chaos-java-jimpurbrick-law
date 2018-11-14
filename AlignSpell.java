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

/**
 * Represents a spell that changes the world alignment when cast.
 */
public class AlignSpell extends Spell
{
    private int factor;
    private int align;

    AlignSpell(int index, Wizard caster)
    {
    		super(Data.alignString[(index%2==1?-1:1)+1]+"-"+(index%2+1),120-20*(index%2+1),0,(index%2==1?-1:1),(index%2+1),caster,"Alters the world's alignment.",1);
    }

    public boolean cast(Square pos)
    {
        if(super.cast(pos))
        {
        	// caster.removeSelectedSpell();
        }
        return true;        
    }

		/*
    public void infoPaint(Graphics g)
    {
        Font font = new Font("Ariel", 1, 24);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString(name, 300 - fontMetrics.stringWidth(name) / 2, 50);
        String string = "( " + Data.alignString[align + 1] + " " + factor * 2 + " )";
        g.drawString(string, 300 - fontMetrics.stringWidth(string) / 2, 80);
        g.drawString("Spell Range 0", 5, 360);
        g.drawString("Casting Chance " + (120 - 20 * factor) + "%", 5, 390);
    }
    */
}
