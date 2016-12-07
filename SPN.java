import java.util.ArrayList;

/**
 * Created by Alan Padilla on 11/21/2016.
 * SPN - shortest process next
 */
public class SPN extends scheduler{
    private void setup(ArrayList<job> list){
        int time = 0; // clock holds the time
        job nxtJob; // holds job gives an array of int that correlates to the amount of spaces needed to print before each job starts
        ArrayList<job> copyList = copy(list); // create copy of list
        int index; // holds index
        while(!(copyList.isEmpty())){
            nxtJob = min(copyList,time); // get the next job to run
            index = indexOfJob(list,nxtJob); // get index of job from original list
            list.get(index).setSpaceCount(time); // add the space count of that time
            time += nxtJob.getDuration(); // update the clock
        }
    }

    private job min(ArrayList<job> copyList, int clock){
        job chosen = null; // job that will be ran
        int index = 0; // index used to remove the job from copy of list
        int min = 1000; // holds the min value
        for(int i = 0; i < copyList.size(); i++){ // goes through all list
            if(copyList.get(i).getArrivalTime() <= clock){ // if they arrived before or at the current clock time
               if(copyList.get(i).getDuration() <= min){ // if its duration less then the current smallest duration
                   min = copyList.get(i).getDuration(); // update min
                   index = i; // save index
                   chosen = copyList.get(i); // copy the job
                   chosen.setSpaceCount(clock); // set the space count to current clock
               }
            }
        }
        copyList.remove(index); // remove the job from the list
        return chosen; // return chosen
    }
    public void schedule(ArrayList<job> list){
        setScheduler("Shortest Process Next",false);
        System.out.println(getName());// prints name
        ArrayList<job> copyList = copy(list); // create a copy of the job list
        setup(copyList); // call setup which selects which jobs to run
        for(job j: copyList){ // for each loop runs through list of jobs
            j.runCompletion(); // runs the job until completion
        }

    }
}
