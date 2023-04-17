package standalone.eduqfix.qfixinfo.com.app.seller_app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.PriintFlyerDetailActivityt;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.FlyerProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SMSContactListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.FlyerProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactListData;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;

public class PrintFlyerProductFragment extends BottomSheetDialogFragment implements FlyerProductAdapter.ClickListener {


    private static PrintFlyerProductFragment.closeClickListener closeClickListener;
    Button submit;
    ImageView close;
    SharedPreferences sharedPreferences;
    TextView resetFilter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String result;
    ArrayList<FlyerProductModel> flyerProductModels;
    LinearLayoutManager linearLayoutManager;
    EditText sms_search;
    public static ArrayList<FlyerProductModel> array_sort;
    FlyerProductAdapter flyerProductAdapter;
    int textlength = 0;
    FlyerProductModel flyerProductModel;

    public static PrintFlyerProductFragment newInstance() {
        return new PrintFlyerProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.printflyer_product_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(getActivity());

        recyclerView = view.findViewById(R.id.flyer_product_recylerview);
        sms_search = view.findViewById(R.id.flyer_product_search);


        linearLayoutManager = new LinearLayoutManager(getActivity());



        PriintFlyerDetailActivityt productDetails = (PriintFlyerDetailActivityt) getActivity();
        productDetails.getMyData();


        flyerProductAdapter = new FlyerProductAdapter(getActivity(), productDetails.getMyData(),productDetails.getPosition());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(flyerProductAdapter);

        flyerProductAdapter = new FlyerProductAdapter();
        flyerProductAdapter.setOnItemClickListener(this);

        array_sort = new ArrayList<>();
        sms_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textlength = sms_search.getText().length();
                array_sort.clear();
                for (int j = 0; j < productDetails.getMyData().size(); j++) {
                    if (textlength <= productDetails.getMyData().get(j).getName().length()) {
                        if (productDetails.getMyData().get(j).getName().toLowerCase().trim().contains(
                                sms_search.getText().toString().toLowerCase().trim())) {
                            array_sort.add(productDetails.getMyData().get(j));
                            Log.d("data", String.valueOf(array_sort));
                        }
                    }
                }
                flyerProductAdapter = new FlyerProductAdapter(getActivity(), array_sort);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(flyerProductAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });


        return view;

    }


    public void setOnCloseClickListener(PrintFlyerProductFragment.closeClickListener clickListener) {
        PrintFlyerProductFragment.closeClickListener = clickListener;
    }

    @Override
    public void onItemClick(View v,int position) {

        flyerProductModel = flyerProductAdapter.getWordAtPosition(position);
        Log.d("flyer",flyerProductModel.getImage()+ " "+flyerProductModel.getName());


    }

    public interface closeClickListener {
        void onCloseClick(View v);
    }
}
