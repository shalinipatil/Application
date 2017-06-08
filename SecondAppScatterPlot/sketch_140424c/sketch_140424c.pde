class Checkbox {
  int x, y;
  boolean b;
  Checkbox(int _x, int _y){
    x = _x;
    y = _y;
    b = false;
  }
  void render(){
    stroke(255);
    fill(isOver()?128:64);
    rect(x, y, x+10, y+10);
    if(b){
      line(x, y, x+10, y+10);
      line(x, y+10, x+10, y);
    }
  }
  void click(){
    if(isOver()){
      b=!b;
    }
  }
  boolean isOver(){
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
color rectColor, rectHighlight;

String[] Names = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","D.C.","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana",
                  "Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska",
                  "Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania",
                  "Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington",
                  "West Virginia","Wisconsin","Wyoming","U.S.A."};


void setup(){

  size(1350,700);
  for(int i=0;i<rectOver.length;i++){
  rectOver[i] = false;
   }
  data = new FloatTable("crime.tsv");
  columnCount = data.getColumnCount();
  rowCount = data.getRowCount();
  
  years = int (data.getRowNames());
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
  smooth();

  for(int i=0; i< boxes.length; i++){
    boxes[i] = new Checkbox(rectX[i], rectY[i]);
  }
}

void draw(){

  background(#000000);
  fill(#000000);
  rectMode(CORNERS);
  stroke(#000000);
  rect(plotX1-10,plotY1,plotX2+10,plotY2);
   mainTitle("Crime Data Of USA: 53 years of data");
  drawTitle();
  drawAxisLabels();
  drawYearLabels();
  drawVolumeLabels();
  
  stroke(#FE0AFF);
  strokeWeight(5);
  drawDataPoints(currentColumn); //Draw data points on grid
  
  noFill();
  stroke(#FF0000);
  strokeWeight(2);
 // drawDataLine(currentColumn); // draw lines when pressed key;
  
  drawDataHighlight(currentColumn);
  
 
// stroke(192);

for(int i=0;i<Names.length;i++){
   fill(#FFFEE0);
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



void drawTitle() {
  fill(#FFFEE0);
  textSize(20);
  textAlign(LEFT);
  String title = data.getColumnName(currentColumn);
  text(title, plotX1, plotY1 - 10);
}


void drawAxisLabels() {
  fill(#FFFEE0);
  textSize(15);
  textLeading(15);
  
  textAlign(CENTER, CENTER);
  text("Crime \n Rate\n Per \n 100000 \nPopulation", labelX+40, (plotY1+plotY2)/2);
  textAlign(CENTER);
  text("Year", (plotX1+plotX2)/2, labelY+660);
}


void drawYearLabels() {
  fill(#FFFEE0);
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

void drawVolumeLabels() {
  fill(#FFFEE0);
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

void drawDataPoints(int col){
 
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

void drawDataLine(int col) {  
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

void drawDataHighlight(int col) {
  for (int row = 0; row < rowCount; row++) {
    if (data.isValid(row, col)) {
      float value = data.getFloat(row, col);
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      float y = map(value, dataMin, dataMax, plotY2, plotY1);
      if (dist(mouseX, mouseY, x, y) < 3) {
       // strokeWeight(10);
        point(x, y);
        fill(#FFFEE0);
        textSize(20);
        
        textAlign(CENTER);
        text(nf(value, 0, 2) + " (" + years[row] + ")", x, y-8);
        textAlign(LEFT);
      }
    }
  }
}


void keyPressed(){
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


void mousePressed(){
  for(int i=0; i< boxes.length; i++){
    boxes[i].click();
  }
}
  


// mainTitle //////////////////////////////////////////////////////////////////
void mainTitle (String titleText) {
  textAlign(CENTER, CENTER);
  textFont(plotFont);
  fill(#FFFEE0);
  text(titleText, width/2, 25);
  textFont(plotFont);
  fill(#FFFEE0);
  text(statString, width/2, 60);
}




