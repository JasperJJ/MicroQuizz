package com.jasper.microquizz.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import io.reactivex.ObservableSource;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.UUID;

public class UartService {

	private Context mContext;

	private final UUID characteristicUUID =
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private final byte[] bytesToWrite = "hellow".getBytes();

	@SuppressLint("CheckResult")
	public UartService(Context context) {
		mContext = context;
		RxBleClient rxBleClient = RxBleClient.create(mContext);

		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		int REQUEST_ENABLE_BT = 1;
		( (Activity) mContext ).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

		String macAddress = "AA:BB:CC:DD:EE:FF";
		RxBleDevice device = rxBleClient.getBleDevice(macAddress);

		Disposable disposable = device.establishConnection(false)
		                              .subscribe(new Consumer<RxBleConnection>() {
			                                         @Override
			                                         public void accept(
					                                         RxBleConnection rxBleConnection)
					                                         throws Exception {
				                                         // All GATT operations are done through the rxBleConnection.
			                                         }
		                                         },
				                              new Consumer<Throwable>() {
					                              @Override
					                              public void accept(Throwable throwable)
							                              throws Exception {
						                              // Handle an error here.
					                              }
				                              }
		                              );

		// Change notifications
		device.establishConnection(false)
		      .flatMap(new Function<RxBleConnection, ObservableSource<?>>() {
			      @Override
			      public ObservableSource<?> apply(RxBleConnection rxBleConnection)
					      throws Exception {
				      return rxBleConnection.setupNotification(characteristicUUID);
			      }
		      })
		      .doOnNext(new Consumer<Object>() {
			      @Override
			      public void accept(Object notificationObservable) throws Exception {
				      // Notification has been set up
			      }
		      })
		      .flatMap(new Function<Object, ObservableSource<?>>() {
			      @Override
			      public ObservableSource<?> apply(Object notificationObservable) throws Exception {
				      return (ObservableSource<?>) notificationObservable;
			      }
		      }) // <-- Notification has been set up, now observe value changes.
		      .subscribe(
				      new Consumer<Object>() {
					      @Override
					      public void accept(Object bytes) throws Exception {
						      // Given characteristic has been changes, here is the value.
					      }
				      },
				      new Consumer<Throwable>() {
					      @Override
					      public void accept(Throwable throwable) throws Exception {
						      // Handle an error here.
					      }
				      }
		      );

		// Read
		device.establishConnection(false)
		      .flatMapSingle(new Function<RxBleConnection, SingleSource<?>>() {
			      @Override
			      public SingleSource<?> apply(RxBleConnection rxBleConnection) throws Exception {
				      return rxBleConnection.readCharacteristic(characteristicUUID);
			      }
		      })
		      .subscribe(
				      new Consumer<Object>() {
					      @Override
					      public void accept(Object characteristicValue) throws Exception {
						      // Read characteristic value.
					      }
				      },
				      new Consumer<Throwable>() {
					      @Override
					      public void accept(Throwable throwable) throws Exception {
						      // Handle an error here.
					      }
				      }
		      );

		// Write
		device.establishConnection(false)
		      .flatMapSingle(new Function<RxBleConnection, SingleSource<?>>() {
			      @Override
			      public SingleSource<?> apply(RxBleConnection rxBleConnection) throws Exception {
				      return rxBleConnection.writeCharacteristic(characteristicUUID, bytesToWrite);
			      }
		      })
		      .subscribe(
				      new Consumer<Object>() {
					      @Override
					      public void accept(Object characteristicValue) throws Exception {
						      // Characteristic value confirmed.
					      }
				      },
				      new Consumer<Throwable>() {
					      @Override
					      public void accept(Throwable throwable) throws Exception {
						      // Handle an error here.
					      }
				      }
		      );
		// When done... dispose and forget about connection teardown :)
		disposable.dispose();
	}
}
