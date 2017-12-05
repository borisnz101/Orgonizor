package ca.uottawa.exception.orgonizor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Dialog myDialog;
    boolean loggedIn = false;
    private User logged;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private DBHandler db;
    private HashMap<String, Integer> priorite;
    StorageUnit fridge = new StorageUnit(1);
    StorageUnit cup = new StorageUnit(2);
    StorageUnit broomC = new StorageUnit(3);
    StorageUnit material = new StorageUnit(4);
    StorageUnit groce = new StorageUnit(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        db = new DBHandler(this);
        priorite = new HashMap<String, Integer>();
        priorite.put("URGENT", 3);
        priorite.put("IMPORTANT", 2);
        priorite.put("NORMAL", 1);
        priorite.put("NOT_URGENT", 0);

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        final GridLayout fridGrid = (GridLayout) findViewById(R.id.FridgeGrid);
        final GridLayout cupGrid = (GridLayout) findViewById(R.id.CupGrid);
        final GridLayout broomCGrid = (GridLayout) findViewById(R.id.BroomCGrid);
        final GridLayout materialGrid = (GridLayout) findViewById(R.id.MaterialGrid);
        final GridLayout groceGrid = (GridLayout) findViewById(R.id.GroceGrid);


        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
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

        findViewById(R.id.addtask).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //addTaskToView(new Task(null, null, "", "", 0, null, 0, "Clean garage", "", 0, ""));
                callAddTask(true);
            }
        });
        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addTaskToView(new Task(null, null, "", "", 0, null, 0, "Clean garage", "", 0, ""));
                //callAddTask(true);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(fridGrid);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(cupGrid);
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(broomCGrid);
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(materialGrid);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(groceGrid);
            }
        });

        /*findViewById(R.id.TaskTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callAddTask(true); // Maybe we should use the same with Task argument?
            }
        });*/
        findViewById(R.id.switch1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideTask(User);
                // TODO implement hideTask
            }
        });

        //uncomment to delete the database for testing
        //db.onUpgrade(db.getWritableDatabase(),0,0);

        //add all tasks from the db to the view
        ArrayList<Task> tasks = db.getTasks();

        for(Task task : tasks){
            System.out.println("ID: " + task.getId());
            if(task.getStatus() != Task.Status.COMPLETED.getValue()) {
                addTaskToView(task);
            }
        }

        if (!db.usersExist()) {
            callRegisterDialog(true, false);
        } else if (!loggedIn) {
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

    private void addTaskToView(Task task){
        final LinearLayout list = (LinearLayout) findViewById(R.id.taskList);
        final View layout2 = LayoutInflater.from(this).inflate(R.layout.task_entry, list, false);

        TextView title = (TextView) layout2.findViewById(R.id.TaskTitle);
        title.setText(task.getTitle());

        TextView id = (TextView) layout2.findViewById(R.id.taskid);
        id.setText(task.getId()+"");

        final CheckBox chk = (CheckBox) layout2.findViewById(R.id.taskComplete);

        chk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView id = (TextView) layout2.findViewById(R.id.taskid);
                int iden = Integer.parseInt(id.getText().toString());
                if(logged.getIsParent() || db.getTask(iden).getAssignedTo().getId() == logged.getId()) {
                    db.removeTask(iden);
                    list.removeView(layout2);
                    Toast.makeText(getApplication().getBaseContext(), R.string.taskcomplete, Toast.LENGTH_SHORT).show();
                }else{
                    chk.setChecked(false);
                    Toast.makeText(getApplication().getBaseContext(), R.string.tasknotremovednoperm, Toast.LENGTH_SHORT).show();
                }
            }
        });
        list.addView(layout2);
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

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your login calculation goes here
                System.out.println(user.getText() + " " + password.getText());
                //if(user.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                User usere = db.authenticateUser(user.getText().toString(), password.getText().toString());
                if (usere != null) {
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

        final Spinner assignee= (Spinner) myDialog.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.getUsers());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        assignee.setAdapter(spinnerArrayAdapter);
        final Spinner priority= (Spinner) myDialog.findViewById(R.id.spinner2);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.priority));
        priority.setAdapter(spinnerCountShoesArrayAdapter);

        final EditText due = (EditText) myDialog.findViewById(R.id.dueDate);

        Button submit = (Button) myDialog.findViewById(R.id.addtaskbutton);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String taskAssignee = assignee.getSelectedItem().toString();
                String taskPriority = priority.getSelectedItem().toString();
                String taskTitle = ((EditText)myDialog.findViewById(R.id.editText)).getText().toString();
                String taskDescription = ((EditText)myDialog.findViewById(R.id.editText2)).getText().toString();
                String taskDuration = ((EditText)myDialog.findViewById(R.id.editText4)).getText().toString();
                String taskReward = ((EditText)myDialog.findViewById(R.id.editText7)).getText().toString();
                int status = 1;
                User use;
                if(taskAssignee.equals("Unassigned")){
                    status = 0;
                    use = null;
                }else{
                    use = db.getUser(taskAssignee);
                }
                Task task = new Task(use, logged, due.getText().toString(), taskDuration, priorite.get(taskPriority), new StorageUnit(1), status, taskTitle, taskDescription, -1, taskReward);
                task = db.addTask(task);
                addTaskToView(task);
                myDialog.dismiss();
            }
        });


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                due.setText(sdf.format(myCalendar.getTime()));
            }

        };
        due.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        myDialog.show();

    }

    private void callAddItem(final GridLayout grid) {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.additem_popup);
        myDialog.show();

        Button ok = (Button) myDialog.findViewById(R.id.buttonOK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editT = (EditText) myDialog.findViewById(R.id.editText);
                String itemName = editT.getText().toString();
                CheckBox itemAdded = new CheckBox(MainActivity.this);
                if (itemAdded.isChecked()) {
                    switch(grid.toString()){
                        case "fridGrid":
                            StoredItem fridItem= new StoredItem(itemName, fridge);
                            fridge.addStoredItem(fridItem);
                        case "cupGrid":
                            StoredItem cupItem= new StoredItem(itemName, cup);
                            fridge.addStoredItem(cupItem);
                        case "broomCGrid":
                            StoredItem broomCItem= new StoredItem(itemName, broomC);
                            fridge.addStoredItem(broomCItem);
                        case "materialGrid":
                            StoredItem materialItem= new StoredItem(itemName, material);
                            fridge.addStoredItem(materialItem);
                        case "groceGrid":
                            StoredItem groceItem= new StoredItem(itemName, groce);
                            fridge.addStoredItem(groceItem);
                    }
                }
                itemAdded.setText(itemName);
                grid.addView(itemAdded);
                myDialog.dismiss();
            }
        });
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
        if (firstUser) {
            layoutAdmin.setVisibility(View.INVISIBLE);
        }
        message.setVisibility(View.INVISIBLE);

        myDialog.show();

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your login calculation goes here
                System.out.println(user.getText() + " " + password.getText() + " " + passconfirm.getText());
                //first check that there is a username
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(user.getText().toString());
                boolean b = m.find();
                m = p.matcher(name.getText().toString());
                boolean c = m.find();
                if (user.getText().toString().length() < 1) {
                    message.setText("Username must have a greater length than 0!");
                    message.setVisibility(View.VISIBLE);
                } else if (name.getText().toString().length() < 1) {
                    message.setText("Name must have a greater length than 0!");
                    message.setVisibility(View.VISIBLE);
                } else if (b) {
                    message.setText("Username can not contain special characters!");
                    message.setVisibility(View.VISIBLE);
                } else if (!password.getText().toString().equals(passconfirm.getText().toString())) {
                    message.setText("Passwords do not match!");
                    message.setVisibility(View.VISIBLE);
                } else if (c) {
                    message.setText("Name can not contain special characters!");
                    message.setVisibility(View.VISIBLE);
                } else {
                    //no errors, we are good to add this user to the DB
                    //the DBHandler takes care of hashing for us, we don't have to worry
                    CheckBox admin = (CheckBox) myDialog.findViewById(R.id.checkBox29);
                    int accessLevel = admin.isChecked() ? 1 : 0;
                    db.addUser(user.getText().toString(), password.getText().toString(), name.getText().toString(), accessLevel); //access level determined by the little check box

                    //code to autoLogin if this is the first time someone registers
                    if (firstUser) {
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
