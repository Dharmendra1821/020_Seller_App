package standalone.eduqfix.qfixinfo.com.app.invoices.model;

/**
 * Created by darshan on 27/2/19.
 */

public class SearchInvoiceRequest {

//    private InvoiceList invoiceList;
//
//    public InvoiceList getInvoiceList() {
//        return invoiceList;
//    }
//
//    public void setInvoiceList(InvoiceList invoiceList) {
//        this.invoiceList = invoiceList;
//    }
    private String firstName;
    private Integer userId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
