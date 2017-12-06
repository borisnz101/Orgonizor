package ca.uottawa.exception.orgonizor;


import java.util.*;

// line 61 "model.ump"
// line 86 "model.ump"
public class StoredItem {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StoredItems Attributes
  private String name;
  private String description;
  private int id;

  //StoredItems Associations
  private List<Task> tasks;
  private StorageUnit storageUnit;

  //------------------------
  // CONSTRUCTOR
  //------------------------

    public StoredItem(String aName)//, StorageUnit aStorageUnit)
    {
        name = aName;
    }

  public StoredItem(String aName, String aDescription, int aId, StorageUnit aStorageUnit)
  {
    name = aName;
    description = aDescription;
    id = aId;
    tasks = new ArrayList<Task>();
    boolean didAddStorageUnit = setStorageUnit(aStorageUnit);
    if (!didAddStorageUnit)
    {
      throw new RuntimeException("Unable to create storedItem due to storageUnit");
    }
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

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public int getId()
  {
    return id;
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

  public StorageUnit getStorageUnit()
  {
    return storageUnit;
  }

  public static int minimumNumberOfTasks()
  {
    return 0;
  }

  public boolean addTask(Task aTask)
  {
    boolean wasAdded = false;
    if (tasks.contains(aTask)) { return false; }
    tasks.add(aTask);
    if (aTask.indexOfStoredItem(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aTask.addStoredItem(this);
      if (!wasAdded)
      {
        tasks.remove(aTask);
      }
    }
    return wasAdded;
  }

  public boolean removeTask(Task aTask)
  {
    boolean wasRemoved = false;
    if (!tasks.contains(aTask))
    {
      return wasRemoved;
    }

    int oldIndex = tasks.indexOf(aTask);
    tasks.remove(oldIndex);
    if (aTask.indexOfStoredItem(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aTask.removeStoredItem(this);
      if (!wasRemoved)
      {
        tasks.add(oldIndex,aTask);
      }
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

  public boolean setStorageUnit(StorageUnit aStorageUnit)
  {
    boolean wasSet = false;
    if (aStorageUnit == null)
    {
      return wasSet;
    }

    StorageUnit existingStorageUnit = storageUnit;
    storageUnit = aStorageUnit;
    if (existingStorageUnit != null && !existingStorageUnit.equals(aStorageUnit))
    {
      existingStorageUnit.removeStoredItem(this);
    }
    storageUnit.addStoredItem(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ArrayList<Task> copyOfTasks = new ArrayList<Task>(tasks);
    tasks.clear();
    for(Task aTask : copyOfTasks)
    {
      aTask.removeStoredItem(this);
    }
    StorageUnit placeholderStorageUnit = storageUnit;
    this.storageUnit = null;
    placeholderStorageUnit.removeStoredItem(this);
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "storageUnit = "+(getStorageUnit()!=null?Integer.toHexString(System.identityHashCode(getStorageUnit())):"null");
  }
}