package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class HelpFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5546626617527424902L;

	private JPanel panel;
	private SmoothLabel label;
	
	private static String list = """
			<html>
				<p style='margin-top: 5'>spacebar &#8594; pause/resume (or restart if the execution has finished)</p>
				<p style='margin-top: 5'> right arrow &#8594; Advance just one instruction forward</p>
				<p style='margin-top: 5'> enter &#8594; Jump to the end of the execution</p>
				<p style='margin-top: 5'> r &#8594; Stop, load a new program and run it</p>
				<p style='margin-top: 5'> ESC &#8594; Exit cleanly</p>
				<p style='margin-top: 5'> + &#8594; Increase speed</p>
				<p style='margin-top: 5'> - &#8594; Decrease speed</p>
				<p style='margin-top: 5'> = &#8594; Restore initial speed</p>
			</html>
			""";
	
	public HelpFrame(String title) {
		super(title);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
           System.err.println(e);
        }
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(640, 200));
        panel.setBackground(new Color(44, 44, 44));
        panel.setOpaque(true);
        
        label = new SmoothLabel(list);

        label.setBounds(150, 100, 640-150, 480);
        label.setBackground(new Color(44, 44, 44));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.PLAIN, 14));
        label.setBorder(new EmptyBorder(0,50,0,0));//top,left,bottom,right
        panel.add(label);
        
        getContentPane().add(panel);
        setResizable(true);
        
        pack();
	}
	
	public static String getText() {
		return list;
	}
	
	public void setText(String text) {
		list = text;
		label.setText(text);
	}
	
}
