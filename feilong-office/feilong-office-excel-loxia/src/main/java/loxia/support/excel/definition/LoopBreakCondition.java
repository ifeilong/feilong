package loxia.support.excel.definition;

public class LoopBreakCondition {
	private int rowOffset;
	private int colOffset;
	private String flagString;
	public int getRowOffset() {
		return rowOffset;
	}
	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}
	public int getColOffset() {
		return colOffset;
	}
	public void setColOffset(int colOffset) {
		this.colOffset = colOffset;
	}
	public String getFlagString() {
		return flagString;
	}
	public void setFlagString(String flagString) {
		this.flagString = flagString;
	}
	
	public LoopBreakCondition cloneCondition(){
		LoopBreakCondition condition = new LoopBreakCondition();
		condition.setRowOffset(rowOffset);
		condition.setColOffset(colOffset);
		condition.setFlagString(flagString);
		return condition;
	}
}
