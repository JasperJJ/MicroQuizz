package com.jasper.microquizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasper.microquizz.interfaces.LoadDataCallback;
import com.jasper.microquizz.models.Museums;
import com.jasper.microquizz.utils.QUIZ;
import java.util.HashMap;

public class PlayActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;

	private Context context;

	private Museums musea;
	private HashMap<String, String> quiz;

	private TextView tv_vraag;
	private RadioButton rb_antwoord1;
	private RadioButton rb_antwoord2;
	private RadioButton rb_antwoord3;
	private RadioGroup rg_antwoorden;
	private int mHighscore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		configureNavigationDrawer();
		configureToolbar();

		context = getApplicationContext();

		initControl();
		getQuestions();
	}

	public void initControl() {
		tv_vraag = findViewById(R.id.tv_vraag);
		rb_antwoord1 = findViewById(R.id.rb_antwoord1);
		rb_antwoord2 = findViewById(R.id.rb_antwoord2);
		rb_antwoord3 = findViewById(R.id.rb_antwoord3);

		rg_antwoorden = findViewById(R.id.rg_antwoorden);
		rg_antwoorden.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int index) {
				final View radioButton = rg_antwoorden.findViewById(index);

				String answer = ( (AppCompatRadioButton) radioButton ).getText().toString();
				final boolean isCorrect = checkAnswer(answer);

				if (isCorrect) {
					( (AppCompatRadioButton) radioButton ).setChecked(false);
					for (int i = 0; i < rg_antwoorden.getChildCount(); i++) {
						( rg_antwoorden.getChildAt(i) ).setEnabled(false);
					}
					newQuestion();

				} else {
					( (AppCompatRadioButton) radioButton ).setChecked(false);
					for (int i = 0; i < rg_antwoorden.getChildCount(); i++) {
						( rg_antwoorden.getChildAt(i) ).setEnabled(false);
					}
					newQuestion();
				}
			}
		});
	}

	private void addToHigscore() {
		mHighscore += 10;
	}

	private int getHigscore() {
		return mHighscore;
	}

	private void uploadHighscore() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		final String displayName = user.getDisplayName();

		final int newHighScore = getHigscore();
		final String highScoreKey = "" + musea.getCurrentMuseumKey() + musea.getCurrentObjectKey();

		final String path = "/highscore/" + displayName + "/" + highScoreKey;

		final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(path);

		mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Object value = dataSnapshot.getValue(Object.class);

				int highScore = 0;
				if (value != null) {
					highScore = Integer.parseInt(value.toString());
				}

				if (displayName != null && !displayName.isEmpty() && newHighScore > highScore) {
					mDatabase.setValue(newHighScore);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	private void getQuestions() {
		App app = (App) context.getApplicationContext();
		if (app.getMuseum() == null) {
			new LoadMuseum(getApplicationContext(), new LoadDataCallback() {
				@Override
				public void done(Museums museums) {
					musea = museums;
					try {
						quiz = musea.getQuiz();
					} catch (Exception e) {
						finish();
					}
					if (quiz != null) {
						setQuestions(quiz);
					} else {
						finish();
					}
				}
			});
		} else {
			musea = app.getMuseum();
			try {
				quiz = musea.getQuiz();
			} catch (Exception ignored) {
			}

			if (quiz != null) {
				setQuestions(quiz);
			} else {
				finish();
			}
		}
	}

	private void setQuestions(HashMap<String, String> quiz) {
		String vraag = quiz.get(QUIZ.question.name());
		String keuze1 = quiz.get(QUIZ.choice1.name());
		String keuze2 = quiz.get(QUIZ.choice2.name());
		String keuze3 = quiz.get(QUIZ.choice3.name());

		tv_vraag.setText(vraag);
		rb_antwoord1.setText(keuze1);
		rb_antwoord2.setText(keuze2);
		rb_antwoord3.setText(keuze3);

		for (int i = 0; i < rg_antwoorden.getChildCount(); i++) {
			( rg_antwoorden.getChildAt(i) ).setEnabled(true);
		}

//		For testing
//		System.out.println("Musea name: " + musea.getMuseaName());
//		System.out.println("Object name: " + musea.getObjectName());
//		System.out.println("Quiz name: " + musea.getQuiz());
//
//		System.out.println("Musea list: " + musea.getMuseaList());
//		System.out.println("Object list: " + musea.getObjectList());
//		System.out.println("Quiz list: " + musea.getQuizList());
	}

	private boolean checkAnswer(String answer) {
		String antwoord = quiz.get(QUIZ.answer.name());
		boolean isCorrect = antwoord.equals(answer);
		if (isCorrect) {
			addToHigscore();
			Snackbar snackbar = Snackbar.make(tv_vraag, "Antwoord Goed", Snackbar.LENGTH_SHORT);
			snackbar.getView().setBackgroundColor(
					ContextCompat.getColor(PlayActivity.this, R.color.colorGreen));
			snackbar.show();
		} else {
			Snackbar snackbar = Snackbar.make(tv_vraag, "Antwoord Fout", Snackbar.LENGTH_SHORT);
			snackbar.getView().setBackgroundColor(
					ContextCompat.getColor(PlayActivity.this, R.color.colorRed));
			snackbar.show();
		}
		return isCorrect;
	}

	private void newQuestion() {
		quiz = musea.nextQuiz();
		Handler handler = new Handler();
		if (quiz.size() != 0) {
			handler.postDelayed(new Runnable() {
				public void run() {
					setQuestions(quiz);
				}
			}, 1300);
		} else {
			uploadHighscore();
			Toast.makeText(PlayActivity.this, "Quiz Afgerond", Toast.LENGTH_SHORT).show();
			handler.postDelayed(new Runnable() {
				public void run() {
					finish();
				}
			}, 1300);
		}
	}

	private void configureToolbar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionbar = getSupportActionBar();
		if (actionbar != null) {
			actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);
			actionbar.setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}
	}

	private void configureNavigationDrawer() {
		drawerLayout = findViewById(R.id.drawer_layout);
		NavigationView navView = findViewById(R.id.navigation);
		navView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						int itemId = menuItem.getItemId();
						if (itemId == R.id.action_home) {
							Intent intent = new Intent(PlayActivity.this, HomeActivity.class);
							startActivity(intent);
							return true;
						} else if (itemId == R.id.uitloggen) {
							Intent intent = new Intent(PlayActivity.this, Beginscherm.class);
							startActivity(intent);
							return true;
						}
						return false;
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			drawerLayout.openDrawer(GravityCompat.START);
			return true;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
