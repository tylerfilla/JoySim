package com.gmail.tylerfilla.ftc5703;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

public class JoystickFrame extends JFrame implements KeyListener, MouseListener,
        MouseMotionListener {
    
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
    
    private int joy1X;
    private int joy1Y;
    
    private int joy2X;
    private int joy2Y;
    
    private int stateDrag;
    private String stateKeys;
    
    public JoystickFrame() {
        this.joy1X = 0;
        this.joy1Y = 0;
        
        this.joy2X = 0;
        this.joy2Y = 0;
        
        this.stateDrag = 0;
        this.stateKeys = "";
        
        this.setTitle("JoySim for FTC team 5703 - Joystick Control Window");
        
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        g2d.drawString("X = " + this.joy1X, JOYSTICK_1_POS_X - 96, JOYSTICK_1_POS_Y + 50);
        g2d.drawString("Y = " + this.joy1Y, JOYSTICK_1_POS_X - 96, JOYSTICK_1_POS_Y + 62);
        
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
        g2d.drawString("Joystick 2", JOYSTICK_2_POS_X + JOYSTICK_SIZE + 32, JOYSTICK_2_POS_Y + 32);
        g2d.drawString("X = " + this.joy2X, JOYSTICK_2_POS_X + JOYSTICK_SIZE + 32,
                JOYSTICK_2_POS_Y + 50);
        g2d.drawString("Y = " + this.joy2Y, JOYSTICK_2_POS_X + JOYSTICK_SIZE + 32,
                JOYSTICK_2_POS_Y + 62);
        
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
        g2d.drawLine(JOYSTICK_2_CENTER_X, JOYSTICK_2_CENTER_Y, JOYSTICK_2_POS_X + JOYSTICK_SIZE,
                JOYSTICK_2_CENTER_Y);
        g2d.setColor(Color.RED);
        g2d.drawLine(JOYSTICK_2_CENTER_X, JOYSTICK_2_CENTER_Y, JOYSTICK_2_CENTER_X + joy2XDisplay,
                JOYSTICK_2_CENTER_Y - joy2YDisplay);
        g2d.setColor(Color.PINK);
        g2d.drawLine(JOYSTICK_2_CENTER_X - joy2XDisplay, JOYSTICK_2_CENTER_Y + joy2YDisplay,
                JOYSTICK_2_CENTER_X, JOYSTICK_2_CENTER_Y);
        g2d.setColor(Color.BLUE);
        g2d.drawLine(JOYSTICK_2_CENTER_X + joy2XDisplay, JOYSTICK_2_CENTER_Y - joy2YDisplay,
                JOYSTICK_2_POS_X + JOYSTICK_SIZE, JOYSTICK_2_CENTER_Y);
        
        // Joystick 2 origin marker
        g2d.setColor(Color.BLACK);
        g2d.fillRect(JOYSTICK_2_CENTER_X - 2, JOYSTICK_2_CENTER_Y - 2, 4, 4);
        
        // Joystick 2 position marker
        g2d.setColor(Color.GREEN);
        g2d.fillRect(JOYSTICK_2_CENTER_X + joy2XDisplay - 3,
                JOYSTICK_2_CENTER_Y - joy2YDisplay - 3, 6, 6);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (this.stateKeys.contains(new String(new char[] { e.getKeyChar() }))) {
            return;
        }
        
        this.stateKeys += e.getKeyChar();
        
        int newJoy1X = this.joy1X;
        int newJoy1Y = this.joy1Y;
        int newJoy2X = this.joy2X;
        int newJoy2Y = this.joy2Y;
        
        switch (e.getKeyChar()) {
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
        if (newJoy1X * newJoy1X + newJoy1Y * newJoy1Y <= (JOYSTICK_SIZE / 2) * (JOYSTICK_SIZE / 2)) {
            this.joy1X = newJoy1X;
            this.joy1Y = newJoy1Y;
        } else {
            double magnitudeNew = Math.sqrt(newJoy1X * newJoy1X + newJoy1Y * newJoy1Y);
            
            double unitNewX = newJoy1X / magnitudeNew;
            double unitNewY = newJoy1Y / magnitudeNew;
            
            this.joy1X = (int) (unitNewX * (JOYSTICK_SIZE / 2));
            this.joy1Y = (int) (unitNewY * (JOYSTICK_SIZE / 2));
        }
        
        this.joy1X = (int) (((double) this.joy1X / (double) (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
        this.joy1Y = (int) (((double) this.joy1Y / (double) (JOYSTICK_SIZE / 2)) * 127.5d - 0.5d);
        
        this.repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (!this.stateKeys.contains(new String(new char[] { e.getKeyChar() }))) {
            return;
        }
        
        this.stateKeys = this.stateKeys.replaceAll(new String(new char[] { e.getKeyChar() }), "");
        
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
        int x = e.getX();
        int y = e.getY();
        
        if (this.stateDrag == 1) {
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
        } else if (this.stateDrag == 2) {
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
        
        this.repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
