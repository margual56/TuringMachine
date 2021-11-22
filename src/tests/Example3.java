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
public class Example3 {
	@Parameters(name = "{index}: state{0} => {1}")
	public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
		    {"{0q0111110111100};", "1"},	// 4 - 3 = 1
		    {"{0q0110111100};", "0"},		// 1 - 3 = 0
		    {"{0q0101111};", "0"},			// 0 - 3 = 0
		    {"{0q011110100};", "3"},		// 3 - 0 = 3
        });
	}
	
	@Parameter(0)
	public String input;
	
	@Parameter(1)
	public String expected;
	
	@Test
	public void test() throws SyntaxError, IOException, RuntimeError {
		String code = """
				// x - y
				""";
		code += input;
		code += """

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

		assertEquals(expected, Helper.run(code));
	}
}
