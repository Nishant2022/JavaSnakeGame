import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener, KeyListener, MouseListener {
	private Cell[][] cells = new Cell[17][17];
	private final int cellSize = 46;
	private Snake game;
	private Timer timer;
	private SnakePlayer player;
	private Cell food;
	private int score = -1;
	private static int bestScore = 0, tempScore = 0;
	private Queue<Integer> queue;
	private Status status = Status.MENU;
	private boolean firstGame = true;
	private Image apple, trophy;
	private ImageObserver observer;

	public Board(Snake game) {
		this.game = game;
		String appleResource = "images/SnakeGameApple.png";
		String trophyResource = "images/SnakeGameTrophy.png";
		apple = ResourceLoader.getImage(appleResource);
		trophy = ResourceLoader.getImage(trophyResource);
		setFocusable(true);
		addKeyListener(this);
		reset();
		timer.start();
	}

	private enum Status {
		START, END, MENU, WAIT
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(74, 117, 44));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		g.setColor(new Color(87, 138, 52));
		g.fillRect(0, cellSize * 2, game.getWidth(), game.getHeight());
		g.drawImage(apple, 30, 22, 66, 68, 0, 0, apple.getWidth(observer), apple.getHeight(observer), observer);

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
					g.setColor(new Color(167, 217, 72));
					g.fillRect(cellSize * i + 30, cellSize * j + 30 + cellSize * 2, cellSize, cellSize);
				} else {
					g.setColor(new Color(142, 204, 57));
					g.fillRect(cellSize * i + 30, cellSize * j + 30 + cellSize * 2, cellSize, cellSize);
				}
			}
		}

		player.paintSnake(g);
		paintFood(g);
		
		String scr = score + "";
		String bestscr = bestScore + "";
		String tempscr = tempScore + "";
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString(scr, 90, 4 * cellSize / 3);
		if (!firstGame) {
			g.drawImage(trophy, 160, 23, 200, 69, 0, 0, trophy.getWidth(observer), trophy.getHeight(observer),
					observer);
			bestscr = bestScore + "";
			g.setColor(Color.WHITE);
			g.drawString(bestscr, 210, 4 * cellSize / 3);
		}

		if (status == Status.END) {
			g.setColor(new Color(77, 193, 249));
			g.fillRoundRect(237, 329, 8 * cellSize, 8 * cellSize, cellSize, cellSize);
			g.setColor(new Color(17, 85, 204));
			g.fillRoundRect(329, 707, 4 * cellSize, cellSize, 25, 25);
			g.drawImage(apple, 259, 382, 304, 439, 0, 0, apple.getWidth(observer), apple.getHeight(observer), observer);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.PLAIN, 55));
			g.drawString(tempscr, 314, 432);
			g.drawString(bestscr, 482, 432);
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			FontMetrics playButtonFont = g.getFontMetrics();
			g.drawString("Play", 329 + (92 - playButtonFont.stringWidth("Play") / 2), 740);
			g.drawImage(trophy, 427, 391, 467, 437, 0, 0, trophy.getWidth(observer), trophy.getHeight(observer),
					observer);

		}
	}

	public void generateFood() {
		score++;
		int x = (int) (17 * Math.random());
		int y = (int) (17 * Math.random());
		while (cells[x][y].getCellType() != CellType.EMPTY) {
			x = (int) (17 * Math.random());
			y = (int) (17 * Math.random());
		}

		cells[x][y].setCellType(CellType.FOOD);
		food.setRow(x);
		food.setColumn(y);
	}

	public void paintFood(Graphics g) {
		int x = cellSize * food.getRow() + 35;
		int y = cellSize * food.getColumn() + 30 + cellSize * 2;
		g.drawImage(apple, x, y, x + 36, y + 46, 0, 0, apple.getWidth(observer), apple.getHeight(observer), observer);
	}

	public void lose() {
		tempScore = score;
		if (score > bestScore) {
			bestScore = score;
		}
		firstGame = false;
		status = Status.END;
		repaint();

		if (status == Status.END) {
			reset();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (status == Status.END)
			return;
		int i = e.getKeyCode();
		if (status == Status.WAIT && i == KeyEvent.VK_UP || i == KeyEvent.VK_DOWN || i == KeyEvent.VK_RIGHT) {
			status = Status.START;
		}
		queue.add(i);
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (status == Status.START) {
			if (queue.size() != 0) {
				player.changeDirection(queue.remove());
			}
			player.move();
			repaint();
		}
		if (!firstGame) {
			if (score > bestScore) {
				bestScore = score;
			}
		}
	}

	public void reset() {
		score = 0;
		addMouseListener(this);
		status = Status.END;
		queue = new LinkedList<Integer>();
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell(i, j);
				cells[i][j].setCellType(CellType.EMPTY);
			}
		}
		food = new Cell(12, 8);
		food.setCellType(CellType.FOOD);
		cells[12][8].setCellType(CellType.FOOD);
		player = new SnakePlayer(game, this, cells, food);
		timer = new Timer(100, this);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (status == Status.END) {
			if (e.getX() >= 329 && e.getX() <= 513) {
				if (e.getY() >= 707 && e.getY() <= 753) {
					status = Status.WAIT;
					repaint();
				}
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		;

	}

	@Override
	public void mouseExited(MouseEvent e) {
		;

	}

	public int getScore() {
		return score;
	}
}
