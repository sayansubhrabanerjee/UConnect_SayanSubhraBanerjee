package uconnect.sayan.uconnect;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface {

    public GooeyMenu mGooeyMenu;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713"); //appid from adMob..



        //new abc().xyz(this,"Hi");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable
                (getResources().getColor(R.color.default_color)));
        //setUpActionBar();

        mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        mGooeyMenu.setOnMenuListener(this);

        new AppRater().app_launched(this);

        /*Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("market://details?id=uconnect.sayan.uconnect"));
        startActivity(intent);*/
        /*Intent updateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/app-release.apk"));
        startActivity(updateIntent);*/





        //new AppRating().launchMarket(this,"unable to find market app");


    }

    /*private void setUpActionBar() {
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //ActionBar actionBar = getActionBar();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable
                    (getResources().getColor(R.color.default_color)));
            //toolbar.setDisplayHomeAsUpEnabled(true);
        }

    }*/

    /*@Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
        }
        super.onDestroy();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main) {
            //new DisplayToast().showToast(this,"Your location is: ");
            //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            Intent intent = new Intent(getApplicationContext(), GetYourLocation.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void menuOpen() {
        //showToast("Menu Open");
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.double_up_click);
        mediaPlayer.start();
        //new DisplayToast().showToast(this, "Menu Opened");
    }

    @Override
    public void menuClose() {
        //showToast( "Menu Close");
        mediaPlayer = MediaPlayer.create(this, R.raw.tiny_button);
        mediaPlayer.start();
        //new DisplayToast().showToast(this, "Menu Closed");
    }

    @Override
    public void menuItemClicked(int menuNumber) {
        //showToast("Item: "+menuNumber);

        switch (menuNumber) {
            case 1:
                //showToast("Phone");
                //onClickSelectContact();
                //startActivity(new Intent(this,DialerActivity.class));
                //new DialerActivity().onClickSelectContact(this);
                //new CallActivity().onClickSelectContact(this);


                mediaPlayer = MediaPlayer.create(this, R.raw.single_oil_can);
                mediaPlayer.start();
                //startActivity(new Intent(this,MainActivity.class));
                MainActivityFragment mainActivityFragment =
                        (MainActivityFragment) getSupportFragmentManager().findFragmentById
                                (R.id.fragment_main_id);
                mainActivityFragment.onClickSelectContact();
                break;

            case 2:
                //showToast("Docs");

                mediaPlayer = MediaPlayer.create(this, R.raw.single_oil_can);
                mediaPlayer.start();
                startActivity(new Intent(this, SMSActivity.class));
                break;

            case 3:
                //showToast("Mail");
                //mailNow();

                mediaPlayer = MediaPlayer.create(this, R.raw.single_oil_can);
                mediaPlayer.start();
                new SendEmail().mailNow(this);
                break;

            case 4:
                //showToast("Music");

                mediaPlayer = MediaPlayer.create(this, R.raw.single_oil_can);
                mediaPlayer.start();
                //new DisplayToast().showToast(this,"Muzic");
                startActivity(new Intent(this, LoginActivityInstantMessaging.class));
                break;

            case 5:
                //showToast("Video");

                mediaPlayer = MediaPlayer.create(this, R.raw.single_oil_can);
                mediaPlayer.start();
                //new DisplayToast().showToast(this,"Videos");
                startActivity(new Intent(this, LoginActivity.class));
                break;

            default:
                //showToast("Invalid!");
                new DisplayToast().showToast(this, "Invalid Choice!");
        }
    }

}