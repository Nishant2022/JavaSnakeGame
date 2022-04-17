import java.awt.Dimension;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Snake extends JFrame {
	private static final int width = 858, height = 971;
	Board board;

	public Snake() {
		setSize(width, height);
		setPreferredSize(new Dimension(width, height));
		setTitle("Nishant's Snake Game Pre-Release 1");
		setResizable(false);
		board = new Board(this);
		add(board);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Snake();
	}

}
