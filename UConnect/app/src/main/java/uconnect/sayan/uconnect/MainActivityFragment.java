package uconnect.sayan.uconnect;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    //private static final int PERMISSION_REQUEST_CODE_CONTACTS = 2;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myRootView = inflater.inflate(R.layout.fragment_main, container, false);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        final Toast toast = Toast.makeText(
//                                getApplicationContext(), count + "",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
                        Random rnd = new Random();
                        int color1 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                                rnd.nextInt(256));
                        myRootView.getRootView().setBackgroundColor(color1);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                //toast.cancel();
                            }
                        }, 10000);

                    }
                });
            }
        }, 0, 10000); //will pop up after every 10 secs


        return myRootView;
    }

    public void onClickSelectContact() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("This app needs contact access");
            builder.setMessage("Please grant contact access so this app can show the contacts of your phonebook.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PICK_CONTACTS);
                }
            });
            builder.show();
            //return;
        }

        else {
            // using native contacts selection
            // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
            startActivityForResult(new Intent
                            (Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
                    REQUEST_CODE_PICK_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS) {
            /*if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setTrafficEnabled(true);
                    Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();

                }
            }*/

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(getContext().getPackageName(), "phonebook permission granted");
                //Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
                onClickSelectContact();
                //updateLocation(myLoc);
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since contacts access has not been granted, this app will not be running in its full functionality.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                });
                builder.show();
            }
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == MainActivity.RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();

        }
    }

    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream
                    (getContext().getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                                    new Long(contactID)));


            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                //ImageView imageView = (ImageView) getActivity().findViewById(R.id.img_contact);
                //imageView.setImageBitmap(photo);
            }

            //assert inputStream != null;
            if (inputStream == null) {
                inputStream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContext().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);


        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone =
                getContext().getContentResolver().query
                        (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                                new String[]{contactID},
                                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        //Toast.makeText(getContext(), "Contact Number: " +
        // contactNumber, Toast.LENGTH_SHORT).show();

        LocationManager locationManager = (LocationManager)
                getContext().getSystemService(getContext().LOCATION_SERVICE);
        if (locationManager != null) {
            if (locationManager != null) {
                if (getContext().checkCallingOrSelfPermission
                        (Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        || getContext().checkCallingOrSelfPermission
                        (Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //locationManager.removeUpdates(GPSListener.this);
                }
            }
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactNumber)));
        //getActivity().finish(); //bounce back to fragment_main after the call is dropped.


    }


    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContext().getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER = Indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        //Log.d(TAG, "Contact Name: " + contactName);
        //Toast.makeText(getContext().getApplicationContext(),
        // "Name: "+contactName,Toast.LENGTH_SHORT).show();

    }


}
