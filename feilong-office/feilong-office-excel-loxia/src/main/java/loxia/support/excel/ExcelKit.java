package loxia.support.excel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import loxia.support.excel.exception.ExcelManipulateException;

public class ExcelKit{

    public static final String                         DEFAULT_MESSAGE        = "excelkit_message_default";

    public static final String                         READ_STATUS_PREFIX     = "readStatus";

    public static final String                         WRITE_STATUS_PREFIX    = "writeStatus";

    public static final String                         EXCEL_EXCEPTION_PREFIX = "emException";

    public static final String                         XLS                    = "xls";

    public static final String                         XLSX                   = "xlsx";

    public static final String                         DATA_TYPE_INTEGER      = "integer";

    public static final String                         DATA_TYPE_LONG         = "long";

    public static final String                         DATA_TYPE_BIGDECIMAL   = "bigdecimal";

    public static final String                         DATA_TYPE_STRING       = "string";

    public static final String                         DATA_TYPE_DATE         = "date";

    public static final String                         DATA_TYPE_BOOLEAN      = "boolean";

    private final Set<Locale>                          defaultMissResources   = new HashSet<Locale>();

    private final Set<Locale>                          customMissResources    = new HashSet<Locale>();

    private final Map<MessageFormatKey, MessageFormat> mfMap                  = new HashMap<MessageFormatKey, MessageFormat>();

    private static ExcelKit                            instance;

    private ExcelKit(){

    };

    public Workbook createWorkbook(){
        return createWorkbook(null);
    }

    public Workbook createWorkbook(String type){
        if (XLSX.equals(type) || type == null){
            return new XSSFWorkbook();
        }else if (XLS.equals(type)){
            return new HSSFWorkbook();
        }else
            throw new IllegalArgumentException();
    }

    public static ExcelKit getInstance(){
        if (instance == null)
            instance = new ExcelKit();
        return instance;
    }

    private String customResource;

    public void setCustomResource(String resourceName){
        this.customResource = resourceName;
    }

    public String getReadStatusMessages(ExcelManipulateException e,Locale locale){
        return getMessage(EXCEL_EXCEPTION_PREFIX + "." + e.getErrorCode(), locale, e.getArgs());
    }

    public List<String> getReadStatusMessages(ReadStatus readStatus,Locale locale){
        List<String> result = new ArrayList<String>();
        result.add(getMessage(READ_STATUS_PREFIX + "." + readStatus.getStatus(), locale, null));
        if (readStatus.getStatus() != ReadStatus.STATUS_SUCCESS){
            if (readStatus.getStatus() == ReadStatus.STATUS_DATA_COLLECTION_ERROR){
                for (Exception e : readStatus.getExceptions()){
                    if (e instanceof ExcelManipulateException){
                        ExcelManipulateException eme = (ExcelManipulateException) e;
                        result.add(getMessage(EXCEL_EXCEPTION_PREFIX + "." + eme.getErrorCode(), locale, eme.getArgs()));
                    }
                }
            }else
                result.add(readStatus.getMessage());
        }
        return result;
    }

    public List<String> getWriteStatusMessages(WriteStatus writeStatus,Locale locale){
        List<String> result = new ArrayList<String>();
        result.add(getMessage(WRITE_STATUS_PREFIX + "." + writeStatus.getStatus(), locale, null));
        if (writeStatus.getStatus() != ReadStatus.STATUS_SUCCESS){
            result.add(writeStatus.getMessage());
        }
        return result;
    }

    public String getMessage(String key,Locale locale,Object[] params){
        ResourceBundle bundle = getCustomBundle(locale);
        String value = null;
        if (bundle != null){
            try{
                value = bundle.getString(key);
            }catch (MissingResourceException e){}
        }
        if (value == null){
            bundle = getDefaultBundle(locale);
            if (bundle != null){
                try{
                    value = bundle.getString(key);
                }catch (MissingResourceException e){}
            }
        }
        if (value == null)
            return key;
        MessageFormatKey mkey = new MessageFormatKey(value, locale);
        MessageFormat format = mfMap.get(mkey);
        if (format == null){
            format = new MessageFormat(value, locale);
            mfMap.put(mkey, format);
        }
        return format.format(params);
    }

    public ResourceBundle getDefaultBundle(Locale locale){
        synchronized (defaultMissResources){
            try{
                if (!defaultMissResources.contains(locale)){
                    return ResourceBundle.getBundle(DEFAULT_MESSAGE, locale, Thread.currentThread().getContextClassLoader());
                }
            }catch (MissingResourceException e){
                defaultMissResources.add(locale);
            }
        }
        return null;
    }

    public ResourceBundle getCustomBundle(Locale locale){
        synchronized (customMissResources){
            if (customResource == null)
                return null;
            try{
                if (!customMissResources.contains(locale)){
                    return ResourceBundle.getBundle(customResource, locale, Thread.currentThread().getContextClassLoader());
                }
            }catch (MissingResourceException e){
                customMissResources.add(locale);
            }
        }
        return null;
    }

    static class MessageFormatKey{

        String pattern;

        Locale locale;

        MessageFormatKey(String pattern, Locale locale){
            this.pattern = pattern;
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o){
            if (this == o)
                return true;
            if (!(o instanceof MessageFormatKey))
                return false;

            final MessageFormatKey messageFormatKey = (MessageFormatKey) o;

            if (locale != null ? !locale.equals(messageFormatKey.locale) : messageFormatKey.locale != null)
                return false;
            if (pattern != null ? !pattern.equals(messageFormatKey.pattern) : messageFormatKey.pattern != null)
                return false;

            return true;
        }

        @Override
        public int hashCode(){
            int result;
            result = (pattern != null ? pattern.hashCode() : 0);
            result = 29 * result + (locale != null ? locale.hashCode() : 0);
            return result;
        }
    }
}
