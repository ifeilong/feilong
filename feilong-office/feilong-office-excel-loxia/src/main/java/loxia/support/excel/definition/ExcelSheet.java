package loxia.support.excel.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import loxia.support.excel.ExcelUtil;
import loxia.utils.StringUtil;

public class ExcelSheet{	

	private String name;
	private String displayName;
	private List<ExcelBlock> excelBlocks = new ArrayList<ExcelBlock>();
	private Boolean isOrdered = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<ExcelBlock> getExcelBlocks() {
		return excelBlocks;
	}
	public void setExcelBlocks(List<ExcelBlock> excelBlocks) {
		synchronized (isOrdered) {
			this.excelBlocks = excelBlocks;
			isOrdered = false;
		}		
	}	
	public ExcelBlock getExcelBlock(String start,String end){
		if(excelBlocks.size() == 0) return null;
		for(ExcelBlock b: excelBlocks){
			if(ExcelUtil.getCellIndex(b.getStartRow(), b.getStartCol()).equalsIgnoreCase(start.trim()) &&
					ExcelUtil.getCellIndex(b.getEndRow(), b.getEndCol()).equalsIgnoreCase(end.trim()))
				return b;
		}
		return null;
	}
	public ExcelBlock getExcelBlock(){
		if(excelBlocks.size() == 0) return null;
		return excelBlocks.iterator().next();
	}
	public void setExcelBlock(ExcelBlock excelBlock){
		synchronized (isOrdered) {
			excelBlocks.clear();
			excelBlocks.add(excelBlock);
			isOrdered = false;
		}
	}
	
	public void addExcelBlock(ExcelBlock excelBlock){
		synchronized (isOrdered) {
			excelBlocks.add(excelBlock);
			isOrdered = false;
		}
	}
	
	public List<ExcelBlock> getSortedExcelBlocks(){
		synchronized (isOrdered) {
			if(!isOrdered){
				Collections.sort(excelBlocks);
				isOrdered = true;
			}
		}
		return excelBlocks;
	}
	
	public ExcelSheet cloneSheet(){
		ExcelSheet excelSheet = new ExcelSheet();
		excelSheet.setName(name);
		excelSheet.setDisplayName(displayName);
		for(ExcelBlock block: getSortedExcelBlocks())
			excelSheet.addExcelBlock(block.cloneBlock());
		return excelSheet;
	}
	
	@Override
	public String toString() {
		return "ExcelSheet [name=" + name + ", excelBlocks=\r\n\t" + StringUtil.join(excelBlocks, "\r\n\t") + "]";
	}
}
