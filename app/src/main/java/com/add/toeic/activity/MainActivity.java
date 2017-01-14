package com.add.toeic.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.add.toeic.Constants.DBInfo;
import com.add.toeic.R;
import com.add.toeic.fragment.BookFragment;
import com.add.toeic.fragment.NotificationsFragment;
import com.add.toeic.fragment.PracticeFragment;
import com.add.toeic.fragment.SettingsFragment;
import com.add.toeic.fragment.WordFragment;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.Word;
import com.add.toeic.model.WordInfo;
import com.add.toeic.other.CircleTransform;
import com.add.toeic.services.FloatingViewService;
import com.add.toeic.services.UnlockedScreenService;
import com.add.toeic.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor> {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ToggleButton tgBtnLockScreen;
    private ToggleButton remindWordBtn;
    private Context mContext;

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

    private static final String quickAnswerState = "tgbtn_unlocked_quick_answer_state";
    private static final String screenHeaderView = "screen_header_view_state";
    private static final int LOADER_CALLBACK_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        initView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        getSupportLoaderManager().initLoader(LOADER_CALLBACK_ID, null, this);

    }

    private void preLockScreenProcess() {
        SharedPreferences sharedPrefs = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE);
        if (sharedPrefs == null) {
            initSharePreferenceLockScreen();
        } else {
            boolean isEnabled = sharedPrefs.getBoolean(quickAnswerState, false);
            tgBtnLockScreen.setChecked(isEnabled);
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
        editor.putBoolean(quickAnswerState, false);
        editor.apply();

        SharedPreferences.Editor editor2 = getSharedPreferences(PREF_NAME_CHAT_HEADER, MODE_PRIVATE).edit();
        editor2.putBoolean(screenHeaderView, false);
        editor2.apply();
    }

    public void initView() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        tgBtnLockScreen = (ToggleButton) navHeader.findViewById(R.id.lock_screen);
        remindWordBtn = (ToggleButton) navHeader.findViewById(R.id.remind_word);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Toiec 650+");
        txtWebsite.setText("duynguyen93.vnu@gmail.com");

        tgBtnLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state1 = tgBtnLockScreen.isChecked();
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME_LOCK_SCREEN, MODE_PRIVATE).edit();
                editor.putBoolean(quickAnswerState, state1); // value to store
                editor.apply();
            }
        });

        remindWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "remind word started", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatingViewService.class));
            }
        });

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
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
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
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

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
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
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
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


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

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
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
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
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        String[] projections = null;

        // muon lay full word thi de null, muon lay reminder thi de nhu duoi
        String selection = null;
        // String selection = WordContract.Word.WORD_STATUS + " = " + 0;

        String[] argu = null;
        String orderBy = null;
        uri = DBInfo.CONTENT_URI;
        Log.d("anhdt", " load cursor loader with id " + id);
        CursorLoader cursorLoader = new CursorLoader(mContext, uri, projections, selection, argu, orderBy);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        initLoadData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private void initLoadData(Cursor cursor) {
        AsyncTask<Cursor, Void, ArrayList<Word>> loadBitmapTask = new AsyncTask<Cursor, Void, ArrayList<Word>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected ArrayList<Word> doInBackground(Cursor... params) {
                return getListWord(params[0]);
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

    private ArrayList<Word> getListWord(Cursor cursor) {
        ArrayList<Word> arr = new ArrayList<>();

        if (cursor != null) {
            Log.d("anhdt", " cursor " + cursor.getCount());
            while (cursor.moveToNext()) {
                Word app = new Word();
                String id = cursor.getString(cursor.getColumnIndex(DBInfo.COLUMN_WORD_ID));
                app.setName(id);

                String name = cursor.getString(cursor.getColumnIndex(DBInfo.COLUMN_WORD_NAME));
                app.setName(name);

                String name_key = cursor.getString(cursor.getColumnIndex(DBInfo.COLUMN_WORD_NAME_KEY));
                app.setName_key(name_key);

                String sound = cursor.getString(cursor.getColumnIndex(DBInfo.COLUMN_WORD_SOUND));
                app.setSound(sound);

                String examlpe = cursor.getString(cursor.getColumnIndex(DBInfo.COLUMN_WORD_EXAMPLE));
                app.setExample(examlpe);

                String examlpe_key = cursor.getString(cursor.getColumnIndex(DBInfo.COLUMN_WORD_EXAMPLE_KEY));
                app.setExample(examlpe_key);

                int kind = cursor.getInt(cursor.getColumnIndex(DBInfo.COLUMN_WORD_KIND));
                app.setKind_word(kind);
                // viet full get doi tuong o day
                arr.add(app);
            }
            cursor.close();
        }
        return arr;
    }
}