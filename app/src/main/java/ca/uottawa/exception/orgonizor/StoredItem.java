package ca.uottawa.exception.orgonizor;

public class StoredItem {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StoredItems Attributes
  private String name;
  private String description;
  private int id;

  //StoredItems Associations
  private StorageUnit storageUnit;

  //------------------------
  // CONSTRUCTOR
  //------------------------

    public StoredItem(String aName)
    {
        name = aName;
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

  public StorageUnit getStorageUnit()
  {
    return storageUnit;
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

  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "storageUnit = "+(getStorageUnit()!=null?Integer.toHexString(System.identityHashCode(getStorageUnit())):"null");
  }
}