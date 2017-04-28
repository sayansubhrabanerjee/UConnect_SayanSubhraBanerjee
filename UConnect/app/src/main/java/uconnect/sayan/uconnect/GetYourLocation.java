package uconnect.sayan.uconnect;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * Created by banersay on 15-06-2016.
 */
public class GetYourLocation extends FragmentActivity implements LocationListener {
    GoogleMap googleMap;
    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        /*ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mWifi.isConnected()) {
            // Do whatever
            Toast.makeText(this,"Wifi is not connected",Toast.LENGTH_SHORT).show();
        }*/


        setContentView(R.layout.activity_location);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = supportMapFragment.getMap();
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //googleMap.setMyLocationEnabled(true);
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (!wifiManager.isWifiEnabled()) {
            buildAlertMessageNoWLAN();
        }
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();

        }
        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

    private void buildAlertMessageNoWLAN() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your WLAN/Wi-fi seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView locationTv = (TextView) findViewById(R.id.latlongLocation);

        double latitude = location.getLatitude();

        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        try {
            showSpinner();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String street = addresses.get(0).getAddressLine(0); // If any additional address line
            // present than only,
            // check with max available address
            // lines by getMaxAddressLineIndex()
            String block = addresses.get(0).getAddressLine(1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            /*String knownName = addresses.get(0).getFeatureName();
            String phone = addresses.get(0).getPhone();
            String subLocality = addresses.get(0).getSubLocality();
            String subAdmin = addresses.get(0).getSubAdminArea();*/


            //locationTv.setText("Lat: "+latitude+"\n Long: "+longitude);

            // Street: "+addressLine1+"\n Block: "+addressLine2+"\n City: "+city+"\n
            // State: "+state+"\n Country: "+country+"\n PIN: "+postalCode+"\n
            // Known Name: "+knownName+"\n Phone: "+phone+"\n Sub Locality: "+subLocality+"\n
            // Sub Admin: "+subAdmin);



            String mlatitude = getResources().getString(R.string.latitude, latitude);
            locationTv.setText(mlatitude);
            locationTv.append("\n");
            String mlongitude = getResources().getString(R.string.longitude, longitude);
            locationTv.append(mlongitude);
            locationTv.append("\n");
            String mStreet = getResources().getString(R.string.street, street);
            locationTv.append(mStreet);
            locationTv.append("\n");
            String mBlock = getResources().getString(R.string.block, block);
            locationTv.append(mBlock);
            locationTv.append("\n");
            String mCity = getResources().getString(R.string.city, city);
            locationTv.append(mCity);
            locationTv.append("\n");
            String mState = getResources().getString(R.string.state, state);
            locationTv.append(mState);
            locationTv.append("\n");
            String mCountry = getResources().getString(R.string.country, country);
            locationTv.append(mCountry);
            locationTv.append("\n");
            String mPostalCode = getResources().getString(R.string.postalCode, postalCode);
            locationTv.append(mPostalCode);

            /*locationTv.append("\n");
            String mKnownName = getResources().getString(R.string.knownName,knownName);
            locationTv.append(mKnownName);
            locationTv.append("\n");
            String mPhone = getResources().getString(R.string.phone,phone);
            locationTv.append(mPhone);
            locationTv.append("\n");
            String mSubLocality = getResources().getString(R.string.subLocality,subLocality);
            locationTv.append(mSubLocality);
            locationTv.append("\n");
            String mSubAdmin = getResources().getString(R.string.subLocality,subAdmin);
            locationTv.append(mSubAdmin);*/

            /*SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+919206961639", null,
            "http://maps.google.com/?q="+latitude+","+longitude, null, null);*/



        } catch (Exception e) {
            Log.d("Message", "Location exception");
        }

    }
    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Tracking your location");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                mSpinner.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
