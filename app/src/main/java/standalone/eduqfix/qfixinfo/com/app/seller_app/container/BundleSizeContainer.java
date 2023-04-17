package standalone.eduqfix.qfixinfo.com.app.seller_app.container;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.GroupProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BundleContainerModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLinkModel;

public class BundleSizeContainer extends LinearLayout {
    Context mContext;
    TextView header,movietitle;
    LinearLayout childContainer,editTextContainer,textContainer;
    String showTime;
    ProgressDialog progressDialog;
    int cateID;
    String priceval;
    EditText editText;
    int position;
    List<BundleContainerModel> bundleContainerModels;
    SharedPreferences sharedPreferences;
    ArrayAdapter<String> bundleAdapter;
    ArrayList<String> bundle_product;
    Spinner spinner,qtyspinner;
    LayoutParams layoutParams,layoutParams1,layoutParams2;
    TextView quantity;
    public static ArrayList<String> bundleQuantityValue = new ArrayList<>();
    public static HashMap<Integer,String> HashMap=new HashMap<Integer,String>();
    public static HashMap<Integer,String> Quantity=new HashMap<Integer,String>();

    public static ArrayList<String> getBundleQuantityValue() {
        return bundleQuantityValue;
    }

    public static void setBundleQuantityValue(ArrayList<String> bundleQuantityValue) {
        BundleSizeContainer.bundleQuantityValue = bundleQuantityValue;
    }

    public BundleSizeContainer(Context context){
        super(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setupView(String title, final BundleContainerModel bundleContainerModel){

         LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
         final View view = inflater.inflate(R.layout.bundle_size_container, this);
         childContainer = view.findViewById(R.id.bundle_container);
         editTextContainer = view.findViewById(R.id.bundle_container_edit);
         textContainer    = view.findViewById(R.id.bundle_container_textview);

         List<String> list = new ArrayList<String>();
         list.add("1");
         list.add("2");
         list.add("3");
         list.add("4");
         list.add("5");
         list.add("6");
         list.add("7");
         list.add("8");
         list.add("9");
         list.add("10");

          GroupProductAdapter.getGroupQuantityValue().clear();
          HashMap.clear();

         progressDialog = new ProgressDialog(mContext);
         progressDialog.setCancelable(false);
         childContainer.removeAllViews();
         final List<ProductLinkModel> productLinkModelArrayList = bundleContainerModel.getProductLinkModelList();
         bundle_product = new ArrayList<>();

        for(int i=0;i<productLinkModelArrayList.size();i++){

              spinner = new Spinner(mContext);
              spinner.setTag(i);
              layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
              layoutParams.setMargins(6, 0, 6, 0);
              bundle_product.add(productLinkModelArrayList.get(i).getProductName());

              qtyspinner = new Spinner(mContext,Spinner.MODE_DIALOG);
              qtyspinner.setTag(i);
              layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
              layoutParams1.setMargins(6, 0, 6, 0);

              quantity = new TextView(mContext);
              quantity.setText("QTY");
              quantity.setTextColor(Color.parseColor("#333333"));
              quantity.setTextSize(14);
              layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
              layoutParams2.setMargins(6, 0, 6, 0);

        }

        bundleAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_layout, bundle_product);
        spinner.setAdapter(bundleAdapter);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_layout, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_text_layout);
        qtyspinner.setAdapter(dataAdapter);

        HashMap.clear();
        Quantity.clear();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Value......",productLinkModelArrayList.get(i).getSku1()+productLinkModelArrayList.get(i).getProductName()+
                        productLinkModelArrayList.get(i).getProductPrice()+"bundle");

               final int position = i;

                try {
                    Log.d("adapter....", String.valueOf(spinner.getTag()));

                    HashMap.put(productLinkModelArrayList.get(position).getOption_Id(),productLinkModelArrayList.get(position).getSku1()+":"+
                            qtyspinner.getSelectedItem()+":"+
                            "bundle"+":"+productLinkModelArrayList.get(position).getProductName()+":"+productLinkModelArrayList.get(position).getProductPrice());

                    qtyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("i value..",String.valueOf(qtyspinner.getTag()));

                            HashMap.put(productLinkModelArrayList.get(position).getOption_Id(),productLinkModelArrayList.get(position).getSku1()+":"+
                                    qtyspinner.getSelectedItem()+":"+
                                    "bundle"+":"+productLinkModelArrayList.get(position).getProductName()+":"+productLinkModelArrayList.get(position).getProductPrice());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }catch (Exception e){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        childContainer.addView(spinner,layoutParams);
        editTextContainer.addView(qtyspinner,layoutParams1);
        textContainer.addView(quantity,layoutParams2);


    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setChildContainerListener(OnClickListener onClickListener){
        childContainer.setOnClickListener(onClickListener);
    }

}
