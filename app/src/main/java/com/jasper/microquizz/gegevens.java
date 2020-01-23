package com.jasper.microquizz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class gegevens extends AppCompatActivity {

	private FirebaseAuth firebaseAuth;
	private FirebaseUser mUser;
	private Button bVerwijder;
	private EditText etEmail;
	private EditText etName;
	private Button btnChangePassword;
	private Button btnSave;
	private ImageView iv_back;
	private ProgressDialog progressDialog;
	private DrawerLayout drawerLayout;
	private boolean isNameChanged;
	private boolean isEmailChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gegevens);

		initControl();
		setBackGroundColors();
		configureNavigationDrawer();
		configureToolbar();

		firebaseAuth = FirebaseAuth.getInstance();
		mUser = firebaseAuth.getCurrentUser();
		reAuthenticatie();
		if (mUser == null) {
			startActivity(new Intent(gegevens.this, Beginscherm.class));
			finish();
		}

		bVerwijder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popUpverwijder();
			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				changeProfile();
			}
		});

		btnChangePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(gegevens.this, WachtwoordWijzig.class));
			}
		});

		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void changeProfile() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Laden");
		progressDialog.show();
		veranderNaam();
	}

	private void reAuthenticatie() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.re_authentication_prompt, null);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setPositiveButton("Doorgaan", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				EditText etEmail = dialogView.findViewById(R.id.et_email);
				EditText etPassword = dialogView.findViewById(R.id.et_password);
				String email = etEmail.getText().toString();
				String password = etPassword.getText().toString();

				if (!email.isEmpty() && !password.isEmpty()) {
					final AuthCredential credential = EmailAuthProvider
							.getCredential(email, password);

					final ProgressDialog dialog = new ProgressDialog(gegevens.this);
					dialog.setMessage("Gegevens ophalen...");
					dialog.setIndeterminate(true);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();

					mUser.reauthenticate(credential)
					     .addOnCompleteListener(new OnCompleteListener<Void>() {
						     @Override
						     public void onComplete(@NonNull Task<Void> task) {
							     if (task.isSuccessful()) {
								     fillItems();
								     dialog.dismiss();
							     } else {
								     dialog.dismiss();
								     Toast.makeText(gegevens.this, "Authenticatie mislukt",
										     Toast.LENGTH_SHORT).show();
								     reAuthenticatie();
							     }
						     }
					     });
				} else {
					Toast.makeText(gegevens.this, "Email en wachtwoord moet worden ingevuld",
							Toast.LENGTH_SHORT).show();
					reAuthenticatie();
				}
			}
		});
		dialogBuilder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				gegevens.super.onBackPressed();
			}
		});

		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	private void fillItems() {
		String name = mUser.getDisplayName();
		String email = mUser.getEmail();

		etEmail.setText(email);
		etName.setText(name);
	}

	public void initControl() {
		bVerwijder = findViewById(R.id.btn_delete);
		etEmail = findViewById(R.id.et_email);
		etName = findViewById(R.id.et_name);
		btnChangePassword = findViewById(R.id.btn_change_password);
		btnSave = findViewById(R.id.btn_save);
		iv_back = findViewById(R.id.ivBack);
	}

	private void VerwijderAccount() {
		mUser.delete()
		     .addOnCompleteListener(new OnCompleteListener<Void>() {
			     @Override
			     public void onComplete(@NonNull Task<Void> task) {
				     if (task.isSuccessful()) {
					     Toast.makeText(gegevens.this, "Uw account is succesvol verwijderd",
							     Toast.LENGTH_SHORT).show();
					     finish();
					     startActivity(new Intent(gegevens.this, Beginscherm.class));
				     } else {
					     Toast.makeText(gegevens.this, "Uw account verwijderen is mislukt",
							     Toast.LENGTH_SHORT).show();
				     }
			     }
		     });
	}

	private void popUpverwijder() {
		AlertDialog.Builder builder = new AlertDialog.Builder(gegevens.this);

		builder.setCancelable(true);
		builder.setTitle("Weet je zeker dat je jouw account wilt verwijderen?");

		builder.setNegativeButton("nee", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		});

		builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				VerwijderAccount();
			}
		});
		builder.show();
	}

	private void veranderEmail() {
		String nieuwEmail = etEmail.getText().toString().trim();
		String oldEmail = mUser.getEmail();

		isEmailChanged = true;
		if (oldEmail == null || !oldEmail.equals(nieuwEmail)) {
			isEmailChanged = false;
			mUser.updateEmail(nieuwEmail)
			     .addOnCompleteListener(new OnCompleteListener<Void>() {
				     @Override
				     public void onComplete(@NonNull Task<Void> task) {
					     isEmailChanged = true;
					     if (task.isSuccessful()) {
						     onSuccess();
					     } else {
						     onFailed();
						     Toast.makeText(gegevens.this,
								     "Emailadres opslaan is niet gelukt, probeer opnieuw",
								     Toast.LENGTH_SHORT).show();
					     }
				     }
			     });
		} else {
			onSuccess();
		}
	}

	public void veranderNaam() {
		String nieuwNaam = etName.getText().toString().trim();
		String oldNaam = mUser.getDisplayName();

		isNameChanged = true;
		if (oldNaam == null || !oldNaam.equals(nieuwNaam)) {
			isNameChanged = false;
			UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
					.setDisplayName(nieuwNaam)
					.build();

			mUser.updateProfile(profileUpdates)
			     .addOnCompleteListener(new OnCompleteListener<Void>() {
				     @Override
				     public void onComplete(@NonNull Task<Void> task) {
					     isNameChanged = true;
					     veranderEmail();
					     if (task.isSuccessful()) {
						     onSuccess();
					     } else {
						     onFailed();
						     Toast.makeText(gegevens.this,
								     "Naam wijzigen is niet gelukt, probeer opnieuw",
								     Toast.LENGTH_SHORT).show();
					     }
				     }
			     });
		} else {
			veranderEmail();
		}
	}

	public void setBackGroundColors() {
		btnChangePassword.setTextColor(Color.WHITE);
		btnSave.setTextColor(Color.WHITE);
		bVerwijder.setTextColor(Color.WHITE);

		GradientDrawable btnChangePassword_bg = (GradientDrawable) btnChangePassword
				.getBackground();
		GradientDrawable btnSave_bg = (GradientDrawable) btnSave.getBackground();
		GradientDrawable bVerwijder_bg = (GradientDrawable) bVerwijder.getBackground();

		btnChangePassword_bg.setColor(getResources().getColor(R.color.colorBlue));
		btnSave_bg.setColor(getResources().getColor(R.color.colorBlue));
		bVerwijder_bg.setColor(getResources().getColor(R.color.colorRed));
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

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.black));
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();
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
							Intent intent = new Intent(gegevens.this, HomeActivity.class);
							startActivity(intent);
							return true;
						} else if (itemId == R.id.uitloggen) {
							Intent intent = new Intent(gegevens.this, Beginscherm.class);
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

	public void onSuccess() {
		if (isEmailChanged && isNameChanged) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	public void onFailed() {
		if (isEmailChanged && isNameChanged) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
