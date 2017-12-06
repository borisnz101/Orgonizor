package ca.uottawa.exception.orgonizor;

/**
 * Created by raphael on 2017-11-23.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "orgonizor";
    // Contacts table name
    private static final String TABLE_USER = "users";
    private static final String TABLE_TASK = "tasks";
    private static final String TABLE_STORAGE = "storage";
    // Shops Table Columns names
    private static final String KEY_ID = "id";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
        + KEY_ID + " INTEGER PRIMARY KEY autoincrement,username TEXT, password TEXT, name TEXT, accessLevel INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement, assignedto INTEGER, creator INTEGER, due TEXT, duration TEXT, priority INTEGER, tools INTEGER, status INTEGER, title TEXT, description TEXT, reward TEXT" + ")";
        db.execSQL(CREATE_TASK_TABLE);
        String CREATE_STORAGE_TABLE = "CREATE TABLE " + TABLE_STORAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement, name TEXT, storageid INTEGER" + ")";
        db.execSQL(CREATE_STORAGE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORAGE);
        // Creating tables again
        onCreate(db);
    }

    //getStorage(int id) 1 2 3 4 5

    //switch(String name, int from, int to)

    //add(String, int storage)

    //remove(String, int storage)

    public ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        //get data
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASK, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        while(c.moveToNext()){
            tasks.add(new Task(getUser(c.getInt(1)), getUser(c.getInt(2)), c.getString(3), c.getString(4), c.getInt(5), getStorageUnit(c.getInt(6)), c.getInt(7), c.getString(8), c.getString(9), c.getInt(0), c.getString(10)));
        }
        return tasks;
    }

    public ArrayList<Task> getTasks(String query){
        ArrayList<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        //get data
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASK + " " + query, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        while(c.moveToNext()){
            tasks.add(new Task(getUser(c.getInt(1)), getUser(c.getInt(2)), c.getString(3), c.getString(4), c.getInt(5), getStorageUnit(c.getInt(6)), c.getInt(7), c.getString(8), c.getString(9), c.getInt(0), c.getString(10)));
        }
        return tasks;
    }

    public Task getTask(int id){
        Task task;
        SQLiteDatabase db = this.getReadableDatabase();
        //get data
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE id = " + id, null);
        int dot = c.getCount();
        System.out.println("Count: " + dot);
        db.close(); // Closing database connection
        c.moveToNext();
        task = new Task(getUser(c.getInt(1)), getUser(c.getInt(2)), c.getString(3), c.getString(4), c.getInt(5), getStorageUnit(c.getInt(6)), c.getInt(7), c.getString(8), c.getString(9), c.getInt(0), c.getString(10));
        return task;
    }

    public void removeTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        //get data
        Cursor c = db.rawQuery("UPDATE " + TABLE_TASK + " SET status = 2 WHERE id = " + id , null);
        System.out.println("UPDATE " + TABLE_TASK + " SET status = 2 WHERE id = " + id);
        System.out.println("Deleted: " + c.getCount());
        db.close(); // Closing database connection
    }

    //User aAssignedTo, User aCreator, String aDue, String aDuration, Priority aPriority, StorageUnit aTools, Status aStatus, String aTitle, String aDescription, int aId, String aReward
    public Task addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(task.getAssignedTo() == null){
            values.put("assignedto", -1);
        }else {
            values.put("assignedto", task.getAssignedTo().getId());
        }
        values.put("creator", task.getCreator().getId());
        values.put("due", task.getDue());
        values.put("duration", task.getDuration());
        values.put("priority", task.getPriority());
        values.put("tools", task.getTools().getStorageID());
        values.put("status", task.getStatus());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("reward", task.getReward());
        //values.put("name", name);
        //values.put("accessLevel", accessLevel);
        // Inserting Row
        long id = db.insert(TABLE_TASK, null, values);
        task.setId(id);
        db.close(); // Closing database connection
        return task;
    }

    //TODO complete
    public StorageUnit getStorageUnit(int id){
        return null;
    }

    public void addUser(String user, String password, String name, int accessLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user); // Shop Name
        try {
            values.put("password", Base64.encodeToString(generateKey(password.toCharArray(), "marcelle".getBytes()).getEncoded(), Base64.DEFAULT)); // Shop Phone Number
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        values.put("name", name);
        values.put("accessLevel", accessLevel);
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    public User authenticateUser(String user, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String pass = "";
        try {
            pass = Base64.encodeToString(generateKey(password.toCharArray(), "marcelle".getBytes()).getEncoded(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        // Inserting Rowx
        Cursor c = db.query(TABLE_USER, new String[]{"username", "name", "accessLevel", KEY_ID}
                , "username = ? AND password = ?" ,new String[]{user, pass}, null, null, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        if(dot > 0){
            c.moveToNext();
            return new User(c.getInt(3), c.getString(1), c.getString(0), c.getInt(2) == 1 ? true : false, null);
        }
        return null;
    }

    //returns if there are any users in the DB
    public boolean usersExist(){
        SQLiteDatabase db = this.getReadableDatabase();
        // Inserting Rowx
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        if(dot > 0){
            return true;
        }
        return false;
    }

    public String[] getUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        // Inserting Rowx
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int dot = c.getCount();
        String[] users = new String[dot+1];
        for(int i = 1; i < dot+1; i++){
            c.moveToNext();
            users[i] = c.getString(1);
        }
        users[0] = "Unassigned";
        db.close(); // Closing database connection
        return users;
    }

    public User getUser(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        if(id == -1){
            return new User(-1, "Unassigned", "Unassigned", false, null);
        }
        // Inserting Rowx
        Cursor c = db.query(TABLE_USER, new String[]{"username", "name", "accessLevel", KEY_ID}
                , "id = ?" ,new String[]{id+""}, null, null, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        if(dot > 0){
            c.moveToNext();
            return new User(c.getInt(3), c.getString(1), c.getString(0), c.getInt(2) == 1 ? true : false, null);
        }
        return null;
    }

    public User getUser(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        // Inserting Rowx
        Cursor c = db.query(TABLE_USER, new String[]{"username", "name", "accessLevel", KEY_ID}
                , "username = ?" ,new String[]{name}, null, null, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        if(dot > 0){
            c.moveToNext();
            return new User(c.getInt(3), c.getString(1), c.getString(0), c.getInt(2) == 1 ? true : false, null);
        }
        return null;
    }

    //Comes from the android official API doc
    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }
}