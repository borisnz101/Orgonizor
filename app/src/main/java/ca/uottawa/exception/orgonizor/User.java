package ca.uottawa.exception.orgonizor;

import android.media.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class User {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private int points;
  private String name;
  private String username;
  private boolean isParent;
  private Image avatar;
  private int id;

  //User Associations
  private List<StorageUnit> storageUnits;
  private List<Task> tasks;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(int id, String aName, String aUsername, boolean aIsParent, Image aAvatar, int points)
  {
    this.id = id;
    name = aName;
    username = aUsername;
    isParent = aIsParent;
    avatar = aAvatar;
    storageUnits = new ArrayList<StorageUnit>();
    tasks = new ArrayList<Task>();
    this.points = points;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void setPoints(int points) {
        this.points = points;
  }

  public String getName()
  {
    return name;
  }

  public String getUsername()
  {
    return username;
  }

  public boolean getIsParent()
  {
    return isParent;
  }

  public Image getAvatar()
  {
    return avatar;
  }

  public int getPoints() {
      return points;
  }

  public int getId(){
    return id;
  }

  public int indexOfStorageUnit(StorageUnit aStorageUnit)
  {
    int index = storageUnits.indexOf(aStorageUnit);
    return index;
  }

  public boolean addStorageUnit(StorageUnit aStorageUnit)
  {
    boolean wasAdded = false;
    if (storageUnits.contains(aStorageUnit)) { return false; }
    storageUnits.add(aStorageUnit);
    if (aStorageUnit.indexOfUser(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aStorageUnit.addUser(this);
      if (!wasAdded)
      {
        storageUnits.remove(aStorageUnit);
      }
    }
    return wasAdded;
  }

  public boolean removeStorageUnit(StorageUnit aStorageUnit)
  {
    boolean wasRemoved = false;
    if (!storageUnits.contains(aStorageUnit))
    {
      return wasRemoved;
    }

    int oldIndex = storageUnits.indexOf(aStorageUnit);
    storageUnits.remove(oldIndex);
    if (aStorageUnit.indexOfUser(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aStorageUnit.removeUser(this);
      if (!wasRemoved)
      {
        storageUnits.add(oldIndex,aStorageUnit);
      }
    }
    return wasRemoved;
  }

  public boolean addTask(Task aTask)
  {
    boolean wasAdded = false;
    if (tasks.contains(aTask)) { return false; }
    User existingUser = aTask.getUser();
    if (existingUser == null)
    {
      aTask.setUser(this);
    }
    else if (!this.equals(existingUser))
    {
      existingUser.removeTask(aTask);
      addTask(aTask);
    }
    else
    {
      tasks.add(aTask);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTask(Task aTask)
  {
    boolean wasRemoved = false;
    if (tasks.contains(aTask))
    {
      tasks.remove(aTask);
      aTask.setUser(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "username" + ":" + getUsername()+ "," +
            "isParent" + ":" + getIsParent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "avatar" + "=" + (getAvatar() != null ? !getAvatar().equals(this)  ? getAvatar().toString().replaceAll("  ","    ") : "this" : "null");
  }

}