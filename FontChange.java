//IMPORT LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class FontChange extends JFrame
{
	//DECLARE FONTS FOR GUI
	private Font plainFont = new Font("TimesRoman", Font.PLAIN, 14);
	private Font boldFont = new Font("TimesRoman", Font.BOLD, 14);
	private Font italicFont = new Font("TimesRoman", Font.ITALIC, 14);
	private Font boldItalicFont = new Font("TimesRoman", Font.BOLD+Font.ITALIC, 14);
	private Font selectedFont;
	//DECLARE COMPONENTS FOR GUI
	private JRadioButton plain = new JRadioButton("Plain", true);
	private JRadioButton bold = new JRadioButton("Bold", false);
	private JRadioButton italic = new JRadioButton("Italic", false);
	private JRadioButton boldItalic = new JRadioButton("Bold/Italic", false);
	private ButtonGroup fontGroup;
	private Container pane;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///FontChange change = new FontChange();
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 70;
	///final int WIDTH = 300;
	///change.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///					((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///	change.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR FONT CHANGE GUI
	public FontChange()
	{
		//ADD WINDOW LISTENER FOR WINDOW CLOSING
		addWindowListener(
			new WindowAdapter()
			{
				//SEND FONT TO FORM DISPLAY APPROPRIATE ERROR
				public void windowClosing(WindowEvent e)
				{
					try
					{
						ReadMailWindowGUI.setNewFont(selectedFont);
					}
					catch(NullPointerException npe)
					{
						JOptionPane.showMessageDialog(pane, "Display Font Changed");
					}
					try
					{
						SendMailWindowGUI.setNewFont(selectedFont);
					}
					catch(NullPointerException npe)
					{
						JOptionPane.showMessageDialog(pane, "Display Font Changed");
					}
				}
			}
		);
		//SET GUI TITLE
		setTitle("Change Current Font");
		//GET CONTENT PANE
		pane = getContentPane();
		//SET LAYOUT
		pane.setLayout(new FlowLayout());
		//ADD FONTS TO PANE
		pane.add(plain);
		pane.add(bold);
		pane.add(italic);
		pane.add(boldItalic);
		//ADD HANDLERS AND LISTENERS TO PANE
		RadioButtonHandler handler = new RadioButtonHandler();
		plain.addItemListener(handler);
		bold.addItemListener(handler);
		italic.addItemListener(handler);
		boldItalic.addItemListener(handler);
		//ADD FONTS TO FONT GROUP
		fontGroup = new ButtonGroup();
		fontGroup.add(plain);
		fontGroup.add(bold);
		fontGroup.add(italic);
		fontGroup.add(boldItalic);
	}

	//RADIO BUTTON HANDLER CLASS
	private class RadioButtonHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			//RETURN SELECTED FONT
			if (e.getSource() == plain)
				selectedFont = plainFont;
			else if(e.getSource() == bold)
				selectedFont = boldFont;
			else if(e.getSource() == italic)
				selectedFont = italicFont;
			else if(e.getSource() == boldItalic)
				selectedFont = boldItalicFont;
		}
	}
}



