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

/* Decompiled by Mocha from DisbelieveSpell.class */
/* Originally compiled from DisbelieveSpell.java */

package law;

import java.awt.*;

/**
  * A spell that destroys illusionary targets it's cast at.
  */
public class DisbelieveSpell extends Spell implements Data
{
    DisbelieveSpell(Wizard caster)
    {
    		super("Disbelieve",100,100,0,0,caster,"Destroys illusionary targets. Does not require line of sight.",1);
    }

		public boolean validTarget(Square pos)
		{
				if(pos.getOccupant() == null)
				{
					caster.getGame().getFrame().getMessageArea().setText("Target must be a creature");
					return false;
				}
				else return true;
		}
		
    public boolean cast(Square pos)
    {
    		super.cast(pos);
        Creature creature = pos.getOccupant();
        if (creature != null && creature.isIllusion())
            creature.destroy();
        return true;
    }
}
