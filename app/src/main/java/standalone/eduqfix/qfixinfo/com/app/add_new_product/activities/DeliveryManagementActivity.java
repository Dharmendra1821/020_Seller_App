package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import standalone.eduqfix.qfixinfo.com.app.R;

public class DeliveryManagementActivity extends AppCompatActivity implements View.OnClickListener {

    CardView add_delievry_boy,assign_delivery,delivery_boy_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_management);

        getSupportActionBar().setTitle("Delivery Management");

        add_delievry_boy  = findViewById(R.id.card_add_delivery_boy);
        assign_delivery   = findViewById(R.id.card_assign_delivery);
        delivery_boy_list = findViewById(R.id.card_deliveryboy_list);

        add_delievry_boy.setOnClickListener(this);
        assign_delivery.setOnClickListener(this);
        delivery_boy_list.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

           case R.id.card_add_delivery_boy:

               Intent intent = new Intent(DeliveryManagementActivity.this,AddDeliveryBoyActivity.class);
               intent.putExtra("fname","");
               intent.putExtra("lname","");
               intent.putExtra("entity_id","");
               intent.putExtra("email","");
               startActivity(intent);
               break;

            case R.id.card_deliveryboy_list:

                Intent intent1 = new Intent(DeliveryManagementActivity.this,DeliveryBoyListingActivity.class);
                startActivity(intent1);
                break;

            case R.id.card_assign_delivery:

                Intent intent2 = new Intent(DeliveryManagementActivity.this,DeliveryBoyOrderActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
