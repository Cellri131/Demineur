package ihm;
import javax.swing.*;

public class DemineurFrame extends JFrame {
	public DemineurFrame(int size) {
		super("Démineur");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new DemineurPanel(size));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}
}
