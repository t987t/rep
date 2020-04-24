//INCLUDE LIBARRIES NEEDED FOR GUI SETUP
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//INCLUDE LIBRARIES NEEDED FOR OPERATION OF SERVER
import java.io.*;
import java.net.*;
import java.util.*;

public class EMailServer extends JFrame
{
	//DECLARE VARIABLES NEEDED FOR SERVER OPERATION
	private static ServerSocket servSocket;
	private static final int PORT = 1234;
	private static Vector userNames;
	private static Vector mail;
	private static ObjectOutputStream fileOut;
	private static ObjectInputStream fileIn;
	private static ObjectOutputStream objectOut;
	private static ObjectInputStream objectIn;
	//DECLARE COMPONENETS TO BE PLACED ON GUI
	private static JTextArea messageWindow;

	//CONSTRUCTOR FOR SERVER GUI
	public EMailServer()
	{
		//WINDOW CLOSING HANDLING CODE
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					shutDownServer();
				}
			}
		);
		//SET WINDOW TITLE
		setTitle("E - Mail Server Information");
		//INITIALISE COMPONENTS
		messageWindow = new JTextArea(28,30);
		//SET UP JTEXTAREAS PROPERTIES
		messageWindow.setWrapStyleWord(true);
		messageWindow.setLineWrap(true);
		messageWindow.setEditable(false);
		//ADD COMPONENTS TO CONTENT PANEL
		getContentPane().add(messageWindow, BorderLayout.NORTH);
		//ADD SCROLL BAR TO SERVER WINDOW
		getContentPane().add(new JScrollPane(messageWindow));
	}

	//MAIN METHOD FOR SERVER
	public static void main(String[] args) throws IOException
	{
		//CREATE NEW INSTANCE OF SERVER STORAGE VECTORS
		userNames = new Vector();
		mail = new Vector();
		//CREATE NEW INSTANCE OF SERVER GUI TO DISPLAY MESSAGES
		EMailServer serverGUI = new EMailServer();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int HEIGHT = 500;
		final int WIDTH = 400;
		serverGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
							((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
		serverGUI.setVisible(true);
		//STARTS THE SERVER
		startUpServer();
		//SET UP SOCKET TO ACCEPT CONNECTIONS
		try
		{
			servSocket = new ServerSocket(PORT);
		}
		catch (IOException ioe)
		{
			//ON ERROR DISPLAY APPROPRIATE MESSAGE AND SHUT DOWN WITH ERROR CODE
			updateMessageWindow("Unable To Set Up Port");
			System.exit(1);
		}
		do
		{
			//ACCEPT NEW CLIENT
			Socket client = servSocket.accept(); //Wait for client.
			//UPDATE SERVER WINDOW DETAILS
			updateMessageWindow("Accepting Incoming Connection...");
			//ALLOCATE NEW CLIENT HANDLER THREAD
			ClientHandler handler = new ClientHandler(client);
			handler.start();
		}while (true); //RUN INDEFINATELY
	}

	//STARTS UP THE SERVER IN THE CORRECT MANNER
	private static void startUpServer()
	{
		try
		{
			//EXTRACT INFORMATION FOR SERVER FROM FILES
			readInUserNamesFromServerFile();
			readInMailFromServerFile();
		}
		catch(IOException ioe)
		{
			updateMessageWindow("Error Reading From File");
		}
	}

	//GETS A VECTOR OF USER NAMES FROM PERSISTENT STORAGE
	private static void readInUserNamesFromServerFile() throws IOException
	{
		//SET UP FILE INPUT STREAM FROM PERSISTENT SERVER FILE
		fileIn = new ObjectInputStream(new FileInputStream("Usernames.dat"));
		try
		{
			do
			{
				//READ OBJECT FROM FILE AND ADD TO SERVER VECTOR
				User temp = (User)fileIn.readObject();
				addUser(temp);
			}while(true);//DO UNTIL EOF EXCEPTION OCCURS
		}
		catch(EOFException eofe)
		{
			//DISPLAY MESSAGE AND CLOSE STREAM
			updateMessageWindow("User Names Read Succesfully");
			fileIn.close();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}

	}

	//GETS A VECTOR OF EMAILS FROM PERSISTENT STORAGE
	private static void readInMailFromServerFile() throws IOException
	{
		//SET UP FILE INPUT STREAM FROM PERSISTENT SERVER FILE
		fileIn = new ObjectInputStream(new FileInputStream("Mail.dat"));
		try
		{
			do
			{
				//READ OBJECT FROM FILE AND ADD TO SERVER VECTOR
				Email temp = (Email)fileIn.readObject();
				addMail(temp);
			}while(true);//DO UNTIL EOF EXCEPTION OCCURS
		}
		catch(EOFException eofe)
		{
			//DISPLAY MESSAGE AND CLOSE STREAM
			updateMessageWindow("Mail Read Succesfully");
			fileIn.close();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}

	//SHUTS DOWN THE SERVER IN THE CORRECT MANNER
	private static void shutDownServer()
	{
		try
		{
			//OUTPUT SERVER INFORMATION TO PERSISTENT STORAGE
			writeOutMailToServerFile();
			writeOutUsersToServerFile();
		}
		catch(IOException ioe)
		{
			updateMessageWindow("Error Writing To File");
		}
		System.exit(0);
	}

	//SENDS ALL SERVER USERNAMES TO FILE
	private static void writeOutUsersToServerFile() throws IOException
	{
		//SET UP NEW FILE OUTPUT STREAM
		fileOut = new ObjectOutputStream(new FileOutputStream("Usernames.dat"));
		//GET SIZE OF USERNAMES VECTOR
		int vectorSize = getUserNamesVectorSize();
		//IF VECTOR HAS MORE THAN ZERO USERS
		if(vectorSize > 0)
		{
			//UPDATE SERVER MESSAGE WINDOW
			updateMessageWindow("Adding Users To Permament Storage");
			//ADD EACH USER TO FILE
			for(int count = 0; count < vectorSize; count++)
			{
				User user = (User)getUser(count);
				fileOut.writeObject(user);
			}
			//CLOSE FILE OUPTUT STREAM
			fileOut.close();
		}
		else
		{
			//DISPLAY APPROPRIATE SERVER MESSAGE
			updateMessageWindow("No Current Users!");
		}
	}

	//SEND ALL SERVER MAIL TO A FILE
	private static void writeOutMailToServerFile() throws IOException
	{
		//SET UP NEW FILE OUTPUT STREAM
		fileOut = new ObjectOutputStream(new FileOutputStream("Mail.dat"));
		//GET SIZE OF MAIL VECTOR
		int vectorSize = getMailSize();
		//IF VECTOR HAS MORE THAN ZERO MAILS
		if(vectorSize > 0)
		{
			//UPDATE SERVER MESSAGE WINDOW
			updateMessageWindow("Adding Mail To Permament Storage");
			//ADD EACH MAIL TO FILE
			for(int count = 0; count < vectorSize; count++)
			{
				Email mail = (Email)getEmail(count);
				fileOut.writeObject(mail);
			}
			//CLOSE FILE OUTPUT STREAM
			fileOut.close();
		}
		else
		{
			//DISPLAY APPROPRIATE SERVER MESSAGE
			updateMessageWindow("No Current Mail!");
		}
	}

	//ACCESSOR METHODS
	public static User getUser(int position)
	{
		return (User)userNames.elementAt(position);
	}
	public static int getUserNamesVectorSize()
	{
		return userNames.size();
	}
	public static Email getEmail(int position)
	{
		return (Email)mail.elementAt(position);
	}
	public static int getMailSize()
	{
		return mail.size();
	}

	//MUTATOR METHODS
	public static void addUser(User newUser)
	{
		userNames.add(newUser);
	}
	public static void addMail(Email newMail)
	{
		mail.add(newMail);
	}
	public static void deleteMail(int position)
	{
		mail.removeElementAt(position);
	}
	public static void updateMessageWindow(String info)
	{
		messageWindow.setText(messageWindow.getText() + "\n" + info);
	}
}








