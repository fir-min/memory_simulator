package simulator;

import ui.UI;

import java.io.File;

public class BestFit extends Simulator {
    /**
     * constructor
     * @param inputFile the input file
     * @param ui the ui
     * @param defrag defrag flag
     */
    public BestFit(File inputFile, UI ui, boolean defrag) {
        super(inputFile, ui, defrag);
    }

    /**
     * gets the start & end positions for the given process
     * @param process the process to be added to memory
     * @return true if the process can be added to memory, false otherwise
     */
    @Override
    public boolean getSlot(Process process) {
        return false;
    }

}
