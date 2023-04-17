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
import android.widget.EditText;
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
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.IProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.datachanged;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.MediaGalleryEntry;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;


public class ProductGroupAdapter extends RecyclerView.Adapter<ProductGroupAdapter.ViewHolder>  {

    List<ProductLink> productLinkList;
    List<MediaGalleryEntry> mediaGalleryEntries;
    private Context context1;
    datachanged datachanged1;
      IProductList iProduct;
    ProductLink productLink;
    Integer count = 0,c=0;
     Integer quantity;
     ArrayList<Integer> QTY = new ArrayList<>();
     ArrayList<String> name = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
     String smallimage = null;
    String Productimage=null;
    String thumbnailimage = null;

    List<String> checkname = new ArrayList<>();


    public ProductGroupAdapter(Context context, List<ProductLink> productLinkList, List<MediaGalleryEntry> mediaGalleryEntries, datachanged datachanged  )

    {
        this.datachanged1=datachanged;
        this.context1=context;
        this.mediaGalleryEntries=mediaGalleryEntries;
        this.productLinkList = productLinkList;

    }

    @NonNull
    @Override
    public ProductGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context1).inflate(R.layout.product_group,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ProductLink productLink = productLinkList.get(i);


        SharedPreferences sharedPreferences = context1.getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("KEY",true);


        thumbnailimage= (String) productLink.getThumbnail();
        smallimage   = (String) productLink.getProductSmallImage();
        Productimage = (String) productLink.getProductImage();
        editor.putString("SMALLIMAGE",smallimage);
        editor.putString("PRODUCTIMAGE",Productimage);
        editor.putString("THUMBNAILIMAGE",thumbnailimage);

        editor.apply();


      viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        thumbnailimage= (String) productLink.getThumbnail();
        smallimage   = (String) productLink.getProductSmallImage();
        Productimage = (String) productLink.getProductImage();
        editor.putString("SMALLIMAGE",smallimage);
        editor.putString("PRODUCTIMAGE",Productimage);
        editor.putString("THUMBNAILIMAGE",thumbnailimage);

        editor.apply();

        Intent intent = ((Activity) context1).getIntent();

        ((Activity) context1).finish();
        context1.startActivity(intent);

    }
});


        quantity = sharedPreferences.getInt("QUANTITY", 0);

        Double price1= Double.parseDouble(productLink.getProductPrice());
        viewHolder.productprice.setText(price1.toString());
        price.add(price1.toString());

        viewHolder.productname.setText(productLink.getProductName());
        name.add(productLink.getProductName());
        QTY.add(1);


        if (quantity<=0)
        {
            viewHolder.editTextqty.setText("1");
            quantity = 1;
            count++;
        }
        else
        {
            //viewHolder.editTextqty.setText(quantity.toString());
            viewHolder.editTextqty.setText("1");
            quantity = 1;
            count++;

        }



        viewHolder.editTextqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int in, KeyEvent keyEvent) {

                Integer check = (Integer) Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));
             if ( check == 0 )
             {
                 count=0;
             }
             else {
                  name.set(i, productLink.getProductName());
                 QTY.set(i,check);
                 c++;
                 count++;

             }

return false;
            }
        });



    }

//    public void addcartitem(String authtoken,Integer listquantity) {
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
//
//        for (int q=0;q<QTY.size();q++)
//        {
//            if (QTY.get(q)==0)
//            {
//                count=0;
//            }
//        }
//
//        if (count>0)
//        {
//
//            for (int i = 0;i<listquantity;i++) {
//
//
//                    if (name.size() != 0)
//                    {
//                          for (int k = 0; k <name.size(); k++) {
//
//                                  final int lo;
//                                  lo = k;
//                                  cItems.setQty(QTY.get(k));
//                                  cItems.setSku(productLinkList.get(k).getLinkedProductSku());
//                                  cItems.setName(name.get(k));
//                                  cItems.setPrice(Double.valueOf(price.get(k)));
//                                  cItems.setQuote_id(String.valueOf(cartId));
//                                  cItems.setProductType(productLinkList.get(k).getLinkedProductType());
//                                  productCart.setCartItem(cItems);
//
//
//                                  Gson gson = new Gson();
//                                  String productGson = gson.toJson(productCart);
//                                  Log.d("Product JSON", "Product Json == " + productGson);
//
//                                  services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                                      @Override
//                                      public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//                                          if (response.code() == 200 || response.isSuccessful()) {
//                                              progressDialog.dismiss();
//                                              Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
//
//                                              Handler handler = new Handler();
//                                              handler.postDelayed(new Runnable() {
//                                                  @Override
//                                                  public void run() {
//                                                Intent intent = ((Activity) context1).getIntent();
//                                              ((Activity) context1).finish();
//                                             context1.startActivity(intent);
//
//                                                  }
//                                              }, 8000);
//
//
//                                              //productLinkList.add(productLink);
//
//
//                                          } else if (response.code() == 400) {
//                                              progressDialog.dismiss();
//
//                                             final AlertDialog.Builder builder = new AlertDialog.Builder(context1);
//                                              builder.setTitle("OUT OF STOCK");
//                                              builder.setMessage("PRODUCT NAME : " + productLinkList.get(lo).getProductName());
//                                              builder.setCancelable(false);
//                                              builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                  @Override
//                                                  public void onClick(DialogInterface dialogInterface, int i) {
//                                                      Intent intent = ((Activity) context1).getIntent();
//                                                      ((Activity) context1).finish();
//                                                      context1.startActivity(intent);
//                                                  }
//                                              });
//                                              final AlertDialog alertDialog = builder.create();
//                                              alertDialog.show();
//
//                                              //   Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                          } else if (response.code() == 401) {
//                                              progressDialog.dismiss();
//                                              Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                          } else if (response.code() == 500) {
//                                              progressDialog.dismiss();
//                                              Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                          } else {
//                                              progressDialog.dismiss();
//                                              Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                          }
//                                      }
//
//                                      @Override
//                                      public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                          progressDialog.hide();
//                                          Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                                      }
//                                  });
//
//
//                          }
//
//            }
//
//       else {
//                        Toast.makeText(context1, "No Product To Add", Toast.LENGTH_SHORT).show();
//                    }
//            }
//
//}
//        //ELSE OF COUNT
//
//else {
//    progressDialog.dismiss();
//    Toast.makeText(context1,"Quantity Cannot Be  0 ",Toast.LENGTH_SHORT).show();
//}
//
//    }









    @Override
    public int getItemCount() {
        return productLinkList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView productname , productprice;
        final EditText editTextqty;
        ViewHolder(View view)
        {
            super(view);
            productname=view.findViewById(R.id.productname10);
            productprice = view.findViewById(R.id.productprice);
            editTextqty=view.findViewById(R.id.EDITQTY1G);


            editTextqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    String n1 = editTextqty.getText().toString();


                    return false;
                }
            });

        }

    }
}

