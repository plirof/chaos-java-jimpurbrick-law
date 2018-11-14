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

// --------------------------------------------------------------------------
//
// NAME                    : CommandLineParser.java
// VERSION                 : 
// COMPILED USING          : SUN JDK 1.1.2 / KAWA IDE 2.5
// AUTHOR                  : Dan Page (dsp@cs.nott.ac.uk)
// LAST REVISION DATE      : 
// NOTES                   : 
//
// --------------------------------------------------------------------------

// -------------------------------------------
// Declare we are part of the hairNET package.
// -------------------------------------------
//package hairNET.lang;
package law; // For now....
 
// --------------------------------------------------------------------------
// Import all the super duper java class files.
// --------------------------------------------------------------------------
import java.util.*;
 
// --------------------------------------------------------------------------
// Define the main class.
// --------------------------------------------------------------------------
class CommandLineParser
{

	// --------------------------------
  // Define private member variables.
  // --------------------------------
  private Vector parsedArguments 		= null;
  private Vector parsedExtras				= null;
  private String switchString  			= null;
  private String[] commandArguments 	= null;
  private String[] flagArguments			= null;
  
  // -----------------------------------------
  // Define a constructor to set up the class.
  // -----------------------------------------
	public CommandLineParser( String[] args ){ this( args, null, "-" ); }

  // -----------------------------------------
  // Define a constructor to set up the class.
  // -----------------------------------------
	public CommandLineParser( String[] args, String[] flags ) { this( args, flags, "-" ); }

	
  // -----------------------------------------
  // Define a constructor to set up the class.
  // -----------------------------------------
	public CommandLineParser( String[] args, String[] flags, String option )
	{
	
		// Create member variables.
		switchString   		= option;
		commandArguments	= args;
		flagArguments			= flags;
		parsedArguments 	= new Vector( 10, 10 );
		parsedExtras			= new Vector( 10, 10 );
		
		// Parse the command line to get the options.
		parseCommandLine( args );
	
	}
	
	// ---------------------------------------------
	// Define a method to count all command options.
	// ---------------------------------------------
	public int noArguments() { return parsedArguments.size(); }
	public int noExtras() { return parsedExtras.size(); }

	// --------------------------------------------	
	// Define a method to test if an option exists.
	// --------------------------------------------
	public boolean getArgument( String argument ) { return getArgument( argument, "" ); }
	public boolean getArgument( String argument, String shortArgument ) 
	{ 
	
		// Try and get the option referered to by the argument.
		return ( findOption( argument ) != null ) || ( findOption( shortArgument ) != null ); 
		
	}

	// -----------------------------------------------	
	// Define a method to get the value for an option.
	// -----------------------------------------------
	public String getArgumentOption( String argument ) { return getArgumentOption( argument, "" ); }
	public String getArgumentOption( String argument, String shortArgument ) 
	{
	
		// Try and find the option.
		if     ( findOption( argument ) != null ) 			{ return findOption( argument ).getOption();  }
		else if( findOption( shortArgument ) != null ) 	{ return findOption( shortArgument ).getOption();  }
		else   				                            				{ return null; }
	
	}

	// -------------------------------------------------------	
	// Define a method to get an argument option as a boolean.
	// -------------------------------------------------------
	public boolean getArgumentOptionAsBoolean( String argument ) { return getArgumentOptionAsBoolean( argument, "" ); }
	public boolean getArgumentOptionAsBoolean( String argument, String shortArgument ) { return getArgumentOptionAsBoolean( argument, "", true ); }
	public boolean getArgumentOptionAsBoolean( String argument, String shortArgument, boolean result ) 
	{
	
		// Try and decode the option.
		String option = getArgumentOption( argument, shortArgument );
		
		// Check if the option was found.
		if( option != null )
		{
			// Check the option.
			if     ( option.toLowerCase().equals( "" ) )				{ return true; }
			else if( option.toLowerCase().equals( "true" ) ) 		{ return true; }
			else if( option.toLowerCase().equals( "false" ) ) 	{ return false; }
			else if( option.toLowerCase().equals( "on" ) ) 			{ return true; }
			else if( option.toLowerCase().equals( "off" ) ) 		{ return false; }
			else 																									{ return result; }	
		}
		else { return result; }
		
	}
	
	// -------------------------------------------------------	
	// Define a method to get an argument option as a boolean.
	// -------------------------------------------------------
	public int getArgumentOptionAsInt( String argument ) { return getArgumentOptionAsInt( argument, "" ); }
	public int getArgumentOptionAsInt( String argument, String shortArgument ) { return getArgumentOptionAsInt( argument, "", 0 ); }
	public int getArgumentOptionAsInt( String argument, String shortArgument, int result ) 
	{
	
		// Try and decode the option.
		String option = getArgumentOption( argument, shortArgument );
		
		// Check if the option was found.
		if( option != null )
		{
			// Try and parse the integer.
			try { return Integer.parseInt( option ); }
			catch( NumberFormatException e ) { return result; }	
		}
		else { return result; }
		
	}

	// -------------------------------------------	
	// Define a method to get the extra arguments.
	// -------------------------------------------
	public String[] getArgumentExtras()
	{
	
		// Define local variables.
		int counter                 	= 0;
		String[] extraArray						= new String[ parsedExtras.size() ];
		CommandLineArgument object 	= null;
		Enumeration enum     				= null;
	
		// Look for the option.
		for( enum = parsedExtras.elements(); enum.hasMoreElements(); )
		{
		
			// Get the option and test it.
			object = ( CommandLineArgument )( enum.nextElement() );
			extraArray[ counter ] = new String( object.getArgument() );
			counter++;
			
		}
		
		// Return the extra arguments.
		return extraArray;
	
	}
	
	// -------------------------------------------------------
	// Define a method to find a command option in the vector.
	// -------------------------------------------------------
	private CommandLineArgument findOption( String argument )
	{
	
		// Define local variables.
		CommandLineArgument object 	= null;
		Enumeration enum     				= null;
	
		// Look for the option.
		for( enum = parsedArguments.elements(); enum.hasMoreElements(); )
		{
		
			// Get the option and test it.
			object = ( CommandLineArgument )( enum.nextElement() );
			if( object.getArgument().equals( argument ) || object.getArgument().equals( String.valueOf( switchString ) + argument ) ) { return object; }
			
		}
		
		// We didn't find the option, return null.
		return null;
	
	}
	
	// -------------------------------------------------------
	// Define a method to find a command option in the vector.
	// -------------------------------------------------------
	private boolean findFlag( String argument )
	{
	
		// Define local variables.
		int counter = 0;
		
		// Check if the flags are null.
		if( flagArguments == null ) { return false; }
	
		// Look for the option.
		for( counter = 0; counter < flagArguments.length; counter++ )
		{
			if( flagArguments[ counter ].equals( argument ) ) { return true; }				
		}
		
		// We didn't find the option, return null.
		return false;
	
	}
			
	// ------------------------------------------------------------------------
	// Define a method to parse the command line and put results in args array.
	// ------------------------------------------------------------------------
	private void parseCommandLine( String[] args )
	{
	
		// Define local variables.
		int counter 							= 0;
		String currentArgument 	= null;
		String currentOption			= null;
		
		// Loop through all the arguments looking for the options.
		for( counter = 0; counter < commandArguments.length; counter++ )
		{
		
			// Get the current argument.
			currentArgument = commandArguments[ counter ];
			
			// Does the argument start with the switch string ?
			if( currentArgument.startsWith( switchString ) )
			{
			
				// Strip the switchString from the argument.
				currentArgument = currentArgument.substring( switchString.length() );	
			
				// Check for the optional part of the option.
				if( counter + 1 < commandArguments.length )
				{
				
					// Check the next argument to see if it is a flag or option.
					if( !findFlag( currentArgument ) && !commandArguments[ counter + 1 ].startsWith( switchString ) )
					{
					
						// Increment the counter and save the current option.
						currentOption = commandArguments[ counter + 1 ];
						counter++;
					
					}
					else { currentOption = ""; }

				}
				else { currentOption = ""; }
				
				// Add the option to the parsed ones.
				parsedArguments.addElement( new CommandLineArgument( currentArgument, currentOption ) );
				
			}
			else
			{
			
				// It is an 'extra' argument so add it to the extra's hashtable.
				parsedExtras.addElement( new CommandLineArgument( currentArgument ) );
			
			}
		
		}
	
		
	}

	// ----------------------------------------------------	
	// Define a method to test the CommandLineParser parse.
	// ----------------------------------------------------
	public void debugCommandLine()
	{
	
		// Define local varaibles.
		int counter 										= 0;
		CommandLineArgument argument 	= null;
		
		// Loop through the extra options.
		for( counter = 0; counter < parsedExtras.size(); counter++ )
		{
			// Get the current argument and debug it.
			argument = ( CommandLineArgument )( parsedExtras.elementAt( counter ) );
			System.out.println( "EXTRA [value=" + argument.getArgument() + "]" );
		}
		
		// Loop through the extra options.
		for( counter = 0; counter < parsedArguments.size(); counter++ )
		{
			// Get the current argument and debug it.
			argument = ( CommandLineArgument )( parsedArguments.elementAt( counter ) );
			System.out.println( "ARG   [value=" + argument.getArgument() + ", option=" + argument.getOption() + "]" );
		}
	
	}
	
}

// --------------------------------------------------------------------------
// Define a class to hold details about a single option.
// --------------------------------------------------------------------------
class CommandLineArgument
{

	// --------------------------------
  // Define private member variables.
  // --------------------------------
  private String argumentValue 	= null;
  private String optionValue	  	= null;
  
  // -----------------------------------------
  // Define a constructor to set up the class.
  // -----------------------------------------
	CommandLineArgument( String argument ) { this( argument, "" ); }

  // -----------------------------------------
  // Define a constructor to set up the class.
  // -----------------------------------------
	CommandLineArgument( String argument, String option )
	{
	
		// Create member variables.
		argumentValue = new String( argument );
		optionValue   = new String( option );
	
	}

	// ---------------------------------------
	// Define methods to set member variables.
	// ---------------------------------------
	public void setArgument( String s ) 	{ argumentValue = new String( s ); }
	public void setOption( String s )  	{ optionValue = new String( s );  }
	
	// ---------------------------------------
	// Define methods to get member variables.
	// ---------------------------------------
	public String getArgument() 	{ return argumentValue; }
	public String getOption() 		{ return optionValue; }

}

 
