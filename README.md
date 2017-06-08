First Application uses the thematic map with three layer. First layer uses Open Street Map data and Mapbox to draw the 
neighboring territories.  Base layer shows the population density of US States by using interactive choropleth map. 
Choropleth map has been referenced from leaflet examples. Purpose of this layer is to render geojson data into the map format. 
Various interactions are been set such as hovering displays the state name and the population density. 
Legend has been used to visualize the color coding with respect to density. Hovering on each state highlights the state boundary. 
Zoom in at different levels shows road map of respective state. 

Second Layer uses interaction to show three years of crime comparison and poverty rate. 
On selection of year from dropdown menu, bar chart will be displayed. Minichart interaction uses leaflet plugin named leaflet.minichart. 
Second Application is Parallel Coordinates graph also named as Scatter Graph and is the best way to represent multidimensional data.
Points in scatter graph will show the distribution of crimes over the time for each state. As the number is bigger to represent 
data in scatter format, one couldn't easily figure out the aim of represented data. So to make it easy, we can use interaction 
events like zoom in/zoom out, Dragging etc. Addition to this, Interaction like hover will help to analyze single point and the data 
related to that point.

Programming is focused on HTML, CSS javascript, leaflet library and plugins such as leaflet label, leaflet minichart.  
Second application uses processing visualization sketch box language. All above mention resources are open source, 
free to download with lots of documentation. 

Datasets used for the first application are of .geojson, .js, and .json file type and obtained from https://www.census.gov/geo/maps-data/.
Second Layer of first application uses only three years of data for sake of simplicity. The dataset needed for the second application is of 
.csv file type obtained from http://www.ucrdatatool.gov/ and it includes crime information from year 1960 to year 2012 for all 51 
territories (50 states + 1 federal district Washington, D.C.) in United States of America.  
