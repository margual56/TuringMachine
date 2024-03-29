package app;

import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * @author marcos
 *
 * Renders the text with antialiasing
 */
class SmoothLabel extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -488310624992033567L;

	public SmoothLabel(String text) {
        super(text);
    }
 
    /**
     * Render the text with antialiasing
     */
    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                         RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
    }
}