package app;

import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author marcos
 * 
 *	Custom File Chooser. It restricts the choice to Turing machine files (tm) 
 *	and tries to set the Windows look and feel
 */
@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {
	/**
	 * Calls the other constructor with the working directory as the path.
	 */
	public FileChooser() {
		this(workingDirectory());
	}
	/**
	 * Default constructor. Takes the path, tries to set the look and feel 
	 * and the window title
	 * 
	 * @param path Default path to show
	 */
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
	
	/**
	 * @return The current working directory
	 */
	public static String workingDirectory() {
		return Paths.get("").toAbsolutePath().toString();
	}
}
