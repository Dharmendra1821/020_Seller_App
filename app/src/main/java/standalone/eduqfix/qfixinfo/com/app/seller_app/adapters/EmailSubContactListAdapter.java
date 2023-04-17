package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SubContactListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactListData;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;

public class EmailSubContactListAdapter extends RecyclerView.Adapter<EmailSubContactListAdapter.MyViewHolder>  {

    Context mContext;
    List<SMSContactModel> smsContactModels;
    List<SMSContactListData> smsContactListDataList;
    EmailSubContactListAdapter.MyViewHolder viewholder;
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
        EmailSubContactListAdapter.smsIds = smsIds;
    }

    private static EmailSubContactListAdapter.ClickListener clickListener;
    public static ArrayList<String> smsIds = new ArrayList<>();


    private List<SMSContactModel> catList;

    public EmailSubContactListAdapter() {

    }



    public EmailSubContactListAdapter(Context context, List<SMSContactListData> smsContactListDataList) {
        this.mContext = context;
        this.smsContactListDataList = smsContactListDataList;
        this.catList = smsContactModels;

       // Log.d("ddddddd",contactListSize);

    }

    @NonNull
    @Override
    public EmailSubContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_sub_contact_list, parent, false);
        EmailSubContactListAdapter.MyViewHolder viewHolder = new EmailSubContactListAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmailSubContactListAdapter.MyViewHolder holder, final int position) {

        final SMSContactListData smsContactModel = smsContactListDataList.get(position);
        pos = position;
        viewholder = holder;


        sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);





        holder.id.setText(smsContactListDataList.get(position).getId());
        holder.name.setText(smsContactListDataList.get(position).getName().equalsIgnoreCase("null") ? "" : smsContactListDataList.get(position).getName());

//        holder.checkBox.setChecked(smsContactListDataList.get(position).isSelected());
//        holder.checkBox.setTag(position);
//
//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Integer pos = (Integer) holder.checkBox.getTag();
//                if (smsContactListDataList.get(pos).isSelected()) {
//                    smsContactListDataList.get(pos).setSelected(false);
//                    smsIds.remove(smsContactListDataList.get(position).getId());
//                } else {
//                    smsContactListDataList.get(pos).setSelected(true);
//                    smsIds.add(smsContactListDataList.get(position).getId());
//                }
//            }
//        });


    }
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    @Override
    public int getItemCount() {
        return smsContactListDataList.size();
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



        public MyViewHolder(View itemView) {
            super(itemView);
            id  =  itemView.findViewById(R.id.sms_sub_contact_id);
            name  =  itemView.findViewById(R.id.sms_sub_contact_name);
          //  checkBox = itemView.findViewById(R.id.sms_sub_contact_checkbox);


        }
    }
    public SMSContactListData getWordAtPosition(int position) {
        return smsContactListDataList.get(position);
    }

    public void setOnItemClickListener(EmailSubContactListAdapter.ClickListener clickListener) {
        EmailSubContactListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    smsContactModels = catList;
//                } else {
//                    List<SMSContactModel> filteredList = new ArrayList<>();
//                    for (SMSContactModel row : catList) {
//                        if (row.getListname().toLowerCase().contains(charString.toLowerCase())) {
//                            filteredList.add(row);
//                        }
//                    }
//                    smsContactModels = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = smsContactModels;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                smsContactModels = (ArrayList<SMSContactModel>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

}
