package pw.jawedyx.tensortest.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import pw.jawedyx.tensortest.room.dao.UserDao;
import pw.jawedyx.tensortest.room.entities.User;

import static pw.jawedyx.tensortest.utils.Constants.ROOM_VERSION;

/**
 * Класс описания БД
 */
@Database(entities = {User.class}, version = ROOM_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
