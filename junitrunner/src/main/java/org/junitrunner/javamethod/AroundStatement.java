package org.junitrunner.javamethod;

import org.junit.runners.model.Statement;

public class AroundStatement extends Statement {

    private final Statement next;

    protected AroundStatement(Statement next) {
        this.next = next;
    }

    protected void before() throws Throwable {
    }

    protected void after(@SuppressWarnings("unused") Throwable failure) throws Throwable {
    }

    @Override
    public void evaluate() throws Throwable {

        before();

        Throwable failure = null;
        try {
            next.evaluate();
        } catch (Throwable exc) {
            failure = exc;
        }

        try {
            after(failure);
        } catch (Throwable exc) {
            if (failure == null) {
                throw exc;
            }
        }

        if (failure != null) {
            throw failure;
        }
    }
}
