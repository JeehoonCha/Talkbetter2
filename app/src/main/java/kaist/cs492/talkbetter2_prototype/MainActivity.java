package kaist.cs492.talkbetter2_prototype;

import android.os.Bundle;
import android.view.Menu;

import com.nclab.sociophone.SocioPhone;
import com.nclab.sociophone.interfaces.DisplayInterface;
import com.nclab.sociophone.interfaces.EventDataListener;
import com.nclab.sociophone.interfaces.TurnDataListener;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements TurnDataListener, DisplayInterface, EventDataListener, OnClickListener{
	SocioPhone mSocioPhone;
	Button connect, start;
	ToggleButton serverButton;
	EditText ipText;
	TextView debugText, turnText ,myIP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSocioPhone = new SocioPhone(this, this, this, this, false);
		mSocioPhone.setNetworkMode(true);
		mSocioPhone.setVolumeOrderMode(true);

		connect = (Button) findViewById(R.id.connectButton);
		start = (Button) findViewById(R.id.startButton);
		serverButton = (ToggleButton) findViewById(R.id.toggleButton1);
		ipText = (EditText) findViewById(R.id.ipText);
		debugText = (TextView) findViewById(R.id.debugText);
		turnText = (TextView) findViewById(R.id.turnText);
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ipAddressStr = String.format("%d.%d.%d.%d",(ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
		myIP = (TextView) findViewById(R.id.myiptext);
		myIP.setText(ipAddressStr);

		connect.setOnClickListener(this);
		start.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public void onDisplayMessageArrived(int arg0, String arg1) {
		// TODO Auto-generated method stub
		debugText.setText(arg1+"\r\n"+debugText.getText().toString());
		runOnUiThread(new Runnable() {
			public void run() {
				debugText.invalidate();
			}
		});
	}

	@Override
	public void onTurnDataReceived(int[] arg0) {
		if(arg0.length != 0) {
			turnText.setText(""+arg0[0]);
			debugText.setText("I am : "+mSocioPhone.getMyId()+" turn : " + arg0[0]+"\r\n"+debugText.getText().toString());

			runOnUiThread(new Runnable() {
				public void run() {
					debugText.invalidate();
					turnText.invalidate();
				}
			});
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.connectButton) {

			if(serverButton.isChecked()) {
				mSocioPhone.isServer = true;
				mSocioPhone.openServer();
			}
			else {
				mSocioPhone.connectToServer(ipText.getText().toString());

			}

		}
		if(view.getId() == R.id.startButton) {

			mSocioPhone.startRecord(0, "temp");

		}

	}

	@Override
	public void onInteractionEventOccured(int speakerID, int eventType,
			long timestamp) {
		// TODO Auto-generated method stub
	}
}