package Exceptions;

@SuppressWarnings("serial")
public class SyntaxError extends Exception {
	String str = "";

	public SyntaxError() {
	}

	public SyntaxError(String text) {
		str = text;
	}

	@Override
	public String toString() {
		return "Syntax error: " + str;
	}
}
