package lgtech.iotdemo;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DigitalFragment extends androidx.fragment.app.Fragment {

    View view;

    TextView tv1, tv2, tv3, tv4;
    EditText et1, et2_1, et2_2, et3, et4;
    Button b1;

    String sGateWithSensor;

    public static void buttonEffect(View button){
        button.setOnTouchListener(new OnTouchListener() {
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
    /*
    public class myTimerTask extends TimerTask {

        int count = 0;

        @Override
        public void run() {
            String number = String.valueOf(count % 2);
            ((MainActivity)getActivity()).send(sGateWithSensor+",WRITE,DIGITAL,3,"+number);
            count = (count+1) % 2;
            //Log.d("ppp+","yes");
        }
    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_digital, container, false);

        et1 = (EditText) view.findViewById(R.id.textInput1);
        b1 = (Button) view.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1.setText("");
            }
        });

        return view;
    }


    public void showDisconnection() {
        Toast toast = Toast.makeText(getActivity(), "Link is Down", Toast.LENGTH_LONG);
        toast.show();
        //Log.d("test","toast.show()");
    }
}