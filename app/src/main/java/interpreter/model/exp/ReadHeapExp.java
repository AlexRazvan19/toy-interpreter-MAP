package interpreter.model.exp;

// TODO: ReadHeapExp(IExp exp) – E6, toString "rH(" + exp + ")"
// the value of exp must be a RefValue; its address must be in the heap

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;

public record ReadHeapExp(IExp exp) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap)
        throws MyException {
        var value = exp.eval(symTable, heap);

        if (!(value instanceof RefValue ref))
            throw new ExpressionException("rH: " + exp + " is not a reference");

        if (!heap.isDefined(ref.address()))
            throw new ExpressionException("rH: address " + ref.address()
                + " is not in the heap");

        return heap.get(ref.address());
    }
    
    @Override
    public String toString() {
        return "rH(" + exp + ")";
    }
}
