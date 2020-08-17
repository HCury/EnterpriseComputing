/* Name:Henrique Cury
Course: CNT 4714 Spring 2020 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  February 9, 2020
Class:  Enterprise Computing*/

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

class conveyer {
    private int convNum;
    public ReentrantLock loc = new ReentrantLock();
    private boolean work = false;

    public conveyer(int convNum) {
        this.convNum = convNum;
    }

    public void inConnection(int stationNum) {
        System.out.println("Station " + stationNum + ": successfully moves packages on conveyer " + this.convNum);
    }

    public void outConnection(int stationNum) {
        System.out.println("Station " + stationNum + ": successfully moves packages on conveyer " + this.convNum);
    }

}

public class station implements Runnable {

    private static int numStations;
    private int workload = 0;
    private int inConveyer;
    private int outConveyer;
    private int stationNum;
    private conveyer input;
    private conveyer output;
    static int numOfStations;
    static int[] workLoads; //each index represents a station
    static conveyer[] conveyers;
    static station[] stations;


    public station(int workload, int stationNum, int numStations)
    {
        this.stationNum = stationNum;
        this.numStations = numStations;
        this.workload = workload;
        this.setInConveyer();
        this.setOutConveyer();

        System.out.println("Station " + stationNum + ": Workload set. Station " + this.stationNum + " has " + this.workload + " package groups to move" );
    }


    @Override
    public void run() {
        while(this.workload != 0)
        {
            if(input.loc.tryLock())
            {
                System.out.println("station " + this.stationNum + ":granted access to conveyer " + this.inConveyer);

                if(output.loc.tryLock())
                {
                    System.out.println("Station " + this.stationNum + ":granted access to conveyer " + this.outConveyer);
                    doWork();
                } else {
                    System.out.println("Station " + this.stationNum + ": released access to conveyer " + this.inConveyer);
                    input.loc.unlock();

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException i) {
                        i.printStackTrace();
                    }
                }

                if(input.loc.isHeldByCurrentThread()) {
                    System.out.println("Station " + this.stationNum + ": released access to conveyer " + this.inConveyer);
                    input.loc.unlock();
                }

                if(output.loc.isHeldByCurrentThread()) {
                    System.out.println("Station " + this.stationNum + ": released access to conveyer " + this.outConveyer);
                    output.loc.unlock();
                }


                try {
                    Thread.sleep(1500);
                } catch ( InterruptedException i) {
                    i.printStackTrace();
                }
            }
        }

        System.out.println();
        System.out.println("* * Station " + stationNum + ": Workload successfully completed. * *\n");
    }

    public void doWork() {
        this.input.inConnection(this.stationNum);
        this.output.outConnection(this.stationNum);
        this.workload--;
        System.out.println("Station " + this.stationNum + ": has" + this.workload + " package groups left to move.");
    }

    //getters and setters

    public conveyer getInput(){
        return input;
    }

    public void setIn(conveyer in) {
        this.input = in;
    }

    public conveyer getOutput(){
        return output;
    }

    public void setOut(conveyer out) {
        this.output = out;
    }

    public int getInConveyer(){
        return inConveyer;
    }

    public void setInConveyer (){
        if(stationNum == 0) {
            this.inConveyer = 0;
        }else {
            this.inConveyer = this.stationNum - 1;
        }
        System.out.println("station " + stationNum + ": In-Connection set to conveyer "+ this.inConveyer);
    }

    public int getOutConveyer(){
        return outConveyer;
    }

    public void setOutConveyer(){
        if(this.stationNum == 0) {
            this.outConveyer = this.numStations - 1;
        } else {
            this.outConveyer = this.stationNum;
        }
        System.out.println("Station " + stationNum + ": Out-Connection set to conveyer " + this.outConveyer);
    }

    public static void main(String[] args) throws FileNotFoundException
    {

        File file = new File("config.txt");
        Scanner read = new Scanner(file);

        numOfStations = read.nextInt();
        workLoads = new int[numOfStations];
        conveyers = new conveyer[numOfStations];

        for(int i = 0; i < conveyers.length; i++)
            conveyers[i] = new conveyer(i);

        stations = new station[numOfStations];

        //create a thread pool with numOfStaions threads
        ExecutorService ex = Executors.newFixedThreadPool(numOfStations);

        for(int i = 0; i < workLoads.length && i < conveyers.length && i < stations.length; i++)
        {
            //set workload
            workLoads[i] = read.nextInt();
            //create instance of stations
            stations[i] = new station(workLoads[i], i, numOfStations);
            //set input and output conveyers
            stations[i].setIn(conveyers[stations[i].getInConveyer()]);
            stations[i].setOut(conveyers[stations[i].getOutConveyer()]);

            //start up the stations
            try
            {
                ex.execute(stations[i]);
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
            }

        }

        //end threadpool
        ex.shutdown();


    }

}

