package pw.jawedyx.tensortest;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;

import pw.jawedyx.tensortest.room.entities.User;
import pw.jawedyx.tensortest.utils.Constants;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<User>> lData;

    /**
     * Метод инициализирует тестовые данные
     * @return обновленные данные
     */
    public MutableLiveData<List<User>> getData() {
        if(lData == null){
            lData = new MutableLiveData<>();

            AsyncTask.execute(() -> {
                if(App.getDb().userDao().getById(1) == null && App.getDb().userDao().getById(2) == null){
                    User user = new User("avatar", "Антон", "Лещев", "Романович", "07.09.1997", "+79108111424", "", "", Constants.Role.FRIEND);
                    User user2 = new User("avatar2", "Иван", "Иванов", "Иванович", "", "+7133713228", "10-49-40", "Ведущий разработчик", Constants.Role.COLLEAGUE);
                    App.getDb().userDao().insert(user);
                    App.getDb().userDao().insert(user2);
                }
            });

            AsyncTask.execute(() -> {
                lData.postValue(App.getDb().userDao().getAll());
            });
        }
        return lData;
    }

    /**
     * Обновляет данные из БД
     */
    public void reload() {
        AsyncTask.execute(() -> {
            lData.postValue(App.getDb().userDao().getAll());
        });
    }
}
