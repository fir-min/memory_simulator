package simulator;

import ui.UI;

import java.io.File;

public class NextFit extends Simulator{
    /**
     * the pointer
     */
    private int pointer = 0;

    /**
     * the previous pointer from the last time the getSlot method was called
     */
    private int lastPointer = pointer;

    /**
     * constructor
     * @param inputFile the input file
     * @param ui the UI
     * @param defrag defrag flag
     */
    public NextFit(File inputFile, UI ui, boolean defrag) {
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

        if (pointer == 0) {
            pointer = process.getSize();
        }

        if (pointer == -1 || (100 - pointer) < process.getSize()) {
            pointer = 0;
        }

        int start, end;

        boolean fits = false;
        int count = 0;
        while (pointer < memLength - 1) {
            for (int i = pointer; i < memLength; i++) {
                if (mainMem[i].equals("#")) {
                    count ++;
                    if (count == process.getSize()) {
                        fits = true;
                        break;
                    }

                } else {
                    pointer = i + 1;
                    count = 0;
                    fits = false;
                    break;
                }
            }
            if (fits) {
                start = pointer;
                end = pointer + count;

                if (start >= 100 || end >= 100) {
                    return false;
                }
                process.setStart(start);
                process.setEnd(end);
                pointer = end;

                if (pointer >= memLength || pointer == lastPointer ) {
                    pointer = -1;
                }
                lastPointer = pointer;
                return true;
            }
        }
        return false;
    }
}
