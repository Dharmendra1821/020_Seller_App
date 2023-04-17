package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SubContactListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OutofStcokModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.QfixLibraryModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactListData;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class SMSContactListAdapter extends RecyclerView.Adapter<SMSContactListAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<SMSContactModel> smsContactModels;
    ArrayList<SMSContactListData> smsContactListDataList;
    SMSContactListAdapter.MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    String quantity;
    ArrayList<SMSContactModel> arraylist;
    ArrayList<String> contactListSize;
    public static ArrayList<String> getSmsIds() {
        return smsIds;
    }

    public static void setSmsIds(ArrayList<String> smsIds) {
        SMSContactListAdapter.smsIds = smsIds;
    }

    private static SMSContactListAdapter.ClickListener clickListener;
    public static ArrayList<String> smsIds = new ArrayList<>();

    public static ArrayList<SMSContactListData> getSmsContactListData() {
        return smsContactListData;
    }

    public static void setSmsContactListData(ArrayList<SMSContactListData> smsContactListData) {
        SMSContactListAdapter.smsContactListData = smsContactListData;
    }

    public static ArrayList<SMSContactListData> smsContactListData = new ArrayList<>();
    private List<SMSContactModel> catList;

    public SMSContactListAdapter() {

    }

    public SMSContactListAdapter(Context context, ArrayList<SMSContactModel> smsContactModels){
        this.mContext = context;
        this.arraylist = new ArrayList<SMSContactModel>();
        this.smsContactModels = smsContactModels;
    }

    public SMSContactListAdapter(Context context, List<SMSContactModel> smsContactModels, ArrayList<SMSContactListData> smsContactListDataList,ArrayList<String> contactListSize, String name) {
        this.mContext = context;
        this.smsContactModels = smsContactModels;
        this.smsContactListDataList = smsContactListDataList;
        this.contactListSize = contactListSize;
        this.catList = smsContactModels;

       // Log.d("ddddddd",contactListSize);

    }

    @NonNull
    @Override
    public SMSContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_contact_list, parent, false);
        SMSContactListAdapter.MyViewHolder viewHolder = new SMSContactListAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SMSContactListAdapter.MyViewHolder holder, final int position) {

        final SMSContactModel smsContactModel = smsContactModels.get(position);
        pos = position;
        viewholder = holder;


        sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);





        holder.id.setText(smsContactModels.get(position).getList_id());
        holder.name.setText(smsContactModels.get(position).getListname().equalsIgnoreCase("null") ? "" : smsContactModels.get(position).getListname());
        holder.contact_size.setText(contactListSize.get(position)+" "+ "View");
        holder.contact_size.setPaintFlags( holder.contact_size.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        holder.checkBox.setChecked(smsContactModels.get(position).isSelected());
        holder.checkBox.setTag(position);

        holder.contact_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSContactListAdapter.getSmsContactListData().clear();
                for (int i=0; i < smsContactListDataList.size();i++){
                    if(Objects.equals(smsContactModels.get(position).getList_id(), smsContactListDataList.get(i).getList_id())){
                        Log.d("datata....", String.valueOf(smsContactListDataList.get(i)));
                        smsContactListData.add(smsContactListDataList.get(i));

                    }

                }
                Intent intent = new Intent(mContext, SubContactListActivity.class);
                mContext.startActivity(intent);

            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.checkBox.getTag();
                if (smsContactModels.get(pos).isSelected()) {
                    smsContactModels.get(pos).setSelected(false);
                    smsIds.remove(smsContactModel.getList_id());
                } else {
                    smsContactModels.get(pos).setSelected(true);

                    smsIds.add(smsContactModel.getList_id());
                }
            }
        });


    }
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    @Override
    public int getItemCount() {
        return smsContactModels.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView id;
        CheckBox checkBox;
        TextView contact_size;


        public MyViewHolder(View itemView) {
            super(itemView);
            id  =  itemView.findViewById(R.id.sms_contact_id);
            name  =  itemView.findViewById(R.id.sms_contact_name);
            checkBox = itemView.findViewById(R.id.sms_contact_checkbox);
            contact_size = itemView.findViewById(R.id.sms_contact_name_size);

        }
    }
    public SMSContactModel getWordAtPosition(int position) {
        return smsContactModels.get(position);
    }

    public void setOnItemClickListener(SMSContactListAdapter.ClickListener clickListener) {
        SMSContactListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    smsContactModels = catList;
                } else {
                    List<SMSContactModel> filteredList = new ArrayList<>();
                    for (SMSContactModel row : catList) {
                        if (row.getListname().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    smsContactModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = smsContactModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                smsContactModels = (ArrayList<SMSContactModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
