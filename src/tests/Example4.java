package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;

@RunWith(Parameterized.class)
public class Example4 {
	@Parameters(name = "{index}: state{0} => {1}")
	public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
		    {"{q0100};", "0"},		// sumatory from 1 to 0 = 0
		    {"{q01100};", "1"},		// sumatory from 1 to 1 = 1
		    {"{q0111111};", "15"}	// sumatory from 1 to 5 = 1 + 2 + 3 + 4 + 5 = 15
        });
	}
	
	@Parameter(0)
	public String input;
	
	@Parameter(1)
	public String expected;
	
	@Test
	public void test() throws SyntaxError, IOException, RuntimeError {
		String code = """
				//SUMATORY FROM 1 TO X

				""";
		code += input;
		code += """

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

		assertEquals(expected, Helper.run(code));
	}
}
