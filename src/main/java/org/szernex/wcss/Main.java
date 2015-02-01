package org.szernex.wcss;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class Main extends Frame implements KeyListener, WindowListener
{
	public static final String CONFIG_FILE = "wcss.properties";

	private static final String ACTION_SNAPSHOT = "snapshot";
	private static Main Instance;

	private Properties Config;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		Instance = new Main();
	}

	public Main() throws IOException
	{
		loadProperties();

		setLayout(new GridLayout(1, 1));
		addKeyListener(this);
		addWindowListener(this);

		Label label = new Label("Press ENTER to take a snapshot");
		add(label);

		setTitle("WebcamSnapshot");
		setSize(200, 200);
		setVisible(true);
	}

	private void loadProperties() throws IOException
	{
		File file = new File(CONFIG_FILE);

		if (!file.exists())
		{
			if (!file.createNewFile())
			{
				throw new IOException("could not create new properties file");
			}
		}

		InputStream input = new FileInputStream(file);
		Config = new Properties();

		Config.load(input);
		input.close();

		OutputStream output = new FileOutputStream(file);

		Config.setProperty("application", Config.getProperty("application", "mspaint %1"));

		Config.store(output, "");
		output.close();
	}

	public void takeSnapshot()
	{
		try
		{
			File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
			file.deleteOnExit();

			Webcam webcam = Webcam.getDefault();
			webcam.setViewSize(new Dimension(640, 480));
			webcam.open();
			ImageIO.write(webcam.getImage(), "PNG", file);
			webcam.close();

			String application = Config.getProperty("application").replace("%1", file.getAbsolutePath());
			System.out.println("Executing " + application);

			Runtime runtime = Runtime.getRuntime();
			runtime.exec(application);
		}
		catch (IOException ex)
		{
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent keyEvent)
	{

	}

	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		System.out.println(keyEvent.getKeyCode());

		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
		{
			takeSnapshot();
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{

	}

	@Override
	public void windowOpened(WindowEvent windowEvent)
	{

	}

	@Override
	public void windowClosing(WindowEvent windowEvent)
	{
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent windowEvent)
	{

	}

	@Override
	public void windowIconified(WindowEvent windowEvent)
	{

	}

	@Override
	public void windowDeiconified(WindowEvent windowEvent)
	{

	}

	@Override
	public void windowActivated(WindowEvent windowEvent)
	{

	}

	@Override
	public void windowDeactivated(WindowEvent windowEvent)
	{

	}
}
