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

class RaiseDeadSpell extends Spell
{
    RaiseDeadSpell(Wizard caster)
    {
    		// Raise dead stats are correct.
    		super("Raise Dead",50,5,-1,2,caster,"Target becomes an undead creature under your control.",1);
    }

		public boolean validTarget(Square pos)
		{
			if(! super.validTarget(pos)) return false;
			if(pos.getOccupant() != null) 
    	{
    		caster.getGame().getFrame().getMessageArea().setText("Target must be dead");
    		return false;
    	}
    	if(pos.topCorpse() == null)
    	{
    		caster.getGame().getFrame().getMessageArea().setText("No target");
    		return false;
    	}
    	return true;
    }
			
    public boolean cast(Square pos)
    {   
    	  if(super.cast(pos))
    	  {
	        Creature target = pos.topCorpse();
	    		if (caster.getGame().getDice().d(12) > target.getMagRes())
	    		{
	    			caster.getGame().getFrame().getMessageArea().setText("Creature Raised");
	    			pos.popCorpse();
	    			target.setUndead(true);
	    			target.setDead(false);
	    			pos.setOccupant(target);
	    			target.setLeader(caster);	    			
	    		}
	        return true;
	      }
	      else return false;
    }
}
