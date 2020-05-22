package covidsimulation;

import java.util.ArrayList;
import java.util.Random;

public class CovidSim {

	public static int movedpopulation = 0;
	public static int notMovedpopulation = 0;
	public static int movedpopulationInfected = 0;
	public static int notMovedpopulationInfected = 0;
	public static int suseptablemoved = 0;
	public static int infectedmoved = 0;
	public static int recoveredmoved = 0;
	public int endTime = 0;
	public int lockdownperiod = 10;
	public int maxPeopleCanMove = 20;
	public int atOnceCanMove = 2;
	public Random generator;
	public ArrayList<MovementIndex> mvIndex;
	public boolean flag = false;
	public int ni, nj;

	public CovidSim(int endTime) {
		this.endTime = endTime;
		generator = new Random();
		mvIndex = new ArrayList<>();
	}

	public void simulate(Region[] region) {

		int currTime = 0;
		int regionCount = region.length;
		StoreData sd = new StoreData();
		generateMovementOptions();
		for (int k = 0; k < regionCount; k++) {
			region[k].susceptibleCounts[0] = region[k].population;
			region[k].infectedCounts[0] = region[k].initInfectedPopulation;
			region[k].recoverCounts[0] = 0;
			region[k].totalPopulation[0] = region[k].population + region[k].initInfectedPopulation;
		}

		while (currTime != endTime) {

			for (int k = 0; k < regionCount; k++) {
				for (int i = 0; i < region[k].prevGrid.getMaxRows(); i++) {
					for (int j = 0; j < region[k].prevGrid.getMaxColumns(); j++) {

						if (region[k].prevGrid.grid[i][j].state == 1) {
							double randomProbability = Math.random();
							flag = true;
							ni = i + 1;
							nj = j;
							if (ni < region[k].prevGrid.maxRows && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i - 1;
							nj = j;
							if (ni >= 0 && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i;
							nj = j + 1;
							if (nj < region[k].prevGrid.maxColumns && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i;
							nj = j - 1;
							if (nj >= 0 && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i + 1;
							nj = j + 1;
							if (ni < region[k].prevGrid.maxRows && nj < region[k].prevGrid.maxColumns && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i - 1;
							nj = j - 1;
							if (ni >= 0 && nj >= 0 && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i + 1;
							nj = j - 1;
							if (ni < region[k].prevGrid.maxRows && nj >= 0 && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
							ni = i - 1;
							nj = j + 1;
							if (ni >= 0 && nj < region[k].prevGrid.maxColumns && flag) {
								if (region[k].prevGrid.grid[ni][nj].state == 2) {
									if (randomProbability < region[k].infectionProbability) {
										region[k].currGrid.grid[i][j].state = 2;
										flag = false;
									}
								}
							}
						}

						if (region[k].prevGrid.grid[i][j].state == 2) {

							if (region[k].prevGrid.grid[i][j].currStateTimeStep == region[k].resistanceRate) {

								region[k].currGrid.grid[i][j].state = 3;

							}
							region[k].currGrid.grid[i][j].currStateTimeStep++;
						}
					}
				}
				equateGrids(region[k]);

				int susceptibleC = CountSuseptable(region[k]);
				int infectedC = CountInfected(region[k]);
				int resistantC = CountRecovered(region[k]);

				region[k].susceptibleCounts[currTime + 1] = (susceptibleC);
				region[k].infectedCounts[currTime + 1] = (infectedC);
				region[k].recoverCounts[currTime + 1] = (resistantC);
				region[k].totalPopulation[currTime + 1] = (susceptibleC + infectedC + resistantC);

				sd.save("avg10-SD-" + region[k].Name + ".txt", susceptibleC, infectedC, resistantC,
						(susceptibleC + infectedC + resistantC));
				if (currTime == (endTime - 1))
					sd.save("SD-" + region[k].Name + ".txt", susceptibleC, infectedC, resistantC,
							(susceptibleC + infectedC + resistantC));
			}

			currTime++;
			//if (currTime < 84 || currTime > 182)
				movePeople(region, currTime);

		}
		countMovedPeople(region);
		sd.save("Choics-Data.txt", movedpopulation, movedpopulationInfected, notMovedpopulation,notMovedpopulationInfected, suseptablemoved, infectedmoved, recoveredmoved);

	}

	public void equateGrids(Region region) {
		for (int m = 0; m < region.prevGrid.maxRows; m++) {
			for (int n = 0; n < region.prevGrid.maxColumns; n++) {
				region.prevGrid.grid[m][n].choice = region.currGrid.grid[m][n].choice;
				region.prevGrid.grid[m][n].state = region.currGrid.grid[m][n].state;
				region.prevGrid.grid[m][n].whenMoved = region.currGrid.grid[m][n].whenMoved;
				region.prevGrid.grid[m][n].currStateTimeStep = region.currGrid.grid[m][n].currStateTimeStep;
				region.prevGrid.grid[m][n].initialRegion = region.currGrid.grid[m][n].initialRegion;
				region.prevGrid.grid[m][n].coordinates.x = region.currGrid.grid[m][n].coordinates.x;
				region.prevGrid.grid[m][n].coordinates.y = region.currGrid.grid[m][n].coordinates.y;
			}
		}
	}

	public void countMovedPeople(Region[] region) {
		for (int k = 0; k < region.length; k++) {
			for (int i = 0; i < region[k].currGrid.maxRows; i++) {
				for (int j = 0; j < region[k].currGrid.maxColumns; j++) {
					if ((region[k].currGrid.grid[i][j].initialRegion != (k + 1))
							&& (region[k].currGrid.grid[i][j].initialRegion != 0)) {
						CovidSim.movedpopulation++;
						if ((region[k].currGrid.grid[i][j].whenMoved == 1) && (region[k].currGrid.grid[i][j].state == 2
								|| region[k].currGrid.grid[i][j].state == 3)) {
							CovidSim.movedpopulationInfected++;
						}
						if (region[k].currGrid.grid[i][j].whenMoved == 1) {
							CovidSim.suseptablemoved++;
						}
						if (region[k].currGrid.grid[i][j].whenMoved == 2) {
							CovidSim.infectedmoved++;
						}
						if (region[k].currGrid.grid[i][j].whenMoved == 3) {
							CovidSim.recoveredmoved++;
						}
					} else {
						if (region[k].currGrid.grid[i][j].state != 0) {
							CovidSim.notMovedpopulation++;
						}
						if (region[k].currGrid.grid[i][j].state == 2 || region[k].currGrid.grid[i][j].state == 3) {
							CovidSim.notMovedpopulationInfected++;
						}
					}
				}
			}
		}
	}

	public void movePeople(Region[] region, int currTime) {
		for (int i = 0; i < mvIndex.size(); i++) {
			moveBetweenRegion(region[mvIndex.get(i).R1 - 1], region[mvIndex.get(i).R2 - 1], currTime);

		}
	}

	public void generateMovementOptions() {
		int factor = 2;
		int r2 = 0;
		System.out.println("............ Generating Movement Options ............");
		for (int r1 = 1; r1 <= Main.regCount; r1++) {
			r2 = factor;
			while (r2 <= Main.regCount) {
				MovementIndex mv = new MovementIndex(r1, r2);
				this.mvIndex.add(mv);
				r2++;
			}
			factor++;

		}
	}

	// move from X to Y and Vice-versa
	public void moveBetweenRegion(Region regionx, Region regiony, int currTime) {

		float movementProb = 0;
		movementProb = getMovementProbability(regionx, regiony, currTime);
		// move from X to Y
		if (regionx.canMove.size() > 0) {
			for (int i = 0; i < regionx.canMove.size(); i++) {
				if (regionx.canMove.get(i).choice == regiony.regID) {
					boolean wish = false;

					float rand = generator.nextFloat();
					if (rand < movementProb)
						wish = true;
					if (wish) {
						if (regiony.emptySpace.size() > 0) {
							int destination = generator.nextInt(regiony.emptySpace.size());
							Coordinates cord = regiony.emptySpace.get(destination);
							regiony.currGrid.grid[cord.x][cord.y].state = regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].state;
							regiony.currGrid.grid[cord.x][cord.y].choice = regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].choice;
							regiony.currGrid.grid[cord.x][cord.y].currStateTimeStep = regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].currStateTimeStep;
							regiony.currGrid.grid[cord.x][cord.y].initialRegion = regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].initialRegion;
							regiony.currGrid.grid[cord.x][cord.y].whenMoved = regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].state;
							regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].state = 0;
							regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].choice = 0;
							regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].currStateTimeStep = 0;
							regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].initialRegion = 0;
							regionx.currGrid.grid[regionx.canMove.get(i).coordinates.x][regionx.canMove.get(i).coordinates.y].whenMoved = 0;

							System.out.println("........moved from 1 to 2 (" + regionx.canMove.get(i).coordinates.x + "," + regionx.canMove.get(i).coordinates.y + ") ..........");

							regiony.emptySpace.remove(destination);
							regionx.canMove.remove(i);
							i--;

						}
					}
				}
			}
		}
		// move from Y to X
		movementProb = getMovementProbability(regiony, regionx, currTime);
		if (regiony.canMove.size() > 0) {
			for (int i = 0; i < regiony.canMove.size(); i++) {
				if (regiony.canMove.get(i).choice == regionx.regID) {
					boolean wish = false;

					float rand = generator.nextFloat();
					if (rand < movementProb)
						wish = true;
					if (wish) {
						if (regionx.emptySpace.size() > 0) {
							int destination = generator.nextInt(regionx.emptySpace.size());
							Coordinates cord = regionx.emptySpace.get(destination);
							regionx.currGrid.grid[cord.x][cord.y].state = regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].state;
							regionx.currGrid.grid[cord.x][cord.y].choice = regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].choice;
							regionx.currGrid.grid[cord.x][cord.y].currStateTimeStep = regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].currStateTimeStep;
							regionx.currGrid.grid[cord.x][cord.y].initialRegion = regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].initialRegion;
							regionx.currGrid.grid[cord.x][cord.y].whenMoved = regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].state;
							regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].state = 0;
							regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].choice = 0;
							regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].currStateTimeStep = 0;
							regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].initialRegion = 0;
							regiony.currGrid.grid[regiony.canMove.get(i).coordinates.x][regiony.canMove.get(i).coordinates.y].whenMoved = 0;

							System.out.println("........moved from 2 to 1 (" + regiony.canMove.get(i).coordinates.x+ "," + regiony.canMove.get(i).coordinates.y + ") ..........");

							regionx.emptySpace.remove(destination);
							regiony.canMove.remove(i);
							i--;

						}
					}
				}
			}
		}

		equateGrids(regionx);
		equateGrids(regionx);
	}

	public int CountSuseptable(Region reg) {
		int counts = 0;
		for (int i = 0; i < reg.prevGrid.maxRows; i++) {
			for (int j = 0; j < reg.prevGrid.maxColumns; j++) {
				if (reg.currGrid.grid[i][j].state == 1) {
					counts++;
				}
			}
		}
		return counts;
	}

	public int CountInfected(Region reg) {
		int counti = 0;
		for (int i = 0; i < reg.prevGrid.maxRows; i++) {
			for (int j = 0; j < reg.prevGrid.maxColumns; j++) {
				if (reg.currGrid.grid[i][j].state == 2) {
					counti++;
				}
			}
		}
		return counti;
	}

	public int CountRecovered(Region reg) {
		int countr = 0;
		for (int i = 0; i < reg.prevGrid.maxRows; i++) {
			for (int j = 0; j < reg.prevGrid.maxColumns; j++) {
				if (reg.currGrid.grid[i][j].state == 3) {
					countr++;
				}
			}
		}
		return countr;
	}

	// while moving from regx to regy
	public float getMovementProbability(Region regx, Region regy, int currTime) {
		float prob = 0;
		prob = ((regx.infectedCounts[currTime] - regy.infectedCounts[currTime]) / 2)
				/ (regx.totalPopulation[currTime] * (regx.choiceFraction / (Main.regCount - 1)));
		if (prob <= 0)
			prob = 0;
		return prob;
	}

}
