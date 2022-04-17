import java.util.LinkedList;

public class Cell {
	private int row, column;

	private CellType cellType;

	public Cell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public CellType getCellType() {
		return cellType;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isSameCell(Cell b) {
		if (this.getRow() == b.getRow()) {
			if (this.getColumn() == b.getColumn()) {
				return true;
			}
		}
		return false;
	}

	public boolean equals(Cell cell) {
		if (this.getRow() != cell.getRow()) {
			return false;
		}
		if (this.getColumn() != cell.getColumn()) {
			return false;
		}
		return true;
	}

	public static int positionOf(int i, Cell cell, LinkedList<Cell> cells) {
		for (; i < cells.size(); i++) {
			if (cell.equals(cells.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return row + ", " + column;
	}
}
