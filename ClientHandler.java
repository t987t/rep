//IMPORT LIBRARIES NEEDED FOR THREAD HANDLING
import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler extends Thread
{
	//DECLARE VARIABLES USED IN CLASS
	private Socket client;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;

	//DECLARE CLASS CONSTRUCTOR
	public ClientHandler(Socket socket)
	{
		//GET SOCKET REFERENCE FROM SERVER
		client = socket;
	}

	//MULTITHREADED METHOD DEFINITION
	public synchronized void run()
	{
		try
		{
			//SEND USER NAMES TO CONNECTING CLIENT
			sendUserNamesToClient();
			//RECIEVE USER NAME FROM CLIENT AND SET THREAD NAME TO CLIENT NAME
			String user = getUserNameFromClient();
			setName(user);
			char operation;
			//WHILE CLIENT IS CONNECTED
			do
			{
				//SET UP INPUT STREAM AND READ INSTRUCTIONS
				objectIn = new ObjectInputStream(client.getInputStream());
				String option = (String)objectIn.readObject();
				operation = option.charAt(0);
				switch (operation)
				{
					case 'S': 	recieveMessagesFromClient();
								break;
					case 'R':	sendMessagesToClient();
								break;
					case 'Q':	EMailServer.updateMessageWindow("Disconnecting " + getName() + "...");
								break;
				}
			}while (operation != 'Q');
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		finally
		{
			try
			{
				//SHUT DOWN CLIENT CONNECTION CORRECTLY, DISPLAY MESSAGES
				EMailServer.updateMessageWindow("Closing down connection...");
				client.close();
				EMailServer.updateMessageWindow("Client Disconnected Succesfully");
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}

	//SEND SERVERS LIST OF USER NAMES TO CLIENT
	private synchronized void sendUserNamesToClient() throws IOException
	{
		//SET UP OBJECT OUTPUT STREAM, SERIALIZED
		objectOut = new ObjectOutputStream(client.getOutputStream());
		//GET AMOUNT OF USERS STORED IN SERVER
		int vectorSize = EMailServer.getUserNamesVectorSize();
		//SEND AMOUNT OF USERS CLIENT IS TO RECIEVE
		objectOut.writeObject(new Integer(vectorSize));
		//SEND ALL USER DETAILS TO CONNECTING CLIENT
		for(int count = 0; count < vectorSize; count++)
		{
			//SEND INDIVIDUAL USER FROM VECTOR
			objectOut.writeObject((User)EMailServer.getUser(count));
		}
		objectOut.flush();
	}

	//RECIEVE CORRECT LOGIN NAME FROM CLIENT OR NEW USER NAME
	private synchronized String getUserNameFromClient() throws IOException, ClassNotFoundException
	{
		//SET UP INPUT STREAM TO RECIEVE DATA FROM USER
		objectIn = new ObjectInputStream(client.getInputStream());
		//GET USER NAME VIA TYPECAST INTO USER CLASS
		User user = (User)objectIn.readObject();
		//GET WHETHER NEW USER OR CURRENT USER VIA TYPECAST
		Boolean objectNewUser = (Boolean)objectIn.readObject();
		//EXTRACT PRIMITIVE BOOLEAN VALUE FROM BOOLEAN OBJECT
		boolean newUser = objectNewUser.booleanValue();
		//UPDATE SERVER WINDOW WITH CONNECTED CLIENT INFORMATION
		EMailServer.updateMessageWindow("..." + user.getUserName() + " is now connected to server");
		//IF NEW USER ADD DETAILS TO SERVER STORAGE VECTOR
		if (newUser == true)
		{
			EMailServer.addUser(user);
		}
		//RETURN CURRENT USERS NAME AS STRING
		return (user.getUserName());
	}

	//RECIEVE MESSAGES FROM CLIENT
	private synchronized void recieveMessagesFromClient()
	{
		try
		{
			//RECIEVE EMAIL OBJECT FROM OBJECT INPUT STREAM VIA TYPECAST
			Email email = (Email)objectIn.readObject();
			//ADD MAIL TO SERVER VECTOR LIST
			EMailServer.addMail(email);
			//UPDATE EMAIL SERVER WINDOW WITH RELKEVANT DATA
			EMailServer.updateMessageWindow("Recieving Outgoing Mail From " + email.getSender());
			EMailServer.updateMessageWindow(email.getSender() + " Sending Mail To " + email.getRecipient());
			EMailServer.updateMessageWindow("Mail Recieved From " + email.getSender()
												+ " Stored In Outbox For Delivery To " + email.getRecipient());
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}

	//SEND MESSAGES TO CLIENT FROM SERVER
	private synchronized void sendMessagesToClient()
	{
		try
		{
			//SET UP OBJECT OUTPUT STREAM FOR TRANSMSISION
			objectOut = new ObjectOutputStream(client.getOutputStream());
			//UPDATE SERVER DISPLAY WITH CURRENT ACTION
			EMailServer.updateMessageWindow("Retrieving Messages For " + getName());
			int vectorSize = EMailServer.getMailSize();
			//SEARCH SERVER INBOX FOR CLIENTS MESSAGES
			for(int count = 0; count < vectorSize; count++)
			{
				if(getName().equals(EMailServer.getEmail(count).getRecipient()))
				{
					//SEND MESSAGES TO CONNECTED CLIENT
					objectOut.writeObject("SENDING");
					objectOut.flush();
					objectOut.writeObject(EMailServer.getEmail(count));
					objectOut.flush();
					//DELETE SENT MESSAGE FROM SERVER
					EMailServer.deleteMail(count);
					count--;
					vectorSize--;
				}
			}
			//SEND END TRANSMISSION INSTRUCTION
			objectOut.writeObject("END");
			objectOut.flush();
			//UPDATE SERVER DISPLAY WITH CURRENT ACTION
			EMailServer.updateMessageWindow("Messages sent to " + getName());
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}

