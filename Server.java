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
//	Law Server v4.0
//
//	Project: Law
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	18/6/02
//
//	Notes:

package law;

//	Import java class libraries.
import java.net.*;
import java.util.*;
import java.io.*;

/*
 *	A dumb server that simply acts as a hub connecting several applets
 *	Data read from one client is simply sent to every other client.
 */

// The server class just parses the command line and then 
// starts up a specified number of connections.
public class Server implements Data
{
  private static PrintStream out = null;		// Stream connected to log file.
  private static PrintStream error = null;	// Stream connected to error file.
  
  Server()
  {
    this(CHAOSPORT);
  }
  
  Server(int port)
  {
    int nThreads = MAXPLAYERS;
    
    // Create the common vectors used as communication between 
    // the connections.
    Vector recievers = new Vector(MAXPLAYERS); // Connections currently recieving messages.
    Vector players = new Vector(MAXPLAYERS); // Players in game.

    try
    {
      // Open the server socket listening on the specified port.
      ServerSocket s = new ServerSocket(port);
      
      // Create the specified number of connections.
      for(int i = 0; i < nThreads; i++)
      {
		Connection c = new Connection(s,recievers, players, i);
		c.start();
      }			
    }	
    catch(IOException e) {error("IOException starting server.");}
    
    log("Law Server v4.0 listening on port " + port + '.');
  }
	
  public static void error(String message)
  {
    if(error != null)
    {
      synchronized(error)
      {
	error.println(new Date().toString() + ":Error:" + message);
      }
    }
  }
  
  public static void log(String message)
  {
    if(out != null)
    {
      synchronized(out)
      {
	out.println(new Date().toString() + ':' + message);
      }
    }
  }
	
  // The single main method for starting up the server.
  public static void main(String argv[]) throws IOException
  {
    CommandLineParser parser = new CommandLineParser(argv);
    
    if(parser.getArgument("help","h") || parser.getArgument("?"))
    {
      System.out.println("Law Server v4.0 (C) 1998-2002 Jim Purbrick (jimpurbrick@yahoo.co.uk)");
      System.out.println("");
      System.out.println("Usage:   java Server [-f <file>] [-v] [-p <port>]");
      System.out.println("");
      System.out.println("Where:   -f <file> enables logging to <file>");
      System.out.println("         -v        enables verbose logging");
      System.out.println("         -p <port> tells server to listen on port <port>");
      System.out.println("                   <port> must be an integer between 2000 and 64000");
      System.out.println("                   if this option is not set <port> defaults to " + CHAOSPORT);
      System.exit(1);
    }
		
    if(parser.getArgument("file","f"))
    {
      String fileName = parser.getArgumentOption("file","f");
      error = new PrintStream(new FileOutputStream(fileName));
      if(parser.getArgument("verbose","v"))
	out = error;
    }
		
    Server s = new Server(parser.getArgumentOptionAsInt("port","p",CHAOSPORT));
  }
}

class ServerPlayerInfo
{
	public int number;
	public String name;
	public int id;
}

// One way of writing the connection would be to have it handle
// a single game and then exit, leaving the server to create
// recievers as they are needed. This approach can be dangerous 
// as, in some virtual machines, threads are not garbage collected
// so the process would steadily run out of memory. To avoid this
// problem the recievers repeatedly service games and so persist
// throughout the life of the server. Threads are not continually 
// created and so the process doesn't leak memory. This also makes 
// the server much simpler.
class Connection extends Thread implements NetGameConstants
{
	// Define member variables.
	private ServerSocket server;		// The socket to listen on.
	private Socket client;			// The connection to the applet.
	private DataInputStream in;		// Stream used for communication.
	private PrintStream out;		// Stream used for communication.		
	private Vector recievers;		// Connections currently recieving messages.
	private int id;				// Used to identify applet.
	private Vector players;			// Players in game.
	
	//---------------------------------
	//	Constructor.
	//---------------------------------
	
	public Connection(ServerSocket s, Vector c, Vector p, int i)
	{
		// Set name up in Thread class.
		super("connection" + i);
		
		// Set up members.		
		id = i;
		server = s;
		recievers = c;
		players = p;
		in = null;
		out = null;
		client = null;
	}
	
	// Method to broadcast messages to the other applets in the game.
	private synchronized void broadcast(String message)
	{
		synchronized(recievers)
		{
			// Loop through recievers sending the message to each of them.
			try
			{	
				Connection current;
				for(Enumeration e = recievers.elements(); e.hasMoreElements();)
				{
					current = ((Connection) e.nextElement());
					if(current != this) current.sendMessage(message);
				}
			}
			catch(IOException e)
			{
				Server.error(getName() + ": error broadcasting message " + e);
			}
		}
	}
	
	// Method called by other recievers wanting to send a message
	// to this recievers applet. Synchronization stops two recievers
	// attempting to write to the connection at the same time.
	public synchronized void sendMessage(String s) throws IOException
	{
		out.println(s);
		Server.log(getName() + ":sending message \"" + s + "\".");
	}
	
	// Body of the thread which loops around servicing recievers
	// until the server process is killed.
	public void run()
	{				
		while(true) // Infinite loop.
		{
			// Accept a connection.
			try
			{
				client = server.accept();
			
				// Run the game.
				runGame();
			}
			catch(IOException e)
			{
				Server.error(getName() + ":error while accepting connection " + e);

				try
				{
					client.close();
				}
				catch(IOException e2)
				{
					Server.error(getName() + ":IOException while closing socket.");
				}
			}
		}
	}
	
	// Method to handle a single game.
	private void runGame()
	{
		// Variables used to parse a command.
		String command, prefix;
		StringTokenizer s;
				
		try
		{	
			// Set up all streams.
			in = new DataInputStream(client.getInputStream());
			out = new PrintStream(client.getOutputStream());

			// Give the applet an identity.
			sendMessage(String.valueOf(ID) + id);
			
			Server.log(getName() + ":joining game.");
		
			// Add current players to client.
			synchronized(players)
			{
				for(Enumeration e = players.elements(); e.hasMoreElements();)
				{
					ServerPlayerInfo playerInfo = (ServerPlayerInfo) e.nextElement();
					sendMessage(String.valueOf(ADD_PLAYER) + playerInfo.number + ":" + playerInfo.name + ":" + playerInfo.id);
				}
			}

			// Join the list of recievers recieving messages from the game.
			synchronized(recievers)
			{
				recievers.addElement(this);

				// Set the applet as the boss if it was the first to join the game.
				if(recievers.firstElement() == this)
				{
					sendMessage(String.valueOf(BOSS));
				}
			}
			
			// Read and process the messages for this game.
			while(true) 
			{		
					command = in.readLine();
					if(command == null)
					{
						Server.log(getName() + ":read null line");
						break;
					}
					command.trim();					
					s = new StringTokenizer(command.substring(1),":");
					switch(command.charAt(0))
					{
					case END_GAME:
						break;
					case ADD_PLAYER:
						synchronized(players)
						{
							ServerPlayerInfo playerInfo = new ServerPlayerInfo();
							playerInfo.number = Integer.parseInt(s.nextToken());
							playerInfo.name = s.nextToken();
							playerInfo.id = Integer.parseInt(s.nextToken());
							Server.log(getName() + ":adding player " + playerInfo.number + " (" + playerInfo.name + ") to game.");
							players.addElement(playerInfo);
						}
						broadcast(command);
						break;
					case REMOVE_PLAYER:
						synchronized(players)
						{
							int number = Integer.parseInt(s.nextToken());
							for(Enumeration e = players.elements();e.hasMoreElements();)
							{
								ServerPlayerInfo pi = (ServerPlayerInfo) e.nextElement();
								if(pi.number == number)
								{
									Server.log(getName() + ":removing player " + pi.number + " (" + pi.name + ") from game.");
									players.removeElement(pi);
									break;
								}
							}
						}
						broadcast(command);
						break;
					default:
					  broadcast(command);
					}
			}
		}
		catch(IOException e) {Server.error(getName() + ":lost connection to client " + e);}
		finally
		{
			// Clean up our hooks into the game.
			try
			{
				// Remove this connection from the list of recievers.
				synchronized(recievers)
				{
					Server.log(getName() + ":leaving game.");
					recievers.removeElement(this);

					if(recievers.size() > 0)
					{
						// Set the first connection to be the boss, in case we used to be.
						((Connection) recievers.firstElement()).sendMessage(String.valueOf(BOSS));
					}
				}

				synchronized(players)
				{
					// Remove this connection's players from the game.
					// Loop through using indices as elements will be removed, which confuses
					// enumerations.
					for(int index = 0; index < players.size(); index++)
					{	
						ServerPlayerInfo pi = (ServerPlayerInfo) players.elementAt(index);
						if(pi.id == id)
						{
							Server.log(getName() + ":removing player " + pi.number + " (" + pi.name + ") from game.");
							broadcast(String.valueOf(REMOVE_PLAYER) + pi.number);
							players.removeElementAt(index);

							// Decrement index to reflect removal.
							index--;
						}
					}
				}
							
				// Close our connection to the applet.
				client.close();
			}
			catch(IOException e2)
			{
				Server.error(getName() + ":IOException while closing socket.");
			}
		}
	}
	
	protected void finalize() throws IOException
	{
		// Close our connection to the applet.
		client.close();
	}
		
} // End class Connection.
