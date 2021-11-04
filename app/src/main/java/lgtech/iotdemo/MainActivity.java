package lgtech.iotdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;

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

    private boolean useWebSyncExtension = false;
    private boolean slientDevice = true;
    private Stream textStream;
    private Client client;
    private Conference conference;

    private ExecutorService background = Executors.newSingleThreadExecutor();

    private String m_content = "test";
    private String itemJson;

    public static final String BROADCAST_ACTION = "lgtech.adanywhere.service";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationManager locationManager=null;
    private LocationListener locationListener=null;
    public String cityName=null;

    Intent intent = new Intent(BROADCAST_ACTION);
    private final Handler handler = new Handler();

    private String longitude=null;
    private String latitude=null;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    private static boolean loginOK = false;
    private JSONObject jObj= null;
    private static boolean signallingStarted;

    private static fm.icelink.Conference Conference = null;
    private static fm.icelink.BaseLink baselink = null;

    private final static String TAG = MainActivity.class.getSimpleName();

    Switch sw1;
    Switch sw2;
    Switch sw3;
    Switch sw4;
    Switch sw5;
    Switch sw6;
    Switch sw7;
    Switch sw8;
    ToggleButton tgb1, tgb2;
    ToggleButton tgb3, tgb4;
    ToggleButton tgb5, tgb6;
    ToggleButton tgb7, tgb8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectServer("/iiot", 1);

        sw1 = (Switch) findViewById(R.id.switch1);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command1=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sw2 = (Switch) findViewById(R.id.switch2);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command2=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sw3 = (Switch) findViewById(R.id.switch3);
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command3=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sw4 = (Switch) findViewById(R.id.switch4);
        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command4=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        sw5 = (Switch) findViewById(R.id.switch5);
        sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command5=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        sw6 = (Switch) findViewById(R.id.switch6);
        sw6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command6=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        sw7 = (Switch) findViewById(R.id.switch7);
        sw7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command7=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        sw8 = (Switch) findViewById(R.id.switch8);
        sw8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "0";
                if(isChecked)
                    str = "1";
                try {
                    send("command8=" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tgb1 = (ToggleButton) findViewById(R.id.togglebutton1);
        tgb2 = (ToggleButton) findViewById(R.id.togglebutton2);
        tgb3 = (ToggleButton) findViewById(R.id.togglebutton3);
        tgb4 = (ToggleButton) findViewById(R.id.togglebutton4);
        tgb5 = (ToggleButton) findViewById(R.id.togglebutton5);
        tgb6 = (ToggleButton) findViewById(R.id.togglebutton6);
        tgb7 = (ToggleButton) findViewById(R.id.togglebutton7);
        tgb8 = (ToggleButton) findViewById(R.id.togglebutton8);


    }


    void send(String command)
    {
        // Get message from user input.
        final String message = command;

        // Write message to screen.

        background.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {

                    // Send message to the conference links.
                    if(useReliableChannels)
                    {
                        ConferenceExtensions.sendReliableString(conference, reliableDataChannelInfo, message);
                    }
                    else
                    {
                        ConferenceExtensions.sendData(conference, unreliableDataChannelInfo, message);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

    }

    private String getPeerName(BaseLinkArgs e)
    {
        @SuppressWarnings("unchecked")
        HashMap<String, Record> peerBindings = (useWebSyncExtension ? BaseLinkArgsExtensions.getPeerClient(e).getBoundRecords() : (HashMap<String, Record>)e.getPeerState());
        try
        {
//                String ip = Serializer.deserializeString(peerBindings.get("ip").getValueJson());
            String name = Serializer.deserializeString(peerBindings.get("name").getValueJson());
            return Serializer.deserializeString(peerBindings.get("name").getValueJson());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public void toast(String msg)
    {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

    public String getDeviceId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void scriptAlertBox(String title, String mymessage)
    {
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
        new Thread() {
            public void run()  {
                String disMsg;

                try {


                    if(useReliableChannels)
                    {
                        // Create a reliable WebRTC data channel description, including a
                        // handler for processing received messages.
                        reliableDataChannelInfo = new ReliableDataChannel(!orderedDeliveryForReliableChannels,"mydatachannel","chat")
                        {{
                            setOnReceive(new SingleAction<ReliableDataReceiveArgs>()
                            {
                                public void invoke(ReliableDataReceiveArgs e) {
                                    String msg = e.getDataString();
                                    if(!msg.equals("hello")) {
                                        String[] separated = msg.split("=");
                                        boolean checked = false;
                                        if (Integer.valueOf(separated[1]) == 1) {
                                            checked = true;
                                        }
                                        final boolean bCheck = checked;
                                        switch (separated[0]) {
                                            case "sensor1":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb1.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor2":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb2.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor3":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb3.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor4":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb4.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor5":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb5.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor6":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb6.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor7":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb7.setChecked(bCheck);
                                                    }
                                                });
                                                break;
                                            case "sensor8":
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        tgb8.setChecked(bCheck);
                                                    }
                                                });
                                                break;

                                        }
                                    }
                               }
                            });
                        }};

                        // Create a WebRTC data channel stream description using
                        // our data channel.
                        ReliableDataStream dataChannelStream = new ReliableDataStream(reliableDataChannelInfo);

                        // Create a conference using our stream description.
                        conference = new Conference(icelinkServerAddress, new Stream[] { dataChannelStream });
                        Conference = conference;
                        websyncConferenceChannelName=mychat;
                    }
                    else
                    {
                        // Create an unreliable  WebRTC data channel description, including a
                        // handler for processing received messages.
                        unreliableDataChannelInfo = new DataChannelInfo("mydatachannelChat")
                        {{
                            setOnReceive(new SingleAction<DataChannelReceiveArgs>()
                            {
                                public void invoke(DataChannelReceiveArgs e)
                                {
                                    //   writeLine("%s: %s", getPeerName(e), e.getData());
                                }
                            });
                        }};
                        // Create a WebRTC data channel stream description using
                        // our data channel.
                        DataChannelStream dataChannelStream = new DataChannelStream(unreliableDataChannelInfo);

                        // Create a conference using our stream description.
                        conference = new Conference(icelinkServerAddress, new Stream[] { dataChannelStream });
                        websyncConferenceChannelName="/reliablechat";
                    }
                    // Supply TURN relay credentials in case we are behind a
                    // highly restrictive firewall. These credentials will be
                    // verified by the TURN server.
                    conference.setRelayUsername("cloudkiller");
                    conference.setRelayPassword("Jackie01!");

                    // Add a few event handlers to the conference so we can see
                    // when a new P2P link is created or changes state.
                    conference.addOnLinkInit(new SingleAction<LinkInitArgs>()
                    {
                        public void invoke(LinkInitArgs e)
                        {
                            Log.d(TAG,"Link to " + getPeerName(e) + " initializing...");
                        }
                    });
                    conference.addOnLinkUp(new SingleAction<LinkUpArgs>()
                    {
                        public void invoke(LinkUpArgs e)
                        {
                            Log.d(TAG,"Link to " + getPeerName(e) + " is UP.");
                            Log.d(TAG,"Type a message and click/press Send.");
                        }
                    });
                    conference.addOnLinkDown(new SingleAction<LinkDownArgs>()
                    {
                        public void invoke(LinkDownArgs e)
                        {
                            Log.d(TAG,"Link to " + getPeerName(e) + " is DOWN. " + e.getException().getMessage());
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
                    client.connect(new ConnectArgs()
                    {{
                        setOnSuccess(new SingleAction<ConnectSuccessArgs>()
                        {
                            public void invoke(ConnectSuccessArgs e)
                            {
                                //writeLine("-- Connected to WebSync.");
                            }
                        });
                        setOnFailure(new SingleAction<ConnectFailureArgs>()
                        {
                            public void invoke(ConnectFailureArgs e)
                            {
                                //writeLine( e.getException().getMessage());

                                e.setRetry(false);
                            }
                        });
                        setOnStreamFailure(new SingleAction<StreamFailureArgs>()
                        {
                            public void invoke(StreamFailureArgs e)
                            {
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
                    if (useWebSyncExtension)
                    {
                        // Manage the conference automatically using a WebSync
                        // channel. P2P links will be created automatically to
                        // peers that join the same channel.
                        ClientExtensions.joinConference(client, new JoinConferenceArgs(websyncConferenceChannelName, conference)
                        {{
                            setOnSuccess(new SingleAction<JoinConferenceSuccessArgs>()
                            {
                                public void invoke(JoinConferenceSuccessArgs e)
                                {
                                }
                            });
                            setOnFailure(new SingleAction<JoinConferenceFailureArgs>()
                            {
                                public void invoke(JoinConferenceFailureArgs e)
                                {
                                }
                            });
                        }});
                    }
                    else
                    {
                        // If the WebSync stream goes down, destroy all P2P links.
                        // The WebSync client reconnect procedure will cause new
                        // P2P links to be created.
                        client.addOnStreamFailure(new SingleAction<StreamFailureArgs>()
                        {
                            public void invoke(StreamFailureArgs e)
                            {
                                try
                                {
                                    conference.unlinkAll();
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        // Add a couple event handlers to the conference to send
                        // generated offers/answers and candidates to a peer.
                        // The peer ID is something we define later. In this case,
                        // it represents the remote WebSync client ID. WebSync's
                        // "notify" method is used to send data to a specific client.
                        conference.addOnLinkOfferAnswer(new SingleAction<LinkOfferAnswerArgs>()
                        {
                            public void invoke(LinkOfferAnswerArgs e)
                            {
                                try
                                {
                                    client.notify(new NotifyArgs(new Guid(e.getPeerId()), e.getOfferAnswer().toJson(), "offeranswer"));
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        conference.addOnLinkCandidate(new SingleAction<LinkCandidateArgs>()
                        {
                            public void invoke(LinkCandidateArgs e)
                            {
                                try
                                {
                                    client.notify(new NotifyArgs(new Guid(e.getPeerId()), e.getCandidate().toJson(), "candidate"));
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        // Add an event handler to the WebSync client to receive
                        // incoming offers/answers and candidates from a peer.
                        // Call the "receiveOfferAnswer" or "receiveCandidate"
                        // method to pass the information to the conference.
                        client.addOnNotify(new SingleAction<NotifyReceiveArgs>()
                        {
                            public void invoke(NotifyReceiveArgs e)
                            {
                                try
                                {
                                    String peerId = e.getNotifyingClient().getClientId().toString();
                                    Object peerState = e.getNotifyingClient().getBoundRecords();
                                    if (e.getTag().equals("offeranswer"))
                                    {
                                        conference.receiveOfferAnswer(OfferAnswer.fromJson(e.getDataJson()), peerId, peerState);
                                    }
                                    else if (e.getTag().equals("candidate"))
                                    {
                                        conference.receiveCandidate(Candidate.fromJson(e.getDataJson()), peerId);
                                    }
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        // Subscribe to a WebSync channel. When another client joins the same
                        // channel, create a P2P link. When a client leaves, destroy it.
                        SubscribeArgs subscribeArgs = new SubscribeArgs(websyncConferenceChannelName)
                        {{
                            setOnSuccess(new SingleAction<SubscribeSuccessArgs>()
                            {
                                public void invoke(SubscribeSuccessArgs e)
                                {
                                }
                            });
                            setOnFailure(new SingleAction<SubscribeFailureArgs>()
                            {
                                public void invoke(SubscribeFailureArgs e)
                                {
                                    //                                       writeLine("-- Could not subscribe to %s. %s", e.getChannel(), e.getException().getMessage());
                                }
                            });
                            setOnReceive(new SingleAction<SubscribeReceiveArgs>()
                            {
                                public void invoke(SubscribeReceiveArgs e) { }
                            });
                        }};
                        SubscribeArgsExtensions.setOnClientSubscribe(subscribeArgs, new SingleAction<ClientSubscribeArgs>()
                        {
                            public void invoke(ClientSubscribeArgs e)
                            {
                                try
                                {
                                    String peerId = e.getSubscribedClient().getClientId().toString();
                                    Object peerState = e.getSubscribedClient().getBoundRecords();
                                    conference.link(peerId, peerState);
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        SubscribeArgsExtensions.setOnClientUnsubscribe(subscribeArgs, new SingleAction<ClientUnsubscribeArgs>()
                        {
                            public void invoke(ClientUnsubscribeArgs e)
                            {
                                try
                                {
                                    String peerId = e.getUnsubscribedClient().getClientId().toString();
                                    conference.unlink(peerId);
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        client.subscribe(subscribeArgs);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }.start();
    }





}
