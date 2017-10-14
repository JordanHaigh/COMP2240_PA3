public class Page
{
    private int id;
    private static Instruction instruction = new Instruction();
    private int currentTime = 0;

    public Page(int id)
    {
        this.id = id;
    }



    public void updateCurrentTime()
    {
        currentTime += instruction.INSTRUCTION_TIME;
         //Observer pattern, update CPU time and possible Main time
    }




}
