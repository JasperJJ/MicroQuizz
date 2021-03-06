package com.jasper.microquizz.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jasper.microquizz.R;

import java.util.List;

public class locatieAdapter extends RecyclerView.Adapter<locatieAdapter.MyViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private List<String> mDatasetTitle;
    private List<String> mDatasetText;
    private List<String> mDatasetImage;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView titleView;
        TextView textView;
        ImageView imageView;

        MyViewHolder(View v) {
            super(v);
            titleView = v.findViewById(R.id.tv_score);
            textView = v.findViewById(R.id.tv_name);
            imageView = v.findViewById(R.id.iv_subject);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(this.textView, getAdapterPosition());
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public locatieAdapter(Context context, List<String> myDatasetTitle, List<String> myDatasetText, List<String> myDatasetImages) {
        this.mInflater = LayoutInflater.from(context);
        mDatasetTitle = myDatasetTitle;
        mDatasetText = myDatasetText;
        mDatasetImage = myDatasetImages;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public locatieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = mInflater.inflate(R.layout.location_list, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String title = mDatasetTitle.get(position);
        holder.titleView.setText(title);

        String text = mDatasetText.get(position);
        holder.textView.setText(text);

        byte[] imageBytes = Base64.decode(mDatasetImage.get(position), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imageView.setImageBitmap(decodedImage);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetText.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(TextView textView, int position);
    }
}
