package ca.uottawa.exception.orgonizor;

import java.util.*;

// line 48 "model.ump"
// line 81 "model.ump"
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

    public boolean addStoredItem(LinkedList aStoredItem)
    {
        boolean wasAdded = false;
        if (storedItems.contains(aStoredItem)) { return false; }
        wasAdded = storedItem.add(aStoredItem);
        return wasAdded;
    }

    public boolean removeStoredItem(LinkedList aStoredItem)
    {
        boolean wasRemoved = false;
        wasRemoved = storedItem.remove(aStoredItem);
        return wasRemoved;
    }

    public boolean setStorageID(int aStorageID)
    {
        boolean wasSet = false;
        storageID = aStorageID;
        wasSet = true;
        return wasSet;
    }

    public LinkedList getStoredItem(int index)
    {
        LinkedList aStoredItem = storedItem.get(index);
        return aStoredItem;
    }

    public LinkedList[] getStoredItem()
    {
        LinkedList[] newStoredItem = storedItem.toArray(new LinkedList[storedItem.size()]);
        return newStoredItem;
    }

    public int numberOfStoredItem()
    {
        int number = storedItem.size();
        return number;
    }

    public boolean hasStoredItem()
    {
        boolean has = storedItem.size() > 0;
        return has;
    }

    public int indexOfStoredItem(LinkedList aStoredItem)
    {
        int index = storedItem.indexOf(aStoredItem);
        return index;
    }

    public int getStorageID()
    {
        return storageID;
    }

    public List<StoredItem> getStoredItems()
    {
        List<StoredItem> newStoredItems = Collections.unmodifiableList(storedItems);
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

    public int indexOfStoredItem(StoredItem aStoredItem)
    {
        int index = storedItems.indexOf(aStoredItem);
        return index;
    }

    public User getUser(int index)
    {
        User aUser = users.get(index);
        return aUser;
    }

    public List<User> getUsers()
    {
        List<User> newUsers = Collections.unmodifiableList(users);
        return newUsers;
    }

    public int numberOfUsers()
    {
        int number = users.size();
        return number;
    }

    public boolean hasUsers()
    {
        boolean has = users.size() > 0;
        return has;
    }

    public int indexOfUser(User aUser)
    {
        int index = users.indexOf(aUser);
        return index;
    }

    public static int minimumNumberOfStoredItems()
    {
        return 0;
    }

    public StoredItem addStoredItem(String aName, String aDescription, int aId)
    {
        return new StoredItem(aName, aDescription, aId, this);
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

    public boolean addStoredItemAt(StoredItem aStoredItem, int index)
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

    public boolean addOrMoveStoredItemAt(StoredItem aStoredItem, int index)
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

    public static int minimumNumberOfUsers()
    {
        return 0;
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

    public boolean addUserAt(User aUser, int index)
    {
        boolean wasAdded = false;
        if(addUser(aUser))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
            users.remove(aUser);
            users.add(index, aUser);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveUserAt(User aUser, int index)
    {
        boolean wasAdded = false;
        if(users.contains(aUser))
        {
            if(index < 0 ) { index = 0; }
            if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
            users.remove(aUser);
            users.add(index, aUser);
            wasAdded = true;
        }
        else
        {
            wasAdded = addUserAt(aUser, index);
        }
        return wasAdded;
    }

    public void delete()
    {
        for(int i=storedItems.size(); i > 0; i--)
        {
            StoredItem aStoredItem = storedItems.get(i - 1);
            aStoredItem.delete();
        }
        ArrayList<User> copyOfUsers = new ArrayList<User>(users);
        users.clear();
        for(User aUser : copyOfUsers)
        {
            aUser.removeStorageUnit(this);
        }
    }

    // line 54 "model.ump"
    public void addItem(User username, String name){

    }

    // line 55 "model.ump"
    //TODO // FIXME: 11/24/2017 NEEDS FIXING
    public boolean isInStorage(String name){
        return false;
    }

    // line 56 "model.ump"
    //TODO // FIXME: 11/24/2017 NEEDS FIXING
    public String storedItem(){
           return "";
    }


    public String toString()
    {
        return super.toString() + "["+
                "storageID" + ":" + getStorageID()+ "]";
    }
}