package org.example;

import java.util.HashMap;
import java.util.Map;

public class GameEngine implements IGameEngine{
    private int width;
    private int height;
    private boolean gameStart = false;
    private final Map<String, Unit> units = new HashMap<>();
    private final Map<String, Building> buildings = new HashMap<>();
    private final Map<String, CommandHandler> handlers = new HashMap<>();

    public interface CommandHandler{
        void handle(String[] args);
    }

    public GameEngine(){
        registerDefaultCommand();
    }

    public void registerCommand(String name, CommandHandler handler){
        handlers.put(name, handler);
    }

    @Override
    public void exec(Command c){
        CommandHandler handler = handlers.get(c.getName());
        if (handler == null) {
            System.out.println("Неизвестная команда '"+ c.getName() + "'");
            return;
        }
        try {
            handler.handle(c.getArgs());
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении "+ c.getName() +": " + e.getMessage());
        }
    }

    private void registerDefaultCommand (){
        registerCommand("initMap", args -> {
            this.width = Integer.parseInt(args[0]);
            this.height = Integer.parseInt(args[1]);
            System.out.println("Создана карта " + width + "x" + height);
        });

        registerCommand("addUnit", a -> {
            String id = a[0];
            String type = a[1];
            int x = Integer.parseInt(a[2]);
            int y = Integer.parseInt(a[3]);
            checkCoord(x, y);
            if(!CellEmpty(x, y)){
                throw new IllegalStateException("Клетка занята");
            }
            units.put(id, new Unit(id, type, x, y));
            System.out.println("Добавлен юнит "+id+" ("+type+")"+" в клетке ("+x+","+y+")");

        });

        registerCommand("startGame", args -> {
            this.gameStart = true;
            System.out.println("Игра началась! ");
        });

        registerCommand("move", a ->{
            checkGameStarted();
            String id = a[0];
            int x = Integer.parseInt(a[1]);
            int y = Integer.parseInt(a[2]);
            checkCoord(x, y);
            if(!CellEmpty(x, y)){
                throw new IllegalStateException("Клетка занята");
            }

            Unit unit = getValidUnit(id);
            unit.setPosition(x, y);
            System.out.println("Юнит "+id+" перемещен в ("+x+","+y+")");

        });

        registerCommand("attack", a ->{
            checkGameStarted();
            String unitID = a[0];
            String targetID = a[1];
            Unit unit = getValidUnit(unitID);
            Unit target = getValidUnit(targetID);
            target.kill();
            System.out.println("Юнит "+unitID+" атакует, юнит " +targetID + " убит");
        });

        registerCommand("build", a -> {
            checkGameStarted();
            String buildID = a[0];
            String type = a[1];
            int x = Integer.parseInt(a[2]);
            int y = Integer.parseInt(a[3]);
            checkCoord(x, y);
            if(!CellEmpty(x, y)){
                throw new IllegalStateException("Клетка занята");
            }
            buildings.put(buildID, new Building(buildID, type, x, y));
            System.out.println("Построено здание "+buildID+" ("+type+") в ("+x+","+y+")" );
        });

        registerCommand("endTurn", args -> {
            checkGameStarted();
            System.out.println("Ход завершен");
        });

        registerCommand("print", args -> {
            System.out.println("=== Состояние игры ===");
            System.out.println("Размер карты: "+width+"x"+height+". Статус: " + (gameStart ? "Игра идет" : "Игра не начата"));
            System.out.println("Юниты: ");
            units.values().forEach(u -> System.out.println("- " + u.getId()+" ["+ u.getType()+"] в ("+u.getX()+","+u.getY()+") Состояние: " +( u.IsAlive() ? "Жив" : "Мертв")));
            System.out.println("Здания: ");
            buildings.values().forEach(b -> System.out.println("- "+b.getId()+" ["+b.getType()+"] в ("+b.getX()+","+b.getY()+")"));
            System.out.println("========================");
        });
    }

    private void checkGameStarted(){
        if(!gameStart){
            throw new IllegalStateException("Игра ещё не началась");
        }
    }

    private Unit getValidUnit(String id){
        Unit unit = units.get(id);
        if(unit == null){
            throw new IllegalArgumentException("Юнит не существует");
        }
        if(!unit.IsAlive()){
            throw new IllegalStateException("Юнит мертв");
        }
        return unit;
    }

    private boolean CellEmpty(int x, int y){
        boolean unitsIn = units.values().stream().anyMatch(u -> u.IsAlive() && u.getX() == x&&u.getY()==y);
        boolean buildingsIn = buildings.values().stream().anyMatch(b->b.getX()==x&&b.getY()==y);
        return !unitsIn && !buildingsIn;
    }

    private void checkCoord(int x, int y){
        if(x < 0 || x >= width || y < 0 || y >= height){
            throw new IllegalArgumentException("Координаты выходят за границы карты");
        }
    }

}














