package Exceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SyntaxError extends Exception {
	String str = "";

	public SyntaxError() {
		JOptionPane.showMessageDialog(null, "Unknown syntax error", "Syntax error", JOptionPane.ERROR_MESSAGE);
	}

	public SyntaxError(String text) {
		str = text;

		JOptionPane.showMessageDialog(null, text, "Syntax error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String toString() {
		return "Syntax error: " + str;
	}
}
