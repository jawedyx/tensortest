package pw.jawedyx.tensortest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {


    /**
     * Преобразует строку в дату
     * @param inDate входная дата в виде строки
     * @return Дату если преобразование успешно, {@code null} если нет
     */
    public static Date ParseDate(String inDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.APP_DATE_FORMAT);
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return null;
        }
    }

    /**
     * Преобразует входную дату в строку
     * @param date объект даты
     * @return преобразованную дату
     */
    public static String dateToString(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.APP_DATE_FORMAT);
        return dateFormat.format(date);
    }


}
