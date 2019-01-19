package pw.jawedyx.tensortest.fragments;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pw.jawedyx.tensortest.App;
import pw.jawedyx.tensortest.R;
import pw.jawedyx.tensortest.room.entities.User;
import pw.jawedyx.tensortest.utils.Constants;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface UserAdapterListener{
        void onItemClick(User user);
    }

    private UserAdapterListener mListener;

    private ArrayList<User> mUsers;
    private Constants.Role mType;

    public UserAdapter(Constants.Role type) {
        mUsers = new ArrayList<>();
        mType = type;
    }

    /**
     * @return {@code ViewHolder} с полями, подходящими только для нужной роли
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mType == Constants.Role.FRIEND){
            CardView cv = (MaterialCardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_card, viewGroup, false);
            return new FriendViewHolder(cv);
        }else{
            CardView cv = (MaterialCardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.colleague_card, viewGroup, false);
            return new ColleagueViewHolder(cv);
        }


    }

    /**
     * Устанавливает значения полям в карточке для нужной роли
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        User user = mUsers.get(holder.getAdapterPosition());

        if(mType == Constants.Role.FRIEND){
            FriendViewHolder friendViewHolder = (FriendViewHolder)holder;


            if(!user.getAvatar().isEmpty()) App.getPicasso().load(user.getAvatar()).resize(90, 90).placeholder(R.drawable.empty_photo).into(friendViewHolder.mCivAvatar);
            friendViewHolder.mTvFullName.setText(user.getFullName());
            friendViewHolder.mTvBirthday.setText(String.format("День рождения: %s", user.getBirthday()));
            friendViewHolder.mTvPhone.setText(String.format("Телефон: %s", user.getPhone()));

            friendViewHolder.item.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(user);
                }
            });

        }else{
            ColleagueViewHolder colleagueViewHolder = (ColleagueViewHolder)holder;

            if(!user.getAvatar().isEmpty()) App.getPicasso().load(user.getAvatar()).resize(90, 90).placeholder(R.drawable.empty_photo).into(colleagueViewHolder.mCivAvatar);
            colleagueViewHolder.mTvFullName.setText(user.getFullName());
            colleagueViewHolder.mTvWorkPhone.setText(String.format("Рабочий телефон: %s", user.getWorkPhone()));
            colleagueViewHolder.mTvPhone.setText(String.format("Мобильный телфон: %s", user.getPhone()));
            colleagueViewHolder.mTvPosition.setText(user.getPosition());

            colleagueViewHolder.item.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void registerListener(UserAdapterListener listener) {
        mListener = listener;
    }

    public void unregisterListener() {
        mListener = null;
    }

    /**
     * @param newData обновленные данные для адаптера
     */
    public void setData(ArrayList<User> newData) {
        this.mUsers = newData;
        notifyDataSetChanged();
    }

    /**
     * Холдер друзей
     */
    class FriendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_item)
        MaterialCardView item;
        @BindView(R.id.civ_avatar)
        CircleImageView mCivAvatar;
        @BindView(R.id.tv_full_name)
        TextView mTvFullName;
        @BindView(R.id.tv_birthday)
        TextView mTvBirthday;
        @BindView(R.id.tv_phone)
        TextView mTvPhone;

        public FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Холдер коллег
     */
    class ColleagueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_item)
        MaterialCardView item;
        @BindView(R.id.civ_avatar)
        CircleImageView mCivAvatar;
        @BindView(R.id.tv_full_name)
        TextView mTvFullName;
        @BindView(R.id.tv_phone)
        TextView mTvPhone;
        @BindView(R.id.tv_work_phone)
        TextView mTvWorkPhone;
        @BindView(R.id.tv_position)
        TextView mTvPosition;

        public ColleagueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
