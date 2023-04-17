package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.ICartUpdate;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewProductResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCartNewRequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.MyCartProduct> implements View.OnClickListener {

    List<ProductCartResponse> productList;
    Context context;
    TextView BillAmountTextView, TotalTextView,shippingChargesTextView;
    double Shippingcharges;
    Activity activity;
    SharedPreferences sharedPreferences;
    ICartUpdate cartUpdate;
    int flag;
    double taxValue;
    ArrayAdapter<String> dataAdapter;
    MyCartProduct viewHolder;
    ProductCartResponse product;
    int totalQty = 0;
    int open = 1;
    public CartProductAdapter(Context context){
        this.cartUpdate = (ICartUpdate) context;
    }

    public CartProductAdapter(Context context, List<ProductCartResponse> productList, TextView billAmountTextView, TextView totalTextView,TextView shippingChargesTextView, double shippingcharges, Activity activity, int flag, double taxValue){
        this.context = context;
        this.productList = productList;
        BillAmountTextView=billAmountTextView;
        TotalTextView=totalTextView;
        Shippingcharges=shippingcharges;
        this.shippingChargesTextView = shippingChargesTextView;
        this.activity = activity;
        this.cartUpdate = ((ICartUpdate) activity);
        this.flag = flag;
        this.taxValue = taxValue;
        Log.d("taxvalue", String.valueOf(taxValue));
    }

    @NonNull
    @Override
    public MyCartProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_activity,parent,false);
        return new MyCartProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartProduct holder, int position) {

        final ProductCartResponse product = productList.get(position);

        String productPrice = String.valueOf(product.getRow_total()) ;
        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String imageUrl =  product.getProduct_image();
        if(product.getProduct_image() != null){
            Glide.with(context).load(imageUrl).into(holder.productImageView);
        }else {
            holder.productImageView.setImageResource(R.drawable.no_product);
        }
        final double price = productPrice != null ?  Math.round(Double.parseDouble(productPrice) * 100)/100.00:0.0;
        final double tax = product.getTax_amount();
        final double productPriceWithTax = price+tax;
        final double ActualPrice = price !=0 ?  Math.round((price / Integer.valueOf(product.getQty()) * 100))/100.00:0.0;
        holder.productNameTextView.setText(product.getName());
        holder.productsSubNameTextView.setText(product.getSku());
        final String quantity = String.valueOf(product.getQty())  != null ? String.valueOf(product.getQty()) : String.valueOf(0);
        holder.selectedQuantityTextView.setText(quantity);
        String productCartPrice = String.valueOf(Math.round(productPriceWithTax) * 100 / 100.00);
        String subTotalVal = String.format("%.2f", Double.valueOf(productCartPrice));
        holder.rupeeTextView.setText(subTotalVal);



    //    holder.quantityCartEditText.setText(String.valueOf(product.getQty()));

      /*  List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10+");

         dataAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_text_layout, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        holder.quantitySpinner.setAdapter(dataAdapter);
        if (product.getQty() != null) {
            int spinnerPosition = dataAdapter.getPosition(String.valueOf(product.getQty()));
         //   holder.quantitySpinner.setSelection(spinnerPosition);
            holder.quantitySpinner.setPrompt(String.valueOf(product.getQty()));
        }
*/
      // used flag first time then refresh activity only on delete product
      /*  holder.quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(flag==0) {
                    flag=1;
                }
                else {
                    if(holder.quantitySpinner.getSelectedItem().toString().equalsIgnoreCase("10+")){

                        holder.quantitySpinner.setVisibility(View.GONE);
                        holder.updateEdittext.setVisibility(View.VISIBLE);
                        holder.update.setVisibility(View.VISIBLE);
                        holder.cancel.setVisibility(View.VISIBLE);
                        holder.qty_textview.setVisibility(View.GONE);
                    }
                    else {
                        UpdateParentScreenWithCalculation();
                        addtoCart(product, holder.quantitySpinner.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityEditText = holder.updateEdittext.getText().toString();
                if(holder.updateEdittext.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context,"Invalid quantity",Toast.LENGTH_SHORT).show();
                }
                else {

                    addtoCart(product, quantityEditText);
                }
            }
        });

       // holder.cancel.setOnClickListener(this);
     /*   holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.updateEdittext.setVisibility(View.GONE);
                holder.update.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                holder.qty_textview.setVisibility(View.GONE);
                UpdateParentScreenWithCalculation();

            }
        });*/

        holder.decrementCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity = Integer.valueOf(holder.updateEdittext.getText().toString());
                if(quantity >= 2){
                    quantity = quantity - 1;
                    holder.updateEdittext.setText(String.valueOf(quantity));
                    product.setRow_total(ActualPrice*quantity);
                    product.setQty(quantity);
                    holder.selectedQuantityTextView.setText(String.valueOf(quantity));
                    String price = String.format("%.2f", Double.valueOf(product.getRow_total()));
                    holder.rupeeTextView.setText(price);
                  /*  UpdateParentScreenWithCalculation();
                    addtoCart(product, String.valueOf(quantity));*/
                }
            }
        });

        holder.incrementCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer quantity = Integer.valueOf(holder.updateEdittext.getText().toString());
                quantity = quantity + 1;
                holder.updateEdittext.setText(String.valueOf(quantity));
                product.setRow_total(ActualPrice*quantity);
                product.setQty(quantity);
                holder.selectedQuantityTextView.setText(String.valueOf(quantity));
                String price = String.format("%.2f", Double.valueOf(product.getRow_total()));
                holder.rupeeTextView.setText(price);
              /*  UpdateParentScreenWithCalculation();
                addtoCart(product, String.valueOf(quantity));*/
            }});

        holder.removeCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myQuittingDialogBox =new AlertDialog.Builder(view.getContext())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Are you sure you wish to Delete from cart?")
                        .setIcon(R.drawable.trash)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteItemFromCart(product);
                                notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                myQuittingDialogBox.show();
            }
        });

        holder.editCartImagview.setOnClickListener(this);

        holder.editCartImagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(open==1) {
                    holder.editCartLinearLayout.setVisibility(View.VISIBLE);
                    holder.updateEdittext.setText(String.valueOf(product.getQty()));
                    holder.updateEdittext.setVisibility(View.VISIBLE);
                    holder.update.setVisibility(View.VISIBLE);
                    holder.qty_textview.setVisibility(View.VISIBLE);
                  //  UpdateParentScreenWithCalculation();
                    open=0;
                }
                else  {
                    holder.editCartLinearLayout.setVisibility(View.GONE);
                    holder.updateEdittext.setText(String.valueOf(product.getQty()));
                    holder.updateEdittext.setVisibility(View.GONE);
                    holder.update.setVisibility(View.GONE);
                    holder.qty_textview.setVisibility(View.GONE);
                  //  UpdateParentScreenWithCalculation();
                    open=1;
                }

            }
        });


      /*  holder.updateProductCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
                Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_URL);
                SharedPreferences sharedPreferences = context.getSharedPreferences("MPOSShopping",Context.MODE_PRIVATE);
                Integer cartId = sharedPreferences.getInt("ProductCartId",0);
                MPOSServices services = retrofit.create(MPOSServices.class);
                String userToken = "Bearer " + sharedPreferences.getString("customerToken",null);

                String selectedQty = holder.quantityCartEditText.getText().toString();

                ProductCart productCart = new ProductCart();
                CartItem cItems = new CartItem();
                cItems.setQty(Integer.valueOf(selectedQty));
                cItems.setSku(product.getSku());
                cItems.setQuote_id(String.valueOf(cartId));
                cItems.setName(product.getName());
                cItems.setProductType(product.getProduct_type());
                cItems.setItemId(product.getItem_id());
                productCart.setCartItem(cItems);

                services.addProductToCart(productCart,userToken).enqueue(new Callback<ProductCartResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ProductCartResponse> call, @NonNull Response<ProductCartResponse> response) {
                        if(response.code() == 200 || response.isSuccessful()){
                            cartUpdate.onCartItemUpdatedOrDeleted(context);
                            Toast.makeText(context,"Cart updated successfully",Toast.LENGTH_LONG).show();
                        }else if(response.code() == 400) {
                            Toast.makeText(context,"Bad Request",Toast.LENGTH_LONG).show();
                        }else if(response.code() == 401) {
                            Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
                        }else if(response.code() == 500) {
                            Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProductCartResponse> call, @NonNull Throwable t) {
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
            } implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")
        });*/

      UpdateParentScreenWithCalculation();
    }

    public void UpdateParentScreenWithCalculation(){
        double totalPrice=0.0;
        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        for(ProductCartResponse product : productList)
        {
            totalPrice += Double.valueOf(product.getRow_total());
        }

        totalQty = 0;
        for (int i = 0; i<productList.size(); i++)
        {
            totalQty += productList.get(i).getQty();
        }
        Log.d("total ...", String.valueOf(totalQty));

        float ratePrice = sharedPreferences.getFloat("shipping_rate_service",0);
        Log.d("rateprice..", String.valueOf(ratePrice));

        float shipprate = ratePrice * totalQty;
        BillAmountTextView.setText(context.getString(R.string.Rs)+String.valueOf(Math.round(totalPrice * 100)/100.00));
        TotalTextView.setText(context.getString(R.string.Rs)+String.valueOf(Math.round((totalPrice+shipprate+taxValue) * 100)/100.00));
        String subTotalVal = String.format("%.2f", shipprate);
//        shippingChargesTextView.setText(String.valueOf(subTotalVal));
        /*InputMethodManager imm = (InputMethodManager) context.getSystemService((INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);*/
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void deleteItemFromCart(final ProductCartResponse product){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            Log.d("Delete....",product.getQuote_id()+".."+product.getProductId());
             sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
            Integer cartId = sharedPreferences.getInt("ProductCartId",0);
            String adminToken = "Bearer "+sharedPreferences.getString("adminToken",null);

            sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);



            mposServices.deleteProductFromCart(product.getQuote_id(),product.getProductId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        progressDialog.dismiss();
                        productList.remove(product);
                        UpdateParentScreenWithCalculation();
                        cartUpdate.onCartItemUpdatedOrDeleted(context);
                        Toast.makeText(context,"Product removed from cart",Toast.LENGTH_LONG).show();
                        hideKeyboard(activity);
                        notifyDataSetChanged();
                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 403){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Forbidden",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Log.d("failure...", t.getMessage());
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.editCartImagview:
                viewHolder.editCartLinearLayout.setVisibility(View.VISIBLE);
                viewHolder.updateEdittext.setText(String.valueOf(product.getQty()));
                viewHolder.updateEdittext.setVisibility(View.VISIBLE);
                viewHolder.update.setVisibility(View.VISIBLE);
                viewHolder.qty_textview.setVisibility(View.VISIBLE);
                UpdateParentScreenWithCalculation();

                break;
        }

    }


    public class MyCartProduct extends RecyclerView.ViewHolder{
        TextView productNameTextView,productsSubNameTextView,selectedQuantityTextView,rupeeTextView,decrementCartTextView,incrementCartTextView;
        ImageView productImageView,removeCartImageView,editCartImagview;
        EditText quantityCartEditText;
        CheckBox updateProductCheckBox;
        LinearLayout editCartLinearLayout;
        Spinner quantitySpinner;
        EditText updateEdittext;
        TextView cancel;
        Button update;
        TextView qty_textview;
        public MyCartProduct(View itemView) {
            super(itemView);

              productNameTextView     = itemView.findViewById(R.id.productNameTextView);
              productsSubNameTextView = itemView.findViewById(R.id.productsSubNameTextView);
              selectedQuantityTextView = itemView.findViewById(R.id.selectedQuantityTextView);
              rupeeTextView = itemView.findViewById(R.id.rupeeTextView);
              productImageView = itemView.findViewById(R.id.productImageView);
              quantitySpinner  = itemView.findViewById(R.id.quantity_spinner);
            decrementCartTextView=itemView.findViewById(R.id.decrementCartTextView);
            incrementCartTextView=itemView.findViewById(R.id.incrementCartTextView);
            update = itemView.findViewById(R.id.update_done);
         //   quantityCartEditText=itemView.findViewById(R.id.quantityCartEditText);
              removeCartImageView=itemView.findViewById(R.id.removeCartImageView);
              editCartImagview = itemView.findViewById(R.id.editCartImagview);
       //     updateProductCheckBox = itemView.findViewById(R.id.updateProductCheckBox);
              editCartLinearLayout = itemView.findViewById(R.id.editCartLinearLayout);
              updateEdittext = itemView.findViewById(R.id.update_quantity_edittext);

              qty_textview = itemView.findViewById(R.id.qty_textview);
        }
    }

    public void addtoCart(final ProductCartResponse product,final String selectedQty){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
         sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        MPOSServices services = retrofit.create(MPOSServices.class);
        String userToken = "Bearer " + sharedPreferences.getString("customerToken",null);


        ProductCartNewRequest productCartNewRequest = new ProductCartNewRequest();

        ProductCart productCart = new ProductCart();
        CartItem cItems = new CartItem();
        cItems.setQty(Integer.valueOf(selectedQty));
        cItems.setSku(product.getSku());
        cItems.setUser_id(String.valueOf(cartId));
        cItems.setName(product.getName());
        cItems.setProductType(product.getProduct_type());
        cItems.setItemId(Integer.parseInt(product.getItem_id()));
        cItems.setType("checkout");
        productCart.setCart(cItems);
        productCartNewRequest.setRequest(productCart);

        Gson gson = new Gson();
        String productGson = gson.toJson(productCartNewRequest);
        Log.d("Product JSON","Product Json == "+productGson);
        services.addProductToCart(productCartNewRequest).enqueue(new Callback<ProductCartResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductCartResponse> call, @NonNull Response<ProductCartResponse> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    flag=0;
                    cartUpdate.onCartItemUpdatedOrDeleted(context);
                    Toast.makeText(context,"Cart updated successfully",Toast.LENGTH_LONG).show();
                    UpdateParentScreenWithCalculation();
                    hideKeyboard((activity));
                    progressDialog.dismiss();
                }else if(response.code() == 400) {
                    Toast.makeText(context,"Product went out of stock please decrease the quantity",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else if(response.code() == 401) {
                    Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else if(response.code() == 500) {
                    Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductCartResponse> call, @NonNull Throwable t) {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (Exception e){

        }
    }
}
