package com.jasper.microquizz;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

// importeer firebase extensie
import com.google.firebase.auth.FirebaseAuth;
import com.jasper.microquizz.bluetooth.Bluetooth;
import com.jasper.microquizz.bluetooth.uart;
import com.jasper.microquizz.interfaces.ParsedNdefRecord;
import com.jasper.microquizz.interfaces.onBLEConnection;
import com.jasper.microquizz.nfc.parser.NdefMessageParser;
import java.util.List;
import com.jasper.microquizz.interfaces.LoadDataCallback;
import com.jasper.microquizz.models.Museums;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private Button logout;
    private DrawerLayout drawerLayout;
    private TextView tv_description;
    private Button btn_highscore;
    private Button btn_location;
    private ImageView iv_nfcscanner;
    private LinearLayout rl_loading;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private Bluetooth mBlue;
    private boolean bleConnected;

	private uart mUART;

    private AlphaAnimation fadeIn;
    private AlphaAnimation fadeOut;
    private String bltID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // haal huidige sessie op
        firebaseAuth = FirebaseAuth.getInstance();

        // Preload Quiz data
	    App app = (App) getApplicationContext();
	    if (app.getMuseum() == null) {
		    new LoadMuseum(getApplicationContext(), new LoadDataCallback() {
			    @Override
			    public void done(Museums museums) { }
		    });
	    }

// test code om via highscore uit te loggen dit kan dus voor andere doeleinde worden gebruikt.
        //logout = (Button)findViewById(R.id.btn_highscore);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logout();
//            }
//        });



        initControl();
        setBackGroundColors();
        configureNavigationDrawer();
        configureToolbar();

        fadeIn = new AlphaAnimation(0.0f , 1.0f);
        fadeOut = new AlphaAnimation(1.0f , 0.0f);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

//        if (nfcAdapter == null) {
//            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        mBlue = new Bluetooth(this, new onBLEConnection() {

            @Override
            public void onConnected() {
                bleConnected = true;
                if (mUART.initialize()) {
                    if (mUART.connect(bltID)) {
//                        mUART.enableTXNotification();
//                        mUART.getSupportedGattServices();
                        mUART.writeRXCharacteristic("Hallo :".getBytes());
                        mUART.writeRXCharacteristic(":".getBytes());
                        mUART.disconnect();
                        mUART.close();
                    }
                }
                startPlayActivity();
            }

            @Override
            public void onDisconnected(String reason) {
                bleConnected = false;
                setNFCScanner(reason);
            }
        });

        mUART = new uart(this);
    }

    //logout functie om uit te roepen en te verwijzen naar het inlogscherm
    // als je naar een ander scherm wilt gaan wanneer je uitlogd kan je dat hier toepassen
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, Beginscherm.class));

    }


    // dubbele code voor het menu, dit was niet nodig en veroorzaakte problemen met het inloggen.
    //@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

//    @Override
//    public  boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.uitloggen: {
//                Logout();
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    // zet de toolbaar in de applicatie waar de sidebar aan gekoppeld is.
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);
            actionbar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    // dit is voor de sidebar als er op een menu item wordt gedrukt.
    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_home) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }else if (itemId == R.id.action_feedback) {
                    Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                    // verwijs naar een andere activiteit locaties.
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }else if (itemId == R.id.action_gegevens) {
                    Intent intent = new Intent(HomeActivity.this, gegevens.class);
                    // verwijs naar een andere activiteit locaties.
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.uitloggen) {
                    // als er op logout wordt gedrukt dan roepen we de uitlog functie op.
                    Logout();
                    drawerLayout.closeDrawer(GravityCompat.START);
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

    // een fucntie om variabelen te koppelen aan de buttons, textvieuws etc.
    public void initControl() {
        tv_description = findViewById(R.id.tv_description);
        btn_highscore = findViewById(R.id.btn_highscore);
        btn_location = findViewById(R.id.btn_location);
        iv_nfcscanner = findViewById(R.id.iv_nfcscanner);
        rl_loading = findViewById(R.id.rl_loading);

        btn_location.setOnClickListener(this);
        btn_highscore.setOnClickListener(this);
    }


// bepaal kleuren voor id's etc.
    public void setBackGroundColors() {
        GradientDrawable tv_description_bg = (GradientDrawable) tv_description.getBackground();
        GradientDrawable bt_highscore_bg = (GradientDrawable) btn_highscore.getBackground();
        GradientDrawable bt_location_bg = (GradientDrawable) btn_location.getBackground();

        tv_description_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_highscore_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_location_bg.setColor(getResources().getColor(R.color.colorGreen));
    }

        // verwijzen naar andere schermen wanneer er op x wordt gedrukt.
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_location) {
            Intent intent = new Intent(this, museumKiezen.class);
            this.startActivity(intent);

        } else if (v.getId() == R.id.btn_highscore) {
            Intent intent = new Intent(this, HighscoreActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bleConnected) {
            mBlue.close();
        }

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettings();
            }

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                displayMsgs(msgs);
            }
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str);
            if (i != ( size - 1 ))
                builder.append("\n");
        }

        String[] parts = builder.toString().split(",");
        String museaKey = parts[0].split("=")[1];
        String objectKey = parts[1].split("=")[1];
        String quizKey = parts[2].split("=")[1];
        bltID = parts[3].split("=")[1];

        App app = (App) getApplicationContext();
        Museums museum = app.getMuseum();
        museum.setCurrentMuseumKey(Integer.parseInt(museaKey));
        museum.setCurrentObjectKey(Integer.parseInt(objectKey));
        museum.setCurrentquizKey(Integer.parseInt(quizKey));

        if (mBlue.startConnection(bltID)) {
            setLoading();
        }
    }

    private void startPlayActivity() {
        Intent intent = new Intent(this, PlayActivity.class);
        this.startActivity(intent);
    }

    private void setLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv_nfcscanner.startAnimation(fadeOut);
                rl_loading.startAnimation(fadeIn);

                iv_nfcscanner.setVisibility(View.INVISIBLE);
                rl_loading.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setNFCScanner(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv_nfcscanner.clearAnimation();
                rl_loading.clearAnimation();

                iv_nfcscanner.setVisibility(View.VISIBLE);
                rl_loading.setVisibility(View.INVISIBLE);

                if (reason.equals("Failed")) {
	                Toast.makeText(HomeActivity.this, "Mislukt probeer opnieuw", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
