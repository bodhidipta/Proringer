package com.android.llc.proringer.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.main_content.DashBoard;
import com.android.llc.proringer.fragments.main_content.FavPros;
import com.android.llc.proringer.fragments.main_content.Message;
import com.android.llc.proringer.fragments.main_content.MyProjects;
import com.android.llc.proringer.fragments.postproject.PostProject;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;

public class LandScreenActivity extends AppCompatActivity {
    ImageView nav_toggle;
    private DrawerLayout mDrawer;
    private BottomNav bottomNavInstance = null;
    private NavigationHandler NavDrawerInstance = null;
    private Toolbar toolbar = null;
    private Toolbar back_toolbar = null;
    private ActionBarDrawerToggle toggle = null;
    private NavigationView nav_view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_toolbar = (Toolbar) findViewById(R.id.back_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view = (NavigationView) findViewById(R.id.nav_view);

        RelativeLayout cont_bottom = (RelativeLayout) findViewById(R.id.cont_bottom);
        bottomNavInstance = BottomNav.getInstance(LandScreenActivity.this, cont_bottom);
        NavDrawerInstance = NavigationHandler.getInstance(mDrawer);
        bottomNavInstance.init(new BottomNav.onSelectListener() {
            @Override
            public void onClick(String selected_tag) {
                Logger.prontMessage("Fragmnet_stack", "" + getSupportFragmentManager().getBackStackEntryCount());
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
        NavDrawerInstance.handleViewInput(new NavigationHandler.OnHandleInput() {
            @Override
            public void onClickItem() {

            }
        });

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.nav_toggle_icon, null));


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
        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        trasaction.replace(R.id.fragment_container, new DashBoard(), "" + DashBoard.class.getCanonicalName());
        trasaction.disallowAddToBackStack();
        trasaction.commit();

    }

    private void transactMyProjects() {
        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        trasaction.replace(R.id.fragment_container, new MyProjects(), "" + MyProjects.class.getCanonicalName());
        trasaction.disallowAddToBackStack();
        trasaction.commit();

    }

    private void transactFavPros() {
        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        trasaction.replace(R.id.fragment_container, new FavPros(), "" + FavPros.class.getCanonicalName());
        trasaction.disallowAddToBackStack();
        trasaction.commit();

    }

    private void transactMessages() {
        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        trasaction.replace(R.id.fragment_container, new Message(), "" + Message.class.getCanonicalName());
        trasaction.disallowAddToBackStack();
        trasaction.commit();

    }

    private void transactCreateProject() {
        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        trasaction.replace(R.id.fragment_container, new PostProject(), "" + PostProject.class.getCanonicalName());
        trasaction.disallowAddToBackStack();
        trasaction.commit();

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
            ((PostProject)getSupportFragmentManager().findFragmentByTag(PostProject.class.getCanonicalName())).performBack();


        }
        return super.onOptionsItemSelected(item);

    }
}
