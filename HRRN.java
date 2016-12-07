import java.util.ArrayList;

/**
 * Created by Alan Padilla on 11/21/2016.
 * HRRN - Highest response ratio next
 */
public class HRRN extends scheduler {
    private void setup(ArrayList<job> list){
        ArrayList<job> copyList = copy(list);// copy of list
        int time = 0; // set time to start at 0
        while(!(copyList.isEmpty())){ // while copy list isnt empty
            job chosen = max(copyList,time); // get the job to run next
            int index = indexOfJob(list,chosen); // get index of the job in main list
            list.get(index).setSpaceCount(time); // set up the delay count for the job
            time += chosen.getDuration(); // increase the time
        }
    }
    private job max(ArrayList<job> copyList, int clock){
        double MinR = 0.0; // r holds ratio
        job chosen = null; // job that is returned to run
        ArrayList<job> currentList = setupWaitList(copyList,clock); // get a list of jobs that are waiting to run
        int waitTime; // int to hold the wait time
        int serviceTime; // int to hold service time
        for(job currJob: currentList){
            waitTime = clock - currJob.getArrivalTime();// wait time is equal to clock - arrival time
            serviceTime = currJob.getDuration(); // service time is equal to duration
            double R = (waitTime + serviceTime)/serviceTime; // R uses the main HRRN algorithm for ratio
            if(R >= MinR){
                chosen = new job(currJob); // chosen copies the current job
                MinR = R; // set the new MinR
            }
        }
        int index = indexOfJob(copyList,chosen); // get the index of job
        copyList.remove(index); // remove that job from the main list
        return chosen; // return the copy
    }
    public void schedule(ArrayList<job> list){
        setScheduler("Highest Response Ratio Next",false);
        System.out.println(getName()); // prints name
        ArrayList<job> copyList = copy(list); // create a copy of the job list
        setup(copyList); // call setup which selects which jobs to run

        for(job j: copyList){ // for each loop runs through list of jobs
            j.runCompletion(); // runs the job until completion
        }
    }
}
