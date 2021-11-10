package app;

import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;

import Machines.TM;
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
			//System.err.println("Can't set look to \"WindowsLookAndFeel\", what a shame");
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
		} catch (SyntaxError sE) {
			System.err.println(sE);
			System.exit(-1);
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "\"" + program + "\" does not exist");
			print(e);
			System.exit(-2);
			return;
		}

		text = turing.toString();
	}

	public void draw() {
		background(44);

		try {
			turing.showTapeSummary(50, 50, width - 100, 300, this);
			// turing.show(50, 400, width-100, 300, 6);
		} catch (Exception error) {
			print(error);
		}

		try {
			textSize(60);
			textAlign(LEFT, TOP);
			fill(255);
	
			if (state == -1)
				text(String.format("Output: %s", ((char) 193) + ""), 50, 350);
			else
				text(String.format("Output: %s", turing.output()), 50, 350);
	
			if (!pause && !finished) {
				if(frameCount % fps == 0 && turing.finishedTransition())
					doStep();
				
				turing.animateTape(fps, this);
			}
			
	
			fill(255);
			textSize(20);
			textAlign(RIGHT, BOTTOM);
			text("FPS: " + nfc(frameRate/fps, 2), width-50, height-50);
			
			noFill();
			stroke(255);
			turing.show(0, height/2, width, 50, 0, this);
			
			textSize(30);
			textAlign(CENTER, TOP);
			text(turing.getInstruction(), width/2, height/2+150);
		} catch (RuntimeError e) {
			e.printStackTrace();
		}
	}

	public void keyPressed() {
		if(keyCode == RIGHT) {
			doStep();
		}else if(keyCode == ENTER) {
			while(!finished)
				doStep();
		}else {
			switch(key) {
			case 'r':
				program = "";
				frameCount = -1;
				break;
			
			case '-':
				fps +=5;
				break;
			
			case '+':
				fps = constrain(fps-1, 1, fps);
				break;
				
			case '=':
				fps = 10;
				break;
			
			case ' ':
				if (finished)
					frameCount = -1;
				else
					pause = !pause;
				
				break;
			}
		}
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
		if(args.length == 0) {
			String[] processingArgs = {"Turing Machine"};
			MainWindow mySketch = new MainWindow();
			PApplet.runSketch(processingArgs, mySketch);
		} else {
			for(int i = 0; i<args.length; i++) {
				if(args[i].compareToIgnoreCase("-h") == 0 || args[i].compareToIgnoreCase("--help") == 0) {
					System.out.println(help());
					System.exit(0);
				}else if(args[i].compareToIgnoreCase("--headless") == 0) {
					if(i == args.length-1) {
						System.err.println("You need to provide a file when running the headless mode!");
						System.out.println(help());
						System.exit(1);
					}
					
					String file = args[i+1];
					
					try {
						String result = headless(file);
						
						System.out.println("Result: " + result);
						
						System.exit(0);
					} catch (SyntaxError | RuntimeError sE) {
						System.err.println(sE);
						System.exit(1);
					} catch (IOException e) {
						System.err.println("The file \"" + file + "\" does not exist");
						System.exit(2);
					}
					
				}else if(args[i].compareToIgnoreCase("-e") == 0 || args[i].compareToIgnoreCase("--example") == 0) {
					System.out.println(example());
					System.exit(0);
				}
			}
			
			System.err.println("No argument you provided was valid.");
			System.out.println(help());
			System.exit(1);
		}
	}
	
	private static String headless(String file) throws SyntaxError, IOException, RuntimeError {
		TM machine = new TM(Paths.get(file));
		
		int code = 1;
		while(code != 0) {
			code = machine.update();
			
			if(code == -1)
				throw new RuntimeError("A Halt state was reached, but it wasn't a final state!");
		}
		
		return machine.output();
	}
	
	private static String help() {
		return """
				Usage: java -jar TuringMachine.jar [options]
				
				A simple Turing Machine simulator with a GUI. It uses the syntax we use at class in Computability (EPI Gijón).
				
				Optional arguments:
					(none)			Run the program normally (GUI mode)
					-h, --help		Show this help message and exit
					--headless FILE		Run in headless mode (print the result and exit, no GUI)
					-e, --example		Print an example program and exit
					
				Note:
					You can only provide one argument at a time. If more than one is provided, just the first one is going to be taken into account.
					
				Author:
					Marcos Gutiérrez Alonso
				
				Repository:
					https://github.com/margual56/TuringMachine		(GPLv3)
				""";
	}
	
	private static String example() {
		return """
				Printing the Example 1:
					// a + b
	
					{q011111011};
					
					#define F = {f};
					
					(q0, 1, 0, R, q1);
					
					(q1, 1, 1, R, q1);
					(q1, 0, 0, R, q2);//1
					
					(q2, 0, 1, L, q3);
					(q2, 1, 1, R, q2);
					
					(q3, 1, 1, L ,q4);
					(q3, 0, 0, L ,q3);
					
					(q4, 1, 1, L, q4);
					(q4, 0, 0, L, q5);//2
					
					(q5, 1, 1, L, q5);
					(q5, 0, 0, R, q0);
					
					(q0, 0, 0, R, q6);//3
					
					(q6, 0, 0, R, q6);
					(q6, 1, 0, R, q7);//4
					
					(q7, 1, 0, R, f);
					
					(f, 0, 0, H, f);
					(f, 1, 1, H, f);
				""";
	}
}