package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IntValue;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {
    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program,
                new MyDictionary<>(),
                new MyHeap()
        );
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertEquals(new IntValue(15), state.getOut().get(0));
        assertEquals(new IntValue(50), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExamplePrintsZeroesForAnEmptyFile() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertEquals(new IntValue(0), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void ex5PrintsFive() throws MyException {
        PrgState state = run(Examples.EX5);

        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void ex6PrintsTheReferenceValues() throws MyException {
        PrgState state = run(Examples.EX6);

        assertEquals(new RefValue(1, new IntType()), state.getOut().get(0));
        assertEquals(
                new RefValue(2, new RefType(new IntType())),
                state.getOut().get(1)
        );
    }

    @Test
    void ex7PrintsTwentyAndTwentyFive() throws MyException {
        PrgState state = run(Examples.EX7);

        assertEquals(new IntValue(20), state.getOut().get(0));
        assertEquals(new IntValue(25), state.getOut().get(1));
    }

    @Test
    void ex8PrintsTwentyAndThirtyFive() throws MyException {
        PrgState state = run(Examples.EX8);

        assertEquals(new IntValue(20), state.getOut().get(0));
        assertEquals(new IntValue(35), state.getOut().get(1));
    }

    @Test
    void ex9PrintsTwentyWithSafeGarbageCollection() throws MyException {
        PrgState state = run(Examples.EX9);

        assertEquals(new IntValue(20), state.getOut().get(0));
    }

    @Test
    void ex10PrintsFourThroughZero() throws MyException {
        PrgState state = run(Examples.EX10);

        assertEquals(new IntValue(4), state.getOut().get(0));
        assertEquals(new IntValue(3), state.getOut().get(1));
        assertEquals(new IntValue(2), state.getOut().get(2));
        assertEquals(new IntValue(1), state.getOut().get(3));
        assertEquals(new IntValue(0), state.getOut().get(4));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(new Examples().getEx1());

        String log = Files.readString(dir.resolve("log.txt"));
        int exeStack = log.indexOf("ExeStack:");
        int symTable = log.indexOf("SymTable:");
        int out = log.indexOf("Out:");
        int fileTable = log.indexOf("FileTable:");

        assertTrue(0 <= exeStack && exeStack < symTable
                && symTable < out && out < fileTable);
    }
}