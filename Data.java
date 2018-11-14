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

interface Data
{
		// Wizard colours.
		public static final Color blue = new Color(1, 52, 254);
    public static final Color red = new Color(206, 49, 49);
    public static final Color purple = new Color(142, 0, 238);
    public static final Color green = new Color(31, 178, 4);
    public static final Color colours[] = { red, blue, green, purple, Color.magenta, Color.orange, Color.cyan, Color.pink };
    
    // Game colours.
    public static final Color FOREGROUND = Color.yellow;
    public static final Color BACKGROUND = Color.black;
    
    // Game board dimensions.
		public static final int SQUAREWIDTH = 40;
		public static final int SQUAREHEIGHT = 40;
    public static final int SQUARESIZE = 40;
    public static final int NOSQUARESWIDE = 15;
    public static final int NOSQUARESHIGH = 10;
    public static final int NOWIZARDS = 1;
    public static final int NOWIZARDIMAGES = 12;
    
    // Quantity constants.
    public static final int MAXPLAYERS = 8;
    public static final int NOCREATURESPELLS = 32;
    public static final int NOAUTOCREATURESPELLS = 7;
    public static final int NOZAPSPELLS = 6;
    public static final int NOMAGICITEMSPELLS = 7;
    public static final int NOALIGNSPELLS = 4;
    public static final int TOTALNOSPELLS = 58;
    
    // Combat tweaking parameters.
    public static final int def = 6;
    public static final int att = 8;
    
    // Miscellaneous constants.
    public static final String alignString[] = { "Chaos", "Neutral", "Law" };
    public static final int CHAOSPORT = 8888;
    public static final Font infoFont = new Font("Helvetica",Font.PLAIN,20);
    
    // Specifies the dimensions of the screens in the cardlayout.
    public static Dimension screenSize = new Dimension(614,438);
 }
