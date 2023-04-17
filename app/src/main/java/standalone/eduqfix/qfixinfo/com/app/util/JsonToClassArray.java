package standalone.eduqfix.qfixinfo.com.app.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerAddressList;
import standalone.eduqfix.qfixinfo.com.app.login.model.CustomerDetails;


public class JsonToClassArray {
    Context context;
    public JsonToClassArray(Context cont)
    {
        context=cont;
    }

    public List<CustomerDetails> GetCustomers()
    {
        try
        {
            InputStream is = context.getAssets().open("customers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            List<CustomerDetails> tax =new Gson().fromJson(new String(buffer, "UTF-8"), new TypeToken<List<CustomerDetails>>() {
            }.getType());
            return  tax;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public List<CustomerAddressList> GetCustomersAddress()
    {
        try
        {
            InputStream is = context.getAssets().open("customeraddress.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            List<CustomerAddressList> tax =new Gson().fromJson(new String(buffer, "UTF-8"), new TypeToken<List<CustomerAddressList>>() {
            }.getType());
            return  tax;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

}
