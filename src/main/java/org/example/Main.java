import org.example.Command;
import org.example.Executer;
import org.example.IGameEngine;
import org.example.GameEngine;
import org.example.GameScript;

public static void main(String[] args){
    GameScript script = new GameScript();
    script.add(new Command("initMap", "10", "10"));
    script.add(new Command("addUnit", "unit1", "warrior", "1", "1"));
    script.add(new Command("addUnit", "unit2", "archer", "2", "2"));
    script.add(new Command("addUnit", "unit3", "warrior", "4", "4"));
    script.add(new Command("move", "unit1", "3", "3"));
    script.add(new Command("startGame"));
    script.add(new Command("move", "unit1", "-1", "3"));
    script.add(new Command("move", "unit1", "3", "3"));
    script.add(new Command("move", "unit2", "3", "3"));
    script.add(new Command("attack", "unit1", "unit2"));
    script.add(new Command("build", "barracks1", "barracks", "5", "5"));
    script.add(new Command("endTurn"));
    script.add(new Command("print"));

    System.out.println("=== Скрипт (обход через for-each) ===");
    for (Command c : script) System.out.println(c);

    System.out.println();
    System.out.println("=== Анализ скрипта (Stream API) ===");
    System.out.println("1. Самая частая команда:       " + script.mostPopularCommand());
    System.out.println("2. Уникальные id юнитов:        " + script.uniqueUnitIds());
    System.out.println("3. Команды по частоте:          " + script.commandsByFrequency());
    System.out.println("4. Есть ли attack:              " + script.containsCommandType("attack"));
    System.out.println("   Есть ли cheatCode:           " + script.containsCommandType("cheatCode"));
    System.out.println("5. Создано сущностей по типам:  " + script.countCreatedEntitiesByType());

    System.out.println();
    System.out.println("=== Выполнение скрипта ===");
    IGameEngine engine = new GameEngine();
    Executer exec = new Executer(engine);
    exec.run(script);

    System.out.println();
    System.out.println("=== Добавление новой команды в движок ===");
    engine.exec(new Command("cheatCode", "infinite_armor"));
    ((GameEngine) engine).registerCommand("cheatCode", a -> {
        System.out.println("Выполняется чит-код: " + a[0]);
    });
    engine.exec(new Command("cheatCode", "infinite_armor"));
}
