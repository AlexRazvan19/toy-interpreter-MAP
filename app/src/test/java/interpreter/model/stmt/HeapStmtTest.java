package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeapStmtTest {
    private PrgState state;
    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state); // Ref int v
    }

    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newWithAValueOfTheWrongTypeThrows() {
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new NewStmt("v", exp).execute(state));
    }

    @Test
    void whileWithANonBooleanConditionThrows() {
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }

    @Test
    void newWithAnUndeclaredVariableThrows() {
        assertThrows(
                StatementException.class,
                () -> new NewStmt("missing", new ValueExp(new IntValue(1))).execute(state)
        );
    }

    @Test
    void newWithANonReferenceVariableThrows() throws MyException {
        PrgState plainState = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(plainState);

        assertThrows(
                StatementException.class,
                () -> new NewStmt("x", new ValueExp(new IntValue(1))).execute(plainState)
        );
    }

    @Test
    void writeHeapChangesTheLocationAndChecksItsType() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);

        assertEquals(new IntValue(30), state.getHeap().get(1));
    }

    @Test
    void writeHeapRejectsUndeclaredAndNonReferenceVariables() throws MyException {
        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "missing",
                        new ValueExp(new IntValue(1))
                ).execute(state)
        );

        PrgState plainState = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(plainState);
        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "x",
                        new ValueExp(new IntValue(1))
                ).execute(plainState)
        );
    }

    @Test
    void writeHeapRejectsAWrongValueTypeAndMissingAddress() throws MyException {
        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "v",
                        new ValueExp(new IntValue(30))
                ).execute(state)
        );

        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "v",
                        new ValueExp(new BoolValue(true))
                ).execute(state)
        );
    }

    @Test
    void whileTruePushesThisAndTheBodyAndFalsePushesNothing() throws MyException {
        var falseLoop = new WhileStmt(
                new ValueExp(new BoolValue(false)),
                new NopStmt()
        );
        falseLoop.execute(state);
        assertEquals(1, state.getExeStack().toList().size());

        IStmt body = new NopStmt();
        var trueLoop = new WhileStmt(
                new ValueExp(new BoolValue(true)),
                body
        );
        trueLoop.execute(state);

        assertEquals(body, state.getExeStack().pop());
        assertEquals(trueLoop, state.getExeStack().pop());
    }
}

