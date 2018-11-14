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
import java.util.*;

/**
 * A spell that summons an AutoCreature (Gooey Blob, Castle, Magic wood etc.)
 * when cast.
 */
class AutoCreatureSpell extends Spell
{
    private static final String names[] = { "Gooey Blob", "Magic Fire", "Dark Citadel", "Magic Castle", "Shadow Wood", "Magic Wood", "Magic Wall" };
    private static final int ranges[] = { 6, 6, 8, 8, 6, 6, 6 };
    private static final int chances[] = { 60, 60, 50, 50, 60, 60, 80 };
    private static final int aligns[] = { 0, -1, -1, 1, -1, 1, 0 };
    private static final int factors[] = { 0, 1, 1, 1, 1, 1, 0 };
    private static final int nTargets[] = { 1, 1, 1, 1, 6, 6, 4 };
    private static final String info[] = {"Spreads across battlefield engulfing creatures.","Spreads across battlefield burning creatures.","Gives caster a building to hide in.","Gives caster a building to hide in.","Creates a forest of shadow trees to attack enemies. Line of sight is needed when placing and trees cannot be adjacent.","Creates a forest of magic trees. Leaving a wizard in a magic tree for a while will give him a new spell.","Creates a strong wall. All sections must be adjacent."};
    private int index;
    private int count;
    // private Square positions[];

    AutoCreatureSpell(int i, Wizard caster)
    {
    		super(names[i],chances[i],ranges[i],aligns[i],factors[i],caster,info[i],nTargets[i]);
        index = i;
        count = 0;
        // if (index == 4 || index == 5) positions = new Square[6];
    }

		public boolean validTarget(Square pos)
		{
				if(! super.validTarget(pos)) return false;
        if (pos.getOccupant() != null)
        {
            caster.getGame().getFrame().getMessageArea().setText("Square must be empty");
            return false;
        }
        if(index == 4)
        {
        	for(Enumeration e = caster.getGame().getGameBoard().getAdjacentCreatures(pos); e.hasMoreElements();)
        	{
        		if(e.nextElement() instanceof ShadowWood) 
        		{
        			caster.getGame().getFrame().getMessageArea().setText("Cannot be cast next to another tree");
        			return false;
        		}
        	}
        }
        if(index == 5)
        {
        	for(Enumeration e = caster.getGame().getGameBoard().getAdjacentCreatures(pos); e.hasMoreElements();)
        	{
        		if(e.nextElement() instanceof MagicWood) 
        		{
        			caster.getGame().getFrame().getMessageArea().setText("Cannot be cast next to another tree");
        			return false;
        		}
        	}
        }
        return true;
    }
    
    public boolean cast(Square pos)
    {
        if(super.cast(pos))
        {
	        switch (index)
	        {
	        case 0:
	            GooeyBlob gooeyBlob = new GooeyBlob(pos, caster);
	            break;
	
	        case 1:
	            MagicFire magicFire = new MagicFire(pos, caster);
	            break;
	
	        case 2:
	        case 3:
	            Castle castle = new Castle(pos, caster, index);
	            break;
	
	        case 4:
	        case 5:
	            Object object;
	            /*
	           	System.out.println("Checking for adjacent trees.");
	            if (checkAdjacent(pos))
	            {
	                caster.getGame().getFrame().getMessageArea().setText("Cannot be cast next to another tree");
	                return false;
	            }
	            */
	            if (index == 4)
	                object = new ShadowWood(pos, caster);
	            else
	                object = new MagicWood(pos, caster);
	            // positions[count++] = pos;
	            return true;
	
	        case 6:
	        		Wall w = new Wall(pos, caster);
	            return true;
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
        g.drawString(name, 300 - fontMetrics.stringWidth(name) / 2, 50);
        String string = "( " + Data.alignString[aligns[index] + 1] + " " + factors[index] + " )";
        g.drawString(string, 300 - fontMetrics.stringWidth(string) / 2, 80);
        g.drawString("Spell Range " + ranges[index], 5, 360);
        g.drawString("Casting Chance " + getChance() + "%", 5, 390);
    }
    */

		/*
    private boolean checkAdjacent(Square pos)
    {
        for (int i = 0; i < count; i++)
            if (pos.adjacent(positions[i]))
                return true;
        return false;
    }
    */
}
