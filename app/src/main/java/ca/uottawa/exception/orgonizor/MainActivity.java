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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    private HashMap<Integer, String> priorities;
    private HashMap<Integer, String> statuses;
    GridLayout foodGrid;
    GridLayout broomCGrid;
    GridLayout materialGrid;
    GridLayout groceGrid;
    CheckBox itemAdded;
    StorageUnit food = new StorageUnit(1);
    StorageUnit broomC = new StorageUnit(2);
    StorageUnit material = new StorageUnit(3);
    StorageUnit groce = new StorageUnit(4);

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
        priorities = new HashMap<Integer, String>();
        priorities.put(3, "URGENT");
        priorities.put(2, "IMPORTANT");
        priorities.put(1, "NORMAL");
        priorities.put(0, "NOT_URGENT");
        statuses = new HashMap<Integer, String>();
        statuses.put(2, "COMPLETED");
        statuses.put(1, "ASSIGNED");
        statuses.put(0, "NOT_ASSIGNED");

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        foodGrid =(GridLayout) findViewById(R.id.FoodGrid);
        broomCGrid = (GridLayout) findViewById(R.id.BroomCGrid);
        materialGrid = (GridLayout) findViewById(R.id.MaterialGrid);
        groceGrid = (GridLayout) findViewById(R.id.GroceGrid);


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
                if(logged.getIsParent()) {
                    callAddTask(true);
                }
            }
        });
        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callAddTask(true);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(foodGrid, "foodGrid");
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(broomCGrid, "broomCGrid");
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(materialGrid, "materialGrid");
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddItem(groceGrid, "groceGrid");
            }
        });

        findViewById(R.id.Filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFilter();
            }
        });

        //uncomment to delete the database for testing
        //db.onUpgrade(db.getWritableDatabase(),0,0);

        //add all tasks from the db to the view
        ArrayList<Task> tasks = db.getTasks();

        for(Task task : tasks){
            System.out.println("ID: " + task.getId());
            if(task.getStatus() != Task.Status.COMPLETED.getValue()) {
                addTaskToView(task, false);
            }
        }

        generateStorage();

        if (!db.usersExist()) {
            callRegisterDialog(true, false);
        } else if (!loggedIn) {
            callLoginDialog(false);
        }
    }

    private void generateStorage(){
        if(foodGrid.getChildCount() > 0)
            foodGrid.removeAllViews();
        List<StoredItem> itemsFridge = db.getStorageUnit(1).getStoredItems();
        for(StoredItem item : itemsFridge){
            addItemToView(foodGrid, item, 1);
        }

        if(broomCGrid.getChildCount() > 0)
            broomCGrid.removeAllViews();
        List<StoredItem> itemsBroom = db.getStorageUnit(2).getStoredItems();
        for(StoredItem item : itemsBroom){
            addItemToView(broomCGrid, item, 2);
        }

        if(materialGrid.getChildCount() > 0)
            materialGrid.removeAllViews();
        List<StoredItem> itemsMaterial = db.getStorageUnit(3).getStoredItems();
        for(StoredItem item : itemsMaterial){
            addItemToView(materialGrid, item, 3);
        }

        if(groceGrid.getChildCount() > 0)
            groceGrid.removeAllViews();
        List<StoredItem> itemsGroce = db.getStorageUnit(4).getStoredItems();
        for(StoredItem item : itemsGroce){
            addItemToView(groceGrid, item, 4);
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

    private void addItemToView(final GridLayout grid, final StoredItem item, final int storage){
        final CheckBox newItem = new CheckBox(MainActivity.this);
        newItem.setText(item.getName());
        newItem.setChecked(true);
        grid.addView(newItem);

        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoredItem foodItem= new StoredItem(item.getName());
                StoredItem broomCItem= new StoredItem(item.getName());
                StoredItem materialItem= new StoredItem(item.getName());
                StoredItem groceItem= new StoredItem(item.getName());
                switch(storage){
                    case 1:
                        if (newItem.isChecked()) {
                            db.addItem(foodItem, food);
                            db.removeItem(foodItem, groce);
                        }
                        else {
                            db.removeItem(foodItem, food);
                            db.addItem(foodItem, groce);
                        }
                        break;
                    case 2:
                        if (newItem.isChecked()) {
                            db.addItem(broomCItem, broomC);
                            db.removeItem(broomCItem, material);
                        }
                        else {
                            db.removeItem(broomCItem, broomC);
                            db.addItem(broomCItem, material);
                        }
                        break;
                    case 3:
                        if (newItem.isChecked()) {
                            db.addItem(materialItem, material);
                            db.removeItem(materialItem, broomC);
                        }
                        else {
                            db.removeItem(materialItem, material);
                            db.addItem(materialItem, broomC);
                        }
                        break;
                    case 4:
                        if (newItem.isChecked()) {
                            db.addItem(groceItem, groce);
                            db.removeItem(groceItem, food);
                        }
                        else {
                            db.removeItem(groceItem, groce);
                            db.addItem(groceItem, food);
                        }
                        break;
                }
                generateStorage();
            }
        });

    }

    private void addTaskToView(Task task, boolean checked){
        final LinearLayout list = (LinearLayout) findViewById(R.id.taskList);
        final View layout2 = LayoutInflater.from(this).inflate(R.layout.task_entry, list, false);

        TextView title = (TextView) layout2.findViewById(R.id.TaskTitle);
        title.setText(task.getTitle());

        TextView id = (TextView) layout2.findViewById(R.id.taskid);
        id.setText(task.getId()+"");

        final CheckBox chk = (CheckBox) layout2.findViewById(R.id.taskComplete);
        chk.setChecked(checked);

        if(!checked) {
            chk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TextView id = (TextView) layout2.findViewById(R.id.taskid);
                    int iden = Integer.parseInt(id.getText().toString());
                    if (logged.getIsParent() || db.getTask(iden).getAssignedTo().getId() == logged.getId()) {
                        db.removeTask(iden);
                        Task task = db.getTask(iden);
                        int points = logged.getPoints() + task.getReward();
                        logged.setPoints(points);
                        db.updateUserPoints(logged, points);
                        TextView pointField = mDrawerLayout.findViewById(R.id.points);
                        pointField.setText(String.valueOf(logged.getPoints()));
                        list.removeView(layout2);
                        String done = getString(R.string.taskcomplete, task.getReward());
                        Toast.makeText(getApplication().getBaseContext(), done, Toast.LENGTH_SHORT).show();
                    } else {
                        chk.setChecked(false);
                        Toast.makeText(getApplication().getBaseContext(), R.string.tasknotremovednoperm, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            chk.setClickable(false);
        }

        layout2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView id = (TextView) layout2.findViewById(R.id.taskid);
                int iden = Integer.parseInt(id.getText().toString());
                Task task = db.getTask(iden);
                if(logged.getId() == task.getCreator().getId() || logged.getIsParent()){
                    callEditTask(true, task);
                }else {
                    callViewTask(true, task);
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
                    TextView pointField = mDrawerLayout.findViewById(R.id.points);
                    pointField.setText(String.valueOf(usere.getPoints()));
                    myDialog.dismiss();
                }
            }
        });

    }

    private void callViewTask(boolean cancelable, Task task) {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.view_task_popup);
        myDialog.setCancelable(cancelable);

        EditText taskTitle = myDialog.findViewById(R.id.titlev);
        taskTitle.setText(task.getTitle());
        taskTitle.setFocusable(false);
        EditText taskDescription = myDialog.findViewById(R.id.description);
        taskDescription.setText(task.getDescription());
        taskDescription.setFocusable(false);
        EditText taskDue = myDialog.findViewById(R.id.dueDate);
        taskDue.setText(task.getDue());
        taskDue.setFocusable(false);
        EditText taskDuration = myDialog.findViewById(R.id.duration);
        taskDuration.setText(task.getDuration());
        taskDuration.setFocusable(false);
        EditText taskTools = myDialog.findViewById(R.id.tools);
        taskTools.setText("");
        taskTools.setFocusable(false);
        EditText taskReward = myDialog.findViewById(R.id.reward);
        taskReward.setText(String.valueOf(task.getReward()));
        taskReward.setFocusable(false);
        EditText taskAssignee = myDialog.findViewById(R.id.assignee);
        taskAssignee.setText(task.getAssignedTo().getUsername());
        taskAssignee.setFocusable(false);
        EditText taskCreator = myDialog.findViewById(R.id.creator);
        taskCreator.setText(task.getCreator().getUsername());
        taskCreator.setFocusable(false);
        EditText taskPriority = myDialog.findViewById(R.id.priority);
        taskPriority.setText(priorities.get(task.getPriority()));
        taskPriority.setFocusable(false);
        EditText taskStatus = myDialog.findViewById(R.id.status);
        taskStatus.setText(statuses.get(task.getStatus()));
        taskStatus.setFocusable(false);

        Button close = myDialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    private void callEditTask(boolean cancelable, final Task task) {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.edit_task_popup);
        myDialog.setCancelable(cancelable);

        EditText taskTitle = myDialog.findViewById(R.id.titlev);
        taskTitle.setText(task.getTitle());
        taskTitle.setFocusable(false);
        EditText taskDescription = myDialog.findViewById(R.id.description);
        taskDescription.setText(task.getDescription());
        final EditText due = myDialog.findViewById(R.id.dueDate);
        due.setText(task.getDue());
        EditText taskDuration = myDialog.findViewById(R.id.duration);
        taskDuration.setText(task.getDuration());
        final EditText taskTools = myDialog.findViewById(R.id.tools);
        taskTools.setText(task.getTools());
        EditText taskReward = myDialog.findViewById(R.id.reward);
        taskReward.setText(String.valueOf(task.getReward()));


        final Spinner assignee= (Spinner) myDialog.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.getUsers());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        assignee.setAdapter(spinnerArrayAdapter);
        assignee.setSelection(task.getAssignedTo().getId());
        final Spinner priority= (Spinner) myDialog.findViewById(R.id.spinner2);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.priority));
        priority.setAdapter(spinnerCountShoesArrayAdapter);
        priority.setSelection(task.getPriority());

        final TextView message = myDialog.findViewById(R.id.alert);

        Button update = myDialog.findViewById(R.id.edittaskbutton);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String taskAssignee = assignee.getSelectedItem().toString();
                String taskPriority = priority.getSelectedItem().toString();
                String taskTitle = ((EditText)myDialog.findViewById(R.id.titlev)).getText().toString();
                String taskDescription = ((EditText)myDialog.findViewById(R.id.description)).getText().toString();
                String taskDuration = ((EditText)myDialog.findViewById(R.id.duration)).getText().toString();
                String taskReward = ((EditText)myDialog.findViewById(R.id.reward)).getText().toString();
                String taskTools = ((EditText)myDialog.findViewById(R.id.tools)).getText().toString();


                //bunch of input validation
                boolean rewardIsNumber = taskReward.matches("-?\\d+(\\.\\d+)?");
                if(taskTitle.length() < 1){
                    message.setText("Title must have a greater length than 0!");
                    message.setVisibility(View.VISIBLE);
                }else if(!rewardIsNumber){
                    message.setText("Please enter a number of points as the reward");
                    message.setVisibility(View.VISIBLE);
                }else {
                    //no validation problems we good to go
                    int status = 1;
                    User use;
                    if (taskAssignee.equals("Unassigned")) {
                        status = 0;
                        use = null;
                    } else {
                        use = db.getUser(taskAssignee);
                    }
                    Task taske = new Task(use, logged, due.getText().toString(), taskDuration, priorite.get(taskPriority), taskTools, status, taskTitle, taskDescription, task.getId(), Integer.parseInt(taskReward));
                    db.updateTask(taske);
                    myDialog.dismiss();
                }
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
                String myFormat = "yyyy-MM-dd"; //In which you need put here
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

    private void callAddTask(boolean cancelable) {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.addtask_popup);
        myDialog.setCancelable(cancelable);

        final TextView message = myDialog.findViewById(R.id.alert);
        message.setVisibility(View.INVISIBLE);
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
                String taskTools = ((EditText)myDialog.findViewById(R.id.editText6)).toString();

                //bunch of input validation
                boolean rewardIsNumber = taskReward.matches("-?\\d+(\\.\\d+)?");
                if(taskTitle.length() < 1){
                    message.setText("Title must have a greater length than 0!");
                    message.setVisibility(View.VISIBLE);
                }else if(!rewardIsNumber){
                    message.setText("Please enter a number of points as the reward");
                    message.setVisibility(View.VISIBLE);
                }else {
                    //no validation problems we good to go
                    int status = 1;
                    User use;
                    if (taskAssignee.equals("Unassigned")) {
                        status = 0;
                        use = null;
                    } else {
                        use = db.getUser(taskAssignee);
                    }
                    Task task = new Task(use, logged, due.getText().toString(), taskDuration, priorite.get(taskPriority), taskTools, status, taskTitle, taskDescription, -1, Integer.parseInt(taskReward));
                    task = db.addTask(task);
                    addTaskToView(task, false);
                    myDialog.dismiss();
                }
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
                String myFormat = "yyyy-MM-dd"; //In which you need put here
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

    private void callAddItem(final GridLayout grid, final String text) {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.additem_popup);
        myDialog.show();
        Button ok = (Button) myDialog.findViewById(R.id.buttonOK);
        itemAdded = new CheckBox(MainActivity.this);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editT = (EditText) myDialog.findViewById(R.id.editText);
                String itemName = editT.getText().toString();
                StoredItem item = new StoredItem(itemName);
                itemAdded.setText(itemName);
                System.out.println("Added item: " + itemName);
                grid.addView(itemAdded);
                myDialog.dismiss();
            }
        });
        itemAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoredItem foodItem= new StoredItem(itemAdded.getText().toString());
                StoredItem broomCItem= new StoredItem(itemAdded.getText().toString());
                StoredItem materialItem= new StoredItem(itemAdded.getText().toString());
                StoredItem groceItem= new StoredItem(itemAdded.getText().toString());
                switch(text){
                    case "foodGrid":
                        if (itemAdded.isChecked()) {
                            db.addItem(foodItem, food);
                            db.removeItem(foodItem, groce);
                        }
                        else {
                            db.removeItem(foodItem, food);
                            db.addItem(foodItem, groce);
                        }
                        break;
                    case "broomCGrid":
                        if (itemAdded.isChecked()) {
                            db.addItem(broomCItem, broomC);
                            db.removeItem(broomCItem, material);
                        }
                        else {
                            db.removeItem(broomCItem, broomC);
                            db.addItem(broomCItem, material);
                        }
                        break;
                    case "materialGrid":
                        if (itemAdded.isChecked()) {
                            db.addItem(materialItem, material);
                            db.removeItem(materialItem, broomC);
                        }
                        else {
                            db.removeItem(materialItem, material);
                            db.addItem(materialItem, broomC);
                        }
                        break;
                    case "groceGrid":
                        if (itemAdded.isChecked()) {
                            db.addItem(groceItem, groce);
                            db.removeItem(groceItem, food);
                        }
                        else {
                            db.removeItem(groceItem, groce);
                            db.addItem(groceItem, food);
                        }
                        break;
                }
                generateStorage();
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
                } else if(user.getText().toString().equalsIgnoreCase("Unassigned")){
                    message.setText("Username is reserved for system use!");
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

    private void callFilter() {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.filter_popup);
        final Spinner assignee= (Spinner) myDialog.findViewById(R.id.selectUser);
        List<String> users = new ArrayList<String>(Arrays.asList(db.getUsers()));
        users.add(0, "None");

        String[] useres = new String[users.size()];
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users.toArray(useres));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        assignee.setAdapter(spinnerArrayAdapter);

        final Spinner filtere= (Spinner) myDialog.findViewById(R.id.selectFilter);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.filters));
        filtere.setAdapter(spinnerCountShoesArrayAdapter);
        myDialog.show();

        final CheckBox chkbox = myDialog.findViewById(R.id.showCompleted);

        Button filter = (Button) myDialog.findViewById(R.id.FilterButton);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //filterTasks(priority.isChecked(), user.isChecked(), duration.isChecked(), date.isChecked(), reward.isChecked(), status.isChecked());
                filterTasks(assignee.getSelectedItem().toString(), filtere.getSelectedItem().toString(), chkbox.isChecked());
                myDialog.dismiss();
            }
        });
    }

    public void filterTasks(String user, String filter, boolean showCompleted){
        String filterString = "";
        if(!user.equals("None")){
            User usere = db.getUser(user);
            filterString += "WHERE assignedto = " + usere.getId() + " ";
        }

        String appended = "";
        if(filter.equals("Date")){
            appended = "ORDER BY strftime('%s', due)";
        }else if(filter.equals("Priority")){
            appended = "ORDER BY priority desc";
        }else if(filter.equals("Status")){
            appended = "ORDER BY status asc";
        }

        LinearLayout list = (LinearLayout) findViewById(R.id.taskList);
        if(list.getChildCount() > 0)
            list.removeAllViews();

        ArrayList<Task> tasks = db.getTasks(filterString + appended);

        for(Task task : tasks){
            System.out.println("ID: " + task.getId());
            if(task.getStatus() != Task.Status.COMPLETED.getValue() || showCompleted) {
                addTaskToView(task, task.getStatus() == Task.Status.COMPLETED.getValue());
            }
        }
        
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.registernew: {
                if(logged.getIsParent()) {
                    callRegisterDialog(false, true);
                }
                break;
            }

            case R.id.switche: {
                callLoginDialog(true);
                break;
            }

            case R.id.logout: {
                logged = null;
                loggedIn = false;
                callLoginDialog(false);
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