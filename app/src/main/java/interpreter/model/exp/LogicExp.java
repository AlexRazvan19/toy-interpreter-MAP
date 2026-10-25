package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record LogicExp(LogicOp op, IExp left, IExp right) implements IExp{
    
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException{
        IValue leftValue = left.eval(symTable);

        if(!(leftValue instanceof BoolValue(boolean leftBoolean))){
            throw new ExpressionException("first operand is not boolean");
        }

        IValue rightValue = right.eval(symTable);
        if(!(rightValue instanceof BoolValue(boolean rightBoolean))){
            throw new ExpressionException("second operand is not boolean");
        }
        return switch (op){
            case AND -> new BoolValue(leftBoolean && rightBoolean);
            case OR -> new BoolValue(leftBoolean || rightBoolean);
        };
    }

}