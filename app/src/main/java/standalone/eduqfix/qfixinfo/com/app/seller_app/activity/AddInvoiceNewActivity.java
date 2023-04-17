package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.InvoiceProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerInvoiceRequest;

public class AddInvoiceNewActivity extends AppCompatActivity implements View.OnClickListener {

    Button addBtn,submitBtn;
    EditText editText;
    LinearLayout container;
    int count;
    EditText product,price,qty,cgst,igst;
    SellerInvoiceRequest sellerInvoiceRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice_new);

        addBtn = findViewById(R.id.add_button_new);
        submitBtn = findViewById(R.id.submit_button_new);
        submitBtn.setOnClickListener(this);
        count = 0;
        container = (LinearLayout)findViewById(R.id.container);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row_container, null);
                 count++;
                 EditText id = addView.findViewById(R.id.add_invoice_id_new);
                 product = addView.findViewById(R.id.add_invoice_product_new);
                 price = addView.findViewById(R.id.add_invoice_price_new);
                 qty = addView.findViewById(R.id.add_invoice_qty_new);
                 cgst = addView.findViewById(R.id.add_invoice_cgst_new);
                 igst = addView.findViewById(R.id.add_invoice_igst_new);
                 id.setText(String.valueOf(count));



                Button buttonRemove = (Button)addView.findViewById(R.id.remove_button_new);
                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                });

                container.addView(addView);
            }

        });

    }

    @Override
    public void onClick(View v) {


    }
}
