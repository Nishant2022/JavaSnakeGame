import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class SnakePlayer {
	private LinkedList<Cell> snakeParts;
	private Cell head, food;
	@SuppressWarnings("unused")
	private Snake game;
	private Board board;
	private Cell[][] cells;
	private int cellSize = 46;
	private Direction direction;

	public SnakePlayer(Snake game, Board board, Cell[][] cells, Cell food) {
		this.game = game;
		this.board = board;
		this.cells = cells;
		this.food = food;
		snakeParts = new LinkedList<Cell>();
		head = new Cell(3, 8);
		cells[3][8].setCellType(CellType.SNAKE_PART);
		head.setCellType(CellType.SNAKE_PART);
		snakeParts.add(new Cell(3, 8));
		direction = Direction.RIGHT;
	}

	public void changeDirection(int e) {
		switch (e) {
		case KeyEvent.VK_UP:
			if (direction == Direction.DOWN)
				break;
			direction = Direction.UP;
			break;
		case KeyEvent.VK_DOWN:
			if (direction == Direction.UP)
				break;
			direction = Direction.DOWN;
			break;
		case KeyEvent.VK_RIGHT:
			if (direction == Direction.LEFT)
				break;
			direction = Direction.RIGHT;
			break;
		case KeyEvent.VK_LEFT:
			if (direction == Direction.RIGHT)
				break;
			direction = Direction.LEFT;
		}
	}

	public void move() {
		if (!next())
			return;
		snakeParts.add(0, new Cell(head.getRow(), head.getColumn()));
		cells[head.getRow()][head.getColumn()].setCellType(CellType.SNAKE_PART);
		Cell tail = snakeParts.getLast();
		cells[tail.getRow()][tail.getColumn()].setCellType(CellType.EMPTY);
		snakeParts.remove(snakeParts.size() - 1);

		if (head.isSameCell(food)) {
			int row = 0;
			int col = 0;
			if (snakeParts.size() > 1) {
				snakeParts.add(tail);
				cells[tail.getRow()][tail.getColumn()].setCellType(CellType.SNAKE_PART);
			} else {
				switch (direction) {
				case UP:
					col += 1;
					break;
				case DOWN:
					col += -1;
					break;
				case RIGHT:
					row += 1;
					break;
				case LEFT:
					row += -1;
					break;
				}
				snakeParts.add(new Cell(snakeParts.get(snakeParts.size() - 1).getRow() + row,
						snakeParts.get(snakeParts.size() - 1).getColumn() + col));
			}

			board.generateFood();
		}
	}

	private boolean next() {
		switch (direction) {
		case UP:
			if (head.getColumn() == 0) {
				board.lose();
				return false;
			} else if (Cell.positionOf(1, head, snakeParts) != -1) {
				board.lose();
				return false;
			}
			head.setColumn(head.getColumn() - 1);
			break;
		case DOWN:
			if (head.getColumn() == 16) {
				board.lose();
				return false;
			} else if (Cell.positionOf(1, head, snakeParts) != -1) {
				board.lose();
				return false;
			}
			head.setColumn(head.getColumn() + 1);
			break;
		case LEFT:
			if (head.getRow() == 0) {
				board.lose();
				return false;
			}
			if (Cell.positionOf(1, head, snakeParts) != -1) {
				board.lose();
				return false;
			}
			head.setRow(head.getRow() - 1);
			break;
		case RIGHT:
			if (head.getRow() == 16) {
				board.lose();
				return false;
			} else if (Cell.positionOf(1, head, snakeParts) != -1) {
				board.lose();
				return false;
			}
			head.setRow(head.getRow() + 1);
			break;
		}
		return true;

	}

	public void paintSnake(Graphics g) {
//		for (int i = 0; i < snakeParts.size(); i++) {
//			g.setColor(new Color(73 - i, 119 - i, 238 - i));
//			int currentRow = 0, currentColumn = 0;
//			int previousRow = 0, previousColumn = 0;
//			int nextRow = 0, nextColumn = 0;
//			int firstRow = snakeParts.getFirst().getRow(), firstColumn = snakeParts.getFirst().getColumn();
//			int lastRow = snakeParts.getLast().getRow(), lastColumn = snakeParts.getLast().getColumn();
//
//			if (snakeParts.size() == 1) {
//				g.fillOval(firstRow * cellSize + 35, firstColumn * cellSize + 35 + cellSize * 2, cellSize - 10,
//						cellSize - 10);
//				return;
//			}
//
//			if (snakeParts.size() > 3) {
//				if (i > 0 && i < snakeParts.size()) {
//					currentRow = snakeParts.get(i).getRow();
//					currentColumn = snakeParts.get(i).getColumn();
//					previousRow = snakeParts.get(i - 1).getRow();
//					previousColumn = snakeParts.get(i - 1).getColumn();
//					nextRow = snakeParts.get(i + 1).getRow();
//					nextColumn = snakeParts.get(i + 1).getColumn();
//					
//				}
//			}
//			if (i == 0) {
//				switch (direction) {
//				case UP:
//					g.fillArc(firstRow * cellSize + 35, firstColumn * cellSize + 30 + cellSize * 2, cellSize - 10,
//							cellSize, 0, 180);
//					break;
//				case DOWN:
//					g.fillArc(firstRow * cellSize + 35, firstColumn * cellSize + 30 + cellSize * 2, cellSize - 10,
//							cellSize, 180, 180);
//					break;
//				case LEFT:
//					g.fillArc(firstRow * cellSize + 30, firstColumn * cellSize + 35 + cellSize * 2, cellSize - 10,
//							cellSize, 90, 180);
//					break;
//				case RIGHT:
//					g.fillArc(firstRow * cellSize + 30, firstColumn * cellSize + 35 + cellSize * 2, cellSize - 10,
//							cellSize, 270, 180);
//					break;
//				}
//				return;
//			} else if (i == snakeParts.size() - 1) {
//
//			} else if (currentRow == previousRow && currentRow == nextRow) {
//				g.fillRect(cellSize * currentRow + 30, cellSize * currentColumn + 35 + cellSize * 2, cellSize,
//						cellSize - 10);
//			} else if (currentColumn == previousColumn && currentColumn == nextColumn) {
//				g.fillRect(cellSize * currentRow + 35, cellSize * currentColumn + 30 + cellSize * 2, cellSize - 10,
//						cellSize);
//			} else if (currentRow == previousRow && currentRow > nextRow) {
//
//			} else if (currentRow == previousRow && currentRow < nextRow) {
//
//			} else if (currentRow == nextRow && currentRow > previousRow) {
//
//			} else if (currentRow == nextRow && currentRow < previousRow) {
//
//			}
//		}
		for (Cell part : snakeParts) {
			g.setColor(new Color(53, 99, 218));
			g.fillRect(cellSize * part.getRow() + 30, cellSize * part.getColumn() + 30 + cellSize * 2, cellSize,
					cellSize);
			g.setColor(new Color(73, 119, 238));
			g.fillRect(cellSize * part.getRow() + 30 + 5, cellSize * part.getColumn() + 30 + cellSize * 2 + 5,
					cellSize - 10, cellSize - 10);
		}
	}

	private enum Direction {
		LEFT, RIGHT, UP, DOWN;
	}
}
