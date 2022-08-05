package lgtech.iotdemo;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class GetFragment extends androidx.fragment.app.Fragment {

    View view;
    EditText etGD1, etGD2, etGD3, etGD4, etGD5, etGD6, etGD7, etGD8;
    EditText etGA1, etGA2, etGA3, etGA4, etGA5;
    EditText etGR1, etGR2;
    Button bGD1, bGD2, bGD3, bGD4, bGD5, bGD6, bGD7, bGD8;
    Button bGA1, bGA2, bGA3, bGA4, bGA5;
    Button bGR1, bGR2;

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
        bGD1.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD2.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD3.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD4.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD5.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD6.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD7.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGD8.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGA1.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGA2.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGA3.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGA4.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGA5.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGR1.setBackgroundColor(Color.parseColor("#190C0A0A"));
        bGR2.setBackgroundColor(Color.parseColor("#190C0A0A"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_get, container, false);

        sGateWithSensor = ((MainActivity)getActivity()).getsGateWithSensor();
        if (sGateWithSensor.equals("YES")) {
            sGateWithSensor = "1";
        } else {
            sGateWithSensor = "0";
        }

        etGD1 = (EditText) view.findViewById(R.id.textInputGD1);
        bGD1 = (Button) view.findViewById(R.id.buttonGD1);
        bGD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD1.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 1");
            }
        });

        etGD2 = (EditText) view.findViewById(R.id.textInputGD2);
        bGD2 = (Button) view.findViewById(R.id.buttonGD2);
        bGD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD2.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 2");
            }
        });

        etGD3 = (EditText) view.findViewById(R.id.textInputGD3);
        bGD3 = (Button) view.findViewById(R.id.buttonGD3);
        bGD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD3.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 3");
            }
        });

        etGD4 = (EditText) view.findViewById(R.id.textInputGD4);
        bGD4 = (Button) view.findViewById(R.id.buttonGD4);
        bGD4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD4.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 4");
            }
        });

        etGD5 = (EditText) view.findViewById(R.id.textInputGD5);
        bGD5 = (Button) view.findViewById(R.id.buttonGD5);
        bGD5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD5.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 5");
            }
        });

        etGD6 = (EditText) view.findViewById(R.id.textInputGD6);
        bGD6 = (Button) view.findViewById(R.id.buttonGD6);
        bGD6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD6.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 6");
            }
        });

        etGD7 = (EditText) view.findViewById(R.id.textInputGD7);
        bGD7 = (Button) view.findViewById(R.id.buttonGD7);
        bGD7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD7.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 7");
            }
        });

        etGD8 = (EditText) view.findViewById(R.id.textInputGD8);
        bGD8 = (Button) view.findViewById(R.id.buttonGD8);
        bGD8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGD8.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, DIGITAL, 8");
            }
        });

        etGA1 = (EditText) view.findViewById(R.id.textInputGA1);
        bGA1 = (Button) view.findViewById(R.id.buttonGA1);
        bGA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGA1.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, ANALOG, 1");
            }
        });

        etGA2 = (EditText) view.findViewById(R.id.textInputGA2);
        bGA2 = (Button) view.findViewById(R.id.buttonGA2);
        bGA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGA2.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, ANALOG, 2");
            }
        });

        etGA3 = (EditText) view.findViewById(R.id.textInputGA3);
        bGA3 = (Button) view.findViewById(R.id.buttonGA3);
        bGA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGA3.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, ANALOG, 3");
            }
        });

        etGA4 = (EditText) view.findViewById(R.id.textInputGA4);
        bGA4 = (Button) view.findViewById(R.id.buttonGA4);
        bGA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGA4.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, ANALOG, 4");
            }
        });

        etGA5 = (EditText) view.findViewById(R.id.textInputGA5);
        bGA5 = (Button) view.findViewById(R.id.buttonGA5);
        bGA5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGA5.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, ANALOG, 5");
            }
        });

        etGR1 = (EditText) view.findViewById(R.id.textInputGR1);
        bGR1 = (Button) view.findViewById(R.id.buttonGR1);
        bGR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGR1.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, RELAY, 1");
            }
        });

        etGR2 = (EditText) view.findViewById(R.id.textInputGR2);
        bGR2 = (Button) view.findViewById(R.id.buttonGR2);
        bGR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllColor();
                bGR2.setBackgroundColor(Color.parseColor("#FFEB3B"));
                ((MainActivity)getActivity()).send(sGateWithSensor+", READ, RELAY, 2");
            }
        });

        return view;
    }
}