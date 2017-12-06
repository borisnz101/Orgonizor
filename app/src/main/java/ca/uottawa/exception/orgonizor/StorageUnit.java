package ca.uottawa.exception.orgonizor;

import java.util.*;

public class StorageUnit
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //StorageUnit Attributes
    private List<LinkedList> storedItem;
    private int storageID;

    //StorageUnit Associations
    private List<StoredItem> storedItems;
    private List<User> users;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public StorageUnit(int aStorageID)
    {
        storedItem = new ArrayList<LinkedList>();
        storageID = aStorageID;
        storedItems = new ArrayList<StoredItem>();
        users = new ArrayList<User>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    public int getStorageID()
    {
        return storageID;
    }

    public List<StoredItem> getStoredItems()
    {
        List<StoredItem> newStoredItems = Collections.unmodifiableList(storedItems);
        return newStoredItems;
    }


    public int indexOfUser(User aUser)
    {
        int index = users.indexOf(aUser);
        return index;
    }

    public boolean addStoredItem(StoredItem aStoredItem)
    {
        boolean wasAdded = false;
        if (storedItems.contains(aStoredItem)) { return false; }
        StorageUnit existingStorageUnit = aStoredItem.getStorageUnit();
        boolean isNewStorageUnit = existingStorageUnit != null && !this.equals(existingStorageUnit);
        if (isNewStorageUnit)
        {
            aStoredItem.setStorageUnit(this);
        }
        else
        {
            storedItems.add(aStoredItem);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeStoredItem(StoredItem aStoredItem)
    {
        boolean wasRemoved = false;
        //Unable to remove aStoredItem, as it must always have a storageUnit
        if (!this.equals(aStoredItem.getStorageUnit()))
        {
            storedItems.remove(aStoredItem);
            wasRemoved = true;
        }
        return wasRemoved;
    }


    public boolean addUser(User aUser)
    {
        boolean wasAdded = false;
        if (users.contains(aUser)) { return false; }
        users.add(aUser);
        if (aUser.indexOfStorageUnit(this) != -1)
        {
            wasAdded = true;
        }
        else
        {
            wasAdded = aUser.addStorageUnit(this);
            if (!wasAdded)
            {
                users.remove(aUser);
            }
        }
        return wasAdded;
    }

    public boolean removeUser(User aUser)
    {
        boolean wasRemoved = false;
        if (!users.contains(aUser))
        {
            return wasRemoved;
        }

        int oldIndex = users.indexOf(aUser);
        users.remove(oldIndex);
        if (aUser.indexOfStorageUnit(this) == -1)
        {
            wasRemoved = true;
        }
        else
        {
            wasRemoved = aUser.removeStorageUnit(this);
            if (!wasRemoved)
            {
                users.add(oldIndex,aUser);
            }
        }
        return wasRemoved;
    }

    public String toString()
    {
        return super.toString() + "["+
                "storageID" + ":" + getStorageID()+ "]";
    }
}