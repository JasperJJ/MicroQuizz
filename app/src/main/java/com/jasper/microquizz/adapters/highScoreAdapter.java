package com.jasper.microquizz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jasper.microquizz.R;
import java.util.ArrayList;
import java.util.List;

public class highScoreAdapter extends RecyclerView.Adapter<highScoreAdapter.MyViewHolder> {
    private LayoutInflater mInflater;

    private List<String> mDatasetName;
    private List<Long> mDatasetScore;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView scoreView;

        MyViewHolder(View v) {
            super(v);
            nameView = v.findViewById(R.id.tv_name);
            scoreView = v.findViewById(R.id.tv_score);
        }
    }

    public highScoreAdapter(Context context, ArrayList<String> DatasetName, ArrayList<Long> DatasetScore) {
        this.mInflater = LayoutInflater.from(context);
        mDatasetName = DatasetName;
        mDatasetScore = DatasetScore;
    }

    @NonNull
    @Override
    public highScoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.highscore_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = mDatasetName.get(position);
        Long score = mDatasetScore.get(position);

        holder.nameView.setText(name + ": ");
        holder.scoreView.setText(score.toString());
    }

    @Override
    public int getItemCount() {
        return mDatasetName.size();
    }
}
