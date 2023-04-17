package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SMSContactListAdapter;

public class MarketingDashboard extends AppCompatActivity implements View.OnClickListener {

    ImageView emailImg,smsImg,printImg,adImg;
    CardView card_email,card_sms,card_print,card_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_dashboard);

        emailImg = findViewById(R.id.card_email_marketing_img);
      //  smsImg = findViewById(R.id.card_sms_marketing_image);
        printImg = findViewById(R.id.card_print_marketing_image);
        adImg = findViewById(R.id.card_ad_marketing_image);

        card_email = findViewById(R.id.card_email_marketing);
      //  card_sms = findViewById(R.id.card_sms_marketing);
        card_print = findViewById(R.id.card_print_marketing);
        card_ad = findViewById(R.id.card_ad_marketing);

        SMSContactListAdapter.getSmsIds().clear();

        Glide.with(this).load("https://shopping.eduqfix.com/pub/media/campaigns/Email_Campaign.png").error(R.drawable.download).into(emailImg);
     //   Glide.with(this).load("https://standalonetest.eduqfix.com/pub/media/campaigns/SMS_Campaign.png").error(R.drawable.download).into(smsImg);
        Glide.with(this).load("https://shopping.eduqfix.com/pub/media/campaigns/Print_Flyer.png").error(R.drawable.download).into(printImg);
        Glide.with(this).load("https://shopping.eduqfix.com/pub/media/wysiwyg/add_campaign/Ads_manager.png").error(R.drawable.download).into(adImg);

        card_email.setOnClickListener(this);
       // card_sms.setOnClickListener(this);
        card_print.setOnClickListener(this);
        card_ad.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.card_ad_marketing:
                Intent intent = new Intent(MarketingDashboard.this,AdCampaignActivity.class);
                startActivity(intent);
                break;
//            case R.id.card_sms_marketing:
//                Intent sms = new Intent(MarketingDashboard.this,SMSMarketingActivity.class);
//                startActivity(sms);
//                break;
            case R.id.card_print_marketing:
                Intent print = new Intent(MarketingDashboard.this,PrintFlyerActivity.class);
                print.putExtra("from_page","print");
                startActivity(print);
                break;
            case R.id.card_email_marketing:
                Intent email = new Intent(MarketingDashboard.this,EmailTemplateActivity.class);
                startActivity(email);
                break;
        }
    }
}
