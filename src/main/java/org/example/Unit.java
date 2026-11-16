package org.example;

public class Unit {
    private int x;
    private int y;
    private boolean alive = true;
    private final String id;
    private final String type;

    public Unit(String id, String type, int x, int y){
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

    public boolean IsAlive(){
        return alive;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void kill(){
        alive = false;
    }
}














