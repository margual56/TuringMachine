package app;

import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import Machines.TMd;
import processing.core.PApplet;

public class MainWindow extends PApplet {
	//The default Turing program to execute
	String program = "";

	TMd turing;
	String text;
	boolean pause = false, finished;
	int state = 1;
	int fps = 10;

	public void settings() {
		size(1200, 1000);
	}

	public void setup() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			System.err.println("Can't set look to \"WindowsLookAndFeel\", what a shame");
		}
		
		if(program.isBlank()) {
			String workingDirectory = Paths.get("").toAbsolutePath().toString();
			JFileChooser j = new JFileChooser(workingDirectory);
			j.setDialogTitle("Select a Turing Machine Code file");
	
			// only allow files of .tm extension 
	        j.setAcceptAllFileFilterUsed(false); 
			FileNameExtensionFilter restrict = new FileNameExtensionFilter("Turing Machine files (.tm)", "tm");
			j.addChoosableFileFilter(restrict);
	
			// invoke the showsOpenDialog function to show the save dialog 
			int r = j.showOpenDialog(null);
	
			// if the user selects a file 
			if (r == JFileChooser.APPROVE_OPTION) {
				// set the label to the path of the selected file 
				program = j.getSelectedFile().getAbsolutePath();
			}
			// if the user cancelled the operation 
			else {
				exit();
				return;
			}
		}
		
		finished = false;
		text = "";
		state = 1;

		try {
			turing = new TMd(Paths.get(program));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "\"" + program + "\" does not exist");
			print(e);
			exit();
			return;
		}

		text = turing.toString();

		frameRate(60);
	}

	public void draw() {
		background(44);

		try {
			turing.showTapeSummary(50, 50, width - 100, 300, this);
			// turing.show(50, 400, width-100, 300, 6);
		} catch (Exception error) {
			print(error);
		}

		textSize(60);
		textAlign(LEFT, TOP);
		fill(255);

		if (state == -1)
			text(String.format("Output: %s", ((char) 193) + ""), 50, 350);
		else
			text(String.format("Output: %s", turing.output()), 50, 350);
			
		if (!pause && !finished && frameCount % fps == 0)
			doStep();

		fill(255);
		textSize(60);
		textAlign(LEFT, CENTER);
		text(text, 50, height * 0.65f);
		

		fill(255);
		textSize(20);
		textAlign(RIGHT, BOTTOM);
		text("FPS: " + nfc(frameRate/fps, 2), width-50, height-50);
	}

	public void keyPressed() {
		if(key == ' ') {
			if (finished)
				frameCount = -1;
			else
				pause = !pause;
		}else if(keyCode == RIGHT) {
			doStep();
		}else if(keyCode == ENTER) {
			while(!finished)
				doStep();
		}else if(key == 'r') {
			program = "";
			frameCount = -1;
		}else if(key == '-') {
			fps +=1;
		}else if(key == '+') {
			fps = constrain(fps-5, 1, fps);
		}else if(key == '=') {
			fps = 10;
		}
	}

	int sign(float n) {
		if (n == 0)
			return 0;

		return round(abs(n) / n);
	}

	private void doStep() {	
		if (state <= 0)
			finished = true;
	
		try {
			state = turing.update();
			text = turing.printTape();
		} catch (Exception error) {
			print(error);
			finished = true;
		}	
	}
	
	public static void main(String[] args) {
		String[] processingArgs = {"Turing Machine"};
		MainWindow mySketch = new MainWindow();
		PApplet.runSketch(processingArgs, mySketch);
	}
}