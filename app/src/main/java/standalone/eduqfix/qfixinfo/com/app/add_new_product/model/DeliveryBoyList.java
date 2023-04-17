package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

public class DeliveryBoyList {

    private int id;
    private String entityId;
    private String email;
    private String firstname;
    private String lastname;
    private String contact;
    private String status;

    public DeliveryBoyList(String entityId, String firstname, String lastname) {
        this.entityId = entityId;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DeliveryBoyList() {

    }

    public DeliveryBoyList(int id, String entityId, String email, String firstname, String lastname,
                           String contact, String status) {
        this.id = id;
        this.entityId = entityId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.status = status;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
