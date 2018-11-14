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

class ZapSpell extends Spell
{
    private int targetCount;
    private int index;
    private static final String names[]= {"Lightning Bolt", "Justice", "Vengence", "Dark Power", "Decree","Fireball" };
    private static final int chances[] = {100, 50, 80, 50, 80, 100}; // These casting chances are correct, not made up as it may seem.
    private static final int aligns[]  = {1, 1, -1, -1, 1, -1};
    private static final int factor[]  = {1, 2, 1, 2, 1, 1};
    private static final int ranges[]  = {4, 20, 20, 20, 20, 6};
    private static final int nTargets[] = {1, 3, 1, 3, 1, 1};
    private static final int powers[]  = {6, 3, 4, 4, 4, 4};
    private static final String info[] = {"Attacks a single creature. ","When cast on a wizard all its creatures are killed. ","Attacks a single creature. ","Allows 3 attacks on creatures. ","Attacks a single creature. ","Attacks a single creature. "};

    ZapSpell(int i, Wizard caster)
    {
    	super(names[i],chances[i],ranges[i],aligns[i],factors[i],caster,info[i] + " Requires line of sight to target.", nTargets[i]);
    	index = i;
    }
    
    public boolean validTarget(Square pos)
    {
    	if(! super.validTarget(pos)) return false;
    	if(pos.getOccupant() == null)
    	{
    		caster.getGame().getFrame().getMessageArea().setText("No target");
    		return false;
    	}
    	return true;
    }
    
    public boolean cast(Square pos)
    {
        if (super.cast(pos))
        {
	        Creature creature = pos.getOccupant();
	        switch (index)
	        {
	        case 0:
	            Color acolor1[] = new Color[15];
	            for (int i1 = 0; i1 < 15; i1++)
	            {
	                acolor1[i1++] = new Color(128, 128, 255);
	                acolor1[i1++] = Color.white;
	            }
	            GFX agFX[] = { new ColourCycler(creature, caster.getGame().getBattleField(), acolor1), new LightningBolt(caster.getSquare(), pos) };
	            MultiGFX multiGFX = new MultiGFX(agFX);
	            caster.queEffect(multiGFX);
	            multiGFX.waitTillDead();
	            if (creature.magicAttack(creature, powers[index]))
	                creature.die();
	            break;
	
	        case 1:
	            if (!(creature instanceof Wizard))
	            {
	                caster.getGame().getFrame().getMessageArea().setText("Target must be wizard");
	                return false;
	            }
	            Wizard wizard = (Wizard) creature;
	            ColourCycler colourCycler1 = new ColourCycler(creature, caster.getGame().getBattleField());
	            caster.queEffect(colourCycler1);
	            colourCycler1.waitTillDead();
	            if (caster.getGame().getDice().d(6) + caster.getGame().getDice().d(6) + wizard.getMagRes() < caster.getGame().getDice().d(6) + caster.getGame().getDice().d(6) + powers[index])
	            {
	                for (Enumeration enumeration1 = caster.getGame().getGameBoard().getCreatures(wizard); enumeration1.hasMoreElements(); )
	                {
	                    creature = (Creature)enumeration1.nextElement();
	                    if(creature != wizard) creature.die();
	                }
	            }
	            break;
	
	        case 2:
	            if (creature.magicAttack(creature, powers[index]))
	                creature.die();
	            Color acolor2[] = new Color[15];
	            for (int j = 0; j < 15; j++)
	                acolor2[j] = new Color(105 + j * 10, j * 17, j * 17);
	            ColourCycler colourCycler2 = new ColourCycler(creature, caster.getGame().getBattleField(), acolor2);
	            caster.queEffect(colourCycler2);
	            colourCycler2.waitTillDead();
	            break;
	
	        case 3:
	            if (creature.magicAttack(creature, powers[index]))
	                creature.die();
	            Color acolor3[] = new Color[15];
	            for (int k = 0; k < 15; k++)
	                acolor3[k] = new Color(0, 0, 100 + k % 3 * 30);
	            ColourCycler colourCycler3 = new ColourCycler(creature, caster.getGame().getBattleField(), acolor3);
	            caster.queEffect(colourCycler3);
	            colourCycler3.waitTillDead();
	            break;
	
	        case 4:
	            if (creature.magicAttack(creature, powers[index]))
	                creature.die();
	            Color acolor4[] = new Color[15];
	            for (int i2 = 0; i2 < 15; i2++)
	                acolor4[i2] = new Color(255, 255, 255 - i2 % 3 * 80);
	            ColourCycler colourCycler4 = new ColourCycler(creature, caster.getGame().getBattleField(), acolor4);
	            caster.queEffect(colourCycler4);
	            colourCycler4.waitTillDead();
	            break;
	        case 5: // Fireball.
	        		GFX fireball = new Fireball(caster.getSquare(),pos);
	        		GFX explode  = new Explode(pos);
	        		caster.queEffect(fireball);
	        		caster.queEffect(explode);
	        		explode.waitTillDead();
	        		if (creature.magicAttack(creature, powers[index]));
	        				creature.die();
	        		break;
	        		
	        }       
	        return true;
	      }
	      else return false;
    }
}
