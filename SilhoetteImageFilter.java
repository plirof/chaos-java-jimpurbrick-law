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

/* Decompiled by Mocha from SilhoetteImageFilter.class */
/* Originally compiled from ColourCycler.java */

package law;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

class SilhoetteImageFilter extends RGBImageFilter
{
    private int background;
    private int newColour;

    public SilhoetteImageFilter(Color color)
    {
        background = color.getRGB();
        // canFilterIndexColorModel = true;
        //// 1.0 Fix ////
        // Need to make top left transparent to get around
        // transparency problems.
        canFilterIndexColorModel = false;
        //// End 1.0 Fix ////
    }

    public int filterRGB(int i, int j, int k)
    {
    		//// 1.0 Fix ////
        // Need to make top left transparent to get around
        // transparency problems.
        if (k != background || (i == 0 && j == 0))
            return 16777215;
        else
            return background | -16777216;
        //// End 1.0 Fix ////
    }
}
