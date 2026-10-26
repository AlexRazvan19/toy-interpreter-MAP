package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {
    private static PrgState run(IStmt program) throws MyException {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        new Controller(new Repository(state), false).allStep();
        return state;
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        IStmt program = new Examples().getEx2();
        PrgState state = run(program);

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void allExamplesProduceExpectedOutput() throws MyException {
        Examples examples = new Examples();

        assertEquals(new IntValue(2), run(examples.getEx1()).getOut().get(0));
        assertEquals(new IntValue(18), run(examples.getEx2()).getOut().get(0));
        assertEquals(new IntValue(2), run(examples.getEx3()).getOut().get(0));
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = run(new interpreter.model.stmt.NopStmt());

        assertTrue(state.getExeStack().isEmpty());
        assertThrows(
                interpreter.exceptions.StatementException.class,
                () -> new Controller(
                        new Repository(state),
                        false
                ).oneStep(state)
        );
    }
}
