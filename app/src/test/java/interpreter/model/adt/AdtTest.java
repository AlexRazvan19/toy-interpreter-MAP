package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdtTest {
    @Test
    void stackIsLifo() throws ADTException {
        IStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);

        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void emptyStackPopThrows() {
        IStack<Integer> stack = new MyStack<>();

        assertThrows(ADTException.class, stack::pop);
    }

    @Test
    void dictionarySupportsPutLookupAndUpdate() throws ADTException {
        IDictionary<String, IntValue> dictionary = new MyDictionary<>();
        dictionary.put("x", new IntValue(1));

        assertTrue(dictionary.isDefined("x"));
        assertEquals(new IntValue(1), dictionary.lookup("x"));

        dictionary.update("x", new IntValue(2));
        assertEquals(new IntValue(2), dictionary.lookup("x"));
    }

    @Test
    void missingDictionaryKeyThrows() {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();

        assertFalse(dictionary.isDefined("missing"));
        assertThrows(ADTException.class, () -> dictionary.lookup("missing"));
        assertThrows(ADTException.class, () -> dictionary.update("missing", 1));
    }

    @Test
    void listSupportsAddGetAndSize() throws ADTException {
        IList<String> list = new MyList<>();
        list.add("first");
        list.add("second");

        assertEquals(2, list.size());
        assertEquals("first", list.get(0));
        assertEquals("second", list.get(1));
    }

    @Test
    void listInvalidIndexThrows() {
        IList<String> list = new MyList<>();

        assertThrows(ADTException.class, () -> list.get(0));
    }
}
