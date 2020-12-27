package com.e.jobkwetu.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.e.jobkwetu.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        //viewHolder.textViewDescription.setText("This is slider item " + position);
        String defaultValue = "https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260";
        SharedPreferences sharedPref = context.getSharedPreferences("SomeName", Context.MODE_PRIVATE);
        String retrievedString1 = sharedPref.getString("String1", defaultValue);
        String retrievedString2 = sharedPref.getString("String2", defaultValue);
        String retrievedString3 = sharedPref.getString("String3", defaultValue);
        String retrievedString4 = sharedPref.getString("String4", defaultValue);
        String retrievedString5 = sharedPref.getString("String5", defaultValue);


        switch (position) {
            case 0:
                Picasso.get()
                        .load(retrievedString1)
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                Picasso.get()
                        .load(retrievedString2)
                        .into(viewHolder.imageViewBackground);
                break;
            case 2:
                Picasso.get()
                        .load(retrievedString3)
                        .into(viewHolder.imageViewBackground);
                break;
            case 3:
                Picasso.get()
                        .load(retrievedString4)
                        .into(viewHolder.imageViewBackground);
                break;
            default:
                Picasso.get()
                        .load(retrievedString5)
                        .into(viewHolder.imageViewBackground);
                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 5;
    }

     class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        //TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.sldimage);
            //textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}