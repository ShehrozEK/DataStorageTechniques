package shehroz.com.datastoragetechniques;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList;
    ViewPagerAdapter( FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
