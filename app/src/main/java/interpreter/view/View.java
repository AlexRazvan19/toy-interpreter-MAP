package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.repository.Repository;

import java.util.Scanner;

public class View {
    private final Examples examples = new Examples();
    private final Scanner scanner = new Scanner(System.in);
    private IStmt selectedProgram;

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> selectProgram();
                case "2" -> runSelectedProgram();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Select a program");
        System.out.println("2. Run the selected program");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void selectProgram() {
        System.out.println();
        System.out.println("1. int v; v = 2; print(v)");
        System.out.println("2. int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
        System.out.println("3. bool a; int v; if a then v = 2 else v = 3; print(v)");
        System.out.print("Choose a program: ");

        String option = scanner.nextLine().trim();
        switch (option) {
            case "1" -> selectedProgram = examples.getEx1();
            case "2" -> selectedProgram = examples.getEx2();
            case "3" -> selectedProgram = examples.getEx3();
            default -> {
                System.out.println("Invalid program.");
                return;
            }
        }

        System.out.println("Program selected.");
    }

    private void runSelectedProgram() {
        if (selectedProgram == null) {
            System.out.println("Please select a program first.");
            return;
        }

        try {
            PrgState state = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<IValue>(),
                    selectedProgram
            );
            Controller controller = new Controller(new Repository(state), true);
            controller.allStep();
            System.out.println("Program output: " + state.getOut());
        } catch (MyException | RuntimeException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }
}
