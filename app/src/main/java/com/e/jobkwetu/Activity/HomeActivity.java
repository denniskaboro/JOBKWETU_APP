package com.e.jobkwetu.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.CategoryAdapter;
import com.e.jobkwetu.Adapters.InspiredAdapter;
import com.e.jobkwetu.Adapters.NewlyJoinedAdapter;
import com.e.jobkwetu.Adapters.PopularAdapter;
import com.e.jobkwetu.Adapters.SliderAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Helper.DatabaseHelper;
import com.e.jobkwetu.Home_Fragments.About_us_fragment;
import com.e.jobkwetu.Home_Fragments.Contact_us_fragment;
import com.e.jobkwetu.Home_Fragments.Disclaimer_fragment;
import com.e.jobkwetu.Home_Fragments.Explore_Fragment;
import com.e.jobkwetu.Home_Fragments.History_fragment;
import com.e.jobkwetu.Home_Fragments.Privacy_policy_fragment;
import com.e.jobkwetu.Home_Fragments.Taskers_list_Fragment;
import com.e.jobkwetu.Home_Fragments.Universal_tasks_fragment;
import com.e.jobkwetu.Model.Category;
import com.e.jobkwetu.Model.Inspired_model;
import com.e.jobkwetu.Model.Popular_Model;
import com.e.jobkwetu.Model.Taskers;
import com.e.jobkwetu.Model.newly_joined_model;
import com.e.jobkwetu.R;
import com.e.jobkwetu.Register_User.LoginActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Taskers_list_Fragment.SelectedWorker {
    private static final String TAG = "HomeActivity";
    BottomNavigationView bottomNavigation;
    private DrawerLayout drawer;
    FragmentManager mFragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
    List<Fragment> activefragment = new ArrayList<Fragment>();

    private CategoryAdapter mAdapter;
    private PopularAdapter mAdapter2;
    private NewlyJoinedAdapter mAdapter3;
    private InspiredAdapter mAdapter4;

    private List<Category> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView, fabtxt, fabtxt1;
    private Button invite;
    private FloatingActionButton fab, fab1, fab2;
    private ArrayList<Popular_Model> popularArrayList;
    private ArrayList<newly_joined_model> newlyArrayList;
    private ArrayList<Inspired_model> InspArrayList;
    private RecyclerView poprecycler;
    private RecyclerView newlyrecycler;
    private RecyclerView insprecycler;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ShimmerFrameLayout shimmerFrameLayout2;
    private ShimmerFrameLayout shimmerFrameLayout3;
    private DatabaseHelper db;
    private boolean isOpen = false;
    private Animation FadOpen, FadeClose;
    private Uri mInvitationUrl;


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Check for login session. If not logged in launch
         * login activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            launchLoginActivity();
        }
        setContentView(R.layout.activity_home);

        //Bottom navigation menu
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //openFragment(new Explore_Fragment())


        Toolbar toolbar = (Toolbar) findViewById(R.id.hmtoolbar);
        setSupportActionBar(toolbar);

        shimmerFrameLayout = findViewById(R.id.popular_shine);
        shimmerFrameLayout2 = findViewById(R.id.newly_shine);
        shimmerFrameLayout3 = findViewById(R.id.inspired_shine);

        fab = findViewById(R.id.main_fab);
        fab1 = findViewById(R.id.jobberform_btn);
        fab2 = findViewById(R.id.taskform_btn1);
        fabtxt = findViewById(R.id.jobberform_text);
        fabtxt1 = findViewById(R.id.taskform_text);

        invite = findViewById(R.id.inviteBtn);

        poprecycler = findViewById(R.id.popular_recycler);
        popularArrayList = new ArrayList<>();
        mAdapter2 = new PopularAdapter(this, popularArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        poprecycler.setLayoutManager(layoutManager);
        poprecycler.setItemAnimator(new DefaultItemAnimator());
        poprecycler.setAdapter(mAdapter2);
        fetchPopularThread();

        newlyrecycler = findViewById(R.id.newly_joined_recycler);
        newlyArrayList = new ArrayList<>();
        mAdapter3 = new NewlyJoinedAdapter(this, newlyArrayList);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        newlyrecycler.setLayoutManager(layoutManager2);
        newlyrecycler.setItemAnimator(new DefaultItemAnimator());
        newlyrecycler.setAdapter(mAdapter3);
        fetchNewlyThread();

        insprecycler = findViewById(R.id.inspired_recycler);
        InspArrayList = new ArrayList<>();
        mAdapter4 = new InspiredAdapter(this, InspArrayList);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        insprecycler.setLayoutManager(layoutManager3);
        insprecycler.setItemAnimator(new DefaultItemAnimator());
        insprecycler.setAdapter(mAdapter4);
        fetchIspiredThread();


        fab.setVisibility(View.VISIBLE);
        FadOpen = AnimationUtils.loadAnimation(this, R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(this, R.anim.fade_close);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createinvitationlink();
                Log.d(TAG, "onClick: invite clicked");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                if (isOpen) {
                    fab1.startAnimation(FadeClose);
                    fab2.startAnimation(FadeClose);
                    fab1.setClickable(false);
                    fab2.setClickable(false);


                    fabtxt.startAnimation(FadeClose);
                    fabtxt1.startAnimation(FadeClose);

                    fabtxt.setClickable(false);
                    fabtxt1.setClickable(false);
                    isOpen = false;

                } else {
                    fab1.startAnimation(FadOpen);
                    fab2.startAnimation(FadOpen);
                    fab1.setClickable(true);
                    fab2.setClickable(true);


                    fabtxt.startAnimation(FadOpen);
                    fabtxt1.startAnimation(FadOpen);
                    fabtxt.setClickable(true);
                    fabtxt1.setClickable(true);
                    isOpen = true;
                }
                //fab1.setVisibility(View.VISIBLE);
                //fab2.setVisibility(View.VISIBLE);
                //fabtxt.setVisibility(View.VISIBLE);
                //fabtxt1.setVisibility(View.VISIBLE);
            }
        });


        //coordinatorLayout = findViewById(R.id.coordinator_layout);
        // recyclerView = findViewById(R.id.recycler_view);
        // noNotesView = findViewById(R.id.empty_notes_view);
        db = new DatabaseHelper(this);
        //notesList.addAll(db.getAllNotes());


        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapter adapter = new SliderAdapter(this);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        //mAdapter = new CategoryAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        // recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        // recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();


        invalidateOptionsMenu();
        CheckBalance();


        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         *
         recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
         recyclerView, new RecyclerTouchListener.ClickListener() {
        @Override public void onClick(View view, final int position) {
        if (getSupportFragmentManager().getBackStackEntryCount() <1) {
        // pass data to new activity
        Bundle args = new Bundle();
        args.putString("id", String.valueOf(position));
        task.setArguments(args);
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.container,task)
        .addToBackStack(null)
        .commit();
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.replace(R.id.container, task);
        //transaction.addToBackStack(null);
        // activefragment.add(task);
        // transaction.commit();
        }

        }

        @Override public void onLongClick(View view, int position) {
        showActionsDialog(position);
        }
        }));
         */


        //Navigation drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View header = navigationView.getHeaderView(0);

        TextView Header_username = header.findViewById(R.id.head_username);
        TextView Header_phone = header.findViewById(R.id.head_phone_no);
        CircleImageView header_image = header.findViewById(R.id.head_imageView);
        final String username1 = MyApplication.getInstance().getPrefManager().getUser().getName();
        final String phone2 = MyApplication.getInstance().getPrefManager().getUser().getPhone();
        Header_username.setText(username1);
        Header_phone.setText(phone2);
    }

    private void createinvitationlink() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String link = "https://Jobkwetu.page.link/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://jobkwetu.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.example.android")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = shortDynamicLink.getShortLink();
                        sendInvitation(mInvitationUrl);
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e);
                    }
                });
        // [END ddl_referral_create_link]
    }
    public void sendInvitation(Uri minvitation) {
        // [START ddl_referral_send]
        String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String subject = String.format("%s wants you to join Jobkwetu", referrerName);
        String invitationLink = minvitation.toString();
        String msg = "Welcome to Jobkwetu! Use my referrer link: "
                + invitationLink;
        String msgHtml = String.format("<p>Welcome to Jobkwetu! Use my "
                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        // [END ddl_referral_send]
    }

    private void addData() {

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, JobberFormActivity.class);
                startActivity(intent);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, FillTaskFormActivity.class);
                startActivity(intent);

            }
        });

    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        activefragment.add(fragment);
        fab.setVisibility(View.INVISIBLE);
        if (isOpen) {
            fab1.startAnimation(FadeClose);
            fab2.startAnimation(FadeClose);
            fab1.setClickable(false);
            fab2.setClickable(false);


            fabtxt.startAnimation(FadeClose);
            fabtxt1.startAnimation(FadeClose);

            fabtxt.setClickable(false);
            fabtxt1.setClickable(false);
            isOpen = false;

        }
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:

                            if (activefragment.size() > 0) {
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                for (Fragment fragment : activefragment) {
                                    transaction.remove(fragment);
                                }
                                fab.setVisibility(View.VISIBLE);
                                if (isOpen) {
                                    fab1.startAnimation(FadeClose);
                                    fab2.startAnimation(FadeClose);
                                    fab1.setClickable(false);
                                    fab2.setClickable(false);


                                    fabtxt.startAnimation(FadeClose);
                                    fabtxt1.startAnimation(FadeClose);

                                    fabtxt.setClickable(false);
                                    fabtxt1.setClickable(false);
                                    isOpen = false;

                                }
                                activefragment.clear();
                                transaction.commit();
                            }
                            return true;
                        case R.id.navigation_explore:
                            openFragment(new Explore_Fragment());
                            return true;
                        case R.id.navigation_history:
                            openFragment(new History_fragment());
                            return true;
                        // case R.id.navigation_message:
                        //    openFragment(new Message_Fragment());
                        //    return true;
                        case R.id.navigation_notification:
                            startActivity(new Intent(getApplicationContext(), ChatRoomActivity.class));
                            //openFragment(new NotificationsFragment());
                            return true;
                        case R.id.navigation_tasks:
                            startActivity(new Intent(getApplicationContext(), MainChatActivity.class));
                            //openFragment(new Tasks_Fragment());
                            return true;
                    }
                    return false;
                }
            };

    private void launchLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        CheckBalance();
        return true;
    }

    private void CheckBalance() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.MPESABALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (true == obj.getBoolean("success")) {
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("data");


                        String n = userObj.getString("balance");
                        //Toast.makeText(TransactionsActivity.this, n, Toast.LENGTH_SHORT).show();


                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Name, n);
                        editor.commit();
                        //final String balance22 = MyApplication.getInstance().getPrefManager().getUser().getToken();



                        //WHAT TODO


                    } else {
                        // login error - simply toast the message
                        Toast.makeText(HomeActivity.this, "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(HomeActivity.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                Toast.makeText(HomeActivity.this, "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {



            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.home_balance);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Name)) {
            item.setTitle("KSH:"+sharedpreferences.getString(Name, ""));
        }else {item.setTitle("KSH:....");}
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout:
                MyApplication.getInstance().logout();
                FirebaseAuth.getInstance().signOut();
                break;
            case R.id.home_about_us:
                openFragment(new About_us_fragment());
                break;
            case R.id.home_balance:
                startActivity(new Intent(getApplicationContext(), TransactionsActivity.class));
                 break;
            case R.id.home_contact_us:
                openFragment(new Contact_us_fragment());
                break;
            case R.id.home_disclaimer:
                openFragment(new Disclaimer_fragment());
                break;
            case R.id.home_privacy_policy:
                openFragment(new Privacy_policy_fragment());
                break;
            case R.id.home_treding_news:
                openFragment(new Universal_tasks_fragment());
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void ftAnimation() {
        if (isOpen) {
            fab1.startAnimation(FadeClose);
            fab2.startAnimation(FadeClose);
            fab1.setClickable(false);
            fab2.setClickable(false);


            fabtxt.startAnimation(FadeClose);
            fabtxt1.startAnimation(FadeClose);

            fabtxt.setClickable(false);
            fabtxt1.setClickable(false);
            isOpen = false;

        } else {
            fab1.startAnimation(FadOpen);
            fab2.startAnimation(FadOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);


            fabtxt.startAnimation(FadOpen);
            fabtxt1.startAnimation(FadOpen);
            fabtxt.setClickable(true);
            fabtxt1.setClickable(true);
            isOpen = true;
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                super.onBackPressed();
                fab.setVisibility(View.VISIBLE);
                if (isOpen) {
                    fab1.startAnimation(FadeClose);
                    fab2.startAnimation(FadeClose);
                    fab1.setClickable(false);
                    fab2.setClickable(false);


                    fabtxt.startAnimation(FadeClose);
                    fabtxt1.startAnimation(FadeClose);

                    fabtxt.setClickable(false);
                    fabtxt1.setClickable(false);
                    isOpen = false;

                }
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                    //fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                    if (isOpen) {
                        fab1.startAnimation(FadeClose);
                        fab2.startAnimation(FadeClose);
                        fab1.setClickable(false);
                        fab2.setClickable(false);


                        fabtxt.startAnimation(FadeClose);
                        fabtxt1.startAnimation(FadeClose);

                        fabtxt.setClickable(false);
                        fabtxt1.setClickable(false);
                        isOpen = false;

                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle(getString(R.string.exit));
                    builder.setMessage(getString(R.string.exit_message));
                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.show();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Home:
                if (activefragment.size() > 0) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    for (Fragment fragment : activefragment) {
                        transaction.remove(fragment);
                    }
                    activefragment.clear();
                    transaction.commit();
                }
                break;
            case R.id.nav_balance:
                startActivity(new Intent(getApplicationContext(), TransactionsActivity.class));
                //openFragment(new History_fragment());
                break;
            case R.id.nav_history:
                openFragment(new History_fragment());
                break;
            case R.id.nav_explore:
                openFragment(new Explore_Fragment());
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
                startActivity(intent);
                //openFragment(new Profile_Activity());
                break;
            case R.id.nav_setting:
                Intent intent2 = new Intent(getApplicationContext(), Settings_Activity.class);
                startActivity(intent2);
                //openFragment(new Settings_Activity());
                break;
            case R.id.nav_ratting:
                try {
                    Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException e) {
                    Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }
                break;
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "JOBKWETU APP");
                    String shareMessage = "Goal App.\nLet me recommend you this application that has high aim to share information.\n\n";
                    shareMessage = shareMessage + "https://drive.google.com/open?id=1-pFgVofDmDw6e-ZN4ilpRkvWeaPhL4Bx" + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share JOBKWETU APP"));
                } catch (Exception e) {
                    Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_invite_friends:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "JOBKWETU APP");
                    String shareMessage = "Goal App.\nLet me recommend, you this application that has high aim to share information.\n\n";
                    shareMessage = shareMessage + "https://drive.google.com/open?id=1-pFgVofDmDw6e-ZN4ilpRkvWeaPhL4Bx" + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share JOBKWETU APP"));
                } catch (Exception e) {
                    Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_website:
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://jobkwetu.com"));
                startActivity(viewIntent);
                break;
            case R.id.nav_facebook:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://facebook.com/jobkwetu"));
                startActivity(viewIntent);
                break;
            case R.id.nav_twitter:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://twitter.com/jobkwetu"));
                startActivity(viewIntent);
                break;
            case R.id.nav_youtube:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://youtube.com/channel/UCQQ9GZ90-E_Ci04jQ"));
                startActivity(viewIntent);
                break;
            case R.id.nav_logout:
                MyApplication.getInstance().logout();
                FirebaseAuth.getInstance().signOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
   /* private void createCategory(String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertCategory(note);

        // get the newly inserted note from db
        Category n = db.getCategory(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    */

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateCategory(String category, int position) {
        Category n = notesList.get(position);
        // updating note text
        n.setNote(category);

        // updating note in db
        db.updateCategory(n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteCategory(int position) {
        // deleting the note from db
        db.deleteCategory(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteCategory(position);
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Category category, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && category != null) {
            inputNote.setText(category.getNote());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(HomeActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && category != null) {
                    // update note by it's id
                    updateCategory(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    // createCategory(inputNote.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getCategoryCount() > 0) {
            //noNotesView.setVisibility(View.GONE);
        } else {
            //noNotesView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void workersselected(Taskers data) {
        Intent intent = new Intent(this, FillTaskFormActivity.class);
        intent.putExtra("data2", data.getJobber_id());
        intent.putExtra("data", data.getUsername());
        startActivity(intent);
    }
    private void fetchPopularThread() {

        String endPoint = EndPoints.POPULAR;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);

                            String populartitle = popularObj.getString("title");

                            String popularimage = popularObj.getString("image");
                            //String createdAt = popularObj.getString("created_at");

                            //JSONObject userObj = commentObj.getJSONObject("user");
                            //String userId = userObj.getString("user_id");
                            //String userName = userObj.getString("username");
                            //User user = new User(userId, userName, null);

                            Popular_Model popular= new Popular_Model(populartitle,popularimage);
                            //String populaertitle2=popular.getTitle();
                            //popular.setId(commentId);

                            popularArrayList.add(popular);
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }

                        mAdapter2.notifyDataSetChanged();
                        if (mAdapter2.getItemCount() > 1) {
                            poprecycler.getLayoutManager().smoothScrollToPosition(poprecycler, null, mAdapter2.getItemCount() + 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void fetchNewlyThread() {

        String endPoint = EndPoints.NEWLY;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);

                            String username = popularObj.getString("username");
                            String category = popularObj.getString("category");
                            String location = popularObj.getString("location");
                            String date = popularObj.getString("date");
                            String image = popularObj.getString("image");
                            newly_joined_model newlymodel= new newly_joined_model(username,image,category,location,date);
                            newlyArrayList.add(newlymodel);
                        }
                        shimmerFrameLayout2.stopShimmerAnimation();
                        shimmerFrameLayout2.setVisibility(View.GONE);

                        mAdapter3.notifyDataSetChanged();
                        if (mAdapter3.getItemCount() > 1) {
                            newlyrecycler.getLayoutManager().smoothScrollToPosition(newlyrecycler, null, mAdapter3.getItemCount() + 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void fetchIspiredThread() {

        String endPoint = EndPoints.INSPIRED;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);

                            String description = popularObj.getString("description");
                            Double ratting = popularObj.getDouble("ratting");
                            Integer votes = popularObj.getInt("votes");
                            Integer cost = popularObj.getInt("cost");
                            Boolean favorite = popularObj.getBoolean("favorite");
                            String image = popularObj.getString("image");
                            Inspired_model inspmodel= new Inspired_model(image,description,favorite,cost,votes,ratting);
                            InspArrayList.add(inspmodel);
                        }

                        shimmerFrameLayout3.stopShimmerAnimation();
                        shimmerFrameLayout3.setVisibility(View.GONE);
                        mAdapter4.notifyDataSetChanged();
                        if (mAdapter4.getItemCount() > 1) {
                            insprecycler.getLayoutManager().smoothScrollToPosition(insprecycler, null, mAdapter4.getItemCount() + 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
        shimmerFrameLayout2.startShimmerAnimation();
        shimmerFrameLayout3.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout2.stopShimmerAnimation();
        shimmerFrameLayout3.stopShimmerAnimation();
        super.onPause();
    }
}
