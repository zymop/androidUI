package lgtech.iotdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fm.*;
import fm.icelink.*;
import fm.icelink.webrtc.ConferenceExtensions;
import fm.icelink.webrtc.DataChannelInfo;
import fm.icelink.webrtc.DataChannelReceiveArgs;
import fm.icelink.webrtc.DataChannelStream;
import fm.icelink.webrtc.ReliableDataChannel;
import fm.icelink.webrtc.ReliableDataReceiveArgs;
import fm.icelink.webrtc.ReliableDataStream;
import fm.icelink.websync.*;
import fm.websync.*;
import fm.websync.subscribers.*;


public class MainActivity extends AppCompatActivity {

    private String icelinkServerAddress = "67.43.170.11:4007";
    private String websyncServerUrl = "https://www.lgtech.mobi:3007/websync.ashx"; // WebSync On-Demand

    static boolean useReliableChannels = true; //set to true for reliable (SCTP-based) channels, set to false for unreliable (RTP-based) channels.
    static boolean orderedDeliveryForReliableChannels = true; //for reliable data channels, set this to true to guarantee ordered delivery of the
    //application messages within a given channel). This has no effect for the unreliable data channels.


    private DataChannelInfo unreliableDataChannelInfo;
    private ReliableDataChannel reliableDataChannelInfo;
    private String websyncConferenceChannelName;

    private boolean useWebSyncExtension = true;
    private boolean slientDevice = true;
    private Stream textStream;
    private Client client;
    private Conference conference;

    private ExecutorService background = Executors.newSingleThreadExecutor();

    private String m_content = "test";
    private String itemJson;

    public static final String BROADCAST_ACTION = "lgtech.adanywhere.service";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    public String cityName = null;

    Intent intent = new Intent(BROADCAST_ACTION);
    private final Handler handler = new Handler();

    private String longitude = null;
    private String latitude = null;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    private static boolean loginOK = false;
    private JSONObject jObj = null;
    private static boolean signallingStarted;

    private static fm.icelink.Conference Conference = null;
    private static fm.icelink.BaseLink baselink = null;

    private final static String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mMainNavi;
    private FrameLayout mMainFrame;

    private DigitalFragment digitalFragment;
    private AnalogAndRelayFragment analogAndRelayFragment;
    private GetFragment getFragment;

    EditText et1, et2_1, et2_2, et3, et4;
    EditText etSD1, etSD2, etSD3, etSD4, etSD5, etSD6, etSD7, etSD8;
    EditText etGD1, etGD2, etGD3, etGD4, etGD5, etGD6, etGD7, etGD8;
    EditText etGA1, etGA2, etGA3, etGA4, etGA5;
    EditText etSR1, etSR2;
    EditText etGR1, etGR2;
    String sAppID, sGatewayID, sGateWithSensor;

    private boolean receivedHeartBeat = false;
    private MainActivity parent = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        sAppID = intent.getStringExtra("AppID");
        sGatewayID = intent.getStringExtra("GatewayID");
        sGateWithSensor = intent.getStringExtra("GateWithSensor");

        //Log.d("pppp+ ", sAppID);
        //Log.d("pppp+ ", sGatewayID);
        //Log.d("pppp+ ", sGateWithSensor);

        connectServer("/iiotY", 1);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNavi = (BottomNavigationView) findViewById(R.id.main_navi);

        digitalFragment = new DigitalFragment();
        analogAndRelayFragment = new AnalogAndRelayFragment();
        getFragment = new GetFragment();


        setFragment(digitalFragment);
        //send("GateWithSensor, " + sGateWithSensor);

        mMainNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navi_digital:
                        setFragment(digitalFragment);
                        return true;
                    case R.id.navi_Analog:
                        setFragment(analogAndRelayFragment);
                        return true;
                    case R.id.navi_Get:
                        setFragment(getFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

        /*
        exitB = (Button) findViewById(R.id.ExitButton);
        exitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finishAffinity();
                //((LoginActivity)getActivity()).close();
                System.exit(0);
            }
        });

         */
    }

    private void setFragment(androidx.fragment.app.Fragment fragment) {
        androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    String getsGateWithSensor() {
        return sGateWithSensor;
    }

    void send(String command) {
        // Get message from user input.
        final String message = command;

        // Write message to screen.

        background.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (useReliableChannels) {
                        ConferenceExtensions.sendReliableString(conference, reliableDataChannelInfo, message);
                    } else {
                        ConferenceExtensions.sendData(conference, unreliableDataChannelInfo, message);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private String getPeerName(BaseLinkArgs e) {
        @SuppressWarnings("unchecked")
        HashMap<String, Record> peerBindings = (useWebSyncExtension ? BaseLinkArgsExtensions.getPeerClient(e).getBoundRecords() : (HashMap<String, Record>) e.getPeerState());
        try {
//                String ip = Serializer.deserializeString(peerBindings.get("ip").getValueJson());
            String name = Serializer.deserializeString(peerBindings.get("name").getValueJson());
            return Serializer.deserializeString(peerBindings.get("name").getValueJson());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void shortToast(String msg) {Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();}

    public String getDeviceId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void scriptAlertBox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mymessage)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void connectServer(final String chatRoomName, final int param) {
        final String mychat = chatRoomName;
        final String name = "name";
        final int myParam = param;
        loginOK = false;

        FMThread fmThread = new FMThread(mychat, name, parent);
        fmThread.start();
        //HeartBeatThread heartBeatThread = new HeartBeatThread();
        //heartBeatThread.start();

    }

    class FMThread extends Thread {
        String mychat, name;
        MainActivity parent;

        public FMThread(String mychat, String name, MainActivity parent) {
            this.mychat = mychat;
            this.name = name;
            this.parent = parent;
        }

        public void run() {
            String disMsg;
            try {
                if (useReliableChannels) {
                    // Create a reliable WebRTC data channel description, including a
                    // handler for processing received messages.
                    reliableDataChannelInfo = new ReliableDataChannel(!orderedDeliveryForReliableChannels, "mydatachannel", "chat") {{
                        setOnReceive(new SingleAction<ReliableDataReceiveArgs>() {
                            public void invoke(ReliableDataReceiveArgs e) {
                                String msg = e.getDataString();
                                if (msg.equals("I am up")) {
                                    receivedHeartBeat = true;
                                    //Log.d("test", "received: I am up");
                                    send("I am up");

                                } else {
                                    String[] separated = msg.split(",");
                                    //Log.d("pppp+",msg);
                                    et1 = (EditText) findViewById(R.id.textInput1);
                                    et2_1 = (EditText) findViewById(R.id.textInput2_1);
                                    et2_2 = (EditText) findViewById(R.id.textInput2_2);
                                    et3 = (EditText) findViewById(R.id.textInput3);
                                    et4 = (EditText) findViewById(R.id.textInput4);

                                    etSD1 = (EditText) findViewById(R.id.textInputSD1);
                                    etSD2 = (EditText) findViewById(R.id.textInputSD2);
                                    etSD3 = (EditText) findViewById(R.id.textInputSD3);
                                    etSD4 = (EditText) findViewById(R.id.textInputSD4);
                                    etSD5 = (EditText) findViewById(R.id.textInputSD5);
                                    etSD6 = (EditText) findViewById(R.id.textInputSD6);
                                    etSD7 = (EditText) findViewById(R.id.textInputSD7);
                                    etSD8 = (EditText) findViewById(R.id.textInputSD8);
                                    etSR1 = (EditText) findViewById(R.id.textInputSR1);
                                    etSR2 = (EditText) findViewById(R.id.textInputSR2);

                                    etGD1 = (EditText) findViewById(R.id.textInputGD1);
                                    etGD2 = (EditText) findViewById(R.id.textInputGD2);
                                    etGD3 = (EditText) findViewById(R.id.textInputGD3);
                                    etGD4 = (EditText) findViewById(R.id.textInputGD4);
                                    etGD5 = (EditText) findViewById(R.id.textInputGD5);
                                    etGD6 = (EditText) findViewById(R.id.textInputGD6);
                                    etGD7 = (EditText) findViewById(R.id.textInputGD7);
                                    etGD8 = (EditText) findViewById(R.id.textInputGD8);
                                    etGA1 = (EditText) findViewById(R.id.textInputGA1);
                                    etGA2 = (EditText) findViewById(R.id.textInputGA2);
                                    etGA3 = (EditText) findViewById(R.id.textInputGA3);
                                    etGA4 = (EditText) findViewById(R.id.textInputGA4);
                                    etGA5 = (EditText) findViewById(R.id.textInputGA5);
                                    etGR1 = (EditText) findViewById(R.id.textInputGR1);
                                    etGR2 = (EditText) findViewById(R.id.textInputGR2);
                                    if (separated[1].equals(" NOTIFY") && separated[2].equals(" PUSH BUTTON PRESSED"))
                                        et1.setText("PRESSED");
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" POTENTIOMETER")) {
                                        et2_1.setText(separated[3]);
                                        et2_2.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" RED LED"))
                                        et3.setText(separated[3]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" STEP MOTOR"))
                                        et4.setText(separated[3]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 1")) {
                                        etGD1.setText(separated[4]);
                                        etSD1.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 2")) {
                                        etSD2.setText(separated[4]);
                                        etGD2.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 3")) {
                                        etGD3.setText(separated[4]);
                                        etSD3.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 4")) {
                                        etGD4.setText(separated[4]);
                                        etSD4.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 5")) {
                                        etGD5.setText(separated[4]);
                                        etSD5.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 6")) {
                                        etGD6.setText(separated[4]);
                                        etSD6.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 7")) {
                                        etGD7.setText(separated[4]);
                                        etSD7.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" DIGITAL") && separated[3].equals(" 8")) {
                                        etGD8.setText(separated[4]);
                                        etSD8.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ANALOG") && separated[3].equals(" 1"))
                                        etGA1.setText(separated[4]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ANALOG") && separated[3].equals(" 2"))
                                        etGA2.setText(separated[4]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ANALOG") && separated[3].equals(" 3"))
                                        etGA3.setText(separated[4]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ANALOG") && separated[3].equals(" 4"))
                                        etGA4.setText(separated[4]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ANALOG") && separated[3].equals(" 5"))
                                        etGA5.setText(separated[4]);
                                    else if (separated[1].equals(" NOTIFY") && separated[2].equals(" RELAY") && separated[3].equals(" 1")) {
                                        etGR1.setText(separated[4]);
                                        etSR1.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" RELAY") && separated[3].equals(" 2")) {
                                        etGR2.setText(separated[4]);
                                        etSR2.setText(separated[4]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ALL_DIGITAL")) {
                                        etGD1.setText(separated[3]);
                                        etGD2.setText(separated[4]);
                                        etGD3.setText(separated[5]);
                                        etGD4.setText(separated[6]);
                                        etGD5.setText(separated[7]);
                                        etGD6.setText(separated[8]);
                                        etGD7.setText(separated[9]);
                                        etGD8.setText(separated[10]);

                                        etSD1.setText(separated[3]);
                                        etSD2.setText(separated[4]);
                                        etSD3.setText(separated[5]);
                                        etSD4.setText(separated[6]);
                                        etSD5.setText(separated[7]);
                                        etSD6.setText(separated[8]);
                                        etSD7.setText(separated[9]);
                                        etSD8.setText(separated[10]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ALL_ANALOG")) {
                                        etGA1.setText(separated[3]);
                                        etGA2.setText(separated[4]);
                                        etGA3.setText(separated[5]);
                                        etGA4.setText(separated[6]);
                                        etGA5.setText(separated[7]);
                                    } else if (separated[1].equals(" NOTIFY") && separated[2].equals(" ALL_RELAY")) {
                                        etGR1.setText(separated[3]);
                                        etGR2.setText(separated[4]);

                                        etSR1.setText(separated[3]);
                                        etSR2.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 1") && separated[5].equals(" 0")) {
                                        etSD1.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 2") && separated[5].equals(" 0")) {
                                        etSD2.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 3") && separated[5].equals(" 0")) {
                                        etSD3.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 4") && separated[5].equals(" 0")) {
                                        etSD4.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 5") && separated[5].equals(" 0")) {
                                        etSD5.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 6") && separated[5].equals(" 0")) {
                                        etSD6.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 7") && separated[5].equals(" 0")) {
                                        etSD7.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 8") && separated[5].equals(" 0")) {
                                        etSD8.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" RELAY")
                                            && separated[3].equals(" 1") && separated[5].equals(" 0")) {
                                        etSR1.setText(separated[4]);
                                    } else if (separated[1].equals(" WRITE") && separated[2].equals(" RELAY")
                                            && separated[3].equals(" 2") && separated[5].equals(" 0")) {
                                        etSR2.setText(separated[4]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 1") && separated[4].equals(" 0")) {
                                        etGD1.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 2") && separated[4].equals(" 0")) {
                                        etGD2.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 3") && separated[4].equals(" 0")) {
                                        etGD3.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 4") && separated[4].equals(" 0")) {
                                        etGD4.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 5") && separated[4].equals(" 0")) {
                                        etGD5.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 6") && separated[4].equals(" 0")) {
                                        etGD6.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 7") && separated[4].equals(" 0")) {
                                        etGD7.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" DIGITAL")
                                            && separated[3].equals(" 8") && separated[4].equals(" 0")) {
                                        etGD8.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" ANALOG")
                                            && separated[3].equals(" 1") && separated[4].equals(" 0")) {
                                        etGA1.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" ANALOG")
                                            && separated[3].equals(" 2") && separated[4].equals(" 0")) {
                                        etGA2.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" ANALOG")
                                            && separated[3].equals(" 3") && separated[4].equals(" 0")) {
                                        etGA3.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" ANALOG")
                                            && separated[3].equals(" 4") && separated[4].equals(" 0")) {
                                        etGA4.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" ANALOG")
                                            && separated[3].equals(" 5") && separated[4].equals(" 0")) {
                                        etGA5.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" RELAY")
                                            && separated[3].equals(" 1") && separated[4].equals(" 0")) {
                                        etGR1.setText(separated[5]);
                                    } else if (separated[1].equals(" READ") && separated[2].equals(" RELAY")
                                            && separated[3].equals(" 2") && separated[4].equals(" 0")) {
                                        etGR2.setText(separated[5]);
                                    }
                                }
                            }
                        });
                    }};

                    // Create a WebRTC data channel stream description using
                    // our data channel.
                    ReliableDataStream dataChannelStream = new ReliableDataStream(reliableDataChannelInfo);

                    // Create a conference using our stream description.
                    conference = new Conference(icelinkServerAddress, new Stream[]{dataChannelStream});
                    Conference = conference;
                    websyncConferenceChannelName = mychat;
                } else {
                    // Create an unreliable  WebRTC data channel description, including a
                    // handler for processing received messages.
                    unreliableDataChannelInfo = new DataChannelInfo("mydatachannelChat") {{
                        setOnReceive(new SingleAction<DataChannelReceiveArgs>() {
                            public void invoke(DataChannelReceiveArgs e) {
                                //   writeLine("%s: %s", getPeerName(e), e.getData());
                            }
                        });
                    }};
                    // Create a WebRTC data channel stream description using
                    // our data channel.
                    DataChannelStream dataChannelStream = new DataChannelStream(unreliableDataChannelInfo);

                    // Create a conference using our stream description.
                    conference = new Conference(icelinkServerAddress, new Stream[]{dataChannelStream});
                    websyncConferenceChannelName = "/reliablechat";
                }
                // Supply TURN relay credentials in case we are behind a
                // highly restrictive firewall. These credentials will be
                // verified by the TURN server.
                conference.setRelayUsername("cloudkiller");
                conference.setRelayPassword("Jackie01!");

                // Add a few event handlers to the conference so we can see
                // when a new P2P link is created or changes state.
                conference.addOnLinkInit(new SingleAction<LinkInitArgs>() {
                    public void invoke(LinkInitArgs e) {
                        //Log.d(TAG, "Link to " + getPeerName(e) + " initializing...");
                    }
                });
                conference.addOnLinkUp(new SingleAction<LinkUpArgs>() {
                    public void invoke(LinkUpArgs e) {
                        //Log.d(TAG, "Link to " + getPeerName(e) + " is UP.");
                        //Log.d(TAG,"Type a message and click/press Send.");
                        try {
                            ConferenceExtensions.sendReliableString(conference, reliableDataChannelInfo, "I am up");
                            //Log.d("test","send: I am up");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
                conference.addOnLinkDown(new SingleAction<LinkDownArgs>() {
                    public void invoke(LinkDownArgs e) {
                        //Log.d(TAG, "Link to " + getPeerName(e) + " is DOWN. " + e.getException().getMessage());
                        receivedHeartBeat = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(parent.getBaseContext(), "Link is Down", Toast.LENGTH_LONG).show();
                                //Log.d("test","what");

                            }
                        });
                        //Log.d("test","digitalFragment.showDisconnection");
                    }
                });

                // Before we can create a P2P link, the peers have to exchange
                // some information - specifically descriptions of the streams
                // (called the offer/answer) and some local network addresses
                // (called candidates). IceLink generates this information
                // automatically, but you are responsible for distributing it
                // to the peer as quickly as possible. This is called "signalling".

                // We're going to use WebSync here, but other popular options
                // include SIP and XMPP - any real-time messaging system will do.
                // We use WebSync since it uses HTTP (WebSockets/long-polling) and
                // therefore has no issues with firewalls or connecting from
                // JavaScript-based web applications.

                // Create a WebSync client and establish a persistent
                // connection to the server.
                client = new Client(websyncServerUrl);
                //client.setDomainKey(new Guid("5fb3bdc2-ea34-11dd-9b91-3e6b56d89593")); // WebSync On-Demand
                client.connect(new ConnectArgs() {{
                    setOnSuccess(new SingleAction<ConnectSuccessArgs>() {
                        public void invoke(ConnectSuccessArgs e) {
                            //writeLine("-- Connected to WebSync.");
                        }
                    });
                    setOnFailure(new SingleAction<ConnectFailureArgs>() {
                        public void invoke(ConnectFailureArgs e) {
                            //writeLine( e.getException().getMessage());

                            e.setRetry(false);
                        }
                    });
                    setOnStreamFailure(new SingleAction<StreamFailureArgs>() {
                        public void invoke(StreamFailureArgs e) {
                            //writeLine( e.getException().getMessage());
                        }
                    });
                }});

                // Bind the user-supplied name to the WebSync client. Later,
                // when linking, we will store a peer's bound records in the
                // IceLink peer state so we can get their name at any time.
                client.bind(new BindArgs("name", Serializer.serializeString(name)));

                // IceLink includes a WebSync client extension that will
                // automatically manage session negotiation for you. If
                // you are not using WebSync, see the 'else' block for a
                // session negotiation template.
                if (useWebSyncExtension) {
                    // Manage the conference automatically using a WebSync
                    // channel. P2P links will be created automatically to
                    // peers that join the same channel.
                    ClientExtensions.joinConference(client, new JoinConferenceArgs(websyncConferenceChannelName, conference) {{
                        setOnSuccess(new SingleAction<JoinConferenceSuccessArgs>() {
                            public void invoke(JoinConferenceSuccessArgs e) {
                            }
                        });
                        setOnFailure(new SingleAction<JoinConferenceFailureArgs>() {
                            public void invoke(JoinConferenceFailureArgs e) {
                            }
                        });
                    }});
                } else {
                    // If the WebSync stream goes down, destroy all P2P links.
                    // The WebSync client reconnect procedure will cause new
                    // P2P links to be created.
                    client.addOnStreamFailure(new SingleAction<StreamFailureArgs>() {
                        public void invoke(StreamFailureArgs e) {
                            try {
                                conference.unlinkAll();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Add a couple event handlers to the conference to send
                    // generated offers/answers and candidates to a peer.
                    // The peer ID is something we define later. In this case,
                    // it represents the remote WebSync client ID. WebSync's
                    // "notify" method is used to send data to a specific client.
                    conference.addOnLinkOfferAnswer(new SingleAction<LinkOfferAnswerArgs>() {
                        public void invoke(LinkOfferAnswerArgs e) {
                            try {
                                client.notify(new NotifyArgs(new Guid(e.getPeerId()), e.getOfferAnswer().toJson(), "offeranswer"));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    conference.addOnLinkCandidate(new SingleAction<LinkCandidateArgs>() {
                        public void invoke(LinkCandidateArgs e) {
                            try {
                                client.notify(new NotifyArgs(new Guid(e.getPeerId()), e.getCandidate().toJson(), "candidate"));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Add an event handler to the WebSync client to receive
                    // incoming offers/answers and candidates from a peer.
                    // Call the "receiveOfferAnswer" or "receiveCandidate"
                    // method to pass the information to the conference.
                    client.addOnNotify(new SingleAction<NotifyReceiveArgs>() {
                        public void invoke(NotifyReceiveArgs e) {
                            try {
                                String peerId = e.getNotifyingClient().getClientId().toString();
                                Object peerState = e.getNotifyingClient().getBoundRecords();
                                if (e.getTag().equals("offeranswer")) {
                                    conference.receiveOfferAnswer(OfferAnswer.fromJson(e.getDataJson()), peerId, peerState);
                                } else if (e.getTag().equals("candidate")) {
                                    conference.receiveCandidate(Candidate.fromJson(e.getDataJson()), peerId);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Subscribe to a WebSync channel. When another client joins the same
                    // channel, create a P2P link. When a client leaves, destroy it.
                    SubscribeArgs subscribeArgs = new SubscribeArgs(websyncConferenceChannelName) {{
                        setOnSuccess(new SingleAction<SubscribeSuccessArgs>() {
                            public void invoke(SubscribeSuccessArgs e) {
                            }
                        });
                        setOnFailure(new SingleAction<SubscribeFailureArgs>() {
                            public void invoke(SubscribeFailureArgs e) {
                                //                                       writeLine("-- Could not subscribe to %s. %s", e.getChannel(), e.getException().getMessage());
                            }
                        });
                        setOnReceive(new SingleAction<SubscribeReceiveArgs>() {
                            public void invoke(SubscribeReceiveArgs e) {
                            }
                        });
                    }};
                    SubscribeArgsExtensions.setOnClientSubscribe(subscribeArgs, new SingleAction<ClientSubscribeArgs>() {
                        public void invoke(ClientSubscribeArgs e) {
                            try {
                                String peerId = e.getSubscribedClient().getClientId().toString();
                                Object peerState = e.getSubscribedClient().getBoundRecords();
                                conference.link(peerId, peerState);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    SubscribeArgsExtensions.setOnClientUnsubscribe(subscribeArgs, new SingleAction<ClientUnsubscribeArgs>() {
                        public void invoke(ClientUnsubscribeArgs e) {
                            try {
                                String peerId = e.getUnsubscribedClient().getClientId().toString();
                                conference.unlink(peerId);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    client.subscribe(subscribeArgs);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
