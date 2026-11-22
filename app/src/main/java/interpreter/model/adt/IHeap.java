package interpreter.model.adt;

import java.util.Map;
import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

public interface IHeap {
    int allocate(IValue value); // stores value at a new address
    IValue get(int address) throws ADTException;
    void update(int address, IValue value) throws ADTException;
    boolean isDefined(int address);
    Map<Integer, IValue> getContent(); // a read-only copy
    void setContent(Map<Integer, IValue> content); // used by the garbage collector
}
