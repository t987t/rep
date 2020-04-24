 //DECALRE LIBRARIES NEEDED FOR CLIENT OPERATIONS
import java.io.*;
import java.net.*;
import java.util.*;
//DECLARE LIBARRIES NEEDED FOR GUI FUNCTIONS
import java.awt.*;
import javax.swing.*;

public class EMailClient
{
	//DECLARE VARIABLES USED BY CLIENT
	private static InetAddress host;
	private static final int PORT = 1234;
	private static Socket link;
	private static Vector inbox;
	private static Vector backupInbox;
	private static Vector userNames;
	private static User user;
	private static ObjectInputStream objectIn;
	private static ObjectOutputStream objectOut;

	public static void main(String[] args) throws IOException
	{
		try
		{
			//SET UP CONNECTION INFORMATION
			host = InetAddress.getLocalHost();
			link = new Socket(host, PORT);
			//SET UP CLIENT DATA STORAGE
			userNames = new Vector();
			inbox = new Vector();
			backupInbox = new Vector();
			//GET PERMITTED USER NAMES FROM SERVER
			getUserNamesFromServer();
			//LAUNCH LOGIN GUI
			MainLoginMenuGUI mainLoginMenuGUI = new MainLoginMenuGUI();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 300;
			final int WIDTH = 300;
			mainLoginMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
							           ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			mainLoginMenuGUI.setVisible(true);
		}
		//EXCEPTION HANDLING ROUTINES AND CLASSES
		catch(UnknownHostException uhe)
		{
			System.out.println("Host ID Not Found");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	//RECIEVES VECTOR OF PERMITTED USER NAMES FROM SERVER
	private static void getUserNamesFromServer() throws IOException
	{
		try
		{
			//SET UP OBJECT INPUT STREAM FOR DATA RECEIPT
			objectIn = new ObjectInputStream(link.getInputStream());
			//GET SIZE OF USER NAMES VECTOR FROM SERVER
			Integer vectorSizeText = (Integer)objectIn.readObject();
			//RETRIEVE SIZE VIA TYPECAST TO PRIMITIVE
			int vectorSize = vectorSizeText.intValue();
			//EXTRTACT ALL USER NAMES FROM SERVER, ADD TO CLIENT VECTOR
			for(int count = 0; count < vectorSize; count++)
			{
				userNames.add((User)objectIn.readObject());
				User temp = (User)userNames.elementAt(count);
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}

	//SHUTS DOWN THE CLIENT CORRECTLY WHEN FINISHED
	public static void closeDown()
	{
		try
		{
			//SEND UNDELETED MAIL BACK TO SERVER FOR STORAGE
			for(int count = 0; count < inbox.size(); count++)
			{
				returnMail(getMail(count));
				//DELETE MAIL FROM INBOX
				inbox.removeElementAt(count);
				count--;
			}
			//SET UP OUTPUT STREAM FOR MAIL
			ObjectOutputStream objectOut = new ObjectOutputStream(link.getOutputStream());
			//SEND MESSAGE SERVER TO SHUT DOWN CONNECTION
			String option = "QUITTING";
			objectOut.writeObject(option);
			//CLEAR OUTPUT BUFFER
			objectOut.flush();
			//CLOSE LINK
			link.close();
			//SHUT DOWN SYSTEM
			System.exit(0);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	//SENDS ANY MAIL IN CLIENTS INBOX BACK TO SERVER(USUALLY NONE)
	private static void returnMail(Email mail)
	{
		try
		{
			//SET UP NEW OBJECT OUTPUT STREAM FOR SENDING MAIL BACK TO SERVER
			ObjectOutputStream objectOut = new ObjectOutputStream(link.getOutputStream());
			//SEND SERVER CORRECT INSTRUCTION
			String option = "SENDING";
			//SEND OPTION, MAIL THEN FLUSH BUFFER
			objectOut.writeObject(option);
			objectOut.flush();
			objectOut.writeObject(mail);
			objectOut.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	//CREATES A COPY OF CLIENTS INBOX FOR UNDELETION
	public static void backupMail()
	{
		//GET SIZE OF INBOX
		int vectorSize = inboxSize();
		//EMPTY PREVIOUS INBOX BACKUP
		backupInbox.removeAllElements();
		//COPY EACH INBOX ITEM TO BACKUP
		for(int count = 0; count < vectorSize; count++)
		{
			Email temp = getMail(count);
			backupInbox.add(temp);
		}
	}

	//RESTORES A COPY OF INBOX FOR UNDELETION
	public static void restoreMail()
	{
		//GET SIZE OF BACKUP
		int vectorSize = backupInbox.size();
		//EMPTY CURRENT INBOX
		inbox.removeAllElements();
		//COPY EACH ITEM FROM BACKUP TO INBOX
		for(int count = 0; count < vectorSize; count++)
		{
			Email temp = (Email)backupInbox.elementAt(count);
			inbox.add(temp);
		}
	}

	//ACCESSOR METHODS
	public static String getCurrentUserName()
	{
		return user.getUserName();
	}
	public static Vector getUserNames()
	{
		return userNames;
	}
	public static int getUserNamesSize()
	{
		return userNames.size();
	}
	public static String getName(int position)
	{
		User temp = (User)userNames.elementAt(position);
		return temp.getUserName();
	}
	public static Email getMail(int position)
	{
		return (Email)inbox.elementAt(position);
	}
	public static Socket getLink()
	{
		return link;
	}
	public static Vector getMailVector()
	{
		return inbox;
	}
	public static Vector getBackupVector()
	{
		return backupInbox;
	}
	public static int inboxSize()
	{
		return inbox.size();
	}

	//MUTATOR METHODS
	public static void setUser(User newUser)
	{
		user = newUser;
	}
	public static void addUser(User newUser)
	{
		userNames.add(newUser);
	}
	public static void addMail(Email email)
	{
		inbox.add(email);
	}
	public static void setBackupVector(Vector vector)
	{
			backupInbox = vector;
	}
	public static void setInbox(Vector vector)
	{
		inbox = vector;
	}
	public static void deleteMail(int position)
	{
		inbox.removeElementAt(position);
	}

	public static void deleteAll()
	{
		inbox.removeAllElements();
	}
}
