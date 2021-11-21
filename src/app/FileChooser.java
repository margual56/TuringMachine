package app;

import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {
	public FileChooser() {
		this(workingDirectory());
	}
	public FileChooser(String path) {
		super(path);
		
		this.setDialogTitle("Select a Turing Machine Code file");
		
		// only allow files of .tm extension 
        this.setAcceptAllFileFilterUsed(false); 
		FileNameExtensionFilter restrict = new FileNameExtensionFilter("Turing Machine files (.tm)", "tm");
		this.addChoosableFileFilter(restrict);
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			//System.err.println("Can't set look to \"WindowsLookAndFeel\", what a shame");
		}
	}
	
	public static String workingDirectory() {
		return Paths.get("").toAbsolutePath().toString();
	}
}
