package pw.jawedyx.tensortest.fragments;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pw.jawedyx.tensortest.MainViewModel;
import pw.jawedyx.tensortest.R;
import pw.jawedyx.tensortest.detail.DetailActivity;
import pw.jawedyx.tensortest.room.entities.User;
import pw.jawedyx.tensortest.utils.Constants;
import pw.jawedyx.tensortest.utils.Filter;


/**
 * Фрагмент, использует {@code Constants.Role} для создания нужной выборки
 * с данными.
 */
public class UserFragment extends Fragment implements UserAdapter.UserAdapterListener {

    @BindView(R.id.rv_friends)
    RecyclerView mRvFriends;
    Unbinder unbinder;
    private MainViewModel mMainViewModel;
    private UserAdapter userAdapter;
    private Constants.Role type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);
        type = (Constants.Role)getArguments().getSerializable("type");

        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        unbinder = ButterKnife.bind(this, view);

        userAdapter = new UserAdapter(type);
        mRvFriends.setAdapter(userAdapter);

        mMainViewModel.getData().observe(this, users -> {
            userAdapter.setData(Filter.filter(users, user -> user.getRole().compareTo(type) == 0));
        });
        return view;
    }

    /**
     * Реализация интерфейса нажания на карточку
     * Открывает {@code DetailActivity} для редактирования карточки пользователя
     * @param user объект пользователя из карточки
     */
    @Override
    public void onItemClick(User user) {
        startActivityForResult(DetailActivity.newInstance(getContext(), user), DetailActivity.DETAIL_REQUEST);
    }

    @Override
    public void onResume() {
        super.onResume();
        userAdapter.registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        userAdapter.unregisterListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Формирует фрагмент
     * @param type роль
     * @return UserFragment с необходимой ролью
     */
    public static UserFragment newInstance(Constants.Role type){
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DetailActivity.DETAIL_REQUEST && resultCode == Activity.RESULT_OK){
            mMainViewModel.reload();
        }
    }
}


