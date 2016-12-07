import java.io.*;
import java.util.*;

/**
 * Created by Alan Padilla on 11/21/2016.
 *
 */
public class OS {
    public static void main(String arg[]){
        ArrayList<job> jobList = new ArrayList<>(); // arraylist for jobs
        setup(jobList);// sets up the job list
        menu(jobList); // run the menu
    }
    public static void setup(ArrayList<job> list){
        String file = "jobs.txt"; // file name
        String job; // job holds the line from file
        try{
            FileReader readFile = new FileReader(file); // file reader method to open file
            BufferedReader readBuffer = new BufferedReader(readFile); // buffer reader to read file

            while((job = readBuffer.readLine()) != null){// reads the file until it ends
                String[] parsedLine = job.split("\t",3); //parses the line into 3 strings
                list.add(new job(parsedLine[0],// name
                             Integer.parseInt(parsedLine[1]), // arrival time
                             Integer.parseInt(parsedLine[2]))); // duration time

            }

        }catch (FileNotFoundException ex){ // catches if file is not fount
            System.out.println("Unable to open file '" + file + "'");
        }
        catch (IOException e){ // catches if IO error
            System.out.println("Error reading file '" + file + "'");
        }
    }

    public static void menu(ArrayList<job> list){
        scheduler[] schedulers = {new FCFS(),//0 =FCFS
                                  new RR(), // 1 = ROUND ROBIN
                                  new SPN(),// 2 = SHORTEST PROCESS NEXT
                                  new SRT(),// 3 = SHORTEST REMAINING TIME
                                  new HRRN(),//4 = HRRN
                                  new Feedback()};// 5 = Feedback

        Scanner input = new Scanner(System.in); // scanner used for input
        String choice = ""; // holds user input
        while(!(choice.equals("q"))) { // runs this until user quits
            System.out.println("OS Scheduling Algorithms");
            System.out.println("Main Menu");
            System.out.println("Enter the letter to choose an algorithm:");
            System.out.println("\t\ta - FCFS");
            System.out.println("\t\tb - RR");
            System.out.println("\t\tc - SPN");
            System.out.println("\t\td - SRT");
            System.out.println("\t\te - HRRN");
            System.out.println("\t\tf - Feedback");
            System.out.println("\t\tq - Quit");
            choice = input.nextLine();
            switch (choice){
                case "a":
                        schedulers[0].schedule(list);//FCFS
                    break;
                case "b":
                        schedulers[1].schedule(list);//RR
                    break;
                case "c":
                        schedulers[2].schedule(list);//SPN
                    break;
                case "d":
                        schedulers[3].schedule(list);//SRT
                    break;
                case "e":
                        schedulers[4].schedule(list);//HRRN
                    break;
                case "f":
                        schedulers[5].schedule(list);//Feedback
                    break;
                case "q":
                    System.exit(0); //exit program
                    break;
            }
        }

    }
}
