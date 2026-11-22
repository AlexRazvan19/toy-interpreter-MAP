package interpreter.model.stmt;

// TODO: WriteHeapStmt(String varName, IExp exp) – S12, toString "wH(…, …)"
// check the variable, the address and the type, then heap.update(…)

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public record WriteHeapStmt(String varName, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new StatementException("wH: " + varName + " is not declared");
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("wH: " + varName + " is not a reference");
        if (!state.getHeap().isDefined(ref.address()))
            throw new StatementException("wH: address " + ref.address()
                + " is not in the heap");
        IValue value = exp.eval(symTable, state.getHeap());
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("wH: " + exp + " has type " + value.getType()
                + ", expected " + ref.locationType());
        state.getHeap().update(ref.address(), value);
        return state;
    }
    @Override 
    public String toString() { 
        return "wH(" + varName + ", " + exp + ")"; 
    }
}
