package com.android.llc.proringer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.fragments.bottomNav.DashBoard;
import com.android.llc.proringer.fragments.bottomNav.FavPros;
import com.android.llc.proringer.fragments.bottomNav.Message;
import com.android.llc.proringer.fragments.bottomNav.MyProjects;
import com.android.llc.proringer.fragments.bottomNav.PostProject;
import com.android.llc.proringer.fragments.drawerNav.HomeReminders;
import com.android.llc.proringer.fragments.drawerNav.InviteAfriend;
import com.android.llc.proringer.fragments.drawerNav.LoginSettings;
import com.android.llc.proringer.fragments.drawerNav.Notifications;
import com.android.llc.proringer.fragments.drawerNav.SearchLocalPro;
import com.android.llc.proringer.fragments.drawerNav.UserInformation;
import com.android.llc.proringer.fragments.main_content.MyProjectDetails;
import com.android.llc.proringer.fragments.main_content.MyProjectRatePro;
import com.android.llc.proringer.fragments.main_content.ProjectMessaging;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;

public class LandScreenActivity extends AppCompatActivity{

    ImageView nav_toggle;
    private DrawerLayout mDrawer;
    public BottomNav bottomNavInstance = null;

    private Toolbar toolbar = null;
    private Toolbar back_toolbar = null;
    private ActionBarDrawerToggle toggle = null;
    private FragmentManager fragmentManager = null;

    private InputMethodManager keyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_toolbar = (Toolbar) findViewById(R.id.back_toolbar);

        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

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
        NavigationHandler.getInstance().init(mDrawer, new NavigationHandler.OnHandleInput() {
            @Override
            public void onClickItem(String tag) {
                switch (tag) {
                    case NavigationHandler.FIND_LOCAL_PROS:
                        closeDrawer();
                        toggleProMapSearch(true);
                        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                        transactSearchLocalPros();
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
                        ProApplication.getInstance().logOut();
                        startActivity(new Intent(LandScreenActivity.this, GetStartedActivity.class));
                        finish();
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

        /**
         * place dashboard fragment when visit first time
         * also highlight bottom navigation selection
         */
        redirectToDashBoard();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(LandScreenActivity.this)
                    .setMessage("Do you want to exit application?")
                    .setPositiveButton("Abort", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).show();
        }
    }

    /**
     * \
     * Fragment transaction of DashBoard
     */
    private void transactDashBoard() {

        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + DashBoard.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + DashBoard.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + DashBoard.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + DashBoard.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction trasaction = fragmentManager.beginTransaction();
        trasaction.replace(R.id.fragment_container, new DashBoard(), "" + DashBoard.class.getCanonicalName());
        trasaction.addToBackStack("" + DashBoard.class.getCanonicalName());
        trasaction.commit();
        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
    }

    /**
     * Fragment transaction for MyProject
     */
    public void transactMyProjects() {

        toggleToolBar(false);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MyProjects.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MyProjects.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MyProjects.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MyProjects.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction trasaction = fragmentManager.beginTransaction();
        trasaction.replace(R.id.fragment_container, new MyProjects(), "" + MyProjects.class.getCanonicalName());
        trasaction.addToBackStack("" + MyProjects.class.getCanonicalName());
        trasaction.commit();

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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MyProjectDetails.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MyProjectDetails.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MyProjectDetails.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MyProjectDetails.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction trasaction = fragmentManager.beginTransaction();
        trasaction.replace(R.id.fragment_container, new MyProjectDetails(), "" + MyProjectDetails.class.getCanonicalName());
        trasaction.addToBackStack("" + MyProjectDetails.class.getCanonicalName());
        trasaction.commit();

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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + Notifications.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + Notifications.class.getCanonicalName());

            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + Notifications.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + Notifications.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction trasaction = fragmentManager.beginTransaction();
        trasaction.replace(R.id.fragment_container, new Notifications(), "" + Notifications.class.getCanonicalName());
        trasaction.addToBackStack("" + Notifications.class.getCanonicalName());
        trasaction.commit();

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
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + FavPros.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + FavPros.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + FavPros.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + FavPros.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction trasaction = fragmentManager.beginTransaction();
        trasaction.replace(R.id.fragment_container, new FavPros(), "" + FavPros.class.getCanonicalName());
        trasaction.addToBackStack("" + FavPros.class.getCanonicalName());
        trasaction.commit();

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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + Message.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + Message.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + Message.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + Message.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction trasaction = fragmentManager.beginTransaction();
        trasaction.replace(R.id.fragment_container, new Message(), "" + Message.class.getCanonicalName());
        trasaction.addToBackStack("" + Message.class.getCanonicalName());
        trasaction.commit();

        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            Logger.printMessage("back_stack", "" + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }
    }

    /**
     * Transact individual message from details message
     * flow is Message(main sectional fragment)->detailedMessage(fragment)
     */
    public void transactProjectMessaging() {
        toggleToolBar(true);
        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + ProjectMessaging.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + ProjectMessaging.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + ProjectMessaging.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + ProjectMessaging.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ProjectMessaging(), "" + ProjectMessaging.class.getCanonicalName());
        transaction.addToBackStack("" + ProjectMessaging.class.getCanonicalName());
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

        toggleToolBar(false);
        toggleProMapSearch(true);
        bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + SearchLocalPro.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + SearchLocalPro.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + SearchLocalPro.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + SearchLocalPro.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchLocalPro(), "" + SearchLocalPro.class.getCanonicalName());
        transaction.addToBackStack("" + SearchLocalPro.class.getCanonicalName());
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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + MyProjectRatePro.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + MyProjectRatePro.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + MyProjectRatePro.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + MyProjectRatePro.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MyProjectRatePro(), "" + MyProjectRatePro.class.getCanonicalName());
        transaction.addToBackStack("" + MyProjectRatePro.class.getCanonicalName());
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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + LoginSettings.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + LoginSettings.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + LoginSettings.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + LoginSettings.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginSettings(), "" + LoginSettings.class.getCanonicalName());
        transaction.addToBackStack("" + LoginSettings.class.getCanonicalName());
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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + InviteAfriend.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + InviteAfriend.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + InviteAfriend.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + InviteAfriend.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new InviteAfriend(), "" + InviteAfriend.class.getCanonicalName());
        transaction.addToBackStack("" + InviteAfriend.class.getCanonicalName());
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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + UserInformation.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + UserInformation.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + UserInformation.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + UserInformation.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new UserInformation(), "" + UserInformation.class.getCanonicalName());
        transaction.addToBackStack("" + UserInformation.class.getCanonicalName());
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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + HomeReminders.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + HomeReminders.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + HomeReminders.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + HomeReminders.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeReminders(), "" + HomeReminders.class.getCanonicalName());
        transaction.addToBackStack("" + HomeReminders.class.getCanonicalName());
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
            if (fragmentManager.findFragmentByTag(PostProject.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(PostProject.class.getCanonicalName()))
                ((PostProject) fragmentManager.findFragmentByTag(PostProject.class.getCanonicalName())).performBack();
            else if (fragmentManager.findFragmentByTag(ProjectMessaging.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(ProjectMessaging.class.getCanonicalName())) {
                toggleToolBar(false);
                transactMessages();
            } else if (fragmentManager.findFragmentByTag(MyProjectDetails.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(MyProjectDetails.class.getCanonicalName())) {
                toggleToolBar(false);
                transactMyProjects();
            } else if (fragmentManager.findFragmentByTag(MyProjectRatePro.class.getCanonicalName()) != null && fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName().equals(MyProjectRatePro.class.getCanonicalName())) {
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
            }
        });
    }

    private void toggleProMapSearch(boolean state) {
        if (!state) {
            findViewById(R.id.search_local_pro_header_map).setVisibility(View.GONE);
            findViewById(R.id.search_local_pro_header).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.search_local_pro_header_map).setVisibility(View.VISIBLE);
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

}
