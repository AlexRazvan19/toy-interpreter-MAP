package interpreter.model.value;

import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringValueTest {
    @Test
    void stringVariableHasEmptyStringAsDefaultValue() throws Exception {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt(),
                new MyDictionary<>()
        );

        new VarDeclStmt("text", new StringType()).execute(state);

        assertEquals(new StringValue(""), state.getSymTable().lookup("text"));
    }

    @Test
    void equalStringValuesHaveEqualHashCodes() {
        StringValue first = new StringValue("hello");
        StringValue second = new StringValue("hello");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}
