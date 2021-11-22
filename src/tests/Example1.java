package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;

@RunWith(Parameterized.class)
public class Example1 {
	@Parameters(name = "{index}: state{0} => {1}")
	public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
		    {"{q011111011};", "5"},	// 4+1 = 5
		    {"{q01111010};", "3"},	// 3+0 = 3
		    {"{q0101111};", "3"}	// 0+3 = 3
        });
	}
	
	@Parameter(0)
	public String input;
	
	@Parameter(1)
	public String expected;
	
	@Test
	public void test() throws SyntaxError, IOException, RuntimeError {
		String code = """
				// a + b

				""";
		code += input;
		code += """

				#define F = {q2};

				(q0, 1, 0, R, q1);

				(q1, 1, 1, R, q1);
				(q1, 0, 0, R, q2);

				(q2, 1, 0, H, q2);
				(q2, 0, 0, H, q2);
				""";

		assertEquals(expected, Helper.run(code));
	}
}
