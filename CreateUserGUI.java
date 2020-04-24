//IMPORT LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//IMPORT LIBRARY NEEDED FOR VECTOR
import java.util.*;
//IMPORT LIBRARY NEEDED FOR STREAMS
import java.io.*;

public class CreateUserGUI extends JFrame implements ActionListener
{
	//DECLARE PANELS NEEDED FOR GUI
	private JPanel buttonPanel;
	private JPanel inputPanel;
	//DECLARE BUTTONS NEEDED FOR GUI
	private JButton create;
	//DECLARE LABELS NEEDED FOR GUI
	private JLabel labelPassword;
	private JLabel labelUsername;
	//DECLARE FIELDS NEEDED FOR GUI
	private JPasswordField textPassword;
	private JTextField textUsername;
	//DECLARE CONTAINER NEEDED FOR GUI
	private Container pane;
	//DECLARE SOCKET AND OUTPUT STREAM FOR USER
	private static ObjectOutputStream objectOut;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///CreateUserGUI create = new CreateUserGUI();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 100;
	///final int WIDTH = 300;
	///create.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///				    ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///create.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR GUI
	public CreateUserGUI()
	{
		//ADD WINDOW LISTENER TO DETECT CLOSING APPLICATION
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//SHUT DOWN CLIENT CORRECTLY
					EMailClient.closeDown();
				}
			}
		);
		//SET TITLE OF GUI
		setTitle("Create New User");
		//INITIALISE CONTENT PANE AND SET UP LAYOUT
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		//INITIALISE PANELS FOR GUI
		buttonPanel = new JPanel();
		inputPanel = new JPanel();
		//INITIALISE BUTTONS FOR GUI
		create = new JButton("Create New User");
		//INITIALISE LABELS FOR GUI
		labelPassword = new JLabel("Password");
		labelUsername = new JLabel("Username");
		//INITIALISE FIELDS FOR GUI
		textPassword = new JPasswordField(10);
		textUsername = new JTextField(10);
		//SET UP LAYOUTS FOR PANELS
		buttonPanel.setLayout(new GridLayout(1,1));
		inputPanel.setLayout(new GridLayout(2,2));
		//ADD GUI COMPONENTS TO PANELS
		buttonPanel.add(create, BorderLayout.CENTER);
		inputPanel.add(labelUsername);
		inputPanel.add(textUsername);
		inputPanel.add(labelPassword);
		inputPanel.add(textPassword);
		//ADD PANELS TO CONTENT PANE OF GUI
		pane.add(inputPanel, BorderLayout.NORTH);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		//ADD ACTION LISTENER FOR BUTTONS
		create.addActionListener(this);
	}

	//ACTION LISTENER FOR GUI BUTTON
	public void actionPerformed(ActionEvent e)
	{
		//SPECIFY ACTION TO BE TAKEN ON BUTTON PRESS
		//DECLARE VARIABLES LOCAL TO METHOD
		String userName = textUsername.getText();
		String password = new String(textPassword.getPassword());
		//CHECK IF USER HAS ENTERED A VALID USERNAME AND PASSWORD
		boolean userExists = checkUserExists(userName, password);
		//IF VALID USERNAME LOG INTO SYSTEM
		if(userExists == false)
		{
			sendUserNameToServer(userName, password);
			sendUserNameToClient(userName, password);
			JOptionPane.showMessageDialog(pane, "New User Accepted");
			setVisible(false);
			//CREATE FUNCTION MENU GUI AND DISPLAY
			MainFunctionMenuGUI mainFunctionMenuGUI = new MainFunctionMenuGUI();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 350;
			final int WIDTH = 500;
			mainFunctionMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
								          ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			mainFunctionMenuGUI.setVisible(true);
		}
		else
		{
			//USERNAME ALREADY USED, DISPLAY WARNING CLEAR FIELDS
			JOptionPane.showMessageDialog(pane, "Sorry User Already Exists");
			textUsername.setText("");
			textPassword.setText("");
		}
	}

	//CHECKS IF A USER ALREADY EXISTS ON SERVER
	public static boolean checkUserExists(String userName, String password)
	{
		//CHECKS WHETHER OR NOT A USER EXISTS ON SERVER
		//MAKE A COPY OF USERNAMES FROM SERVER
		Vector userNames = EMailClient.getUserNames();
		//DECLARE VARIABLES LOCAL TO METHOD
		int vectorSize = userNames.size();
		int vectorPosition;
		String extractedUser;
		String extractedPassword;
		boolean exists = false;
		//SEARCH THROUGH VECTOR TO CHECK ALL USERNAMES
		for(vectorPosition = 0; vectorPosition < vectorSize; vectorPosition++)
		{
			//RETRIEVE FIRST USER OBJECT FROM VECTOR
			User temp = (User)userNames.elementAt(vectorPosition);
			//EXTRACT USERNAME AND PASSWORD FROM USER OBJECT
			extractedUser = temp.getUserName();
			extractedPassword = temp.getPassword();
			//CHECK IF EXTRACTED INFO MATCHES PASSED IN INFO
			if(extractedUser.equals(userName))
			{
				//IF USER EXISTS SET TO TRUE AND QUIT LOOP
				exists = true;
				break;
			}
		}
		//RETURN WHETHER USER EXISTS OR NOT
		return exists;
	}

	//SENDS THE NEWLY CREATED USERS DETAILS TO SERVER
	private static void sendUserNameToServer(String userName, String password)
	{
		try
		{
			//SET USER STATUS TO TRUE FOR NEW USER
			Boolean userStatus = Boolean.TRUE;
			//SET UP OBJECT OUTPUT STREAM
			objectOut = new ObjectOutputStream(EMailClient.getLink().getOutputStream());
			//CREATE NEW USER FROM ENTERED USER
			User user = new User(userName, password);
			//SEND INFORMATION TO SERVER
			objectOut.writeObject(user);
			objectOut.writeObject(userStatus);
			//FLUSH STREAM
			objectOut.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	//SETS THE USERS NAME ON THE CLIENT
	private void sendUserNameToClient(String userName, String password)
	{
		User user = new User(userName, password);
		EMailClient.setUser(user);
		EMailClient.addUser(user);
	}
}