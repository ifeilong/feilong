package loxia.support.excel;

public interface WriteStatus {
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_READ_TEMPLATE_FILE_ERROR = 1;
	public static final int STATUS_SETTING_ERROR = 2;
	public static final int STATUS_WRITE_FILE_ERROR = 3;
	public static final int STATUS_SYSTEM_ERROR = 5;
	
	int getStatus();
	void setStatus(int status);
	String getMessage();
	void setMessage(String message);
}
