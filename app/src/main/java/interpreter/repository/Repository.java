package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> programs;
    private final String logFilePath;

    public Repository(PrgState program, String logFilePath) {
        this.programs = new ArrayList<>();
        this.programs.add(program);
        this.logFilePath = logFilePath;
    }

    public Repository(PrgState program) {
        this(program, "log.txt");
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

    @Override
    public void logPrgStateExec() throws MyException {
        try (var logFile = new PrintWriter(
            new BufferedWriter(new FileWriter(logFilePath, true)))) { // append
            logFile.println(getCrtPrg().toLogString());
        } catch (IOException e) {
            throw new RepositoryException("cannot write the log file " + logFilePath
                + ": " + e.getMessage());
        }
    }
}
