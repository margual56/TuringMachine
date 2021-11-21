package app;

import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Exceptions.RuntimeError;
import Exceptions.SyntaxError;

import Machines.TM;
import Machines.TMd;
import processing.core.PApplet;

public class MainWindow extends PApplet {
	public static final int DEFAULT_FONT_SIZE = 12;
	//The default Turing program to execute
	String program = "";

	TMd turing;
	boolean pause = true, finished;
	int state = 1;
	int fps = 10;
	
	PButton helpButton;

	public void settings() {
		size(min(1200, displayWidth), min(1000, displayHeight));
	}

	public void setup() {
		if(program.isBlank()) {
			FileChooser j = new FileChooser();
	
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
		state = 1;

		try {
			turing = new TMd(Paths.get(program));
		} catch (SyntaxError sE) {
			System.err.println(sE);
			JOptionPane.showMessageDialog(null, sE, "Syntax Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
			return;
		} catch (RuntimeError rE) {
			System.err.println(rE);
			JOptionPane.showMessageDialog(null, rE, "Runtime Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "\"" + program + "\" does not exist");
			print(e);
			System.exit(-2);
			return;
		}
		
		helpButton = new PButton(50, height-50, "Show help", this, 
				(mX, mY, app) -> {
					HelpFrame frame = new HelpFrame("Help");
			        frame.setVisible(true);
				}
		);
		helpButton.setBgColor(255);
		helpButton.setFgColor(0);
		helpButton.setFontSize(15, this);
		helpButton.setY(height-helpButton.getHeight()-50);
	}

	public void draw() {
		background(44);

		turing.showTapeSummary(50, 50, width - 100, 300, this);
		// turing.show(50, 400, width-100, 300, 6);

		try {
			textSize(60);
			textAlign(LEFT, TOP);
			fill(255);
	
			/*if (state == -1)
				text(String.format("Output: %s", ((char) 193) + ""), 50, 350);
			else*/
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
			
			if(helpButton.hovered(mouseX, mouseY))
				helpButton.drawHovered(this);
			else			
				helpButton.draw(this);
			
		} catch (RuntimeError e) {
			System.err.println(e);
			JOptionPane.showMessageDialog(null, e, "Runtime Error", JOptionPane.ERROR_MESSAGE);
			
			System.exit(-1);
		}
	}

	public void keyPressed() {
		if(keyCode == RIGHT) {
			doStep();
			turing.resetAnimation();
		}else if(keyCode == ENTER) {
			end();
			turing.resetAnimation();
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
	
	public void mousePressed() {
		if(helpButton.hovered(mouseX, mouseY))
			helpButton.pressed(mouseX, mouseY, this);
	}
	
	private void end(){
		int execution_code = 1;
		do {

			try {
				execution_code = turing.update();
			}catch(Exception error) {
				System.err.println(error);
				
				finished = true;
				
				return;
			}
			
			if(execution_code == -1) {
				System.err.println("A Halt state was reached, but it wasn't a final state!");
				
				finished = true;
				
				return;
			}
		}while(execution_code != 0);
		
		finished = true;
		redraw();
	}
	
	private void doStep() {	
		if (state == 0)
			finished = true;
	
		try {
			state = turing.update();
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
			boolean verbose = false;
			for(int i = 0; i<args.length; i++) {
				if(args[i].compareToIgnoreCase("-v") == 0) {
					verbose = true;
					break;
				}
			}
			
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
						String result = headless(file, verbose);
						
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
	
	private static String headless(String file, boolean verbose) throws SyntaxError, IOException, RuntimeError {
		TM machine = new TM(Paths.get(file));

		if(verbose)
			System.out.println(machine.getTape());
		
		int code = 1;
		while(code != 0) {
			
			code = machine.update();

			if(verbose)
				System.out.println(machine.getTape());
			
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
					--headless FILE	[-v]	Run in headless mode (print the result and exit, no GUI). Write `-v` to get the verbose output.
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
		return  """
				// a + b

				{q011111011}; // 4 + 1
				
				#define F = {q2};
				
				(q0, 1, 0, R, q1);
				
				(q1, 1, 1, R, q1);
				(q1, 0, 0, R, q2);
				
				(q2, 1, 0, H, q2);
				(q2, 0, 0, H, q2);
				""";
	}
}