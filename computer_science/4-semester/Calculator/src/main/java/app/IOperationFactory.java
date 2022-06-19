package app;


import app.operations.MathOperation;

public interface IOperationFactory {

    MathOperation getOperation(String command);
}