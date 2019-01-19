package pw.jawedyx.tensortest.detail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import pw.jawedyx.tensortest.App;
import pw.jawedyx.tensortest.room.entities.User;
import pw.jawedyx.tensortest.utils.Constants;
import pw.jawedyx.tensortest.utils.DataInfo;
import pw.jawedyx.tensortest.utils.StringHelper;

public class DetailViewModel extends ViewModel {

    private final String SAVED_PIC_DIR_NAME = "tensortest";

    /**
     * Объект Picasso для сохранения выбраных аватарок
     * в кэш папку приложения
     */
    private Target picassoTarget = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(() -> {
                File direct = new File(Environment.getExternalStorageDirectory() + File.separator + SAVED_PIC_DIR_NAME);

                if (!direct.exists()) {
                    File wallpaperDirectory = new File( File.separator + "sdcard" +  File.separator + SAVED_PIC_DIR_NAME +  File.separator);
                    wallpaperDirectory.mkdirs();
                }

                File file = new File(
                        new File(File.separator + "sdcard" +  File.separator + SAVED_PIC_DIR_NAME +  File.separator),
                        StringHelper.generateName(10)
                );

                if (file.exists()) {
                    file.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    avatarURI = file.toURI().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            loadedAvatar.postValue(bitmap);
        }
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            info.postValue(new DataInfo("Не удалось загрузить аватар пользователя!", 1, "onBitmapFailed"));
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public String avatarURI = "";
    public MutableLiveData<Bitmap> loadedAvatar = new MutableLiveData<>();
    public MutableLiveData<DataInfo> info = new MutableLiveData<>();

    /**
     * Вставка в БД после валидации в методе {@code validateUser}
     */
    private void editUser(String fn, String ln, String mn, String phone, String workPhone, String birth, String position, Constants.Role role, long userId) {
        User replaceUser;

        if(role == Constants.Role.FRIEND){
            replaceUser = new User(avatarURI, fn, ln, (!TextUtils.isEmpty(mn)? mn: ""), birth, phone, "", "", Constants.Role.FRIEND);
        }else{
            replaceUser = new User(avatarURI, fn, ln, (!TextUtils.isEmpty(mn)? mn: ""), "", phone, workPhone, position, Constants.Role.COLLEAGUE);
        }

        replaceUser.setId(userId);

        AsyncTask.execute(()->{
            long success = -1;
            success = App.getDb().userDao().insert(replaceUser);
            if (success != -1) {
                info.postValue(new DataInfo("editUser"));
            }
            else {
                info.postValue(new DataInfo("Не удалось добавить пользователя", 1, "editUser"));
            }
        });
    }

    /**
     * Проверка условий, если все условия пройдены,
     * выполняется вставка в базу методом {@code editUser}
     * @param fn Имя пользовователя из формы
     * @param ln Фамилия пользователя из формы
     * @param mn Отчество пользователя из формы
     * @param phone Телефон пользователя из формы
     * @param workPhone Рабочий телефон пользователя из формы
     * @param birth День рождения пользователя из формы
     * @param position Должность пользователя из формы
     * @param role Роль пользователя из интента
     * @param userId ID редактирования в базе данных, 0 если добавление данных
     */
    public void validateUser(String fn, String ln, String mn, String phone, String workPhone, String birth, String position, Constants.Role role, long userId) {

        fn = fn.trim();
        ln = ln.trim();
        mn = mn.trim();

        if(TextUtils.isEmpty(fn) || TextUtils.isEmpty(ln)) {
            info.postValue(new DataInfo("Имя и Фамилия обязательны для заполнения!", 1, "validateUser"));
            return;
        }

        if(fn.contains(" ") || ln.contains(" ") || mn.contains(" ")) {
            info.postValue(new DataInfo("ФИО не должно содержать пробелы", 1, "validateUser"));
            return;
        }

        if(TextUtils.isEmpty(phone)) {
            info.postValue(new DataInfo("Поле \"Мобильный телефон\" обязательно для заполнения!", 1, "validateUser"));
            return;
        }

        if(!birth.contains(".") && role == Constants.Role.FRIEND) {
            info.postValue(new DataInfo("Выберите дату рождения", 1, "validateUser"));
            return;
        }

        if(TextUtils.isEmpty(workPhone) && role == Constants.Role.COLLEAGUE) {
            info.postValue(new DataInfo("Поле \"Рабочий телефон\" обязательно для заполнения!", 1, "validateUser"));
            return;
        }

        if(TextUtils.isEmpty(position) && role == Constants.Role.COLLEAGUE) {
            info.postValue(new DataInfo("Заполните поле позиции", 1, "validateUser"));
            return;
        }

        editUser(fn, ln, mn, phone, workPhone, birth, position, role, userId);
    }

    public Target getPicassoTarget() {
        return picassoTarget;
    }
}
