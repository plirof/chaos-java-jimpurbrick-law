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

/* Decompiled by Mocha from Dice.class */
/* Originally compiled from Dice.java */

package law;

import java.util.Random;

class Dice
{
    private Random randy;
    private int roll = 0;

    public int d(int i)
    {
        int thisRoll = Math.abs(randy.nextInt()) % i;
        System.out.println("Dice: Roll " + ++roll + " was " + thisRoll);
        return thisRoll;
    }

    public void setSeed(int seed)
    {
    	randy = new Random(seed);
    	roll = 0;
    }

    Dice()
    {
    	randy = new Random();
    }
}
