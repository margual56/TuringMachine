package Machines;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;

/**
 * Basic implementation of the Turing Machine. This is not graphical, it just
 * processes the tape, the states and the code.
 * 
 * @author Marcos Guti�rrez Alonso
 * @version 2.0
 *
 */
public class TM {
	protected HashMap<String, String[]> instructions; // Stores the instructions for a given state
	protected String finalStates[]; // Stores the final states of the code
	protected int head = 0; // Point of execution
	protected String state = ""; // Current state of the machine
	protected char tape[]; // Stores all the information on the tape
	protected boolean undefined = false;

	public TM(String code) throws SyntaxError, IOException {

		/////////////////////////////////////// READ CODE ///////////////////////////////////////
		String lines[];
		lines = code.split("\n");

		///////////////////////////////////// PROCESS CODE /////////////////////////////////////
		interpret(lines);
	}

	public TM(Path code) throws SyntaxError, IOException {

		/////////////////////////////////////// READ CODE ///////////////////////////////////////
		String lines[];

		Charset charset = StandardCharsets.UTF_8;
		lines = Files.readAllLines(code, charset).toArray(new String[0]);

		///////////////////////////////////// PROCESS CODE ///////////////////////////////////// 
		interpret(lines);
	}

	public TM(Path code, String initialState) throws SyntaxError, IOException {
		this(code);

		setInitialState(initialState); // Override the initial state to the one given as argument
	}

	private void interpret(String lines[]) throws SyntaxError {
		String mycode = "";
		instructions = new HashMap<String, String[]>();

		for (String line : lines) {
			if (line.indexOf("//") != -1) { // If the line has a comment
				line = line.substring(0, line.indexOf("//")); // Get everything in front of the comment
			}

			line = line.replace(" ", "").replace("\n", ""); // Remove the spaces and EOL

			mycode += line; // Add the line to the code
		}

		// Since lines are separated by ";" (not by EOL), we just joined all the code
		// into one single line without spaces
		// so now, we split the line by the separator (";") and this way we obtain all
		// the separate commands
		String commands[] = mycode.split(";");

		/////////////////////////////// CHECK DEFINITIONS FOR INITIAL AND FINAL STATES ///////////////////////////////

		if (commands[0].charAt(0) != '{')
			throw new SyntaxError("No initial state definition!\n\"{00...<initial_state>...00}\" needed!");

		if (commands[1].indexOf("F={") == -1)
			throw new SyntaxError("No final state definition!\n\"F={<final_state(s)>}\" needed!");

		/////////////////////////////////////// POPULATE THE INSTRUCTIONS HASH /////////////////////////////////////// 
		String state, reads;
		for (int i = 2; i < commands.length; i++) {
			String line = commands[i];

			line = line.replace("(", "").replace(")", ""); // Remove the parenthesis (they are just for clarity)

			String s[] = line.split(","); // Get the different parts of the instruction

			if (s.length != 5) // If there are not 5 and exactly 5 parts per line, throw an error
				throw new SyntaxError("Error in command " + i + ", 5 parameters expected. " + s.length
						+ " parameters found instead.");

			state = s[0];
			reads = s[1];
			// Otherwise, add the equivalence (e.g.: "(q0, 1, 0, R, q1);" -> {"q01": ["q0", "1", "0", "R", "q1"]} )
			// So, "q01" means "state q0 with input 1"
			//System.out.printf("instructions.put(%s + %s, [%s, %s, %s, %s, %s])\n", state, reads, s[0], s[1], s[2], s[3], s[4]);
			instructions.put(state + reads, s);
		}

		setInitialState(commands[0]); // Because the initial state of the tape should be in the first line
		setFinalStates(commands[1]); // And the final states should be in the second line
	}

	/**
	 * Sets the initial state and the initial values of the tape for the Turing
	 * Machine.
	 * 
	 * @param Line containing the initial state of the tape
	 */
	private void setInitialState(String state) {
		state = state.replace("{", "").replace("}", ""); // Remove the braces from the initial state line

		////////////////////////////////// SET THE INITIAL STATE //////////////////////////////////
		int i = 0;

		// Move forward until the character is a letter.
		// This gives us the position of the position of the initial state (q0)
		while (!Character.isLetter(state.charAt(i))) {
			i++;
		}

		// Set the head to this position because later we are going to delete the state
		// e.g.: "1001q0110" (position 4) -> "10011(<-)10"
		head = i;

		String initialPosition = "";

		while (Character.isLetter(state.charAt(i))) {
			initialPosition += Character.toString(state.charAt(i));
			i++;
		}

		initialPosition += Character.toString(state.charAt(i));

		state = state.replace(initialPosition, "");
		this.state = initialPosition;

		////////////////////////////////////////////////////////////////////////////////////////////

		// And now we set the tape
		tape = new char[state.length()];

		for (i = 0; i < tape.length; i++)
			tape[i] = state.charAt(i);
	}

	/**
	 * Generates a list with the final states
	 * 
	 * @param Line containing the final states
	 */
	private void setFinalStates(String state) {
		state = state.replace("#defineF={", "").replace("}", "");

		finalStates = state.split(",");

		// If the final states are not explicitly defined, create them
		for (int i = 0; i < finalStates.length; i++) {
			if (!instructions.containsKey(finalStates[i]+"0"))
				instructions.put(finalStates[i] + "0", new String[] { finalStates[i], "0", "0", "H", finalStates[i] });
			
			if(!instructions.containsKey(finalStates[i]+"1"))
				instructions.put(finalStates[i] + "1", new String[] { finalStates[i], "1", "1", "H", finalStates[i] });
		}
	}

	/**
	 * Executes the next instruction and updates everything (the tape, the states,
	 * the head, etc).
	 * 
	 * @return execution return code
	 * @throws RuntimeError
	 */
	public int update() throws RuntimeError {
		String[] current;

		try {
			current = getInstruction(this.state, getTape(this.head));
		} catch (RuntimeError error) {
			throw error;
		}

		// Set the tape value at the position of the head to the programmed value
		this.tape[this.head] = current[2].charAt(0);
		
		if (current[3].equals("H")) {
			if(isFinal(this.state)) // We can end the execution.
				return 0; // With an exit code 0 (finished successfully)
			else if(current[0].equals(current[4])) {
				undefined = true;
				return -1; // Final state not defined
			}
		}
		
		// 3 -> 4th position -> (R | L) - > move the tape to the right or to the left
		move(current[3]);
		
		// Then, we assign the new state to the corresponding one
		this.state = current[4];

		// This may throw a RuntimeError if the new state is not defined
		current = getInstruction(this.state, getTape(this.head));

		
		return 1; // Normal execution returns 1
	}

	/**
	 * @param state to be evaluated
	 * @return true if the given state is final
	 */
	private boolean isFinal(String s) {
		for (String st : finalStates) // Just check if the given state is in the "finalStates" array
			if (st.equals(s))
				return true;

		return false;
	}

	/**
	 * 
	 * 
	 * @param m "R" or "L"
	 * @throws RuntimeError
	 */
	protected void move(String m) throws RuntimeError {
		if (m.equals("R")) {
			this.head++;

			if (this.tape.length - this.head - 1 <= 1) {
				char newtape[] = new char[this.tape.length + 2];

				for (int i = 0; i < this.tape.length; i++) {
					newtape[i] = tape[i];
				}

				newtape[this.tape.length + 0] = '0';
				newtape[this.tape.length + 1] = '0';

				this.tape = newtape;
			}
		} else if (m.equals("L")) {
			this.head--;

			if (this.head <= 1) {
				char newtape[] = new char[this.tape.length + 2];
				newtape[0] = '0';
				newtape[1] = '0';

				for (int i = 0; i < this.tape.length; i++) {
					newtape[i + 2] = tape[i];
				}

				this.tape = newtape;

				this.head += 2;
			}
		} else if(!m.equals("H")) {
			throw new RuntimeError("\"" + m + "\" is not a valid movement of the tape. Please, use \"R\", \"L\" or \"H\"");
		}
	}

	private String[] getInstruction(String s, String v) throws RuntimeError {
		String[] toReturn = (String[]) instructions.get(s + v);

		if (toReturn == null)
			throw new RuntimeError("The state " + s + " with the value " + v + " on the tape is not defined");

		return toReturn;
	}

	public String getCurrentInstruction() throws RuntimeError {
		String[] text = getInstruction(state, "" + getTape(head));
		String out = "";

		out += "(";
		for (int i = 0; i < text.length - 1; i++)
			out += text[i] + ", ";
		out += text[text.length - 1] + ")\n";

		return out;
	}

	public String getTape() {
		String t = "{";

		for (int i = 0; i < tape.length; i++) {
			if (i == head) {
				t += "(" + state + ")" + tape[i];
			} else {
				t += tape[i] + "";
			}
		}

		return t + "};";
	}

	public String getTape(int index) {
		// If out of bounds, return zero (the tape is technically infinite)
		if (index < 0 || index >= tape.length)
			return "0";

		return Character.toString(tape[index]);
	}

	public int getTapeLength() {
		return tape.length;
	}

	public int getHead() {
		return head;
	}

	@Override
	public String toString() {
		String out = "";

		for (int i = 0; i < tape.length - 1; i++) {
			out += tape[i] + " ";
		}

		out += tape[tape.length - 1] + "\n";

		for (int i = 0; i < head * 2; i++)
			out += " ";

		out += "^\n";

		String s[];

		try {
			s = getInstruction(state, "" + getTape(head));
		} catch (RuntimeError error) {
			return out;
		}

		out += "(";
		for (int i = 0; i < s.length - 1; i++)
			out += s[i] + ", ";
		out += s[s.length - 1] + ")\n";

		return out;
	}

	public String getState() {
		return state;
	}

	public String output() {
		// if (undefined)
		// return ((char) 193) + "";

		int sum = 0;

		for (int i = 0; i < tape.length; i++) {
			sum += tape[i] - '0';
		}

		return Integer.toString(sum);
	}

	/**
	 * @return Turing Machine's tape (as a String of zeroes and ones)
	 * @implNote The "^" character represents the position of the head, but it does
	 *           not always align correctly: it depends on the font size and other
	 *           variables.
	 */
	public String printTape() {
		String out = "";

		//////////////////////////////////////////// TAPE VALUES
		//////////////////////////////////////////// ////////////////////////////////////////////

		for (int i = 0; i < tape.length - 1; i++) {
			// We don't want to use the function {@link #getTape(int)} here,
			// since it is not necessary and this way is faster.

			// Because we need to convert char to String, we add the space to the char and
			// java automatically converts the char to String
			out += tape[i] + " ";
		}

		// The last element does not need an space, so we add it here, together with an
		// EOL
		out += tape[tape.length - 1] + "\n";

		//////////////////////////////////////////// TAPE HEAD
		//////////////////////////////////////////// /////////////////////////////////////////////

		for (int i = 0; i < head * 3; i++)
			out += " ";

		out += "^\n";

		//////////////////////////////////////////// TAPE STATE
		//////////////////////////////////////////// ////////////////////////////////////////////

		String s[];

		try {
			s = getInstruction(state, getTape(head));
		} catch (RuntimeError error) {
			return out;
		}

		// Join all the instruction (as written in code)
		out += "(";
		for (int i = 0; i < s.length - 1; i++)
			out += s[i] + ", ";
		out += s[s.length - 1] + ")\n";

		return out;
	}
}
