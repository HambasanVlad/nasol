import controller.Controller;
import exception.MyException;
import model.PrgState;
import model.adt.*;
import model.expression.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import repository.IRepository;
import repository.Repository;
import view.ExitCommand;
import view.RunExample;
import view.TextMenu;

public class Interpreter {

    // --- HELPER METHOD: Verifică Tipuri -> Creează Controller -> Adaugă în Meniu ---
    private static void addProgram(TextMenu menu, String key, String description, IStmt program) {
        try {
            // PASUL 1: Type Check (Verificare Statică)
            // Creăm un mediu de tipuri gol
            MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
            program.typecheck(typeEnv);

            // PASUL 2: Dacă nu a crăpat, creăm starea de rulare
            PrgState prgState = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    new MyDictionary<>(),
                    new MyHeap(),
                    program
            );
            IRepository repo = new Repository(prgState, "log" + key + ".txt");
            Controller ctr = new Controller(repo);

            // PASUL 3: Adăugăm în meniu
            menu.addCommand(new RunExample(key, description, ctr));

        } catch (MyException e) {
            // Dacă TypeCheck eșuează, afișăm eroarea și NU adăugăm în meniu
            System.out.println("Error creating program " + key + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));

        // --- DEFINIRE EXEMPLE ---

        // Ex 1: int v; v=2; Print(v)
        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));
        addProgram(menu, "1", ex1.toString(), ex1);

        // Ex 2: int a; int b; ...
        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)),
                                new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"),
                                        new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
        addProgram(menu, "2", ex2.toString(), ex2);

        // Ex 3: if
        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),
                                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))))));
        addProgram(menu, "3", ex3.toString(), ex3);

        // Ex 4: Files
        IStmt ex4 = new CompStmt(
                new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new OpenRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf"))))))))));
        addProgram(menu, "4", ex4.toString(), ex4);

        // Ex 5: Relational
        IStmt ex5 = new CompStmt(new VarDeclStmt("x", new IntType()),
                new CompStmt(new AssignStmt("x", new ValueExp(new IntValue(10))),
                        new CompStmt(new VarDeclStmt("y", new IntType()),
                                new CompStmt(new AssignStmt("y", new ValueExp(new IntValue(12))),
                                        new CompStmt(new VarDeclStmt("b", new BoolType()),
                                                new CompStmt(new AssignStmt("b", new RelationalExp("<", new VarExp("x"), new VarExp("y"))),
                                                        new PrintStmt(new VarExp("b"))))))));
        addProgram(menu, "5", ex5.toString(), ex5);

        // Ex 6: Runtime Error (Div by 0) - TypeCheck trebuie să treacă! (int / int e valid ca tip)
        IStmt ex6 = new CompStmt(new VarDeclStmt("x", new IntType()),
                new CompStmt(new AssignStmt("x", new ValueExp(new IntValue(10))),
                        new CompStmt(new AssignStmt("x", new ArithExp('/', new VarExp("x"), new ValueExp(new IntValue(0)))),
                                new PrintStmt(new VarExp("x")))));
        addProgram(menu, "6", ex6.toString(), ex6);

        // Ex 7: Type Error (bool b; b=5) - TypeCheck trebuie să pice!
        IStmt ex7 = new CompStmt(new VarDeclStmt("b", new BoolType()),
                new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(5))),
                        new PrintStmt(new VarExp("b"))));
        // Asta va afisa eroare in consola la pornire si NU va aparea in meniu
        addProgram(menu, "7", ex7.toString(), ex7);

        // Ex 8: Heap Allocation
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));
        addProgram(menu, "8", ex8.toString(), ex8);

        // Ex 9: Heap Reading
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+', new ReadHeapExp(new ReadHeapExp(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));
        addProgram(menu, "9", ex9.toString(), ex9);

        // Ex 10: Heap Writing
        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+', new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5))))))));
        addProgram(menu, "10", ex10.toString(), ex10);

        // Ex 11: Garbage Collector
        IStmt ex11 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a")))))))));
        addProgram(menu, "11", ex11.toString(), ex11);

        // Ex 12: While Loop
        IStmt ex12 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelationalExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));
        addProgram(menu, "12", ex12.toString(), ex12);

        // Ex 13: Fork (A5)
        IStmt ex13 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new CompStmt(
                                                        // Delay mic pentru a vedea efectul concurentei
                                                        new VarDeclStmt("timer", new IntType()),
                                                        new CompStmt(new WhileStmt(new RelationalExp("<", new VarExp("timer"), new ValueExp(new IntValue(10))),
                                                                new AssignStmt("timer", new ArithExp('+', new VarExp("timer"), new ValueExp(new IntValue(1))))),
                                                                new PrintStmt(new ReadHeapExp(new VarExp("a")))))))))));
        addProgram(menu, "13", ex13.toString(), ex13);

        menu.show();
    }
}