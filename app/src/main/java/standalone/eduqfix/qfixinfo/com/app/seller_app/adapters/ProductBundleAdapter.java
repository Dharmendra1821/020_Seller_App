package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.datachanged;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BundleProductOption;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.MediaGalleryEntry;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class ProductBundleAdapter extends RecyclerView.Adapter<ProductBundleAdapter.ViewHolder>

{

    List<BundleProductOption> bundleProductOptionList;
    List<MediaGalleryEntry> mediaGalleryEntries;
    List<ProductLink> productLinkList ;


    private Context context1;
    List<String>Dropdown = new ArrayList<>();

    datachanged datachanged1;

    Integer n =1;
    RadioButton radioButton;
    String smallimage = null;
    String Productimage=null;
    String thumbnailimage = null;
    Integer count = 0,select=0,c=0;
    ArrayList<String> name = new ArrayList<>();

    Integer quantity;
    ArrayList<Integer> QTY = new ArrayList<>();
    ArrayList<Integer> Index = new ArrayList<>();

    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> skulist = new ArrayList<>();
    ArrayList<String> pricelist = new ArrayList<>();

    String CartName;
    String CartSKU;
    String CartPrice;
    List<String> typecheck = new ArrayList<>();

    String typecheck1;

    public  int mSelectedItem = -1;

    List<RadioButton> allRadioButons = new ArrayList<>();

    Integer index;


    public ProductBundleAdapter(Context context,  List<BundleProductOption> bundleProductOptionList ,List<MediaGalleryEntry> mediaGalleryEntries,List<ProductLink> productLinkList,Integer index, datachanged datachanged)
    {


        this.datachanged1=datachanged;
        this.context1=context;
        this.mediaGalleryEntries=mediaGalleryEntries;
        this.bundleProductOptionList = bundleProductOptionList;
        this.productLinkList=productLinkList;
        this.index=index;

    }

    @NonNull
    @Override
    public ProductBundleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
       View view = LayoutInflater.from(context1).inflate(R.layout.product_bundle,viewGroup,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ProductBundleAdapter.ViewHolder viewHolder, final int i) {

        SharedPreferences sharedPreferences = context1.getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("KEY", true);



        quantity = sharedPreferences.getInt("QUANTITY", 0);

        if (!mediaGalleryEntries.isEmpty()) {

            for (int media = 0; media < mediaGalleryEntries.size(); media++) {
                thumbnailimage = mediaGalleryEntries.get(media).getFile();
                //thumbnailimage= (String)bundleProductOptionList.get(media).getProductLinks().get(i).getThumbnail();
                smallimage = (String) bundleProductOptionList.get(media).getProductLinks().get(i).getProductSmallImage();
                Productimage = (String) bundleProductOptionList.get(media).getProductLinks().get(i).getProductImage();


            }
        } else {
            editor.putString("THUMBNAILIMAGE", null);
            editor.putString("SMALLIMAGE", null);
            editor.putString("PRODUCTIMAGE", null);

            editor.apply();
        }
        if (thumbnailimage != null) {

            editor.putString("THUMBNAILIMAGE", thumbnailimage);
            editor.putString("SMALLIMAGE", smallimage);
            editor.putString("PRODUCTIMAGE", Productimage);
            editor.putString("THUMBNAILIMAGE", thumbnailimage);

            editor.apply();


        } else {
            editor.putString("THUMBNAILIMAGE", null);
            editor.putString("SMALLIMAGE", null);
            editor.putString("PRODUCTIMAGE", null);

            editor.apply();

        }


        //QUANTITY CHECK

        if (quantity <= 0) {
            viewHolder.editTextqty.setText("1");
            quantity = 1;
            count++;
        } else {
            //viewHolder.editTextqty.setText(quantity.toString());
            viewHolder.editTextqty.setText("1");
            quantity = 1;
            count++;

        }
        //END OF QUANTITY CHECK


        viewHolder.editTextqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int m, KeyEvent keyEvent) {




                if (viewHolder.editTextqty.isEnabled()) {
                    Integer check = (Integer) Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));
                    if (check == 0) {
                        count = 0;

                    } else {

                        name.add(c, bundleProductOptionList.get(0).getProductLinks().get(i).getProductName());
                        QTY.add(c, check);
                        Index.add(c, i);
                        c++;
                        quantity = check;
                        count++;

                    }
                }
                else
                {
                            viewHolder.editTextqty.setError("You Cant change the quantity After Selecting ");

                }
                return false;
            }
        });


        // TWO BUNDLE FUNCTIONALITY


        if (bundleProductOptionList.size() > 1) {

            if (bundleProductOptionList.get(index).getType().equals("select")) {


                typecheck1 = bundleProductOptionList.get(index).getType();
                viewHolder.spinner.setVisibility(View.VISIBLE);
                Dropdown.add(0, "SELECT");
                viewHolder.label.setText(bundleProductOptionList.get(index).getTitle());

                for (int k = 0; k < bundleProductOptionList.get(index).getProductLinks().size(); k++) {

                    Dropdown.add(n, productLinkList.get(k).getProductName());
                    n++;

                    Double price = Double.parseDouble(productLinkList.get(k).getProductPrice());
                    viewHolder.Price.setText(price.toString());
                    viewHolder.editTextqty.setText("1");
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, Dropdown);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // viewHolder.spinner.setPrompt("SELECT");
                viewHolder.spinner.setAdapter(arrayAdapter);


                viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position >= 1) {


                            CartName = productLinkList.get(position - 1).getProductName();
                            CartSKU = productLinkList.get(position - 1).getSku();
                            CartPrice = productLinkList.get(position - 1).getProductPrice();
                            quantity = (Integer) Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));
                            viewHolder.productvalue.setVisibility(View.VISIBLE);
                            select++;
                        } else {
                            select = 0;
                            viewHolder.productvalue.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        count = 0;
                    }
                });
            }


            //START OF CHECKBOX

            else {
                if (bundleProductOptionList.get(index).getType().equals("checkbox") || bundleProductOptionList.get(index).getType().equals("multi")) {
                    typecheck1 = bundleProductOptionList.get(index).getType();
                    viewHolder.relativeLayout.setVisibility(View.VISIBLE);
                    viewHolder.productvalue.setVisibility(View.VISIBLE);
                    viewHolder.label.setText(bundleProductOptionList.get(index).getTitle());


                    viewHolder.checkBox1.setText(productLinkList.get(i).getProductName());
                    Double price = Double.parseDouble(productLinkList.get(i).getProductPrice());
                    viewHolder.Price.setText(price.toString());

                    viewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (buttonView.isChecked()) {


                                Integer Checkfor = Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));

                                if (Checkfor == 0) {
                                    count = 0;
                                    QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));

                                } else {
                                    namelist.add(bundleProductOptionList.get(index).getProductLinks().get(i).getProductName());
                                    skulist.add(bundleProductOptionList.get(index).getProductLinks().get(i).getSku());
                                    pricelist.add(bundleProductOptionList.get(index).getProductLinks().get(i).getProductPrice());
                                    QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));
                                    Toast.makeText(context1, " You Selected " + bundleProductOptionList.get(index).getProductLinks().get(i).getProductName(), Toast.LENGTH_SHORT).show();

                                }


                            } else {
                                namelist.remove(bundleProductOptionList.get(index).getProductLinks().get(i).getProductName());
                                skulist.remove(bundleProductOptionList.get(index).getProductLinks().get(i).getSku());
                                pricelist.remove(bundleProductOptionList.get(index).getProductLinks().get(i).getProductPrice());
                                QTY.remove(i);

                                Toast.makeText(context1, " You Unchecked " + bundleProductOptionList.get(index).getProductLinks().get(i).getProductName(), Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                } else {
                    if (bundleProductOptionList.get(0).getType().equals("radio")) {
                        viewHolder.radioGroup.setVisibility(View.VISIBLE);
                        viewHolder.productvalue.setVisibility(View.VISIBLE);

                        typecheck.add(bundleProductOptionList.get(0).getType());

                        Double price = Double.parseDouble(bundleProductOptionList.get(0).getProductLinks().get(i).getProductPrice());
                        viewHolder.Price.setText(price.toString());
                        viewHolder.label.setText(bundleProductOptionList.get(0).getTitle());
                        radioButton.setText(bundleProductOptionList.get(0).getProductLinks().get(i).getProductName());
                        radioButton.setTag("RADIO-" + bundleProductOptionList.get(0).getProductLinks().get(i).getId());
                        allRadioButons.add(radioButton);

                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSelectedItem = i;
                                for (RadioButton button : allRadioButons) {
                                    if (button.getTag() != null && !button.getTag().equals(view.getTag())) {
                                        button.setChecked(false);
                                    }
                                }
                                CartName = bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductName();
                                CartSKU = bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getSku();
                                CartPrice = bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductPrice();
                                quantity = Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));

                                //image view for each Product after selecting.
                                SharedPreferences sharedPreferences = context1.getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("KEY", true);
                                thumbnailimage = (String) bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getThumbnail();
                                smallimage = (String) bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductSmallImage();
                                Productimage = (String) bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductImage();
                                editor.putString("SMALLIMAGE", smallimage);
                                editor.putString("PRODUCTIMAGE", Productimage);
                                editor.putString("THUMBNAILIMAGE", thumbnailimage);

                                editor.apply();

                            }
                        });

                    }
                }


            }


        }



        // FOR ONE BUNDLE FUNCTIONALITY


        else
        {


        if (bundleProductOptionList.get(0).getType().equals("select")) {

            typecheck.add( bundleProductOptionList.get(i).getType());
            viewHolder.spinner.setVisibility(View.VISIBLE);
            Dropdown.add(0, "SELECT");


            for (int k = 0; k < bundleProductOptionList.get(i).getProductLinks().size(); k++) {

                Dropdown.add(n, bundleProductOptionList.get(i).getProductLinks().get(k).getProductName());
                n++;

                viewHolder.editTextqty.setText("1");
                viewHolder.label.setText(bundleProductOptionList.get(i).getTitle());


            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, Dropdown);
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            // viewHolder.spinner.setPrompt("SELECT");
            viewHolder.spinner.setAdapter(arrayAdapter);


            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position >= 1) {
                        Double price = Double.parseDouble(bundleProductOptionList.get(i).getProductLinks().get(position-1).getProductPrice());
                        viewHolder.Price.setText(price.toString());

                        CartName = bundleProductOptionList.get(i).getProductLinks().get(position - 1).getProductName();
                        CartSKU = bundleProductOptionList.get(i).getProductLinks().get(position - 1).getSku();
                        CartPrice = bundleProductOptionList.get(i).getProductLinks().get(position - 1).getProductPrice();
                        quantity = (Integer) Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));
                        viewHolder.productvalue.setVisibility(View.VISIBLE);
                        select++;
                    } else {
                        select = 0;
                        viewHolder.productvalue.setVisibility(View.INVISIBLE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        //START OF RADIO GROUP

        else if (bundleProductOptionList.get(0).getType().equals("radio")) {
            viewHolder.radioGroup.setVisibility(View.VISIBLE);
            viewHolder.productvalue.setVisibility(View.VISIBLE);

            typecheck.add( bundleProductOptionList.get(0).getType());

            Double price = Double.parseDouble(bundleProductOptionList.get(0).getProductLinks().get(i).getProductPrice());
            viewHolder.Price.setText(price.toString());
            viewHolder.label.setText(bundleProductOptionList.get(0).getTitle());
            radioButton.setText(bundleProductOptionList.get(0).getProductLinks().get(i).getProductName());
            radioButton.setTag("RADIO-" + bundleProductOptionList.get(0).getProductLinks().get(i).getId());
            allRadioButons.add(radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedItem = i;
                    for (RadioButton button : allRadioButons) {
                        if (button.getTag() != null && !button.getTag().equals(view.getTag())) {
                            button.setChecked(false);
                        }
                    }
                    CartName = bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductName();
                    CartSKU = bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getSku();
                    CartPrice = bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductPrice();
                    quantity = Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));

                    //image view for each Product after selecting.
                    SharedPreferences sharedPreferences = context1.getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("KEY", true);
                    thumbnailimage = (String) bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getThumbnail();
                    smallimage = (String) bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductSmallImage();
                    Productimage = (String) bundleProductOptionList.get(0).getProductLinks().get(mSelectedItem).getProductImage();
                    editor.putString("SMALLIMAGE", smallimage);
                    editor.putString("PRODUCTIMAGE", Productimage);
                    editor.putString("THUMBNAILIMAGE", thumbnailimage);

                    editor.apply();

                }
            });


        }
        //START OF CHECKBOX

        else {
            if (bundleProductOptionList.get(0).getType().equals("checkbox") || bundleProductOptionList.get(0).getType().equals("multi")) {
                typecheck.add( bundleProductOptionList.get(0).getType());
                viewHolder.relativeLayout.setVisibility(View.VISIBLE);
                viewHolder.productvalue.setVisibility(View.VISIBLE);



                for (int k = 0; k < bundleProductOptionList.size(); k++) {


                    viewHolder.checkBox1.setText(bundleProductOptionList.get(k).getProductLinks().get(i).getProductName());
                    Double price = Double.parseDouble(bundleProductOptionList.get(k).getProductLinks().get(i).getProductPrice());
                    viewHolder.Price.setText(price.toString());
                    viewHolder.label.setText(bundleProductOptionList.get(k).getTitle());


                }


                viewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isChecked()) {

                            viewHolder.editTextqty.setEnabled(false);


                            Integer Checkfor = Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));

                            if (Checkfor == 0) {
                                count = 0;
                                QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));

                            } else {

                                namelist.add(bundleProductOptionList.get(0).getProductLinks().get(i).getProductName());
                                skulist.add(bundleProductOptionList.get(0).getProductLinks().get(i).getSku());
                                pricelist.add(bundleProductOptionList.get(0).getProductLinks().get(i).getProductPrice());
                               QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));

                                Toast.makeText(context1, " You Selected " + bundleProductOptionList.get(0).getProductLinks().get(i).getProductName(), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            viewHolder.editTextqty.setEnabled(true);
                            namelist.remove(bundleProductOptionList.get(0).getProductLinks().get(i).getProductName());
                            skulist.remove(bundleProductOptionList.get(0).getProductLinks().get(i).getSku());
                            pricelist.remove(bundleProductOptionList.get(0).getProductLinks().get(i).getProductPrice());
                           QTY.remove(i);

                            Toast.makeText(context1, " You Unchecked ", Toast.LENGTH_LONG).show();


                        }
                    }
                });

                return;
            } else {
                return;
            }
        }


    }




    }







//    public void addcartitem(String authtoken,Integer listquantity) {
//
//
//        final ProgressDialog progressDialog = new ProgressDialog(context1);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_URL);
//        SharedPreferences sharedPreferences = context1.getSharedPreferences("StandAlone", MODE_PRIVATE);
//        Integer cartId = sharedPreferences.getInt("ProductCartId", 0);
//        MPOSServices services = retrofit.create(MPOSServices.class);
//        boolean IsAllExecuted = true;
//
//        ProductCart productCart = new ProductCart();
//        CartItem cItems = new CartItem();
//
////FOR TWO BUNDLE
//        if (bundleProductOptionList.size() > 1) {
//
//
//            if (count > 0) {
//
//            if (typecheck1.equals("checkbox") || typecheck1.equals("multi")) {
//
//
//                if (namelist.size() == 0) {
//
//                    progressDialog.dismiss();
//
//                    Toast.makeText(context1, "SELECT ITEM  ", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//
//
//                    for (int a = 0; a < namelist.size(); a++) {
//
//
//                        cItems.setQty(QTY.get(a));
//                        cItems.setProductType(typecheck1);
//
//                        cItems.setQuote_id(String.valueOf(cartId));
//                        cItems.setPrice(Double.valueOf(pricelist.get(a)));
//                        cItems.setName(namelist.get(a));
//                        cItems.setSku(skulist.get(a));
//                        productCart.setCartItem(cItems);
//
//
//                        Gson gson = new Gson();
//                        String productGson = gson.toJson(productCart);
//                        Log.d("Product JSON", "Product Json == " + productGson);
//
//                        services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                            @Override
//                            public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//
//                                if (response.code() == 200 || response.isSuccessful()) {
//
//                                    progressDialog.dismiss();
//                                    Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
//
//                                    Handler handler = new Handler();
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Intent intent = ((Activity) context1).getIntent();
//                                            ((Activity) context1).finish();
//                                            context1.startActivity(intent);
//
//                                        }
//                                    }, 8000);
//
//
//                                } else if (response.code() == 400) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                } else if (response.code() == 401) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                } else if (response.code() == 500) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                } else {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                }
//
//                                Intent intent = ((Activity) context1).getIntent();
//                                ((Activity) context1).finish();
//
//                                context1.startActivity(intent);
//                            }
//
//                            @Override
//                            public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                progressDialog.dismiss();
//                                Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//
//                    }
//
//                }
//            }
//        }
//
//    }
//        //FOR ONE BUNDLE
//
//        else {
//
//            for (int two = 0; two < bundleProductOptionList.size(); two++) {
//
//                if (count > 0) {
//
//                    if (typecheck.get(two).equals("select")) {
//
//                        if (select > 0) {
//                            for (int Q = 0; Q < listquantity; Q++) {
//
//                                cItems.setSku(CartSKU);
//                                cItems.setQty(quantity);
//                                cItems.setName(CartName);
//                                cItems.setPrice(Double.valueOf(CartPrice));
//                                cItems.setQuote_id(String.valueOf(cartId));
//                                cItems.setProductType(typecheck.get(two));
//                                productCart.setCartItem(cItems);
//
//
//                                Gson gson = new Gson();
//                                String productGson = gson.toJson(productCart);
//                                Log.d("Product JSON", "Product Json == " + productGson);
//
//                                services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                                    @Override
//                                    public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//                                        if (response.code() == 200 || response.isSuccessful()) {
//
//
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
//
//
//                                            Handler handler = new Handler();
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Intent intent = ((Activity) context1).getIntent();
//                                                    ((Activity) context1).finish();
//                                                    context1.startActivity(intent);
//
//                                                }
//                                            }, 8000);
//
//
//                                        } else if (response.code() == 400) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                        } else if (response.code() == 401) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                        } else if (response.code() == 500) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                        } else {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                        }
//                                        Intent intent = ((Activity) context1).getIntent();
//                                        ((Activity) context1).finish();
//
//                                        context1.startActivity(intent);
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//
//                        }
//
//                        //ELSE OF SELECT
//
//                        else {
//                            progressDialog.dismiss();
//                            Toast.makeText(context1, "SELECT ITEM  ", Toast.LENGTH_SHORT).show();
//
//                        }
//
//
//                    }
//
//                    //START OF CHECKBOX
//
//                    else if (typecheck.get(two).equals("checkbox") || typecheck.get(two).equals("multi")) {
//                        if (namelist.size() == 0) {
//
//                            progressDialog.dismiss();
//
//
//                        /*progressDialog.dismiss();
//                        Intent intent = ((Activity) context1).getIntent();
//                        ((Activity) context1).finish();
//
//                        context1.startActivity(intent);*/
//                            Toast.makeText(context1, "SELECT ITEM  ", Toast.LENGTH_SHORT).show();
//
//                        } else {
//
//                            for (int a = 0; a < namelist.size(); a++) {
//
//                                final int lo;
//                                lo=a;
//                                cItems.setQty(QTY.get(a));
//                                cItems.setProductType(typecheck.get(two));
//                                cItems.setQuote_id(String.valueOf(cartId));
//                                cItems.setPrice(Double.valueOf(pricelist.get(a)));
//                                cItems.setName(namelist.get(a));
//                                cItems.setSku(skulist.get(a));
//                                productCart.setCartItem(cItems);
//
//
//                                Gson gson = new Gson();
//                                String productGson = gson.toJson(productCart);
//                                Log.d("Product JSON", "Product Json == " + productGson);
//
//                                services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                                    @Override
//                                    public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//
//                                        if (response.code() == 200 || response.isSuccessful()) {
//
//
//                                            Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
//
//                                            Handler handler = new Handler();
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    progressDialog.dismiss();
//
//                                                    Intent intent = ((Activity) context1).getIntent();
//                                                    ((Activity) context1).finish();
//                                                    context1.startActivity(intent);
//
//                                                }
//                                            }, 8000);
//
//
//                                        } else if (response.code() == 400) {
//                                            progressDialog.dismiss();
//                                            final AlertDialog.Builder builder = new AlertDialog.Builder(context1);
//
//                                            builder.setTitle("OUT OF STOCK");
//                                            builder.setMessage( "PRODUCT NAME : "+namelist.get(lo)  );
//
//                                            builder.setCancelable(false);
//                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    Intent intent = ((Activity) context1).getIntent();
//                                                    ((Activity) context1).finish();
//
//                                                    context1.startActivity(intent);
//                                                }
//                                            });
//                                            final AlertDialog alertDialog = builder.create();
//                                            alertDialog.show();
//                                          //  Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                        } else if (response.code() == 401) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                        } else if (response.code() == 500) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                        } else {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                        }
//
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//
//
//                            }
//
//                        }
//                    } else {
//                        if (typecheck.get(two).equals("radio")) {
//                            if (quantity > 1 || listquantity > quantity || quantity > listquantity || quantity == listquantity) {
//                                quantity = listquantity * quantity;
//                            }
//
//
//                            cItems.setSku(CartSKU);
//                            cItems.setQty(quantity);
//                            cItems.setName(CartName);
//                            cItems.setPrice(Double.valueOf(CartPrice));
//                            cItems.setQuote_id(String.valueOf(cartId));
//                            cItems.setProductType(typecheck.get(two));
//                            productCart.setCartItem(cItems);
//
//
//                            Gson gson = new Gson();
//                            String productGson = gson.toJson(productCart);
//                            Log.d("Product JSON", "Product Json == " + productGson);
//
//                            services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                                @Override
//                                public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//                                    if (response.code() == 200 || response.isSuccessful()) {
//
//
//                                        progressDialog.dismiss();
//                                        Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
//
//                                        Handler handler = new Handler();
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Intent intent = ((Activity) context1).getIntent();
//                                                ((Activity) context1).finish();
//                                                context1.startActivity(intent);
//
//                                            }
//                                        }, 8000);
//
//
//                                    } else if (response.code() == 400) {
//                                        progressDialog.dismiss();
//                                        final AlertDialog .Builder builder = new AlertDialog.Builder(context1);
//
//                                        builder.setTitle("OUT OF STOCK");
//                                        builder.setMessage( "PRODUCT NAME : " +CartName);
//
//                                        builder.setCancelable(false);
//                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                Intent intent = ((Activity) context1).getIntent();
//                                                ((Activity) context1).finish();
//
//                                                context1.startActivity(intent);
//                                            }
//                                        });
//                                        final AlertDialog alertDialog = builder.create();
//                                        alertDialog.show();
//                                       // Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                    } else if (response.code() == 401) {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                    } else if (response.code() == 500) {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                    } else {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                    }
//
//                                }
//
//
//                                @Override
//                                public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//
//                        }
//
//
//                    }
//
//                }
//
//                //ELSE OF COUNT CONDITION
//                else {
//                    progressDialog.dismiss();
//
//                    Toast.makeText(context1, "Quantity Cannot Be  0 ", Toast.LENGTH_SHORT).show();
//
//
//                }
//            }
//        //ELSE OF TWO BUNDLE
//            }
//
//        progressDialog.dismiss();
//
//
//    }
//

    @Override
    public int getItemCount() {

        int size = 0;
        int bsize = 0;
        int tsize =0;
        if (bundleProductOptionList.size()>1)
        {

            size=productLinkList.size();


        }
        else
            {
                if (bundleProductOptionList.get(0).getType().equals("select")) {
                    size = bundleProductOptionList.size();
                }
                else {
                    size = bundleProductOptionList.get(0).getProductLinks().size();
                }

            }

        return size;
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        Spinner spinner;
        LinearLayout radioGroup,productvalue;
        CheckBox checkBox1;
        RelativeLayout relativeLayout;
        EditText editTextqty;
        TextView Price,label;

        ViewHolder(final View view)
        {
            super(view);

            editTextqty = view.findViewById(R.id.EDITQTY1);
            label=view.findViewById(R.id.productlabel);
            Price = view.findViewById(R.id.productprice);
            editTextqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    String value = editTextqty.getText().toString();
                  //  datachanged1.quantity_bundle(value);
                    return false;
                }
            });



            spinner=view.findViewById(R.id.spinner1);
            radioGroup=view.findViewById(R.id.Radiogroup);
             radioButton= view. findViewById(R.id.radiobutton);
            checkBox1 =view.findViewById(R.id.COD);
            relativeLayout =view.findViewById(R.id.checkboxes);
            productvalue = view.findViewById(R.id.productvalue);

        }


        }
    }







