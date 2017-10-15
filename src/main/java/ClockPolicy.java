import java.util.List;

public class ClockPolicy implements IPageReplacementAlgorithm
{
    private Memory memory;


    public ClockPolicy(Memory memory) {this.memory = memory; }


    @Override
    public int getReplacementIndex(Page pageToInsert)
    {
        //need to find out who the page to insert belongs to
        Process parentProcess = pageToInsert.getProcess();
        //Need to find out all pages that belong to this process are running in memory at the moment
        List<Frame> loadedProcessPages = memory.findAllPagesInMemory(parentProcess);

        int replacementIndex = -1;

        //Need to determine what index holds the oldest page
        int indexForOldestPage = -1;
        int oldestPageTime = Integer.MAX_VALUE;
        //todo maybe double check how this code works when there are no pages running on memory - whether the index will be -1 or something

        //Find the oldest time and its index
        for(Frame frame : loadedProcessPages)
            if(frame.getPage().getTimeLastUsed() < oldestPageTime)
            {
                indexForOldestPage = loadedProcessPages.indexOf(frame);
                oldestPageTime = frame.getPage().getTimeLastUsed();
            }

        boolean foundReplacementIndex = false;

        for(int i = indexForOldestPage; i < loadedProcessPages.size(); i++)
        {
            if(loadedProcessPages.get(i).getPage().useBitIsTrue())
               loadedProcessPages.get(i).getPage().setUseBit(false);
            else
            {
                //Found a bit that is set to false
                //This will be the index to be replaced
                foundReplacementIndex = true;
                replacementIndex =  loadedProcessPages.get(i).getIndex();
            }
        }

        //If we were unable to find a replacement index, restart for loop till we find the next replacement
        if(!foundReplacementIndex)
        {
            for(int i = 0; i < loadedProcessPages.size(); i++)
            {
                if(loadedProcessPages.get(i).getPage().useBitIsTrue())
                    loadedProcessPages.get(i).getPage().setUseBit(false);
                else
                {
                    //Found a bit that is set to false
                    //This will be the index to be replaced
                    replacementIndex =  loadedProcessPages.get(i).getIndex();
                }
            }
        }

        return replacementIndex;
    }

    @Override
    public void replacePage(Page pageToInsert, int replacementIndex)
    {
        memory.getFrames()[replacementIndex] = pageToInsert;
        pageToInsert.setUseBit(true);
        //todo page fault
    }

}
