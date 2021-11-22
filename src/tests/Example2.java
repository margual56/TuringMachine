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
public class Example2 {
	@Parameters(name = "{index}: state{0} => {1}")
	public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
		    {"{q01111};", "6"},	// 2*3 = 6
		    {"{q0100};", "0"},	// 2*0 = 0
        });
	}
	
	@Parameter(0)
	public String input;
	
	@Parameter(1)
	public String expected;
	
	@Test
	public void test() throws SyntaxError, IOException, RuntimeError {
		String code = """
				//Number x2

				""";
		code += input;
		code += """

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

		assertEquals(expected, Helper.run(code));
	}
}
