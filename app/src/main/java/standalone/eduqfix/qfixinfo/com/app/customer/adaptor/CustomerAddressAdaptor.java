package standalone.eduqfix.qfixinfo.com.app.customer.adaptor;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Addresses;

import static android.content.Context.MODE_PRIVATE;

public class CustomerAddressAdaptor extends RecyclerView.Adapter<CustomerAddressAdaptor.CustomerAddressHolder> {
    List<Addresses> lCustAddress;
    Context context;
    public CustomerAddressAdaptor(Context conts, List<Addresses> lCustomerAddress)
    {
        lCustAddress=lCustomerAddress;
        context=conts;
    }
    @NonNull
    @Override
    public CustomerAddressAdaptor.CustomerAddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_address_view,parent,false);

        return new CustomerAddressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAddressAdaptor.CustomerAddressHolder holder, int position) {
        final Addresses customerAddressList =lCustAddress.get(position);
        if (!customerAddressList.getId().equals(0))
        {
            holder.SelectAddressCheckBox.setEnabled(true);
            holder.customerAddressCounter.setText("Address "+ (position+1));
            holder.SelectAddressCheckBox.setChecked(customerAddressList.getChecked());
            String sFormAddress = customerAddressList.getStreet().get(0).toString() + "\n" +
                    customerAddressList.getCity()  + "\n" +
                    customerAddressList.getPostcode()+ "\n" +
                    customerAddressList.getRegion().getRegion()+ "\n India";
            holder.customerAddress.setText(sFormAddress);
            holder.SelectAddressCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Addresses> lCustomerAddress = Linq4j.asEnumerable(lCustAddress).where(new Predicate1<Addresses>() {
                        @Override
                        public boolean apply(Addresses addresses) {
                            return addresses.getChecked();
                        }
                    }).toList();
                    if (lCustomerAddress.size()>0)
                    {
                        customerAddressList.setChecked(false);
                        ((CheckBox)view).setChecked(false);
                        Toast.makeText(context,"Please select only one address for product deliver",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        customerAddressList.setChecked(true);
                        String sFormAddress = customerAddressList.getStreet().get(0).toString() + "\n" +
                                customerAddressList.getCity()  + "\n" +
                                customerAddressList.getPostcode()+ "\n" +
                                customerAddressList.getRegion().getRegion()+ "\n India";
                        Log.d("selected area ....",sFormAddress);

                        Log.d("...", String.valueOf(customerAddressList.getRegion_id()));
                      SharedPreferences sharedPreferences = context.getSharedPreferences("StandAlone", MODE_PRIVATE);
                      sharedPreferences.edit().putString("shipping_street",customerAddressList.getStreet().get(0).toString()).apply();
                      sharedPreferences.edit().putString("shipping_city",customerAddressList.getCity()).apply();
                      sharedPreferences.edit().putString("shipping_postcode",customerAddressList.getPostcode()).apply();
                      sharedPreferences.edit().putString("shipping_region",customerAddressList.getRegion().getRegion()).apply();
                      sharedPreferences.edit().putInt("shipping_regionId", customerAddressList.getRegion_id()).apply();
                      sharedPreferences.edit().putString("shipping_telephone", customerAddressList.getTelephone()).apply();
                    }
                }
            });
        }
        else
        {
            holder.customerAddress.setText(customerAddressList.getCity());
            holder.SelectAddressCheckBox.setVisibility(View.GONE);
            holder.customerAddressCounter.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return lCustAddress.size();
    }

    public class CustomerAddressHolder extends RecyclerView.ViewHolder {
        CheckBox SelectAddressCheckBox;
        TextView customerAddressCounter,customerAddress;
        public CustomerAddressHolder(View itemView) {
            super(itemView);
            SelectAddressCheckBox=itemView.findViewById(R.id.SelectAddressCheckBox);
            customerAddressCounter=itemView.findViewById(R.id.customerAddressCounter);
            customerAddress=itemView.findViewById(R.id.customerAddress);


        }

    }
}
