package com.jasper.microquizz;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class NFCActivity extends AppCompatActivity {

	// Bluetooth
	private TextView mBluetoothStatus;

	private BluetoothAdapter mBTAdapter;
	private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
	private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
	private Handler mHandler; // Our main handler that will receive callback notifications

	private static final UUID BTMODULEUUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
	// #defines for identifying shared types between calling functions

	private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
	private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
	private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status


	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;
	private String bluetoothDeviceAddress;
	private BluetoothGatt bluetoothGatt;
	private int connectionState = STATE_DISCONNECTED;

	private final static String TAG = NFCActivity.class.getSimpleName();

	public final static String ACTION_GATT_CONNECTED =
			"com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED =
			"com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED =
			"com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE =
			"com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA =
			"com.example.bluetooth.le.EXTRA_DATA";
	public final static UUID UUID_HEART_RATE_MEASUREMENT =
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);

		// Bluetooth
		mBluetoothStatus = findViewById(R.id.bluetoothStatus);
		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBTAdapter = bluetoothManager.getAdapter();

//		mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == MESSAGE_READ) {
					String readMessage = null;
					try {
						readMessage = new String((byte[]) msg.obj, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					Log.e("bluetooth", "handleMessage: " + readMessage);
//					mReadBuffer.setText();
				}

				if (msg.what == CONNECTING_STATUS) {
					if (msg.arg1 == 1) {
						mBluetoothStatus.setText("Connected to Device: " + (String) ( msg.obj ));
					} else {
						mBluetoothStatus.setText("Connection Failed");
					}
				}
			}
		};

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
		}
		startConnection("F1:1F:AE:B0:8A:7A");
	}

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is carried out as per profile specifications.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			int flag = characteristic.getProperties();
			int format = -1;
			if ((flag & 0x01) != 0) {
				format = BluetoothGattCharacteristic.FORMAT_UINT16;
				Log.d(TAG, "Heart rate format UINT16.");
			} else {
				format = BluetoothGattCharacteristic.FORMAT_UINT8;
				Log.d(TAG, "Heart rate format UINT8.");
			}
			final int heartRate = characteristic.getIntValue(format, 1);
			Log.d(TAG, String.format("Received heart rate: %d", heartRate));
			intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
		} else {
			// For all other profiles, writes the data formatted in HEX.
			final byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				final StringBuilder stringBuilder = new StringBuilder(data.length);
				for(byte byteChar : data)
					stringBuilder.append(String.format("%02X ", byteChar));
				intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
						stringBuilder.toString());
			}
		}
		sendBroadcast(intent);
	}


	// Bluetooth
	public void startConnection(final String rawAddress) {

		close();

		if (!mBTAdapter.isEnabled()) {
			Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
			return;
		}

		mBluetoothStatus.setText("Connecting...");

		final String address = rawAddress.substring(0, 17);
		// Spawn a new thread to avoid blocking the GUI one
		new Thread() {
			public void run() {
				boolean fail = false;

				Log.e("bluetooth", "" + BluetoothAdapter.checkBluetoothAddress(address));
				BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
				final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
					@Override
					public void onConnectionStateChange(BluetoothGatt gatt, int status,
							int newState) {
						String intentAction;
						if (newState == BluetoothProfile.STATE_CONNECTED) {
							intentAction = ACTION_GATT_CONNECTED;
							connectionState = STATE_CONNECTED;
							broadcastUpdate(intentAction);
							Log.i(TAG, "Connected to GATT server.");
							Log.i(TAG, "Attempting to start service discovery:" +
									bluetoothGatt.discoverServices());

						} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
							intentAction = ACTION_GATT_DISCONNECTED;
							connectionState = STATE_DISCONNECTED;
							Log.i(TAG, "Disconnected from GATT server.");
							broadcastUpdate(intentAction);
						}
					}

					@Override
					// New services discovered
					public void onServicesDiscovered(BluetoothGatt gatt, int status) {
						if (status == BluetoothGatt.GATT_SUCCESS) {
							broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
						} else {
							Log.w(TAG, "onServicesDiscovered received: " + status);
						}
					}

					@Override
					// Result of a characteristic read operation
					public void onCharacteristicRead(BluetoothGatt gatt,
							BluetoothGattCharacteristic characteristic,
							int status) {
						if (status == BluetoothGatt.GATT_SUCCESS) {
							broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
						}
					}

				};
				bluetoothGatt = device.connectGatt(getBaseContext(), false, gattCallback);

				try {
					mBTSocket = createBluetoothSocket(device);
				} catch (IOException e) {
					fail = true;
					Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT)
					     .show();
				}
				// Establish the Bluetooth socket connection.
				try {
					mBTSocket.connect();
				} catch (IOException e) {
					try {
						fail = true;
						mBTSocket.close();
						mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
						        .sendToTarget();
					} catch (IOException e2) {
						//insert code to deal with this
						Toast.makeText(getBaseContext(), "Socket creation failed",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (fail == false) {
					mConnectedThread = new ConnectedThread(mBTSocket);
					mConnectedThread.start();

					mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, "NameHere")
					        .sendToTarget();
				}
			}
		}.start();
	}

	private class ConnectedThread extends Thread {

		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];  // buffer store for the stream
			int bytes; // bytes returned from read()
			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.available();
					if (bytes != 0) {
						buffer = new byte[1024];
						SystemClock
								.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
						bytes = mmInStream.available(); // how many bytes are ready to be read?
						bytes = mmInStream
								.read(buffer, 0, bytes); // record how many bytes we actually read
						mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
						        .sendToTarget(); // Send the obtained bytes to the UI activity
					}
				} catch (IOException e) {
					e.printStackTrace();

					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(String input) {
			byte[] bytes = input.getBytes();           //converts entered String into bytes
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
		return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
		//creates secure outgoing connection with BT device using UUID
	}

	private void close() {
		if (bluetoothGatt == null) {
			return;
		}
		bluetoothGatt.close();
		bluetoothGatt = null;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		close();
		finish();
	}
}
