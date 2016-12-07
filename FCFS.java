import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by Alan Padilla on 11/21/2016.
 * FCFS - First Come First Serve
 */
public class FCFS extends scheduler{
    public job max(ArrayList<job> copyList){
        job chosen = null; // job to send
        int max = 10000; // max to hold the arrival closest to start
        int index = 0;
        for(int i = 0; i < copyList.size(); i++){ // checks the whole list
            if(max >= copyList.get(i).getArrivalTime()){ // if the max is greater than or equal to arrival time
                index = i; // save index to remove later
                max = copyList.get(i).getArrivalTime(); // arrival time is the new max to compare
                chosen = copyList.get(i); // chosen holds copy of job
            }
        }
        copyList.remove(index); // remove the job from the copy of list
        return chosen; // send the next job
    }
    public void schedule(ArrayList<job> list){
        setScheduler("First Come First Serve",false); // initialize abstract class
        System.out.println(getName()); // print name of the scheduler algorithm
        ArrayList<job> copyList = copy(list); // create a copy of the list
       // ArrayList<job> copyList = list; // create a copy of the list
        job copy; // job to run
        int time = 0; // time holds the spaces to print 0 for the first one
        while(!(copyList.isEmpty())){ // while the list is not empty
            copy = max(copyList); //gets the next job
            copy.setSpaceCount(time); // set the spaces to current time count
            copy.runCompletion();// run until completions
            time += copy.getDuration(); // get the number of spaces to run for next one

        }

    }
}
