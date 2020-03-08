package shehroz.com.datastoragetechniques;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import shehroz.com.datastoragetechniques.Transformations.FadeOutTransformation;
import shehroz.com.datastoragetechniques.Transformations.HingeTransformation;
import shehroz.com.datastoragetechniques.Transformations.HorizontalFlipTransformation;
import shehroz.com.datastoragetechniques.Transformations.ZoomOutTransformation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

public class ViewPagerActivity extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    RadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4, radioBtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        viewPager = findViewById(R.id.viewPager);
        radioBtn1 = findViewById(R.id.radioBtn1);
        radioBtn2 = findViewById(R.id.radioBtn2);
        radioBtn3 = findViewById(R.id.radioBtn3);
        radioBtn4 = findViewById(R.id.radioBtn4);
        radioBtn5 = findViewById(R.id.radioBtn5);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        populateAdapterFragmentList();
        viewPager.addOnPageChangeListener(new ViewPagerListener());
        viewPager.setCurrentItem(1);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewPager.setPageTransformer(true, new HorizontalFlipTransformation());
        } else {
            viewPager.setPageTransformer(true, new HingeTransformation());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.setAdapter(null);
            viewPager = null;
        }
    }

    private void populateAdapterFragmentList() {
        viewPagerAdapter.addFragment(new SharedPreferenceFragment());
        viewPagerAdapter.addFragment(new CacheFragment());
        viewPagerAdapter.addFragment(new InternalStorageFragment());
        viewPagerAdapter.addFragment(new ExternalStorageFragment());
        viewPagerAdapter.addFragment(new SqliteFragment());
    }

    private class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    radioBtn1.setChecked(true);
                    break;
                case 1:
                    radioBtn2.setChecked(true);
                    break;
                case 2:
                    radioBtn3.setChecked(true);
                    break;
                case 3:
                    radioBtn4.setChecked(true);
                    break;
                case 4:
                    radioBtn5.setChecked(true);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
