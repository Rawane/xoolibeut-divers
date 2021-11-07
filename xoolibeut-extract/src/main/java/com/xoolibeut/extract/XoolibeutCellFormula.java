package com.xoolibeut.extract;

import org.apache.poi.ss.usermodel.Cell;

public class XoolibeutCellFormula {
	private Cell cell;
	private String formula;
	public Cell getCell() {
		return cell;
	}
	public void setCell(Cell cell) {
		this.cell = cell;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
}
