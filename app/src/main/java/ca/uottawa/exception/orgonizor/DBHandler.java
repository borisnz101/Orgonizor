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
    // Shops Table Columns names
    private static final String KEY_ID = "id";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
        + KEY_ID + " INTEGER PRIMARY KEY,username TEXT, password TEXT, name TEXT, accessLevel INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Creating tables again
        onCreate(db);
    }

    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("username", user); // Shop Name
        //values.put("name", name);
        //values.put("accessLevel", accessLevel);
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

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

    public boolean authenticateUser(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String pass = "";
        try {
            pass = Base64.encodeToString(generateKey(password.toCharArray(), "marcelle".getBytes()).getEncoded(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        // Inserting Rowx
        Cursor c = db.query(TABLE_USER, new String[]{"username"}
                , "username = ? AND password = ?" ,new String[]{user, pass}, null, null, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        if(dot > 0){
            return true;
        }
        return false;
    }

    //returns if there are any users in the DB
    public boolean usersExist(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Inserting Rowx
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int dot = c.getCount();
        db.close(); // Closing database connection
        if(dot > 0){
            return true;
        }
        return false;
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