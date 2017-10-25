package ObserverPattern;

import Model.SchedulingProcess;

public class ObservableRemoveProcessReaddMessage extends ObservableMessage
{
    private SchedulingProcess process;

    public ObservableRemoveProcessReaddMessage(SchedulingProcess process)
    {
        this.process = process;
    }

    public SchedulingProcess getProcess() {
        return process;
    }
}
