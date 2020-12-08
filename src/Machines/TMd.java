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
 * @author Marcos Gutiérrez Alonso
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

		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.textSize(cs * 0.9f);

		float x, offset = PApplet.lerp(tmpHead, this.head, tapeCounter);
		float middle_x = x0 + wid / 2;

		app.stroke(255);
		for (int i = Math.min(0, this.head - headspace); i < Math.max(this.head + headspace, this.tape.length + 1); i++) {
			x = middle_x + cs * (i - offset);

			if (x < x0 || x >= x0 + wid)
				continue;

			app.fill(0);
			app.rect(x, y, cs, cs);
			app.fill(255);
			app.text(getTape(i), x + cs / 2, y + cs * 0.4f);
		}

		if (tapeCounter == 1) {
			tapeCounter = 0;
			tmpHead = this.head;
			changing = false;
		} else if (changing) {
			tapeCounter += 0.1;
		}
	}
}
