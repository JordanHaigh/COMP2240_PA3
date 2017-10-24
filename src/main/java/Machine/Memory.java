package Machine;

import Algorithms.IPageReplacementAlgorithm;
import Model.*;
import ObserverPattern.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * Memory.java is used to hold a certain number of frames that store Pages from the processes
 * Each process has a certain fixed allocation number for the number of frames
 */
public class Memory implements ISubscriber
{
    private int maxFrames;
    private int fixedAllocationNumber;
    private IPageReplacementAlgorithm pageReplacementAlgorithm;
    private int size;
    private Page[] frames;


    /**
     * public Memory(int numberOfProcesses)
     * Overloaded Constructor
     * @param numberOfProcesses - Number of processes fed in
     */
    public Memory(int numberOfProcesses)
    {
        maxFrames = 30;
        calculateFixedAllocationNumber(numberOfProcesses);
        size = 0;
        frames = new Page[maxFrames];
    }

    /**
     * public Memory(int numberProcesses, int maxFrames)
     * Overloaded constructor
     * @param numberProcesses - Number of processes found in argument list
     * @param maxFrames - Max number of frames specified by user
     */
    public Memory(int numberProcesses, int maxFrames)
    {
        this.maxFrames = maxFrames;
        calculateFixedAllocationNumber(numberProcesses);
        size = 0;
        frames = new Page[maxFrames];
    }


    /**
     * private void calculateFixedAllocationNumber(int numberOfProcesses)
     * @param numberOfProcesses - Number of processes found in argument list
     */
    private void calculateFixedAllocationNumber(int numberOfProcesses) { fixedAllocationNumber = (int)maxFrames/numberOfProcesses; }

    /**
     * public int getFixedAllocationNumber()
     * @return - Integer containing fixedAllocationNumber
     */
    public int getFixedAllocationNumber(){return fixedAllocationNumber; }

    /**
     * public boolean isFrameOccupied(int index)
     * Determines whether specified frame is occupied
     * @param index - Index to check
     * @return - True or false depending if the index contains an element
     */
    public boolean isFrameOccupied(int index)  { return frames[index] != null; }

    /**
     * public boolean processHasReachedMaxAllocation(SchedulingProcess process)
     * Determines whether the process has n number of pages running in memory determined by the fixed allocation number
     * @param process - Process to check
     * @return - True or false value
     */
    public boolean processHasReachedMaxAllocation(SchedulingProcess process) { return process.countDistinctProcessesRunning() >= fixedAllocationNumber; }

    /**
     * public void setPageReplacementAlgorithm
     * @param pageReplacementAlgorithm - Page Algorithm to set to the Memory module
     */
    public void setPageReplacementAlgorithm(IPageReplacementAlgorithm pageReplacementAlgorithm) { this.pageReplacementAlgorithm = pageReplacementAlgorithm; }

    /**
     * public int getFrames()
     * @return - Integer determining the max number of frames
     */
    public int getMaxFrames() {return maxFrames; }

    /**
     * public Page[] getFrames()
     * @return - Page Array determining all pages currently running
     */
    public Page[] getFrames(){ return frames; }

    /**
     * public void clear()
     * Resets all information currently inside the Memory Class to first instantiation
     */
    public void clear()
    {
        pageReplacementAlgorithm = null;

        size = 0;
        Arrays.fill(frames, null);
    }

    /**
     * public int getCountOfAllPagesRunning()
     * Determines the current number of pages running in memory
     * @return - Integer of number of frames running
     */
    public int getCountOfAllPagesRunning()
    {
        int count = 0;

        for(int i = 0; i < maxFrames; i++)
        {
            if(frames[i] != null)
                count++;
        }
        return count;
    }

    /**
     * public List<Frame> findAllPagesInMemory(SchedulingProcess parentProcess)
     * Finds all pages that belong to the parent process that are currently running in memory
     * @param parentProcess - Process used to check what pages are running in memory and belong to this process
     * @return - List of Frames(Page and its index) of running pages
     */
    public List<Frame> findAllPagesInMemory(SchedulingProcess parentProcess)
    {
        List<Frame> pagesBelongingToParentProcess = new ArrayList<>();
        for(int i = 0; i < maxFrames; i++)
        {
            if(frames[i] != null && frames[i].getParentProcess() == parentProcess)
                pagesBelongingToParentProcess.add(new Frame(frames[i], i));

        }
        return pagesBelongingToParentProcess;
    }

    /**
     * public int findNextEmptyIndex()
     * Finds the next empty index in frames
     * @return - Integer value containing next available index
     */
    public int findNextEmptyIndex()
    {
        for(int i = 0; i < maxFrames; i++)
        {
            if(frames[i] == null)
                return i;
        }
        //If no empty indexes
        return -1;
    }


    /**
     * public void addToMemory(Page pageToInsert, int currentTime)
     * Adds the page into memory at this current time
     * Utilises the page replacement algorithm to determine the replacement index
     * Unblocks the parent process if it is blocked
     * @param pageToInsert - Page to insert into memory
     * @param currentTime - Current time page is to be inserted
     */
    public void addToMemory(Page pageToInsert, int currentTime)
    {
        int index = pageReplacementAlgorithm.getReplacementIndex(pageToInsert);

        if(isFrameOccupied(index))
            unloadPageAtIndex(index, currentTime);

        loadPageAtIndex(pageToInsert, index, currentTime);
        pageToInsert.setTimeLastUsed(currentTime);


        SchedulingProcess parentProcess = pageToInsert.getParentProcess();

        if(parentProcess.isBlocked())
            parentProcess.unblock(currentTime);

    }

    /**
     * private void unloadPageAtIndex(int index, int currentTime)
     * @param index - Index to unload page
     * @param currentTime - Current time the unload occurs
     */
    private void unloadPageAtIndex(int index, int currentTime)
    {
        if(index < 0 || index > maxFrames)
            throw new IllegalArgumentException("Index used to unload page is out of bounds");

        frames[index].setLoadedInMemory(false);
        frames[index].setUseBit(false);
        frames[index] = null;
        size--;
    }

    /**
     * private void loadPageAtIndex(Page page, int index, int currentTime)
     * @param page - Page to load at index
     * @param index - Index to load page into
     * @param currentTime - Current time the load occurs
     */
    private void loadPageAtIndex(Page page, int index, int currentTime)
    {
        if(index < 0 || index > maxFrames)
            throw new IllegalArgumentException("Index used to unload page is out of bounds");

        page.setLoadedInMemory(true);
        page.setUseBit(true);
        frames[index] = page;
        size++;
    }

    /**
     * public void handleMessage(ObservableMessage message)
     * Utilises the Observer Pattern
     * Handles message broadcasted by another class
     * @param message - ObservableMessage that contains relevant time information
     */
    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservablePageReadyMessage)
        {
            Page page = ((ObservablePageReadyMessage) message).getPage();
            int currentTime = ((ObservablePageReadyMessage) message).getCurrentTime();

            addToMemory(page, currentTime);
            //System.out.println("Time  " + currentTime + ": " + page.getParentProcess().toString() + ": PAGE ("+page.getPageNumber()+")LOADED IN MEMORY");

        }
    }
}