package org.example;

//import java.util.Arrays;

public class Executer {
    private final IGameEngine engine;

    public Executer(IGameEngine engine){
        this.engine = engine;
    }

    public void run(Iterable<Command> program){
        for(Command c : program){
            System.out.println("> "+c);
            engine.exec(c);
        }
    }
    /*
    public void run(Command[] program){
        run(Arrays.asList(program));
    }
    */
}
