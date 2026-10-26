package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogicExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    @Test
    void evaluatesAndAndOr() throws MyException {
        assertEquals(
                new BoolValue(false),
                new LogicExp(
                        LogicOp.AND,
                        new ValueExp(new BoolValue(true)),
                        new ValueExp(new BoolValue(false))
                ).eval(symTable)
        );
        assertEquals(
                new BoolValue(true),
                new LogicExp(
                        LogicOp.OR,
                        new ValueExp(new BoolValue(true)),
                        new ValueExp(new BoolValue(false))
                ).eval(symTable)
        );
    }

    @Test
    void nonBooleanOperandThrows() {
        IExp expression = new LogicExp(
                LogicOp.AND,
                new ValueExp(new IntValue(1)),
                new ValueExp(new BoolValue(true))
        );

        assertThrows(ExpressionException.class, () -> expression.eval(symTable));
    }

    @Test
    void undefinedVariableThrows() {
        IExp expression = new VarExp("missing");

        assertThrows(ExpressionException.class, () -> expression.eval(symTable));
    }
}
