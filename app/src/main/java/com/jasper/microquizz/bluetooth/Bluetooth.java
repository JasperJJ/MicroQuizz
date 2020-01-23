package com.jasper.microquizz.bluetooth;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.jasper.microquizz.Manifest;
import com.jasper.microquizz.interfaces.onBLEConnection;
import java.util.UUID;

public class Bluetooth {

	private Context mContext;

	// Bluetooth
	private onBLEConnection mBLEConnection;
	private BluetoothAdapter mBTAdapter;
	private BluetoothGatt bluetoothGatt;

	private final static String TAG = Bluetooth.class.getSimpleName();

	private final static String ACTION_GATT_CONNECTED =
			"com.jasper.microquizz.bluetooth.ACTION_GATT_CONNECTED";
	private final static String ACTION_GATT_DISCONNECTED =
			"com.jasper.microquizz.bluetooth.ACTION_GATT_DISCONNECTED";
	private final static String ACTION_GATT_SERVICES_DISCOVERED =
			"com.jasper.microquizz.bluetooth.ACTION_GATT_SERVICES_DISCOVERED";
	private final static String ACTION_DATA_AVAILABLE =
			"com.jasper.microquizz.bluetooth.ACTION_DATA_AVAILABLE";
	private final static String EXTRA_DATA =
			"com.jasper.microquizz.bluetooth.EXTRA_DATA";
	private final static UUID UUID_HEART_RATE_MEASUREMENT =
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public Bluetooth(Context context, onBLEConnection bleConnection) {
		mContext = context;
		mBLEConnection = bleConnection;
		// Bluetooth

		mBTAdapter = BluetoothAdapter.getDefaultAdapter();

		if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) mContext,
					new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
		}
	}

	// Bluetooth
	public boolean startConnection(final String rawAddress) {
		close();

		if (!mBTAdapter.isEnabled()) {
			Toast.makeText(mContext, "Bluetooth staat niet aan", Toast.LENGTH_SHORT).show();
			return false;
		}

		Log.d(TAG, "Connecting to GATT server.");

		final String address = rawAddress.substring(0, 17);
		// Spawn a new thread to avoid blocking the GUI one
		new Thread() {
			public void run() {
				BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
				bluetoothGatt = device.connectGatt(mContext, false, new BluetoothGattCallback() {
					@Override
					public void onConnectionStateChange(BluetoothGatt gatt, int status,
							int newState) {
						String intentAction;
						if (newState == BluetoothProfile.STATE_CONNECTED) {
							Log.i(TAG, "Connected to GATT server.");
							intentAction = ACTION_GATT_CONNECTED;
							onConnected();
							broadcastUpdate(intentAction);

						} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
							Log.i(TAG, "Disconnected from GATT server.");
							intentAction = ACTION_GATT_DISCONNECTED;
							broadcastUpdate(intentAction);
							onDisconnected("Failed");
						}
					}

					@Override
					// New services discovered
					public void onServicesDiscovered(BluetoothGatt gatt, int status) {
						if (status == BluetoothGatt.GATT_SUCCESS) {
							broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
						} else {
							Log.i(TAG, "onServicesDiscovered received: " + status);
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
				});
			}
		}.start();
		return true;
	}

	private void onConnected() {
		mBLEConnection.onConnected();
	}

	private void onDisconnected(String reason) {
		mBLEConnection.onDisconnected(reason);
	}

	public void close() {
		if (bluetoothGatt == null) {
			return;
		}
		bluetoothGatt.close();
		bluetoothGatt = null;
		onDisconnected("Close");
	}

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		mContext.sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is carried out as per profile specifications.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			int flag = characteristic.getProperties();
			int format = -1;
			if (( flag & 0x01 ) != 0) {
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
				for (byte byteChar : data) {
					stringBuilder.append(String.format("%02X ", byteChar));
				}
				intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
						stringBuilder.toString());
			}
		}
		mContext.sendBroadcast(intent);
	}
}
