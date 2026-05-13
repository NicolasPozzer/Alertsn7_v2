package threads;

import gui.HomeGUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import service.EnvironmentService;

public class HilosHome implements Runnable {

    private int tipo;

    public HilosHome(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public void run() {
        

    }

}
