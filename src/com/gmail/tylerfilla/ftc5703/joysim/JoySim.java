package com.gmail.tylerfilla.ftc5703.joysim;

import com.gmail.tylerfilla.ftc5703.joysim.controller.ControllerManager;

public class JoySim {
    
    public static final JoySim instance = new JoySim();
    
    private final ControllerManager controllerManager;
    
    public JoySim() {
        this.controllerManager = new ControllerManager();
    }
    
    public void run() {
        this.controllerManager.createController();
        this.controllerManager.createController();
        this.controllerManager.createController();
        this.controllerManager.createController();
        this.controllerManager.createController();
    }
    
    public static void main(String[] args) {
        JoySim.instance.run();
    }
    
}
