package com.example.tictactoe;

import java.util.Set;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private static final int REQUEST_ENABLE_BT = 2;
	// score initialized to 0.
	static int player=1;
	 private ArrayAdapter<String> mArrayAdapter;
	 private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	    private ArrayAdapter<String> mNewDevicesArrayAdapter;
	int a[][] = new int[3][3];
	int turn =1;
	private BluetoothChatService mChatService = null;
	private StringBuffer mOutStringBuffer;
	private static final String TAG = "TicTacToe";
	private TextView mTitle;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	int img = R.drawable.cross;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item);
        /*
        // Initialize the button to perform device discovery
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });
        
        
     // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        
     // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        
     // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        */
        
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        	Toast.makeText(MainActivity.this, "Device does not support Bluetooth.", Toast.LENGTH_SHORT).show();                    	
        	return;
        }
        Log.d(TAG,"before enable");
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Log.d(TAG,"start enable");
        }
        setupGame();
        
        /*Intent data=null;
        int resultCode=0;
        int requestCode=1;
       // onActivityResult(requestCode,resultCode,data);
        
    	if(resultCode != RESULT_OK)
    	{
    		Toast.makeText(MainActivity.this, "Device does not support Bluetooth.", Toast.LENGTH_SHORT).show();                    	
    		return;
    	}
        */
        /*
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    	// If there are paired devices
    	if (pairedDevices.size() > 0) {
    	    // Loop through paired devices
    	    for (BluetoothDevice device : pairedDevices) {
    	        // Add the name and address to an array adapter to show in a ListView
    	        //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
    	        Toast.makeText(MainActivity.this, device.getName() + " - " + device.getAddress(), Toast.LENGTH_SHORT).show();  
    	    }
    	}
    	*/
    	// Register the BroadcastReceiver
    	//IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	//registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    	
        
    	/*Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    	discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
   		startActivity(discoverableIntent);
    	*/		
    			
        
        for(int i=0;i<3;i++)
        {
        	for(int j=0;j<3;j++)
        		a[i][j]=-1;
        }
        findViewById(R.id.button1).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button2).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button3).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button4).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button5).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button6).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button7).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button8).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button9).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button10).setOnClickListener(mGlobal_OnClickListener);
        findViewById(R.id.button11).setOnClickListener(mGlobal_OnClickListener);
    }
    
    final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		public void onClick(final View v) {
        	if(v.getId() == R.id.button10)
        	{
        		Log.d(TAG, "Play again...");
        		turn = 1;
        		player = 1;
            	for(int i=0;i<3;i++)
            	{
            		for(int j=0;j<3;j++)
            		{
            			a[i][j]=-1;
            		}
            	}
            	findViewById(R.id.button1).setEnabled(true);
                findViewById(R.id.button2).setEnabled(true);
                findViewById(R.id.button3).setEnabled(true);
                findViewById(R.id.button4).setEnabled(true);
                findViewById(R.id.button5).setEnabled(true);
                findViewById(R.id.button6).setEnabled(true);
                findViewById(R.id.button7).setEnabled(true);
                findViewById(R.id.button8).setEnabled(true);
                findViewById(R.id.button9).setEnabled(true);
                
            	//Remove background image
            	findViewById(R.id.button1).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button2).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button3).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button4).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button5).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button6).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button7).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button8).setBackgroundResource(android.R.drawable.btn_default);
                findViewById(R.id.button9).setBackgroundResource(android.R.drawable.btn_default);
            	return;
        	}
        	Log.d(TAG, "turn: " + turn + " player: " + player);
        	if(turn == player)
        	{
        		if(player == 1)
        		{
        			img = R.drawable.cross;
        		}
        		else
        		{
        			img = R.drawable.dot;
        		}
        	}
        	else
        	{
        		Toast.makeText(MainActivity.this, "Wait for your turn.", Toast.LENGTH_SHORT).show();
        		return;
        	}
            switch(v.getId()) {
                case R.id.button1:
                	
                	if(a[0][0]!=-1)
                	{
                		break;
                	}
                	a[0][0]=player;
                	
                	Log.d(TAG, "image: " + img);
                    //Toast.makeText(MainActivity.this, "Button1 clicked.", Toast.LENGTH_SHORT).show();                
                	findViewById(R.id.button1).setBackgroundResource(img);
                	String s= player + "1";
                	mChatService.write(s.getBytes());
                    checkResult();
                	
                    break;
                case R.id.button2:
                	if(a[0][1]!=-1)
                	{
                		break;
                	}
                	a[0][1]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button2).setBackgroundResource(img);
                	s= player + "2";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button2 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button3:
                	if(a[0][2]!=-1)
                	{
                		break;
                	}
                	
                	a[0][2]=player;
                	Log.d(TAG, "image: " + img);
                	
                	findViewById(R.id.button3).setBackgroundResource(img);
                	s= player + "3";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button3 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button4:
                	if(a[1][0]!=-1)
                	{
                		break;
                	}
                	
                	a[1][0]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button4).setBackgroundResource(img);
                	s=player + "4";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button4 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button5:
                	if(a[1][1]!=-1)
                	{
                		break;
                	}
                	
                	a[1][1]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button5).setBackgroundResource(img);
                	s= player + "5";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button5 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button6:
                	if(a[1][2]!=-1)
                	{
                		break;
                	}
                	
                	a[1][2]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button6).setBackgroundResource(img);
                	s= player + "6";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button6 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button7:
                	if(a[2][0]!=-1)
                	{
                		break;
                	}
                	
                	a[2][0]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button7).setBackgroundResource(img);
                	s= player + "7";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button7 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button8:
                	if(a[2][1]!=-1)
                	{
                		break;
                	}
                	
                	a[2][1]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button8).setBackgroundResource(img);
                	s= player + "8";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button8 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button9:
                	if(a[2][2]!=-1)
                	{
                		break;
                	}
                	
                	a[2][2]=player;
                	Log.d(TAG, "image: " + img);
                	findViewById(R.id.button9).setBackgroundResource(img);
                	s= player + "9";
                	mChatService.write(s.getBytes());
                	checkResult();
                	//Toast.makeText(MainActivity.this, "Button9 clicked.", Toast.LENGTH_SHORT).show();                
                break;
                case R.id.button11:
	                Log.d("TicTacToe","Scanning");
	                // Launch the DeviceListActivity to see devices and do scan
	                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
	                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                	break;
            }
        }
    };
    
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }
    
    private void setupGame() {
        Log.d(TAG, "setupGame()");

        // Initialize the BluetoothGameService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    //mTitle.setText("Connected to");
                    //mTitle.append(mConnectedDeviceName);
                    Log.d(TAG,"Connected to "+ mConnectedDeviceName);
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    //mTitle.setText("Connecting");
                	Log.d(TAG,"Connecting");
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    //mTitle.setText("Not Connected");
                    Log.d(TAG,"Not Connected");
                    break;
                }
                break;
            case MESSAGE_WRITE:
            	Log.d(TAG, "Write Turn: " + turn + " player: " +player);
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Log.d(TAG,"Writing Message: " + writeMessage +" image: " + img);
                turn = (turn==1)?2:1;
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
            	if(turn == 1)
        		{
        			img = R.drawable.cross;
        		}
        		else
        		{
        			img = R.drawable.dot;
        		}
            	Log.d(TAG, "Read Turn: " + turn + " player: " +player);
            	Log.d(TAG,"Readinggg");
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                int number = Integer.parseInt(readMessage);
                player = number / 10;
                
                turn = (turn==1)?2:1;
                int button = number % 10;
                String btn = String.valueOf(button);
                Log.d(TAG,"Read a[][]: " + a[(button-1)/3][(button-1)%3]);
                Log.d(TAG,"Reading Message: " + readMessage);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                if(btn.equals("1"))
                {
                	a[0][0]=player;
                	Log.d(TAG,"image1: " + img);
                	findViewById(R.id.button1).setBackgroundResource(img);
                }
                else if(btn.equals("2"))
                {
                	a[0][1]=player;
                	Log.d(TAG,"image2: " + img);
                	findViewById(R.id.button2).setBackgroundResource(img);
                }
                else if(btn.equals("3"))
                {
                	a[0][2]=player;
                	Log.d(TAG,"image3: " + img);
                	findViewById(R.id.button3).setBackgroundResource(img);
                }
                else if(btn.equals("4"))
                {
                	a[1][0]=player;
                	Log.d(TAG,"image4: " + img);
                	findViewById(R.id.button4).setBackgroundResource(img);
                }
                else if(btn.equals("5"))
                {
                	a[1][1]=player;
                	Log.d(TAG,"image5: " + img);
                	findViewById(R.id.button5).setBackgroundResource(img);
                }
                else if(btn.equals("6"))
                {
                	a[1][2]=player;
                	Log.d(TAG,"image6: "+img);
                	findViewById(R.id.button6).setBackgroundResource(img);
                }
                else if(btn.equals("7"))
                {
                	a[2][0]=player;
                	Log.d(TAG,"image7: "+img);
                	findViewById(R.id.button7).setBackgroundResource(img);
                }
                else if(btn.equals("8"))
                {
                	a[2][1]=player;
                	Log.d(TAG,"image8: "+img);
                	findViewById(R.id.button8).setBackgroundResource(img);
                }
                else if(btn.equals("9"))
                {
                	a[2][2]=player;
                	Log.d(TAG,"image9: "+img);
                	findViewById(R.id.button9).setBackgroundResource(img);
                }
                player = (player==1)?2:1;
                checkResult();
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
        	
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
            	
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                
                Log.d(TAG, "connect device "+ device+" "+ address);
                // Attempt to connect to the device
                mChatService.connect(device,false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupGame();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, "BT not enabled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    
    public void checkResult()
    {
    	Log.d(TAG, "Checking Result");
    	if(check())
    	{
    		Log.d(TAG, "Game over....");
    		findViewById(R.id.button1).setEnabled(false);
            findViewById(R.id.button2).setEnabled(false);
            findViewById(R.id.button3).setEnabled(false);
            findViewById(R.id.button4).setEnabled(false);
            findViewById(R.id.button5).setEnabled(false);
            findViewById(R.id.button6).setEnabled(false);
            findViewById(R.id.button7).setEnabled(false);
            findViewById(R.id.button8).setEnabled(false);
            findViewById(R.id.button9).setEnabled(false);
            findViewById(R.id.button10).setVisibility(View.VISIBLE);
    	}
    }
    
   

    public boolean check()
    {
    	int count1 = 0;
    	int count2 = 0;
    	int count3,count4,count5=0,count6=0,count7=0,count8=0;
    	for(int i=0;i<3;i++)
    	{
    		for(int j=0;j<3;j++)
    		{
    			Log.d(TAG, "" + i + " "+ j + " "+ a[i][j]);
    		}
    	}
    	for(int i=0;i<3;i++)
    	{
    		count1=0;count2=0;count3=0;count4=0;
    		for(int j=0;j<3;j++)
    		{
    			if(a[i][j]==1)
    			{
    				count1++;
    			}
    			else if(a[i][j]==2)
    			{
    				count2++;
    			}
    			
    			if(a[j][i]==1)
    			{
    				count3++;
    			}
    			else if(a[j][i]==2)
    			{
    				count4++;
    			}
    			if(i+j==2)
    			{
    				if(a[i][j]==1)
    				{
    					count7++;
    				}
    				else if(a[i][j]==2)
    				{
    					count8++;
    				}
    			}
    		}
    		if(count1==3)
    		{
    			Toast.makeText(MainActivity.this, "Player 1 win", Toast.LENGTH_SHORT).show();                
    			findViewById(R.id.button10).setVisibility(1);
    			return true;
    		}
    		else if(count2==3)
    		{
    			Toast.makeText(MainActivity.this, "Player 2 win", Toast.LENGTH_SHORT).show(); 
    			findViewById(R.id.button10).setVisibility(1);
    			return true;
    		}
    		if(count3==3)
    		{
    			Toast.makeText(MainActivity.this, "Player 1 win", Toast.LENGTH_SHORT).show(); 
    			findViewById(R.id.button10).setVisibility(1);
    			return true;
    		}
    		else if(count4==3)
    		{
    			Toast.makeText(MainActivity.this, "Player 2 win", Toast.LENGTH_SHORT).show(); 
    			findViewById(R.id.button10).setVisibility(1);
    			return true;
    		}
    		
    		if(a[i][i]==1)
    		{
    			count5++;
    		}
    		else if(a[i][i]==2)
    		{
    			count6++;
    		}
    		if(count5==3)
    		{
    			Toast.makeText(MainActivity.this, "Player 1 win", Toast.LENGTH_SHORT).show();
    			findViewById(R.id.button10).setVisibility(1);
    			return true;
    		}
    		else if(count6==3)
    		{
    			Toast.makeText(MainActivity.this, "Player 2 win", Toast.LENGTH_SHORT).show(); 
    			findViewById(R.id.button10).setVisibility(1);
    			return true;
    		}
    	}
    	if(count7==3)
		{
			Toast.makeText(MainActivity.this, "Player 1 win", Toast.LENGTH_SHORT).show(); 
			findViewById(R.id.button10).setVisibility(1);
			return true;
		}
		else if(count8==3)
		{
			Toast.makeText(MainActivity.this, "Player 2 win", Toast.LENGTH_SHORT).show(); 
			findViewById(R.id.button10).setVisibility(1);
			return true;
		}
    	return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void ensureDiscoverable() {
        Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        
    	switch (item.getItemId()) {
        case R.id.scan:
        	Log.d("TicTacToe","Scanning");
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        case R.id.action_settings:
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
