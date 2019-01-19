package pw.jawedyx.tensortest;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import pw.jawedyx.tensortest.detail.DetailActivity;
import pw.jawedyx.tensortest.utils.Constants;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.vp_fragments)
    ViewPager mViewPager;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbarMain;
    private ActionBar mActionBar;

    private MainViewModel mMainViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_friends:
                    mViewPager.setCurrentItem(0);
                    updateUI(Constants.Role.FRIEND);

                    return true;
                case R.id.navigation_colleagues:
                    mViewPager.setCurrentItem(1);
                    updateUI(Constants.Role.COLLEAGUE);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbarMain);
        mActionBar = getSupportActionBar();
        updateUI(Constants.Role.FRIEND);

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        mNavigation.setSelectedItemId(R.id.navigation_friends);
                        updateUI(Constants.Role.FRIEND);

                        break;
                    case 1:
                        mNavigation.setSelectedItemId(R.id.navigation_colleagues);
                        updateUI(Constants.Role.COLLEAGUE);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * Обновляет UI для выбраной вкладки
     * @param role - Константа Роли
     */
    private void updateUI(Constants.Role role) {
        if(role == Constants.Role.FRIEND){
            if(mActionBar != null){
                mActionBar.setTitle(getResources().getString(R.string.title_friends));
            }
        }else{
            if(mActionBar != null){
                mActionBar.setTitle(getResources().getString(R.string.title_сolleagues));
            }
        }

        mFabAdd.setOnClickListener(v -> {
            startActivityForResult(DetailActivity.newInstance(this, role), DetailActivity.DETAIL_REQUEST);
        });
    }

    /**
     * Принимает результат из Детализации и обновляет данные
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DetailActivity.DETAIL_REQUEST && resultCode == RESULT_OK){
            mMainViewModel.reload();
        }
    }
}
