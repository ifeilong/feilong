package loxia.support.excel.definition;

import java.util.ArrayList;
import java.util.List;

import loxia.support.excel.ExcelUtil;

public class ExcelBlock implements Comparable<ExcelBlock>{
	public static final String LOOP_DIRECTION_HORIZONAL = "horizontal";
	public static final String LOOP_DIRECTION_VERTICAL = "vertical";
	
	private int startRow = 0;
	private int startCol = 0;
	private int endRow = 0;
	private int endCol = 0;
	private String dataName;
	private boolean isLoop = false;
	private String direction = LOOP_DIRECTION_HORIZONAL;
	private LoopBreakCondition breakCondition;
	private Class<? extends Object> loopClass;
	private List<ExcelCell> cells = new ArrayList<ExcelCell>();
	private List<ExcelCellConditionStyle> styles = new ArrayList<ExcelCellConditionStyle>();
	private boolean isChildBlock = false;
	private ExcelBlock childBlock;
	
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
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public boolean isLoop() {
		return isLoop;
	}
	public void setLoop(boolean isLoop) {
		this.isLoop = isLoop;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Class<? extends Object> getLoopClass() {
		return loopClass;
	}
	public void setLoopClass(Class<? extends Object> loopClass) {
		this.loopClass = loopClass;
	}
	public ExcelCell getCell(String position){
		if(cells.size() == 0) return null;
		for(ExcelCell cell: cells){
			if(ExcelUtil.getCellIndex(cell.getRow(), cell.getCol()).equalsIgnoreCase(position.trim()))
				return cell;
		}
		return null;
	}
	public List<ExcelCell> getCells() {
		return cells;
	}
	public void setCells(List<ExcelCell> cells) {
		this.cells = cells;
	}
	public void addCell(ExcelCell cell){
		this.cells.add(cell);
	}
	public ExcelBlock getChildBlock() {
		return childBlock;
	}
	public void setChildBlock(ExcelBlock childBlock) {
		if(childBlock == null){ this.childBlock = null; return; }
		childBlock.setLoop(true);
		childBlock.setEndCellIndex(this.getEndCellIndex());
		this.childBlock = childBlock;
	}
	public LoopBreakCondition getBreakCondition() {
		return breakCondition;
	}
	public void setBreakCondition(LoopBreakCondition breakCondition) {
		this.breakCondition = breakCondition;
	}
	public boolean isChild() {
		return isChildBlock;
	}
	public void setChild(boolean isChildBlock) {
		this.isChildBlock = isChildBlock;
	}
	
	public void setLoopClassByClassName(String className){
		try {
			setLoopClass(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(className + " is not found.");
		}
	}
	
	@Override
	public String toString() {
		return "ExcelBlock[" + getStartCellIndex() + ":" + getEndCellIndex() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		if(!(obj instanceof ExcelBlock)) return false;
		ExcelBlock eb = (ExcelBlock) obj;
		return this.toString().equals(eb.toString());
	}

	@Override
    public int compareTo(ExcelBlock eb) {
		int result = eb.getStartRow() - this.getStartRow();
		if(result == 0) result = eb.getStartCol() - this.getStartCol();
		return result;
	}

	public List<ExcelCellConditionStyle> getStyles() {
		return styles;
	}

	public void setStyles(List<ExcelCellConditionStyle> styles) {
		this.styles = styles;
	}
	public void addStyle(ExcelCellConditionStyle style){
		this.styles.add(style);
	}
	
	public ExcelBlock cloneBlock(){
		ExcelBlock block = new ExcelBlock();
		block.setBreakCondition(breakCondition == null ? null : breakCondition.cloneCondition());
		block.setChild(isChildBlock);		
		block.setDataName(dataName);
		block.setEndCol(endCol);
		block.setEndRow(endRow);
		block.setLoop(isLoop);
		block.setDirection(direction);
		block.setLoopClass(loopClass);
		block.setStartCol(startCol);
		block.setStartRow(startRow);
		
		for(ExcelCellConditionStyle style: getStyles())
			block.addStyle(style.cloneStyle());
		
		for(ExcelCell cell: getCells())
			block.addCell(cell.cloneCell());
		
		block.setChildBlock(childBlock == null? null : childBlock.cloneBlock());
		
		return block;
	}
}
