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

/* Decompiled by Mocha from MagicItemSpell.class */
/* Originally compiled from MagicItemSpell.java */

package law;

import java.awt.*;

/**
 * Spells which confer some modification on the casting wizard.
 */

public class MagicItemSpell extends Spell
{
    private int index;
    private static final String names[] = { "Magic Knife", "Magic Sword", "Magic Shield", "Magic Armour", "Magic Wings", "Magic Bow", "Shadow Form" };
    private static final int aligns[] = { 1, 1, 1, 1, 0, 1, 0 };
    private static final int factors[] = { 1, 1, 1, 1, 0, 1, 0 };
    private static final int chances[] = { 70, 40, 70, 40, 50, 50, 70 };
    private static final String info[] = { "Makes wizard's attacks more powerful and allows wizard to harm undead.","Makes wizards attacks more powerful and allows wizard to harm undead.","Gives wizard increased protection.","Gives wizard increased protection.","Allows wizard to fly.","Gives wizard a ranged attack.","Makes wizard more difficult to harm and increases wizard's movement."};

    MagicItemSpell(int i, Wizard caster)
    {
        super(names[i],chances[i],0,aligns[i],factors[i],caster,info[i],1);
        index = i;
    }

		public boolean validTarget(Square pos)
		{
			return true;
		}
		
    public boolean cast(Square pos)
    {
        if (super.cast(pos))
        {
            switch (index)
            {
            case 0:
                caster.setMagicAttack(true);               
                caster.setAnimation(1,1,1);
                break;

            case 1:
                caster.setMagicAttack(true);
                caster.adjustAttack(2);
                caster.setAnimation(2,2,2);
                break;

            case 2:
                caster.adjustDefense(2);
                caster.setAnimation(3,3,3);
                break;

            case 3:
                caster.adjustDefense(3);
                caster.setAnimation(4,4,4);
                break;

            case 4:
                caster.setFlyer(true);
                caster.setMovement(3);
                caster.setAnimation(5,6,7);
                break;

            case 5:
                caster.setRanged(3);
                caster.setRange(6);
                caster.setAnimation(8,9,10);
                break;

            case 6:
                caster.setMovement(3);
                caster.setAnimation(11,11,0);
                break;
            }
            return true;
        }
        else return false;
    }

		/*
    public void infoPaint(Graphics g)
    {
        String string2;
        Font font = new Font("Ariel", 1, 24);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString(name, 300 - fontMetrics.stringWidth(name) / 2, 50);
        String string1 = "( " + Data.alignString[aligns[index] + 1] + " " + factors[index] + " )";
        g.drawString(string1, 300 - fontMetrics.stringWidth(string1) / 2, 80);
        switch (index)
        {
        case 0:
            string2 = "Creates a magic knife allowing the wizard to harm undead";
            break;
        }
        g.drawString("Range Personal", 5, 360);
        g.drawString("Casting Chance " + getChance() + "%", 5, 390);
    }
    */
}
