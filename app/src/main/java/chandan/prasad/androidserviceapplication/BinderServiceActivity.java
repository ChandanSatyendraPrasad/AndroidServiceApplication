package chandan.prasad.androidserviceapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class BinderServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText messageEditText;

    private BasicBinderService basicBinderService;
    private boolean isBound;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BasicBinderService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_service);
        messageEditText = (EditText) findViewById(R.id.fired_edit_text);
        Button fireMessageButton = (Button) findViewById(R.id.fire_button);
        fireMessageButton.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    //region View.OnClickListener
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fire_button) {
            if (isBound) {
                String message = messageEditText.getText().toString().trim();
                basicBinderService.fireMessage(message);
            }
        }
    }
    //endregion

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BasicBinderService.BasicBinder basicBinder = (BasicBinderService.BasicBinder) iBinder;
            basicBinderService = basicBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
}
