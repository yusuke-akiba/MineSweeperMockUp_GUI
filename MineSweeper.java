// Name: Yusuke Akiba
// Date: 7/29/2014
// Description: Mocking a game of Mine Sweeper, which is available for PC users as an default game.

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
//import java.util.Scanner;
import java.util.Random;
import java.util.regex.*;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//import java.util.ArrayList;
//import java.util.StringTokenizer;
import javax.swing.*;
//import javax.swing.JOptionPane;
import java.awt.event.*;//ex. ActionListener
//import java.io.*; //Don't forget IOException!
//import java.applet.Applet;
//import java.awt.Graphics;
import java.awt.*;
/////////////////////////////////////////////////

public class MineSweeper extends JFrame{
    // declare variables for panels
    private JButton[][] buttons;
    private JPanel optPanel;
    private JPanel gamePanel;
    private ButtonGroup rbGroup;
    private JRadioButton radioLevelI = new JRadioButton("Level I (9 rows x 9 columns with 10 mines)",true);
    private JRadioButton radioLevelII = new JRadioButton("Level II (16 rows x 16 columns with 40 mines)");
    private JRadioButton radioLevelIII = new JRadioButton("Level III (16 rows x 30 columns with 99 mines)");
    private JButton optButton;
    private int rows = 9;
    private int cols = 9;
    private int totalMines;
    private Color bgColor = new Color(238,238,238);
    
    // icons
    private final ImageIcon mineIcon = new ImageIcon("mine.png");
    private final ImageIcon hitMineIcon = new ImageIcon("hitmine.png");
    private final ImageIcon[] numIcons = {
                                            new ImageIcon("num0.png"),
                                            new ImageIcon("num1.png"),
                                            new ImageIcon("num2.png"),
                                            new ImageIcon("num3.png"),
                                            new ImageIcon("num4.png"),
                                            new ImageIcon("num5.png"),
                                            new ImageIcon("num6.png"),
                                            new ImageIcon("num7.png"),
                                            new ImageIcon("num8.png")
                                        };
    
    // selected level
    private int level;
    
    // declare an array of grids information
    private Grid[] grids;
    
    // constants
    private int MAX_ROW = 16;
    private int MAX_COL = 30;
    
    // constructor
    public MineSweeper(){
        buildOptionPanel();
        setVisible(true);
    }
    
    // methods
    // ask players to choose level
    public void buildOptionPanel(){
        setTitle("Mine Sweeper");
        setSize(350,175);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create panel
        optPanel = new JPanel();
        
        //create radio button group
        rbGroup = new ButtonGroup();
        optPanel.add(new JLabel("Please choose a level you prefer to play"));
        // add buttons
        rbGroup.add(radioLevelI);
        rbGroup.add(radioLevelII);
        rbGroup.add(radioLevelIII);
        optPanel.add(radioLevelI);
        optPanel.add(radioLevelII);
        optPanel.add(radioLevelIII);
        // create ok button
        optButton = new JButton(" OK ");
        optButton.addActionListener(new OptButtonListener());
        optPanel.add(optButton);
        // add the panel
        add(optPanel);
    }
    // build game panel
    public void buildGamePanel(int r,int c){
        // get required extra(title, borders) area
        int extraWidth = getSize().width - optPanel.getSize().width;
        int extraHeight = getSize().height - optPanel.getSize().height;
        
        // remove option panel
        remove(optPanel);
        
        // retrieve and set rows and columns
        rows = r;
        cols = c;
        
        // set window size based on number of grids and set unresizable
        setSize(cols*25+extraWidth, rows*25+extraHeight);
        setResizable(false);
        
        // set the behavior when the close button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // create panel
        gamePanel = new JPanel();
        // create grids
        gamePanel.setLayout(new GridLayout(r,c));
        
        // instantiate buttons array
        buttons = new JButton[rows][cols];
        // instantiate grids array
        grids = new Grid[rows*cols];
        // create counter
        int id = 0;
        
        // instantiate values of grids array
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++,id++){
                // create and instantiate each button
                buttons[i][j] = new JButton();
                // add listener to each button
                buttons[i][j].addActionListener(new ButtonsListner());
                // add each button to game panel
                gamePanel.add(buttons[i][j]);
                // instantiate each value of grids array with only indexes
                grids[id] = new Grid(j,i);
            }
        }
        
        // implement mines
        add(gamePanel);
        //set mines randomly using id as maximum index
        int minesToPut = totalMines = level==1?10:(level==2?40:99);
        Random rnd = new Random();
        while(minesToPut>0){
            int puttingSite = rnd.nextInt(id);
            if(!grids[puttingSite].hasMine()){
                grids[puttingSite].setMine();
                minesToPut--;
            }
        }
        //calc number of surrounding mines for each grids and store the information
        for(int i=0;i<grids.length;i++){// for each element of grids array
            int mineCount=0;
            int baseX = grids[i].getCox();
            int baseY = grids[i].getCoy();
            for(int j=baseY-1;j<baseY+2;j++){
                for(int k=baseX-1;k<baseX+2;k++){
                    if(j>-1&&k>-1&&j<rows&&k<cols&&!(j==MMap.indexToCoords(i,cols)[1]&&k==MMap.indexToCoords(i,cols)[0])){
                        if(grids[MMap.coordsToID(k,j,cols)].hasMine()){
                            mineCount++;
                        }
                    }
                }// ends for(k)
            }// ends for(j)
            grids[i].setSurroundingMines(mineCount);
        }// end for(i)
    }
    private void openSurroundings(int cox, int coy, int id){
        for(int i=coy-1;i<coy+2;i++){// Y
            for(int j=cox-1;j<cox+2;j++){// X
                if(i>-1&&j>-1&&i<rows&&j<cols&&!(i==coy&&j==cox)){
                    openGrid(j,i,MMap.coordsToID(j,i,cols));
                }
            }
        }
    }
    private void openGrid(int cox, int coy, int id){
        if(!grids[id].isOpened()){// if the grid had not been opened yet
            buttons[coy][cox].setForeground(bgColor);
            buttons[coy][cox].setForeground(Color.BLACK);
            buttons[coy][cox].setBackground(bgColor);
            grids[id].open();
            if(grids[id].hasMine()){// if hit mine
                userLoses(id);
            }else{//if not hit mine
                if(!showSurroundings(id)){//if it has no mine in surrounding area
                    // open all surrounding area
                    openSurroundings(cox,coy,id);
                }
            }
        }
    }
    private boolean checkIsWin(){
        int numOfClosedGrids=0;
        //count closed
        for(int i=0;i<grids.length;i++){
            if(!grids[i].isOpened()){//if not opened yet
                numOfClosedGrids++;
            }
        }
        return (numOfClosedGrids==totalMines)?true:false;
    }
    private void showAllMines(){
        for(int i=0;i<grids.length;i++){
            if(grids[i].hasMine()){
                buttons[grids[i].getCoy()][grids[i].getCox()].setIcon(mineIcon);
            }
        }
    }// end showAllMines()
    private boolean showSurroundings(int id){
        buttons[grids[id].getCoy()][grids[id].getCox()].setIcon(numIcons[grids[id].getSurroundingMines()]);
        return grids[id].getSurroundingMines()>0?true:false;
    }
    private void userWins(){
        showAllMines();
        JOptionPane.showMessageDialog(this, "You Win!");
        //disable all buttons
        for(int i=0;i<buttons.length;i++){
            for(int j=0;j<buttons[i].length;j++){
                buttons[i][j].setEnabled(false);
            }
        }
    }
    private void userLoses(int id){
        // show all mines
        showAllMines();
        // show x on hit mine
        buttons[grids[id].getCoy()][grids[id].getCox()].setIcon(hitMineIcon);
        
        JOptionPane.showMessageDialog(this, "You Lose!");
        //disable all buttons
        for(int i=0;i<buttons.length;i++){
            for(int j=0;j<buttons[i].length;j++){
                buttons[i][j].setEnabled(false);
            }
        }
    }
    private class ButtonsListner implements ActionListener{
        public void actionPerformed(ActionEvent e){
            // get position
            // get the panel size and calc the size of buttons
            int bWidth = (gamePanel.getSize().width)/cols;
            int bHeight = (gamePanel.getSize().height)/rows;
            // get event info
            String eventInfo = e.toString();
            Pattern p = Pattern.compile("on javax\\.swing\\.JButton\\[,(\\d+),(\\d+),(\\d+)x(\\d+),alignmentX");
            Matcher m = p.matcher(eventInfo);
            if(m.find()){
                // get clicked coords and id
                int clickedCox = Integer.parseInt(m.group(1))/Integer.parseInt(m.group(3));
                int clickedCoy = Integer.parseInt(m.group(2))/Integer.parseInt(m.group(4));
                int clickedID = MMap.coordsToID(clickedCox,clickedCoy,cols);
                openGrid(clickedCox,clickedCoy,clickedID);
                if(checkIsWin()){
                    userWins();
                }
            }//end if(m.find())
        }// end actionPerformed
    } 
    private class OptButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(radioLevelI.isSelected()){
                level = 1;
                buildGamePanel(9,9);
            }else if(radioLevelII.isSelected()){
                level = 2;
                buildGamePanel(16,16);
            }else{
                level = 3;
                buildGamePanel(16,30);
            }
        }
    }
    public static void main(String[] args){
        new MineSweeper();
    }
}