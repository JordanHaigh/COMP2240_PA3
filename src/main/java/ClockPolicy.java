import com.sun.org.apache.xpath.internal.FoundIndex;

import java.util.List;

public class ClockPolicy implements IPageReplacementAlgorithm
{
    private Memory memory;


    public ClockPolicy(Memory memory) {this.memory = memory; }

    private int currentClockIndex = 0;


    @Override
    public int getReplacementIndex(Page pageToInsert)
    {
        Page[] frames = memory.getFrames();
        Process parentProcess = pageToInsert.getParentProcess();


        ////////////////SCENARIO 1 - PAGE ALREADY RUNNING////////////////////

        //Cater for scenario where page may already be running
        for(int i = 0; i < memory.getFramesSize(); i++)
        {
            //Check if they have the same parent process. Find out if they both have the same page number
            if(frames[i].getPageNumber() == pageToInsert.getPageNumber() && frames[i].getParentProcess() == parentProcess)
            {
                // If page is already in memory, set the use bit to 1 and return. You do not need to replace this page
                frames[i].setUseBit(true);

                // return -1 to signal that a page replacement is not required
                return -1;
            }
        }

        ///////////SCENARIO 2 - EMPTY FRAME FOR PAGE ENTRY/////////////////////
        if(parentProcess.getCurrentNumberPagesRunning() < memory.getFixedAllocationNumber())
        {
            //Find the next empty index in the frames
            int foundEmptyIndex = memory.findNextEmptyIndex(); //Will never return -1 due to the if condition

            // if there is no page at this index, we can insert the page here (and move the clock head forward after)
            moveClockIndex();
            return foundEmptyIndex;
            //todo check if code breaks here if foundEmptyIndex == -1
            //todo issue page fault, update current time it entered, etc.
        }

        //////////SCENARIO 3 - NO EMPTY SPACES, NOT IN MEMORY///////////////
        int foundIndex = -1;

        while(foundIndex == -1)
        {
            for(int i = currentClockIndex; i < memory.getFramesSize(); i++)
            {
                // Starting at the currentClockIndex, check if the page's use bit is set to 0
                if(!frames[i].useBitIsTrue() && frames[i].getParentProcess() == parentProcess)
                    // if it is 0, we replace it at this index (and move clock head movement after)
                    foundIndex = i;
                else
                {
                    // if it is 1, we set it to 0 and move the clock head forward
                    frames[i].setUseBit(false);
                    moveClockIndex();
                }
            }
        }

        return foundIndex;
    }

    private void moveClockIndex()
    {
        if(currentClockIndex == memory.getFramesSize())
            this.currentClockIndex = 0;
        else
            this.currentClockIndex++;
    }


}
