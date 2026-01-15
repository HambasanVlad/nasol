package view;

import controller.Controller;
import exception.MyException;
import model.PrgState;
import model.adt.*;
import model.expression.*;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;
import java.util.Scanner;

public class View {

    public static void main(String[] args) {

        // --- Exemplele de la A2 ---

        // Ex 1: int v; v=2; Print(v)
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        // Ex 2: int a; int b; a=2+3*5; b=a+1; Print(b)
        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)),
                                new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"),
                                        new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));

        // Ex 3: bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),
                                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))))));

        // Ex 4: (Error) Division by zero
        IStmt ex4_div_by_zero = new CompStmt(new VarDeclStmt("x", new IntType()),
                new CompStmt(new AssignStmt("x", new ValueExp(new IntValue(10))),
                        new CompStmt(new AssignStmt("x", new ArithExp('/', new VarExp("x"), new ValueExp(new IntValue(0)))),
                                new PrintStmt(new VarExp("x")))));

        // Ex 5: (Error) Type mismatch
        IStmt ex5_type_mismatch = new CompStmt(new VarDeclStmt("b", new BoolType()),
                new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(5))),
                        new PrintStmt(new VarExp("b"))));


        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Choose an A2 option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Exiting A2 View...");
                break;
            }

            IStmt selectedProgram = null;
            switch (choice) {
                case 1: selectedProgram = ex1; break;
                case 2: selectedProgram = ex2; break;
                case 3: selectedProgram = ex3; break;
                case 4: selectedProgram = ex4_div_by_zero; break;
                case 5: selectedProgram = ex5_type_mismatch; break;
                default:
                    System.out.println("Invalid option.");
                    continue;
            }

            // --- Inițializare ---
            MyIStack<IStmt> exeStack = new MyStack<>();
            MyIDictionary<String, Value> symTable = new MyDictionary<>();
            MyIList<Value> out = new MyList<>();
            MyIDictionary<StringValue, BufferedReader> fileTable = new MyDictionary<>();

            // Aici folosim noul constructor cu Heap (A4/A5)
            PrgState prgState = new PrgState(exeStack, symTable, out, fileTable, new MyHeap(), selectedProgram);

            IRepository repo = new Repository(prgState, "log_A2_Demo.txt");
            Controller ctrl = new Controller(repo);

            // --- Rulare (CORECȚIA ESTE AICI) ---
            try {
                ctrl.allStep();
            } catch (InterruptedException e) {
                // Prindem eroarea specifica thread-urilor
                System.out.println("\n!!! Execution interrupted !!!");
            } catch (Exception e) {
                // Prindem orice alta eroare (inclusiv MyException runtime)
                System.out.println("\n!!! An error occurred !!!: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- [A2 DEMO] Toy Language Interpreter ---");
        System.out.println("1. Run Example 1");
        System.out.println("2. Run Example 2");
        System.out.println("3. Run Example 3");
        System.out.println("4. Run Example 4 (Error: Div by 0)");
        System.out.println("5. Run Example 5 (Error: Type mismatch)");
        System.out.println("0. Exit [A2 DEMO]");
    }
}