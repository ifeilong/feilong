package loxia.support.excel.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import loxia.support.excel.ExcelUtil;

public class ExcelCell {
	private int row;
	private int col;
	private String dataName;
	private String dataExpr;
	private String type;
	private boolean isMandatory;
	private String[] availableChoices;
	private String pattern;
	private List<ExcelCellConditionStyle> styles = new ArrayList<ExcelCellConditionStyle>();
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public String[] getAvailableChoices() {
		return availableChoices;
	}
	public void setAvailableChoices(String[] availableChoices) {
		this.availableChoices = availableChoices;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getChoiceString(){
		if(getAvailableChoices() == null || getAvailableChoices().length == 0) return "";
		StringBuffer sb = new StringBuffer();
		for(String str: getAvailableChoices())
			sb.append("," + str);
		return sb.substring(1);
	}
	public void setChoiceString(String choiceString){
		if(choiceString != null && choiceString.length() > 0)
			setAvailableChoices(choiceString.split(","));
	}
	public String getCellIndex(){
		return ExcelUtil.getCellIndex(row, col);
	}
	
	public void setCellIndex(String cellIndex){
		int[] value = ExcelUtil.getCellPosition(cellIndex);
		this.row = value[0];
		this.col = value[1];
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDataExpr() {
		return dataExpr;
	}
	public void setDataExpr(String dataExpr) {
		if(dataExpr == null || dataExpr.trim().length() == 0)
			this.dataExpr = null;
		else
			this.dataExpr = dataExpr;
	}
	
	public ExcelCell cloneCell(){
		ExcelCell cell = new ExcelCell();
		
		cell.setAvailableChoices(availableChoices == null ? null : Arrays.asList(availableChoices).toArray(new String[0]));
		cell.setCol(col);
		cell.setDataExpr(dataExpr);
		cell.setDataName(dataName);
		cell.setMandatory(isMandatory);
		cell.setPattern(pattern);
		cell.setRow(row);
		cell.setType(type);
		
		for(ExcelCellConditionStyle style: getStyles())
			cell.addStyle(style.cloneStyle());
		return cell;
	}
}
