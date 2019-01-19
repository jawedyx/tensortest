package pw.jawedyx.tensortest.utils;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    public interface IPredicate<T> { boolean apply(T type); }

    /**
     * Реализация фильтра списка
     *
     * @param target Входные данные
     * @param predicate Фильтр
     * @return список, прошедший фильтрацию
     */
    public static <T> ArrayList<T> filter(List<T> target, IPredicate<T> predicate) {
        ArrayList<T> result = new ArrayList<T>();
        for (T element: target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
}
