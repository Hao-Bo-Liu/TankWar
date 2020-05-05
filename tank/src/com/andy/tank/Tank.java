package com.andy.tank;

import javax.rmi.CORBA.Util;
import java.awt.*;
import java.util.Random;

public class Tank {

    private int x, y;
    public static int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
    private Direction dir = Direction.DOWN;
    private static final int SPEED = 5;
    private boolean moving = true;
    private boolean isLive = true;
    private Group group = Group.VILLAIN;
    Rectangle rect = new Rectangle();

    GameModel gm = null;

    public Tank() {
    }

    public Tank(int x, int y, Direction dir, GameModel gm) {
        System.out.println("in tank, gm ==" + gm);
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.gm = gm;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

//    public Tank(int x, int y, Direction dir, Group group) {
//        this.x = x;
//        this.y = y;
//        this.dir = dir;
//        this.group = group;
//        rect.x = this.x;
//        rect.y = this.y;
//        rect.width = WIDTH;
//        rect.height = HEIGHT;
//    }

    public Tank(int x, int y, Direction dir, GameModel gm, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.gm = gm;
        this.group = group;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public void paint(Graphics g) {
        if(!isLive){
            gm.enemies.remove(this);
            return;
        }
        drawImage(g);

        move();

    }

    /** draw tank image */
    public void drawImage(Graphics g){
        switch (dir){
            case LEFT:
                g.drawImage(this.group == Group.HERO ? ResourceMgr.goodTankL:ResourceMgr.badTankL, x, y,null);
                break;
            case UP:
                g.drawImage(this.group == Group.HERO ? ResourceMgr.goodTankU:ResourceMgr.badTankU,x,y,null);
                break;
            case RIGHT:
                g.drawImage(this.group == Group.HERO ? ResourceMgr.goodTankR:ResourceMgr.badTankR,x,y,null);
                break;
            case DOWN:
                g.drawImage(this.group == Group.HERO ? ResourceMgr.goodTankD:ResourceMgr.badTankD,x,y,null);
                break;
        }
    }

    private void move(){
        if(!moving) return;

        switch (dir){
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
        }

        /** only VILLAIN group random fires  */
        if(this.group == Group.VILLAIN && Utils.getRandom() > 95){
            fire();
        }
        /** VILLAIN group change directions */
        if(this.group == Group.VILLAIN && Utils.getRandom() > 90){
            changeDiretion();
        }

        /** check boundaries */
        boundaryCheck();

        /** update x,y */
        rect.x = this.x;
        rect.y = this.y;
    }

    public void boundaryCheck(){
        if(x < 0) {
            x = 0;
        }
        if(this.y < 30) {
            y = 30;
        }
        if(this.x > TankFrame.GAME_WIDTH - Tank.WIDTH) {
            x = TankFrame.GAME_WIDTH - Tank.WIDTH;
        }
        if(this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT) {
            y = TankFrame.GAME_HEIGHT - Tank.HEIGHT;
        }
    }

    public void changeDiretion(){
        this.dir = Utils.getRandomDirection();
    }

    public void fire() {
        int bX = this.x;
        int bY = this.y;
        switch (dir){
            /* different dir, different bullet positions */
            case LEFT:
                bX = this.x - Bullet.WIDTH / 2;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
                break;
            case UP:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
                bY = this.y - Bullet.HEIGHT / 2;
                break;
            case RIGHT:
                bX = this.x + Tank.WIDTH - Bullet.WIDTH / 2;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
                break;
            case DOWN:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
                bY = this.y + Tank.HEIGHT - Bullet.HEIGHT / 2;
                break;
        }
        /** add bullet by group  */
        gm.getBullets().add(new Bullet(bX, bY, this.dir, gm, this.getGroup()));
    }

    public void die(){
        /*  tank dies and explodes  */
        Explosion explosion = new Explosion(x, y, gm);
        gm.getExplosions().add(explosion);
        this.setLive(false);
    }








    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


}
