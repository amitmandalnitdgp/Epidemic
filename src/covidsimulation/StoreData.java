package covidsimulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class StoreData {
	

	public void save(String name, int susceptible, int infected, int recovered, int totalPopulation) {
		File file = new File(name);
		
		FileWriter fr;
		try {
			fr = new FileWriter(file, true);
			fr.write(""+susceptible+"\t"+infected+"\t"+recovered+ "\t"+totalPopulation+"\n");
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void save(String name, int movedpopulation, int movedpopulationInfected, int notMovedpopulation, int notMovedpopulationInfected, int suseptablemoved, int infectedmoved, int recoveredmoved) {
		File file = new File(name);
		int total = movedpopulationInfected+notMovedpopulationInfected+infectedmoved+recoveredmoved;
		FileWriter fr;
		try {
			fr = new FileWriter(file, true);
			fr.write(""+movedpopulation+"\t"+movedpopulationInfected+"\t"+notMovedpopulation+ "\t"+notMovedpopulationInfected+ "\t"+suseptablemoved+ "\t"+infectedmoved+"\t"+recoveredmoved+ "\t"+total+"\n");
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
