package com.example.homeuser.simplefilemanager;

public class Item implements Comparable<Item>{
    private String name;
    private String size;
    private String date;
    private String path;
    private String parentPath;
    private String type;
    private String image;

    public Item(String n,String d, String dt, String p, String pp, String t, String img)
    {
        name = n;
        size = d;
        date = dt;
        path = p;
        parentPath = pp;
        type = t;
        image = img;

    }
    public String getName()
    {
        return name;
    }
    public String getSize()
    {
        return size;
    }
    public String getDate()
    {
        return date;
    }
    public String getPath()
    {
        return path;
    }
    public String getParentPath()
    {
        return parentPath;
    }
    public String getType()
    {
        return type;
    }
    public String getImage() {
        return image;
    }


    public void setName(String n)
    {
        name = n;
    }

    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}