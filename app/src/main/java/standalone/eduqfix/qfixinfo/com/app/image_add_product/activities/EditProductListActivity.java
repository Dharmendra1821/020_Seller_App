package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
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
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.CameraFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.GalleryFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.NewCameraFragment;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.QfixlibraryFragment;

public class EditProductListActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener{

    private ActionBar toolbar;
    String productId;
    int position;
    String productSku;
    String valueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_list);

        toolbar = getSupportActionBar();

        // load the store fragment by default
        productId = getIntent().getStringExtra("product_id");
        position  = getIntent().getIntExtra("position",0);
        valueId   = getIntent().getStringExtra("value_id");
        productSku = getIntent().getStringExtra("product_sku");

        toolbar.setTitle("Take Photo");
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        NewCameraFragment cameraFragment = new NewCameraFragment();
        Bundle args = new Bundle();
        args.putString("product_id",productId);
        args.putInt("position",position);
        args.putString("value_id",valueId);
        args.putString("product_sku",productSku);
        cameraFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_container, cameraFragment);
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
                    NewCameraFragment cameraFragment = new NewCameraFragment();
                    Bundle args = new Bundle();
                    args.putString("product_id",productId);
                    args.putInt("position",position);
                    args.putString("value_id",valueId);
                    args.putString("product_sku",productSku);
                    cameraFragment.setArguments(args);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.frame_container, cameraFragment);
                    transaction.commit();
                    return true;
                case R.id.gallery:
                    toolbar.setTitle("Gallery");
                    GalleryFragment galleryFragment = new GalleryFragment();
                    Bundle args1 = new Bundle();
                    args1.putString("product_id",productId);
                    args1.putInt("position",position);
                    args1.putString("value_id",valueId);
                    args1.putString("product_sku",productSku);
                    galleryFragment.setArguments(args1);
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    transaction1.add(R.id.frame_container, galleryFragment);
                    transaction1.commit();
                    return true;
                case R.id.qfix_library:
                    toolbar.setTitle("Qfix Library");
                    fragment = new QfixlibraryFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.add_list:
                    toolbar.setTitle("Products");
                    Intent intent = new Intent(EditProductListActivity.this, AddProductListActivity.class);
                    startActivity(intent);
                    finish();
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
    public void onFragmentInteraction(Bitmap bitmap) {
        if (bitmap != null) {
            CameraFragment cameraFragment = new CameraFragment();
            Bundle args = new Bundle();
            args.putString("product_id",productId);
            args.putInt("position",position);
            args.putString("value_id",valueId);
            args.putString("product_sku",productSku);
            cameraFragment.setArguments(args);
            cameraFragment.imageSetupFragment(bitmap);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, cameraFragment);
            transaction.commit();
        }
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

}
