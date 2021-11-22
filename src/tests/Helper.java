package tests;

import java.io.IOException;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;
import Machines.TM;

public abstract class Helper {
	public static String run(String code) throws SyntaxError, IOException, RuntimeError {
		TM machine = new TM(code);

		int execution_code = 1;
		do {

			execution_code = machine.update();

			if (execution_code == -1)
				throw new RuntimeError("A Halt state was reached, but it wasn't a final state!");
		} while (execution_code != 0);

		return machine.output();
	}
}
