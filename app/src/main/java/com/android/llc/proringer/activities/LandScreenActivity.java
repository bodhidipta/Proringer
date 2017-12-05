package com.android.llc.proringer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.fragments.bottomNav.DashBoardFragment;
import com.android.llc.proringer.fragments.bottomNav.FavProsFragment;
import com.android.llc.proringer.fragments.bottomNav.MessageFragment;
import com.android.llc.proringer.fragments.bottomNav.MyProjectsFragment;
import com.android.llc.proringer.fragments.bottomNav.PostProjectFragment;
import com.android.llc.proringer.fragments.drawerNav.HomeRemindersFragment;
import com.android.llc.proringer.fragments.drawerNav.InviteAFriendFragment;
import com.android.llc.proringer.fragments.drawerNav.LoginSettingsFragment;
import com.android.llc.proringer.fragments.drawerNav.NotificationsFragment;
import com.android.llc.proringer.fragments.drawerNav.SearchLocalProFragment;
import com.android.llc.proringer.fragments.drawerNav.UserInformationFragment;
import com.android.llc.proringer.fragments.main_content.MyProjectDetailsFragment;
import com.android.llc.proringer.fragments.main_content.MyProjectRateProFragment;
import com.android.llc.proringer.fragments.main_content.ProjectMessagingFragment;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class LandScreenActivity extends AppCompatActivity implements MyCustomAlertListener {

    ImageView nav_toggle;
    private DrawerLayout mDrawer;
    public BottomNav bottomNavInstance = null;

    public String local_pros_search_zip = "";

    private Toolbar toolbar = null;
    private Toolbar back_toolbar = null;
    private ActionBarDrawerToggle toggle = null;
    private FragmentManager fragmentManager = null;

    private InputMethodManager keyboard;

    MyLoader myLoader = null;
    NavigationHandler navigationHandler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Fabric.with(this, new Crashlytics());

        // TODO: Move this to where you establish a user session
        logUser();


        setContentView(R.layout.activity_land_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_toolbar = (Toolbar) findViewById(R.id.back_toolbar);

        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        myLoader = new MyLoader(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();


        /**
         * Bottom nav bar handling
         */
        RelativeLayout cont_bottom = (RelativeLayout) findViewById(R.id.cont_bottom);
        bottomNavInstance = BottomNav.getInstance(LandScreenActivity.this, cont_bottom);

        bottomNavInstance.init(new BottomNav.onSelectListener() {
            @Override
            public void onClick(String selected_tag) {
                Logger.printMessage("Fragment_stack", "" + getSupportFragmentManager().getBackStackEntryCount());
                switch (selected_tag) {
                    case BottomNav.DASHBOARD:
                        closeDrawer();
                        toggleProMapSearch(false);
                        transactDashBoard();
                        break;
                    case BottomNav.MY_PROJECTS:
                        closeDrawer();
                        toggleProMapSearch(false);
                        transactMyProjects();
                        break;
                    case BottomNav.MESSAGES:
                        closeDrawer();
                        toggleProMapSearch(false);
                        transactMessages();
                        break;
                    case BottomNav.FAV_PROS:
                        closeDrawer();
                        toggleProMapSearch(false);
                        transactFavPros();
                        break;
                    case BottomNav.CREATE_PROJECT:
                        closeDrawer();
                        toggleProMapSearch(false);
                        toggleToolBar(false);
                        transactCreateProject();
                        break;
                }
            }
        });


        /**
         * Handles view click for drawer layout
         */

        navigationHandler = NavigationHandler.getInstance();

        navigationHandler.init(mDrawer, new NavigationHandler.OnHandleInput() {
            @Override
            public void onClickItem(String tag) {
                switch (tag) {
                    case NavigationHandler.FIND_LOCAL_PROS:
                        closeDrawer();
                        toggleProMapSearch(true);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transactSearchLocalPros();
                        break;
                    case NavigationHandler.ACCOUNT:
                        toggleProMapSearch(false);
                        break;
                    case NavigationHandler.USER_INFORMATION:
                        closeDrawer();
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transactUserInformation();
                        break;
                    case NavigationHandler.LOGIN_SETTINGS:
                        closeDrawer();
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transactLoginSettings();
                        break;
                    case NavigationHandler.NOTIFICATION:
                        closeDrawer();
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transacNotifications();
                        break;
                    case NavigationHandler.HOME_SCHEDUL:
                        closeDrawer();
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transactHomeRemainder();
                        break;
                    case NavigationHandler.INVITE_FRIEND:
                        closeDrawer();
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transactInviteFriends();
                        break;

                    case NavigationHandler.SUPPORT:
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        break;
                    case NavigationHandler.ABOUT:
                        toggleProMapSearch(false);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        break;
                    case NavigationHandler.LOGOUT:
                        closeDrawer();

                        CustomAlert customAlert = new CustomAlert(LandScreenActivity.this, "", "Are you sure you want to log out?", LandScreenActivity.this);
                        customAlert.getListenerRetryCancelFromNormalAlert("Ok", "Cancel", 10);

                        break;

                    case NavigationHandler.Email_Support:
                        closeDrawer();
                        String[] TOSuppory = {"support@proringer.com"};
                        Uri uriSupport = Uri.parse("mailto:support@proringer.com")
                                .buildUpon()
                                .appendQueryParameter("subject", "Support")
                                .appendQueryParameter("body", "I think \n \n \n Proringer mobile app v1.0.1")
                                .build();
                        Intent emailSupportIntent = new Intent(Intent.ACTION_SENDTO, uriSupport);
                        emailSupportIntent.putExtra(Intent.EXTRA_EMAIL, TOSuppory);
                        startActivity(Intent.createChooser(emailSupportIntent, "Send mail..."));

                        break;

                    case NavigationHandler.Faq:
                        closeDrawer();
                        startActivity(new Intent(LandScreenActivity.this, FaqActivity.class));
                        break;

                    case NavigationHandler.Provider_Feedback:
                        closeDrawer();
                        String[] TOFeedback = {"feedback@proringer.com"};
                        Uri uriFeedback = Uri.parse("mailto:feedback@proringer.com")
                                .buildUpon()
                                .appendQueryParameter("subject", "Leave Feedback")
                                .appendQueryParameter("body", "I think \n \n \n Proringer mobile app v1.0.1")
                                .build();
                        Intent emailFeedbackIntent = new Intent(Intent.ACTION_SENDTO, uriFeedback);
                        emailFeedbackIntent.putExtra(Intent.EXTRA_EMAIL, TOFeedback);
                        startActivity(Intent.createChooser(emailFeedbackIntent, "Send mail..."));

                        break;

                    case NavigationHandler.Terms_Of_Service:
                        closeDrawer();
                        Intent intent_terms = new Intent(LandScreenActivity.this, TermsPrivacyActivity.class);
                        intent_terms.putExtra("value", "term");
                        startActivity(intent_terms);
                        break;

                    case NavigationHandler.Privacy_Policy:
                        closeDrawer();
                        Intent intent_policy = new Intent(LandScreenActivity.this, TermsPrivacyActivity.class);
                        intent_policy.putExtra("value", "policy");
                        startActivity(intent_policy);
                        break;
                }
            }
        });

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.nav_toggle_icon, null));


        /**
         * Toolbar icon of search local pro icon
         */
        findViewById(R.id.search_local_pro_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleProMapSearch(true);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.FIND_LOCAL_PROS);
                bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                transactSearchLocalPros();
            }
        });


        findViewById(R.id.img_search_local_pro_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleProMapSearch(true);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.FIND_LOCAL_PROS);
                bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                transactSearchLocalPros();
            }
        });


        if (ProApplication.getInstance().go_to.equalsIgnoreCase("dashboard")) {
            /**
             * place dashboard fragment when visit first time
             * also highlight bottom navigation selection
             */
            redirectToDashBoard();
        } else if (ProApplication.getInstance().go_to.equalsIgnoreCase("myProject")) {
            /**
             * place dashboard fragment when user edit my project
             * also highlight bottom navigation selection
             */
            redirectMyProject();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            CustomAlert customAlert = new CustomAlert(LandScreenActivity.this, "", "Do you want to exit application?", LandScreenActivity.this);
            customAlert.getListenerRetryCancelFromNormalAlert("Yes", "Abort", 1);
        }
    }

    /**
     * \
     * Fragment transaction of DashBoardFragment
     */
    private void transactDashBoard() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + DashBoardFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + DashBoardFragment.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + DashBoardFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + DashBoardFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new DashBoardFragment(), "" + DashBoardFragment.class.getCanonicalName());
        transaction.addToBackStack("" + DashBoardFragment.class.getCanonicalName());
        transaction.commit();
        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
    }

    /**
     * Fragment transaction for MyProject
     */
    public void transactMyProjects() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MyProjectsFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MyProjectsFragment.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MyProjectsFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MyProjectsFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new MyProjectsFragment(), "" + MyProjectsFragment.class.getCanonicalName());
        transaction.addToBackStack("" + MyProjectsFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }

    }


    /**
     * Fragment transaction for MyProject Details
     */
    public void transactMyProjectsDetails() {

        toggleToolBar(true);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MyProjectDetailsFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MyProjectDetailsFragment.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MyProjectDetailsFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MyProjectDetailsFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new MyProjectDetailsFragment(), "" + MyProjectDetailsFragment.class.getCanonicalName());
        transaction.addToBackStack("" + MyProjectDetailsFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }

    }


    /**
     * Fragment transaction for Notification
     */
    public void transacNotifications() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + NotificationsFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + NotificationsFragment.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + NotificationsFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + NotificationsFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new NotificationsFragment(), "" + NotificationsFragment.class.getCanonicalName());
        transaction.addToBackStack("" + NotificationsFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }

    }

    /**
     * Fragment transaction for Favorite Pros
     */
    private void transactFavPros() {
        toggleToolBar(false);
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + FavProsFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + FavProsFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + FavProsFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + FavProsFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new FavProsFragment(), "" + FavProsFragment.class.getCanonicalName());
        transaction.addToBackStack("" + FavProsFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Fragment transaction for Messages
     */
    private void transactMessages() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MessageFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MessageFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MessageFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MessageFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new MessageFragment(), "" + MessageFragment.class.getCanonicalName());
        transaction.addToBackStack("" + MessageFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Transact individual message from details message
     * flow is MessageFragment(main sectional fragment)->detailedMessage(fragment)
     */
    public void transactProjectMessaging(String project_id) {
        toggleToolBar(true);
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + ProjectMessagingFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + ProjectMessagingFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + ProjectMessagingFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + ProjectMessagingFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        Bundle bundle = new Bundle();
        bundle.putString("project_id", project_id);

        ProjectMessagingFragment projectMessagingFragment = new ProjectMessagingFragment();
        projectMessagingFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, projectMessagingFragment, "" + ProjectMessagingFragment.class.getCanonicalName());
        transaction.addToBackStack("" + ProjectMessagingFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Fragment transaction for Search Local Pros
     */
    public void transactSearchLocalPros() {
        local_pros_search_zip = "";
        toggleToolBar(false);
        toggleProMapSearch(true);
        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + SearchLocalProFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + SearchLocalProFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + SearchLocalProFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + SearchLocalProFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchLocalProFragment(), "" + SearchLocalProFragment.class.getCanonicalName());
        transaction.addToBackStack("" + SearchLocalProFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Fragment transaction for my projects rate pro
     */
    public void transactMyProjectRate() {
        toggleToolBar(true);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MyProjectRateProFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MyProjectRateProFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MyProjectRateProFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MyProjectRateProFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MyProjectRateProFragment(), "" + MyProjectRateProFragment.class.getCanonicalName());
        transaction.addToBackStack("" + MyProjectRateProFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Transact Login settings
     */
    public void transactLoginSettings() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + LoginSettingsFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + LoginSettingsFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + LoginSettingsFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + LoginSettingsFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginSettingsFragment(), "" + LoginSettingsFragment.class.getCanonicalName());
        transaction.addToBackStack("" + LoginSettingsFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Transact Invite a friends
     */
    public void transactInviteFriends() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + InviteAFriendFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + InviteAFriendFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + InviteAFriendFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + InviteAFriendFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new InviteAFriendFragment(), "" + InviteAFriendFragment.class.getCanonicalName());
        transaction.addToBackStack("" + InviteAFriendFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + fragmentManager.getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Transact User Information
     */
    public void transactUserInformation() {
        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + UserInformationFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + UserInformationFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + UserInformationFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + UserInformationFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new UserInformationFragment(), "" + UserInformationFragment.class.getCanonicalName());
        transaction.addToBackStack("" + UserInformationFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + fragmentManager.getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Transact Home remainder
     */
    public void transactHomeRemainder() {
        toggleToolBar(false);
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + HomeRemindersFragment.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + HomeRemindersFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + HomeRemindersFragment.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + HomeRemindersFragment.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeRemindersFragment(), "" + HomeRemindersFragment.class.getCanonicalName());
        transaction.addToBackStack("" + HomeRemindersFragment.class.getCanonicalName());
        transaction.commit();

        Logger.printMessage("Tag_frg", "" + fragmentManager.getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }


    /**
     * Transact post project activity
     */
    private void transactCreateProject() {
        startActivity(new Intent(LandScreenActivity.this, PostProjectActivity.class));
    }

    /**
     * Redirection to Dashboard fragment transaction
     */
    public void redirectToDashBoard() {
        bottomNavInstance.highLightSelected(BottomNav.DASHBOARD);
        transactDashBoard();
    }

    /**
     * Redirect to MyProjectList fragment transaction
     */

    public void redirectMyProject() {
        bottomNavInstance.highLightSelected(BottomNav.MY_PROJECTS);
        transactMyProjects();
    }

    /**
     * Toggle toolbar header for message details
     *
     * @param state
     */
    public void toggleToolBar(boolean state) {
        if (state) {
            back_toolbar.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            setSupportActionBar(back_toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
            back_toolbar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawer.addDrawerListener(toggle);
            toggle.syncState();
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.nav_toggle_icon, null));
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        }
    }

    public void performPostProject() {
        toggleToolBar(false);
        transactCreateProject();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (fragmentManager.findFragmentByTag(PostProjectFragment.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(PostProjectFragment.class.getCanonicalName()))
                ((PostProjectFragment) fragmentManager.findFragmentByTag(PostProjectFragment.class.getCanonicalName())).performBack();
            else if (fragmentManager.findFragmentByTag(ProjectMessagingFragment.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(ProjectMessagingFragment.class.getCanonicalName())) {
                toggleToolBar(false);
                transactMessages();
            } else if (fragmentManager.findFragmentByTag(MyProjectDetailsFragment.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(MyProjectDetailsFragment.class.getCanonicalName())) {
                toggleToolBar(false);
                transactMyProjects();
            } else if (fragmentManager.findFragmentByTag(MyProjectRateProFragment.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(MyProjectRateProFragment.class.getCanonicalName())) {
                toggleToolBar(false);
                transactMyProjects();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void closeDrawer() {
        closeKeypad();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mDrawer != null)
                    mDrawer.closeDrawer(GravityCompat.START);

                navigationHandler.closeAndResetSideMenuDesign();

            }
        });
    }

    private void toggleProMapSearch(boolean state) {
        if (!state) {
            findViewById(R.id.search_local_pro_header_map).setVisibility(View.GONE);
            findViewById(R.id.search_local_pro_header).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.search_local_pro_header_map).setVisibility(View.VISIBLE);
            findViewById(R.id.search_local_pro_header_map).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent searchIntent = new Intent(LandScreenActivity.this, SearchNearProActivity.class);
                    startActivityForResult(searchIntent, 41);

                }
            });
            findViewById(R.id.search_local_pro_header).setVisibility(View.GONE);
        }
    }

    public void closeKeypad() {
        try {
            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 41) {

            Bundle extras = data.getBundleExtra("data");
            if (extras != null) {
                local_pros_search_zip = extras.getString("searchZip");
                Logger.printMessage("local_pros_search_zip", "--->" + local_pros_search_zip);


                if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + SearchLocalProFragment.class.getCanonicalName()) != null) {
                    Logger.printMessage("back_stack", "Removed *****" + SearchLocalProFragment.class.getCanonicalName());
                    SearchLocalProFragment fragment = (SearchLocalProFragment) fragmentManager.findFragmentByTag("" + SearchLocalProFragment.class.getCanonicalName());
                    fragment.loadList();
                    Logger.printMessage("LoadListFromActivity", "YES");
                }

            }
        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("Yes") && i == 1) {
            finish();
        }

        if (result.equalsIgnoreCase("Ok") && i == 10) {

            ProServiceApiHelper.getInstance(LandScreenActivity.this).logOut(new ProServiceApiHelper.getApiProcessCallback() {
                @Override
                public void onStart() {
                    myLoader.showLoader();
                }

                @Override
                public void onComplete(String message) {
                    if (myLoader != null && myLoader.isMyLoaderShowing())
                        myLoader.dismissLoader();

                    ProApplication.getInstance().logOut();
                    ProApplication.getInstance().clearFBUserStatus();


                    /////////////////Facebook logout///////////session//
                    if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                    }
//                  /////////////end///////////////////////////////////


                    startActivity(new Intent(LandScreenActivity.this, GetStartedActivity.class));
                    finish();
                }

                @Override
                public void onError(String error) {

                    if (myLoader != null && myLoader.isMyLoaderShowing())
                        myLoader.dismissLoader();
                }
            });
        }
    }


    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }
}
