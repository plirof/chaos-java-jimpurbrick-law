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

/* Decompiled by Mocha from SummonSpell.class */
/* Originally compiled from SummonSpell.java */

package law;

import java.awt.*;

public class SummonSpell extends Spell implements CreatureStats, InfoPainter
{
    private int index;
    private boolean illusion;
    private static Color darkGreen = new Color(0,192,64);
    private static Color greens[];

    static
    {
      greens = new Color[15];
      for (int k = 0; k < 15; k++)
      {
	greens[k] = new Color(0, (255 / 15) * k, 0);
      }
    }

    SummonSpell(int index, Wizard caster)
    {
        super(names[index],chances[index],1,aligns[index],factors[index],caster,"",1);
        this.index = index;
    }

    public void setIllusion(boolean flag)
    {
        illusion = flag;
    }
		
    public boolean cast(Square pos)
    {
    		Creature newCreature;
    		
    		// If creature is an illusion, set chance so casting always succeeds.
    		if(illusion) chance = 200;    		
    		    		        
        if (super.cast(pos))
        {   
	        // Create creature.
	        if (CreatureStats.flyers[index])
	        {
	            if (CreatureStats.mounts[index])
	                newCreature = new FlyingMount(name, pos, illusion, caster.getGame());
	            else
	                newCreature = new Flyer(name, pos, illusion, caster.getGame());
	        }
	        else if (CreatureStats.mounts[index])
		{
	            newCreature = new WalkingMount(name, pos, illusion, caster.getGame());
		}
	        else
		{
	            newCreature = new Walker(name, pos, illusion, caster.getGame());
		}

	        newCreature.setLeader(caster);

	    /*
	        // Fire green arrow from wizard to target square.
		System.out.println("SummonSpell: Queueing arrow...");
	        caster.getGame().getBattleField().queEffect(new Arrow(caster.getSquare(),pos,Color.green));	       
	        
		System.out.println("SummonSpell: Queued arrow");
		
		System.out.println("SummonSpell: Creating colour cycler...");
	        // Fade creature in.
	        ColourCycler colourCycler = new ColourCycler(newCreature, caster.getGame().getBattleField(), greens);
		System.out.println("SummonSpell: Created colour cycler");
	        caster.queEffect(colourCycler);
		System.out.println("SummonSpell: Waiting for colour cycler...");
	        colourCycler.waitTillDead();
	        
		*/

	        // Make creature visible by setting the creature's square (this makes 
	        // it the occupant of the square so it gets painted)
		System.out.println("SummonSpell: Setting square...");
	        newCreature.setSquare(pos);
	        return true;
	        
	      }
	      else return false;
    }

    public void infoPaint(Graphics g)
    {
    		super.infoPaint(g);
        FontMetrics fontMetrics = g.getFontMetrics(infoFont);
        g.setFont(infoFont);
        g.setColor(Color.yellow); 
        String string2 = "";
        if (CreatureStats.undeads[index])
            string2 = "Undead   ";
        if (CreatureStats.flyers[index])
            string2 += "Flyer   ";
        if (CreatureStats.mounts[index])
            string2 += "Mount   ";
        g.drawString(string2, 5, 120);
        g.drawString("Combat " + CreatureStats.combats[index], 5, 180);
        g.drawString("Ranged Combat " + CreatureStats.rangeds[index], 5, 210);
        g.drawString("Range " + CreatureStats.ranges[index], 5, 240);
        g.drawString("Defense " + CreatureStats.defenses[index], 5, 270);
        g.drawString("Movement " + CreatureStats.moves[index], 5, 150);
        g.drawString("Maneouvre " + CreatureStats.mans[index], 5, 300);
        g.drawString("Magic Resistance " + CreatureStats.magRess[index], 5, 330);
    }
  
    //-------------------------------------------------------------------------
    //	1 parameter used to indicate illusionary status: 1 = Illusion 0 = Real
    //-------------------------------------------------------------------------
    
    public void setParameters(int[] params)
    {
    	switch(params[0])
    	{
    	case 1:
    		illusion = true;
    		break;
    	case 0:
    		illusion = false;
    		break;
    	default: // Error.
   		}
   	}
   	
   	public int[] getParameters()
   	{
   		int[] params = new int[1];
   		params[0] = illusion? 1 : 0;
   		return params;
   	}
}
