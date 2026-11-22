package interpreter.view;

import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.IfStmt;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.stmt.WhileStmt;
import interpreter.model.stmt.WriteHeapStmt;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.exp.*;
import interpreter.model.stmt.CloseRFileStmt;
import interpreter.model.stmt.OpenRFileStmt;
import interpreter.model.stmt.ReadFileStmt;
import interpreter.model.type.StringType;
import interpreter.model.value.StringValue;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.WriteHeapStmt;
import interpreter.model.type.RefType;

public class Examples {
    // Ex1: int v; v = 2; print(v)
    public static final IStmt EX1 = new CompStmt(
            new VarDeclStmt("v", new IntType()),
            new CompStmt(
                    new AssignStmt("v", new ValueExp(new IntValue(2))),
                    new PrintStmt(new VarExp("v"))
            )
    );

    // Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
    public static final IStmt EX2 = new CompStmt(
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
    public static final IStmt EX3 = new CompStmt(
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

    public static IStmt fileExample(String fileName) {
    return new CompStmt(
            new VarDeclStmt("varf", new StringType()),
            new CompStmt(
                    new AssignStmt("varf",
                            new ValueExp(new StringValue(fileName))),
                    new CompStmt(
                            new OpenRFileStmt(new VarExp("varf")),
                            new CompStmt(
                                    new VarDeclStmt("varc", new IntType()),
                                    new CompStmt(
                                            new ReadFileStmt(new VarExp("varf"), "varc"),
                                            new CompStmt(
                                                    new PrintStmt(new VarExp("varc")),
                                                    new CompStmt(
                                                            new ReadFileStmt(new VarExp("varf"), "varc"),
                                                            new CompStmt(
                                                                    new PrintStmt(new VarExp("varc")),
                                                                    new CloseRFileStmt(new VarExp("varf"))
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
    );
 }

 // Ex5: int a; int b; a = 5; b = 7;
// if a < b then print(a) else print(b)
    public static final IStmt EX5 = new CompStmt(
        new VarDeclStmt("a", new IntType()),
        new CompStmt(
                new VarDeclStmt("b", new IntType()),
                new CompStmt(
                        new AssignStmt("a", new ValueExp(new IntValue(5))),
                        new CompStmt(
                                new AssignStmt("b", new ValueExp(new IntValue(7))),
                                new IfStmt(
                                        new RelationalExp(
                                                RelOp.LT,
                                                new VarExp("a"),
                                                new VarExp("b")
                                        ),
                                        new PrintStmt(new VarExp("a")),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        )
);

    public static final IStmt EX6 = new CompStmt(
        new VarDeclStmt("v", new RefType(new IntType())),
        new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                        new CompStmt(
                                new NewStmt("a", new VarExp("v")),
                                new CompStmt(
                                        new PrintStmt(new VarExp("v")),
                                        new PrintStmt(new VarExp("a"))
                                )
                        )
                )
        )
);

    public static final IStmt EX7 = new CompStmt(
        new VarDeclStmt("v", new RefType(new IntType())),
        new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                        new CompStmt(
                                new NewStmt("a", new VarExp("v")),
                                new CompStmt(
                                        new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                        new PrintStmt(new ArithExp(
                                                ArithOp.ADD,
                                                new ReadHeapExp(
                                                        new ReadHeapExp(new VarExp("a"))
                                                ),
                                                new ValueExp(new IntValue(5))
                                        ))
                                )
                        )
                )
        )
);

    public static final IStmt EX8 = new CompStmt(
        new VarDeclStmt("v", new RefType(new IntType())),
        new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))),
                new CompStmt(
                        new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                        new CompStmt(
                                new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                new PrintStmt(new ArithExp(
                                        ArithOp.ADD,
                                        new ReadHeapExp(new VarExp("v")),
                                        new ValueExp(new IntValue(5))
                                ))
                        )
                )
        )
);

     public static final IStmt EX9 = new CompStmt(
        new VarDeclStmt("v", new RefType(new IntType())),
        new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                        new CompStmt(
                                new NewStmt("a", new VarExp("v")),
                                new CompStmt(
                                        new NewStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(
                                                new ReadHeapExp(
                                                        new ReadHeapExp(new VarExp("a"))
                                                )
                                        )
                                )
                        )
                )
        )
);

    public static final IStmt EX10 =
            new CompStmt(
                    new VarDeclStmt("v", new IntType()),
                    new CompStmt(
                            new AssignStmt(
                                    "v",
                                    new ValueExp(new IntValue(4))
                            ),
                            new CompStmt(
                                    new WhileStmt(
                                            new RelationalExp(
                                                    RelOp.GT,
                                                    new VarExp("v"),
                                                    new ValueExp(new IntValue(0))
                                            ),
                                            new CompStmt(
                                                    new PrintStmt(new VarExp("v")),
                                                    new AssignStmt(
                                                            "v",
                                                            new ArithExp(
                                                                    ArithOp.SUB,
                                                                    new VarExp("v"),
                                                                    new ValueExp(new IntValue(1))
                                                            )
                                                    )
                                            )
                                    ),
                                    new PrintStmt(new VarExp("v"))
                            )
                    )
            );
 

public static final IStmt EX4 = fileExample("test.in");

    public IStmt getEx1() {
        return EX1;
    }

    public IStmt getEx2() {
        return EX2;
    }

    public IStmt getEx3() {
        return EX3;
    }

    public IStmt getEx4(){
        return EX4;
    }
    public IStmt getEx5() {
        return EX5;
    }
    public IStmt getEx6() { 
        return EX6; 
    }
    public IStmt getEx7() { 
        return EX7; 
    }
    public IStmt getEx8() { 
        return EX8; 
    }
    public IStmt getEx9() { 
        return EX9; 
    }
    public IStmt getEx10() {
        return EX10;
    }
}
