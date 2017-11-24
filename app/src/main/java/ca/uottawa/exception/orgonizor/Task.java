/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.0-33ee578-3609 modeling language!*/


import java.util.*;

// line 2 "model.ump"
// line 71 "model.ump"
public class Task
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Task Attributes
  private User assignedTo;
  private User creator;
  private String due;
  private String duration;
  private enum priority;
  private StorageUnit tools;
  private enum status;
  private String title;
  private String description;
  private int id;
  private String reward;

  //Task Associations
  private List<StoredItems> storedItems;
  private User user;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Task(User aAssignedTo, User aCreator, String aDue, String aDuration, enum aPriority, StorageUnit aTools, enum aStatus, String aTitle, String aDescription, int aId, String aReward)
  {
    assignedTo = aAssignedTo;
    creator = aCreator;
    due = aDue;
    duration = aDuration;
    priority = aPriority;
    tools = aTools;
    status = aStatus;
    title = aTitle;
    description = aDescription;
    id = aId;
    reward = aReward;
    storedItems = new ArrayList<StoredItems>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAssignedTo(User aAssignedTo)
  {
    boolean wasSet = false;
    assignedTo = aAssignedTo;
    wasSet = true;
    return wasSet;
  }

  public boolean setCreator(User aCreator)
  {
    boolean wasSet = false;
    creator = aCreator;
    wasSet = true;
    return wasSet;
  }

  public boolean setDue(String aDue)
  {
    boolean wasSet = false;
    due = aDue;
    wasSet = true;
    return wasSet;
  }

  public boolean setDuration(String aDuration)
  {
    boolean wasSet = false;
    duration = aDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setPriority(enum aPriority)
  {
    boolean wasSet = false;
    priority = aPriority;
    wasSet = true;
    return wasSet;
  }

  public boolean setTools(StorageUnit aTools)
  {
    boolean wasSet = false;
    tools = aTools;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(enum aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
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

  public boolean setReward(String aReward)
  {
    boolean wasSet = false;
    reward = aReward;
    wasSet = true;
    return wasSet;
  }

  public User getAssignedTo()
  {
    return assignedTo;
  }

  public User getCreator()
  {
    return creator;
  }

  public String getDue()
  {
    return due;
  }

  public String getDuration()
  {
    return duration;
  }

  public enum getPriority()
  {
    return priority;
  }

  public StorageUnit getTools()
  {
    return tools;
  }

  public enum getStatus()
  {
    return status;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public int getId()
  {
    return id;
  }

  public String getReward()
  {
    return reward;
  }

  public StoredItems getStoredItem(int index)
  {
    StoredItems aStoredItem = storedItems.get(index);
    return aStoredItem;
  }

  public List<StoredItems> getStoredItems()
  {
    List<StoredItems> newStoredItems = Collections.unmodifiableList(storedItems);
    return newStoredItems;
  }

  public int numberOfStoredItems()
  {
    int number = storedItems.size();
    return number;
  }

  public boolean hasStoredItems()
  {
    boolean has = storedItems.size() > 0;
    return has;
  }

  public int indexOfStoredItem(StoredItems aStoredItem)
  {
    int index = storedItems.indexOf(aStoredItem);
    return index;
  }

  public User getUser()
  {
    return user;
  }

  public boolean hasUser()
  {
    boolean has = user != null;
    return has;
  }

  public static int minimumNumberOfStoredItems()
  {
    return 0;
  }

  public boolean addStoredItem(StoredItems aStoredItem)
  {
    boolean wasAdded = false;
    if (storedItems.contains(aStoredItem)) { return false; }
    storedItems.add(aStoredItem);
    if (aStoredItem.indexOfTask(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aStoredItem.addTask(this);
      if (!wasAdded)
      {
        storedItems.remove(aStoredItem);
      }
    }
    return wasAdded;
  }

  public boolean removeStoredItem(StoredItems aStoredItem)
  {
    boolean wasRemoved = false;
    if (!storedItems.contains(aStoredItem))
    {
      return wasRemoved;
    }

    int oldIndex = storedItems.indexOf(aStoredItem);
    storedItems.remove(oldIndex);
    if (aStoredItem.indexOfTask(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aStoredItem.removeTask(this);
      if (!wasRemoved)
      {
        storedItems.add(oldIndex,aStoredItem);
      }
    }
    return wasRemoved;
  }

  public boolean addStoredItemAt(StoredItems aStoredItem, int index)
  {  
    boolean wasAdded = false;
    if(addStoredItem(aStoredItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStoredItems()) { index = numberOfStoredItems() - 1; }
      storedItems.remove(aStoredItem);
      storedItems.add(index, aStoredItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStoredItemAt(StoredItems aStoredItem, int index)
  {
    boolean wasAdded = false;
    if(storedItems.contains(aStoredItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStoredItems()) { index = numberOfStoredItems() - 1; }
      storedItems.remove(aStoredItem);
      storedItems.add(index, aStoredItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStoredItemAt(aStoredItem, index);
    }
    return wasAdded;
  }

  public boolean setUser(User aUser)
  {
    boolean wasSet = false;
    User existingUser = user;
    user = aUser;
    if (existingUser != null && !existingUser.equals(aUser))
    {
      existingUser.removeTask(this);
    }
    if (aUser != null)
    {
      aUser.addTask(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ArrayList<StoredItems> copyOfStoredItems = new ArrayList<StoredItems>(storedItems);
    storedItems.clear();
    for(StoredItems aStoredItem : copyOfStoredItems)
    {
      aStoredItem.removeTask(this);
    }
    if (user != null)
    {
      User placeholderUser = user;
      this.user = null;
      placeholderUser.removeTask(this);
    }
  }

  // line 18 "model.ump"
   public void createTask(String title, String description, String due, String duration, User creator){
    
  }

  // line 19 "model.ump"
   public void deleteTask(int ID){
    
  }

  // line 20 "model.ump"
   public void modifyTask(int ID){
    
  }

  // line 21 "model.ump"
   public boolean takeTask(String username, int ID){
    
  }

  // line 22 "model.ump"
   public boolean untakeTask(String username, int ID){
    
  }

  // line 23 "model.ump"
   public String taskInfos(int ID){
    
  }

  // line 24 "model.ump"
   public StorageUnit toolsInfos(){
    
  }

  // line 25 "model.ump"
   public void hideTask(String username){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "due" + ":" + getDue()+ "," +
            "duration" + ":" + getDuration()+ "," +
            "title" + ":" + getTitle()+ "," +
            "description" + ":" + getDescription()+ "," +
            "id" + ":" + getId()+ "," +
            "reward" + ":" + getReward()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "assignedTo" + "=" + (getAssignedTo() != null ? !getAssignedTo().equals(this)  ? getAssignedTo().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "creator" + "=" + (getCreator() != null ? !getCreator().equals(this)  ? getCreator().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "priority" + "=" + (getPriority() != null ? !getPriority().equals(this)  ? getPriority().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "tools" + "=" + (getTools() != null ? !getTools().equals(this)  ? getTools().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "user = "+(getUser()!=null?Integer.toHexString(System.identityHashCode(getUser())):"null");
  }
}