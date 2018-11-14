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

interface CreatureStats
{	
		//                                      0         1            2        3      4             5           6            7      8        9            10       11     12         13     14          15       16       17         18        19           20         21        22              23      24      25         26            27         28       29             30         31
    public static final String names[] = { "Wraith", "Dire Wolf", "Eagle", "Bear", "King Cobra", "Skeleton", "Manticore", "Elf", "Hydra", "Crocodile", "Ghost", "Orc", "Unicorn", "Bat", "Vampire", "Horse", "Harpy", "Gryphon", "Goblin", "Giant Rat", "Gorilla", "Zombie", "Green Dragon", "Lion", "Ogre", "Pegasus", "Red Dragon", "Centaur", "Giant", "Gold Dragon", "Spectre", "Faun" };
    public static final int aligns[] = { -1, -1, 1, 1, 1, -1, -1, 1, -1, 0, -1, -1, 1, -1, -1, 1, -1, 1, -1, 0, 0, -1, -1, 1, -1, 1, -1, 1, 1, 1, -1, -1 };
    public static final int factors[] = { 1, 1, 1, 1, 1, 1, 1, 2, 1, 0, 1, 1, 2, 1, 2, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 1 };
    public static final int combats[] = { 5, 3, 3, 6, 4, 3, 3, 1, 7, 3, 1, 2, 5, 1, 6, 1, 4, 3, 2, 1, 6, 1, 5, 6, 4, 2, 7, 1, 9, 9, 4, 3 };
    public static final int rangeds[] = { 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 3, 2, 0, 5, 0, 0 };
    public static final int ranges[] = { 0, 0, 0, 0, 0, 0, 3, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 5, 4, 0, 4, 0, 0 };
    public static final int defenses[] = { 5, 2, 3, 7, 1, 2, 6, 2, 8, 6, 3, 1, 4, 1, 8, 3, 2, 5, 4, 1, 5, 1, 8, 4, 7, 4, 9, 3, 7, 9, 2, 2 };
    public static final int moves[] = { 2, 3, 5, 2, 1, 1, 4, 1, 1, 1, 2, 1, 4, 4, 4, 4, 4, 4, 1, 3, 1, 1, 3, 4, 1, 4, 4, 4, 2, 3, 1, 1 };
    public static final int mans[] = { 4, 7, 8, 6, 6, 3, 6, 5, 4, 2, 9, 4, 9, 9, 6, 8, 8, 5, 4, 8, 4, 2, 4, 8, 3, 6, 5, 5, 6, 5, 6, 7 };
    public static final int magRess[] = { 5, 2, 2, 2, 1, 4, 8, 7, 6, 2, 6, 0, 7, 4, 5, 1, 5, 6, 4, 2, 2, 3, 4, 3, 6, 7, 5, 5, 5, 5, 4, 8 };
    public static final int chances[] = { 50, 80, 70, 60, 80, 70, 40, 70, 50, 80, 50, 90, 60, 80, 20, 90, 60, 60, 90, 100, 70, 90, 10, 60, 70, 60, 10, 60, 40, 10, 60, 80 };
    public static final boolean undeads[] = { true, false, false, false, false, true, false, false, false, false, true, false, false, false, true, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true, false };
    public static final boolean mounts[] = { false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, true, false, true, false, false, false, false, false, false, false, true, false, true, false, false, false, false };
    public static final boolean flyers[] = { false, false, true, false, false, false, true, false, false, false, true, false, false, true, true, false, true, true, false, false, false, false, true, false, false, true, true, false, false, true, false, false };

}
