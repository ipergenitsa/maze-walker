package com.example.maze.walker;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DesktopApp extends Canvas {
    private final Integer[][] array;
    private int playerPosY = 0;
    private int playerPosX = 0;

    public DesktopApp(int height, int width) {
        array = new DfsMazeGeneratorV2().generate(height, width);
        array[playerPosY][playerPosX] = Application.CHARACTER_TILE;

        addKeyListener(new Keyboard(this));
        setFocusable(true);
    }

    public static void main(String[] args) {
        DesktopApp m = new DesktopApp(11, 11);
        JFrame f = new JFrame();
        f.add(m);
        f.setSize(1000, 1000);
        f.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        try {
            Image floor = ImageIO.read(getClass().getResource("/img/Stone_Floor_texture.png"));
            Image player = ImageIO.read(getClass().getResource("/img/Player.png"));
            Image wall = ImageIO.read(getClass().getResource("/img/Wall.png"));
            int size = 64;
            for (int i = 0; i < array[0].length + 2; i++) {
                g.drawImage(wall, -size / 2 + i * size, -size / 2, size, size, this);
                g.drawImage(wall, -size / 2 + i * size, size / 2 + array.length * size, size, size / 2, this);
            }
            for (int i = 0; i < array.length; i++) {
                g.drawImage(wall, -size / 2, size / 2 + i * size, size, size, this);
                g.drawImage(wall, size / 2 + array[0].length * size, size / 2 + i * size, size / 2, size, this);
            }

            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[0].length; j++) {
                    if (array[i][j] == Application.EMPTY_TILE) {
                        draw(g, floor, size, i, j);
                    } else if (array[i][j] == Application.CHARACTER_TILE) {
                        draw(g, floor, size, i, j);
                        draw(g, player, size, i, j);
                    } else {
                        draw(g, wall, size, i, j);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void draw(Graphics g, Image floor, int size, int i, int j) {
        g.drawImage(floor, size - size / 2 + j * size, size - size / 2 + i * size, size, size, this);
    }

    private class Keyboard implements KeyListener {

        private final Canvas canvas;

        public Keyboard(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            Direction direction = switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT;
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> Direction.LEFT;
                case KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.UP;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.DOWN;
                default -> null;
            };
            if (direction == null) {
                return;
            }

            int x = direction.getX();
            int y = direction.getY();

            if (!(playerPosY + y < 0 || playerPosX + x < 0 || playerPosY + y >= array.length || playerPosX + x >= array[0].length)) {
                if (array[playerPosY + y][playerPosX + x] < 1) {
                    array[playerPosY][playerPosX] = Application.EMPTY_TILE;
                    array[playerPosY + y][playerPosX + x] = Application.CHARACTER_TILE;

                    playerPosY += y;
                    playerPosX += x;
                }
            }
            canvas.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
    }


    }
}

