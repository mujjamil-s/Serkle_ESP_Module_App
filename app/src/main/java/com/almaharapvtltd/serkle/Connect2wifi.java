 package com.almaharapvtltd.serkle;

 import android.content.BroadcastReceiver;
 import android.content.Context;
 import android.content.Intent;
 import android.content.IntentFilter;
 import android.net.ConnectivityManager;
 import android.net.DhcpInfo;
 import android.net.wifi.ScanResult;
 import android.net.wifi.WifiConfiguration;
 import android.net.wifi.WifiManager;
 import android.os.Bundle;
 import android.os.StrictMode;
 import android.util.Log;
 import android.view.View;
 import android.widget.Adapter;
 import android.widget.ArrayAdapter;
 import android.widget.Button;
 import android.widget.ListView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

 import com.google.android.material.textfield.TextInputEditText;
 import com.tinder.scarlet.MessageAdapter;
 import com.tinder.scarlet.Scarlet;
 import com.tinder.scarlet.Stream;
 import com.tinder.scarlet.WebSocket;
 import com.tinder.scarlet.lifecycle.android.AndroidLifecycle;
 import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter;
 import com.tinder.scarlet.retry.BackoffStrategy;
 import com.tinder.scarlet.retry.ExponentialBackoffStrategy;
 import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory;
 import com.tinder.scarlet.websocket.okhttp.OkHttpClientUtils;
 import com.tinder.scarlet.websocket.okhttp.OkHttpClientWebSocketConnectionEstablisher;
 import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket;
 import com.tinder.scarlet.websocket.okhttp.request.RequestFactory;

 import java.net.InetAddress;
 import java.net.UnknownHostException;
 import java.util.ArrayList;
 import java.util.List;

 import okhttp3.OkHttpClient;
 import okhttp3.Request;

 public class Connect2wifi extends AppCompatActivity {
     private static final long RETRY_BASE_DURATION = 1000 ;
     private static final long RETRY_MAX_DURATION = 10000;
     WifiManager wifiManager;
    ConnectivityManager connectivityManager;
    ListView wifiList;
   Button scanButton, connectButton, turnOn,turnOff, colorChooser, openHome, sendSSID;

   TextInputEditText ssid, pass;
    ArrayAdapter adapter;
    List<ScanResult> results;
    ArrayList<String> arrayList = new ArrayList<>();
    int size = 0;
    int netid;
    int ipAddress;
    String apIpAddress,
    webSocketClient = "ws://192.168.4.1/ws";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect2wifi);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        wifiList = findViewById(R.id.wifiList);
        scanButton = findViewById(R.id.scanBtn);
        openHome = findViewById(R.id.open_home);

        //ESP Connection
        Connect2Esp();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
            }
        });
        connectButton = findViewById(R.id.CntBtn);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect2Led();
            }
        });
        turnOn = findViewById(R.id.trnon);
        turnOff = findViewById(R.id.trnoff);
        ssid = findViewById(R.id.ssid_text);
        pass = findViewById(R.id.password_text);
        sendSSID = findViewById(R.id.send_ssid_pass);

        StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sendSSID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = "@"+ ssid.getText().toString() + ";" + pass.getText().toString() ;
                client.sendText(ss);
            }
        });

        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendValue('R');
            }
        });

        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendValue('N');
            }
        });
        colorChooser = findViewById(R.id.color_chooser);
        colorChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooser = new Intent(Connect2wifi.this,ColorChooserActivity.class);
                startActivity(chooser);
            }
        });

        openHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooser = new Intent(Connect2wifi.this,HomeActivity.class);
                startActivity(chooser);
            }
        });
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        adapter = new ArrayAdapter(this,R.layout.row,arrayList);
        wifiList.setAdapter(adapter);
        scanWifi();
    }

    private void scanWifi(){
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this,"Scanning WIFI....", Toast.LENGTH_SHORT).show();
        Log.d("Array list", "onReceive: arrayList Count" + arrayList.size() + wifiManager.getScanResults());
    }
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            wifiManager.getConnectionInfo();

            unregisterReceiver(this);

            for(ScanResult scanResult :results){
                arrayList.add((scanResult.SSID + "..." + scanResult.capabilities));
                Log.d("Array list", "onReceive: arrayList Count" + arrayList.size());
                adapter.notifyDataSetChanged();
            }
        }
    };
    public void Connect2Led() {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"ESP8266 Access Point\"";
        config.preSharedKey = "\"123456789\"";
        config.hiddenSSID = true;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
       netid = wifiManager.addNetwork(config);
       if(netid == -1){
           Toast.makeText(this, "Failed to connect to Led", Toast.LENGTH_LONG).show();
       } else{
           Toast.makeText(this, "Connected to connect to Led " + netid, Toast.LENGTH_LONG).show();
       }
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        wifiManager.enableNetwork(netid, true);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration i : list){
            if(i.SSID != null && i.SSID.equals("\""+"WIFI LED"+"\"")){
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                wifiManager.saveConfiguration();

                break;
            }
        }
        ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        Log.d("Dev Ip addr", "onReceive: " + ipAddress);
        apIpAddress = getApIpAddr(getApplicationContext());

        try {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            Log.d("Dev Ip addr111", "onReceive: " + localIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
    public static String getApIpAddr(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo =wifiManager.getDhcpInfo();
        final  int ip = dhcpInfo.ipAddress;
        Log.d("Ip Address byte00", "getApIpAddr: " + ((0xFF& ip)+"."+(0xFF &ip >>8)+"."+(0xFF & ip>>16)+"."+(0xFF & ip>>24)));
        byte[] ipAddess = convert2Bytes(dhcpInfo.serverAddress);
        Log.d("Ip Address of host byte", "getApIpAddr: " + ipAddess);
        try{
            String apIpAddr = InetAddress.getByAddress(ipAddess).getHostAddress();
            Log.d("Ip Address of host", "getApIpAddr: " + apIpAddr);
            return apIpAddr;
        } catch(UnknownHostException e){
            e.printStackTrace();
        }
        return null;
    }

     private static byte[] convert2Bytes(int hostAddress) {
        byte[] addressBytes = {(byte)(0xff & hostAddress),
                (byte)(0xff & (hostAddress >> 8)),
                (byte)(0xff & (hostAddress>>16)),
                (byte)(0xff & (hostAddress>> 24))
        };
        return  addressBytes;
      }


     BackoffStrategy BACKOFF_STRATEGY = new ExponentialBackoffStrategy(RETRY_BASE_DURATION,RETRY_MAX_DURATION);
     TextCommunication client;
   OkHttpWebSocket  protocol;

     public static long getRetryBaseDuration() {
         return RETRY_BASE_DURATION;
     }

     public void Connect2Esp() {
                 client =new Scarlet.Builder().webSocketFactory(OkHttpClientUtils.newWebSocketFactory(new OkHttpClient(),webSocketClient))
                 .addMessageAdapterFactory(new MoshiMessageAdapter.Factory()).addStreamAdapterFactory(new RxJava2StreamAdapterFactory())
                 .backoffStrategy(BACKOFF_STRATEGY).lifecycle(AndroidLifecycle.ofApplicationForeground(getApplication())).build().create(TextCommunication.class);

     }
     public void sendValue(char value) {
         client.sendText(value);
    }

    public Stream<String> receiveValue(){
        return client.receiveText();
    }


 }
