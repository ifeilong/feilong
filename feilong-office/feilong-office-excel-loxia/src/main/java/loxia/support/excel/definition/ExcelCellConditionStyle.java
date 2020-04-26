package loxia.support.excel.definition;

import loxia.support.excel.ExcelUtil;

public class ExcelCellConditionStyle {
	private int startRow = 0;
	private int startCol = 0;
	private int endRow = 0;
	private int endCol = 0;
	private String condition;
	private String cellIndex;
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getCellIndex() {
		return cellIndex;
	}
	public void setCellIndex(String cellIndex) {
		this.cellIndex = cellIndex;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getStartCol() {
		return startCol;
	}
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getEndCol() {
		return endCol;
	}
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}
	public String getStartCellIndex(){
		return ExcelUtil.getCellIndex(startRow, startCol);
	}
	
	public void setStartCellIndex(String startCellIndex){
		int[] value = ExcelUtil.getCellPosition(startCellIndex);
		this.startRow = value[0];
		this.startCol = value[1];
	}
	
	public String getEndCellIndex(){
		return ExcelUtil.getCellIndex(endRow, endCol);
	}
	
	public void setEndCellIndex(String endCellIndex){
		int[] value = ExcelUtil.getCellPosition(endCellIndex);
		this.endRow = value[0];
		this.endCol = value[1];
	}
	
	public ExcelCellConditionStyle cloneStyle(){
		ExcelCellConditionStyle style = new ExcelCellConditionStyle();
		style.setCellIndex(cellIndex);
		style.setCondition(condition);
		style.setEndCol(endCol);
		style.setEndRow(endRow);
		style.setStartCol(startCol);
		style.setStartRow(startRow);
		return style;
	}
}
