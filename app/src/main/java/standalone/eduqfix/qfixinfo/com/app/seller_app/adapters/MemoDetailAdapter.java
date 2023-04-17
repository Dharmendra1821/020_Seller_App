package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ProductDetailModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ComplaintsDetailsActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CreditMemoModel;

public class MemoDetailAdapter extends RecyclerView.Adapter<MemoDetailAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CreditMemoModel> creditMemoModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    ArrayList<String> qtyValue;
    public static ArrayList<String> creditMemoItems = new ArrayList<>();

    public static ArrayList<String> getCreditMemoItems() {
        return creditMemoItems;
    }

    public static void setCreditMemoItems(ArrayList<String> creditMemoItems) {
        MemoDetailAdapter.creditMemoItems = creditMemoItems;
    }

    public MemoDetailAdapter(){
    }

    public MemoDetailAdapter(Context context, ArrayList<CreditMemoModel> creditMemoModels) {
        this.mContext = context;
        this.creditMemoModels = creditMemoModels;
        this.flag = flag;

    }

    @NonNull
    @Override
    public MemoDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_memo_details_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MemoDetailAdapter.MyViewHolder holder, final int position) {

        final CreditMemoModel creditMemoModel = creditMemoModels.get(position);
        pos = position;
        holder.quantity.setTag(pos);
        viewholder = holder;
        qtyValue = new ArrayList<>();
        holder.name.setText(creditMemoModel.getName());
        holder.sku.setText(creditMemoModel.getSku());

        final String minPrice = creditMemoModel.getBase_price();
        final double price = minPrice != null ? Math.round(Double.parseDouble(minPrice) * 100) / 100.00 : 0;
        holder.subtotal.setText(String.valueOf(price));
        String qtyVAL = creditMemoModel.getQty().replace(".","-");
        String[] qty = qtyVAL.split("-");
        String productQty = qty[0];

        qtyValue.add(0,"0");
        for (int i = 0; i < productQty.length(); i++){
            qtyValue.add(String.valueOf(i+1));
        }

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_layout, qtyValue);
        holder.quantity.setAdapter(statusAdapter);

        holder.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = (Integer) holder.quantity.getTag();

                try{
                    MemoDetailAdapter.getCreditMemoItems().remove(holder.getPosition());
                }catch (Exception e){
                }
                creditMemoItems.add(holder.getPosition(),creditMemoModel.getEntity_id()+":"+holder.quantity.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return creditMemoModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView subtotal;
        SearchableSpinner quantity;
        TextView sku;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.memo_name);
            subtotal = itemView.findViewById(R.id.memo_subtotal);
            sku = itemView.findViewById(R.id.memo_sku);
            quantity = itemView.findViewById(R.id.memo_quantity);


        }
    }


}
