package simulator;

import ui.UI;

import java.io.File;

public class FirstFit extends Simulator {
    /**
     * constructor
     * @param inputFile the input file
     * @param ui the UI
     * @param defrag defrag flag
     */
    public FirstFit(File inputFile, UI ui, boolean defrag) {
        super(inputFile, ui, defrag);
    }

    /**
     * gets the start & end positions for the given process
     * @param process the process to be added to memory
     * @return true if the process can be added false otherwise
     */
    @Override
    public boolean getSlot(Process process) {
        if (freeMem < process.getSize()) {
            return false;
        }
        int start, end;
        int e = 0;
        boolean fits = false;
        int count = 0;
        while (e < memLength - 1) {
            for (int i = e; i < memLength; i++) {
                if (mainMem[i].equals("#")) {
                    count ++;
                    if (count == process.getSize()) {
                        fits = true;
                        break;
                    }

                } else {
                    e = i + 1;
                    count = 0;
                    fits = false;
                    break;
                }
            }
            if (fits) {
                start = e;
                end = e + count;

                if (start >= 100 || end >= 100) {
                    return false;
                }
                process.setStart(start);
                process.setEnd(end);
                return true;
            }
        }
        return false;
    }
}
