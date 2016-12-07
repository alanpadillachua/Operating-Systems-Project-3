import java.util.*;

/**
 * Created by Alan Padilla on 11/21/2016.
 *  RR - Round Robin
 */
public class RR extends scheduler {
    private Queue<job> ready = new LinkedList<>(); // ready queue where jobs wait to run
    private int quantum; // quantum is user entered
    private int maxTime; // holds the max time of the list


    private void jobArrivedInterrupt(ArrayList<job> copyList,int time){
        ArrayList<job> jobWaitList = new ArrayList<>(); // temporary list
        for(job currentJob: copyList){//for each job in list
            if(currentJob.getArrivalTime() <= time){ // if currentJob arrived at the current time then add it to the list
                jobWaitList.add(new job(currentJob));// add it to another array list that holds all process at current time
            }
        }
        // now that we have a list of all the jobs that arrived before the current time
        // we must remove them from the copyList so they are not added twice
        for(job j: jobWaitList){
            int index = indexOfJob(copyList,j);
            copyList.remove(index); //
        }
        // then we add the jobs to the ready queue
        for(job newJob: jobWaitList){
            ready.add(new job(newJob)); // add it to the ready queue
        }
    }
    private int runJob(ArrayList<job> copyList,String[][] matrix, int time){
        int newTime = time;
        //can safely assume that each job has a row number set and that all jobs that arrived at 0 are already in the ready cue
        job job2Run; // holds the job that will run
        ArrayList<job> arrivalList = setupWaitList(copyList,time); // get the jobs that arrived at or before current time
        for(job j: arrivalList){ // for each job in the list
            //System.out.println(j.getName() + " added to zero queue at Time = " + time);
            ready.add(j); // add the job to queue
            int indexOfCopy = indexOfJob(copyList,j); // get the index of the job from the list in order to remove it
            copyList.remove(indexOfCopy); // remove job from the main list
        }
        try{
            if(!ready.isEmpty()) {
                job2Run = ready.remove(); //remove job from the ready queue
                newTime = job2Run.runSlice(quantum, time, matrix); // run the job by time and quantum
                if (!(isJobComplete(job2Run))) {// if job is not complete meaning it will not be removed
                    if(quantum > 1){ // if quantum is greater than 1 then we must add jobs that arrived before quantum was complete
                        jobArrivedInterrupt(copyList,(newTime - 1));//add case that add jobs that arrived before the job was complete
                    }
                    ready.add(new job(job2Run)); // add the job back to the end of queue
                }
            }
        }catch (NoSuchElementException e){ // catch error if list is empty
           System.out.println("Queue is empty");
        }
        return newTime;

    }
    public void schedule(ArrayList<job> list){
        setScheduler("Round Robin",true);
        Scanner input = new Scanner(System.in); // scanner used for input
        System.out.print("Please enter quantum value: ");
        quantum = input.nextInt(); // gets the quantum value to give the slices
        System.out.println(getName() + ", quantum = " + quantum); // print name
        int clock = 0; // clock
        setRowNums(list); // set the row numbers on list
        ArrayList<job> copyList = copy(list); // create copy of job list
        String[][] matrix = createMatrix(copyList); // create matrix for graph out put
        maxTime = maxLength(copyList);
        // no job should run at 0
        clock = setUpAtTimeZero(copyList,clock,ready);// gets the jobs that arrive at clock time 0
        // clock now = 1
        while (clock <= maxTime) { //run the clock to max time
            clock =  runJob(copyList, matrix, clock); // run the job
        }
        graphOutPut(matrix); // output the final graph

    }
}
