package interpreter.view;

import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.IfStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;

public class Examples {
    // Ex1: int v; v = 2; print(v)
    IStmt ex1 = new CompStmt(
            new VarDeclStmt("v", new IntType()),
            new CompStmt(
                    new AssignStmt("v", new ValueExp(new IntValue(2))),
                    new PrintStmt(new VarExp("v"))
            )
    );

    // Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
    IStmt ex2 = new CompStmt(
            new VarDeclStmt("a", new IntType()),
            new CompStmt(
                    new VarDeclStmt("b", new IntType()),
                    new CompStmt(
                            new AssignStmt(
                                    "a",
                                    new ArithExp(
                                            ArithOp.ADD,
                                            new ValueExp(new IntValue(2)),
                                            new ArithExp(
                                                    ArithOp.MUL,
                                                    new ValueExp(new IntValue(3)),
                                                    new ValueExp(new IntValue(5))
                                            )
                                    )
                            ),
                            new CompStmt(
                                    new AssignStmt(
                                            "b",
                                            new ArithExp(
                                                    ArithOp.ADD,
                                                    new VarExp("a"),
                                                    new ValueExp(new IntValue(1))
                                            )
                                    ),
                                    new PrintStmt(new VarExp("b"))
                            )
                    )
            )
    );

    // Ex3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
    IStmt ex3 = new CompStmt(
            new VarDeclStmt("a", new BoolType()),
            new CompStmt(
                    new VarDeclStmt("v", new IntType()),
                    new CompStmt(
                            new AssignStmt(
                                    "a",
                                    new ValueExp(new BoolValue(true))
                            ),
                            new CompStmt(
                                    new IfStmt(
                                            new VarExp("a"),
                                            new AssignStmt(
                                                    "v",
                                                    new ValueExp(new IntValue(2))
                                            ),
                                            new AssignStmt(
                                                    "v",
                                                    new ValueExp(new IntValue(3))
                                            )
                                    ),
                                    new PrintStmt(new VarExp("v"))
                            )
                    )
            )
    );

    public IStmt getEx1() {
        return ex1;
    }

    public IStmt getEx2() {
        return ex2;
    }

    public IStmt getEx3() {
        return ex3;
    }
}
