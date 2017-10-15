import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LRUTests {
    @Test
    public void getReplacementIndex() throws Exception
    {
        List<Page> pageList = new ArrayList<Page>();
        int arr[] = {2,3,2,1,5,2,4,5,3,2,5,2};
        for(int i = 0; i < arr.length;i++)
        {
            Page page = new Page(arr[i]);
            pageList.add(page);

        }
        Process process = new Process(1, pageList);

        for(Page page: pageList)
            page.linkProcessToPage(process);

        Memory memory = new Memory(1);
        IPageReplacementAlgorithm clock = new LRU(memory);
        memory.setPageReplacementAlgorithm(clock);


        for(Page page: pageList)
        {
            memory.addToMemory(page);
        }

        Assert.assertEquals(3, memory.getFrames()[0].getPageNumber());
        Assert.assertEquals(2, memory.getFrames()[1].getPageNumber());
        Assert.assertEquals(5, memory.getFrames()[2].getPageNumber());



    }

}