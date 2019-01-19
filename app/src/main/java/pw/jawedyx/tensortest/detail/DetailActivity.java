package pw.jawedyx.tensortest.detail;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pw.jawedyx.tensortest.App;
import pw.jawedyx.tensortest.R;
import pw.jawedyx.tensortest.room.entities.User;
import pw.jawedyx.tensortest.utils.Constants;
import pw.jawedyx.tensortest.utils.DateHelper;

public class DetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public final static String[] AVATAR_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public final static int AVATAR_PERMISSIONS_REQUEST = 320;
    public final static int DETAIL_REQUEST = 100;
    public final static int SELECT_AVATAR_REQUEST = 200;

    private enum OpenAs {
        EDIT, ADD
    }

    @BindView(R.id.toolbar_detail)
    Toolbar mToolbarDetail;
    @BindView(R.id.civ_detail_avatar)
    CircleImageView mCivDetailAvatar;
    @BindView(R.id.et_first_name)
    EditText mEtFirstName;
    @BindView(R.id.et_last_name)
    EditText mEtLastName;
    @BindView(R.id.et_middle_name)
    EditText mEtMiddleName;
    @BindView(R.id.iv_date_picker)
    ImageView mIvDatePicker;
    @BindView(R.id.et_birthday)
    TextView mTvBirthday;
    @BindView(R.id.cl_friend_block)
    ConstraintLayout mClFriendBlock;
    @BindView(R.id.et_work_phone)
    EditText mEtWorkPhone;
    @BindView(R.id.et_position)
    EditText mEtPosition;
    @BindView(R.id.cl_coll_block)
    ConstraintLayout mClCollBlock;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.cl_detail_content)
    ConstraintLayout mClDetailContent;
    @BindView(R.id.btn_detail_save)
    Button mBtnDetailSave;
    @BindView(R.id.civ_change_avatar)
    ImageView mCivChangeAvatar;
    @BindView(R.id.cl_root_view)
    ConstraintLayout clRootView;

    private DetailViewModel mDetailViewModel;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbarDetail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        calendar = Calendar.getInstance();

        if (getIntent().getSerializableExtra("openAs") == OpenAs.EDIT) { //Если редактирование
            if (actionBar != null)
                actionBar.setTitle(getResources().getString(R.string.title_edit));

            User user = getIntent().getParcelableExtra("user");
            updateUI(user); //Обновяем UI с данными пользователя из интента
        } else { //Если добавление
            Constants.Role role = (Constants.Role) getIntent().getSerializableExtra("role");
            //Меняем UI, в зависимости от роли
            if (actionBar != null)
                actionBar.setTitle(getResources().getString(R.string.title_add) + ((role == Constants.Role.FRIEND) ? " " + getString(R.string.title_add_friend) : " " + getString(R.string.title_add_colleague)));
            updateUI(role);
        }

        //Выбор новой фотографии
        mCivChangeAvatar.setOnClickListener(v -> {

            if (hasPermissions(this, AVATAR_PERMISSIONS)){
                selectAvatar();
            }
            else {
                ActivityCompat.requestPermissions(this, AVATAR_PERMISSIONS, AVATAR_PERMISSIONS_REQUEST);
            }

        });

        //Выбор новой даты рождения
        mIvDatePicker.setOnClickListener(v -> {
            new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        mDetailViewModel.loadedAvatar.observe(this, avatar -> {
            if (avatar != null) {
                mCivDetailAvatar.setImageBitmap(avatar);
            }
        });
        mDetailViewModel.info.observe(this, info -> {
            if (info != null) {
                switch (info.getTag()){

                    case "validateUser":
                        switch (info.getIndicator()) {
                            case 1:
                                showSnakbar(clRootView, info.getMessage());
                                break;
                        }
                        break;

                    case "editUser":
                        switch (info.getIndicator()) {
                            case 0:
                                setResult(RESULT_OK);
                                finish();
                                break;
                            case 1:
                                showSnakbar(clRootView, info.getMessage());
                                break;
                        }
                        break;

                    case "onBitmapFailed":
                        switch (info.getIndicator()) {
                            case 1:
                                showSnakbar(clRootView, info.getMessage());
                                break;
                        }
                        break;
                }

                mDetailViewModel.info.postValue(null);
            }
        });
    }

    /**
     * UI добавления
     * @param role Для кого обновить UI
     */
    private void updateUI(Constants.Role role) {
        if (role == Constants.Role.FRIEND) {
            mClCollBlock.setVisibility(View.GONE);
        } else {
            mClFriendBlock.setVisibility(View.GONE);
        }
        mBtnDetailSave.setText(getResources().getText(R.string.title_add));
    }

    /**
     * Выбор аватара
     */
    private void selectAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_AVATAR_REQUEST);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String result = DateHelper.dateToString(calendar.getTime());
        mTvBirthday.setText(result);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @OnClick(R.id.btn_detail_save)
    void onClickSaveBnt(View v) {

        Constants.Role role = (getIntent().hasExtra("user")?
                ((User) getIntent().getParcelableExtra("user")).getRole() : //Если редактирование
                (Constants.Role)getIntent().getSerializableExtra("role")); //Если добавление
        long id = (getIntent().hasExtra("user")?
                ((User) getIntent().getParcelableExtra("user")).getId() : 0); //Если редактирование - взять из модели пользователя, если добавление - 0

        mDetailViewModel.validateUser(
                mEtFirstName.getText().toString(),
                mEtLastName.getText().toString(),
                mEtMiddleName.getText().toString(),
                mEtPhone.getText().toString(),
                mEtWorkPhone.getText().toString(),
                mTvBirthday.getText().toString(),
                mEtPosition.getText().toString(),
                role,
                id
        );

    }

    /**
     * Обновляет UI при редактировании карточки
     * @param user - данные их карточки
     */
    private void updateUI(User user) {
        mEtFirstName.setText(user.getFirstName());
        mEtLastName.setText(user.getLastName());
        mEtMiddleName.setText((!user.getPatronimic().isEmpty()) ? user.getPatronimic() : "");
        mEtPhone.setText(user.getPhone());
        if(!user.getAvatar().isEmpty())
            App.getPicasso().load(user.getAvatar()).resize(90, 90).placeholder(R.drawable.empty_photo).into(mCivDetailAvatar);


        if (user.getRole() == Constants.Role.FRIEND) {
            mClCollBlock.setVisibility(View.GONE);
            mTvBirthday.setText(user.getBirthday());

            Date date = DateHelper.ParseDate(user.getBirthday());
            calendar.setTime(date);
        } else {
            mClFriendBlock.setVisibility(View.GONE);
            mEtWorkPhone.setText(user.getWorkPhone());
            mEtPosition.setText(user.getPosition());
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_AVATAR_REQUEST && resultCode == Activity.RESULT_OK){
            Uri imageUri = data.getData();
            App.getPicasso().load(imageUri).into(mDetailViewModel.getPicassoTarget());
            mDetailViewModel.avatarURI = imageUri.toString();
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Map<String, Integer> mapPer = new HashMap<>();

        for (int i = 0; i < permissions.length; i++)
            mapPer.put(permissions[i], grantResults[i]);

        if(requestCode == AVATAR_PERMISSIONS_REQUEST
                && mapPer.get(Manifest.permission.READ_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED
                && mapPer.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED){
            selectAvatar();
        }
    }

    /**
     * Функция инициализации
     * @return интент, для открытия активности добавления для выбранной роли
     */
    public static Intent newInstance(Context context, Constants.Role role) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("openAs", OpenAs.ADD);
        intent.putExtra("role", role);
        return intent;
    }

    /**
     * Функция инициализации
     * @param user данные для редактирования
     * @return интент для активности редактирования
     */
    public static Intent newInstance(Context context, User user) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("openAs", OpenAs.EDIT);
        intent.putExtra("user", user);
        return intent;
    }

    private void showSnakbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
