package com.gmail.tylerfilla.ftc5703.joysim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

public class CopyOfJoySim_ extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
    
    private static final long serialVersionUID = 2269971701250845501L;
    
    private static final int WIDTH = 512;
    private static final int HEIGHT = 320;
    
    private static final int JOYSTICK_SIZE = 100;
    
    private static final int JOYSTICK_1_POS_X = 120;
    private static final int JOYSTICK_1_POS_Y = 64;
    private static final int JOYSTICK_2_POS_X = 292;
    private static final int JOYSTICK_2_POS_Y = 64;
    
    private static final int JOYSTICK_1_CENTER_X = JOYSTICK_1_POS_X + JOYSTICK_SIZE / 2;
    private static final int JOYSTICK_1_CENTER_Y = JOYSTICK_1_POS_Y + JOYSTICK_SIZE / 2;
    private static final int JOYSTICK_2_CENTER_X = JOYSTICK_2_POS_X + JOYSTICK_SIZE / 2;
    private static final int JOYSTICK_2_CENTER_Y = JOYSTICK_2_POS_Y + JOYSTICK_SIZE / 2;
    
    private static final int ROBOT_SIZE = 64;
    
    private static final int ROBOT_POS_X = 138;
    private static final int ROBOT_POS_Y = 220;
    
    private static final double MOTOR_GRADE_CONSTANT = 1.0d / 45.0d;
    
    private int joy1X;
    private int joy1Y;
    
    private int joy2X;
    private int joy2Y;
    
    private double magnitude;
    private double theta;
    
    private int motorPower1;
    private int motorPower2;
    private int motorPower3;
    private int motorPower4;
    
    private int dragState;
    
    public CopyOfJoySim_() {
        this.joy1X = 0;
        this.joy1Y = 0;
        
        this.joy2X = 0;
        this.joy2Y = 0;
        
        this.magnitude = 0.0d;
        this.theta = 45.0d;
        
        this.motorPower1 = 0;
        this.motorPower2 = 0;
        this.motorPower3 = 0;
        this.motorPower4 = 0;
        
        this.dragState = 0;
        
        this.setTitle("JoySim - The Doppler Effect - FTC 5703");
        
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void calculate() {
        double x = (this.joy1X + 0.5d) / 127.5d;
        double y = (this.joy1Y + 0.5d) / 127.5d;
        
        this.magnitude = Math.sqrt(x * x + y * y);
        this.theta = Math.toDegrees(Math.acos((127.0d * x) / (this.magnitude * 127.0d)));
        
        if (y < 0) {
            this.theta = 360 - this.theta;
        }
        
        if (this.theta < 90) {
            this.motorPower1 = (int) (this.magnitude * 100.0d * (this.theta * MOTOR_GRADE_CONSTANT - 1.0d));
            this.motorPower2 = (int) (this.magnitude * 100.0d * -1.0d);
            this.motorPower3 = (int) (this.magnitude * 100.0d * (this.theta * -MOTOR_GRADE_CONSTANT + 1.0d));
            this.motorPower4 = (int) (this.magnitude * 100.0d * 1.0d);
        } else if (this.theta < 180) {
            this.motorPower1 = (int) (this.magnitude * 100.0d * 1.0d);
            this.motorPower2 = (int) (this.magnitude * 100.0d * (this.theta * MOTOR_GRADE_CONSTANT - 3.0d));
            this.motorPower3 = (int) (this.magnitude * 100.0d * -1.0d);
            this.motorPower4 = (int) (this.magnitude * 100.0d * (this.theta * -MOTOR_GRADE_CONSTANT + 3.0d));
        } else if (this.theta < 270) {
            this.motorPower1 = (int) (this.magnitude * 100.0d * (this.theta * -MOTOR_GRADE_CONSTANT + 5.0d));
            this.motorPower2 = (int) (this.magnitude * 100.0d * 1.0d);
            this.motorPower3 = (int) (this.magnitude * 100.0d * (this.theta * MOTOR_GRADE_CONSTANT - 5.0d));
            this.motorPower4 = (int) (this.magnitude * 100.0d * -1.0d);
        } else if (this.theta < 360) {
            this.motorPower1 = (int) (this.magnitude * 100.0d * -1.0d);
            this.motorPower2 = (int) (this.magnitude * 100.0d * (this.theta * -MOTOR_GRADE_CONSTANT + 7.0d));
            this.motorPower3 = (int) (this.magnitude * 100.0d * 1.0d);
            this.motorPower4 = (int) (this.magnitude * 100.0d * (this.theta * MOTOR_GRADE_CONSTANT - 7.0d));
        }
        
        this.motorPower1 *= this.magnitude;
        this.motorPower2 *= this.magnitude;
        this.motorPower3 *= this.magnitude;
        this.motorPower4 *= this.magnitude;
        
        this.motorPower1 -= ((this.joy2X + 0.5d) / 127.5d) * 100.0d;
        this.motorPower2 -= ((this.joy2X + 0.5d) / 127.5d) * 100.0d;
        this.motorPower3 -= ((this.joy2X + 0.5d) / 127.5d) * 100.0d;
        this.motorPower4 -= ((this.joy2X + 0.5d) / 127.5d) * 100.0d;
        
        if (this.motorPower1 < -100) {
            this.motorPower1 = -100;
        }
        if (this.motorPower2 < -100) {
            this.motorPower2 = -100;
        }
        if (this.motorPower3 < -100) {
            this.motorPower3 = -100;
        }
        if (this.motorPower4 < -100) {
            this.motorPower4 = -100;
        }
        
        if (this.motorPower1 > 100) {
            this.motorPower1 = 100;
        }
        if (this.motorPower2 > 100) {
            this.motorPower2 = 100;
        }
        if (this.motorPower3 > 100) {
            this.motorPower3 = 100;
        }
        if (this.motorPower4 > 100) {
            this.motorPower4 = 100;
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        /* Joystick 1 */
        
        int joy1XDisplay = (int) (((this.joy1X + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
        int joy1YDisplay = (int) (((this.joy1Y + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
        
        // Joystick 1 info
        g2d.setColor(Color.BLACK);
        g2d.drawString("Joystick 1", JOYSTICK_1_POS_X - 96, JOYSTICK_1_POS_Y + 32);
        g2d.drawString("J = <" + this.joy1X + ", " + this.joy1Y + ">", JOYSTICK_1_POS_X - 96,
                JOYSTICK_1_POS_Y + 49);
        g2d.drawString("|J| = " + (int) (this.magnitude * 100.0d) / 100.0d, JOYSTICK_1_POS_X - 96,
                JOYSTICK_1_POS_Y + 61);
        g2d.drawString("\u03b8 = " + (int) (this.theta * 100.0d) / 100.0d + "\u00b0",
                JOYSTICK_1_POS_X - 96, JOYSTICK_1_POS_Y + 73);
        
        // Joystick 1 axes
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("x", JOYSTICK_1_POS_X - 15, JOYSTICK_1_CENTER_Y - 5);
        g2d.drawString("y", JOYSTICK_1_CENTER_X + 5, JOYSTICK_1_POS_Y - 10);
        g2d.drawLine(JOYSTICK_1_POS_X - 20, JOYSTICK_1_POS_Y + JOYSTICK_SIZE / 2, JOYSTICK_1_POS_X
                + JOYSTICK_SIZE + 20, JOYSTICK_1_POS_Y + JOYSTICK_SIZE / 2);
        g2d.drawLine(JOYSTICK_1_POS_X + JOYSTICK_SIZE / 2, JOYSTICK_1_POS_Y - 20, JOYSTICK_1_POS_X
                + JOYSTICK_SIZE / 2, JOYSTICK_1_POS_Y + JOYSTICK_SIZE + 20);
        
        // Joystick 1 outline
        g2d.setColor(Color.BLACK);
        g2d.draw(new Ellipse2D.Double(JOYSTICK_1_POS_X, JOYSTICK_1_POS_Y, JOYSTICK_SIZE,
                JOYSTICK_SIZE));
        
        // Joystick 1 position support lines
        g2d.setColor(Color.BLACK);
        g2d.drawLine(JOYSTICK_1_CENTER_X, JOYSTICK_1_CENTER_Y, JOYSTICK_1_POS_X + JOYSTICK_SIZE,
                JOYSTICK_1_CENTER_Y);
        g2d.setColor(Color.RED);
        g2d.drawLine(JOYSTICK_1_CENTER_X, JOYSTICK_1_CENTER_Y, JOYSTICK_1_CENTER_X + joy1XDisplay,
                JOYSTICK_1_CENTER_Y - joy1YDisplay);
        g2d.setColor(Color.PINK);
        g2d.drawLine(JOYSTICK_1_CENTER_X - joy1XDisplay, JOYSTICK_1_CENTER_Y + joy1YDisplay,
                JOYSTICK_1_CENTER_X, JOYSTICK_1_CENTER_Y);
        g2d.setColor(Color.BLUE);
        g2d.drawLine(JOYSTICK_1_CENTER_X + joy1XDisplay, JOYSTICK_1_CENTER_Y - joy1YDisplay,
                JOYSTICK_1_POS_X + JOYSTICK_SIZE, JOYSTICK_1_CENTER_Y);
        
        // Joystick 1 origin marker
        g2d.setColor(Color.BLACK);
        g2d.fillRect(JOYSTICK_1_CENTER_X - 2, JOYSTICK_1_CENTER_Y - 2, 4, 4);
        
        // Joystick 1 position marker
        g2d.setColor(Color.GREEN);
        g2d.fillRect(JOYSTICK_1_CENTER_X + joy1XDisplay - 3,
                JOYSTICK_1_CENTER_Y - joy1YDisplay - 3, 6, 6);
        
        /* Joystick 2 */
        
        int joy2XDisplay = (int) (((joy2X + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
        int joy2YDisplay = (int) (((joy2Y + 0.5d) / 127.5d) * (JOYSTICK_SIZE / 2));
        
        // Joystick 2 info
        g2d.setColor(Color.BLACK);
        g2d.drawString("Joystick 2", JOYSTICK_2_POS_X + JOYSTICK_SIZE / 2 + 84,
                JOYSTICK_2_POS_Y + 32);
        g2d.drawString("J = <" + this.joy2X + ", " + this.joy2Y + ">", JOYSTICK_2_POS_X
                + JOYSTICK_SIZE / 2 + 84, JOYSTICK_1_POS_Y + 49);
        
        // Joystick 2 axes
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("x", JOYSTICK_2_POS_X - 15, JOYSTICK_2_CENTER_Y - 5);
        g2d.drawString("y", JOYSTICK_2_CENTER_X + 5, JOYSTICK_2_POS_Y - 10);
        g2d.drawLine(JOYSTICK_2_POS_X - 20, JOYSTICK_2_POS_Y + JOYSTICK_SIZE / 2, JOYSTICK_2_POS_X
                + JOYSTICK_SIZE + 20, JOYSTICK_2_POS_Y + JOYSTICK_SIZE / 2);
        g2d.drawLine(JOYSTICK_2_POS_X + JOYSTICK_SIZE / 2, JOYSTICK_2_POS_Y - 20, JOYSTICK_2_POS_X
                + JOYSTICK_SIZE / 2, JOYSTICK_2_POS_Y + JOYSTICK_SIZE + 20);
        
        // Joystick 2 outline
        g2d.setColor(Color.BLACK);
        g2d.draw(new Ellipse2D.Double(JOYSTICK_2_POS_X, JOYSTICK_2_POS_Y, JOYSTICK_SIZE,
                JOYSTICK_SIZE));
        
        // Joystick 2 position support lines
        g2d.setColor(Color.BLUE);
        g2d.drawLine(JOYSTICK_2_CENTER_X, JOYSTICK_2_CENTER_Y, JOYSTICK_2_CENTER_X + joy2XDisplay,
                JOYSTICK_2_CENTER_Y);
        g2d.setColor(Color.RED);
        g2d.drawLine(JOYSTICK_2_CENTER_X + joy2XDisplay, JOYSTICK_2_CENTER_Y, JOYSTICK_2_CENTER_X
                + joy2XDisplay, JOYSTICK_2_CENTER_Y - joy2YDisplay);
        g2d.setColor(Color.PINK);
        g2d.drawLine(JOYSTICK_2_CENTER_X + joy2XDisplay, JOYSTICK_2_CENTER_Y, JOYSTICK_2_CENTER_X
                + joy2XDisplay, JOYSTICK_2_CENTER_Y + joy2YDisplay);
        
        // Joystick 2 origin marker
        g2d.setColor(Color.BLACK);
        g2d.fillRect(JOYSTICK_2_CENTER_X - 2, JOYSTICK_2_CENTER_Y - 2, 4, 4);
        
        // Joystick 2 position marker
        g2d.setColor(Color.GREEN);
        g2d.fillRect(JOYSTICK_2_CENTER_X + joy2XDisplay - 3,
                JOYSTICK_2_CENTER_Y - joy2YDisplay - 3, 6, 6);
        
        /* Divider line */
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(32, ROBOT_POS_Y - 20, WIDTH - 32, ROBOT_POS_Y - 20);
        
        /* Robot */
        
        // Robot info
        g2d.setColor(Color.BLACK);
        g2d.drawString("Robot", ROBOT_POS_X + ROBOT_SIZE + 16, ROBOT_POS_Y + 10);
        g2d.drawString("P1 = " + motorPower1, ROBOT_POS_X + ROBOT_SIZE + 16, ROBOT_POS_Y + 27);
        g2d.drawString("P2 = " + motorPower2, ROBOT_POS_X + ROBOT_SIZE + 16, ROBOT_POS_Y + 39);
        g2d.drawString("P3 = " + motorPower3, ROBOT_POS_X + ROBOT_SIZE + 16, ROBOT_POS_Y + 51);
        g2d.drawString("P4 = " + motorPower4, ROBOT_POS_X + ROBOT_SIZE + 16, ROBOT_POS_Y + 63);
        
        // Robot chassis and labels
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRect(ROBOT_POS_X, ROBOT_POS_Y, ROBOT_SIZE, ROBOT_SIZE);
        g2d.drawLine(ROBOT_POS_X + ROBOT_SIZE - 5, ROBOT_POS_Y + 15, ROBOT_POS_X + ROBOT_SIZE - 15,
                ROBOT_POS_Y + 5);
        g2d.drawLine(ROBOT_POS_X + ROBOT_SIZE - 6, ROBOT_POS_Y + 16, ROBOT_POS_X + ROBOT_SIZE - 16,
                ROBOT_POS_Y + 6);
        g2d.drawString("1", ROBOT_POS_X + ROBOT_SIZE - 18, ROBOT_POS_Y + 23);
        g2d.drawLine(ROBOT_POS_X + 5, ROBOT_POS_Y + 15, ROBOT_POS_X + 15, ROBOT_POS_Y + 5);
        g2d.drawLine(ROBOT_POS_X + 6, ROBOT_POS_Y + 16, ROBOT_POS_X + 16, ROBOT_POS_Y + 6);
        g2d.drawString("2", ROBOT_POS_X + 13, ROBOT_POS_Y + 23);
        g2d.drawLine(ROBOT_POS_X + 5, ROBOT_POS_Y + ROBOT_SIZE - 15, ROBOT_POS_X + 15, ROBOT_POS_Y
                + ROBOT_SIZE - 5);
        g2d.drawLine(ROBOT_POS_X + 6, ROBOT_POS_Y + ROBOT_SIZE - 16, ROBOT_POS_X + 16, ROBOT_POS_Y
                + ROBOT_SIZE - 6);
        g2d.drawString("3", ROBOT_POS_X + 13, ROBOT_POS_Y + ROBOT_SIZE - 13);
        g2d.drawLine(ROBOT_POS_X + ROBOT_SIZE - 5, ROBOT_POS_Y + ROBOT_SIZE - 15, ROBOT_POS_X
                + ROBOT_SIZE - 15, ROBOT_POS_Y + ROBOT_SIZE - 5);
        g2d.drawLine(ROBOT_POS_X + ROBOT_SIZE - 6, ROBOT_POS_Y + ROBOT_SIZE - 16, ROBOT_POS_X
                + ROBOT_SIZE - 16, ROBOT_POS_Y + ROBOT_SIZE - 6);
        g2d.drawString("4", ROBOT_POS_X + ROBOT_SIZE - 19, ROBOT_POS_Y + ROBOT_SIZE - 14);
        
        // Robot direction arrows
        // Motor 1
        AffineTransform transformPrev = g2d.getTransform();
        AffineTransform transformArrow = new AffineTransform();
        transformArrow.rotate(Math.toRadians(45.0d), ROBOT_POS_X + ROBOT_SIZE - 10,
                ROBOT_POS_Y + 10);
        transformArrow.translate(ROBOT_POS_X + ROBOT_SIZE - 10, ROBOT_POS_Y + 10);
        g2d.transform(transformArrow);
        if (this.motorPower1 < 0) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GREEN);
        }
        g2d.drawLine(0, 0, (int) ((-this.motorPower1 / 100.0d) * 30.0d), 0);
        g2d.drawLine((int) ((-this.motorPower1 / 100.0d) * 30.0d), 0,
                (int) ((-this.motorPower1 / 100.0d) * 30.0d)
                        + (this.motorPower1 != 0 ? this.motorPower1 / Math.abs(this.motorPower1)
                                : 0) * 3, 3);
        g2d.drawLine((int) ((-this.motorPower1 / 100.0d) * 30.0d), 0,
                (int) ((-this.motorPower1 / 100.0d) * 30.0d)
                        + (this.motorPower1 != 0 ? this.motorPower1 / Math.abs(this.motorPower1)
                                : 0) * 3, -3);
        g2d.setTransform(transformPrev);
        // Motor 2
        transformPrev = g2d.getTransform();
        transformArrow = new AffineTransform();
        transformArrow.rotate(Math.toRadians(135.0d), ROBOT_POS_X + 10, ROBOT_POS_Y + 10);
        transformArrow.translate(ROBOT_POS_X + 10, ROBOT_POS_Y + 10);
        g2d.transform(transformArrow);
        if (this.motorPower2 < 0) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GREEN);
        }
        g2d.drawLine(0, 0, (int) ((this.motorPower2 / 100.0d) * 30.0d), 0);
        g2d.drawLine((int) ((this.motorPower2 / 100.0d) * 30.0d), 0,
                (int) ((this.motorPower2 / 100.0d) * 30.0d)
                        + (this.motorPower2 != 0 ? -this.motorPower2 / Math.abs(this.motorPower2)
                                : 0) * 3, 3);
        g2d.drawLine((int) ((this.motorPower2 / 100.0d) * 30.0d), 0,
                (int) ((this.motorPower2 / 100.0d) * 30.0d)
                        + (this.motorPower2 != 0 ? -this.motorPower2 / Math.abs(this.motorPower2)
                                : 0) * 3, -3);
        g2d.setTransform(transformPrev);
        // Motor 3
        transformPrev = g2d.getTransform();
        transformArrow = new AffineTransform();
        transformArrow.rotate(Math.toRadians(225.0d), ROBOT_POS_X + 10, ROBOT_POS_Y + ROBOT_SIZE
                - 10);
        transformArrow.translate(ROBOT_POS_X + 10, ROBOT_POS_Y + ROBOT_SIZE - 10);
        g2d.transform(transformArrow);
        if (this.motorPower3 < 0) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GREEN);
        }
        g2d.drawLine(0, 0, (int) ((-this.motorPower3 / 100.0d) * 30.0d), 0);
        g2d.drawLine((int) ((-this.motorPower3 / 100.0d) * 30.0d), 0,
                (int) ((-this.motorPower3 / 100.0d) * 30.0d)
                        + (this.motorPower3 != 0 ? this.motorPower3 / Math.abs(this.motorPower3)
                                : 0) * 3, 3);
        g2d.drawLine((int) ((-this.motorPower3 / 100.0d) * 30.0d), 0,
                (int) ((-this.motorPower3 / 100.0d) * 30.0d)
                        + (this.motorPower3 != 0 ? this.motorPower3 / Math.abs(this.motorPower3)
                                : 0) * 3, -3);
        g2d.setTransform(transformPrev);
        // Motor 4
        transformPrev = g2d.getTransform();
        transformArrow = new AffineTransform();
        transformArrow.rotate(Math.toRadians(315.0d), ROBOT_POS_X + ROBOT_SIZE - 10, ROBOT_POS_Y
                + ROBOT_SIZE - 10);
        transformArrow.translate(ROBOT_POS_X + ROBOT_SIZE - 10, ROBOT_POS_Y + ROBOT_SIZE - 10);
        g2d.transform(transformArrow);
        if (this.motorPower4 < 0) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GREEN);
        }
        g2d.drawLine(0, 0, (int) ((this.motorPower4 / 100.0d) * 30.0d), 0);
        g2d.drawLine((int) ((this.motorPower4 / 100.0d) * 30.0d), 0,
                (int) ((this.motorPower4 / 100.0d) * 30.0d)
                        + (this.motorPower4 != 0 ? -this.motorPower4 / Math.abs(this.motorPower4)
                                : 0) * 3, 3);
        g2d.drawLine((int) ((this.motorPower4 / 100.0d) * 30.0d), 0,
                (int) ((this.motorPower4 / 100.0d) * 30.0d)
                        + (this.motorPower4 != 0 ? -this.motorPower4 / Math.abs(this.motorPower4)
                                : 0) * 3, -3);
        g2d.setTransform(transformPrev);
        
        /* Instructions */
        
        g2d.setColor(Color.BLACK);
        g2d.drawString("Instructions", WIDTH - 200, HEIGHT - 90);
        g2d.drawString("Click and drag joysticks or use", WIDTH - 200, HEIGHT - 73);
        g2d.drawString("WASD and IJKL to control. Right-", WIDTH - 200, HEIGHT - 61);
        g2d.drawString("click joysticks to reset.", WIDTH - 200, HEIGHT - 49);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
        case 'w':
            this.joy1Y = 127;
            break;
        case 'a':
            this.joy1X = -128;
            break;
        case 's':
            this.joy1Y = -128;
            break;
        case 'd':
            this.joy1X = 127;
            break;
        case 'i':
            this.joy2Y = 127;
            break;
        case 'j':
            this.joy2X = -128;
            break;
        case 'k':
            this.joy2Y = -128;
            break;
        case 'l':
            this.joy2X = 127;
            break;
        }
        
        this.calculate();
        this.repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
        case 'w':
            this.joy1Y = 0;
            break;
        case 'a':
            this.joy1X = 0;
            break;
        case 's':
            this.joy1Y = 0;
            break;
        case 'd':
            this.joy1X = 0;
            break;
        case 'i':
            this.joy2Y = 0;
            break;
        case 'j':
            this.joy2X = 0;
            break;
        case 'k':
            this.joy2Y = 0;
            break;
        case 'l':
            this.joy2X = 0;
            break;
        }
        
        this.calculate();
        this.repaint();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            int x = e.getX();
            int y = e.getY();
            
            if (x >= JOYSTICK_1_POS_X && x <= JOYSTICK_1_POS_X + JOYSTICK_SIZE
                    && y >= JOYSTICK_1_POS_Y && y <= JOYSTICK_1_POS_Y + JOYSTICK_SIZE) {
                this.joy1X = 0;
                this.joy1Y = 0;
            } else if (x >= JOYSTICK_2_POS_X && x <= JOYSTICK_2_POS_X + JOYSTICK_SIZE
                    && y >= JOYSTICK_2_POS_Y && y <= JOYSTICK_2_POS_Y + JOYSTICK_SIZE) {
                this.joy2X = 0;
                this.joy2Y = 0;
            }
            
            this.calculate();
            this.repaint();
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
            int x = e.getX();
            int y = e.getY();
            
            if (x >= JOYSTICK_1_POS_X && x <= JOYSTICK_1_POS_X + JOYSTICK_SIZE
                    && y >= JOYSTICK_1_POS_Y && y <= JOYSTICK_1_POS_Y + JOYSTICK_SIZE) {
                this.dragState = 1;
                this.mouseDragged(e);
            } else if (x >= JOYSTICK_2_POS_X && x <= JOYSTICK_2_POS_X + JOYSTICK_SIZE
                    && y >= JOYSTICK_2_POS_Y && y <= JOYSTICK_2_POS_Y + JOYSTICK_SIZE) {
                this.dragState = 2;
                this.mouseDragged(e);
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        this.dragState = 0;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if (this.dragState == 1) {
            int newX = x - JOYSTICK_1_CENTER_X;
            int newY = -(y - JOYSTICK_1_CENTER_Y);
            
            if (newX * newX + newY * newY <= (JOYSTICK_SIZE / 2) * (JOYSTICK_SIZE / 2)) {
                this.joy1X = newX;
                this.joy1Y = newY;
            } else {
                double magnitudeNew = Math.sqrt(newX * newX + newY * newY);
                
                double unitNewX = newX / magnitudeNew;
                double unitNewY = newY / magnitudeNew;
                
                this.joy1X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
                this.joy1Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
            }
            
            this.joy1X = (int) (((double) this.joy1X / (double) (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
            this.joy1Y = (int) (((double) this.joy1Y / (double) (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
        } else if (this.dragState == 2) {
            int newX = x - JOYSTICK_2_CENTER_X;
            int newY = -(y - JOYSTICK_2_CENTER_Y);
            
            if (newX * newX + newY * newY <= (JOYSTICK_SIZE / 2) * (JOYSTICK_SIZE / 2)) {
                this.joy2X = newX;
                this.joy2Y = newY;
            } else {
                double magnitudeNew = Math.sqrt(newX * newX + newY * newY);
                
                double unitNewX = newX / magnitudeNew;
                double unitNewY = newY / magnitudeNew;
                
                this.joy2X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
                this.joy2Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
            }
            
            this.joy2X = (int) (((double) this.joy2X / (double) (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
            this.joy2Y = (int) (((double) this.joy2Y / (double) (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
        }
        
        this.calculate();
        this.repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
