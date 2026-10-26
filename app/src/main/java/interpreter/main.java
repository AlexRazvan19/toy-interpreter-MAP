package interpreter;

import interpreter.view.View;

public class Main {
    public String getGreeting() {
        return "Toy interpreter";
    }

    public static void main(String[] args) {
        new View().run();
    }
}
