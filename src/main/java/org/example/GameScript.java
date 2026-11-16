package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class GameScript implements Iterable<Command> {

    private static final Map<String, int[]> UNIT_ID_ARGS = Map.of(
            "addUnit", new int[]{0},
            "move",    new int[]{0},
            "attack",  new int[]{0, 1}
    );

    private static final Map<String, Integer> CREATED_TYPE_ARG = Map.of(
            "addUnit", 1,
            "build",   1
    );

    private static final Comparator<Map.Entry<String, Long>> BY_POPULARITY =
            Map.Entry.<String, Long>comparingByValue().reversed()
                    .thenComparing(Map.Entry.comparingByKey());

    private final List<Command> commands = new ArrayList<>();

    public GameScript add(Command command) {
        commands.add(java.util.Objects.requireNonNull(command, "command"));
        return this;
    }

    @Override
    public Iterator<Command> iterator() {
        return Collections.unmodifiableList(commands).iterator();
    }

    public String mostPopularCommand() {
        return commandCounts().entrySet().stream()
                .sorted(BY_POPULARITY)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public List<String> uniqueUnitIds() {
        return commands.stream()
                .flatMap(c -> argsAt(c, UNIT_ID_ARGS.getOrDefault(c.getName(), new int[0])))
                .distinct()
                .toList();
    }

    public List<String> commandsByFrequency() {
        return commandCounts().entrySet().stream()
                .sorted(BY_POPULARITY)
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean containsCommandType(String type) {
        return commands.stream().anyMatch(c -> c.getName().equals(type));
    }

    public Map<String, Long> countCreatedEntitiesByType() {
        return commands.stream()
                .filter(c -> CREATED_TYPE_ARG.containsKey(c.getName()))
                .filter(c -> c.getArgs().length > CREATED_TYPE_ARG.get(c.getName()))
                .collect(Collectors.groupingBy(
                        c -> c.getArgs()[CREATED_TYPE_ARG.get(c.getName())],
                        LinkedHashMap::new,
                        Collectors.counting()));
    }

    private Map<String, Long> commandCounts() {
        return commands.stream()
                .collect(Collectors.groupingBy(Command::getName, Collectors.counting()));
    }

    private static Stream<String> argsAt(Command c, int[] indexes) {
        String[] args = c.getArgs();
        return Arrays.stream(indexes)
                .filter(i -> i < args.length)
                .mapToObj(i -> args[i]);
    }
}
