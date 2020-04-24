//IMPORT STANDARD GUI PACKAGES
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//IMPORT NEEDED FOR VECTORS AND STREAMS
import java.util.*;
import java.io.*;

class LoginWindowGUI extends JFrame implements ActionListener
{
	//DECLARE PANELS USED IN GUI
	private JPanel buttonPanel;
	private JPanel inputPanel;
	//DECLARE BUTTONS USED IN GUI
	private JButton login;
	//DECLARE LABELS USED IN GUI
	private JLabel labelPassword;
	private JLabel labelUsername;
	//DECLARE INPUT FIELDS USED IN GUI
	private JPasswordField textPassword;
	private JTextField textUsername;
	//DECLARE GUI CONTAINER PANE
	private Container pane;
	//DECLARE VARAIABLE NEEDED FOR OUTPUT STREAM
	private static ObjectOutputStream objectOut;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///LoginWindowGUI login = new LoginWindowGUI();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 100;
	///final int WIDTH = 300;
	///login.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///	     	       ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///login.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//DECLARE CONSTRUCTOR USED FOR GUI
	public LoginWindowGUI()
	{
		//ADD WINDOW LISTENER FOR DETECTING WINDOW CLOSE
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
		//SET GUI TITLE
		setTitle("Login User");
		//SET UP CONTAINER PANE AND LAYOUT
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		//INITIALISE PANELS FOR GUI
		buttonPanel = new JPanel();
		inputPanel = new JPanel();
		//INITIALISE BUTTONS FOR GUI
		login = new JButton("Login");
		//INITIALISE LABELS FOR GUI
		labelPassword = new JLabel("Password");
		labelUsername = new JLabel("Username");
		//INITIALISE TEXT FIELDS FOR GUI
		textPassword = new JPasswordField(10);
		textUsername = new JTextField(10);
		//SET UP LAYOUTS FOR PANELS
		buttonPanel.setLayout(new GridLayout(1,1));
		inputPanel.setLayout(new GridLayout(2,2));
		//ADD COMPONENTS TO BUTTON PANEL
		buttonPanel.add(login, BorderLayout.CENTER);
		//ADD COMPONENENTS TO INPUT PANEL
		inputPanel.add(labelUsername);
		inputPanel.add(textUsername);
		inputPanel.add(labelPassword);
		inputPanel.add(textPassword);
		//ADD PANELS TO CONTAINER PANE
		pane.add(inputPanel, BorderLayout.NORTH);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		//ADD ACTION LISTENER FOR LOGIN BUTTON
		login.addActionListener(this);
	}

	//ACTION LISTENERS FOR GUI BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		//SPECIFY ACTION TO BE TAKEN ON BUTTON PRESS
		//DECLARE VARIABLES LOCAL TO METHOD
		String userName = textUsername.getText();
		String password = new String(textPassword.getPassword());
		//CHECK IF USER HAS ENTERED A VALID USERNAME AND PASSWORD
		boolean validUser = checkUserExists(userName, password);
		//IF VALID USERNAME LOG INTO SYSTEM
		if(validUser == true)
		{
			JOptionPane.showMessageDialog(pane, "Login Accepted");
			setVisible(false);
			//SEND USERNAME AND PASSWORD TO CLIENT
			sendUserNameToClient(userName, password);
			sendUserNameToServer(userName, password);
			//CREATE MAIN FUNCTION MENU GUI AND DISPLAY
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
			//INVALID USERNAME CLEAR FIELDS DISPLAY WARNING
			JOptionPane.showMessageDialog(pane, "Incorrect Login");
			textUsername.setText("");
			textPassword.setText("");
		}
	}

	//CHECK IF A PARTICULAR USER EXISTS
	public boolean checkUserExists(String userName, String password)
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
			if(extractedUser.equals(userName) && extractedPassword.equals(password))
			{
				//IF USER EXISTS SET TO TRUE AND QUIT LOOP
				exists = true;
				break;
			}
		}
		//RETURN WHETHER USER EXISTS OR NOT
		return exists;
	}

	//SEND LOGGED IN USERS NAME TO SERVER
	private void sendUserNameToServer(String userName, String Password)
	{
		try
		{
			//SET USER STATUS TO TRUE FOR EXISTING USER
			Boolean userStatus = Boolean.FALSE;
			//SET UP OBJECT OUTPUT STREAM
			objectOut = new ObjectOutputStream(EMailClient.getLink().getOutputStream());
			//CREATE NEW USER FROM ENTERED USER
			User user = new User(userName, Password);
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

	//SEND USERS NAME TO CLIENT
	private void sendUserNameToClient(String userName, String password)
	{
		User user = new User(userName, password);
		EMailClient.setUser(user);
	}
}

