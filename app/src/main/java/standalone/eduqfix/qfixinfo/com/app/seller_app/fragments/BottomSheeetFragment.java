package standalone.eduqfix.qfixinfo.com.app.seller_app.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.StoreCustomerAdapter;

public class BottomSheeetFragment  extends BottomSheetDialogFragment {

    private static BottomSheeetFragment.ClickListener clickListener;
    private static BottomSheeetFragment.closeClickListener closeClickListener;
    Button submit;
    ImageView close;
    TextView fromdate,todate;
    LinearLayout fromLayout,toLayout;
    private int mYear, mMonth, mDay;
    SharedPreferences sharedPreferences;
    TextView resetFilter;
    public static BottomSheeetFragment newInstance() {
        return new BottomSheeetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        sharedPreferences = getActivity().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

         submit = view.findViewById(R.id.bottom_sheet_submit);
         close = view.findViewById(R.id.close_sheet);
         fromdate = view.findViewById(R.id.sheet_from_date);
         todate = view.findViewById(R.id.sheet_to_date);
         fromLayout = view.findViewById(R.id.from_date_layout);
         toLayout = view.findViewById(R.id.to_date_layout);
         resetFilter = view.findViewById(R.id.reset_filter);

         String fromDate = sharedPreferences.getString("from_date",null);
         if(fromDate!=null){
             fromdate.setText(fromDate);
         }

        String toDate = sharedPreferences.getString("to_date",null);
        if(toDate!=null){
            todate
                    .setText(toDate);
        }

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("from_date", null).apply();
                sharedPreferences.edit().putString("to_date", null).apply();
                closeClickListener.onCloseClick(view);
            }
        });
        fromLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthString = String.valueOf((monthOfYear + 1));
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayMonth = String.valueOf(dayOfMonth);
                                if (dayMonth.length() == 1) {
                                    dayMonth = "0" + dayMonth;
                                }
                                fromdate.setText(dayMonth + "-" + monthString + "-" + year);
                                sharedPreferences.edit().putString("from_date", fromdate.getText().toString()).apply();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMaxDate(c.getTimeInMillis());
            }
        });

        toLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthString = String.valueOf((monthOfYear + 1));
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayMonth = String.valueOf(dayOfMonth);
                                if (dayMonth.length() == 1) {
                                    dayMonth = "0" + dayMonth;
                                }
                                todate.setText(dayMonth + "-" + monthString + "-" + year);
                                sharedPreferences.edit().putString("to_date", todate.getText().toString()).apply();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMaxDate(c.getTimeInMillis());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromdate.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Select from date",Toast.LENGTH_LONG).show();
                }
                else if(todate.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Select to date",Toast.LENGTH_LONG).show();
                }
                else {
                    String fromDate = fromdate.getText().toString();
                    String toDate = todate.getText().toString();
                    clickListener.onItemClick(view,fromDate,toDate);
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeClickListener.onCloseClick(view);
            }
        });


        return view;

    }

    public void setOnItemClickListener(BottomSheeetFragment.ClickListener clickListener) {
        BottomSheeetFragment.clickListener = clickListener;
    }

    public void setOnCloseClickListener(BottomSheeetFragment.closeClickListener clickListener) {
        BottomSheeetFragment.closeClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v,String fromDate,String toDate);
    }

    public interface closeClickListener {
        void onCloseClick(View v);
    }
}
