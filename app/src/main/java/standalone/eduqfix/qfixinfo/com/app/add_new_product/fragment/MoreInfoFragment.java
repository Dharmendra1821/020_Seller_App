package standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import standalone.eduqfix.qfixinfo.com.app.R;

public class MoreInfoFragment extends Fragment {
    public static View fragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.more_info_fragment, container, false);




        return fragment;

    }
    }
