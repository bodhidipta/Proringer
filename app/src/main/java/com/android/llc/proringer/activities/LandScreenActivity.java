package com.android.llc.proringer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.fragments.bottomNav.DashBoard;
import com.android.llc.proringer.fragments.bottomNav.FavPros;
import com.android.llc.proringer.fragments.drawerNav.HomeReminders;
import com.android.llc.proringer.fragments.drawerNav.InviteAfriend;
import com.android.llc.proringer.fragments.drawerNav.LoginSettings;
import com.android.llc.proringer.fragments.drawerNav.Notifications;
import com.android.llc.proringer.fragments.drawerNav.SearchLocalPro;
import com.android.llc.proringer.fragments.bottomNav.Message;
import com.android.llc.proringer.fragments.bottomNav.MyProjects;
import com.android.llc.proringer.fragments.bottomNav.PostProject;
import com.android.llc.proringer.fragments.drawerNav.UserInfromation;
import com.android.llc.proringer.fragments.main_content.ProjectMessaging;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;

public class LandScreenActivity extends AppCompatActivity {
    ImageView nav_toggle;
    private DrawerLayout mDrawer;
    private BottomNav bottomNavInstance = null;

    private Toolbar toolbar = null;
    private Toolbar back_toolbar = null;
    private ActionBarDrawerToggle toggle = null;
    private FragmentManager fragmentManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_toolbar = (Toolbar) findViewById(R.id.back_toolbar);

        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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
                Logger.printMessage("Fragmnet_stack", "" + getSupportFragmentManager().getBackStackEntryCount());
                switch (selected_tag) {
                    case BottomNav.DASHBOARD:
                        transactDashBoard();
                        break;
                    case BottomNav.MY_PROJECTS:
                        transactMyProjects();
                        break;
                    case BottomNav.MESSAGES:
                        transactMessages();
                        break;
                    case BottomNav.FAV_PROS:
                        transactFavPros();
                        break;
                    case BottomNav.CREATE_PROJECT:
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
                        transactSearchLocalPros();
                        break;
                    case NavigationHandler.USER_INFORMATION:
                        closeDrawer();
                        transactUserInformation();
                        break;
                    case NavigationHandler.LOGIN_SETTINGS:
                        closeDrawer();
                        transactLoginSettings();
                        break;
                    case NavigationHandler.NOTIFICATION:
                        closeDrawer();
                        transacNotifications();
                        break;
                    case NavigationHandler.HOME_SCHEDUL:
                        closeDrawer();
                        transactHomeRemainder();
                        break;
                    case NavigationHandler.INVITE_FRIEND:
                        closeDrawer();
                        transactInviteFriends();
                        break;
                    case NavigationHandler.SUPPORT:
                        break;
                    case NavigationHandler.ABOUT:
                        break;
                    case NavigationHandler.LOGOUT:
                        closeDrawer();
                        ProApplication.getInstance().logOut();
                        startActivity(new Intent(LandScreenActivity.this, GetStarted.class));
                        finish();
                        break;
                }
            }
        });

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.nav_toggle_icon, null));

        findViewById(R.id.search_local_pro_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.getInstance().highlightTag(NavigationHandler.FIND_LOCAL_PROS);
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

    private void transactMyProjects() {

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


    private void transacNotifications() {

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
     * Transact Local pro list
     */
    public void transactSearchLocalPros() {

        toggleToolBar(false);

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

        if (fragmentManager.getBackStackEntryCount() > 0 && fragmentManager.findFragmentByTag("" + UserInfromation.class.getCanonicalName()) != null) {
            Logger.printMessage("back_stack", "Removed *****" + UserInfromation.class.getCanonicalName());
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("" + UserInfromation.class.getCanonicalName())).commit();
            fragmentManager.popBackStack("" + UserInfromation.class.getCanonicalName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new UserInfromation(), "" + UserInfromation.class.getCanonicalName());
        transaction.addToBackStack("" + UserInfromation.class.getCanonicalName());
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
        startActivity(new Intent(LandScreenActivity.this, ActivityPostProject.class));
    }

    public void redirectToDashBoard() {
        bottomNavInstance.highLightSelected(BottomNav.DASHBOARD);
        transactDashBoard();
    }

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
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void closeDrawer() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mDrawer != null)
                    mDrawer.closeDrawer(GravityCompat.START);
            }
        });
    }

}
