//INCLUDE LIBRARIES NEEDED FOR GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//INCLUDE LIBRARIES FOR FILES AND MEDIA
import java.io.*;
import javax.media.*;

public class ViewMediaAttachment extends JFrame implements ControllerListener
{
	//DECLARE CLASS COMPONENTS
	private Container pane;
	private byte[] attachment;
	private Player player;
	private FileOutputStream fileOut;
	private File file;

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	/////	PREFERRED SETTINGS FOR GUI STARTUP	//////////////////
	///ViewMediaAttachment attachment = new ViewMediaAttachment(email.getAttachment(), email.getAttachmentName());
	///Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	///final int HEIGHT = 600;
	///final int WIDTH = 600;
	///attachment.setBounds(((screenSize.width / 2) - (WIDTH / 2)),
	///						((screenSize.height / 2) - (HEIGHT / 2)), WIDTH, HEIGHT);
	///attachment.setVisible(true);
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////

	//CONSTRUCTOR FOR MEDIA PLAYER
	public ViewMediaAttachment(byte[] attachment, String name)
	{
		try
		{
			//ADD WINDOW LISTENER FOR DETECTING WINDOW CLOSE
			addWindowListener(
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						//DELETE TEMPORARY FILE
						file.delete();
					}
				}
			);
			//GET PASSED IN ATTACHMENT
			this.attachment = attachment;
			//SET UP FILE STREAM
			fileOut = new FileOutputStream(name);
			//WRITE ATTACHMENT TO FILE
			fileOut.write(attachment);
			//CREATE NEW FILE OBJECT
			file = new File(name);
			//SET WINDOW TITLE
			setTitle("Java Media Player");
			//SET UP CONTENT PANE
			pane = getContentPane();
			//CREATE MEDIA PLAYER AND CONTOL PANEL
			player = Manager.createPlayer(file.toURL());
			player.addControllerListener(this);
			player.start();
		}
		catch(FileNotFoundException fnfe)
		{
			JOptionPane.showMessageDialog(null, "File Not Found");
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
	}

	//SET UP CONTROLLER EVENT LISTENER FOR PLAYER
	public void controllerUpdate(ControllerEvent e)
	{
		if (e instanceof RealizeCompleteEvent)
		{
			//CREATE PLAY WINDOW AND ADD TO PANE
			Component visualComponent = player.getVisualComponent();
			if(visualComponent != null)
			{
				pane.add(visualComponent, BorderLayout.CENTER);
			}
			//CREATE CONTROL PANEL AND ADD TO WINDOW
			Component controlsComponent = player.getControlPanelComponent();
			if(controlsComponent != null)
			{
				pane.add(controlsComponent, BorderLayout.SOUTH);
			}
			//SET UP LAYOUT
			pane.doLayout();
		}
	}
}
