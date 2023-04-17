package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import standalone.eduqfix.qfixinfo.com.app.R;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout editMicroMarket,addSeller,sellerList,sellerReturnList,sellerComplainList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        getSupportActionBar().setTitle("Community MarketPlace");

        editMicroMarket = findViewById(R.id.edit_microMarket_layout);
        addSeller = findViewById(R.id.addSeller_microMarket_layout);
        sellerList = findViewById(R.id.sellerList_microMarket_layout);
        sellerReturnList = findViewById(R.id.sellerReturnList_microMarket_layout);
        sellerComplainList = findViewById(R.id.sellerComplaintList_microMarket_layout);

        editMicroMarket.setOnClickListener(this);
        addSeller.setOnClickListener(this);
        sellerList.setOnClickListener(this);
        sellerReturnList.setOnClickListener(this);
        sellerComplainList.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_microMarket_layout:
                Intent editIntent = new Intent(CommunityActivity.this,EditMicroMarketActivity.class);
                startActivity(editIntent);
                break;
            case R.id.addSeller_microMarket_layout:
                Intent addIntent = new Intent(CommunityActivity.this,AddSellerActivity.class);
                startActivity(addIntent);
                break;
            case R.id.sellerList_microMarket_layout:
                Intent sellerListIntent = new Intent(CommunityActivity.this,CommunitySellerActivity.class);
                startActivity(sellerListIntent);
                break;
        }
    }
}
