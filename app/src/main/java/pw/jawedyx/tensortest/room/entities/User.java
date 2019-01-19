package pw.jawedyx.tensortest.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import pw.jawedyx.tensortest.utils.Constants;

/**
 * Таблица User для БД
 */
@Entity
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String avatar;
    private String firstName;
    private String lastName;
    private String patronimic;
    private String birthday;
    private String phone;
    private String workPhone;
    private String position;
    @TypeConverters({RoleConverter.class})
    private Constants.Role role;

    public User(){

    }

    public User(String avatar, String firstName, String lastName, String patronimic, String birthday, String phone, String workPhone, String position, Constants.Role role) {
        this.id = 0;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronimic = patronimic;
        this.birthday = birthday;
        this.phone = phone;
        this.workPhone = workPhone;
        this.position = position;
        this.role = role;
    }

    protected User(Parcel in) {
        id = in.readLong();
        avatar = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        patronimic = in.readString();
        birthday = in.readString();
        phone = in.readString();
        workPhone = in.readString();
        position = in.readString();
        role = (Constants.Role) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(avatar);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(patronimic);
        dest.writeString(birthday);
        dest.writeString(phone);
        dest.writeString(workPhone);
        dest.writeString(position);
        dest.writeSerializable(role);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //getters
    public String getFullName(){
        return lastName + " " + firstName + " " + patronimic;
    }

    public long getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronimic() {
        return patronimic;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public String getPosition() {
        return position;
    }

    public Constants.Role getRole() {
        return role;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronimic(String patronimic) {
        this.patronimic = patronimic;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setRole(Constants.Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return id + ": " + getFullName();
    }
}
