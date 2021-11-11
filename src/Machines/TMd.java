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
	private float transition;
	private int prevHead = -1;
	private String prevInstruction;
	
	private static int MAX_CELLS = 25;
	
	public TMd(Path code) throws SyntaxError, IOException, RuntimeError {
		super(code);
		
		prevHead = head;
		prevInstruction = getCurrentInstruction();
	}

	public TMd(Path code, String initialState) throws SyntaxError, IOException, RuntimeError {
		super(code, initialState);
		
		prevHead = head;
		prevInstruction = getCurrentInstruction();
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

		int columns = 35;
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

	public void show(float x0, float y0, float wid, float hei, int headspace, PApplet app){		
		float cs = Math.max(wid/MAX_CELLS, hei);

		int len = getTapeLength();
		int head = getHead();
		
		int leftIndex, rightIndex;
		if(head != prevHead) {
			leftIndex = (prevHead < MAX_CELLS/2.0)? 0 : (int)Math.floor(head-MAX_CELLS/2.0);
			rightIndex = (len-prevHead < MAX_CELLS/2.0)? len : (int)Math.ceil(head+MAX_CELLS/2.0);
			
			app.pushMatrix();
			app.translate(PApplet.lerp(Math.signum(head-prevHead)*cs, 0, transition), 0);
		}else {
			leftIndex = (prevHead < MAX_CELLS/2.0)? 0 : (int)Math.floor(prevHead-MAX_CELLS/2.0);
			rightIndex = (len-prevHead < MAX_CELLS/2.0)? len : (int)Math.ceil(prevHead+MAX_CELLS/2.0);
		}
		
		float middlePoint = (x0+wid/2) - cs/2;
		
		app.textAlign(PApplet.CENTER, PApplet.BOTTOM);
		for(int i = head, index = 0; i>=leftIndex; i--, index++) {
			String val = getTape(i);
			
			if(val.equals("1")) {
				app.fill(255, 0, 0, 50);
			}else {
				app.noFill();
			}
			
			app.rect(middlePoint-index*cs, y0, cs, cs);
			
			app.fill(255);
			app.text(val, middlePoint-index*cs + cs/2.0f, y0+cs/2);
		}
		
		for(int i = head+1, index = 1; i<rightIndex; i++, index++) {
			String val = getTape(i);
			
			if(val.equals("1")) {
				app.fill(255, 0, 0, 50);
			}else {
				app.noFill();
			}
			
			app.rect(middlePoint+index*cs, y0, cs, cs);
			
			app.fill(255);
			app.text(val, middlePoint+index*cs + cs/2.0f, y0+cs/2);
		}

		if(head != prevHead) {
			app.popMatrix();
		}
		
		app.stroke(255);
		app.strokeWeight(5);
		app.noFill();
		needle(x0+wid/2.0f, y0+hei*1.75f, cs, getState(), app);
		app.strokeWeight(1);
	}
	
	public void animateTape(float fps, PApplet app) throws RuntimeError {
		if(head != prevHead) {
			if(transition == 0) transition = 0.01f;
			else transition += 5/(app.frameRate+(fps-10)*2);
			
			if(transition > 0.99) {
				transition = 0;
				prevHead = head;
				prevInstruction = getCurrentInstruction();
			}
		}
	}
	
	public boolean finishedTransition() {
		return head == prevHead || transition > 0.99 ;
	}
	
	public String getInstruction() throws RuntimeError {
		if(!finishedTransition() || transition <= 0.5)
			return prevInstruction;
		else
			return getCurrentInstruction();
	}
	
	public void resetAnimation() {
		prevHead = head;
		transition = 1;
	}

	void needle(float x, float y, float size, String state, PApplet app){
		float xOffset = x-size/2;
		float yOffset = y-size/2;
		
		app.beginShape();
		app.vertex(size/2  + xOffset, -size/2   + yOffset);
		app.vertex(size    + xOffset, size/3    + yOffset);
		app.vertex(size    + xOffset, size      + yOffset);
		app.vertex(0       + xOffset, size      + yOffset);
		app.vertex(0       + xOffset, size/3    + yOffset);
		app.endShape(PApplet.CLOSE);

		app.fill(255);
		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.text(state, size/2 + xOffset, size/2 + yOffset);
	}
}
