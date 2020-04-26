package loxia.support.excel.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import loxia.support.excel.ReadStatus;

public class DefaultReadStatus implements ReadStatus, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 567602698187676255L;
	
	private int status = STATUS_SUCCESS;
	private String message;
	private List<Exception> exceptions = new ArrayList<Exception>();
	
	@Override
    public int getStatus() {
		return status;
	}
	@Override
    public void setStatus(int status) {
		this.status = status;
	}
	@Override
    public String getMessage() {
		return message;
	}
	@Override
    public void setMessage(String message) {
		this.message = message;
	}
	@Override
    public List<Exception> getExceptions() {
		return exceptions;
	}
	@Override
    public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}
	@Override
    public void addException(Exception exception){
		this.exceptions.add(exception);
	}
}
