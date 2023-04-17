package standalone.eduqfix.qfixinfo.com.app.login.model;

/**
 * Created by darshan on 27/4/19.
 */

public class ForgotPassword {

    private String email;
    private String template;
    private Integer websiteId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }
}
