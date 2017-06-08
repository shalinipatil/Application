import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_140424c extends PApplet {

class Checkbox {
  int x, y;
  boolean b;
  Checkbox(int _x, int _y){
    x = _x;
    y = _y;
    b = false;
  }
  public void render(){
    stroke(255);
    fill(isOver()?128:64);
    rect(x, y, x+10, y+10);
    if(b){
      line(x, y, x+10, y+10);
      line(x, y+10, x+10, y);
    }
  }
  public void click(){
    if(isOver()){
      b=!b;
    }
  }
  public boolean isOver(){
    return(mouseX>x&&mouseX<x+20&&mouseY>y&&mouseY<y+20);
  }
}
Checkbox[] boxes = new Checkbox[52];
 




FloatTable data;
float dataMin,dataMax;

float plotX1,plotY1;
float plotX2,plotY2;
float labelX, labelY;

int currentColumn = 0;
int columnCount;
int rowCount;

int yearMin,yearMax;
int[] years;

int yearInterval = 1;
int volumeInterval = 1;

PFont plotFont;

String statString = "Data from 1960 to 2012 of all U.S. States";

int rectX[] = {960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,
               960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245,960,1055,1150,1245};
               
int rectY[] = {10,10,10,10,30,30,30,30,50,50,50,50,70,70,70,70,90,90,90,90,110,110,110,110,130,130,130,130,150,150,150,150,170,170,170,170,190,190,190,190,210,210,210,210,
                230,230,230,230,250,250,250,250};
               
boolean rectOver[] = new boolean[rectX.length];
int rectSize =10;
int rectColor, rectHighlight;

String[] Names = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","D.C.","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana",
                  "Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska",
                  "Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania",
                  "Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington",
                  "West Virginia","Wisconsin","Wyoming","U.S.A."};


public void setup(){

  
  for(int i=0;i<rectOver.length;i++){
  rectOver[i] = false;
   }
  data = new FloatTable("crime.tsv");
  columnCount = data.getColumnCount();
  rowCount = data.getRowCount();
  
  years = PApplet.parseInt (data.getRowNames());
  yearMin = years[0];
  yearMax = years[years.length - 1];
  
  dataMin = 0;
  dataMax = ceil(data.getTableMax() / volumeInterval) * volumeInterval;
  
  plotX1 = 120;
  plotX2 = width - plotX1;
  plotY1 = 80;
  plotY2 = height - plotY1;
  
  plotFont = createFont("SansSerif.bold",20);
  textFont(plotFont);
  rectColor = color(0);
  rectHighlight = color(51);
  

  for(int i=0; i< boxes.length; i++){
    boxes[i] = new Checkbox(rectX[i], rectY[i]);
  }
}

public void draw(){

  background(0xff000000);
  fill(0xff000000);
  rectMode(CORNERS);
  stroke(0xff000000);
  rect(plotX1-10,plotY1,plotX2+10,plotY2);
   mainTitle("Crime Data Of USA: 53 years of data");
  drawTitle();
  drawAxisLabels();
  drawYearLabels();
  drawVolumeLabels();
  
  stroke(0xffFE0AFF);
  strokeWeight(5);
  drawDataPoints(currentColumn); //Draw data points on grid
  
  noFill();
  stroke(0xffFF0000);
  strokeWeight(2);
 // drawDataLine(currentColumn); // draw lines when pressed key;
  
  drawDataHighlight(currentColumn);
  
 
// stroke(192);

for(int i=0;i<Names.length;i++){
   fill(0xffFFFEE0);
  textSize(11);
  textAlign(LEFT);
    text(Names[i],rectX[i]+15,rectY[i]+10);
   
}

       
   for(int i=0; i< boxes.length; i++){
    boxes[i].render();
    if(boxes[i].b){
      stroke(255);
     noFill();
 drawDataLine(i);
  drawDataHighlight(i);  
  
}
   }
}



public void drawTitle() {
  fill(0xffFFFEE0);
  textSize(20);
  textAlign(LEFT);
  String title = data.getColumnName(currentColumn);
  text(title, plotX1, plotY1 - 10);
}


public void drawAxisLabels() {
  fill(0xffFFFEE0);
  textSize(15);
  textLeading(15);
  
  textAlign(CENTER, CENTER);
  text("Crime \n Rate\n Per \n 100000 \nPopulation", labelX+40, (plotY1+plotY2)/2);
  textAlign(CENTER);
  text("Year", (plotX1+plotX2)/2, labelY+660);
}


public void drawYearLabels() {
  fill(0xffFFFEE0);
  textSize(10);
  textAlign(CENTER);
  
  // Use thin, gray lines to draw the grid
  stroke(224);
  strokeWeight(3);
  line(plotX1-15, plotY1, plotX1-15, plotY2);
  
  for (int row = 0; row < rowCount; row++) {
    if (years[row] % yearInterval == 0) {
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      text(years[row], x, plotY2 + textAscent() + 10);
     //line(x, plotY1, x, plotY2);
    }
  }
}


int volumeIntervalMinor = 1;   // Add this above setup()

public void drawVolumeLabels() {
  fill(0xffFFFEE0);
  textSize(10);
  textAlign(RIGHT);
  
  stroke(224);
  strokeWeight(3);
  line(plotX1-15, plotY1+540, plotX2+15, plotY1+540); 
  
  for (float v = dataMin; v <= dataMax; v += volumeIntervalMinor) {
    if (v % volumeIntervalMinor == 0) {     // If a tick mark
      float y = map(v, dataMin, dataMax, plotY2, plotY1);  
      if (v % volumeInterval == 0) {        // If a major tick mark
        float textOffset = textAscent()/2;  // Center vertically
        if (v == dataMin) {
          textOffset = 0;                   // Align by the bottom
        } else if (v == dataMax) {
          textOffset = textAscent();        // Align by the top
        }
        text(floor(v), plotX1-20, y + textOffset);
        //line(plotX1 - 10, y, plotX1, y);     // Draw major tick
      } else {
        line(plotX1 - 2, y, plotX1, y);     // Draw minor tick
      }
    }
  }
}

public void drawDataPoints(int col){
 
  for(int column = 0; column<columnCount;column++){
  for(int row = 0;row < rowCount; row++){
  
    if(data.isValid(row, column)){
    float value = data.getFloat(row,column);
    float x = map(years[row], yearMin, yearMax,plotX1,plotX2);
    float y = map(value, dataMin, dataMax, plotY2, plotY1);
    point(x,y);
 }
}
} 
}

public void drawDataLine(int col) {  
  beginShape();
  for (int row = 0; row < rowCount; row++) {
    if (data.isValid(row, col)) {
      float value = data.getFloat(row, col);
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      float y = map(value, dataMin, dataMax, plotY2, plotY1);      
      vertex(x, y);
    }
  }
  endShape();
}

public void drawDataHighlight(int col) {
  for (int row = 0; row < rowCount; row++) {
    if (data.isValid(row, col)) {
      float value = data.getFloat(row, col);
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      float y = map(value, dataMin, dataMax, plotY2, plotY1);
      if (dist(mouseX, mouseY, x, y) < 3) {
       // strokeWeight(10);
        point(x, y);
        fill(0xffFFFEE0);
        textSize(20);
        
        textAlign(CENTER);
        text(nf(value, 0, 2) + " (" + years[row] + ")", x, y-8);
        textAlign(LEFT);
      }
    }
  }
}


public void keyPressed(){
    if(key == '['){
        currentColumn--;
        if(currentColumn < 0){
        currentColumn = columnCount -1;
        }
    }else if(key ==']'){
    currentColumn++;
    if(currentColumn == columnCount){
    currentColumn = 0;
    }
    
 }
}


public void mousePressed(){
  for(int i=0; i< boxes.length; i++){
    boxes[i].click();
  }
}
  


// mainTitle //////////////////////////////////////////////////////////////////
public void mainTitle (String titleText) {
  textAlign(CENTER, CENTER);
  textFont(plotFont);
  fill(0xffFFFEE0);
  text(titleText, width/2, 25);
  textFont(plotFont);
  fill(0xffFFFEE0);
  text(statString, width/2, 60);
}
// first line of the file should be the column headers
// first column should be the row titles
// all other values are expected to be floats
// getFloat(0, 0) returns the first data value in the upper lefthand corner
// files should be saved as "text, tab-delimited"
// empty rows are ignored
// extra whitespace is ignored


class FloatTable {
  int rowCount;
  int columnCount;
  float[][] data;
  String[] rowNames;
  String[] columnNames;
  
  
  FloatTable(String filename) {
    String[] rows = loadStrings(filename);
    
    String[] columns = split(rows[0], TAB);
    columnNames = subset(columns, 1); // upper-left corner ignored
    scrubQuotes(columnNames);
    columnCount = columnNames.length;

    rowNames = new String[rows.length-1];
    data = new float[rows.length-1][];

    // start reading at row 1, because the first row was only the column headers
    for (int i = 1; i < rows.length; i++) {
      if (trim(rows[i]).length() == 0) {
        continue; // skip empty rows
      }
      if (rows[i].startsWith("#")) {
        continue;  // skip comment lines
      }

      // split the row on the tabs
      String[] pieces = split(rows[i], TAB);
      scrubQuotes(pieces);
      
      // copy row title
      rowNames[rowCount] = pieces[0];
      // copy data into the table starting at pieces[1]
      data[rowCount] = parseFloat(subset(pieces, 1));

      // increment the number of valid rows found so far
      rowCount++;      
    }
    // resize the 'data' array as necessary
    data = (float[][]) subset(data, 0, rowCount);
  }
  
  
  public void scrubQuotes(String[] array) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].length() > 2) {
        // remove quotes at start and end, if present
        if (array[i].startsWith("\"") && array[i].endsWith("\"")) {
          array[i] = array[i].substring(1, array[i].length() - 1);
        }
      }
      // make double quotes into single quotes
      array[i] = array[i].replaceAll("\"\"", "\"");
    }
  }
  
  
  public int getRowCount() {
    return rowCount;
  }
  
  
  public String getRowName(int rowIndex) {
    return rowNames[rowIndex];
  }
  
  
  public String[] getRowNames() {
    return rowNames;
  }

  
  // Find a row by its name, returns -1 if no row found. 
  // This will return the index of the first row with this name.
  // A more efficient version of this function would put row names
  // into a Hashtable (or HashMap) that would map to an integer for the row.
  public int getRowIndex(String name) {
    for (int i = 0; i < rowCount; i++) {
      if (rowNames[i].equals(name)) {
        return i;
      }
    }
    //println("No row named '" + name + "' was found");
    return -1;
  }
  
  
  // technically, this only returns the number of columns 
  // in the very first row (which will be most accurate)
  public int getColumnCount() {
    return columnCount;
  }
  
  
  public String getColumnName(int colIndex) {
    return columnNames[colIndex];
  }
  
  
  public String[] getColumnNames() {
    return columnNames;
  }


  public float getFloat(int rowIndex, int col) {
    // Remove the 'training wheels' section for greater efficiency
    // It's included here to provide more useful error messages
    
    // begin training wheels
    if ((rowIndex < 0) || (rowIndex >= data.length)) {
      throw new RuntimeException("There is no row " + rowIndex);
    }
    if ((col < 0) || (col >= data[rowIndex].length)) {
      throw new RuntimeException("Row " + rowIndex + " does not have a column " + col);
    }
    // end training wheels
    
    return data[rowIndex][col];
  }
  
  
  public boolean isValid(int row, int col) {
    if (row < 0) return false;
    if (row >= rowCount) return false;
    //if (col >= columnCount) return false;
    if (col >= data[row].length) return false;
    if (col < 0) return false;
    return !Float.isNaN(data[row][col]);
  }
  
  
  public float getColumnMin(int col) {
    float m = Float.MAX_VALUE;
    for(int row =0; row < rowCount; row++){
    
      if(isValid(row,col)){
      if(data[row][col] < m){
      m = data[row][col];
      }
      }
    }
   /* for (int i = 0; i < rowCount; i++) {
      if (!Float.isNaN(data[i][col])) {
        if (data[i][col] < m) {
          m = data[i][col];
        }
      }*/
    
    return m;
  }

  
  public float getColumnMax(int col) {
    float m = -Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      if (isValid(i, col)) {
        if (data[i][col] > m) {
          m = data[i][col];
        }
      }
    }
    return m;
  }

  
  public float getRowMin(int row) {
    float m = Float.MAX_VALUE;
    for (int i = 0; i < columnCount; i++) {
      if (isValid(row, i)) {
        if (data[row][i] < m) {
          m = data[row][i];
        }
      }
    }
    return m;
  } 

  
  public float getRowMax(int row) {
    float m = -Float.MAX_VALUE;
    for (int i = 0; i < columnCount; i++) {/*
      if (!Float.isNaN(data[row][i])) {*/
      if(isValid(row,i)){
        if (data[row][i] > m) {
          m = data[row][i];
        }
      }
    }
    return m;
  }
  
  
  public float getTableMin() {
    float m = Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (isValid(i, j)) {
          if (data[i][j] < m) {
            m = data[i][j];
          }
        }
      }
    }
    return m;
  }

  
  public float getTableMax() {
    float m = -Float.MAX_VALUE;
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (isValid(i, j)) {
          if (data[i][j] > m) {
            m = data[i][j];
          }
        }
      }
    }
    return m;
  }
}
  public void settings() {  size(1350,700);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_140424c" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
