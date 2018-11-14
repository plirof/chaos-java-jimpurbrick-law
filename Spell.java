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

//-----------------------------------------------------------------------
//	Spell class
//
//	Project: Chaos
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	3/9/97
//
//	Notes:

//-----------------------------------------------------------------------

package law;

import java.awt.*;
import java.util.*;

/**
 *	Provides the common interface to all the various spell classes
 *	as well as static methods to get spells randomly or by ID.
 *	This whole class heirarchy needs tidying up, but it works for now :-)
 */

public abstract class Spell implements Data, CreatureStats, InfoPainter
{    
    protected int id;
    protected String name;
    protected int chance;
    protected int range;
    protected int align;
    protected int factor;
    protected int nTargetsNeeded;
    private boolean successfullyCast; // Success should only be tested for once.
    private String info;
    protected Wizard caster;

    Spell(String name, int chance, int range, int align, int factor, Wizard caster, String info, int nTargets)
    {
        this.name = name;
        this.chance = chance;
        this.range = range;
        this.align = align;
        this.factor = factor;
        this.caster = caster;
        this.info = info;
        nTargetsNeeded = nTargets;
        successfullyCast = false;
    }

		/*
    public int getID()
    {
    	return id;
    }
    
    public void setID(int i)
    {
    	id = i;
    }
    */

    static Spell getSpell(int i, Wizard wizard)
    {
        return new SummonSpell(i, wizard);
    }
    
    static Spell getSpell(Wizard wizard)
    {
    		int i = wizard.getGame().getDice().d(TOTALNOSPELLS);
    		Spell s = null;
        if (i < NOCREATURESPELLS)
            s = new SummonSpell(i, wizard);
        else if ((i -= NOCREATURESPELLS) < NOZAPSPELLS)
            s = new ZapSpell(i, wizard);
        else if ((i -= NOZAPSPELLS) < NOAUTOCREATURESPELLS)
            s = new AutoCreatureSpell(i, wizard);
        else if ((i -= NOAUTOCREATURESPELLS) < NOMAGICITEMSPELLS)
            s = new MagicItemSpell(i, wizard);
        else if ((i -= NOMAGICITEMSPELLS) < NOALIGNSPELLS)
            s = new AlignSpell(i,wizard);
        else
        {
        	switch(i -= NOALIGNSPELLS)
        	{
        		case 0:
        			s = new SubversionSpell(wizard);
        			break;
        		case 1:
        			s = new RaiseDeadSpell(wizard);
        			break;  		
        	}
        }
        return s;
    }
    
    public String getName()
    {
    	return name;
    }

		public int getTargetsNeeded()
		{
			return nTargetsNeeded;
		}
		
		// Returns true if the spell was sucessfully cast. If false is
		// returned, no further casting attempts should be made.
    public boolean cast(Square pos)
    {
    	nTargetsNeeded--;
    	System.out.println("Spell: " + nTargetsNeeded + " targets needed.");
    	    	
    	// If success test has previously been passed, just succeed.
    	if(successfullyCast) return true;
    	
    	// This is the first casting attempt for this spell, so test for success.
    	if(caster.getGame().getDice().d(100) < getChance())
    	{
    		// Further casting attempts should always succeed.
    		successfullyCast = true;
    			
    		// Adjust the world's alignment - this should only be done once.
    		caster.getGame().getFrame().getAlignOMeter().adjustAlignment(align * factor * 10);
    		return true;
    	}
    	// Casting attempt failed. No further attempts should be made.
    	else return false;
    }
    
    
    // Returns true if the Square is a valid target.
  	// Default behaviour is to check range and line of sight only.
  	public boolean validTarget(Square pos)
  	{
  		if (pos.range(caster.getSquare()) > range)
      {
        caster.getGame().getFrame().getMessageArea().setText("Out of range");
        return false;
      } 
      if (! caster.getGame().getGameBoard().LOS(caster.getSquare(), pos))
      {
        caster.getGame().getFrame().getMessageArea().setText("No LOS");
        return false;
      }
      return true;
		}
    
    private int search(String string)
    {
        for (int i = 0; i < CreatureStats.names.length; i++)
            if (CreatureStats.names[i].equals(string))
                return i;
        return -1;
    }
    
    public int getChance()
    {
    	// Work out current casting chance from base chance, spell alignment and world alignment.
    	int i = chance + align * caster.getGame().getFrame().getAlignOMeter().getAlignment();
    	
    	// Chances should only be increased, not decreased, by world alignment.
    	if(i < chance) i = chance;
    	
    	// Chances should never be greater than 100 or less than 0.
      if (i > 100) i = 100;
      else if (i < 0) i = 0;
      
      return i;
    }
    
    // Paints spell information to the screen. Should be
    // updated with wordwraping, possibly HTML version with
    // default behaviour.
    public void infoPaint(Graphics g)
    {
        FontMetrics fontMetrics = g.getFontMetrics(infoFont);
        g.setFont(infoFont);
        g.setColor(Color.yellow);
        g.drawString(name, 300 - fontMetrics.stringWidth(name) / 2, 50);
        String string = "( " + Data.alignString[align + 1] + " " + factor + " )";
        g.drawString(string, 300 - fontMetrics.stringWidth(string) / 2, 80);
        
        StringTokenizer s = new StringTokenizer(info," ",true);
        String token = "";
        Point p = new Point(35,140);
        while(s.hasMoreTokens())
       	{
       		token = s.nextToken();
       		if(p.x + fontMetrics.stringWidth(token) > 450)
       		{
       			p.y += 30;
       			p.x = 35;
       		}
       		g.drawString(token,p.x,p.y);
       		p.x += fontMetrics.stringWidth(token);
       	}
       	
        g.drawString("Spell Range " + (range > 0? Integer.toString(range) : "Personal"), 5, 360);
        g.drawString("Casting Chance " + getChance() + "%", 5, 390);
    }
    
    //-------------------------------------------------------------------------
    //	Methods for parameterising spells over the network.
    //-------------------------------------------------------------------------
    
    void setParameters(int[] params)
    {
    }
    
   	int[] getParameters()
   	{
   		return new int[0];
   	}
}
