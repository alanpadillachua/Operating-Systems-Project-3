import java.util.Comparator;

/**
 * Created by Alan Padilla on 11/21/2016.
 *
 */
public class job {
    private String name; // name of job
    private int arrivalTime; // arrival time of job
    private int duration; // duration of job
    private int spaceCount; // spaceCount used for non preemptive schedulers
    private int row = -1; // row is only used for preemptive schedulers that assigns it a row in the output matrix
    private int runAmount = 0;
    //constructor used for copy
    job(job old){
        name = old.name;
        arrivalTime = old.arrivalTime;
        duration = old.duration;
        spaceCount = old.spaceCount;
        row = old.row; // copy row
        runAmount = old.runAmount; // copy the run amount
    }
    /**
     * Overloaded constructor used to set up the job
     */
    job(String n, int aTime, int dTime){
        name = n;
        arrivalTime = aTime;
        duration = dTime;
    }
    public void modifyArrivalTime(int num){
        arrivalTime += num;
    }

    public void setRow(int r){
        row = r;
    }
    public int getRow() {
        return row;
    }

    public void setSpaceCount(int num){
        spaceCount = num;
    }
    // accessor method returns name
    public String getName(){
        return name;
    }
    // accessor method returns arrival time
    public int getArrivalTime(){
        return arrivalTime;
    }
    // accessor method returns duration
    public int getDuration(){
        return duration;
    }
    // get space count
    public int getSpaceCount(){return spaceCount;}
    public int getRunAmount(){return runAmount;} // returns number of times job has ran
    /**
     * RUN method that runs until completion for non preemptive
     * schedulers
     */
    public void runCompletion(){
        for(int j = 0; j <= getSpaceCount(); j++){ // runs after the 1st job
            System.out.print(" "); // print a space
        }
        for(int i = 0; i < duration; i++){ // for loop that runs to completion
            System.out.print(name); // print name
        }
        System.out.println(); // print line complete
    }
    /**
     * RUN method that runs in slices for preemptive
     * schedulers
     * because preemptive schedulers output via matrix rather than directly to console
     * run prints directly to the output matrix that is passed into the run slice
     */
    public int runSlice(int timeSlice,int time,String[][] matrix){
        int tempTime = time - 1; // assume that time starts at 1
        for(int i = 0; i < timeSlice; i++){
            if(i < duration &&// make sure i stays within the duration
                    runAmount < duration &&  // and runAmount does also stays within duration
                    time < matrix[row].length){ // make sure it stays within bounds of the array
                matrix[row][tempTime] = name; // prints the name
                time++; // increase time
                tempTime++; // increase the temp time as well
                runAmount++; // increase run amount of the job
            }
        }
        return time; // return updated time
    }

}
