package Algorithms;

import Machine.Memory;
import Model.Frame;
import Model.Page;
import Model.SchedulingProcess;

import java.util.List;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ClockPolicy.java is one of the page replacement algorithms.
 * Algorithm utilises a clock header to track the last position where a page was manipulated
 */
public class ClockPolicy implements IPageReplacementAlgorithm
{
    private Memory memory;


    /**
     * public ClockPolicy(Memory memory)
     * @param memory - Memory Object to connect Clock Policy
     */
    public ClockPolicy(Memory memory) {this.memory = memory; }

    private int currentClockIndex;
    private SchedulingProcess currentProcess;
    private List<Frame> loadedProcessPages;

    /**
     * public int getReplacementIndex(Page pageToInsert)
     * Determines if there are empty positions or if the program needs to remove a running page
     * @param pageToInsert - Page to insert into memory
     * @return - Integer index to add page into memory
     */
    @Override
    public int getReplacementIndex(Page pageToInsert)
    {
        Page[] frames = memory.getFrames();
        SchedulingProcess parentProcess = pageToInsert.getParentProcess();

        currentProcess = parentProcess;
        currentClockIndex = parentProcess.getClockIndex();
        loadedProcessPages = memory.findAllPagesInMemory(parentProcess);


        ////////////////SCENARIO 1 - PAGE ALREADY RUNNING////////////////////
        //Scenario already catered for, as the AddToMemory method is called after it is confirmed the page is not in memory
        //
        //for(int i = 0; i < memory.getCountOfAllPagesRunning(); i++)
        // {
        //    //Check if they have the same parent process. Find out if they both have the same page number
        //    if(frames[i].getPageNumber() == pageToInsert.getPageNumber() && frames[i].getParentProcess() == parentProcess)
        //    {
        //        // If page is already in memory, set the use bit to 1 and return. You do not need to replace this page
        //       frames[i].setUseBit(true);
        //
        //        // return -1 to signal that a page replacement is not required
        //        return -1;
        //    }
        //}

        ///////////SCENARIO 2 - EMPTY FRAME FOR PAGE ENTRY/////////////////////
        if(!memory.processHasReachedMaxAllocation(parentProcess))
        {
            //Find the next empty index in the frames
            int foundEmptyIndex = memory.findNextEmptyIndex(); //Will never return -1 due to the if condition

            // if there is no page at this index, we can insert the page here (and move the clock head forward after)
            moveClockIndex();
            return foundEmptyIndex;
        }

        //////////SCENARIO 3 - NO EMPTY SPACES, NOT IN MEMORY///////////////
        int foundIndex = -1;

        while(foundIndex == -1)
        {
            for(int i = currentClockIndex; i < loadedProcessPages.size(); i++)
            {
                if(loadedProcessPages.get(i).getPage().useBitIsTrue())
                {
                    loadedProcessPages.get(i).getPage().setUseBit(false);
                    moveClockIndex();
                }
                else
                {
                    // if it is 0, we replace it at this index (and move clock head movement after)
                    foundIndex = loadedProcessPages.get(i).getIndex();
                    moveClockIndex();
                    break;
                }
            }

        }

        return foundIndex;
    }

    /**
     * private void moveClockIndex()
     * Moves the clock index by 1.
     * If the clock has reached the end of the frames, it will restart at position 0.
     */
    private void moveClockIndex()
    {
        if(currentClockIndex == memory.getFixedAllocationNumber()-1)
        {
            this.currentClockIndex = 0;
            currentProcess.resetClockIndex();
        }
        else
        {
            this.currentClockIndex++;
            currentProcess.incrementClockIndex();
        }
    }

    /**
     * public String toString()
     * @return - Clock Name String
     */
    @Override
    public String toString() {
        return "Clock - Fixed";
    }


}