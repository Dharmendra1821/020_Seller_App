package standalone.eduqfix.qfixinfo.com.app.seller_app.container;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;

public class ProductSizeContainer extends LinearLayout {

    Context mContext;
    TextView header,movietitle;
    LinearLayout childContainer,childContainer1;
    String showTime;
    ProgressDialog progressDialog;
    int cateID;
    String priceval;
    int position;
    List<ProductSizeModel> productSizeModels;
    SharedPreferences sharedPreferences;


    public ProductSizeContainer(Context context){
        super(context);
        mContext = context;
    }

    public void setupView(String title,final List<ProductSizeModel> row){

         LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
         final View view = inflater.inflate(R.layout.product_size_container, this);
         childContainer = view.findViewById(R.id.size_container);

         progressDialog = new ProgressDialog(mContext);
         progressDialog.setCancelable(false);
         childContainer.removeAllViews();

         sharedPreferences = mContext.getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

         for(int i=0;i<row.size();i++){

            final Button button = new Button(mContext);
            button.setBackgroundResource(R.drawable.button_border);
            button.setTextColor(Color.parseColor("#333333"));
            button.setTextSize(12);
            button.setText(row.get(i).getLabel());
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150,120);
            layoutParams.setMargins(6, 0, 6, 0);

            productSizeModels = row;
            Log.d("product....",productSizeModels.get(i).getPrice());

            if(i==0)
            {
                button.setBackgroundResource(R.drawable.button_border);
                priceval = row.get(0).getPrice();
                Intent intent = new Intent("config-message");
                intent.putExtra("price",priceval);
                intent.putExtra("attribute",String.valueOf(row.get(0).getProductid()));
                intent.putExtra("index",row.get(0).getIndex());
                intent.putExtra("label",row.get(0).getLabel());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
              }

            final String pricevalue = row.get(i).getPrice();
            final String attributId = String.valueOf(row.get(i).getProductid());
            final String indexValue = row.get(i).getIndex();
            final String label      = row.get(i).getLabel();

            Log.d("attt....",row.get(i).getSku());

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent("config-message");
                        intent.putExtra("price", pricevalue);
                        intent.putExtra("attribute", attributId);
                        intent.putExtra("index", indexValue);
                        intent.putExtra("label",label);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }catch (Exception e){
                        Log.d("exception...",e.toString());
                    }
                }
            });
            childContainer.addView(button,layoutParams);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setChildContainerListener(OnClickListener onClickListener){
        childContainer.setOnClickListener(onClickListener);
    }

}
