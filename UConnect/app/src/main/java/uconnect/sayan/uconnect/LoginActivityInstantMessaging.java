package uconnect.sayan.uconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sinch.android.rtc.SinchError;

public class LoginActivityInstantMessaging extends BaseActivityInstantMessaging implements SinchServiceInstantChat.StartFailedListener {

    private Button mLoginButtonIM;

    private EditText mLoginNameIM;
    private ProgressDialog mSpinnerIM;
    public TextView mtroubleTextViewIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_instant_messaging);

        mtroubleTextViewIM = (TextView)findViewById(R.id.troubleTextViewIM);
        mLoginButtonIM = (Button) findViewById(R.id.loginButtonIM);
        mLoginButtonIM.setEnabled(false);
        mLoginNameIM = (EditText) findViewById(R.id.loginNameIM);

        mLoginButtonIM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClickedIM(v);
            }
        });
        mtroubleTextViewIM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                troubleshootClicked(view);
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        mLoginButtonIM.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinnerIM != null) {
            mSpinnerIM.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        //Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        new DisplayToast().showToast(this,error.toString());
        if (mSpinnerIM != null) {
            mSpinnerIM.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openMessagingActivity();
    }

    private void loginClickedIM(View view) {
        String userName = mLoginNameIM.getText().toString();

        if (userName.isEmpty()) {
            //Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            //new DisplayToast().showToast(this,"Please enter a name");
            Snackbar snackbar = Snackbar
                    .make(view, "Please enter a name", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            snackbar.getView().setBackgroundColor(Color.parseColor("#7F049E"));
            snackbar.show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openMessagingActivity();
        }
    }

    public void troubleshootClicked(View view){
        StringBuffer buffer = new StringBuffer();
        buffer.append(getApplication().getResources().getString(R.string.TroubleShoot));
        ContextThemeWrapper ctw = new ContextThemeWrapper(view.getContext(), R.style.Theme_Show_Dialog_Alert);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctw);
        //final AlertDialog dialog = alertDialog.create();
        alertDialog.setTitle("Your Guide is here");
        alertDialog.setMessage(buffer);
        alertDialog.setIcon(R.drawable.mainapplogo);
        alertDialog.show();
        /*alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });*/
    }

    private void openMessagingActivity() {
        Intent messagingActivity = new Intent(this, MessagingActivity.class);
        startActivity(messagingActivity);
    }

    private void showSpinner() {
        mSpinnerIM = new ProgressDialog(this);
        mSpinnerIM.setTitle("Logging in");
        mSpinnerIM.setMessage("Please wait...");
        mSpinnerIM.show();
    }
}
