package pw.jawedyx.tensortest.utils;


/**
 * Объект для передачи информации между
 * {@code Activity} и {@code ViewModel}
 */
public class DataInfo {
    private String message;
    private int indicator;
    private String tag;

    public DataInfo(String tag) {
        this.message = "";
        this.indicator = 0;
        this.tag = tag;
    }

    public DataInfo(String message, int indicator) {
        this.message = message;
        this.indicator = indicator;
        this.tag = "default";
    }

    public DataInfo(String message, int indicator, String tag) {
        this.message = message;
        this.indicator = indicator;
        this.tag = tag;
    }

    //Getters
    public String getMessage() {
        return message;
    }

    public int getIndicator() {
        return indicator;
    }

    public String getTag() {
        return tag;
    }

    //Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
