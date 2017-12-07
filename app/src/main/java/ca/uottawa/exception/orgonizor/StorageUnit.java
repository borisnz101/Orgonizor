package ca.uottawa.exception.orgonizor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageUnit
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //StorageUnit Attributes
    private int storageID;

    //StorageUnit Associations
    private List<StoredItem> storedItems;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public StorageUnit(int aStorageID)
    {
        storageID = aStorageID;
        storedItems = new ArrayList<StoredItem>();
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
    public String toString()
    {
        return super.toString() + "["+
                "storageID" + ":" + getStorageID()+ "]";
    }
}