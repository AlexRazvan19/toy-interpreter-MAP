package interpreter.repository;

import interpreter.model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> programs;

    public Repository(PrgState program) {
        this.programs = new ArrayList<>();
        this.programs.add(program);
    }

    @Override
    public PrgState getCrtPrg() {
        if (programs.isEmpty()) {
            throw new IllegalStateException("repository contains no program states");
        }

        return programs.get(0);
    }

    public void addProgram(PrgState program) {
        if (program == null) {
            throw new IllegalArgumentException("program state cannot be null");
        }

        programs.add(program);
    }
}
