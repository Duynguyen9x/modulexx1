package com.add.toeic.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.fragment.BookFragment;
import com.add.toeic.fragment.NotificationsFragment;
import com.add.toeic.fragment.PracticeFragment;
import com.add.toeic.fragment.SettingsFragment;
import com.add.toeic.fragment.WordFragment;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.Word;
import com.add.toeic.other.CircleTransform;
import com.add.toeic.provider.AppProvider;
import com.add.toeic.services.FloatingViewService;
import com.add.toeic.services.UnlockedScreenService;
import com.add.toeic.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private View mNavHeader;
    private ImageView mImgNavHeaderBg, mImgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar mToolbar;
    private Switch mSwitchLockScreen;
    private Switch mSwitchChatHeader;
    private Context mContext;
    private LinearLayout ln_navi_lockscreen;
    private LinearLayout ln_navi_chatheader;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://scontent.fhan2-2.fna.fbcdn.net/v/t1.0-9/12003202_832755983507674_3619550408923210585_n.jpg?oh=6fe9fe5ed14d3277810f071885efb11a&oe=58BEA76B";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_WORDs = "Words";
    private static final String TAG_BOOKS = "Books";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_WORDs;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    // Toggle button nav_header_main
    private static final int REQUEST_CODE_SYSTEM_ALERT_WINDOW = 1;
    private static String[] APP_PRE_PERMISSIONS = {Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final String PREF_NAME_LOCK_SCREEN = "saveStateLockScreen";
    private static final String PREF_NAME_CHAT_HEADER = "saveStateChatheader";

    private static final String LOCKSCREEN_IS_OPEN = "lockscreen_is_open";
    private static final String CHATHEADER_IS_OPEN = "chatheader_is_open";
    private static final String screenHeaderView = "screen_header_view_state";
    private static final int LOADER_CALLBACK_ID = 0;

    BroadcastReceiver refreshSwitchChathearReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("anhdt", "MainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();

        IntentFilter filter = new IntentFilter("com.add.toeic.CUSTOM_INTENT_SWITCH_OFF");
        refreshSwitchChathearReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mSwitchChatHeader.setChecked(false);
            }
        };

        registerReceiver(refreshSwitchChathearReceiver, filter);

        initView();

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_WORDs;
            loadHomeFragment();
        }

        preLockScreenProcess();

        if (AppProvider.tb_All_is_Empty(mContext)) {
            AsyncTask<Void, Void, Void> generateData = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Log.d("anhdt", "populateTB_ALL");
                    AppProvider.populateTB_ALL(mContext);
                    return null;
                }
            };
            generateData.execute();
        }
        if (getIntent().getBooleanExtra("open_drawer", false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }, 1000);

            // trigger ripple effect
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ln_navi_lockscreen.setPressed(true);
                }
            }, 2000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ln_navi_lockscreen.setPressed(false);
                }
            }, 2100);
        }
    }

    private void preLockScreenProcess() {
        SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE);
        if (sharedPrefs == null) {
            initSharePreferenceLockScreen();
        } else {
            boolean isEnabled = sharedPrefs.getBoolean(LOCKSCREEN_IS_OPEN, false);
            mSwitchLockScreen.setChecked(isEnabled);
        }
        if (!UnlockedScreenService.isCreated()) {
            if (Utils.isUnderM()) {
                startService(new Intent(getApplicationContext(), UnlockedScreenService.class));
            } else {
                // if not, the permission will be handled in onActivityResult
                if (Utils.checkAndRequestPermissions(this, APP_PRE_PERMISSIONS, REQUEST_CODE_SYSTEM_ALERT_WINDOW)) {
                    startService(new Intent(getApplicationContext(), UnlockedScreenService.class));
                }
            }
        }
    }

    private void preChatHeaderProcess() {
        SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE);
        if (sharedPrefs == null) {
            initSharePreferenceLockScreen();
        } else {
            boolean isEnabled = sharedPrefs.getBoolean(LOCKSCREEN_IS_OPEN, false);
            mSwitchChatHeader.setChecked(isEnabled);
        }
        if (!UnlockedScreenService.isCreated()) {
            if (Utils.isUnderM()) {
                startService(new Intent(getApplicationContext(), UnlockedScreenService.class));
            } else {
                // if not, the permission will be handled in onActivityResult
                if (Utils.checkAndRequestPermissions(this, APP_PRE_PERMISSIONS, REQUEST_CODE_SYSTEM_ALERT_WINDOW)) {
                    startService(new Intent(getApplicationContext(), UnlockedScreenService.class));
                }
            }
        }
    }
    

    private void initSharePreferenceLockScreen() {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE).edit();
        editor.putBoolean(LOCKSCREEN_IS_OPEN, false);
        editor.apply();

        SharedPreferences.Editor editor2 = getSharedPreferences(PREF_NAME_CHAT_HEADER, MODE_PRIVATE).edit();
        editor2.putBoolean(screenHeaderView, false);
        editor2.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        refreshSwithButton();
        Log.d("anhdt", "MainActivity resume");
    }

    private void refreshSwithButton(){
        SharedPreferences sharedPrefs= getSharedPreferences(PREF_NAME_CHAT_HEADER, MODE_PRIVATE);
        if(sharedPrefs==null) {
            mSwitchChatHeader.setChecked(false);
            return;
        }
        boolean isEnabled = sharedPrefs.getBoolean(CHATHEADER_IS_OPEN, false);
        if(isEnabled) {
            mSwitchChatHeader.setChecked(true);
        } else {
            mSwitchChatHeader.setChecked(false);
        }
    }

    public void initView() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        mNavHeader = mNavigationView.getHeaderView(0);
        txtName = (TextView) mNavHeader.findViewById(R.id.name);
        txtWebsite = (TextView) mNavHeader.findViewById(R.id.website);
        mImgNavHeaderBg = (ImageView) mNavHeader.findViewById(R.id.img_header_bg);
        mImgProfile = (ImageView) mNavHeader.findViewById(R.id.img_profile);
        mSwitchLockScreen = (Switch) mNavHeader.findViewById(R.id.lock_screen);
        mSwitchChatHeader = (Switch) mNavHeader.findViewById(R.id.remind_word);
        ln_navi_lockscreen = (LinearLayout) mNavHeader.findViewById(R.id.ln_navi_lockscreen);
        ln_navi_chatheader = (LinearLayout) mNavHeader.findViewById(R.id.ln_navi_chatheader);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
    }

    private void loadNavHeader() {
        // name, website
        txtName.setText(getResources().getString(R.string.app_name));
        txtWebsite.setText("");

        mSwitchLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state1 = mSwitchLockScreen.isChecked();
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE).edit();
                editor.putBoolean(LOCKSCREEN_IS_OPEN, state1); // value to store
                editor.apply();
            }
        });
        ln_navi_lockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state1 = mSwitchLockScreen.isChecked();
                mSwitchLockScreen.setChecked(!state1);
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE).edit();
                editor.putBoolean(LOCKSCREEN_IS_OPEN, !state1); // value to store
                editor.apply();
            }
        });

        mSwitchChatHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean state1 = mSwitchChatHeader.isChecked();
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME_CHAT_HEADER, MODE_PRIVATE).edit();
                editor.putBoolean(CHATHEADER_IS_OPEN, state1); // value to store
                editor.apply();
                if(state1) {
                    //Toast.makeText(getApplicationContext(), "remind word started", Toast.LENGTH_SHORT).show();
                    startService(new Intent(MainActivity.this, FloatingViewService.class));
                } else {
                    stopService(new Intent(MainActivity.this, FloatingViewService.class));
                }
            }
        });

        ln_navi_chatheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state1 = mSwitchChatHeader.isChecked();
                mSwitchChatHeader.setChecked(!state1);
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME_CHAT_HEADER, MODE_PRIVATE).edit();
                editor.putBoolean(CHATHEADER_IS_OPEN, !state1); // value to store
                editor.apply();

                if(!state1) {
                   // Toast.makeText(getApplicationContext(), "remind word started", Toast.LENGTH_SHORT).show();
                    startService(new Intent(MainActivity.this, FloatingViewService.class));
                } else {
                    stopService(new Intent(MainActivity.this, FloatingViewService.class));
                }
            }
        });

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImgProfile);

        // showing dot next to notifications label
        mNavigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                WordFragment wordFragment = new WordFragment();
                return wordFragment;
            case 1:
                // photos
                BookFragment bookFragment = new BookFragment();
                return bookFragment;
            case 2:
                // movies fragment
                PracticeFragment practiceFragment = new PracticeFragment();
                return practiceFragment;
            case 3:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;

            case 4:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new WordFragment();
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            mDrawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main_menu content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        mDrawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        mNavigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main_menu content with ContentFragment Which is our Inbox View;
                    case R.id.nav_word:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_WORDs;
                        break;
                    case R.id.nav_book:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_BOOKS;
                        break;
                    case R.id.nav_practice:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        mDrawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        mDrawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_WORDs;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_search_main) {
            Intent intentSearch = new Intent(this, SearchActivity.class);
            startActivity(intentSearch);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SYSTEM_ALERT_WINDOW && Utils.enabledDrawOverlays(mContext)) {
            if (!UnlockedScreenService.isCreated()) {
                startService(new Intent(getApplicationContext(), UnlockedScreenService.class));
            }
        }
    }

    private void initLoadData(Cursor cursor) {
        AsyncTask<Cursor, Void, ArrayList<Word>> loadBitmapTask = new AsyncTask<Cursor, Void, ArrayList<Word>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected ArrayList<Word> doInBackground(Cursor... params) {
                return AppProvider.getAllWords(false);
            }

            @Override
            protected void onPostExecute(ArrayList<Word> result) {
                Log.d("anhdt", "notify" + result.size());

                for (int i = 0; i < result.size(); i++) {
                    Log.i("anhdt", "Test.item=" + result.get(i).toString());
                }
            }
        };

        loadBitmapTask.execute(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(refreshSwitchChathearReceiver);
    }
}