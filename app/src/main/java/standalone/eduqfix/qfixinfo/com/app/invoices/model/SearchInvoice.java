package standalone.eduqfix.qfixinfo.com.app.invoices.model;

import android.text.TextUtils;

/**
 * Created by darshan on 27/2/19.
 */

public class SearchInvoice {

    public String name;
    public String invoiceNumber;

    public SearchInvoice(String name, String invoiceNumber){
        this.name = name;
        this.invoiceNumber = invoiceNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean isValidData(){
        return (!TextUtils.isEmpty(getName()) || !TextUtils.isEmpty(getInvoiceNumber()));
    }
}
