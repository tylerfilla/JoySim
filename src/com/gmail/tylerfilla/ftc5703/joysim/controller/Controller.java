package com.gmail.tylerfilla.ftc5703.joysim.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Controller {
    
    private final int controller;
    private final ControllerFrame frame;
    
    private int joy1X;
    private int joy1Y;
    private double joy1Magnitude;
    private double joy1Angle;
    
    private int joy2X;
    private int joy2Y;
    private double joy2Magnitude;
    private double joy2Angle;
    
    private int buttonState;
    
    Controller(int controller) {
        this.controller = controller;
        this.frame = new ControllerFrame();
        
        this.joy1X = 0;
        this.joy1Y = 0;
        this.joy1Magnitude = 0.0d;
        this.joy1Angle = 0.0d;
        
        this.joy2X = 0;
        this.joy2Y = 0;
        this.joy2Magnitude = 0.0d;
        this.joy2Angle = 0.0d;
        
        this.buttonState = 0;
    }
    
    public int getJoy1X() {
        return this.joy1X;
    }
    
    public void setJoy1X(int joy1X) {
        this.joy1X = joy1X;
        this.update();
    }
    
    public int getJoy1Y() {
        return this.joy1Y;
    }
    
    public void setJoy1Y(int joy1Y) {
        this.joy1Y = joy1Y;
        this.update();
    }
    
    public double getJoy1Angle() {
        return this.joy1Angle;
    }
    
    public double getJoy1Magnitude() {
        return this.joy1Magnitude;
    }
    
    public int getJoy2X() {
        return this.joy2X;
    }
    
    public void setJoy2X(int joy2X) {
        this.joy2X = joy2X;
        this.update();
    }
    
    public int getJoy2Y() {
        return this.joy2Y;
    }
    
    public void setJoy2Y(int joy2Y) {
        this.joy2Y = joy2Y;
        this.update();
    }
    
    public double getJoy2Angle() {
        return this.joy2Angle;
    }
    
    public double getJoy2Magnitude() {
        return this.joy2Magnitude;
    }
    
    public int getButtonState() {
        return this.buttonState;
    }
    
    public void setButtonState(int buttonState) {
        this.buttonState = buttonState;
    }
    
    public boolean getButtonDown(Button button) {
        return (this.buttonState & button.getBitmask()) > 0;
    }
    
    public void setButtonDown(Button button, boolean state) {
        if (this.getButtonDown(button) == state) {
            return;
        }
        
        if (state) {
            this.buttonState += button.getBitmask();
        } else {
            this.buttonState -= button.getBitmask();
        }
    }
    
    private void update() {
        double joy1XUnit = (this.joy1X + 0.5d) / 127.5d;
        double joy1YUnit = (this.joy1Y + 0.5d) / 127.5d;
        double joy2XUnit = (this.joy2X + 0.5d) / 127.5d;
        double joy2YUnit = (this.joy2Y + 0.5d) / 127.5d;
        
        this.joy1Magnitude = Math.sqrt(joy1XUnit * joy1XUnit + joy1YUnit * joy1YUnit);
        this.joy1Angle = Math.acos((127.0d * joy1XUnit) / (this.joy1Magnitude * 127.0d));
        
        if (this.joy1Y < 0) {
            this.joy1Angle = 2 * Math.PI - this.joy1Angle;
        }
        
        this.joy2Magnitude = Math.sqrt(joy2XUnit * joy2XUnit + joy2YUnit * joy2YUnit);
        this.joy2Angle = Math.acos((127.0d * joy2XUnit) / (this.joy2Magnitude * 127.0d));
        
        if (this.joy2Y < 0) {
            this.joy2Angle = 2 * Math.PI - this.joy2Angle;
        }
    }
    
    public enum Button {
        
        _1(1), _2(2), _3(4), _4(8), _5(16), _6(32), _7(64), _8(128), _9(256), _10(512),
        
        // D-pad
        D0(1024),
        D1(2048),
        D2(4096),
        D3(8192),
        D4(16384),
        D5(32768),
        D6(65536),
        D7(131072);
        
        private final int bitmask;
        
        private Button(int bitmask) {
            this.bitmask = bitmask;
        }
        
        public int getBitmask() {
            return this.bitmask;
        }
        
    }
    
    private class ControllerFrame extends JFrame implements KeyListener, MouseListener,
            MouseMotionListener, WindowListener {
        
        private static final long serialVersionUID = 2269971701250845501l;
        
        private static final int WIDTH = 532;
        private static final int HEIGHT = 320;
        
        private static final int JOYSTICK_SIZE = 100;
        
        private static final int JOYSTICK_1_POS_X = 128;
        private static final int JOYSTICK_1_POS_Y = 40;
        private static final int JOYSTICK_2_POS_X = 300;
        private static final int JOYSTICK_2_POS_Y = 40;
        
        private static final int JOYSTICK_1_CENTER_X = JOYSTICK_1_POS_X + JOYSTICK_SIZE / 2;
        private static final int JOYSTICK_1_CENTER_Y = JOYSTICK_1_POS_Y + JOYSTICK_SIZE / 2;
        private static final int JOYSTICK_2_CENTER_X = JOYSTICK_2_POS_X + JOYSTICK_SIZE / 2;
        private static final int JOYSTICK_2_CENTER_Y = JOYSTICK_2_POS_Y + JOYSTICK_SIZE / 2;
        
        private final RenderPanel renderPanel;
        
        private int stateDrag;
        private final HashSet<Character> stateKeys;
        
        private final UpdateThread updateThread;
        
        public ControllerFrame() {
            this.renderPanel = new RenderPanel();
            
            this.stateDrag = 0;
            this.stateKeys = new HashSet<Character>();
            
            this.updateThread = new UpdateThread();
            this.updateThread.start();
            
            this.setVisible(true);
            this.add(this.renderPanel);
            
            this.setTitle("JoySim - Controller " + Controller.this.controller);
            
            this.setSize(WIDTH, HEIGHT);
            this.setResizable(false);
            
            this.addKeyListener(this);
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addWindowListener(this);
            
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        
        private void processKeyChar(char c) {
            int newJoy1X = Controller.this.joy1X;
            int newJoy1Y = Controller.this.joy1Y;
            int newJoy2X = Controller.this.joy2X;
            int newJoy2Y = Controller.this.joy2Y;
            
            switch (c) {
            case 'w':
                newJoy1Y = 127;
                break;
            case 'a':
                newJoy1X = -128;
                break;
            case 's':
                newJoy1Y = -128;
                break;
            case 'd':
                newJoy1X = 127;
                break;
            case 'i':
                newJoy2Y = 127;
                break;
            case 'j':
                newJoy2X = -128;
                break;
            case 'k':
                newJoy2Y = -128;
                break;
            case 'l':
                newJoy2X = 127;
                break;
            }
            
            // Check joystick 1
            if (newJoy1X != Controller.this.joy1X || newJoy1Y != Controller.this.joy1Y) {
                if (newJoy1X * newJoy1X + newJoy1Y * newJoy1Y <= (JOYSTICK_SIZE / 2)
                        * (JOYSTICK_SIZE / 2)) {
                    Controller.this.joy1X = newJoy1X;
                    Controller.this.joy1Y = newJoy1Y;
                } else {
                    double magnitudeNew = Math.sqrt(newJoy1X * newJoy1X + newJoy1Y * newJoy1Y);
                    
                    double unitNewX = newJoy1X / magnitudeNew;
                    double unitNewY = newJoy1Y / magnitudeNew;
                    
                    Controller.this.joy1X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
                    Controller.this.joy1Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
                }
                Controller.this.joy1X = (int) (((double) Controller.this.joy1X / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
                Controller.this.joy1Y = (int) (((double) Controller.this.joy1Y / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
            }
            
            // Check joystick 2
            if (newJoy2X != Controller.this.joy2X || newJoy2Y != Controller.this.joy2Y) {
                if (newJoy2X * newJoy2X + newJoy2Y * newJoy2Y <= (JOYSTICK_SIZE / 2)
                        * (JOYSTICK_SIZE / 2)) {
                    Controller.this.joy2X = newJoy2X;
                    Controller.this.joy2Y = newJoy2Y;
                } else {
                    double magnitudeNew = Math.sqrt(newJoy2X * newJoy2X + newJoy2Y * newJoy2Y);
                    
                    double unitNewX = newJoy2X / magnitudeNew;
                    double unitNewY = newJoy2Y / magnitudeNew;
                    
                    Controller.this.joy2X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
                    Controller.this.joy2Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
                }
                Controller.this.joy2X = (int) (((double) Controller.this.joy2X / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
                Controller.this.joy2Y = (int) (((double) Controller.this.joy2Y / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
            }
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (!this.stateKeys.contains(e.getKeyChar())) {
                this.stateKeys.add(e.getKeyChar());
            } else {
                return;
            }
            
            this.processKeyChar(e.getKeyChar());
            Controller.this.update();
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            if (this.stateKeys.contains(e.getKeyChar())) {
                this.stateKeys.remove(e.getKeyChar());
            } else {
                return;
            }
            
            if ("wasd".indexOf(e.getKeyChar()) > -1) {
                Controller.this.joy1X = 0;
                Controller.this.joy1Y = 0;
            }
            
            if ("ijkl".indexOf(e.getKeyChar()) > -1) {
                Controller.this.joy2X = 0;
                Controller.this.joy2Y = 0;
            }
            
            for (char c : this.stateKeys) {
                this.processKeyChar(c);
            }
            
            Controller.this.update();
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                int x = e.getX() - 2;
                int y = e.getY() - 24;
                
                if (x >= JOYSTICK_1_POS_X && x <= JOYSTICK_1_POS_X + JOYSTICK_SIZE
                        && y >= JOYSTICK_1_POS_Y && y <= JOYSTICK_1_POS_Y + JOYSTICK_SIZE) {
                    Controller.this.joy1X = 0;
                    Controller.this.joy1Y = 0;
                } else if (x >= JOYSTICK_2_POS_X && x <= JOYSTICK_2_POS_X + JOYSTICK_SIZE
                        && y >= JOYSTICK_2_POS_Y && y <= JOYSTICK_2_POS_Y + JOYSTICK_SIZE) {
                    Controller.this.joy2X = 0;
                    Controller.this.joy2Y = 0;
                }
                
                Controller.this.update();
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int x = e.getX() - 2;
                int y = e.getY() - 24;
                
                if (x >= JOYSTICK_1_POS_X && x <= JOYSTICK_1_POS_X + JOYSTICK_SIZE
                        && y >= JOYSTICK_1_POS_Y && y <= JOYSTICK_1_POS_Y + JOYSTICK_SIZE) {
                    this.stateDrag = 1;
                    this.mouseDragged(e);
                } else if (x >= JOYSTICK_2_POS_X && x <= JOYSTICK_2_POS_X + JOYSTICK_SIZE
                        && y >= JOYSTICK_2_POS_Y && y <= JOYSTICK_2_POS_Y + JOYSTICK_SIZE) {
                    this.stateDrag = 2;
                    this.mouseDragged(e);
                }
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            this.stateDrag = 0;
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX() - 2;
            int y = e.getY() - 24;
            
            if (this.stateDrag == 1) {
                int newX = x - JOYSTICK_1_CENTER_X;
                int newY = -(y - JOYSTICK_1_CENTER_Y);
                
                if (newX * newX + newY * newY <= (JOYSTICK_SIZE / 2) * (JOYSTICK_SIZE / 2)) {
                    Controller.this.joy1X = newX;
                    Controller.this.joy1Y = newY;
                } else {
                    double magnitudeNew = Math.sqrt(newX * newX + newY * newY);
                    
                    double unitNewX = newX / magnitudeNew;
                    double unitNewY = newY / magnitudeNew;
                    
                    Controller.this.joy1X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
                    Controller.this.joy1Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
                }
                
                Controller.this.joy1X = (int) (((double) Controller.this.joy1X / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
                Controller.this.joy1Y = (int) (((double) Controller.this.joy1Y / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
            } else if (this.stateDrag == 2) {
                int newX = x - JOYSTICK_2_CENTER_X;
                int newY = -(y - JOYSTICK_2_CENTER_Y);
                
                if (newX * newX + newY * newY <= (JOYSTICK_SIZE / 2) * (JOYSTICK_SIZE / 2)) {
                    Controller.this.joy2X = newX;
                    Controller.this.joy2Y = newY;
                } else {
                    double magnitudeNew = Math.sqrt(newX * newX + newY * newY);
                    
                    double unitNewX = newX / magnitudeNew;
                    double unitNewY = newY / magnitudeNew;
                    
                    Controller.this.joy2X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
                    Controller.this.joy2Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
                }
                
                Controller.this.joy2X = (int) (((double) Controller.this.joy2X / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
                Controller.this.joy2Y = (int) (((double) Controller.this.joy2Y / (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
            }
            
            Controller.this.update();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
        }
        
        @Override
        public void windowActivated(WindowEvent e) {
        }
        
        @Override
        public void windowClosed(WindowEvent e) {
        }
        
        @Override
        public void windowClosing(WindowEvent e) {
            this.updateThread.end();
        }
        
        @Override
        public void windowDeactivated(WindowEvent e) {
        }
        
        @Override
        public void windowDeiconified(WindowEvent e) {
        }
        
        @Override
        public void windowIconified(WindowEvent e) {
        }
        
        @Override
        public void windowOpened(WindowEvent e) {
        }
        
        public class RenderPanel extends JPanel {
            
            private static final long serialVersionUID = 7369581981329508845l;
            
            public RenderPanel() {
            }
            
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                Graphics2D g2d = (Graphics2D) g;
                
                /* Joystick 1 */
                
                int joy1XDisplay = (int) (((Controller.this.joy1X + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
                int joy1YDisplay = (int) (((Controller.this.joy1Y + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
                
                // Joystick 1 info
                g2d.setColor(Color.BLACK);
                g2d.drawString("Joystick 1", JOYSTICK_1_POS_X - 100, JOYSTICK_1_POS_Y + 32);
                g2d.drawString("X = " + Controller.this.joy1X, JOYSTICK_1_POS_X - 100,
                        JOYSTICK_1_POS_Y + 50);
                g2d.drawString("Y = " + Controller.this.joy1Y, JOYSTICK_1_POS_X - 100,
                        JOYSTICK_1_POS_Y + 62);
                g2d.drawString("M = " + (int) (Controller.this.joy1Magnitude * 100.0d) / 100.0d,
                        JOYSTICK_1_POS_X - 100, JOYSTICK_1_POS_Y + 74);
                g2d.drawString("\u03b8 = " + (int) (Controller.this.joy1Angle / Math.PI * 100.0d)
                        / 100.0d + "\u03c0/"
                        + (int) (Math.toDegrees(Controller.this.joy1Angle) * 100.0d) / 100.0d
                        + "\u00b0", JOYSTICK_1_POS_X - 100, JOYSTICK_1_POS_Y + 86);
                
                // Joystick 1 axes
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawString("x", JOYSTICK_1_POS_X - 15, JOYSTICK_1_CENTER_Y - 5);
                g2d.drawString("y", JOYSTICK_1_CENTER_X + 5, JOYSTICK_1_POS_Y - 10);
                g2d.drawLine(JOYSTICK_1_POS_X - 20, JOYSTICK_1_POS_Y + JOYSTICK_SIZE / 2,
                        JOYSTICK_1_POS_X + JOYSTICK_SIZE + 20, JOYSTICK_1_POS_Y + JOYSTICK_SIZE / 2);
                g2d.drawLine(JOYSTICK_1_POS_X + JOYSTICK_SIZE / 2, JOYSTICK_1_POS_Y - 20,
                        JOYSTICK_1_POS_X + JOYSTICK_SIZE / 2, JOYSTICK_1_POS_Y + JOYSTICK_SIZE + 20);
                
                // Joystick 1 outline
                g2d.setColor(Color.BLACK);
                g2d.draw(new Ellipse2D.Double(JOYSTICK_1_POS_X, JOYSTICK_1_POS_Y, JOYSTICK_SIZE,
                        JOYSTICK_SIZE));
                
                // Joystick 1 position support lines
                g2d.setColor(Color.BLACK);
                g2d.drawLine(JOYSTICK_1_CENTER_X, JOYSTICK_1_CENTER_Y, JOYSTICK_1_POS_X
                        + JOYSTICK_SIZE, JOYSTICK_1_CENTER_Y);
                g2d.setColor(Color.RED);
                g2d.drawLine(JOYSTICK_1_CENTER_X, JOYSTICK_1_CENTER_Y, JOYSTICK_1_CENTER_X
                        + joy1XDisplay, JOYSTICK_1_CENTER_Y - joy1YDisplay);
                g2d.setColor(Color.PINK);
                g2d.drawLine(JOYSTICK_1_CENTER_X - joy1XDisplay,
                        JOYSTICK_1_CENTER_Y + joy1YDisplay, JOYSTICK_1_CENTER_X,
                        JOYSTICK_1_CENTER_Y);
                g2d.setColor(Color.BLUE);
                g2d.drawLine(JOYSTICK_1_CENTER_X + joy1XDisplay,
                        JOYSTICK_1_CENTER_Y - joy1YDisplay, JOYSTICK_1_POS_X + JOYSTICK_SIZE,
                        JOYSTICK_1_CENTER_Y);
                
                // Joystick 1 origin marker
                g2d.setColor(Color.BLACK);
                g2d.fillRect(JOYSTICK_1_CENTER_X - 2, JOYSTICK_1_CENTER_Y - 2, 4, 4);
                
                // Joystick 1 position marker
                g2d.setColor(Color.GREEN);
                g2d.fillRect(JOYSTICK_1_CENTER_X + joy1XDisplay - 3, JOYSTICK_1_CENTER_Y
                        - joy1YDisplay - 3, 6, 6);
                
                /* Joystick 2 */
                
                int joy2XDisplay = (int) (((Controller.this.joy2X + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
                int joy2YDisplay = (int) (((Controller.this.joy2Y + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
                
                // Joystick 2 info
                g2d.setColor(Color.BLACK);
                g2d.drawString("Joystick 2", JOYSTICK_2_POS_X + JOYSTICK_SIZE + 28,
                        JOYSTICK_2_POS_Y + 32);
                g2d.drawString("X = " + Controller.this.joy2X, JOYSTICK_2_POS_X + JOYSTICK_SIZE
                        + 28, JOYSTICK_2_POS_Y + 50);
                g2d.drawString("Y = " + Controller.this.joy2Y, JOYSTICK_2_POS_X + JOYSTICK_SIZE
                        + 28, JOYSTICK_2_POS_Y + 62);
                g2d.drawString("M = " + (int) (Controller.this.joy2Magnitude * 100.0d) / 100.0d,
                        JOYSTICK_2_POS_X + JOYSTICK_SIZE + 28, JOYSTICK_2_POS_Y + 74);
                g2d.drawString("\u03b8 = " + (int) (Controller.this.joy2Angle / Math.PI * 100.0d)
                        / 100.0d + "\u03c0/"
                        + (int) (Math.toDegrees(Controller.this.joy2Angle) * 100.0d) / 100.0d
                        + "\u00b0", JOYSTICK_2_POS_X + JOYSTICK_SIZE + 28, JOYSTICK_2_POS_Y + 86);
                
                // Joystick 2 axes
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawString("x", JOYSTICK_2_POS_X - 15, JOYSTICK_2_CENTER_Y - 5);
                g2d.drawString("y", JOYSTICK_2_CENTER_X + 5, JOYSTICK_2_POS_Y - 10);
                g2d.drawLine(JOYSTICK_2_POS_X - 20, JOYSTICK_2_POS_Y + JOYSTICK_SIZE / 2,
                        JOYSTICK_2_POS_X + JOYSTICK_SIZE + 20, JOYSTICK_2_POS_Y + JOYSTICK_SIZE / 2);
                g2d.drawLine(JOYSTICK_2_POS_X + JOYSTICK_SIZE / 2, JOYSTICK_2_POS_Y - 20,
                        JOYSTICK_2_POS_X + JOYSTICK_SIZE / 2, JOYSTICK_2_POS_Y + JOYSTICK_SIZE + 20);
                
                // Joystick 2 outline
                g2d.setColor(Color.BLACK);
                g2d.draw(new Ellipse2D.Double(JOYSTICK_2_POS_X, JOYSTICK_2_POS_Y, JOYSTICK_SIZE,
                        JOYSTICK_SIZE));
                
                // Joystick 2 position support lines
                g2d.setColor(Color.BLACK);
                g2d.drawLine(JOYSTICK_2_CENTER_X, JOYSTICK_2_CENTER_Y, JOYSTICK_2_POS_X
                        + JOYSTICK_SIZE, JOYSTICK_2_CENTER_Y);
                g2d.setColor(Color.RED);
                g2d.drawLine(JOYSTICK_2_CENTER_X, JOYSTICK_2_CENTER_Y, JOYSTICK_2_CENTER_X
                        + joy2XDisplay, JOYSTICK_2_CENTER_Y - joy2YDisplay);
                g2d.setColor(Color.PINK);
                g2d.drawLine(JOYSTICK_2_CENTER_X - joy2XDisplay,
                        JOYSTICK_2_CENTER_Y + joy2YDisplay, JOYSTICK_2_CENTER_X,
                        JOYSTICK_2_CENTER_Y);
                g2d.setColor(Color.BLUE);
                g2d.drawLine(JOYSTICK_2_CENTER_X + joy2XDisplay,
                        JOYSTICK_2_CENTER_Y - joy2YDisplay, JOYSTICK_2_POS_X + JOYSTICK_SIZE,
                        JOYSTICK_2_CENTER_Y);
                
                // Joystick 2 origin marker
                g2d.setColor(Color.BLACK);
                g2d.fillRect(JOYSTICK_2_CENTER_X - 2, JOYSTICK_2_CENTER_Y - 2, 4, 4);
                
                // Joystick 2 position marker
                g2d.setColor(Color.GREEN);
                g2d.fillRect(JOYSTICK_2_CENTER_X + joy2XDisplay - 3, JOYSTICK_2_CENTER_Y
                        - joy2YDisplay - 3, 6, 6);
            }
            
        }
        
        private class UpdateThread extends Thread {
            
            private boolean stop;
            
            public UpdateThread() {
                this.stop = false;
            }
            
            @Override
            public void run() {
                while (true) {
                    if (this.stop) {
                        break;
                    }
                    
                    try {
                        Thread.sleep(10l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    ControllerFrame.this.repaint();
                }
            }
            
            public void end() {
                this.stop = true;
            }
            
        }
        
    }
    
}
