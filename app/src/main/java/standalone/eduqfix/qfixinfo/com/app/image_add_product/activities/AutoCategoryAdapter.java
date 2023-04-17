package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;

public class AutoCategoryAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<DatabaseCategoriesModel> originalList;
    private List<DatabaseCategoriesModel> suggestions = new ArrayList<>();
    private Filter filter = new AutoCategoryAdapter.CustomFilter();

    public AutoCategoryAdapter(Context context, List<DatabaseCategoriesModel> originalList) {
        this.mContext = context;
        this.originalList = originalList;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public DatabaseCategoriesModel getItem(int i) {
        return suggestions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AutoCategoryAdapter.ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.particular_list, viewGroup, false);
            holder = new AutoCategoryAdapter.ViewHolder();
            holder.autoText = ((TextView) view.findViewById(R.id.textview));
            view.setTag(holder);
        } else {
            holder = (AutoCategoryAdapter.ViewHolder) view.getTag();
        }
        holder.autoText.setText(suggestions.get(i).getName());

        return view;

    }

    private static class ViewHolder {
        TextView autoText;
        TextView Id;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();
            // Check if the Original List and Constraint aren't null.
            if (originalList != null && constraint != null) {
                for (int i = 0; i < originalList.size(); i++) {
                    if (originalList.get(i).getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(originalList.get(i));
                    }
                }
            }
            // Create new Filter Results and return this to publishResults;
            FilterResults results = new FilterResults();
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
