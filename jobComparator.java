import java.util.Comparator;

/**
 * Created by Alan Padilla Chua on 11/21/2016.
 * Job Comparator is a class that is used when
 * Collections.sort is called to sort the job arraylist
 */
public class jobComparator implements Comparator<job> { // implements comparator so it can be sorted easier
    @Override
    public int compare(job job1, job job2) {
        int job1Arrival = job1.getArrivalTime();
        int job2Arrival = job2.getArrivalTime();

        if (job1Arrival > job2Arrival) { // if 1st job arrived before 2nd job then
            return 1; // return 1
        } else if (job1Arrival < job2Arrival) { // if the 2nd job arrived before the first job then return -1
            return -1;
        } else {
            return 0; // if they both arrived at the same time then return 0
        }
    }
}
