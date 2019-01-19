package pw.jawedyx.tensortest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pw.jawedyx.tensortest.fragments.UserFragment;
import pw.jawedyx.tensortest.utils.Constants;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return UserFragment.newInstance(Constants.Role.FRIEND);
            case 1:
                return UserFragment.newInstance(Constants.Role.COLLEAGUE);
            default:
                return UserFragment.newInstance(Constants.Role.FRIEND);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
