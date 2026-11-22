package interpreter.controller;

import interpreter.model.value.IValue;
import java.util.Set;
import java.util.Collection;
import interpreter.model.value.RefValue;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayDeque;
import java.util.HashSet;


public final class GarbageCollector {
    private GarbageCollector() {}
    /** The addresses that occur in the RefValues of the SymTable. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream().filter(v -> v instanceof RefValue).map(v -> ((RefValue) v).address())
            .collect(Collectors.toSet());
    }
    /** UNSAFE: keeps only the heap entries referred to directly from the SymTable. */
    public static Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr,
        Map<Integer, IValue> heap) {
            return heap.entrySet().stream().filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<Integer> reachableAddresses(
            Set<Integer> roots,
            Map<Integer, IValue> heap) {
        Set<Integer> reachable = new HashSet<>(roots);
        var worklist = new ArrayDeque<>(roots);

        while (!worklist.isEmpty()) {
            Integer address = worklist.remove();
            IValue value = heap.get(address);

            if (value instanceof RefValue ref
                    && reachable.add(ref.address())) {
                worklist.add(ref.address());
            }
        }

        return reachable;
    }

    public static Map<Integer, IValue> safeGarbageCollector(
            Set<Integer> symTableAddr,
            Map<Integer, IValue> heap) {
        Set<Integer> reachable =
                reachableAddresses(symTableAddr, heap);

        return heap.entrySet().stream()
                .filter(entry -> reachable.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}
