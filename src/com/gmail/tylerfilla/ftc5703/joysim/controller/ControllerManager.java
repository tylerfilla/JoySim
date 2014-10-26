package com.gmail.tylerfilla.ftc5703.joysim.controller;

import java.util.HashSet;

public class ControllerManager {
    
    private final HashSet<Controller> controllerSet;
    private int controllerNumber;
    
    public ControllerManager() {
        this.controllerSet = new HashSet<Controller>();
        this.controllerNumber = 0;
    }
    
    public HashSet<Controller> getControllers() {
        return this.controllerSet;
    }
    
    public void addController(Controller controller) {
        this.controllerSet.add(controller);
    }
    
    public void removeController(Controller controller) {
        this.controllerSet.remove(controller);
    }
    
    public Controller createController() {
        Controller controller = new Controller(++this.controllerNumber);
        this.controllerSet.add(controller);
        return controller;
    }
    
}
