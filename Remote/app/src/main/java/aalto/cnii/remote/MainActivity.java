package aalto.cnii.remote;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ServerConnect server;
    private Boolean lightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText serverIP = (EditText) findViewById(R.id.ipText);
        serverIP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                serverIP.setCursorVisible(true);
                return false;
            }
        });
        serverIP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == 10203040) {
                    new Thread(new ConnectToServer()).start();
                    serverIP.setCursorVisible(false);
                }
                return false;
            }
        });

        final Button switchButton = (Button) findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lightOn)
                    new Thread(new TurnLightOff()).start();
                else
                    new Thread(new TurnLightOn()).start();
            }
        });
    }

    private class ConnectToServer implements Runnable {
        @Override
        public void run() {
            final EditText serverIP = (EditText) findViewById(R.id.ipText);
            try {
                server = new ServerConnect(serverIP.getText().toString(), 5000);
                runOnUiThread(new SetLogs("Connected to server!"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button switchButton = (Button) findViewById(R.id.switchButton);
                        switchButton.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new SetLogs(e.getMessage()));
            }
        }
    }

    private class TurnLightOn implements Runnable {
        @Override
        public void run() {
            try {
                String reply = server.sendOn();
                if (reply.equals("OK")) {
                    runOnUiThread(new SetLogs("Light is now on!"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Button switchButton = (Button) findViewById(R.id.switchButton);
                            switchButton.setText("Turn off light");
                        }
                    });
                    lightOn = true;
                } else {
                    runOnUiThread(new SetLogs("Did not change the light!"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new SetLogs(e.getMessage()));
            }
        }
    }

    private class TurnLightOff implements Runnable {
        @Override
        public void run() {
            try {
                server.sendOff();
                runOnUiThread(new SetLogs("Light is now off!"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button switchButton = (Button) findViewById(R.id.switchButton);
                        switchButton.setText("Turn on light");
                    }
                });
                lightOn = false;
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new SetLogs(e.getMessage()));
            }
        }
    }

    private class SetLogs implements Runnable {
        TextView logs = (TextView) findViewById(R.id.logsView);
        String message;

        public SetLogs(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            logs.append(message + "\n");
        }
    }
}
