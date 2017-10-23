package Algorithms;

import Machine.Memory;
import Model.*;

import java.util.List;

public class LRU implements IPageReplacementAlgorithm {
    Memory memory;

    public LRU(Memory memory) {
        this.memory = memory;
    }

    @Override
    public int getReplacementIndex(Page pageToInsert) {
        //Need to find out who the page belongs to
        SchedulingProcess parentProcess = pageToInsert.getParentProcess();

        //Need to find out what pages are currently loaded
        //Look through the memory module and pick out pages that belong to the process
        //Add that to a separate list, making note of their indexes (for replacement)
        List<Frame> loadedProcessPages = memory.findAllPagesInMemory(parentProcess);


        //////////////////SCENARIO 1 - PAGE ALREADY RUNNING////////////////////
        //Scenario already catered for, as the AddToMemory method is called after it is confirmed the page is not in memory
        //Cater for scenario where page may already be running
        //for(Model.Frame frame: loadedProcessPages)
        //{
        //    //We already know they have the same parent process
        //    // Find out if they both have the same page number
        //    if(frame.getPage().getPageNumber() == pageToInsert.getPageNumber())
        //    {
        //        //This page number is already running in memory
        //        //Then we don't need to replace this page and issue a page fault
        //        return -1; //Sentinel value to determine if we replace or not
        //    }
        //}

        //////////////SCENARIO 2 - EMPTY FRAMES FOR PAGE ENTRY/////////////////////

        //Cater for the scenario that frames are empty
        if (loadedProcessPages.size() < memory.getFixedAllocationNumber()) {
            //Find the next empty index in the frames
            return memory.findNextEmptyIndex(); //Will never return -1 due to the if condition
        }

        ////////////SCENARIO 3 - NO EMPTY SPACES, NOT IN MEMORY///////////////

        //Need to find the least recently used page in that is loaded in the memory module
        //page needs to know the last time it was used

        Frame leastRecentlyUsedPage = loadedProcessPages.get(0); //Initialise first

        //look through the list of pages loaded in memory and keep track of what their last time used is
        //then determine what the lowest time value is of all pages - this will be the page to be replaced.

        for (Frame frame : loadedProcessPages) {
            if (frame.getPage().getTimeLastUsed() < leastRecentlyUsedPage.getPage().getTimeLastUsed())
                leastRecentlyUsedPage = frame;
        }


        return leastRecentlyUsedPage.getIndex();


    }

    @Override
    public String toString() {
        return "LRU - Fixed";
    }
}
