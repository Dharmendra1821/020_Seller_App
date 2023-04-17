package standalone.eduqfix.qfixinfo.com.app.qpos_login.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoices.activity.SearchInvoiceActivity;

import static android.content.Context.MODE_PRIVATE;

public class SelectMPOSDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    Button mpos_yes,mpos_no;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mposdevice);

        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        mpos_yes = findViewById(R.id.mpos_device_yes);
        mpos_no  = findViewById(R.id.mpos_device_no);

        mpos_yes.setOnClickListener(this);
        mpos_no.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.mpos_device_yes:
                sharedPreferences.edit().putString("mpos_id","1").apply();
                Intent intent = new Intent(SelectMPOSDeviceActivity.this,QPOSLoginActivity.class);
                startActivity(intent);
                break;

            case R.id.mpos_device_no:
                sharedPreferences.edit().putString("mpos_id","0").apply();
                Intent intent2 = new Intent(SelectMPOSDeviceActivity.this, SearchInvoiceActivity.class);
                startActivity(intent2);

                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
