package ca.uottawa.exception.orgonizor;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Dialog myDialog;
    boolean loggedIn = false;
    private User logged;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        db = new DBHandler(this);

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabHost tabs = (TabHost)findViewById(R.id.tabhost);
        tabs.setup();

        // Shopping
        TabHost.TabSpec calculatorTab = tabs.newTabSpec("Shopping");
        calculatorTab.setContent(R.id.tab3);
        calculatorTab.setIndicator("Shopping");
        tabs.addTab(calculatorTab);

        // Tasks
        TabHost.TabSpec homeTab = tabs.newTabSpec("Tasks");
        homeTab.setContent(R.id.tab2);
        homeTab.setIndicator("Tasks");
        tabs.addTab(homeTab);

        // Storage
        TabHost.TabSpec store = tabs.newTabSpec("Storage");
        store.setContent(R.id.tab1);
        store.setIndicator("Storage");
        tabs.addTab(store);

        findViewById(R.id.addtask).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                callAddTask(true);
            }
        });
        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                callAddTask(true);
            }
        });
        //uncomment to delete the database for testing
        //db.onUpgrade(db.getWritableDatabase(),0,0);
        if(!db.usersExist()){
           callRegisterDialog(true, false);
        }else if(!loggedIn) {
            callLoginDialog(false);
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void callLoginDialog(boolean cancelable) {
        final EditText user;
        final EditText password;
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.activity_login_pop_up);
        myDialog.setCancelable(cancelable);
        Button login = (Button) myDialog.findViewById(R.id.login);

        user = (EditText) myDialog.findViewById(R.id.username);
        password = (EditText) myDialog.findViewById(R.id.password);
        myDialog.show();

        login.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //your login calculation goes here
                System.out.println(user.getText() + " " + password.getText());
                //if(user.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                User usere = db.authenticateUser(user.getText().toString(), password.getText().toString());
                 if(usere != null){
                     logged = usere;
                     loggedIn = true;
                     TextView nameField = (TextView) mDrawerLayout.findViewById(R.id.nameField);
                     nameField.setText(usere.getName());
                     myDialog.dismiss();
                }
            }
        });


    }

    private void callAddTask(boolean cancelable) {
        final EditText user;
        final EditText password;
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.addtask_popup);
        myDialog.setCancelable(cancelable);
        Button login = (Button) myDialog.findViewById(R.id.login);

        user = (EditText) myDialog.findViewById(R.id.username);
        password = (EditText) myDialog.findViewById(R.id.password);
        myDialog.show();


    }

    //Firstuser is used to know if we should bother showing an admin checkbox, if there is no other admins this one must be an admin
    private void callRegisterDialog(final boolean firstUser, boolean cancelable) {
        final EditText user;
        final EditText password;
        final EditText passconfirm;
        final EditText name;
        final TextView message;
        final LinearLayout layoutAdmin;
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.registration_popup);
        myDialog.setCancelable(cancelable);
        Button register = (Button) myDialog.findViewById(R.id.register);

        user = (EditText) myDialog.findViewById(R.id.username);
        password = (EditText) myDialog.findViewById(R.id.password);
        passconfirm = (EditText) myDialog.findViewById(R.id.confirmpass);
        message = (TextView) myDialog.findViewById(R.id.alert);
        name = (EditText) myDialog.findViewById(R.id.name);
        layoutAdmin = (LinearLayout) myDialog.findViewById(R.id.adminField);
        if(firstUser) {
            layoutAdmin.setVisibility(View.INVISIBLE);
        }
        message.setVisibility(View.INVISIBLE);

        myDialog.show();

        register.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //your login calculation goes here
                System.out.println(user.getText() + " " + password.getText() + " " + passconfirm.getText());
                //first check that there is a username
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(user.getText().toString());
                boolean b = m.find();
                m = p.matcher(name.getText().toString());
                boolean c = m.find();
                if(user.getText().toString().length() < 1) {
                    message.setText("Username must have a greater length than 0!");
                    message.setVisibility(View.VISIBLE);
                }else if(name.getText().toString().length() < 1){
                    message.setText("Name must have a greater length than 0!");
                    message.setVisibility(View.VISIBLE);
                }else if(b){
                    message.setText("Username can not contain special characters!");
                    message.setVisibility(View.VISIBLE);
                }else if(!password.getText().toString().equals(passconfirm.getText().toString())) {
                    message.setText("Passwords do not match!");
                    message.setVisibility(View.VISIBLE);
                }else if(c){
                    message.setText("Name can not contain special characters!");
                    message.setVisibility(View.VISIBLE);
                }else{
                    //no errors, we are good to add this user to the DB
                    //the DBHandler takes care of hashing for us, we don't have to worry
                    db.addUser(user.getText().toString(), password.getText().toString(), name.getText().toString(), 1); //access level 1 is admin

                    //code to autoLogin if this is the first time someone registers
                    if(firstUser) {
                        logged = db.authenticateUser(user.getText().toString(), password.getText().toString());
                        TextView nameField = (TextView) mDrawerLayout.findViewById(R.id.nameField);
                        nameField.setText(name.getText().toString());
                    }

                    myDialog.dismiss();
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.registernew: {
                callRegisterDialog(false, true);
                break;
            }

            case R.id.switche: {
                callLoginDialog(true);
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
