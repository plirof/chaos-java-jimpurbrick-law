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
//	NetGame
//
//	Project: Chaos
//	Author:	 James Purbrick (jcp@cs.nott.ac.uk)
//	Last Revision:	1/7/97
//
//	Notes:
//	Provides generalised network services for strategy games.
//-----------------------------------------------------------------------

package law;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;

// Extension to Runnable interface that adds the stop method.
// Allows all active objects to be stopped.
interface Threadable extends Runnable
{
	public void stop();
}

class NetGame implements NetListener, NetGameConstants
{
	ClientConnection connection;
	private int id = -1;
	private boolean boss = false;
	private ChatWindow chatWind;
	private NetGameListener listener;
	
	//---------------------------------------------
	//	Constructors.
	//---------------------------------------------
	
	public NetGame(NetGameListener l, InetAddress i, int port, Frame mainWindow) throws IOException
	{
		listener = l;
		connection = new NetConnection(i,port,this);
		chatWind = new ChatWindow(this, mainWindow);		
	}

	public NetGame(NetGameListener l)
	{
		listener = l;
		connection = new LoopBackConnection(this);
		chatWind = null;
	}	
	
	public void messageRecieved(String message)
	{
			System.out.println("Received:" + message);
			StringTokenizer s = new StringTokenizer(message.substring(1),":",false);
			switch(message.charAt(0))
			{
			case ADD_PLAYER:
				// Tokenizing and integer parsing is not embeded in addPlayer call,
				// because it makes IE3 go mental.
				int playerNumber = Integer.parseInt(s.nextToken());
				String name = s.nextToken();
				int clientID = Integer.parseInt(s.nextToken());
				listener.addPlayer(playerNumber,name,clientID == id);
				break;			
			case REMOVE_PLAYER:
				listener.removePlayer(Integer.parseInt(s.nextToken()));
				break;				
			case TALK:
				if(chatWind != null && s.hasMoreTokens()) chatWind.showMessage(s.nextToken());
				break;
			case ID:		
				id = Integer.parseInt(s.nextToken());
				break;	
			case BOSS:
				// NOTE: This should trigger a listener callback.
				boss = true;
				System.out.println("Boss is " + boss);
				break;
			case START_GAME:				
				listener.startGame(Integer.parseInt(s.nextToken()));
				break;						
			case NEXT_TURN:
				listener.nextTurn();
				break;						
			case UNDEFINED: // Pass message on to listener.
				listener.handleCommand(message.substring(1));
			}
	}
	
	public boolean isBoss()
	{
		return boss;
	}
	
	public void sendChat(String s)
	{
		connection.sendMessage(String.valueOf(TALK) + s);
	}
	
	public void sendNextTurn()
	{
		connection.sendMessage(String.valueOf(NEXT_TURN));
	}
	
	public void sendStartGame()
	{
		if(isBoss())
		{
			Random randy = new Random();
			connection.sendMessage(String.valueOf(START_GAME) + randy.nextInt());
		}
	}
	
	public void sendEndGame()
	{
	  connection.sendMessage(String.valueOf(END_GAME));
	}

	protected void sendCommand(String s)
	{
		System.out.println("Sent:" + s);
		connection.sendMessage(String.valueOf(UNDEFINED) + s);
	}
	
	public void sendAddPlayer(String name, int number)
	{
		// Only allow adding players when id has been set.
		if(id != -1)
		{
			connection.sendMessage(String.valueOf(ADD_PLAYER) + number + ":" + name + ":" + id);
		}
	}
	
	public void sendRemovePlayer(int number, String name)
	{
		connection.sendMessage(String.valueOf(REMOVE_PLAYER) + number + ':' + name);
	}
	
	public Window getChatWindow()
	{
		return chatWind;
	}
	
	/*
	public void dispose()
	{
		if(connection instanceof NetConnection) ((NetConnection) connection).stop();
	}
	*/
	
	public void setListener(NetGameListener l)
	{
		listener = l;
	}

	/*
	// Disconnects this client
	public void disconnect()
	{
		if(connection instanceof NetConnection)
		{
			((NetConnection) connection).stop();
		}
	}
	*/
	
	public Threadable getThreadable()
	{
		if(connection instanceof NetConnection)
		{
			return (Threadable) connection;
		}
		else return null;
	}			
	
} // End class NetGame.

interface NetListener
{
	public void messageRecieved(String message);
}

abstract class ClientConnection
{
	protected NetListener listener;
	
	protected ClientConnection(NetListener nl)
	{
		listener = nl;
	}
	
	public abstract void sendMessage(String message);
}

class LoopBackConnection extends ClientConnection implements NetGameConstants
{
	public LoopBackConnection(NetListener nl)
	{
		super(nl);
		// As it is a local game, this machine is automatically the boss.
		listener.messageRecieved(String.valueOf(BOSS));
		listener.messageRecieved(String.valueOf(ID) + 0);
	}
	
	public void sendMessage(String message)
	{
		listener.messageRecieved(message);
	}
}

class NetConnection extends ClientConnection implements Threadable
{
	private static int threadID;
	private Thread readThread;
	private Socket server;
	private DataInputStream in;
	private PrintStream out;
	
	public NetConnection(InetAddress i, int port, NetListener nl) throws IOException
	{
		super(nl);
		server = new Socket(i,port);
		in = new DataInputStream(server.getInputStream());
		out = new PrintStream(server.getOutputStream());
		start();
	}
	
	public void sendMessage(String message)
	{
		out.println(message.trim());
		listener.messageRecieved(message);
	}
	
	public void run()
	{
		String message;
		try
		{
			while(true)
			{
				message = in.readLine();
				if(message == null) break;
				listener.messageRecieved(message);
			}
		}
		catch(IOException e)
		{	
		}
		finally
		{
			System.out.println("Stopping NetConnection");
			try{server.close();}
			catch(IOException e2){;}
		}
	}
	
	public void start()
	{
		if(readThread == null)
		{
			readThread = new Thread(this,"Network Reader" + (++threadID));
			readThread.start();
		}
	}
	
	public void stop()
	{
		try{server.close();}
		catch(IOException e2){;}
		if(readThread != null && readThread.isAlive())
		{
			readThread.stop();
		}
		readThread = null;
	}
	
	protected void finalize() throws IOException
	{
		server.close();
	}
	
	public Thread getThread()
	{
		return readThread;
	}
}

class ChatWindow extends Dialog
{
	private Button send;
	private TextArea messages;
	private TextField message;
	private NetGame network;
	
	ChatWindow(NetGame n, Frame parent)
	{		
		super(parent,"Chat Window",false);
		System.out.println("Creating chat window.");
		network = n;
		setLayout(new BorderLayout(5,5));
		Panel buttonBar = new Panel();
		messages = new TextArea();
		message = new TextField();
		send = new Button("Send");
		buttonBar.setLayout(new BorderLayout());
		buttonBar.add("East",send);
		buttonBar.add("Center",message);
		add("South",buttonBar);
		add("Center",messages);
		reshape(0,0,300,150);
		show();
	}	
	
	public boolean action(Event e, Object o)
	{
		network.sendChat(message.getText());
		message.setText("");
		return true;
	}
	
	public boolean handleEvent(Event e)
  {
  	if(e.id == Event.WINDOW_DESTROY)
  	{
  		hide();
  		return true;
  	}
  	else return super.handleEvent(e);
  }
	
	public void showMessage(String message)
	{
		if(! isVisible()) show();
		messages.appendText(message + '\n');
	}
	
	public void finalize()
	{
		dispose();
	}
	
	public void setForeground(Color c)
	{
		super.setForeground(c);
		message.setForeground(c);
		messages.setForeground(c);
		send.setForeground(c);
	}
	
	public void setBackground(Color c)
	{
		super.setBackground(c);
		message.setBackground(c);
		messages.setBackground(c);
		send.setBackground(c);
	}
}
