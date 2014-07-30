// Name: Yusuke Akiba
// Date: 7/28/2014
// Description: MMap class for converting coordinations and IDs

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

public class MMap{// Minesweeper Map
    // methods to convert from coordinations to index
    public static int coordsToID(int x, int y, int w){
        return (x+y*w);
    }
    
    // method to convert from index to coordinations
    public static int[] indexToCoords(int id, int w){
        int[] coords = {id%w,id/w};
        return coords;
    }
}