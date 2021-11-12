package app;

import processing.core.PApplet;

public class PButton {
	private static final float DEFAULT_MARGIN_X = 0.4f;
	private static final float DEFAULT_MARGIN_Y = 1f;
	
	private float x, y, width, height;
	private String text;
	private int bgColor, fgColor, hoverColor, hoverStrokeColor, strokeWidth, strokeColor, fontSize;
	private Action onPressed;

	public PButton(float x, float y, String text, PApplet app, Action act) {
		this.x = x;
		this.y = y;
		this.text = text;

		this.bgColor = 255;
		this.hoverColor = 200;
		this.hoverStrokeColor = 0;
		this.fgColor = 0;
		this.strokeWidth = 1;
		this.strokeColor = -1;
		
		this.fontSize = MainWindow.DEFAULT_FONT_SIZE;
		app.textSize(this.fontSize);
		
		this.width = app.textWidth(text) * (1 + DEFAULT_MARGIN_X);
		this.height = (app.textAscent() + app.textDescent()) * (1 + DEFAULT_MARGIN_Y);
		
		this.onPressed = act;
	}
	
	public void draw(PApplet app) {
		app.fill(bgColor);
		
		if(strokeColor != -1) {
			app.strokeWeight(strokeWidth);
			app.stroke(strokeColor);
		}
		
		app.rect(x, y, width, height);
		app.strokeWeight(1);
		
		app.textSize(fontSize);
		app.fill(fgColor);
		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.text(text, x+width/2.0f, y+height/2.0f);

	}
	
	public void drawHovered(PApplet app) {
		app.fill(hoverColor);
		
		app.strokeWeight(strokeWidth);
		app.stroke(hoverStrokeColor);
		
		app.rect(x, y, width, height);
		app.strokeWeight(1);
		
		app.textSize(fontSize);
		app.fill(fgColor);
		app.textAlign(PApplet.CENTER, PApplet.CENTER);
		app.text(text, x+width/2.0f, y+height/2.0f);
	}
	
	public boolean hovered(float mX, float mY) {
		return  mX > x && mX < x+width &&
				mY > y && mY < y+height;
	}

	public void pressed(float mX, float mY, PApplet app) {
		this.onPressed.pressed(mX, mY, app);
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getText() {
		return text;
	}

	public void setText(String text, PApplet app) {
		this.text = text;

		app.textSize(this.fontSize);
		
		this.width = app.textWidth(text) * (1 + DEFAULT_MARGIN_X);
		this.height =  (app.textAscent() + app.textDescent()) * (1 + DEFAULT_MARGIN_Y);
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public void setFgColor(int fgColor) {
		this.fgColor = fgColor;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

	public void setFontSize(int fontSize, PApplet app) {
		this.fontSize = fontSize;

		app.textSize(this.fontSize);
		
		this.width = app.textWidth(text) * (1 + DEFAULT_MARGIN_X);
		this.height =  (app.textAscent() + app.textDescent()) * (1 + DEFAULT_MARGIN_Y);
	}

	public interface Action {
	    void pressed(float mX, float mY, PApplet app);
	}
}
