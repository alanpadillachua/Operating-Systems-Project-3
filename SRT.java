import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by Alan Padilla on 11/21/2016.
 * SRT - shortest remaining time
 *
 */
public class SRT extends scheduler {
    private int quantum ; // quantum is set to 1

    private boolean isJobInList(ArrayList<job> jobWaitList, job current){
        for(job j: jobWaitList){ // for each job in list
            if(j.getName().equals(current.getName())){// if there is a job with the same name
                return true; // then return true that the list already has that job
            }
        }
        return false; // else return false
    }
    private job getJobSRT(ArrayList<job> copyList,ArrayList<job> jobWaitList,int time){ // returns the job with the shortest remaining time from the current list
        //; // temporary list
        int tempTime = time - 1; //
        for(job currentJob: copyList){//for each job in list
            if(currentJob.getArrivalTime() == tempTime){ // if currentJob arrived at the current time then add it to the list
                if(!isJobInList(jobWaitList,currentJob)) { // if the job is not already in the wait list
                    jobWaitList.add(new job(currentJob));// add it to another array list that holds all process at current time
                }
            }
        }
        job chosen = null; // chosen holds the job that must be returned
        int SRT = 10000; // srt holds the shortest remaining time
        int jSRT; // current jobs SRT
        for(job j: jobWaitList){ // for each job in the temporary list
            jSRT = j.getDuration() - j.getRunAmount(); // jSRT is equal to its duration mines the times it has run
            if(jSRT <= SRT){ // if the jobs SRT is smaller than the previous SRT
                SRT = jSRT; // update SRT
                chosen = new job(j); // chosen now is the current job
            }
        }
        int index = indexOfJob(jobWaitList,chosen); // get index from copylist
        return jobWaitList.get(index); // return the job at that index
    }
    private int runJob(ArrayList<job> copyList,ArrayList<job> jobWaitList, String[][] matrix, int time){
        int newTime = time; // new time holds the updated time that continues the loop
        job job2run; // job that holds the job that will run from the ready queue

        if(!jobWaitList.isEmpty()){ // if list is not empty
            job2run = getJobSRT(copyList,jobWaitList,time); //get the job to run
            newTime = job2run.runSlice(quantum,time,matrix); // run the job for 1 quantum
            if(isJobComplete(job2run)){//if job is complete
                int waitIndex = indexOfJob(jobWaitList,job2run); // get index from waitlist
                jobWaitList.remove(waitIndex); // remove it from the wait list
                int index = indexOfJob(copyList,job2run); // get index to remove it from  main list
                copyList.remove(index); // remove it
            }
            // if not than stays in the list and continues
        }
        return newTime; //return updated time
    }

    public void schedule(ArrayList<job> list){
        setScheduler("Shortest Reaming Time",true);
        quantum = 1; // set quantum to 1
        System.out.println(getName() + ", quantum = " + quantum); // print name
        int maxTime; // holds the max time
        int clock = 1; // clock starts at 1
        setRowNums(list); // set the row numbers on list
        ArrayList<job> copyList = copy(list); // create copy of job list
        String[][] matrix = createMatrix(copyList); // create matrix for graph out put
        maxTime = maxLength(copyList);// get the max time to run the while loop
        ArrayList<job> jobWaitList = new ArrayList<>(); // list that acts as a queue
        jobWaitList = setUpAtTimeZero(copyList,jobWaitList);// gets the jobs that arrive at clock time 0
        while (clock <= maxTime) { //run the clock to max time
            clock =  runJob(copyList,jobWaitList,matrix, clock); // run the job
        }
        graphOutPut(matrix); // output the final graph

    }
}
