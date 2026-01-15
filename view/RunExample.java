package view;

import controller.Controller;

public class RunExample extends Command {
    private Controller ctr;

    public RunExample(String key, String desc, Controller ctr) {
        super(key, desc);
        this.ctr = ctr;
    }

    @Override
    public void execute() {
        try {
            ctr.allStep();
        } catch (InterruptedException e) {
            // Aceasta este noua excepție aruncată de Controller în A5 (din cauza thread-urilor)
            System.out.println("\n!!! Execution was interrupted !!!");
        } catch (Exception e) {
            // Aici prindem orice altă eroare (inclusiv MyException dacă apare ca RuntimeException)
            System.out.println("\n!!! An error occurred !!!");
            System.out.println(e.getMessage());
        }
    }
}