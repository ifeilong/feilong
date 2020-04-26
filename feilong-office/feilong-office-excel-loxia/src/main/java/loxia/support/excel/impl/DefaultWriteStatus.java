package loxia.support.excel.impl;

import java.io.Serializable;

import loxia.support.excel.WriteStatus;

public class DefaultWriteStatus implements WriteStatus, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5736231901454798780L;

	private int status = STATUS_SUCCESS;
	private String message;
	
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

}
