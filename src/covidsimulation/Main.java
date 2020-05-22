
package covidsimulation;


public class Main {

  public static final int regCount = 4;
  
  public static void main(String[] args) {
	  

	      int population = 15000;
	      int endTime = 1000;
	      int initInfectedPopulation = 1000;
	      double infectionProbability = 0.05;
	      int infectionDuration = 14;
	      double reservoirProbability = 0;
	      float peoplecanmove = 0.1f;
	      for(int i = 0; i<30; i++) {
		      Region R1 = new Region(population, endTime, initInfectedPopulation, infectionProbability, infectionDuration, reservoirProbability, peoplecanmove, 1,"R1");
		       		      
		      Region R2 = new Region(population, endTime, 0, infectionProbability, infectionDuration, reservoirProbability, peoplecanmove, 2,"R2");
		      
		      Region R3 = new Region(population, endTime, 0, infectionProbability, infectionDuration, reservoirProbability, peoplecanmove, 3, "R3");
		      
		      Region R4 = new Region(population, endTime, 0, infectionProbability, infectionDuration, reservoirProbability, peoplecanmove, 4, "R4");
		      
		      Region[] reg = {R1, R2, R3, R4};
		      CovidSim cs = new CovidSim(endTime);
		      cs.simulate(reg);
		      //R1.simulate();
		      CovidSim.movedpopulation = 0;
		      CovidSim.movedpopulationInfected = 0;
		      CovidSim.notMovedpopulation = 0;
		      CovidSim.notMovedpopulationInfected = 0;
		      CovidSim.suseptablemoved = 0;
		      CovidSim.infectedmoved = 0;
		      CovidSim.recoveredmoved = 0;
	      }
	      
  }
}


