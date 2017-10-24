package Model;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * Frame.java holds a Page and an Index which references a location in the Frames
 */
public class Frame
{
    private Page page; //Page loaded in memory
    private int index; //index where page is in memory

    /**
     * public Frame(Page page, int index)
     * Overloaded Constructor
     * @param page - Page loaded in memory
     * @param index - Index where page is in memory
     */
    public Frame(Page page, int index)
    {
        this.page = page;
        this.index = index;
    }

    /**
     * public Page getPage()
     * @return - Page located in memory
     */
    public Page getPage() {
        return page;
    }

    /**
     * public int getIndex()
     * @return - Index where page is in memory
     */
    public int getIndex() {
        return index;
    }
}
