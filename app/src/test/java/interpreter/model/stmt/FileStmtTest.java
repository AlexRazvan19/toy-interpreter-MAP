package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;

    @BeforeEach
    void setUp() throws Exception {
        Path inputFile = dir.resolve("numbers.in");
        Files.writeString(inputFile, "7\n");
        file = new ValueExp(new StringValue(inputFile.toString()));
        state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt(),
                new MyDictionary<>(),
                new MyHeap()
        );
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertThrows(
                StatementException.class,
                () -> new OpenRFileStmt(file).execute(state)
        );
        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void openingFileAddsItToFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertEquals(1, state.getFileTable().toMap().size());

        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void openingNonStringOrMissingFileThrows() {
        assertThrows(
                StatementException.class,
                () -> new OpenRFileStmt(
                        new ValueExp(new IntValue(1))
                ).execute(state)
        );

        assertThrows(
                StatementException.class,
                () -> new OpenRFileStmt(
                        new ValueExp(new StringValue(dir.resolve("missing.in").toString()))
                ).execute(state)
        );
    }

    @Test
    void readingWithInvalidStateThrows() throws MyException {
        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "missing").execute(state)
        );

        new VarDeclStmt("text", new StringType()).execute(state);
        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "text").execute(state)
        );

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state)
        );
    }

    @Test
    void readingNonIntegerLineThrows() throws Exception {
        Path invalidFile = dir.resolve("invalid.in");
        Files.writeString(invalidFile, "not-a-number\n");
        ValueExp invalidFileExpression =
                new ValueExp(new StringValue(invalidFile.toString()));

        new OpenRFileStmt(invalidFileExpression).execute(state);

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(invalidFileExpression, "x").execute(state)
        );

        new CloseRFileStmt(invalidFileExpression).execute(state);
    }

    @Test
    void closingFileRemovesItAndClosingMissingFileThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);

        assertTrue(state.getFileTable().toMap().isEmpty());
        assertThrows(
                StatementException.class,
                () -> new CloseRFileStmt(file).execute(state)
        );
    }
}