
package covidsimulation;

import java.util.ArrayList;
import java.util.Random;

public class Region {
  int regID; 
  int population;
  int endTime;
  int initInfectedPopulation;
  double infectionProbability;
  int resistanceRate;
  double reservoirProbability;
  float choiceFraction;
  int havingChoice;
  String Name;
  Grid prevGrid;
  Grid currGrid;
  int[] susceptibleCounts;
  int[] infectedCounts;
  int[] recoverCounts;
  int[] totalPopulation;
  ArrayList<Person> canMove;
  ArrayList<Coordinates> emptySpace;
  
  public Region(int population, int endTime, int initInfectedPopulation, double infprob, int infectionDuration, double reservoirProbability, float peoplecanmove, int rID, String name) {
    this.regID = rID;
    this.population = population;
    this.choiceFraction = peoplecanmove;
    this.havingChoice = (int) ((int) population*peoplecanmove);
    this.endTime = endTime;
    this.prevGrid = new Grid(population, havingChoice, rID);
    this.currGrid = new Grid(population, havingChoice, rID);
    for(int i=0; i<prevGrid.maxRows; i++) {
    	for(int j=0; j<prevGrid.maxColumns; j++) {
    		currGrid.grid[i][j].choice = prevGrid.grid[i][j].choice;
    		currGrid.grid[i][j].state = prevGrid.grid[i][j].state;
    		currGrid.grid[i][j].whenMoved = prevGrid.grid[i][j].whenMoved;
    		currGrid.grid[i][j].currStateTimeStep = prevGrid.grid[i][j].currStateTimeStep;
    		currGrid.grid[i][j].initialRegion = prevGrid.grid[i][j].initialRegion;
    		currGrid.grid[i][j].coordinates.x = prevGrid.grid[i][j].coordinates.x;
    		currGrid.grid[i][j].coordinates.y = prevGrid.grid[i][j].coordinates.y;
    	}
    }
    this.initInfectedPopulation = initInfectedPopulation;
    this.infectionProbability = infprob; 
    this.resistanceRate = infectionDuration;
    if(this.resistanceRate < 0) {this.resistanceRate = 0;}
    this.reservoirProbability = reservoirProbability;
    this.Name = name;
    this.susceptibleCounts = new int[endTime+1];
    this.infectedCounts = new int[endTime+1];
    this.recoverCounts = new int[endTime+1];
    this.totalPopulation = new int[endTime+1];
    this.canMove = new ArrayList<>();
    this.emptySpace = new ArrayList<>();
    this.initialize();
    this.peoplCanMove();
    this.getEmptySpace();

    System.out.println("region - "+ rID+" initialized----dimention: "+prevGrid.maxRows+" X " + prevGrid.maxColumns+".................");
    
    
  }
  
  //chooses random persons to be the initially infected members of the population
  public void initialize(){  
    for(int i = 0; i < initInfectedPopulation; i++) {
      Random generator = new Random();
      int x = generator.nextInt(prevGrid.getMaxRows());
      int y = generator.nextInt(prevGrid.getMaxRows());
      if(!currGrid.getPerson(x, y).isExcluded()) {
        prevGrid.changeState(x, y, 2);
        currGrid.changeState(x, y, 2);
        currGrid.grid[x][y].whenMoved = 2;
        prevGrid.grid[x][y].whenMoved = 2;
      } else {
          i--;
      }
    }
  }
   	  

  
  public void peoplCanMove() {
	  for(int p=0; p<this.currGrid.maxRows; p++) {
		  for(int q=0; q<this.currGrid.maxColumns;q++) {
			  if(this.currGrid.grid[p][q].choice>0) {
				  this.canMove.add(this.currGrid.grid[p][q]);
			  }
		  }
	  }
  }
  
  public void getEmptySpace() {
	  for(int p=0; p<this.currGrid.maxRows; p++) {
		  for(int q=0; q<this.currGrid.maxColumns;q++) {
			  if(this.currGrid.grid[p][q].state==0) {
				  Coordinates cord = new Coordinates(p, q);
				  this.emptySpace.add(cord);
			  }
		  }
	  }
  }
  
  
}
