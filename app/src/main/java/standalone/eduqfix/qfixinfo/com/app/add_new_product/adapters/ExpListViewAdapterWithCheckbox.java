package standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ConstantManager;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.GroupModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseSubCategoriesModel;

/**
 * Created by Mac on 19/04/18.
 */

@SuppressLint("UseSparseArrays")
public class ExpListViewAdapterWithCheckbox extends BaseExpandableListAdapter {


    private Context mContext;
    private HashMap<String, List<DatabaseSubCategoriesModel>> mListDataChild;

    private List<String> mListDataGroup;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;
    private HashMap<Integer, boolean[]> mHeadCheckStates;

    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    public   boolean[] mCheckedState;

    private String groupText;
    private String childText;
    public static ArrayList<HashMap<String, String>> parentItems;
    public static java.util.HashMap<Integer,Boolean> selectionCheckbox=new HashMap<Integer, Boolean>();

    public static ArrayList<String> category = new ArrayList<>();
    public static ArrayList<String> categoryName = new ArrayList<>();
    ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<>();

    public static ArrayList<String> getCategory() {
        return category;
    }

    public static ArrayList<String> getCategoryName() {
        return categoryName;
    }
    public static void setCategory(ArrayList<String> category) {
        ExpListViewAdapterWithCheckbox.category = category;
    }

    public static void setCategoryName(ArrayList<String> categoryname) {
        ExpListViewAdapterWithCheckbox.categoryName = categoryname;
    }
    public static int flag =0;




    public ExpListViewAdapterWithCheckbox(Context context, List<String> listDataGroup, HashMap<String, List<DatabaseSubCategoriesModel>> listDataChild) {

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;

        ExpListViewAdapterWithCheckbox.getCategory().clear();
        ExpListViewAdapterWithCheckbox.getCategoryName().clear();

        mChildCheckStates = new HashMap<Integer, boolean[]>();
        mHeadCheckStates = new HashMap<Integer, boolean[]>();
        flag =0;
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final String headerTitle = (String) getGroup(groupPosition);
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
   //     mExpandableListView.setGroupIndicator(null);
        groupText = (String) getGroup(groupPosition);
        final int mGroupPosition = groupPosition;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checkbox_header_list, null);

            // Initialize the GroupViewHolder defined at the bottom of this document
            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText =  convertView.findViewById(R.id.social_category_header);

            convertView.setTag(groupViewHolder);
        } else {

            groupViewHolder = (GroupViewHolder) convertView.getTag();

        }

        StringTokenizer tokens = new StringTokenizer(groupText, ":");
        final String first = tokens.nextToken();
        final String second = tokens.nextToken();



        groupViewHolder.mGroupText.setText(first);

        if(selectedParentCheckBoxesState.size() <= groupPosition){
            selectedParentCheckBoxesState.add(groupPosition, false);
        }else {
            groupViewHolder.mGroupText.setChecked(selectedParentCheckBoxesState.get(groupPosition));
        }



        groupViewHolder.mGroupText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean state = selectedParentCheckBoxesState.get(groupPosition);
                Log.d("TAG", "STATE = " + state);
                selectedParentCheckBoxesState.remove(groupPosition);
                selectedParentCheckBoxesState.add(groupPosition, state ? false : true);

                if(state){
                    category.remove(category.indexOf(second));
                    categoryName.remove(categoryName.indexOf(first));
                }
                else {
                    category.add(second);
                    categoryName.add(first);
                }

                }

              /*  if(state){
                    category.remove(category.indexOf(second));
                    categoryName.remove(categoryName.indexOf(first));

                }
                else {
                    category.add(second);
                    categoryName.add(first);

                }
*/
           //     notifyDataSetChanged();

        });
       /* groupViewHolder.mGroupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
*/
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
    }

    @Override
    public DatabaseSubCategoriesModel getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;


        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checkbox_childlist, null);

            childViewHolder = new ChildViewHolder();
            childViewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.sub_category_child);
            childViewHolder.dividerView = (View) convertView.findViewById(R.id.social_term_divider);

            convertView.setTag(R.layout.checkbox_childlist, childViewHolder);

        } else {

            childViewHolder = (ChildViewHolder) convertView.getTag(R.layout.checkbox_childlist);
        }

        childViewHolder.mCheckBox.setText(getChild(mGroupPosition,mChildPosition).getSub_name());
        childViewHolder.mCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);
        } else {
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];
            mChildCheckStates.put(mGroupPosition, getChecked);
            childViewHolder.mCheckBox.setChecked(false);
        }

        if(DetectSwipeDirectionActivity.getCategoryIds().contains(getChild(mGroupPosition,mChildPosition).getSub_category_id())){
            childViewHolder.mCheckBox.setChecked(true);
        }


        childViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    category.add(getChild(mGroupPosition,mChildPosition).getSub_category_id());
                    categoryName.add(getChild(mGroupPosition,mChildPosition).getSub_name());

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    try{
                        category.remove(category.indexOf(getChild(mGroupPosition,mChildPosition).getSub_category_id()));
                        categoryName.remove(categoryName.indexOf(getChild(mGroupPosition,mChildPosition).getSub_name()));
                    }catch (Exception e){

                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    public final class GroupViewHolder {

        CheckBox mGroupText;
    }

    public final class ChildViewHolder {

        TextView mChildText;
        CheckBox mCheckBox;
        CheckBox subCheckBox;
        View dividerView;
    }

    public static void retrieveValuesFromList(ArrayList list)
    {
        //Retrieving values from list
        int size = list.size();
        for(int i=0;i<size;i++)
        {
            System.out.println(list.get(i));
            category.add(list.get(i).toString());
            flag = 1;
        }
    }

    public static void retrieveValuesFromList1(ArrayList list)
    {
        //Retrieving values from list
        int size = list.size();
        for(int i=0;i<size;i++)
        {
            System.out.println(list.get(i));
            categoryName.add(list.get(i).toString());
            flag = 1;
        }
    }

}
