// Name: yusuke Akiba
// Date: 7/28/2014
// Description: class of grid for storing information of each grid. JButtons are seperatedly managed.

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
//import java.util.Scanner;
//import java.util.Random;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//import java.util.ArrayList;
//import java.util.StringTokenizer;
//import javax.swing.JOptionPane;
//import java.awt.event.*;
//import java.io.*; //Don't forget IOException!
//import java.applet.Applet;
//import java.awt.Graphics;
//import java.awt.*;
/////////////////////////////////////////////////

public class Grid{
    // fields
    private int cox, coy;
    private int surroundingMines;
    private boolean mine = false, opened = false;
    
    
    // Constructors
    public Grid(int x, int y){
        cox = x;
        coy = y;
        mine = false;
        opened = false;
    }
    public Grid(int x, int y, boolean mine){
        cox = x;
        coy = y;
        this.mine = mine;
        opened = false;
    }
    
    // getter methods
    public boolean isOpened(){
        return opened;
    }
    public boolean hasMine(){
        return mine;
    }
    public int getCox(){
        return cox;
    }
    public int getCoy(){
        return coy;
    }
    public int[] getCoords(){
        int[] coords = {cox,coy};
        return coords;
    }
    public int getSurroundingMines(){
        return surroundingMines;
    }
    
    // setter methods
    public void open(){
        opened = true;
    }
    public void setMine(){
        mine = true;
    }
    public void removeMine(){
        mine = false;
    }
    public void setSurroundingMines(int n){
        surroundingMines = n;
    }
}