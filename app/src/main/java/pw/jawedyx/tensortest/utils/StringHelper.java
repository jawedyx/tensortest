package pw.jawedyx.tensortest.utils;

import java.util.Random;

/**
 * Функции для работы со строками
 */
public class StringHelper {

    /**
     * Генерирует строку из случайных латинских букв
     * @param targetStringLength длина генерируемой строки
     * @return сгенерированная строка
     */
    public static String generateName(int targetStringLength) {

        int leftLimit = 97; //'a'
        int rightLimit = 122; //'z'
        Random random = new Random();

        StringBuilder buffer = new StringBuilder(targetStringLength);

        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
