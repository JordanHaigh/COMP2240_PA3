package Model;

public class Frame
{
    private Page page; //Model.Page loaded in memory
    private int index; //index where page is in memory

    public Frame(Page page, int index)
    {
        this.page = page;
        this.index = index;
    }


    public Page getPage() {
        return page;
    }

    public int getIndex() {
        return index;
    }
}
