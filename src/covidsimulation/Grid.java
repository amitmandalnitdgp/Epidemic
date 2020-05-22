
package covidsimulation;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
  Person[][] grid;
  int maxRows;
  int maxColumns;
  int [][] coordinates;
  int [][] choicecordinates;
  
  public Grid(int population, int havingChoice, int rID) {
    int[] dimensions = getDimensions(population);
    maxRows = dimensions[0];
    maxColumns = dimensions[1];
    grid = new Person[maxRows][maxColumns];
    //create an array to store position of entire population and initially it's coordinates set to -1
    coordinates = new int[population][2];
    for(int i= 0; i<coordinates.length; i++) {
    	coordinates[i][0] = -1;
    	coordinates[i][1] = -1;
    }
    
    choicecordinates = new int[havingChoice][2];
    for(int i=0; i<choicecordinates.length; i++) {
    	choicecordinates[i][0] = -1;
    	choicecordinates[i][0] = -1;
    }
    
    initializepeople(population, rID);
    initiaizeChoice(havingChoice, rID);
  }
  
  private int[] getDimensions(int population) {
      double squareRoot = Math.sqrt(population);
      int[] dimensions = new int[2];
      int increment = 50;
      if (squareRoot == Math.floor(squareRoot)) {
          dimensions[0] = (int)squareRoot+increment;
          dimensions[1] = (int)squareRoot+increment;
      } else {
          dimensions[0] = (int)Math.ceil(squareRoot)+increment;
          dimensions[1] = (int)Math.ceil(squareRoot)+increment;
      }
      return dimensions;
  }
  
  public int getMaxRows() {
    return maxRows;
  }
  
  public int getMaxColumns() {
    return maxColumns;
  }
  
  //fill grid up with persons
  public void initializepeople(int population, int rID){
    int counter = 0;
    Random generator = new Random();  
	  for (int i = 0; i < maxRows; i++) {
	        for (int j = 0; j < maxColumns; j++) {
	          Person p = new Person(i, j);
	          grid[i][j] = p;
	     //     grid1[i][j] = p;
	          p.setState(0);
	          p.coordinates.x = i;
	          p.coordinates.y = j;
	        }
	      }
	  
    while(counter<population) {
	    int x = generator.nextInt(maxRows);
	    int y = generator.nextInt(maxColumns);
	    if(!isFieldPopulated(x, y, counter)) {
	    	grid[x][y].setState(1);
	    	grid[x][y].initialRegion = rID;
	    	grid[x][y].whenMoved = 1;
	    	//grid[x][y].choice = -1;
	    	counter++;
	    }
    }
      
  }
 
  // 0 none present, -1 no choice, 1 .. n choice
	public void initiaizeChoice(int havingChoice, int rid) {
		//System.out.println(rid+"--->"+havingChoice);
		int counter = 0;
		Random generator = new Random();
		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < maxColumns; j++) {
				if (grid[i][j].state != 0) {
					grid[i][j].choice = -1;
				}
			}
		}
		while(counter < havingChoice) {
			 int x = generator.nextInt(maxRows);
			 int y = generator.nextInt(maxColumns);
			 int ch = 0;
			 if(grid[x][y].state==1) {
				 if(!isChoiceFieldPopulated(x, y, counter)) {
					 ch = 1+ generator.nextInt(Main.regCount);
					 if(ch != rid) {
						 grid[x][y].choice = ch;
						 counter++;
					 }
				 }
			 }
		}
	}
  
	
	
  //checks if that field is already populated returns true is place is already occupied
  public boolean isFieldPopulated(int x, int y, int counter) {
	  boolean status = false;
	  
	  for(int i=0; i<counter; i++) {
		  if((x==coordinates[i][0])&&(y==coordinates[i][1])) {
			  status = true;
			  break;
		  }
	  }
	  if(status == false) {
		  coordinates[counter][0] = x;
		  coordinates[counter][1] = y;
	  }
	  return status;
  }
  
  public boolean isChoiceFieldPopulated(int x, int y, int counter) {
	  boolean status = false;
	  
	  for(int i=0; i<counter; i++) {
		  if((x==choicecordinates[i][0])&&(y==choicecordinates[i][1])) {
			  status = true;
			  break;
		  }
	  }
	  if(status == false) {
		  choicecordinates[counter][0] = x;
		  choicecordinates[counter][1] = y;
	  }
	  return status;
  }
  
  
  //change state to susceptible(1), infected(2) or resistant(3) 
  public void changeState(int x, int y, int state) {
    grid[x][y].setState(state);
  }
  
  //returns person at coordinate x, y
  public Person getPerson(int x, int y) {
    return grid[x][y];
  }
  
  //returns number of susceptible persons
  public int getSusceptibleCount() {
    int susceptibleCount = 0;
    for (int i = 0; i < maxRows; i++) {
      for (int j = 0; j < maxColumns; j++) {
        Person currPerson = grid[i][j];
        if(currPerson.isSusceptible()) {
          susceptibleCount++;
        }
      }
    }
    return susceptibleCount;
  }
  
  
  //sets state of persons in given coordinates to infected(2)
  public void setInfectedCoordinates(ArrayList<Coordinates> infectedCoordinates) {
    for(int i=0; i<infectedCoordinates.size(); i++) {
      Coordinates currCoordinates = infectedCoordinates.get(i);
      int x = currCoordinates.getX();
      int y = currCoordinates.getY();
      this.changeState(x, y, 2);
    }
  }
  
  //returns number of infected persons
  public int getInfectedCount() {
    int infectedCount = 0;
    for (int i = 0; i < maxRows; i++) {
      for (int j = 0; j < maxColumns; j++) {
        Person currPerson = grid[i][j];
        if(currPerson.isInfected()) {
          infectedCount++;
        }
      }
    }
    return infectedCount;
  }
  
  
  //returns number of resistant persons
  public int getResistantCount() {
    int resistantCount = 0;
    for (int i = 0; i < maxRows; i++) {
      for (int j = 0; j < maxColumns; j++) {
        Person currPerson = grid[i][j];
        if(currPerson.isResistant()) {
          resistantCount++;
        }
      }
    }
    return resistantCount;
  }
  
  //increases each person's currStateTimeStep by 1, except for those who have changed states (becomes 0)

  
  //displays the grid with each persons's state: 1-susceptible, 2-infected, 3-resistant
  public String display() {
    String display = "";
    for (int i = 0; i < maxRows; i++) {
      display += "[ ";
      for (int j = 0; j < maxColumns; j++) {
        display += grid[i][j].getState() + " ";
      }
      display += "] \n";
    }
    return display;
  }
  
  //displays the grid containing how many time steps each individual has been at its current state
  public String displayTimes() {
    String display = "";
    for (int i = 0; i < maxRows; i++) {
      display += "[ ";
      for (int j = 0; j < maxColumns; j++) {
        display += grid[i][j].getCurrStateTimeStep() + " ";
      }
      display += "] \n";
    }
    return display;
  }
}