import java.util.ArrayList;
import java.util.*;
/**
 * Created by Alan Padilla on 11/21/2016.
 *
 */
public abstract class scheduler {
    private String name; //name of scheduler
    private boolean preemptive; // for checking if scheduler is preemptive
    public void setName(String n){
        name = n;
    }
    public void setPreemptive(Boolean set){
        preemptive = set;
    }
    public String getName(){
        return name;
    }
    public ArrayList<job> copy(ArrayList<job> old){ //copy an array list of jobs
        ArrayList<job> newList = new ArrayList<>(); // new array list
        for(job o: old){ // for each job in old list
            newList.add(new job(o));// create same job in new list
        }
        return newList; // return new list
    }
    public ArrayList<job> setUpAtTimeZero(ArrayList<job> copyList,ArrayList<job> jobWaitList){ //
        for(job j: copyList){ // for each job in copy list
            if(j.getArrivalTime() == 0){ // if it arrived at time 0
                jobWaitList.add(new job(j)); // add it to the waitlist
            }
        }
        return jobWaitList;
    }
    public int setUpAtTimeZero(ArrayList<job> copyList,int time, Queue<job> ready){ // set ups matrix by getting all jobs that arrive at time zero
        ArrayList<job> arrivalList = setupWaitList(copyList,time); // get the jobs that arrived at or before current time
        time++; // update time to 1 now so when it runs it starts at 1
        for(job j: arrivalList){ // for each job in the list
            ready.add(j); // add the job to queue
            int indexOfCopy = indexOfJob(copyList,j); // get the index of the job from the list in order to remove it
            copyList.remove(indexOfCopy); // remove job from the main list
        }
        return time; // return 1
    }
    public int indexOfJob(ArrayList<job> list , job oldJob){
        int index = 0; // index to send
        for(int i = 0; i < list.size(); i++){ // goes through the whole list
            if(list.get(i).getName().equals(oldJob.getName())){// if the names are the same then its at the correct index
                index = i; // save index
            }
        }
        return index; // return index
    }
    public boolean getPreemptive(){
        return preemptive;
    }
    //setRowNums should only be called on the first list not the copylist
    //setRowNums also sorts the list which helps reduces errors
    public void setRowNums(ArrayList<job> list){
        Collections.sort(list,new jobComparator()); // sort the list
        int maxAT = 0; // start max at time 0
        int rowNum = 0; // start the row count at 0
        for(job j: list){ // for each job in list
            if(j.getArrivalTime() >= maxAT){ // if its arrival time greater or equal to maxArrivalTime
                j.setRow(rowNum); // set the row num
                rowNum++; // increase row number
                maxAT = j.getArrivalTime(); // new max arrival time is equal to arrival time
            }
        }
    }
    public boolean isJobComplete(job currentJob){ // checks if the current job is complete
        return (currentJob.getRunAmount() == currentJob.getDuration());
    }
    public boolean isJobinList(ArrayList<job> copyList,job current){
        boolean yes = false; // yes set to false
        for(job j: copyList){ // for each job in list
            if(j.getName().equals(current.getName())){ // if the job is in the list
                yes = true; // then it is in the list
            }
        }
        return yes; // return that it is
    }
    public ArrayList<job> setupWaitList(ArrayList<job> copyList, int clock){
        ArrayList<job> jobWaitList = new ArrayList<>(); // temporary list
        if(getPreemptive()){ // the scheduler is preemptive then use this algorithm
            for(job currentJob: copyList){//for each job in list
                if(currentJob.getArrivalTime() <= clock){ // if currentJob arrived at the current time then add it to the list
                    jobWaitList.add(new job(currentJob));// add it to another array list that holds all process at current time
                }
            }
        }
        else if(!(getPreemptive())){ // if its not preemptive then use this algorithm
            for(job currentJob: copyList){//for each job in list
                if(currentJob.getArrivalTime() <= clock){ // if currentJob arrival time less than or equal to clock
                    jobWaitList.add(new job(currentJob));// add it to another array list that holds all process at current time
                }
            }
        }
        return jobWaitList; // return the list
    }

    /*
    * acts as a overloaded constructor
    * */
    public void setScheduler(String name, Boolean set){
        setName(name);
        setPreemptive(set);
    }
    public int maxLength(ArrayList<job>list){
        int length = 0; // start count at 0
        for(job j: list){ // for each job in the list
            length += j.getDuration(); // get the duration and add it to the length
        }
        return length; // return length
    }

    public String[][] createMatrix(ArrayList<job> list){ //method creates output matrix
        int rows = list.size(); // rows holds the number of rows in output matrix
        int columns = maxLength(list)+1; // holds the length of each matrix
        String[][] output = new String[rows][columns]; // output is the matrix
        for(int row = 0; row < output.length; row++){ // travers each row
            for(int col = 0; col < output[row].length; col++){ // traverse each column in row
                output[row][col] = " "; // initialize it to a space
            }
        }
        return output; // return the matrix
    }
    public void graphOutPut(String[][] matrix){ // method used to out put matrix
        for(int row = 0; row < matrix.length; row++){ // travers each row
            for(int col = 0; col < matrix[row].length; col++){ // traverse each column in row
                System.out.print(matrix[row][col]); // print to screen
            }
            System.out.println(); // print new line
        }
    }
    public abstract void schedule(ArrayList<job> list); //abstract method that all derived classes must implement
}
