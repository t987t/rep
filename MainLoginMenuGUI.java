//IMPORT JAVA GUI PACKAGES
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//IMPORT JAVA PACKAGES REQUIRED FOR OPERATIONS
import java.io.*;
import java.net.*;

public class MainLoginMenuGUI extends JFrame implements ActionListener
{
	//DECLARE PANELS USED IN GUI
	private JPanel buttonPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	//DECLARE BUTTONS USED IN GUI
	private JButton login;
	private JButton create;
	//DECLARE LABELS USED IN GUI
	private JLabel title;
	private JLabel picLabel;
	//DECLARE STARTUP IMAGE FOR GUI
	private ImageIcon javaMailPic;
	//DECLARE CONTAINER FOR GUI
	private Container pane;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///MainLoginMenuGUI mainLoginMenuGUI = new MainLoginMenuGUI();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 300;
	///final int WIDTH = 300;
	///mainLoginMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///						      ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///mainLoginMenuGUI.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUSTOR FOR GUI
	public MainLoginMenuGUI()
	{
		//SET UP WINDOW LISTENER FOR SHUT DOWN OF WINDOW
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
		//SET WINDOW TITLE
		setTitle("Email System Login Menu");
		//INITIALISE CONTAINER PANE
		pane = getContentPane();
		pane.setLayout(new FlowLayout());
		//INITIALISE PANELS
		buttonPanel = new JPanel();
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		//INITIALISE BUTTONS
		login = new JButton("Login Existing User");
		create = new JButton("Create New User");
		//INITIALISE LABELS AND ICONS
		javaMailPic = new ImageIcon("JavaMail.gif");
		title = new JLabel("Java Mail");
		picLabel = new JLabel(javaMailPic);
		//SET TITLE FONT SIZE
		title.setFont(new Font("Arial", Font.BOLD, 30));
		//SET UP PANEL LAYOUT AND ADD BUTTONS
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(login);
		buttonPanel.add(create);
		//SET UP PANEL LAYOUT AND ADD LABELS
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.add(title);
		//SET UP PANEL LAYOUT AND ADD PICTURE
		topPanel.setLayout(new BorderLayout());
		topPanel.add(picLabel, BorderLayout.NORTH);
		//ADD PANELS TO CONTAINER PANE
		pane.add(topPanel);
		pane.add(buttonPanel);
		pane.add(bottomPanel);
		//ADD BUTTON ACTION LISTENERS
		login.addActionListener(this);
		create.addActionListener(this);
	}

	//ACTION LISTENERS FOR GUI BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == login)
		{
			//CREATE NEW LOGIN USER GUI
			LoginWindowGUI login = new LoginWindowGUI();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 100;
			final int WIDTH = 300;
			login.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
					        ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			login.setVisible(true);
			setVisible(false);//HIDE MAIN LOGIN MENU
		}

		else
		{
			//CREATE NEW CREATE USER GUI
			CreateUserGUI create = new CreateUserGUI();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 100;
			final int WIDTH = 300;
			create.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
						     ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			create.setVisible(true);
			setVisible(false);//HIDE MAIN LOGIN MENU
		}
	}
}







