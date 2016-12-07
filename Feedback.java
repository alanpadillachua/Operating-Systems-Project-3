import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Alan Padilla on 11/21/2016.
 * Feedback
 */
public class Feedback extends scheduler {
    private int quantum ; // quantum is
    private Queue<job> readyQueueZero = new LinkedList<>(); // ready queue where jobs wait to run when arrived
    private Queue<job> readyQueueOne = new LinkedList<>(); // ready queue after run first time
    private Queue<job> readyQueueTwo = new LinkedList<>(); // ready queue after run 2nd time runs until completion
    private boolean isQueuesEmpty(){ // returns true if all queues are empty
        return (readyQueueZero.isEmpty()&&readyQueueOne.isEmpty() && readyQueueTwo.isEmpty());
    }
    private void newJobArrived(ArrayList<job> copyList,int newTime , job oldJob ){ // checks if job is waiting
        ArrayList<job> readyList = new ArrayList<>();
        for(job j: copyList){
            if(j.getArrivalTime() == newTime-1){
                readyList.add(new job(j));
            }
        }
        if(readyList.isEmpty()){ // if no job arrived
            //updateQueueZero(copyList,newTime);
            if(newTime > 2){ // if its greater than time 2
                readyQueueOne.add(new job(oldJob)); // add it to queue 1
            }
            else { // if not then
                readyQueueZero.add(new job(oldJob)); // add old job to queue zero
            }
        }
        else{
            readyQueueOne.add(new job(oldJob)); // if the list is not empty then a job will arrive so add it to queue one
        }
    }
    private void updateQueueZero(ArrayList<job> copyList, int time){
        ArrayList<job> readyList = new ArrayList<>();
        for(job j: copyList){
            if(j.getArrivalTime() <= time){
                readyList.add(new job(j));
            }
        }
        for (job j: readyList){
            readyQueueZero.add(new job(j));
            int index = indexOfJob(copyList,j);
            copyList.remove(index);
        }
    }

    private int runJob(ArrayList<job> copyList, String[][] matrix, int time){
        int newTime = time; // new time holds the updated time that continues the loop
        job job2run; // job that holds the job that will run from the ready queue
        updateQueueZero(copyList,time);
        if(!isQueuesEmpty()){ //if the queues are not empty meaning there are still jobs to run
           if(!readyQueueZero.isEmpty()){// if job in zero queue run it
               job2run = new job(readyQueueZero.remove()); // job2run is gotten
               newTime = job2run.runSlice(quantum,time,matrix); // run the job
               //System.out.println(job2run.getName() + " RAN from QUEUE 0 at " + time + " NEW TIME = " + newTime);
               if(!isJobComplete(job2run)){ // if the job is not complete
                   newJobArrived(copyList,newTime,job2run);
                   //readyQueueOne.add(new job(job2run));
               }
           }
           else if(!readyQueueOne.isEmpty()){// else if no job in zero queue and jobs are in one queue run it
               job2run = new job(readyQueueOne.remove()); // get the job from queue 1
               newTime = job2run.runSlice(quantum,time,matrix); // run the job
               //System.out.println(job2run.getName() + " RAN from QUEUE 1 at " + time + " NEW TIME = " + newTime);
               if(!isJobComplete(job2run)){ // if the job is not complete
                   //System.out.println(job2run.getName() + " Added to queue 1 at time "+ time + " and new time " + newTime);
                   readyQueueTwo.add(new job(job2run)); // if job is not complete add it to the ready queue 2
               } // if not the job is deleted and lost
           }
           else if(!readyQueueTwo.isEmpty()){ // else if both higher queues are empty then run from ready queue 2
               job2run = new job(readyQueueTwo.remove()); // get the job from queue 2
               newTime = job2run.runSlice(quantum,time,matrix); // run the job
               //System.out.println(job2run.getName() + " RAN from QUEUE 2 at " + time + " NEW TIME = " + newTime);
               if(!isJobComplete(job2run)){ // if the job is not complete
                   //System.out.println(job2run.getName() + " Added to queue 2 at time "+ time + " and new time " + newTime);
                   readyQueueTwo.add(new job(job2run)); // if job is not complete add it to the ready queue 2 again
               } // if not the job is deleted and lost
           }
        }
        updateQueueZero(copyList,newTime);
        return newTime; // return updated time
    }
    private int setUpReadyQueueZero(ArrayList<job> copyList, int clock, Queue<job> readyQueueZero){
        int newTime = clock; // clock is zero but still copy
        for(job currentJob: copyList){//for each job in list
            if(currentJob.getArrivalTime() == newTime){ // if currentJob arrived at the current time plus 1 then add it to the list
               // System.out.println(currentJob.getName() + " SETUP METHOD added to zero queue at Time = " + newTime);
                readyQueueZero.add(new job(currentJob)); // add the job to ready queue zero
            }
        }
        for(job j: readyQueueZero){ // for each job in ready queue zero
            if(isJobinList(copyList,j)) { // checks if the job has not been removed yet
                int index = indexOfJob(copyList, j); // get index of the job
                copyList.remove(index); // remove the job from the list
            }
        }
        newTime++;// increase time to 1
        return newTime; // return the new time which is 1
    }

    public void schedule(ArrayList<job> list){
        int Queue_Amount = 3; // my feedback scheme will use 3 queues
        setScheduler("Feedback",true);
        quantum = 1; // set quantum
        System.out.println(getName() + ", quantum = " + quantum + ", queues = " + Queue_Amount); // print name
        int maxTime; // holds the max time
        int clock = 0; // clock
        setRowNums(list); // set the row numbers on list
        ArrayList<job> copyList = copy(list); // create copy of job list
        String[][] matrix = createMatrix(copyList); // create matrix for graph out put
        maxTime = maxLength(copyList);
        // no job should run at 0
        clock = setUpReadyQueueZero(copyList,clock,readyQueueZero);// gets the jobs that arrive at clock time 0
        /**
         * Because the output matrix prints with an offset of negative 1
         * and the queue system relies on a system of 3 queues that are dependent of the arrival of a certian time
         * the arrival time must be modified by increasing it to one
         * */
        for(job j: copyList){
            j.modifyArrivalTime(1); // modify the arrival time to
        }
        // clock now = 1*/
        while (clock <= maxTime) { //run the clock to max time
            clock =  runJob(copyList, matrix, clock); // run the job
        }
        graphOutPut(matrix); // output the final graph

    }
}
