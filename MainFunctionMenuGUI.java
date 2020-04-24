//IMPORT LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//IMPORT LIBRARIES NEEDED FOR STREAMS
import java.io.*;

class MainFunctionMenuGUI extends JFrame implements ActionListener
{
	//DECLARE COMPONENTS NEEDED FOR GUI
	private JButton read;
	private JButton send;
	private JLabel title;
	private JLabel messageInfo;
	private ImageIcon readIcon;
	private ImageIcon sendIcon;
	private JPanel buttonPanel;
	private JPanel bottomPanel;
	private JPanel topPanel;
	private Container pane;
	//DECLARE STREAMS NEEDED FOR TRANSMISSION
	private static ObjectInputStream objectIn;
	private static ObjectOutputStream objectOut;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///MainFunctionMenuGUI mainFunctionMenuGUI = new MainFunctionMenuGUI();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 350;
	///final int WIDTH = 500;
	///mainFunctionMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///								 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///mainFunctionMenuGUI.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR GUI
	public MainFunctionMenuGUI()
	{
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//CLOSE DOWN CLIENT CORRECTLY
					EMailClient.closeDown();
				}
			}
		);

		addWindowListener(
			new WindowAdapter()
			{
				public void windowOpened(WindowEvent e)
				{
					try
					{
						//RECEIVE MAIL FROM SERVER
						recieveMessagesFromServer();
						//BACK UP MAIL FOR UNDELETING
						EMailClient.backupMail();
					}
					catch (ClassNotFoundException cnfe)
					{
						cnfe.printStackTrace();
					}
				}
			}
		);
		//SET GUI TITLE
		setTitle("E-Mail System Main Menu");
		//CREATE NEW ICONS FROM FILE
		readIcon = new ImageIcon("Mailbrd.gif");
		sendIcon = new ImageIcon("Airmail.gif");
		//CREATE NEW BUTTONS WITH ICONS
		read = new JButton("Read Email", readIcon);
		send = new JButton("Send Email", sendIcon);
		//SET ICON DISPLAY POSITION
		read.setVerticalTextPosition(SwingConstants.BOTTOM);
		read.setHorizontalTextPosition(SwingConstants.CENTER);
		send.setVerticalTextPosition(SwingConstants.BOTTOM);
    	send.setHorizontalTextPosition(SwingConstants.CENTER);
		//SET TITLES FOR GUI
		title = new JLabel("Please Make Your Selection");
		title.setFont(new Font("Arial", Font.BOLD, 25));
		messageInfo = new JLabel("You Have 0 Messages");
		messageInfo.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20));
		//CREATE PANELS FOR GUI
		buttonPanel = new JPanel();
		bottomPanel = new JPanel();
		topPanel = new JPanel();
		//SET UP CONTAINER PANE AND LAYOUTS
		pane = getContentPane();
		pane.setLayout(new FlowLayout());
		buttonPanel.setLayout(new FlowLayout());
		bottomPanel.setLayout(new GridLayout(1,1));
		topPanel.setLayout(new GridLayout(1,1));
		//ADD COMPONENTS TO PANELS
		buttonPanel.add(read);
		buttonPanel.add(send);
		topPanel.add(title);
		bottomPanel.add(messageInfo);
		//ADD PANELS TO PANE
		pane.add(topPanel);
		pane.add(buttonPanel);
		pane.add(bottomPanel);
		//ADD ACTION LISTENERS FOR BUTTONS
		read.addActionListener(this);
		send.addActionListener(this);
	}

	//ACTION LISTENERS FOR GUI BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			if(e.getSource() == send)
			{
				//CREATE GUI FOR SENDING MAIL
				SendMailWindowGUI sendMailWinGUI = new SendMailWindowGUI();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 600;
				final int WIDTH = 600;
				sendMailWinGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
									     ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				sendMailWinGUI.setVisible(true);
				setVisible(false);//HIDE THIS WINDOW
			}
			if(e.getSource() == read)
			{
				//CREATE GUI FOR READING MAIL
				InboxList inbox = new InboxList();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 550;
				final int WIDTH = 600;
				inbox.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
					            ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				inbox.setVisible(true);
				setVisible(false);//HIDE THIS WINDOW
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}

	}

	//RECEIVE CLIENTS MAIL FROM SERVER
	private void recieveMessagesFromServer() throws ClassNotFoundException
	{
		try
		{
			//SET UP STREAM FOR OBJECT OUTPUT
			objectOut = new ObjectOutputStream(EMailClient.getLink().getOutputStream());
			//SEND MESSAGE TO SERVER
			String command = "RECIEVING";
			objectOut.writeObject(command);
			objectOut.flush();
			//SET UP STREAM FOR RECIEVING OBJECTS
			objectIn = new ObjectInputStream(EMailClient.getLink().getInputStream());
			//GET INSTRUCTION FROM SERVER
			String option = (String)objectIn.readObject();
			//GET ALL MAIL FROM SERVER
			while(option.equals("SENDING"))
			{
				Email email = (Email)objectIn.readObject();
				EMailClient.addMail(email);
				option = (String)objectIn.readObject();
			}
			//UPDATE MAIL RECIEVED LABEL
			messageInfo.setText("You Have " + EMailClient.inboxSize() + " Messages");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}



