package standalone.eduqfix.qfixinfo.com.app.customer.activity;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.adaptor.CustomerSearchAdaptor;
import standalone.eduqfix.qfixinfo.com.app.customer.fragment.CustomerAddressDetailsFragment;
import standalone.eduqfix.qfixinfo.com.app.customer.fragment.SearchCustomerDetailsFragment;
import standalone.eduqfix.qfixinfo.com.app.customer.interfaces.ITabs;

public class CustomerTabActivity extends AppCompatActivity implements ITabs {


    TabItem ProfileTabItem,CustomerAddressTabItem;
    LinearLayout ProfileLinearLayout,CustomerAddressLinearLayout;
    TabLayout CustomerTabLayout;
    Fragment profileFrag,custAddressFrag;//paymentFrag;
    CustomerSearchAdaptor customerSearchAdaptor;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;
    String page ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_tab);
        try
        {
            CustomerTabLayout = findViewById(R.id.tabLayout);
            customerSearchAdaptor = new CustomerSearchAdaptor(CustomerTabActivity.this);
            viewPager = findViewById(R.id.viewPager);
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

            final CustomerAddressDetailsFragment customerAddressDetailsFragment =new CustomerAddressDetailsFragment();
            List<Fragment> fragments = new ArrayList<>();
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());
            adapter.addFragment(new SearchCustomerDetailsFragment(),"Customers Detail");
            adapter.addFragment(customerAddressDetailsFragment,"Customers Address");
            CustomerTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getText().equals("Customers Address"))
                    {
                        customerAddressDetailsFragment.FillCustomerDetails();
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            viewPager.setAdapter(adapter);
            CustomerTabLayout.setupWithViewPager(viewPager);
            setupTabIcons(adapter);

            /*profileFrag = new SearchCustomerDetailsFragment();
            SearchCustomerDetailsFragment.ProfileLinearLayout=ProfileLinearLayout;
            SearchCustomerDetailsFragment.CustomerTabLayout=CustomerTabLayout;
            custAddressFrag = new CustomerAddressDetailsFragment();
            CustomerAddressDetailsFragment.CustomerAddressLinearLayout=CustomerAddressLinearLayout;
            FragmentManager fragmentManager =getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();*/
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setupTabIcons(ViewPagerAdapter pagerAdapter) {
        for (int i=0;i<CustomerTabLayout.getTabCount();i++) {
            TabLayout.Tab tab = CustomerTabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public void onSearchItemClick() {
        viewPager.setCurrentItem(1);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private int[] tabIcons = {
                R.drawable.profile,
                R.drawable.navigation
        };

        ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.tab_custom_layout, null);
            TextView textView = v.findViewById(R.id.textView);
            textView.setText(mFragmentTitleList.get(position));
            ImageView imageView = v.findViewById(R.id.imageView);
            imageView.setImageResource(tabIcons[position]);
            return v;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        MenuItem item = menu.findItem(R.id.menu_item_cart);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.menu_item_logout:
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(CustomerTabActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }*/

}
