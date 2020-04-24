//IMPORT LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
//IMPORT LIBRARIES NEEDED FOR OPERATIONS
import java.util.*;
import java.io.*;

public class InboxList extends JFrame implements ActionListener
{
	//DECLARE PANELS NEEDED FOR GUI
	private JPanel buttonPanel;
	private JPanel listPanel;
	//DECLARE BUTTONS NEEDED FOR GUI
	private JButton read;
	private JButton delete;
	private JButton unRead;
	private JButton unDelete;
	//DECLARE LABELS NEEDED FOR GUI
	private JLabel title;
	//DECLARE FIELDS NEEDED FOR GUI
	private JList emailList;
	//DECLARE CONTAINER NEEDED FOR GUI
	private Container pane;
	//DECLARE VECTOR AND OUTPUT STREAM FOR USER
	private Vector mailDescriptor;
	private int selectedMailNumber;
	private ObjectInputStream fileIn;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///InboxList inbox = new InboxList();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 550;
	///final int WIDTH = 600;
	///inbox.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	/// 			   ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///inbox.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR GUI
	public InboxList()
	{
		//ADD WINDOW LISTENER TO DETECT CLOSING APPLICATION
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//OPEN NEW MAIN MENU ON CLOSING
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
		//SET TITLE OF GUI
		setTitle("Inbox");
		//INITIALISE CONTENT PANE AND SET UP LAYOUT
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		//INITIALISE PANELS FOR GUI
		buttonPanel = new JPanel();
		listPanel = new JPanel();
		//INITIALISE BUTTONS FOR GUI
		read = new JButton("Read Email");
		delete = new JButton("Delete Email");
		unRead = new JButton("Unread Email");
		unDelete = new JButton("Undelete Emails");
		//INITIALISE LABELS FOR GUI
		title = new JLabel("Recieved Emails");
		//INITIALISE FIELDS FOR GUI
		mailDescriptor = new Vector();
		selectedMailNumber = 0;
		//INITIALISE NEW MAIL LIST FOR GUI
		emailList = new JList();
		emailList.setVisibleRowCount(33);
		emailList.setFixedCellWidth(320);
		emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		refreshListContents();
		//SET UP LAYOUTS FOR PANELS
		buttonPanel.setLayout(new GridLayout(1,4));
		listPanel.setLayout(new BorderLayout());
		//ADD GUI COMPONENTS TO PANELS
		buttonPanel.add(read);
		buttonPanel.add(delete);
		buttonPanel.add(unRead);
		buttonPanel.add(unDelete);
		listPanel.add(title, BorderLayout.NORTH);
		listPanel.add(new JScrollPane(emailList), BorderLayout.CENTER);
		//ADD PANELS TO CONTENT PANE OF GUI
		pane.add(listPanel, BorderLayout.CENTER);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		//ADD ACTION LISTENER FOR BUTTONS
		read.addActionListener(this);
		delete.addActionListener(this);
		unRead.addActionListener(this);
		unDelete.addActionListener(this);
		ListSelectionHandler handler = new ListSelectionHandler();
		emailList.addListSelectionListener(handler);
	}

	//LIST SELECTION HANDLER FOR JLIST
	private class ListSelectionHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			//GET NUMBER OF MAIL SELECTED FROM LIST
			selectedMailNumber = emailList.getSelectedIndex();
		}
	}

	//ACTION LISTENER FOR MAIL BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		//CHECK IF THERE IS MAIL TO SELECT
		if(EMailClient.inboxSize() > 0 || e.getSource() == unDelete)
		{
			if(e.getSource() == read)
			{
				//GET THE MAIL SELECTED BY USER
				Email selectedMail = EMailClient.getMail(selectedMailNumber);
				//DELETE MAIL FROM VECTOR
				EMailClient.deleteMail(selectedMailNumber);
				//UPDATE READ PROPERTY OF MAIL
				selectedMail.setRead(true);
				//ADD MAIL BACK TO VECTOR
				EMailClient.addMail(selectedMail);
				//UPDATE LIST OF EMAILS
				refreshListContents();
				//OPEN EMAIL IN NEW MAIL WINDOW
				ReadMailWindowGUI readMailWinGUI = new ReadMailWindowGUI(selectedMail);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int HEIGHT = 600;
				final int WIDTH = 600;
				readMailWinGUI.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
										 ((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
				readMailWinGUI.setVisible(true);
			}
			if(e.getSource() == delete)
			{
				//DELETE MAIL FROM MAIL VECTOR
				EMailClient.deleteMail(selectedMailNumber);
				//UPDATE MAIL WINDOW
				refreshListContents();
				//DISPLAY MESSAGE TO USER
				JOptionPane.showMessageDialog(pane, "Mail Deleted");
			}
			if(e.getSource() == unRead)
			{
				//GET THE MAIL SELECTED BY THE USER
				Email selectedMail = EMailClient.getMail(selectedMailNumber);
				if(selectedMail.getRead() == true)
				{
					//DELETE MAIL FROM THE VECTOR
					EMailClient.deleteMail(selectedMailNumber);
					//UPDATE MAIL PROPERTY
					selectedMail.setRead(false);
					//ADD MAIL BACK TO INBOX AS NEW
					EMailClient.addMail(selectedMail);
					//UPDATE INBOX WINDOW
					refreshListContents();
					//DISPLAY MESSAGE TO USER
					JOptionPane.showMessageDialog(pane, "Mail Unread");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Mail is Unread");
				}
			}
			if(e.getSource() == unDelete)
			{
				//RETRIEVE THE BACKUP INBOX FROM CLIENT
				EMailClient.restoreMail();
				//UPDATE INBOX LIST
				refreshListContents();
				//DISPLAY MESSAGE TO USER
				JOptionPane.showMessageDialog(pane, "Mail Undeleted");
			}
		}
		//DISPLAY ERROR MESSAGE
		else
		{
			JOptionPane.showMessageDialog(null, "No Curent Mail");
		}
	}

	//UPDATES THE CONTENT OF THE INBOX LIST
	public void refreshListContents()
	{
		//CLEAR MAIL DESCRIPTION VECTOR
		mailDescriptor.removeAllElements();
		//DECLARE TEMPORARY EMAIL
		Email mail;
		//DECLARE EMAIL DESCRIPTION
		String description;
		//FOR EACH EMAIL GET A DESCRIPTION OF MAIL
		for(int count = 0; count < EMailClient.inboxSize(); count++)
		{
			//GET MAIL FROM INBOX
			mail = EMailClient.getMail(count);
			//IF MAIL HAS BEEN READ CREATE DESCRIPTION
			if(mail.getRead() == true)
			{
				description = ("    " + "FROM: " + mail.getSender() + "    " + "RE: "+ mail.getSubject() +
							   "    " + "ATTACHMENT: " + mail.getAttachmentName());
			}
			//IF MAIL IS UNREAD CREATE DIFFERENT DESCRIPTION
			else
			{
				description = ("NEW " + "FROM: " + mail.getSender() + "    " + "RE: "+ mail.getSubject() +
							   "    " + "ATTACHMENT: " + mail.getAttachmentName());
			}
			//ADD DESCRIPTION TO VECTOR
			mailDescriptor.add(description);
		}
		//SET INBOX WINDOW TO DECSRIPTION OF MAIL
		emailList.setListData(mailDescriptor);
	}
}