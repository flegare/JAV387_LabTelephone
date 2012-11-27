package com.mobidroid.labtel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class LabTelActivity extends Activity {

	private static final String TAG = LabTelActivity.class.getName();

	// retour de ligne
	private static final String LR = "\n\r";

	private TelephonyManager tm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		registerListeners();
		buildStateReport();

	}

	/**
	 * On cree un phone listner pour capturer les events...
	 */
	private void registerListeners() {

		tm.listen(new BigBrother(), PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_CELL_LOCATION
				| PhoneStateListener.LISTEN_DATA_ACTIVITY
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR
				| PhoneStateListener.LISTEN_SERVICE_STATE
				| PhoneStateListener.LISTEN_SIGNAL_STRENGTH
				| PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		findViewById(R.id.btCall).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 String url = "tel:15145551234";
				 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
				 startActivity(intent);
			}
		});
		


	}

	/**
	 * Construit une string qui contient les infos d'état du téléphone.
	 * 
	 * @return
	 */
	private String buildStateReport() {

		String report = "Phone report" + LR;
		report += "-----------------------" + LR;

		// Lecture de l'etat du tel
		report += readableCallState(tm.getCallState()) + LR;
		report += readableCellLoc(tm.getCellLocation()) + LR;
		report += readableDataActivity(tm.getDataActivity()) + LR;		
		report += "Device Id is: " + tm.getDeviceId() + LR;
		report += "Sofrware version is: " + tm.getDeviceSoftwareVersion() + LR;
		report += "Telephone number of the phone: " + tm.getLine1Number() + LR;
		report += "Network operator: " + tm.getNetworkOperator() + LR;
		report += "Network operator name: " + tm.getNetworkOperatorName() + LR;
		report += "Phone type is: " + readablePhoneType(tm.getPhoneType()) + LR;
		report += "Phone SIM serial: " + tm.getSimSerialNumber() + LR;
		report += "Phone subscriber Id: " + tm.getSubscriberId() + LR;
		

		((TextView) findViewById(R.id.reportTxt)).setText(report);

		return report;
	}

	/**
	 * @param phoneType
	 * @return
	 */
	private String readablePhoneType(int phoneType) {

		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.PHONE_TYPE_GSM:
			return "GSM";
		case TelephonyManager.PHONE_TYPE_NONE:
			return "NONE";
		default:
			return "UNKNOWN ("+phoneType+")";
		}
				
	}

	private String readableDataActivity(int dataActivity) {
		switch (dataActivity) {
		case TelephonyManager.DATA_ACTIVITY_DORMANT:
			return "Data activity is DORMANT";
		case TelephonyManager.DATA_ACTIVITY_IN:
			return "Data activity is IN";
		case TelephonyManager.DATA_ACTIVITY_INOUT:
			return "Data activity is INOUT";
		case TelephonyManager.DATA_ACTIVITY_NONE:
			return "Data activity is NONE";
		case TelephonyManager.DATA_ACTIVITY_OUT:
			return "Data activity is OUT";
		default:
			return "Data activity is UNKNOWN ("+dataActivity+")";
		}
	}
	

	private String readableNetworkType(int networkType) {
		
		switch(networkType){
			case TelephonyManager.NETWORK_TYPE_1xRTT: return "1xRTT";
			case TelephonyManager.NETWORK_TYPE_CDMA: return "CDMA";
			case TelephonyManager.NETWORK_TYPE_EDGE: return "EDGE";
			case TelephonyManager.NETWORK_TYPE_EVDO_0: return "EVDO_0";
			case TelephonyManager.NETWORK_TYPE_EVDO_A: return "EVDO_A";
			case TelephonyManager.NETWORK_TYPE_EVDO_B: return "EVDO_B";
			case TelephonyManager.NETWORK_TYPE_GPRS: return "GPRS";
			case TelephonyManager.NETWORK_TYPE_HSDPA: return "HSDPA";		
			case TelephonyManager.NETWORK_TYPE_HSUPA: return "HSUPA";
			case TelephonyManager.NETWORK_TYPE_IDEN: return "IDEN";
			case TelephonyManager.NETWORK_TYPE_UMTS: return "UMTS";		
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			default: 
				return "UNKNOWN ("+networkType+")";		
		}
	}
	
	private String readableServiceState(int state) {
		
		switch(state){
			case ServiceState.STATE_EMERGENCY_ONLY: return "EMERGENCY ONLY";
			case ServiceState.STATE_IN_SERVICE: return "IN SERVICE";	
			case ServiceState.STATE_POWER_OFF: return "POWER OFF";
			case ServiceState.STATE_OUT_OF_SERVICE: return "OUT OF SERVICE";
			default: return "UNKNOWN SERVICE STATE ("+state+")";
		}
		
	}
	
	private String readableConnectionState(int state) {		
		switch(state){
			case TelephonyManager.DATA_DISCONNECTED: return "DATA DISCONNECTED";
			case TelephonyManager.DATA_CONNECTING: return "DATA CONNECTING";
			case TelephonyManager.DATA_CONNECTED: return "DATA CONNECTED";
			case TelephonyManager.DATA_SUSPENDED: return "DATA SUSPENDED";
			default: return "DATA STATE UNKNOWN ("+state+")";
		}
		
	}

	/**
	 * @param cellLocation
	 * @return
	 */
	private String readableCellLoc(CellLocation cellLocation) {

		String msg = "Cell location:" + LR;

		// Peut être CDMA ou GSM on aurait pus utiliser tm.getPhoneType()
		if (cellLocation instanceof GsmCellLocation) {
			GsmCellLocation cellLoc = (GsmCellLocation) cellLocation;

			msg += "Cell ID :" + cellLoc.getCid() + LR;
			msg += "Cell area code :" + cellLoc.getLac() + LR;
			msg += "Scrambling cell code :" + cellLoc.getPsc() + LR;

		} else if (cellLocation instanceof CdmaCellLocation) {

			CdmaCellLocation cellLoc = (CdmaCellLocation) cellLocation;

			msg += "Base station id :" + cellLoc.getBaseStationId() + LR;
			msg += "Base station lat :" + cellLoc.getBaseStationLatitude() + LR;
			msg += "Base station long :" + cellLoc.getBaseStationLongitude()
					+ LR;
			msg += "Network id :" + cellLoc.getNetworkId() + LR;
			msg += "System id :" + cellLoc.getSystemId() + LR;

		}

		return msg;
	}

	private String readableCallState(int callState) {
		switch (callState) {
		case TelephonyManager.CALL_STATE_IDLE:
			return "Call state is IDLE";
		case TelephonyManager.CALL_STATE_OFFHOOK:
			return "Call state is OFFHOOK";
		case TelephonyManager.CALL_STATE_RINGING:
			return "Call state is RINGIGN";
		default:
			return "Call state is UNKNOWN";
		}
	}

	private void showToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT);
	}

	private class BigBrother extends PhoneStateListener {

		@Override
		public void onCallForwardingIndicatorChanged(boolean cfi) {
			super.onCallForwardingIndicatorChanged(cfi);
			showToast("Call fowarding state enabled: " + cfi);
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			Log.i(TAG, "call state changed for: " + readableCallState(state)
					+ " incoming number " + incomingNumber);
			buildStateReport(); 
		}

		@Override
		public void onCellLocationChanged(CellLocation location) {
			super.onCellLocationChanged(location);			
			Log.i(TAG, "onCellLocationChanged: " + readableCellLoc(location));					
			buildStateReport();		
		}

		@Override
		public void onDataActivity(int direction) {
			super.onDataActivity(direction);
			Log.i(TAG, "onDataActivity: " + readableDataActivity(direction));					
			buildStateReport();
		}

		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);		
			Log.i(TAG, "onDataConnectionStateChanged: " + readableConnectionState(state) + " on " + readableNetworkType(networkType));		
			buildStateReport();
		}

		@Override
		public void onDataConnectionStateChanged(int state) {
			super.onDataConnectionStateChanged(state);
			Log.i(TAG, "onDataConnectionStateChanged: " + readableConnectionState(state));					
			buildStateReport();
		}

		@Override
		public void onMessageWaitingIndicatorChanged(boolean mwi) {
			super.onMessageWaitingIndicatorChanged(mwi);
			Log.i(TAG, "onMessageWaitingIndicatorChanged, message waiting:" + mwi);					
			buildStateReport();
		}

		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			super.onServiceStateChanged(serviceState);
			Log.i(TAG, "onServiceStateChanged, service state:" + readableServiceState(serviceState.getState()));					
			buildStateReport();
		}

		@Override
		public void onSignalStrengthChanged(int asu) {
			super.onSignalStrengthChanged(asu);
			Log.i(TAG, "onSignalStrengthChanged, strenght:" + asu);					
			buildStateReport();
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			Log.i(TAG, "onSignalStrengthChanged, strenght:" + signalStrength.toString());					
			super.onSignalStrengthsChanged(signalStrength);
		}

	}

}