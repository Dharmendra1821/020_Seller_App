package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment.AddCameraFragment;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment.AddGalleryFragment;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment.QfixImageLibraryFragment;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment.SurfaceViewCameraFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.CameraFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.GalleryFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.NewCameraFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.QfixlibraryFragment;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.EditProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.RequestTrialListActivity;

public class BottomNavigationActivity extends AppCompatActivity {

    private ActionBar toolbar;
    String productId;
    int position;
    int flag;
    String productSku;
    String valueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        toolbar = getSupportActionBar();

        // load the store fragment by default


        toolbar.setTitle("Take Photo");
        BottomNavigationView navigation =  findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        position = getIntent().getIntExtra("position",0);
        flag = getIntent().getIntExtra("flag",0);

        QfixImageLibraryFragment cameraFragment = new QfixImageLibraryFragment();
        Bundle args = new Bundle();
        args.putInt("position",position);
        cameraFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.bottom_frame_container, cameraFragment);
        transaction.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.take_photo:
                    toolbar.setTitle("Take Photo");
                    AddCameraFragment cameraFragment = new AddCameraFragment();
                    Bundle args = new Bundle();
                    args.putInt("position",position);
                    cameraFragment.setArguments(args);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.bottom_frame_container, cameraFragment);
                    transaction.commit();
                    return true;
                case R.id.gallery:
                    toolbar.setTitle("Gallery");
                    AddGalleryFragment galleryFragment = new AddGalleryFragment();
                    Bundle args1 = new Bundle();
                    args1.putInt("position",position);
                    galleryFragment.setArguments(args1);
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    transaction1.add(R.id.bottom_frame_container, galleryFragment);
                    transaction1.commit();
                    return true;
                case R.id.qfix_library:
                    toolbar.setTitle("Qfix Library");
                    QfixImageLibraryFragment qfixImageLibraryFragment = new QfixImageLibraryFragment();
                    Bundle imageargs = new Bundle();
                    imageargs.putInt("position",position);
                    qfixImageLibraryFragment.setArguments(imageargs);
                    FragmentManager imageManager = getSupportFragmentManager();
                    FragmentTransaction imageTransaction = imageManager.beginTransaction();
                    imageTransaction.add(R.id.bottom_frame_container, qfixImageLibraryFragment);
                    imageTransaction.commit();
                    return true;
                case R.id.add_list:
                    toolbar.setTitle("Products");
                    if(flag == 1){
                        Intent intent = new Intent(BottomNavigationActivity.this, RequestTrialListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(BottomNavigationActivity.this,MyProductListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
