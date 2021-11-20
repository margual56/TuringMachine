package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;
import Machines.TM;

class ExpectedOutput {

	@Test
	void Example1() throws SyntaxError, IOException, RuntimeError {
		String code = """
				// a + b

				{q011111011}; // 4 + 1
				
				#define F = {q2};
				
				(q0, 1, 0, R, q1);
				
				(q1, 1, 1, R, q1);
				(q1, 0, 0, R, q2);
				
				(q2, 1, 0, H, q2);
				(q2, 0, 0, H, q2);
				""";
		
		assertEquals("5", run(code));
	}
	
	@Test
	void Example2() throws SyntaxError, IOException, RuntimeError {
		String code = """
				//Number x2

				{q01111}; // 3
				
				#define F = {q1};
				
				(q0, 1, 0, R, q1);
				
				(q1, 1, 0, R, q2);
				(q1, 0, 0, H, q1);
				
				(q2, 1, 1, R, q2);
				(q2, 0, 0, R, q3);
				
				(q3, 1, 1, R, q3);
				(q3, 0, 1, L, q4);
				
				(q4, 1, 1, L, q4);
				(q4, 0, 0, L, q5);
				
				(q5, 1, 1, L, q5);
				(q5, 0, 1, R, q1);
				""";
		
		assertEquals("6", run(code));
	}
	
	@Test
	void Example3() throws SyntaxError, IOException, RuntimeError {
		String code = """
				// x - y
				{0q0111110111100}; // (4, 3)

				#define F = {f};
				
				//Search for the separator
				(q0, 1, 1, R, q0);
				(q0, 0, 0, R, q1);
				
				//Go to the end of y
				(q1, 1, 1, R, q1);
				(q1, 0, 0, L, q2);
				
				//Substracting 1 from y
				(q2, 1, 0, L, q3);
				
				//If there is no more y, we stop
				(q2, 0, 0, H, f);
				
				//Find the separator
				(q3, 1, 1, L, q3);
				(q3, 0, 0, L, q4);
				
				//Go to the beginning of x
				(q4, 1, 1, L, q4);
				(q4, 0, 0, R, q5);
				
				//Start all over again
				(q5, 1, 0, R, q0);
				(q5, 0, 0, R, q6); //This means that x>y
				
				//Find all the ones and delete them
				(q6, 1, 0, R, q6);
				(q6, 0, 0, H, f);
				
				(f, 0, 0, H, f);
				(f, 1, 1, H, f);
				""";
		
		assertEquals("1", run(code));
	}
	
	@Test
	void Example4() throws SyntaxError, IOException, RuntimeError {
		String code = """
				//SUMATORY FROM 1 TO X

				{q01111}; // 3
				
				#define F = {f};
				
				//Eliminate the first one
				(q0, 1, 0, R, q1);
				
				//Slip a one
				(q1, 1, 1, R, q2);
				
				//STOP
				(q1, 0, 0, H, f);
				
				//Leave a mark
				(q2, 1, 0, R, q3);
				(q2, 0, 0, R, q1);
				
				//Go to the end of x
				(q3, 1, 1, R, q3);
				(q3, 0, 0, R, q4);
				
				//Go to the end and add 1
				(q4, 0, 1, L, q5);
				(q4, 1, 1, R, q4);
				
				//Go to the separator
				(q5, 1, 1, L, q5);
				(q5, 0, 0, L, q6);
				
				//Search for the mark, remove it and start over
				(q6, 1, 1, L, q6);
				(q6, 0, 1, R, q2);
				
				(f, 0, 0, H, f);
				(f, 1, 1, H, f);
				""";
		
		assertEquals("6", run(code));
	}
	
	@Test
	void Example5() throws SyntaxError, IOException, RuntimeError {
		String code = """
				//SUMATORY FROM 1 TO X

				{q01111}; // 3
				
				#define F = {f};
				
				//Eliminate the first one
				(q0, 1, 0, R, q1);
				
				//Slip a one
				(q1, 1, 1, R, q2);
				
				//STOP
				(q1, 0, 0, H, f);
				
				//Leave a mark
				(q2, 1, 0, R, q3);
				(q2, 0, 0, R, q1);
				
				//Go to the end of x
				(q3, 1, 1, R, q3);
				(q3, 0, 0, R, q4);
				
				//Go to the end and add 1
				(q4, 0, 1, L, q5);
				(q4, 1, 1, R, q4);
				
				//Go to the separator
				(q5, 1, 1, L, q5);
				(q5, 0, 0, L, q6);
				
				//Search for the mark, remove it and start over
				(q6, 1, 1, L, q6);
				(q6, 0, 1, R, q2);
				
				(f, 0, 0, H, f);
				(f, 1, 1, H, f);
				""";
		
		assertEquals("6", run(code));
	}
	
	public static String run(String code) throws SyntaxError, IOException, RuntimeError {
		TM machine = new TM(code);
		
		int execution_code = 1;
		do {
			
			execution_code = machine.update();
			
			if(execution_code == -1)
				throw new RuntimeError("A Halt state was reached, but it wasn't a final state!");
		}while(execution_code != 0);
		
		return machine.output();
	}
}
