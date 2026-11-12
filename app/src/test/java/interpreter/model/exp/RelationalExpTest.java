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

class RelationalExpTest {
    private final IDictionary<String, IValue> symTable =
            new MyDictionary<>();

    private static IExp number(int value) {
        return new ValueExp(new IntValue(value));
    }

    @Test
    void evaluatesAllRelationalOperatorsToTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LT, number(1), number(2)).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LT, number(2), number(1)).eval(symTable));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LE, number(1), number(1)).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LE, number(2), number(1)).eval(symTable));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.EQ, number(1), number(1)).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.EQ, number(1), number(2)).eval(symTable));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.NE, number(1), number(2)).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.NE, number(1), number(1)).eval(symTable));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GT, number(2), number(1)).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GT, number(1), number(2)).eval(symTable));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GE, number(1), number(1)).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GE, number(1), number(2)).eval(symTable));
    }

    @Test
    void nonIntegerOperandThrows() {
        assertThrows(
                ExpressionException.class,
                () -> new RelationalExp(
                        RelOp.LT,
                        new ValueExp(new IntValue(1)),
                        new ValueExp(new BoolValue(true))
                ).eval(symTable)
        );
    }
}
