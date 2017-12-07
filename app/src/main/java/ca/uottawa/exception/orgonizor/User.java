package ca.uottawa.exception.orgonizor;


public class User {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private int points;
  private String name;
  private String username;
  private boolean isParent;
  private int id;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(int id, String aName, String aUsername, boolean aIsParent, int points)
  {
    this.id = id;
    name = aName;
    username = aUsername;
    isParent = aIsParent;
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

  public int getPoints() {
      return points;
  }

  public int getId(){
    return id;
  }
  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "username" + ":" + getUsername()+ "," +
            "isParent" + ":" + getIsParent()+ "]" + System.getProperties().getProperty("line.separator");
  }

}