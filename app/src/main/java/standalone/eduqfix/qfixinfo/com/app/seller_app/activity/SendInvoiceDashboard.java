package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.CreateInvoiceActivity;

public class SendInvoiceDashboard extends AppCompatActivity implements View.OnClickListener {

    CardView add_invoice,invoice_list,bulk_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invoice_dashboard);

        add_invoice = findViewById(R.id.card_add_invoice);
        invoice_list = findViewById(R.id.card_invoice_list);
        bulk_upload = findViewById(R.id.card_bulk_invoice);

        add_invoice.setOnClickListener(this);
        invoice_list.setOnClickListener(this);
        bulk_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.card_add_invoice:
                Intent intent1 = new Intent(SendInvoiceDashboard.this, AddInvoiceActivity.class);
                startActivity(intent1);
                break;
            case R.id.card_invoice_list:
                Intent intent = new Intent(SendInvoiceDashboard.this, SellerInvoiceListActivity.class);
                startActivity(intent);
                break;
            case R.id.card_bulk_invoice:
                Intent intent2 = new Intent(SendInvoiceDashboard.this, BulkUploadActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
