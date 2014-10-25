package com.gmail.tylerfilla.ftc5703;

public class JoySim {
    
    public static final JoySim instance = new JoySim();
    
    private final JoystickFrame joystickFrame;
    
    public JoySim() {
        this.joystickFrame = new JoystickFrame();
    }
    
    public void run() {
        this.joystickFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        JoySim.instance.run();
    }
    
}
