package com.example.proofofconcept;



import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Adapter extends ArrayAdapter {


    private List<Models> listmodels;
    private int resource;
    private LayoutInflater layoutInflater;


    public Adapter(Context context, int resource, List<Models> objects) {
        super(context, resource, objects);
        listmodels = objects;
        this.resource = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        Holder holder = null;

        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.row, null);
            holder.imageView = convertView.findViewById(R.id.image);
            holder.heading = convertView.findViewById(R.id.heading);
            holder.description =  convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }


        final ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        ImageLoader.getInstance().displayImage(listmodels.get(position).getImageHref(), holder.imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });


        holder.heading.setText(listmodels.get(position).getTitle());
        holder.description.setText(listmodels.get(position).getDescription());


        return convertView;
    }

   private class Holder{

       private ImageView imageView;
       private TextView heading;
       private TextView description;

    }
};



