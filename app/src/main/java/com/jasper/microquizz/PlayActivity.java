package com.jasper.microquizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
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
				View radioButton = rg_antwoorden.findViewById(index);

				String answer = ( (AppCompatRadioButton) radioButton ).getText().toString();
				boolean isCorrect = checkAnswer(answer);

				if (isCorrect) {
					( (AppCompatRadioButton) radioButton ).setChecked(false);
					newQuestion();
				} else {
					( (AppCompatRadioButton) radioButton ).setChecked(false);
				}
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
					quiz = musea.getQuiz();
					setQuestions(quiz);
				}
			});
		} else {
			musea = app.getMuseum();
			quiz = musea.getQuiz();
			setQuestions(quiz);
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
		if (!isCorrect) {
			Toast.makeText(this, "Fout antwoord", Toast.LENGTH_SHORT).show();
		}
		return isCorrect;
	}

	private void newQuestion() {
		quiz = musea.nextQuiz();
		if (quiz.size() != 0) {
			setQuestions(quiz);
		} else {
			Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
			finish();
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
}
