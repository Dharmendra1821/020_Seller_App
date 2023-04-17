package standalone.eduqfix.qfixinfo.com.app.seller_app.model;
import java.util.List;
public class Items
{
    private int id;

    private int parent_id;

    private String name;

    private boolean is_active;

    private int position;

    private int level;

    private String children;

    private String created_at;

    private String updated_at;

    private String path;

    private List<String> available_sort_by;

    private boolean include_in_menu;

    private List<Custom_attributes> custom_attributes;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setParent_id(int parent_id){
        this.parent_id = parent_id;
    }
    public int getParent_id(){
        return this.parent_id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setIs_active(boolean is_active){
        this.is_active = is_active;
    }
    public boolean getIs_active(){
        return this.is_active;
    }
    public void setPosition(int position){
        this.position = position;
    }
    public int getPosition(){
        return this.position;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public int getLevel(){
        return this.level;
    }
    public void setChildren(String children){
        this.children = children;
    }
    public String getChildren(){
        return this.children;
    }
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public void setUpdated_at(String updated_at){
        this.updated_at = updated_at;
    }
    public String getUpdated_at(){
        return this.updated_at;
    }
    public void setPath(String path){
        this.path = path;
    }
    public String getPath(){
        return this.path;
    }
    public void setAvailable_sort_by(List<String> available_sort_by){
        this.available_sort_by = available_sort_by;
    }
    public List<String> getAvailable_sort_by(){
        return this.available_sort_by;
    }
    public void setInclude_in_menu(boolean include_in_menu){
        this.include_in_menu = include_in_menu;
    }
    public boolean getInclude_in_menu(){
        return this.include_in_menu;
    }
    public void setCustom_attributes(List<Custom_attributes> custom_attributes){
        this.custom_attributes = custom_attributes;
    }
    public List<Custom_attributes> getCustom_attributes(){
        return this.custom_attributes;
    }
}
