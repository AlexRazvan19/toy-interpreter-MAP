package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArithExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static IExp num(int number) {
        return new ValueExp(new IntValue(number));
    }

    @Test
    void respectsOperatorPrecedenceEncodedInTheTree() throws MyException {
        IExp expression = new ArithExp(
                ArithOp.ADD,
                num(2),
                new ArithExp(
                        ArithOp.MUL,
                        num(3),
                        num(5)
                )
        );

        assertEquals(new IntValue(17), expression.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp expression = new ArithExp(
                ArithOp.DIV,
                num(1),
                num(0)
        );

        ExpressionException exception = assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );

        assertEquals("division by zero", exception.getMessage());
    }

    @Test
    void evaluatesEveryArithmeticOperator() throws MyException {
        assertEquals(new IntValue(7),
                new ArithExp(ArithOp.ADD, num(3), num(4)).eval(symTable));
        assertEquals(new IntValue(1),
                new ArithExp(ArithOp.SUB, num(3), num(2)).eval(symTable));
        assertEquals(new IntValue(12),
                new ArithExp(ArithOp.MUL, num(3), num(4)).eval(symTable));
        assertEquals(new IntValue(2),
                new ArithExp(ArithOp.DIV, num(6), num(3)).eval(symTable));
    }

    @Test
    void nonIntegerOperandThrows() {
        IExp expression = new ArithExp(
                ArithOp.ADD,
                new ValueExp(new IntValue(1)),
                new ValueExp(new interpreter.model.value.BoolValue(true))
        );

        assertThrows(ExpressionException.class, () -> expression.eval(symTable));
    }
}
