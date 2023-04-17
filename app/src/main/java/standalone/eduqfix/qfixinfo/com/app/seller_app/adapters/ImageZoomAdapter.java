package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;

public class ImageZoomAdapter extends RecyclerView.Adapter<ImageZoomAdapter.MyViewHolder>
{
    Context context;
    List<String> imageZooms;

    public ImageZoomAdapter(Context context,   List<String> imageZooms)
    {
        this.context=context;
        this.imageZooms=imageZooms;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_zoom,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

if (imageZooms !=null) {
    if (!imageZooms.equals("")) {
        for (int j = 0; j < imageZooms.size(); j++) {
            Glide.with(context).load(imageZooms.get(i)).into(myViewHolder.imageView);
        }
    } else {
        myViewHolder.imageView.setImageResource(R.drawable.no_product);
    }
}
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.zoomdialog,null);
                myViewHolder.photoView = view.findViewById(R.id.photo123);

                Glide.with(context).load(imageZooms.get(i)).into(myViewHolder.photoView);
               //Glide.with(context).load(imageZooms.get(i)).into(myViewHolder.photoView);
                builder.setView(view);
                AlertDialog alertDialog =builder.create();
                alertDialog.show();

            }
        });

      /*  BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imageZooms.get(i), options);
        myViewHolder.imageView.setImageBitmap(bitmap);
*/

    }

    @Override
    public int getItemCount() {
        return imageZooms.size();   }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        PhotoView photoView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagezoom1);

        }
    }
}



