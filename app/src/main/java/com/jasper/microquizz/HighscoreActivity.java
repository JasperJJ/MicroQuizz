package com.jasper.microquizz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasper.microquizz.adapters.highScoreAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class HighscoreActivity extends AppCompatActivity {

	private TextView highscore;
	private ImageView iv_back;
	private highScoreAdapter mAdapter;
	private RecyclerView recyclerView;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore);

		initControl();
		setRecyclerView();
		mProgressDialog = setProgressDialog();
		getHighscore();

		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void initControl() {
		iv_back = findViewById(R.id.ivBack);
		highscore = findViewById(R.id.EThighscore);
	}

	public void getHighscore() {
		showProgressDialog();

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		final String displayName = user.getDisplayName();

		final DatabaseReference mDatabase = FirebaseDatabase.getInstance()
		                                                    .getReference("/highscore/");

		mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				HashMap<String, HashMap<String, Long>> value = (HashMap) dataSnapshot
						.getValue();

				int size = value != null ? value.size() : 0;

				ArrayList<String> names = new ArrayList<>(size);
				ArrayList<Long> scores = new ArrayList<>(size);

				for (String name : value.keySet()) {
					names.add(name);

					Long sum = 0L;
					for (Long score : value.get(name).values()) {
						if (score != null) {
							sum += score;
						}
					}
					scores.add(sum);

					if (name.equals(displayName)) {
						highscore.setText(sum.toString());
					}
				}

				mAdapter = new highScoreAdapter(HighscoreActivity.this, names, scores);
				recyclerView.setAdapter(mAdapter);

				dismissProgressDialog();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				dismissProgressDialog();
			}
		});
	}

	private void setRecyclerView() {
		recyclerView = findViewById(R.id.rv_highscore);
		recyclerView.setHasFixedSize(true);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
	}

	private void showProgressDialog() {
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private ProgressDialog setProgressDialog() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Laden");
		return progressDialog;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}

