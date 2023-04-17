package standalone.eduqfix.qfixinfo.com.app.add_product.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import standalone.eduqfix.qfixinfo.com.app.R;

public class GalleryFragment extends Fragment {
    public static View fragment;
    Button select_gallery;
    String productId,valueId;
    String productSku;
    int position;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.gallery_fragment, container, false);


        select_gallery = fragment.findViewById(R.id.select_image_gallery);

        productId = getArguments().getString("product_id");
        position  = getArguments().getInt("position");
        valueId   = getArguments().getString("value_id");
        productSku = getArguments().getString("product_sku");



        select_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), EditorDemoActivity.class);
//                intent.putExtra("product_id",productId);
//                intent.putExtra("position",position);
//                intent.putExtra("value_id",valueId);
//                intent.putExtra("product_sku",productSku);
//                intent.putExtra("product_page","2");
//                startActivity(intent);

            }
        });


        return fragment;
      }
    }
