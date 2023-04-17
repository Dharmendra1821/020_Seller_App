package standalone.eduqfix.qfixinfo.com.app.customer.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.interfaces.ITabs;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Items;

import static android.content.Context.MODE_PRIVATE;

public class CustomerSearchAdaptor extends RecyclerView.Adapter<CustomerSearchAdaptor.CustomerSearchHolder> {

    private List<CustomerDetails> lCust;
    Context context;
    LinearLayout tabContainer;
    View view =null;
    SharedPreferences sharedPreferences;
    LinearLayout[] ParentLinearLayouts;
    ImageView[] ParentImageViews;
    TextView[] ParentTextViews;
    ITabs tabs;

    public CustomerSearchAdaptor(Activity activity){
        this.tabs = (ITabs) activity;
    }

    public CustomerSearchAdaptor(Context conts, List<CustomerDetails> lCustomers,LinearLayout tabContainer,Activity activity)
    {
        context=conts;
        lCust=lCustomers;
        this.tabContainer=tabContainer;
        sharedPreferences = conts.getSharedPreferences("StandAlone",MODE_PRIVATE);
        this.tabs = (ITabs) activity;
    }

    @NonNull
    @Override
    public CustomerSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_details_view,parent,false);

        return new CustomerSearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerSearchHolder holder, int position) {
        final CustomerDetails custDetails = lCust.get(position);
        if(custDetails != null){
            if(custDetails.getItems().size() > 0){
                final Items item=custDetails.getItems().get(0);
                holder.emailIdEditText.setText(item.getEmail());
                holder.customerNumberTextView.setText(String.valueOf(item.getId()));
                holder.customerNameTextView.setText(String.format("%s %s", item.getFirstname(), item.getLastname()));
                sharedPreferences.edit().putInt("CustomerId",item.getId()).apply();
                holder.customerDetailsLinearLayout.setVisibility(View.VISIBLE);
                holder.noCustomerLinearLayout.setVisibility(View.GONE);
            }else{
                holder.customerDetailsLinearLayout.setVisibility(View.GONE);
                holder.noCustomerLinearLayout.setVisibility(View.VISIBLE);
            }
        }
                view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabs.onSearchItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lCust.size();
    }

    public class CustomerSearchHolder extends RecyclerView.ViewHolder {
        TextView customerNameTextView,customerNumberTextView,emailIdEditText;
        LinearLayout noCustomerLinearLayout,customerDetailsLinearLayout;

        public CustomerSearchHolder(View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            customerNumberTextView=itemView.findViewById(R.id.customerNumberTextView);
            emailIdEditText=itemView.findViewById(R.id.emailIdEditText);
            customerDetailsLinearLayout = itemView.findViewById(R.id.customerDetailsLinearLayout);
            noCustomerLinearLayout = itemView.findViewById(R.id.noCustomerLinearLayout);
        }
    }
}
