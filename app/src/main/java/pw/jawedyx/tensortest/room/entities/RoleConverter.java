package pw.jawedyx.tensortest.room.entities;

import android.arch.persistence.room.TypeConverter;


import pw.jawedyx.tensortest.utils.Constants;

/**
 * Конвертер ролей для объекта {@code User } в таблице {@code User}
 */
public class RoleConverter {

    @TypeConverter
    public static Constants.Role fromString(String value) {
        return Constants.Role.valueOf(value);
    }

    @TypeConverter
    public static String fromEnum(Constants.Role role) {
        return role.toString();
    }

}