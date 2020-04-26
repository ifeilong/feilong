package loxia.support.excel.exception;

import java.util.Arrays;

public class ExcelManipulateException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 478553091122313602L;
	private int errorCode;
	
	/**
	 * [SheetNo,Position,CurrentValue,Pattern,ChoiceList]
	 */
	private Object[] args;

	public ExcelManipulateException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public ExcelManipulateException(int errorCode, Object[] args) {
		this.errorCode = errorCode;
		this.args = args;
	}

	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "ExcelManipulateException[" + this.errorCode + "]" +
			(this.args == null ? "" : Arrays.asList(this.args).toString());
	}
	
	
}
