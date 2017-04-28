package uconnect.sayan.uconnect;

import android.content.Context;
import android.content.Intent;

/**
 * Created by banersay on 08-06-2016.
 */
public class SendEmail {

    public void mailNow(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(context, "There are no email clients installed.",
              //      Toast.LENGTH_SHORT).show();
            new DisplayToast().showToast(context, "There are no email clients installed.");
        }
    }
}
