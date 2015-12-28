package com.app.namesofallah;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.InterstitialAd;

public class SubActivity extends ActionBarActivity {

    private ImageView left,right,playAudio,mainImg;
    private Toolbar toolbar;
    private TextView heading,subheading,desc;
    private int POS;
    Content obj;
    ProgressDialog dialog;
    ListPopupWindow popupWindow;
    InterstitialAd interstitial;
    AdRequest adRequest;
    int counterLeft=0;
    int counterRight=0;
    int maincounter=0;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("99 names of Allah");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.drawable.toolbaricon);
        interstitial = new InterstitialAd(SubActivity.this);
        interstitial.setAdUnitId("ca-app-pb-6733180445570119/3558421980");
        // adview starts

        AdView adView = (AdView) this.findViewById(R.id.adView);
        // Request for Ads
        adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);

       /* // Load ads into Interstitial Ads
        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });*/

        // adview ends

        obj = new Content();
        left = (ImageView)findViewById(R.id.left);
        right = (ImageView)findViewById(R.id.right);
        playAudio = (ImageView)findViewById(R.id.playAudio);

        mainImg = (ImageView)findViewById(R.id.mainImg);
        heading = (TextView)findViewById(R.id.heading);
        subheading= (TextView)findViewById(R.id.subheading);
        desc = (TextView)findViewById(R.id.desc);

        POS = getIntent().getIntExtra("pos",0);
        fillDetails(POS);


        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isPlaying){
                    isPlaying = false;
                    mediaPlayer.stop();
                    changImage();
                }
                maincounter +=1;

                if(maincounter==15){
                    // Load ads into Interstitial Ads
                    interstitial.loadAd(adRequest);
                    // Prepare an Interstitial Ad Listener
                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            maincounter=0;
                            displayInterstitial();

                        }
                    });


                }



                if(POS <= 0){
                    Toast.makeText(SubActivity.this,"You are at First Word",Toast.LENGTH_LONG).show();
                }else {
                    POS -= 1;
                    fillDetails(POS);
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(isPlaying){
                    isPlaying = false;
                    mediaPlayer.stop();
                    changImage();
                }

                maincounter+=1;

                if(maincounter==15){
                    // Load ads into Interstitial Ads
                    interstitial.loadAd(adRequest);
                    // Prepare an Interstitial Ad Listener
                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            maincounter=0;
                            displayInterstitial();

                        }
                    });


                }



                if(POS>=98){
                    Toast.makeText(SubActivity.this,"You are at Last Word",Toast.LENGTH_LONG).show();
                }else {
                    POS += 1;
                    fillDetails(POS);
                }


                return false;
            }
        });

        playAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

               //dialog= ProgressDialog.show(SubActivity.this,obj.NamesSub.get(POS),"Playing audio",true);
                //dialog.setCancelable(true);

                if(isPlaying){
                    isPlaying = false;
                    mediaPlayer.stop();
                    changImage();
                }else {
                    String fname = obj.Audio.get(POS);
                    int resID = getResources().getIdentifier(fname, "raw", getPackageName());
                    mediaPlayer = MediaPlayer.create(SubActivity.this, resID);

                    mediaPlayer.start();

/*
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        mediaPlayer.stop();
                    }
                });
                */

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            //dialog.show();
                            isPlaying = true;
                            changImage();
                        }
                    });

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.stop();
                            isPlaying = false;
                            changImage();

                            //       dialog.dismiss();
                        }
                    });
                }

                return false;
            }
        });


    }

    private void changImage(){
      if(isPlaying)
          playAudio.setImageResource(R.drawable.stop);
        else  playAudio.setImageResource(R.drawable.audio_play);
    }




    public void displayInterstitial() {

        String fname=obj.Audio.get(POS);
        int resID=getResources().getIdentifier(fname, "raw", getPackageName());
        mediaPlayer=MediaPlayer.create(SubActivity.this,resID);

        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {

           if(mediaPlayer.isPlaying()) {
               mediaPlayer.stop();
             //  dialog.dismiss();
           }

            interstitial.show();
        }
    }

    private void fillDetails(int POS2){



     /*   Bitmap bitmapImage = getBitmapFromAsset(obj.BIG_Images.get(POS));
        Drawable drawImage = new BitmapDrawable(getResources(), bitmapImage);*/
        String imgName = obj.BIG_Images.get(POS2);
        int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());

        mainImg.setBackgroundResource(resId);



        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Gabriela-Regular.ttf");
        heading.setTypeface(tf);
        subheading.setTypeface(tf,Typeface.BOLD_ITALIC);
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        desc.setTypeface(tf1);

        heading.setText(obj.NamesSub.get(POS2));
        subheading.setText(obj.Sub_heading.get(POS2));
        desc.setText(obj.Description.get(POS2));

        if(POS2==0){
            playAudio.setVisibility(View.GONE);
        }else {
            playAudio.setVisibility(View.VISIBLE);
        }

    }

   /* private Bitmap getBitmapFromAsset(String imgName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(imgName);
        } catch (IOException e) {
            Log.e("exc",e.toString());
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                openSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        View menuSettings = findViewById(R.id.action_settings); // SAME ID AS MENU ID

        String[] names = {"Share App","Rate App"};
        //naeem int[] img = {R.drawable.icon_wallpaper};

        popupWindow = new ListPopupWindow(SubActivity.this);
        popupWindow.setAnchorView(menuSettings);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(names));

        int width = getResources().getDisplayMetrics().widthPixels;
        int height =  getResources().getDisplayMetrics().heightPixels;

        popupWindow.setWidth((int) (width / 1.5));
        popupWindow.setHeight(150);
        popupWindow.setModal(true);
        popupWindow.setAdapter(new SettingsAdapterSub(SubActivity.this, arrayList));
        popupWindow.show();
    }

    public  class SettingsAdapterSub extends ArrayAdapter<String> {

        // View lookup cache
        private ArrayList<String> users;
        Context ctx;
        //int[] iconImg;

        private class ViewHolder {
            TextView name;
            TextView home;
        }

        public SettingsAdapterSub(Context context, ArrayList<String> users) {
            super(context, R.layout.item_popup, users);
            this.users = users;
            this.ctx = context;
            //this.iconImg = icon;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popup2, parent, false);

                ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
                //imgIcon.setBackgroundResource(iconImg[position]);


                TextView itemNames = (TextView) convertView.findViewById(R.id.txtItemName);
                itemNames.setText(users.get(position));

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (position) {
                        case 0:
                            String text = "Please Check out this amazing app 99 Names of Allah , \n https://play.google.com/store/apps/details?id=com.app.holynamesofallah";

                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                            // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                            ctx.startActivity(Intent.createChooser(sharingIntent, "Share using"));


                            break;
                      /*  case 2:
                            popupWindow.dismiss();

                            break;*/
                        case 1:
                            showalertBox();
                            popupWindow.dismiss();
                            break;


                    }
                }
            });

            // Populate the data into the template view using the data object
            // Return the completed view to render on screen
            return convertView;
        }
        private void showalertBox(){
            CustomDialogBox box = new CustomDialogBox(SubActivity.this);
            box.show();
        }
    }

    //end of main class
}
