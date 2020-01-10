package com.jasper.microquizz;

import android.content.Context;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasper.microquizz.interfaces.LoadDataCallback;
import com.jasper.microquizz.models.Museums;
import java.util.ArrayList;
import java.util.Map;

public class LoadMuseum {
	private static final String TAG = "LoadMuseum";

	private final String MUSEA_KEY = "musea";
	private final String MUSEA_LIST_KEY = "museaList";
	private final String OBJECT_LIST_KEY = "objectList";
	private final String QUIZ_LIST_KEY = "quizList";

	private App app;
	private LoadDataCallback listner;


	public LoadMuseum(Context context, LoadDataCallback listner) {
		app = (App) context.getApplicationContext();
		this.listner = listner;
		loadData();
	}

	private void loadData() {
		Log.d(TAG, "Loading quiz data: Status: Started");
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference(MUSEA_KEY);
		myRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				// This method is called once with the initial value and again
				// whenever data at this location is updated.
				Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();

				Museums musea = new Museums();
				musea.setMuseaList((ArrayList<String>) values.get(MUSEA_LIST_KEY));
				musea.setObjectList((ArrayList) values.get(OBJECT_LIST_KEY));
				musea.setQuizlist((ArrayList) values.get(QUIZ_LIST_KEY));

				app.setMuseum(musea);

				Log.d(TAG, "Loading quiz data: Status: Complete");
				listner.done(musea);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				Log.e(TAG, "Failed to read value.", error.toException());
			}
		});
	}
}
