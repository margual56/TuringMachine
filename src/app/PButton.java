package app;

import processing.core.PApplet;

/**
 * @author marcos
 *
 * A custom button that executes a lambda function when pressed.
 * It also implements variables for the fg and bg colors, as well as margins
 */
public class PButton {
	/**
	 * Horizontal margin
	 */
	private static final float DEFAULT_MARGIN_X = 0.4f;
	
	/**
	 * Vertical margin
	 */
	private static final float DEFAULT_MARGIN_Y = 1f;
	
	/**
	 * Top left corner's position and the size of the button.
	 * The size is calculated based on the font size and the margins.
	 */
	private float x, y, width, height;
	
	/**
	 * Text shown in the button
	 */
	private String text;
	
	/**
	 * Colors for the button
	 */
	private int bgColor, fgColor, hoverColor, hoverStrokeColor, strokeWidth, strokeColor, fontSize;
	
	/**
	 * Lambda function that runs when the button is pressed 
	 * (when the function {@link #pressed(float, float, PApplet)} is called
	 */
	private Action onPressed;

	/**
	 * @param x Horizontal position of the top-left corner of the box
	 * @param y Vertical position of the top-left corner of the box
	 * @param text Text to show in the button
	 * @param app {@link PApplet} to draw to. Used to get the font size, window size and more.
	 * @param act Lambda function to run when the button is {@link #pressed(float, float, PApplet)}
	 */
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
	
	/**
	 * Draws the button
	 * 
	 * @param app {@link PApplet} to draw to
	 */
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
	
	/**
	 * Draw the button with the hovered effect
	 * 
	 * @param app {@link PApplet} to draw to
	 */
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
	
	/**
	 * @param mX Mouse X position
	 * @param mY Mouse Y position
	 * @return Is the mouse over the button
	 */
	public boolean hovered(float mX, float mY) {
		return  mX > x && mX < x+width &&
				mY > y && mY < y+height;
	}

	/**
	 * Run the lambda function
	 * 
	 * @param mX Mouse X position
	 * @param mY Mouse Y position
	 * @param app {@link PApplet} to pass to the lambda function
	 */
	public void pressed(float mX, float mY, PApplet app) {
		this.onPressed.pressed(mX, mY, app);
	}
	
	/**
	 * @return X position of the top-left corner of the button
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return Y position of the top-left corner of the button
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param x New X position of the top-left corner of the button
	 * @param y New Y position of the top-left corner of the button
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return Width of the button
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width New width for the button
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return Height of the button
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height New height of the button
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return Text of the button
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text New text of the button
	 * @param app {@link PApplet} used to get window size, font size and more
	 */
	public void setText(String text, PApplet app) {
		this.text = text;

		app.textSize(this.fontSize);
		
		this.width = app.textWidth(text) * (1 + DEFAULT_MARGIN_X);
		this.height =  (app.textAscent() + app.textDescent()) * (1 + DEFAULT_MARGIN_Y);
	}

	/**
	 * @param bgColor New background color
	 */
	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @param fgColor New foreground color
	 */
	public void setFgColor(int fgColor) {
		this.fgColor = fgColor;
	}

	/**
	 * @param strokeWidth New stroke width
	 */
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * @param strokeColor New stroke color
	 */
	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Recalculates the width and height based on the font size (text size) and margins
	 * 
	 * @param fontSize New font size
	 * @param app {@link PApplet} used to get window size, font size and more
	 */
	public void setFontSize(int fontSize, PApplet app) {
		this.fontSize = fontSize;

		app.textSize(this.fontSize);
		
		this.width = app.textWidth(text) * (1 + DEFAULT_MARGIN_X);
		this.height =  (app.textAscent() + app.textDescent()) * (1 + DEFAULT_MARGIN_Y);
	}

	/**
	 * @author marcos
	 *
	 * Interface containing the definition for the lambda function
	 */
	public interface Action {
	    void pressed(float mX, float mY, PApplet app);
	}
}
