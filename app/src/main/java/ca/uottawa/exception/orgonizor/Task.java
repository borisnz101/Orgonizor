package ca.uottawa.exception.orgonizor;

public class Task {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Task Attributes
  private User assignedTo;
  private User creator;
  private String due;
  private String duration;
  public enum Priority{
    URGENT(3), IMPORTANT(2), NORMAL(1), NOT_URGENT(0);
      private final int value;
      Priority(int value) {
          this.value = value;
      }

      public int getValue() {
          return value;
      }
  }
  private int priority;
  private String tools;
  public enum Status{
    COMPLETED(2), ASSIGNED(1), NOT_ASSIGNED(0);
      private final int value;
      Status(int value) {
          this.value = value;
      }

      public int getValue() {
          return value;
      }
  }
  private int status;
  private String title;
  private String description;
  private long id;
  private int reward;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Task(User aAssignedTo, User aCreator, String aDue, String aDuration, int aPriority, String aTools, int aStatus, String aTitle, String aDescription, long aId, int aReward)
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

  public boolean setPriority(int aPriority)
  {
    boolean wasSet = false;
    priority = aPriority;
    wasSet = true;
    return wasSet;
  }

  public boolean setTools(String aTools)
  {
    boolean wasSet = false;
    tools = aTools;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(int aStatus)
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

  public boolean setId(long aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setReward(int aReward)
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

  public int getPriority()
  {
    return priority;
  }

  public String getTools()
  {
    return tools;
  }

  public int getStatus()
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

  public long getId()
  {
    return id;
  }

  public int getReward()
  {
    return reward;
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
            "  " + "priority" + "=" + (getPriority() + System.getProperties().getProperty("line.separator")) +
            "  " + "tools" + "=" + (getTools() != null ? !getTools().equals(this)  ? getTools().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() + System.getProperties().getProperty("line.separator"));
  }
}