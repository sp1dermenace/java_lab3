package org.example;

public class Building {
    private final String id;
    private final String type;
    private int x;
    private int y;

    public Building(String id, String type, int x, int y){
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getId(){
        return id;
    }

    public String getType(){
        return type;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}











