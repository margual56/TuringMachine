package Exceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class RuntimeError extends Exception {
	String str = "";

	public RuntimeError() {
		JOptionPane.showMessageDialog(null, "Unknown runtime error", "Runtime error", JOptionPane.ERROR_MESSAGE);
	}

	public RuntimeError(String text) {
		str = text;
		JOptionPane.showMessageDialog(null, text, "Runtime error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public String toString() {
		return "Runtime error: " + str;
	}
}