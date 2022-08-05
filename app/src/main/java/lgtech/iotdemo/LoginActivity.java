package lgtech.iotdemo;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etAppID;
    EditText etGatewayID;
    Button bGwsY;
    Button bGwsN;
    Button bJoin;
    Intent intent;
    boolean bClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //intent = new Intent(LoginActivity.this, MainActivity.class);
        intent = new Intent(this, MainActivity.class);


        etAppID = (EditText) findViewById(R.id.appIdInput);
        etGatewayID = (EditText) findViewById(R.id.gateWayInput);
        bGwsY = (Button) findViewById(R.id.ButtonYes);
        bGwsY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("GateWithSensor","YES");
                bGwsY.setBackgroundColor(Color.parseColor("#FFEB3B"));
                bGwsN.setBackgroundColor(Color.parseColor("#190C0A0A"));
                bClicked = true;
            }
        });
        bGwsN = (Button) findViewById(R.id.ButtonNo);
        bGwsN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("GateWithSensor","NO");
                bGwsN.setBackgroundColor(Color.parseColor("#FFEB3B"));
                bGwsY.setBackgroundColor(Color.parseColor("#190C0A0A"));
                bClicked = true;
            }
        });
        bJoin = (Button) findViewById(R.id.ButtonJoin);
        bJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAppID.getText().toString().equals("")) {
                    toast("App ID is Empty");
                }
                else if (etGatewayID.getText().toString().equals("")) {
                    toast("GatewayID ID is Empty");
                }
                else if (!bClicked) {
                    toast("please click YES or NO for gateway with sensor");
                }
                else {
                    intent.putExtra("AppID",etAppID.getText().toString());
                    intent.putExtra("GatewayID",etGatewayID.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    public void close() throws Throwable {
        LoginActivity.this.finalize();
        System.exit(0);
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}