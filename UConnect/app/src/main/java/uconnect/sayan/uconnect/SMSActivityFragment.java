package uconnect.sayan.uconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class SMSActivityFragment extends Fragment {

    public SMSActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sendSMS();
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        //smsIntent.putExtra("address"  , new String ("Type name or number"));
        //smsIntent.putExtra("sms_body"  , "Type text message");

        try {
            startActivity(smsIntent);
            getActivity().finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(getContext(),
              //      "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
            new DisplayToast().showToast(getContext(),
                    "SMS failed, please try again later.");
        }
    }
}
