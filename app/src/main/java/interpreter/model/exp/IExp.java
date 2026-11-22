package interpreter.model.exp;

import interpreter.model.value.IValue;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.exceptions.MyException;

public interface IExp {
 IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException;
}

