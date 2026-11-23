package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;
import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReadHeapExpTest {
    private final IDictionary<String, interpreter.model.value.IValue> symTable =
            new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    @Test
    void readsTheValueStoredAtAReferenceAddress() throws Exception {
        heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(1, new IntType()));

        IValue result = new ReadHeapExp(new VarExp("v")).eval(symTable, heap);

        assertEquals(new IntValue(20), result);
    }

    @Test
    void rejectsANonReferenceExpression() {
        assertThrows(
                ExpressionException.class,
                () -> new ReadHeapExp(new ValueExp(new BoolValue(true)))
                        .eval(symTable, heap)
        );
    }

    @Test
    void rejectsAnAddressThatIsNotInTheHeap() {
        symTable.put("v", new RefValue(1, new IntType()));

        assertThrows(
                ExpressionException.class,
                () -> new ReadHeapExp(new VarExp("v")).eval(symTable, heap)
        );
    }
}
