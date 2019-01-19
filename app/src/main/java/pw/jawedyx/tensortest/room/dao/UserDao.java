package pw.jawedyx.tensortest.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import pw.jawedyx.tensortest.room.entities.User;

/**
 * Интерфейс для взаимодействия с таблицей User в БД
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("SELECT * FROM User WHERE id = :id")
    User getById(long id);

}
