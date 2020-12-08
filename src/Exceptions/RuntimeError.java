package Exceptions;

public class RuntimeError extends Exception {
	String str = "";

	public RuntimeError() {
	}

	public RuntimeError(String text) {
		str = text;
	}

	@Override
	public String toString() {
		return "Runtime error: " + str;
	}
}