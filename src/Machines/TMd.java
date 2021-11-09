package Machines;

import java.io.IOException;
import java.nio.file.Path;

import Exceptions.RuntimeError;
import Exceptions.SyntaxError;
import processing.core.PApplet;

/**
 * "TMd" stands for "Turing Machine draw". Extends the basic functionality of
 * the Turing Machine and draws it to the screen. This implementation needs the
 * <a href="https://processing.org/">Processing</a> "core.jar" library.
 * 
 * @author Marcos Guti�rrez Alonso
 * @version 1.0
 */
public class TMd extends TM {
	private float tapeCounter = 0;
	private float tmpHead;
	private boolean changing = false;

	public TMd(Path code) throws SyntaxError, IOException {
		super(code);

		tmpHead = head;
	}

	public TMd(Path code, String initialState) throws SyntaxError, IOException {
		super(code, initialState);

		tmpHead = head;
	}

	@Override
	protected void move(String m) {
		if (m.equals("R")) {
			this.head++;

			if (this.tape.length - this.head - 1 <= 1) {
				char newtape[] = new char[this.tape.length + 2];

				for (int i = 0; i < this.tape.length; i++) {
					newtape[i] = tape[i];
				}

				newtape[this.tape.length + 0] = '0';
				newtape[this.tape.length + 1] = '0';

				this.tape = newtape;
			}

			changing = true;
		} else if (m.equals("L")) {
			this.head--;

			if (this.head <= 1) {
				char newtape[] = new char[this.tape.length + 2];
				newtape[0] = '0';
				newtape[1] = '0';

				for (int i = 0; i < this.tape.length; i++) {
					newtape[i + 2] = tape[i];
				}

				this.tape = newtape;

				this.head += 2;
			}

			changing = true;
		}
	}

	public void showTapeSummary(float x, float y, float wid, float hei, PApplet app) {
		float margin = Math.min(wid, hei) * 4 / 50;

		app.fill(20);
		app.stroke(0);
		app.rect(x + margin / 2, y + margin, wid - margin + 1, hei - margin);

		app.fill(30, 150);
		app.stroke(0);
		app.rect(x, y, wid, hei);

		app.stroke(0);
		app.line(x, y, x + margin / 2, y + margin);
		app.line(x + wid, y, x + wid - margin / 2 + 1, y + margin);

		int columns = 11;
		int rows = tape.length / columns;

		float cs = Math.min((wid - margin * 2) / columns, (hei - margin) / rows);

		float x1 = x + margin, y1 = y + margin * 3 / 2;

		app.stroke(255);
		for (int i = 0, column = 1; i < tape.length; i++, column++) {
			int value = getTape(i).charAt(0) - '0';

			if (value == 0) {
				app.fill(0);
			} else {
				app.fill(255, 0, 0);
			}

			app.rect(x1, y1, cs, cs);

			x1 += cs;

			if (column == columns) {
				column = 0;
				y1 += cs;
				x1 = x + margin;

				while (y1 > y + hei || y1 + cs > y + hei)
					cs -= 0.5;
			}
		}
	}

	public void show(float x0, float y, float wid, float hei, int headspace, PApplet app) throws RuntimeError {
		float cs = Math.min(wid / (headspace * 2), hei);

		stroke(255);
		strokeWeight(5);
		noFill();
		needle(width/2, height/2+hei, cs, app);
		strokeWeight(1);
	}

	void needle(float x, float y, float size, PApplet app){
		float xOffset = x-size/2;
		float yOffset = y-size/2;
		
		app.beginShape();
		app.vertex(size/2  + xOffset, -size/2   + yOffset);
		app.vertex(size    + xOffset, size/3    + yOffset);
		app.vertex(size    + xOffset, size      + yOffset);
		app.vertex(0       + xOffset, size      + yOffset);
		app.vertex(0       + xOffset, size/3    + yOffset);
		app.endShape(CLOSE);
	}
}
