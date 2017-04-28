package uconnect.sayan.uconnect;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by banersay on 08-06-2016.
 */
public class DisplayToast {

    public Toast mToast;

    public void showToast(Context context, String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
