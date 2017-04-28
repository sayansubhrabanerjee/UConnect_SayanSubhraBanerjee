package uconnect.sayan.uconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sinch.android.rtc.SinchError;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button mLoginButton;
    private EditText mLoginName;
    private ProgressDialog mSpinner;
    public TextView mtroubleTextView;
    public TextView mDescSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mtroubleTextView = (TextView)findViewById(R.id.troubleTextView);

        mDescSnack = new TextView(LoginActivity.this);
        mDescSnack.setText("Please enter a name!");
        mDescSnack.setGravity(Gravity.CENTER);

        mtroubleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                troubleshootClicked(view);
            }
        });

        mLoginName = (EditText) findViewById(R.id.loginName);

        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked(v);
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        //Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        new DisplayToast().showToast(this,error.toString());
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    public void loginClicked(View view) {
        String userName = mLoginName.getText().toString();
        String sDescSnack = mDescSnack.getText().toString();

        if (userName.isEmpty()) {
            //Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            //new DisplayToast().showToast(this,"Please enter a name");
            Snackbar snackbar = Snackbar
                    .make(view, sDescSnack, Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            snackbar.getView().setBackgroundColor(Color.parseColor("#7F049E"));
            snackbar.show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
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

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();

    }
}
