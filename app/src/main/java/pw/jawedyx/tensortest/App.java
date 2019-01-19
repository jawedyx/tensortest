package pw.jawedyx.tensortest;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.squareup.picasso.Picasso;

import pw.jawedyx.tensortest.room.AppDatabase;

/**
 * Главный класс приложения для глобальных объектов
 */
public class App extends Application {

    private static App instance;
    private static AppDatabase db;
    private static Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();
        picasso = Picasso.with(getApplicationContext());
    }

    public static App getInstance() {
        return instance;
    }

    public static AppDatabase getDb() {
        return db;
    }
    public static Picasso getPicasso() {
        return picasso;
    }
}
