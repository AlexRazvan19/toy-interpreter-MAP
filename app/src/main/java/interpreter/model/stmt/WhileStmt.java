package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.BoolValue;
import interpreter.exceptions.MyException;

public record WhileStmt(IExp cond, IStmt body) implements IStmt { // S13
    @Override
    public PrgState execute(PrgState state) throws MyException {
    // TODO: evaluate cond; not a BoolValue -> StatementException
    // true: push this, then push body; false: do nothing
    IValue condValue = cond.eval(state.getSymTable(), state.getHeap());
    if (!(condValue instanceof BoolValue(boolean condBool)))
        throw new StatementException("while: condition is not a boolean");

    if (condBool) {
        state.getExeStack().push(this);
        state.getExeStack().push(body);
    }
    return state;
    }

    @Override 
    public String toString() { 
        return "(while (" + cond + ") " + body + ")"; 
    }
}