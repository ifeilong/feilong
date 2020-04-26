package loxia.support.excel;

import java.util.List;

public interface ReadStatus {
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_READ_FILE_ERROR = 1;
	public static final int STATUS_SETTING_ERROR = 2;
	public static final int STATUS_SYSTEM_ERROR = 5;
	public static final int STATUS_DATA_COLLECTION_ERROR = 10;
	
	int getStatus();
	void setStatus(int status);
	String getMessage();
	void setMessage(String message);
	
	List<Exception> getExceptions();
	void setExceptions(List<Exception> exceptions);
	void addException(Exception exception);
}
