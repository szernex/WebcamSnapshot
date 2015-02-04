package org.szernex.wcss;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Properties;

public class Main extends JFrame implements KeyListener
{
	public static final String CONFIG_FILE = "wcss.properties";

	private Properties Config;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Main app = new Main();
			}
		});
	}

	public Main()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Main");
		setSize(250, 100);

		loadProperties();

		setLayout(new GridLayout(1, 1));
		addKeyListener(this);

		JLabel label = new JLabel("Press ENTER to take a snapshot");
		label.setHorizontalAlignment(JLabel.CENTER);
		add(label);

		setVisible(true);
	}

	private void loadProperties()
	{
		try
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
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Something went wrong:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void takeSnapshot()
	{
		try
		{
			File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
			file.deleteOnExit();

			Webcam webcam = Webcam.getDefault();

			if (webcam == null)
			{
				JOptionPane.showMessageDialog(null, "No webcam found, aborting", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			webcam.setViewSize(webcam.getViewSizes()[webcam.getViewSizes().length - 1]);
			webcam.open();
			ImageIO.write(webcam.getImage(), "PNG", file);
			webcam.close();

			String application = Config.getProperty("application").replace("%1", file.getAbsolutePath());
			System.out.println("Executing: " + application);

			Runtime runtime = Runtime.getRuntime();
			runtime.exec(application);
		}
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Something went wrong:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
		{
			takeSnapshot();
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{

	}
}
