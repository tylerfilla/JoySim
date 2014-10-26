package com.gmail.tylerfilla.ftc5703;

import java.util.ArrayList;

public class JoySim {
    
    public static final JoySim instance = new JoySim();
    
    private final ArrayList<Controller> controllerList;
    
    public JoySim() {
        this.controllerList = new ArrayList<Controller>();
    }
    
    public void run() {
        this.controllerList.add(new Controller(1));
        this.controllerList.add(new Controller(2));
    }
    
    public static void main(String[] args) {
        JoySim.instance.run();
    }
    
}
