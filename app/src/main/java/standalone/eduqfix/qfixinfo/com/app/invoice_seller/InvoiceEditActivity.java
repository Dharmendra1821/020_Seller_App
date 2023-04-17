package standalone.eduqfix.qfixinfo.com.app.invoice_seller;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.adapter.SellerProductAdaptor;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.AppDatabase;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.AppExecutors;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceProductModel;

public class InvoiceEditActivity extends AppCompatActivity {

    RecyclerView recylerview;
    SellerProductAdaptor sellerProductAdaptor;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_edit);
       // recylerview = findViewById(R.id.create_recyclerview);


       // recylerview.setLayoutManager(new LinearLayoutManager(this));

//        // Initialize the adapter and attach it to the RecyclerView
//        sellerProductAdaptor = new SellerProductAdaptor(this);
//        recylerview.setAdapter(sellerProductAdaptor);
//
//        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  retrieveTasks();
    }

//    private void retrieveTasks() {
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                final List<InvoiceProductModel> persons = mDb.personDao().loadAllPersons();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        sellerProductAdaptor.setTasks(persons);
//                    }
//                });
//            }
//        });
//
//
//    }
}
