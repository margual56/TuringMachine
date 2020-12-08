package app;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

public class FileChooser extends JFileChooser {
	public FileChooser() {
		this(null);
	}
	public FileChooser(String path) {
		super(path);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
