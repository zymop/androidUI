package lgtech.iotdemo;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class AnalogAndRelayFragment extends androidx.fragment.app.Fragment {

    View view;
    EditText etSD1, etSD2, etSD3, etSD4, etSD5, etSD6, etSD7, etSD8;
    EditText etSR1, etSR2;
    Button bSD1, bSD2, bSD3, bSD4, bSD5, bSD6, bSD7, bSD8;
    Button bSR1, bSR2;

    String sGateWithSensor;

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }

                return false;
            }
        });
    }
    public void clearAllColor() {
        bSD1.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD2.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD3.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD4.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD5.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD6.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD7.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSD8.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSR1.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bSR2.setBackgroundColor(Color.parseColor("#190C0A0A"));
    }

    public boolean checkOneOrZero(String str) {
        if (str.equals("1") || str.equals("0")) return true;
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_analog_and_relay, container, false);

        sGateWithSensor = ((MainActivity)getActivity()).getsGateWithSensor();
        if (sGateWithSensor.equals("YES")) {
            sGateWithSensor = "1";
        } else {
            sGateWithSensor = "0";
        }

        etSD1 = (EditText) view.findViewById(R.id.textInputSD1);
        bSD1 = (Button) view.findViewById(R.id.buttonSD1);
        bSD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD1.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD1.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 1, "+etSD1.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }
            }
        });

        etSD2 = (EditText) view.findViewById(R.id.textInputSD2);
        bSD2 = (Button) view.findViewById(R.id.buttonSD2);
        bSD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD2.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD2.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 2, "+etSD2.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }
            }
        });

        etSD3 = (EditText) view.findViewById(R.id.textInputSD3);
        bSD3 = (Button) view.findViewById(R.id.buttonSD3);
        bSD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD3.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD3.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 3, "+etSD3.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }
            }
        });

        etSD4 = (EditText) view.findViewById(R.id.textInputSD4);
        bSD4 = (Button) view.findViewById(R.id.buttonSD4);
        bSD4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD4.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD4.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 4, "+etSD4.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }
            }
        });

        etSD5 = (EditText) view.findViewById(R.id.textInputSD5);
        bSD5 = (Button) view.findViewById(R.id.buttonSD5);
        bSD5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD5.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD5.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 5, "+etSD5.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }
            }
        });

        etSD6 = (EditText) view.findViewById(R.id.textInputSD6);
        bSD6 = (Button) view.findViewById(R.id.buttonSD6);
        bSD6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD6.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD6.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 6, "+etSD6.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }
            }
        });

        etSD7 = (EditText) view.findViewById(R.id.textInputSD7);
        bSD7 = (Button) view.findViewById(R.id.buttonSD7);
        bSD7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD7.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD7.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 7, "+etSD7.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }

            }
        });

        etSD8 = (EditText) view.findViewById(R.id.textInputSD8);
        bSD8 = (Button) view.findViewById(R.id.buttonSD8);
        bSD8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSD8.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSD8.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, DIGITAL, 8, "+etSD8.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }

            }
        });

        etSR1 = (EditText) view.findViewById(R.id.textInputSR1);
        bSR1 = (Button) view.findViewById(R.id.buttonSR1);
        bSR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSR1.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSR1.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, RELAY, 1, "+etSR1.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }

            }
        });

        etSR2 = (EditText) view.findViewById(R.id.textInputSR2);
        bSR2 = (Button) view.findViewById(R.id.buttonSR2);
        bSR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bSR2.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if (checkOneOrZero(etSR2.getText().toString())) {
                    ((MainActivity)getActivity()).send(sGateWithSensor+", WRITE, RELAY, 2, "+etSR2.getText().toString());
                }
                else {
                    ((MainActivity)getActivity()).shortToast("Invalid Input, Please enter 1 or 0");
                }

            }
        });
        return view;
    }
}