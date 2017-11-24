/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.0-33ee578-3609 modeling language!*/


import java.util.*;

// line 30 "model.ump"
// line 76 "model.ump"
public class User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private String name;
  private String username;
  private String password;
  private boolean isParent;
  private Image avatar;

  //User Associations
  private List<StorageUnit> storageUnits;
  private List<Task> tasks;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aName, String aUsername, String aPassword, boolean aIsParent, Image aAvatar)
  {
    name = aName;
    username = aUsername;
    password = aPassword;
    isParent = aIsParent;
    avatar = aAvatar;
    storageUnits = new ArrayList<StorageUnit>();
    tasks = new ArrayList<Task>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    username = aUsername;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsParent(boolean aIsParent)
  {
    boolean wasSet = false;
    isParent = aIsParent;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatar(Image aAvatar)
  {
    boolean wasSet = false;
    avatar = aAvatar;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }

  public boolean getIsParent()
  {
    return isParent;
  }

  public Image getAvatar()
  {
    return avatar;
  }

  public StorageUnit getStorageUnit(int index)
  {
    StorageUnit aStorageUnit = storageUnits.get(index);
    return aStorageUnit;
  }

  public List<StorageUnit> getStorageUnits()
  {
    List<StorageUnit> newStorageUnits = Collections.unmodifiableList(storageUnits);
    return newStorageUnits;
  }

  public int numberOfStorageUnits()
  {
    int number = storageUnits.size();
    return number;
  }

  public boolean hasStorageUnits()
  {
    boolean has = storageUnits.size() > 0;
    return has;
  }

  public int indexOfStorageUnit(StorageUnit aStorageUnit)
  {
    int index = storageUnits.indexOf(aStorageUnit);
    return index;
  }

  public Task getTask(int index)
  {
    Task aTask = tasks.get(index);
    return aTask;
  }

  public List<Task> getTasks()
  {
    List<Task> newTasks = Collections.unmodifiableList(tasks);
    return newTasks;
  }

  public int numberOfTasks()
  {
    int number = tasks.size();
    return number;
  }

  public boolean hasTasks()
  {
    boolean has = tasks.size() > 0;
    return has;
  }

  public int indexOfTask(Task aTask)
  {
    int index = tasks.indexOf(aTask);
    return index;
  }

  public static int minimumNumberOfStorageUnits()
  {
    return 0;
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

  public boolean addStorageUnitAt(StorageUnit aStorageUnit, int index)
  {  
    boolean wasAdded = false;
    if(addStorageUnit(aStorageUnit))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStorageUnits()) { index = numberOfStorageUnits() - 1; }
      storageUnits.remove(aStorageUnit);
      storageUnits.add(index, aStorageUnit);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStorageUnitAt(StorageUnit aStorageUnit, int index)
  {
    boolean wasAdded = false;
    if(storageUnits.contains(aStorageUnit))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStorageUnits()) { index = numberOfStorageUnits() - 1; }
      storageUnits.remove(aStorageUnit);
      storageUnits.add(index, aStorageUnit);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStorageUnitAt(aStorageUnit, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfTasks()
  {
    return 0;
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

  public boolean addTaskAt(Task aTask, int index)
  {  
    boolean wasAdded = false;
    if(addTask(aTask))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTasks()) { index = numberOfTasks() - 1; }
      tasks.remove(aTask);
      tasks.add(index, aTask);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTaskAt(Task aTask, int index)
  {
    boolean wasAdded = false;
    if(tasks.contains(aTask))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTasks()) { index = numberOfTasks() - 1; }
      tasks.remove(aTask);
      tasks.add(index, aTask);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTaskAt(aTask, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ArrayList<StorageUnit> copyOfStorageUnits = new ArrayList<StorageUnit>(storageUnits);
    storageUnits.clear();
    for(StorageUnit aStorageUnit : copyOfStorageUnits)
    {
      aStorageUnit.removeUser(this);
    }
    while( !tasks.isEmpty() )
    {
      tasks.get(0).setUser(null);
    }
  }

  // line 40 "model.ump"
   public void createUser(String name, String username, boolean isParent, Image profilePic){
    
  }

  // line 41 "model.ump"
   public void deleteUser(String username){
    
  }

  // line 42 "model.ump"
   public void login(String username, String password){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "username" + ":" + getUsername()+ "," +
            "password" + ":" + getPassword()+ "," +
            "isParent" + ":" + getIsParent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "avatar" + "=" + (getAvatar() != null ? !getAvatar().equals(this)  ? getAvatar().toString().replaceAll("  ","    ") : "this" : "null");
  }
}