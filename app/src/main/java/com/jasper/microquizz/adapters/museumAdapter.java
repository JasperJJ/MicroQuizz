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

import com.jasper.microquizz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class museumAdapter extends RecyclerView.Adapter<museumAdapter.MyViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private List<String> mDatasetName;
    private List<String> mDatasetInfo;
    private List<String> mDatasetImage;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView nameView;
        TextView infoView;
        ImageView imageView;

        MyViewHolder(View v) {
            super(v);
            nameView = v.findViewById(R.id.tv_name);
            infoView = v.findViewById(R.id.tv_info);
            imageView = v.findViewById(R.id.iv_subject);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(this.nameView, getAdapterPosition());
                mClickListener.onItemClick(this.infoView, getAdapterPosition());
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public museumAdapter(Context context, List<String> myDatasetName, List<String> myDatasetInfo, List<String> myDatasetImage) {
        this.mInflater = LayoutInflater.from(context);
        mDatasetName = myDatasetName;
        mDatasetInfo = myDatasetInfo;
        mDatasetImage = myDatasetImage;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public museumAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = mInflater.inflate(R.layout.musea_list, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String name = mDatasetName.get(position);
        holder.nameView.setText(name);

        String info = mDatasetInfo.get(position);
        holder.infoView.setText(info);

        byte[] imageBytes = Base64.decode(mDatasetImage.get(position), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imageView.setImageBitmap(decodedImage);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetName.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(TextView textView, int position);
    }
}
