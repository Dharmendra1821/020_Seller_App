package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import standalone.eduqfix.qfixinfo.com.app.R;

public class AdCampaignActivity extends AppCompatActivity implements View.OnClickListener {

    CardView fbCaard,instaCard,googleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_campaign);

        fbCaard = findViewById(R.id.card_marketing_fb);
        instaCard = findViewById(R.id.card_marketing_instagram);
        googleCard = findViewById(R.id.card_google_marketing);

        fbCaard.setOnClickListener(this);
        instaCard.setOnClickListener(this);
        googleCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_marketing_fb:
                String url = "https://business.facebook.com/login/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
            case R.id.card_marketing_instagram:
                String intsaurl = "https://www.instagram.com/accounts/login/?next=https://business.instagram.com/";
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(intsaurl));
                startActivity(browserIntent2);
                break;
            case R.id.card_google_marketing:
                String googleurl = "https://accounts.google.com/signin/v2/identifier?service=gam&passive=1209600&continue=https%3A%2F%2Fadmanager.google.com%2F%3Futm_source%3Dadmanager.google.com%26utm_medium%3Det%26utm_campaign%3Dadmanager.google.com%2Fhome%2F%23&followup=https%3A%2F%2Fadmanager.google.com%2F%3Futm_source%3Dadmanager.google.com%26utm_medium%3Det%26utm_campaign%3Dadmanager.google.com%2Fhome%2F&faa=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
                Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(googleurl));
                startActivity(browserIntent3);
                break;
        }

    }
}
