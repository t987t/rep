//IMPORT LIBRARIES FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//IMPORT LIBARIES FOR INPUT AND OUTPUT
import java.io.*;

class SendMailWindowGUI extends JFrame implements ActionListener
{
	//DECLARE COMPONENTS USED FOR GUI
	private ImageIcon mailPic;
	private JLabel labSubject;
	private JLabel labTo;
	private JLabel labAttachment;
	private JLabel labPic;
	private JButton sendAttachment;
	private JButton send;
	private JButton changeFont;
	private JButton multipleRecipients;
	private JPanel buttonPanel;
	private JPanel headerPanel;
	private JPanel headerPanelInfo;
	private JPanel headerPanelGraphic;
	private JPanel messagePanel;
	private Container pane;
	private static JTextField textSubject;
	private static JTextField textTo;
	private static JTextField textAttachment;
	private static JTextArea textMessage;
	private static JComboBox comboTo;
	//DECLARE OTHER VARIABLES USED FOR CLASS
	private String recipient;
	private String[] names;
	private static Object[] recipientNames;
	private boolean multiple = false;
	private Email email;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///SendMailWindowGUI sendMailWinGUI = new SendMailWindowGUI(email);
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 600;
	///final int WIDTH = 600;
	///sendMailWindowGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///						       ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///sendMailWindowGUI.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//DEFAULT CONSTRUCTOR FOR CLASS
	public SendMailWindowGUI() throws IOException
	{
		//CREATE NEW EMAIL TO STORE INFORMATION
		email = new Email();
		//CREATE NAMES ARRAY
		names = new String[EMailClient.getUserNamesSize()];
		//POPULATE NAMES ARRAY FROM CLIENT
		for(int count = 0; count < names.length; count++)
		{
			names[count] = EMailClient.getName(count);
		}
		//SET RECIPIENT TO FIRST NAME
		recipient = names[0];
		//ADD WINDOW LISTENER FOR WINDOW CLOSING
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//CREATE NEW FUNCTION MENU WINDOW
					MainFunctionMenuGUI mainFunctionMenuGUI = new MainFunctionMenuGUI();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					final int HEIGHT = 350;
					final int WIDTH = 500;
					mainFunctionMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
											      ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
					mainFunctionMenuGUI.setVisible(true);
				}
			}
		);
		//SET GUI TITLE
		setTitle("Send E-Mail");
		//CREATE NEW IMAGEICON
		mailPic = new ImageIcon("Stamp.gif");
		//CREATE LABELS FOR GUI
		labSubject = new JLabel("SUBJECT:");
		labTo = new JLabel("TO:");
		labAttachment = new JLabel("ATTACHMENT:");
		labPic = new JLabel(mailPic);
		//CREATE TEXT FIELDS FOR GUI
		textSubject = new JTextField(15);
		textAttachment = new JTextField(15);
		textMessage = new JTextArea(30, 49);
		textAttachment.setEditable(false);
		//CREATE NEW BUTTONS FOR GUI
		sendAttachment = new JButton("Send Attachment");
		send = new JButton("Send E-Mail");
		multipleRecipients = new JButton("Multiple Recipients");
		changeFont = new JButton("Change Display Font");
		//CREATE COMBO BOX FOR GUI
		comboTo = new JComboBox(names);
		//CREATE NEW PANELS FOR GUI
		buttonPanel = new JPanel();
		messagePanel = new JPanel();
		headerPanel = new JPanel();
		headerPanelInfo = new JPanel();
		headerPanelGraphic = new JPanel();
		//GET CONTENT PANE
		pane = getContentPane();
		//SET PANEL LAYOUTS
		buttonPanel.setLayout(new GridLayout(1,4));
		messagePanel.setLayout(new FlowLayout());
		headerPanelInfo.setLayout(new GridLayout(3,2));
		headerPanelGraphic.setLayout(new BorderLayout());
		headerPanel.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());
		//ADD COMPONENTS TO BUTTON PANEL
		buttonPanel.add(send);
		buttonPanel.add(sendAttachment);
		buttonPanel.add(multipleRecipients);
		buttonPanel.add(changeFont);
		//ADD COMPONENTS TO MESSAGE PANEL
		messagePanel.add(textMessage);
		//ADD COMPONENTS TO MESSAGE INFO PANEL
		headerPanelInfo.add(labTo);
		headerPanelInfo.add(comboTo);
		headerPanelInfo.add(labSubject);
		headerPanelInfo.add(textSubject);
		headerPanelInfo.add(labAttachment);
		headerPanelInfo.add(textAttachment);
		//ADD COMPONENTS TO HEADER GRAPHIC PANEL
		headerPanelGraphic.add(labPic, BorderLayout.CENTER);
		//ADD PANELS TO HEADER PANEL
		headerPanel.add(headerPanelInfo, BorderLayout.WEST);
		headerPanel.add(headerPanelGraphic, BorderLayout.EAST);
		//ADD PANELS TO CONTENT PANE
		pane.add(messagePanel, BorderLayout.CENTER);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		pane.add(headerPanel, BorderLayout.NORTH);
		//ADD ACTION LISTENERS TO BUTTONS
		send.addActionListener(this);
		sendAttachment.addActionListener(this);
		multipleRecipients.addActionListener(this);
		changeFont.addActionListener(this);
		//ADD ITEM LISTENER FOR COMBO BOX
		ComboHandler handler = new ComboHandler();
		comboTo.addItemListener(handler);
	}

	//OVERLOADED CONSTRUCTOR USED FOR WHEN FORWARDING MAIL
	public SendMailWindowGUI(Email email) throws IOException
	{
		//GET EMAIL FROM CLIENT
		this.email = email;
		email.setRead(false);
		//CREATE NAMES ARRAY
		names = new String[EMailClient.getUserNamesSize()];
		//POPULATE NAMES ARRAY FROM CLIENT
		for(int count = 0; count < names.length; count++)
		{
			names[count] = EMailClient.getName(count);
		}
		//SET RECIPIENT TO FIRST NAME
		recipient = names[0];
		//ADD WINDOW LISTENER FOR WINDOW CLOSING
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//CREATE NEW FUNCTION MENU WINDOW
					MainFunctionMenuGUI mainFunctionMenuGUI = new MainFunctionMenuGUI();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					final int HEIGHT = 350;
					final int WIDTH = 500;
					mainFunctionMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
							    				  ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
					mainFunctionMenuGUI.setVisible(true);
				}
			}
		);
		//SET GUI TITLE
		setTitle("Send E-Mail");
		//CREATE NEW IMAGEICON
		mailPic = new ImageIcon("Stamp.gif");
		//CREATE LABELS FOR GUI
		labSubject = new JLabel("SUBJECT:");
		labTo = new JLabel("TO:");
		labAttachment = new JLabel("ATTACHMENT:");
		labPic = new JLabel(mailPic);
		//CREATE TEXT FIELDS FOR GUI
		textSubject = new JTextField(15);
		textAttachment = new JTextField(15);
		textMessage = new JTextArea(30, 53);
		textAttachment.setEditable(false);
		textSubject.setText(email.getSubject());
		textAttachment.setText(email.getAttachmentName());
		textMessage.setText(email.getContent());
		//CREATE NEW BUTTONS FOR GUI
		sendAttachment = new JButton("Send Attachment");
		send = new JButton("Send E-Mail");
		multipleRecipients = new JButton("Multiple Recipients");
		changeFont = new JButton("Change Display Font");
		//CREATE COMBO BOX FOR GUI
		comboTo = new JComboBox(names);
		//CREATE NEW PANELS FOR GUI
		buttonPanel = new JPanel();
		messagePanel = new JPanel();
		headerPanel = new JPanel();
		headerPanelInfo = new JPanel();
		headerPanelGraphic = new JPanel();
		//GET CONTENT PANE
		pane = getContentPane();
		//SET PANEL LAYOUTS
		buttonPanel.setLayout(new GridLayout(1,4));
		messagePanel.setLayout(new FlowLayout());
		headerPanelInfo.setLayout(new GridLayout(3,2));
		headerPanelGraphic.setLayout(new BorderLayout());
		headerPanel.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());
		//ADD COMPONENTS TO BUTTON PANEL
		buttonPanel.add(send);
		buttonPanel.add(sendAttachment);
		buttonPanel.add(multipleRecipients);
		buttonPanel.add(changeFont);
		//ADD COMPONENTS TO MESSAGE PANEL
		messagePanel.add(textMessage);
		//ADD COMPONENTS TO MESSAGE INFO PANEL
		headerPanelInfo.add(labTo);
		headerPanelInfo.add(comboTo);
		headerPanelInfo.add(labSubject);
		headerPanelInfo.add(textSubject);
		headerPanelInfo.add(labAttachment);
		headerPanelInfo.add(textAttachment);
		//ADD COMPONENTS TO HEADER GRAPHIC PANEL
		headerPanelGraphic.add(labPic, BorderLayout.CENTER);
		//ADD PANELS TO HEADER PANEL
		headerPanel.add(headerPanelInfo, BorderLayout.WEST);
		headerPanel.add(headerPanelGraphic, BorderLayout.EAST);
		//ADD PANELS TO CONTENT PANE
		pane.add(messagePanel, BorderLayout.CENTER);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		pane.add(headerPanel, BorderLayout.NORTH);
		//ADD ACTION LISTENERS TO BUTTONS
		send.addActionListener(this);
		sendAttachment.addActionListener(this);
		multipleRecipients.addActionListener(this);
		changeFont.addActionListener(this);
		//ADD ITEM LISTENER FOR COMBO BOX
		ComboHandler handler = new ComboHandler();
		comboTo.addItemListener(handler);
	}

	//SET UP ITEM LISTENER FOR COMBO BOX
	private class ComboHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent iE)
		{
			//GET RECIPIENT NAME FROM COMBOBOX
			recipient = (names[comboTo.getSelectedIndex()]);
		}
	}

	//SET UP ACTION LISTENER FOR BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		//IF USER WANTS TO SEND EMAIL
		if(e.getSource() == send)
		{
			//CHECK FOR MULTIPLE ATTACHMENTS
			if(multiple == true)
			{
				//IF MULTIPLE ATTACHMENTS SEND MAIL TO EACH RECIPIENT
				for(int count = 0; count < recipientNames.length; count++)
				{
					//GET RECIPEINTS FROM ARRAY
					recipient = (String)recipientNames[count];
					sendEmail();
				}
				//DISPLAY MESSAGE TO USER
				JOptionPane.showMessageDialog(pane, "E-Mail Sent");
			}
			else
			{
				//SEND MAIL TO ONE RECIPIENT
				sendEmail();
				JOptionPane.showMessageDialog(pane, "E-Mail Sent");
			}
			//HIDE THIS WINDOW
			setVisible(false);
			MainFunctionMenuGUI mainFunctionMenuGUI = new MainFunctionMenuGUI();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 350;
			final int WIDTH = 500;
			mainFunctionMenuGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
										  ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			mainFunctionMenuGUI.setVisible(true);
		}
		//IF USER WANTS TO ATTACH FILE
		if(e.getSource() == sendAttachment)
		{
			//CREATE NEW FILE CHOOSER
			JFileChooser chooser = new JFileChooser();
			//SELECT TYPE OF FILE CHOOSER
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//GET RESULT OF FILE CHOOSER
			int result = chooser.showOpenDialog(this);
			//IF CANCEL RETURN TO SEND MAIL WINDOW
			if(result == JFileChooser.CANCEL_OPTION)
				return;
			//CREATE NEW FILE FROM CHOOSER SELECTION
			File temp = chooser.getSelectedFile();
			//IF NO FILE SELECTED
			if(temp == null || temp.getName().equals(""))
			{
				//DISPLAY ERROR MESSAGE
				JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				//ADD ATTACHMENT TO EMAIL
				email.setAttachment(temp);
				email.setAttachmentName(temp.getName());
				//UPDATE GUI
				textAttachment.setText(temp.getName());
			}
		}
		//IF USER SELECTED MULTILE ATTACHMENTS
		if(e.getSource() == multipleRecipients)
		{
			//CREATE NEW RECIPIENT LIST WINDOW
			RecipientList recipient = new RecipientList();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 475;
			final int WIDTH = 300;
			recipient.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
								((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			recipient.setVisible(true);
			//SET MULTIPLE RECIPIENTS TO TRUE
			multiple = true;
		}
		//IF USER SELECTED CHANGE DISPLAY FONT
		if(e.getSource() == changeFont)
		{
			FontChange change = new FontChange();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 70;
			final int WIDTH = 300;
			change.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
		    				 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			change.setVisible(true);
		}
	}

	//SETS RECIPIENT ARRAY WITH NAMES
	public static void setRecipientArray(Object[] temp)
	{
		recipientNames = temp;
	}

	//SENDS MAIL TO SERVER
	private void sendEmail()
	{
		try
		{
			//SET UP NEW OUTPUT STREAM
			ObjectOutputStream objectOut = new ObjectOutputStream(EMailClient.getLink().getOutputStream());
			//SEND INSTRUCTION TO SERVER
			String option = "SENDING";
			objectOut.writeObject(option);
			objectOut.flush();
			//SET UP EMAIL INFORMATION
			email.setSender(EMailClient.getCurrentUserName());
			email.setRecipient(recipient);
			email.setSubject(textSubject.getText());
			email.setContent(textMessage.getText());
			//SEND EMAIL TO SERVER
			objectOut.writeObject(email);
			objectOut.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	//CHANGE DISPLAY FONT
	public static void setNewFont(Font font)
	{
		textSubject.setFont(font);
		comboTo.setFont(font);
		textAttachment.setFont(font);
		textMessage.setFont(font);
	}
}



