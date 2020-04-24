//IMPORT LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
//IMPORT LIBRARIES FOR VECTOR
import java.util.*;

class RecipientList extends JFrame
{
	//DECLARE COMPONENTS FOR GUI
	private JList recipientList;
	private Container pane;
	//DECLARE ARRAY TO HOLD USERNAMES
	private String[] names;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///RecipientList recipient = new RecipientList();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 475;
	///final int WIDTH = 300;
	///recipient.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///						((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///recipient.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR RECIPIENT LIST
	public RecipientList()
	{
		//ADD WINDOWE LISTENER FOR CLOSING
		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					//SEND SELECTED USER NAMES BACK TO SEND MAIL WINDOW
					SendMailWindowGUI.setRecipientArray(recipientList.getSelectedValues());
				}
			}
		);
		//SET GUI TITLE
		setTitle("Select Multiple Recipients");
		//GET CONTENT PANE AND SET LAYOUT
		pane = getContentPane();
		pane.setLayout(new FlowLayout());
		//GET USER NAMES FOR RECIPIENT LIST
		getUserNames();
		//SET UP NEW RECIPIENT LIST
		recipientList = new JList(names);
		recipientList.setVisibleRowCount(22);
		recipientList.setFixedCellWidth(250);
		recipientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//ADD RECIPIENT LIST TO CONTENT PANE
		pane.add(new JScrollPane(recipientList));
	}

	//GET USER NAMES FROM CLIENT
	public void getUserNames()
	{
		names = new String[EMailClient.getUserNamesSize()];
		for(int count = 0; count < names.length; count++)
		{
			names[count] = EMailClient.getName(count);
		}
	}
}



