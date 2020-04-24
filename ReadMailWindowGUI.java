//IMPORT LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//IMPORT LIBRARIES NEEDED FOR STREAMS AND INPUT
import java.io.*;

class ReadMailWindowGUI extends JFrame implements ActionListener
{
	//DECLARE COMPONENTS NEEDED FOR GUI
	private ImageIcon mailPic;
	private JLabel labFrom;
	private JLabel labSubject;
	private JLabel labTo;
	private JLabel labAttachment;
	private JLabel labPic;
	private JButton viewAttachment;
	private JButton saveAttachment;
	private JButton forward;
	private JButton changeFont;
	private JPanel buttonPanel;
	private JPanel headerPanel;
	private JPanel headerPanelInfo;
	private JPanel headerPanelGraphic;
	private JPanel messagePanel;
	private static JTextField textFrom;
	private static JTextField textSubject;
	private static JTextField textTo;
	private static JTextField textAttachment;
	private static JTextArea textMessage;
	private Container pane;
	//DECLARE EMAIL THAT IS RECEIVED AS PARAMETER
	private Email email;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///ReadMailWindowGUI readMailWinGUI = new ReadMailWindowGUI(email);
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 600;
	///final int WIDTH = 600;
	///readMailWindowGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///						((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///readMailWindowGUI.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR GUI
	public ReadMailWindowGUI(Email email)
	{
		//SET RECIEVED EMAIL AS CLASS EMAIL
		this.email = email;
		//SET WINDOW TITLE
		setTitle("Read E-Mail");
		//CREATE NEW ICON FOR GUI
		mailPic = new ImageIcon("Envelope.gif");
		//CREATE GUI LABELS
		labFrom = new JLabel("FROM:");
		labSubject = new JLabel("SUBJECT:");
		labTo = new JLabel("TO:");
		labAttachment = new JLabel("ATTACHMENT:");
		labPic = new JLabel(mailPic);
		//CREATE TEXT FIELDS FOR GUI
		textFrom = new JTextField(15);
		textSubject = new JTextField(15);
		textTo = new JTextField(15);
		textAttachment = new JTextField(15);
		textMessage = new JTextArea(30, 49);
		textFrom.setEditable(false);
		textSubject.setEditable(false);
		textTo.setEditable(false);
		textAttachment.setEditable(false);
		textMessage.setEditable(false);
		textFrom.setText(email.getSender());
		textSubject.setText(email.getSubject());
		textTo.setText(email.getRecipient());
		textAttachment.setText(email.getAttachmentName());
		textMessage.setText(email.getContent());
		//CREATE NEW BUTTONS FOR GUI
		viewAttachment = new JButton("View Attachment");
		saveAttachment = new JButton("Save Attachment");
		forward = new JButton("Forward Email");
		changeFont = new JButton("Change Display Font");
		//CREATE PANELS FOR GUI
		buttonPanel = new JPanel();
		messagePanel = new JPanel();
		headerPanel = new JPanel();
		headerPanelInfo = new JPanel();
		headerPanelGraphic = new JPanel();
		//CREATE CONTENT PANE
		pane = getContentPane();
		//SET UP PANEL LAYOUTS
		buttonPanel.setLayout(new GridLayout(1,4));
		messagePanel.setLayout(new FlowLayout());
		headerPanelInfo.setLayout(new GridLayout(4,2));
		headerPanelGraphic.setLayout(new BorderLayout());
		headerPanel.setLayout(new BorderLayout());
		pane.setLayout(new BorderLayout());
		//ADD COMPONENTS TO BUTTON PANEL
		buttonPanel.add(viewAttachment);
		buttonPanel.add(saveAttachment);
		buttonPanel.add(forward);
		buttonPanel.add(changeFont);
		//ADD COMPONENTS TO MESSAGE PANEL
		messagePanel.add(textMessage);
		//ADD COMPONENTS TO HEADER INFO PANEL
		headerPanelInfo.add(labFrom);
		headerPanelInfo.add(textFrom);
		headerPanelInfo.add(labTo);
		headerPanelInfo.add(textTo);
		headerPanelInfo.add(labSubject);
		headerPanelInfo.add(textSubject);
		headerPanelInfo.add(labAttachment);
		headerPanelInfo.add(textAttachment);
		//ADD COMPOENENTS TO HEADER GRAPHICS PANEL
		headerPanelGraphic.add(labPic, BorderLayout.CENTER);
		//ADD PANELS TO HEADER PANEL
		headerPanel.add(headerPanelInfo, BorderLayout.WEST);
		headerPanel.add(headerPanelGraphic, BorderLayout.EAST);
		//ADD PANELS TO CONTAINER PANE
		pane.add(messagePanel, BorderLayout.CENTER);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		pane.add(headerPanel, BorderLayout.NORTH);
		//ADD ACTION LISTENERS TO BUTTONS
		viewAttachment.addActionListener(this);
		saveAttachment.addActionListener(this);
		forward.addActionListener(this);
		changeFont.addActionListener(this);
	}

	//ACTION LISTENERS FOR BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		//IF VIEW ATTACHMENT BUTTON
		if(e.getSource() == viewAttachment)
		{
			//GET NAME OF ATTCHMENT
			String name = textAttachment.getText();
			//CONVERT NAME TO LOWER CASE
			name = name.toLowerCase();
			//IF TEXT FILE
			if(name.endsWith(".txt"))
			{
				//CREATE NEW VIEW TEXT ATTACHMENT GUI
				ViewTextAttachment attachment = new ViewTextAttachment(email.getAttachment(), email.getAttachmentName());
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 600;
				final int WIDTH = 600;
				attachment.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
									 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				attachment.setVisible(true);
			}
			//IF GRAPHICS FILE
			if(name.endsWith(".gif") || name.endsWith(".jpg") || name.endsWith(".jpeg"))
			{
				//CREATE NEW GRAPHICS ATTACHMENT GUI
				ViewGraphicsAttachment attachment = new ViewGraphicsAttachment(email.getAttachment());
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 800;
				final int WIDTH = 800;
				attachment.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
					    			 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				attachment.setVisible(true);
			}
			//IF AUDIO/VIDEO FILE
			if(name.endsWith(".mov") || name.endsWith(".au") || name.endsWith(".mpeg") || name.endsWith(".mpg"))
			{
				//CREATE NEW MEDIA ATTACHMENT GUI
				ViewMediaAttachment attachment = new ViewMediaAttachment(email.getAttachment(), email.getAttachmentName());
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 600;
				final int WIDTH = 600;
				attachment.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
					        		 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				attachment.setVisible(true);
			}
		}
		//IF SAVE ATTACHMENT
		if(e.getSource() == saveAttachment)
		{
			if(textAttachment.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "No Attachment To Save");
			}
			else
			{
				//CREATE NEW FILE CHOOSER
				JFileChooser chooser = new JFileChooser();
				//SELECT CHOOSE METHOD
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//GET RESULT OF CHOICE
				int result = chooser.showSaveDialog(this);
				//IF CANCELLED CLOSE WINDOW
				if(result == JFileChooser.CANCEL_OPTION)
					return;
				//CREATE NEW FILE OBJECT FROM SELECTION
				File temp = chooser.getSelectedFile();
				//CHECK THAT VALID FILE
				if(temp.getName().equals(""))
				{
					//DISPLAY MESSAGE DIALOG
					JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					try
					{
						//CREATE NEW FILE OUPTUT STREAM
						FileOutputStream fileOut = new FileOutputStream(temp);
						//WRITE ATTACHMENT TO EMAIL
						fileOut.write(email.getAttachment());
						//CLOSE FILESTREAM
						fileOut.close();
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
					}
				}
			}
		}
		//IF FORWARD EMAIL
		if(e.getSource() == forward)
		{
			try
			{
				//CREATE NEW SEND MAIL WINDOW
				SendMailWindowGUI sendMailWinGUI = new SendMailWindowGUI(email);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 600;
				final int WIDTH = 600;
				sendMailWinGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
									     ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				sendMailWinGUI.setVisible(true);
				setVisible(false);
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		//IF CHANGE FONT
		if(e.getSource() == changeFont)
		{
			//CREATE FONT CHANGE GUI
			FontChange change = new FontChange();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			final int HEIGHT = 70;
			final int WIDTH = 300;
			change.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
					    	 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
			change.setVisible(true);
		}
	}
	//SET EMAIL FONT TO NEW FONT
	public static void setNewFont(Font font)
	{
		textFrom.setFont(font);
		textSubject.setFont(font);
		textTo.setFont(font);
		textAttachment.setFont(font);
		textMessage.setFont(font);
	}
}


