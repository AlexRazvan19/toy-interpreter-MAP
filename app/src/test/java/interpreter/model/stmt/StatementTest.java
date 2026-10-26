package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatementTest {
    private static PrgState state(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );
    }

    @Test
    void compStmtExecutesFirstThenSecond() throws MyException {
        PrgState state = state(new NopStmt());
        IStmt first = new NopStmt();
        IStmt second = new NopStmt();

        new CompStmt(first, second).execute(state);

        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
    }

    @Test
    void variableDeclarationStoresDefaultValue() throws MyException {
        PrgState state = state(new NopStmt());

        new VarDeclStmt("x", new IntType()).execute(state);

        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void redeclaredVariableThrows() throws MyException {
        PrgState state = state(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);

        assertThrows(
                StatementException.class,
                () -> new VarDeclStmt("x", new BoolType()).execute(state)
        );
    }

    @Test
    void assignmentUpdatesDeclaredVariable() throws MyException {
        PrgState state = state(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);

        new AssignStmt("x", new ValueExp(new IntValue(5))).execute(state);

        assertEquals(new IntValue(5), state.getSymTable().lookup("x"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        PrgState state = state(new NopStmt());

        assertThrows(
                StatementException.class,
                () -> new AssignStmt("x", new ValueExp(new IntValue(5))).execute(state)
        );
    }

    @Test
    void assignmentTypeMismatchThrows() throws MyException {
        PrgState state = state(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);

        assertThrows(
                StatementException.class,
                () -> new AssignStmt("x", new ValueExp(new BoolValue(true))).execute(state)
        );
    }

    @Test
    void printAppendsEvaluatedValueToOutput() throws MyException {
        PrgState state = state(new NopStmt());

        new PrintStmt(new ValueExp(new IntValue(7))).execute(state);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(7), state.getOut().get(0));
    }

    @Test
    void ifPushesThenOrElseStatement() throws MyException {
        IStmt thenStatement = new NopStmt();
        IStmt elseStatement = new PrintStmt(new ValueExp(new IntValue(1)));
        PrgState state = state(new NopStmt());

        new IfStmt(
                new ValueExp(new BoolValue(true)),
                thenStatement,
                elseStatement
        ).execute(state);

        assertSame(thenStatement, state.getExeStack().pop());
    }

    @Test
    void nonBooleanIfConditionThrows() {
        PrgState state = state(new NopStmt());

        assertThrows(
                StatementException.class,
                () -> new IfStmt(
                        new ValueExp(new IntValue(1)),
                        new NopStmt(),
                        new NopStmt()
                ).execute(state)
        );
    }

    @Test
    void nopLeavesStateUnchanged() throws MyException {
        PrgState state = state(new NopStmt());
        int outputSize = state.getOut().size();

        PrgState result = new NopStmt().execute(state);

        assertSame(state, result);
        assertEquals(outputSize, result.getOut().size());
    }
}
