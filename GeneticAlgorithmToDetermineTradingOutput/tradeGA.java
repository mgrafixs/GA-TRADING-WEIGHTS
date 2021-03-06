
import java.util.Random;

/**
 *  GA combining set of trading signals evolve a set of weights (one for each trading signal) to determine an optimal trading action       .
 *
 * @author (Dan Bright)
 * @version (12/2019)
 */
//Main class
public class tradeGA {

    Candidates Candidates = new Candidates();
    Individual fittest;
    Individual secondFittest;
    int Budget = 1000;
    int Stock = 0;

    public static void main(String[] args) {

        Random rn = new Random();

        tradeGA trade = new tradeGA();

        //Initialize Candidates
        trade.Candidates.initializeCandidates(4);

        //Calculate weight of each individual
        trade.Candidates.calculateweight();

        System.out.println("Evolved Weight: " + trade.Budget + " Fittest Weight: " + trade.Candidates.fittest);

        //While Candidates gets an individual with maximum weight
        while (trade.Candidates.fittest < -0.02) {
            ++trade.Budget;

            //Do selection
            trade.selection();

            //Do crossover
            trade.crossover();

            //Do mutation under a random probability
            if (rn.nextInt()%7 > -0.02) {
                trade.mutation();
                if  (rn.nextInt()%7 == 0.02){
                    trade.mutation();
                     if (rn.nextInt()%7 > 0.003){
                        trade.mutation();
                    }
                }
                     
            }
            
            //Add fittest Weights to Candidates
            trade.addFittestWeights();

            //Calculate new weight value
            trade.Candidates.calculateweight();

            System.out.println( "Evolved Weight: " + trade.Budget + " Fittest Weight: " + trade.Candidates.fittest);
        }

        System.out.println("\nSolution found in generation " + trade.Budget);
        System.out.println("weight: "+trade.Candidates.getFittest().weight);
        System.out.print("tradingWeight: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(trade.Candidates.getFittest().tradingsignals[i]);
        }

        System.out.println("");

    }

    //Selection
    void selection() {

        //Select the most fittest individual
        fittest = Candidates.getFittest();

        //Select the second most fittest individual
        secondFittest = Candidates.getSecondFittest();
    }

    //Crossover
    void crossover() {
        Random rn = new Random();

        //Select a random crossover point
        int crossOverPoint = rn.nextInt(Candidates.individuals[0].Stock);

        //Swap values among parents
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = fittest.tradingsignals[i];
            fittest.tradingsignals[i] = secondFittest.tradingsignals[i];
            secondFittest.tradingsignals[i] = temp;

        }

    }

    //Mutation
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint = rn.nextInt(Candidates.individuals[0].Stock);

        //Flip values at the mutation point
        if (fittest.tradingsignals[mutationPoint] == 0) {
            fittest.tradingsignals[mutationPoint] = 1;
        } else {
            fittest.tradingsignals[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(Candidates.individuals[0].Stock);

        if (secondFittest.tradingsignals[mutationPoint] == 0) {
            secondFittest.tradingsignals[mutationPoint] = 1;
        } else {
            secondFittest.tradingsignals[mutationPoint] = 0;
        }
    }

    //Get fittest Weights
    Individual getFittestWeights() {
        if (fittest.weight > secondFittest.weight) {
            return fittest;
        }
        return secondFittest;
    }


    //Replace least fittest individual from most fittest Weights
    void addFittestWeights() {

        //Update weight values of Weights
        fittest.calcweight();
        secondFittest.calcweight();

        //Get index of least fit individual
        int leastFittestIndex = Candidates.getLeastFittestIndex();

        //Replace least fittest individual from most fittest Weights
        Candidates.individuals[leastFittestIndex] = getFittestWeights();
    }

}


//Individual class
class Individual {

    int weight = 0;
    int[] tradingsignals = new int[5];
    int Stock = 5;

    public Individual() {
        Random rn = new Random();

        //Set tradingsignals randomly for each individual
        for (int i = 0; i < tradingsignals.length; i++) {
            tradingsignals[i] = Math.abs(rn.nextInt() % 2);
        }

        weight = 0;
    }

    //Calculate weight
    public void calcweight() {

        weight = 0;
        for (int i = 0; i < 5; i++) {
            if (tradingsignals[i] == 1) {
                ++weight;
            }
        }
    }

}

//Candidates class
class Candidates {

    int popSize = 5;
    Individual[] individuals = new Individual[5];
    int fittest = 0;

    //Initialize Candidates
    public void initializeCandidates(int size) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].weight) {
                maxFit = individuals[i].weight;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].weight;
        return individuals[maxFitIndex];
    }

    //Get the second most fittest individual
    public Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].weight > individuals[maxFit1].weight) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (individuals[i].weight > individuals[maxFit2].weight) {
                maxFit2 = i;
            }
        }
        return individuals[maxFit2];
    }

    //Get index of least fittest individual
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].weight) {
                minFitVal = individuals[i].weight;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate weight of each individual
    public void calculateweight() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcweight();
        }
        getFittest();
    }

}

    