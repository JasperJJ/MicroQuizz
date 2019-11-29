package com.jasper.microquizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.microquizz.adapters.locatieAdapter;
import com.jasper.microquizz.adapters.museumAdapter;

import java.util.ArrayList;

public class museumKiezen extends AppCompatActivity implements museumAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private museumAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_kiezen);



        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        // specify an adapter (see also next example)
        mAdapter = new museumAdapter(this, list);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(TextView textView, int position) {
        Toast.makeText(getApplicationContext(), "Text: " + textView.getText() + " Position: " + position, Toast.LENGTH_SHORT).show();
    }

}
