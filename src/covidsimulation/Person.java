
package covidsimulation;


public class Person {
  int state = 1;
  int choice=0;
  int initialRegion = 0;
  int whenMoved = 0; 
  int currStateTimeStep; //counter for how many time steps a person has been in that current state 
  Coordinates coordinates;
  
  public Person(int x, int y) {
    this.coordinates = new Coordinates(x, y);
    this.currStateTimeStep = 0;
    //choice = generator.nextInt(3);
  }

  
 
  public void setState(int state) {
    this.state = state;
  }
  
  public int getState() {
    return state;
  }
  
  public boolean isExcluded() {
      if(state == 0) {
          return true;
      } else {
          return false;
      }
  }
  
  public boolean isSusceptible() {
    if(state == 1) {
      return true;
    } else {
      return false;
    }
  }  
  
  public boolean isInfected() {
    if(state == 2) {
      return true;
    } else {
      return false;
    }
  }
  
  public boolean isResistant() {
      if(state == 3) {
      return true;
    } else {
      return false;
    }  
  }
  
  public int getchoice() {
	  return choice;
  }
  
  public int getCurrStateTimeStep() {
    return currStateTimeStep;
  }

  public void resetCurrStateTimeStep() {
    currStateTimeStep = 0;
  }
  
  public void incCurrStateTimeStep() {
    currStateTimeStep++;
  }
}


