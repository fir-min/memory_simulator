package simulator;

import ui.UI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Simulator {
    /**
     * length of the memory
     */
    protected int memLength = 100;

    /**
     * array that represents the memory
     */
    protected String[] mainMem = new String[memLength];

    /**
     * the input file of waitingProcesses
     */
    private File inputFile;

    /**
     * the UI {@link UI}
     */
    private UI ui;

    /**
     * master process list
     */
    private List<Process> processes = new ArrayList<>();

    /**
     * the waiting queue
     */
    private List<Process> waitingProcesses = new ArrayList<>();

    /**
     * list of waitingProcesses to be removed from memory;
     */
    private List<Process> toBeRemoved = new ArrayList<>();

    /**
     * the waitingProcesses which are currently in memory
     */
    private List<Process> processesInMemory = new ArrayList<>();

    /**
     * free memory
     */
    int freeMem = memLength;

    /**
     * the symbol that represents free memory
     */
    private static String FREE_MEM = "#";

    /**
     * boolean flag that indicates if defragmentation is part of the sim
     */
    private static boolean DEFRAG;

    /**
     * the current point in time for the simulation
     */
    private int currentTime = -1;

    /**
     * constructor
     * @param inputFile the input file
     * @param ui the ui
     * @param defrag defragmentation boolean flag
     */
    public Simulator(File inputFile, UI ui, boolean defrag) {
        this.inputFile = inputFile;
        this.ui = ui;
        DEFRAG = defrag;
        initMemory();
        parseInputFile();
    }

    /**
     * gets the start & end positions for the given process
     * @param process the process to be added to memory
     * @return if a slot was found or not
     */
    public abstract boolean getSlot(Process process);

    /**
     * initializes the memory
     */
    private void initMemory() {
        for (int i = 0; i < mainMem.length; i++) {
            mainMem[i] = FREE_MEM;
        }
    }

    /**
     * starts the sim
     */
    public void start() {
        waitingProcesses.forEach(this::addToMemory);
        cleanUpProcesses();
        incrementThroughTime(0);
    }

    /**
     * reads the input file and creates & adds waitingProcesses to the waiting queue
     */
    private void parseInputFile() {
        try {
            String[] lines = Utils.readFileToArray(inputFile);
            for (String line: lines) {
                String[] tokens = line.trim().split(" ");
                Process process = new Process(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                waitingProcesses.add(process);
                processes.add(process);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayInputData() {
        StringBuilder sb = new StringBuilder();
        processes.forEach(
                process -> {
                    sb.append(process.getPid());
                    sb.append(" - s: ");
                    sb.append(process.getSize());
                    sb.append(", d: ");
                    sb.append(process.getDuration());
                    sb.append("\n");
                }
        );
        ui.setInputData(sb.toString());
    }

    /**
     * simply updates the UI
     */
    private void updateUI() {
        displayInputData();
        displayMemory();
        ui.setCurrentTime(currentTime);
        ui.setTotalMemory(memLength);
        ui.setWaitingProcesses(waitingProcesses.size());
        ui.setFreeMemory(freeMem);
    }

    /**
     * cleans up the waiting queue and removes completed waitingProcesses from memory
     */
    private void cleanUpProcesses() {
        for (Process process: processesInMemory) {
            waitingProcesses.remove(process);
        }
        processesInMemory = processesInMemory.stream().filter(process -> process.getTimeLeft() > 0).collect(Collectors.toList());
    }

    /**
     * adds a process to memory
     * @param process the process to be added to memory
     */
    private void addToMemory(Process process) {
        boolean slot = getSlot(process);
        if (slot) {
            writeToMemory(process);
        }
    }

    /**
     * "writes" a process to memory
     * @param process the {@link Process}
     */
    private void writeToMemory(Process process) {
        if (!processesInMemory.contains(process)) {
            processesInMemory.add(process);
            freeMem -= process.getSize();
        }
        System.out.println(process.getPid());
        for (int i = process.getStart(); i < process.getEnd(); i++) {
            mainMem[i] = process.getPid();
        }
    }

    /**
     * defragments the memory
     */
    private void defragment() {
        ui.append("Attempting defragmentation...\n");
        initMemory();
    }

    /**
     * returns a process based on the process id
     * @param pid the process id
     * @return the process or null
     */
    protected Process getProcess(String pid) {
        for (Process p: processesInMemory) {
            if (p.getPid().equals(pid)) {
                return p;
            }
        }
        return null;
    }

    /**
     * "deletes" a process from memory
     * @param process the process
     */
    private void deleteFromMemory(Process process) {
        freeMem += process.getSize();
        for (int i = process.getStart(); i < process.getEnd(); i++) {
            mainMem[i] = FREE_MEM;
        }
    }

    /**
     * calls goToTime x number of times based on the input time
     * @param time the time to go to
     */
    public void incrementThroughTime(int time) {
        if (time > currentTime) {
            int increment = time - currentTime;
            for (int i = 0; i < increment; i++) {
                goToTime(currentTime+1);
            }
        } else {
            ui.append("cannot go backwards in time...\n");
        }
    }

    /**
     * move forward to a particular point in time in the simulation
     * @param time the time to go to
     */
    public void goToTime(int time) {

        int timeToSubtract= currentTime == -1? 0: time - currentTime;
        currentTime = time;
        ui.append("\nmoving forward to time " + time + "\n");

        // updating the time left for each process currently in memory
        processesInMemory.forEach(
                process -> process.setDuration(process.getDuration() - timeToSubtract)
        );
        // update memory
        updateMemory();

        // clean up
        cleanUpProcesses();
        if (currentTime > 0 && waitingProcesses.size() > 0 && DEFRAG) {
            defragment(); // clear memory
            processesInMemory.forEach(this::addToMemory); // add everything back
        }

        // try to add the process in the waiting queue to memory
        waitingProcesses.forEach(this::addToMemory);
        updateUI(); // update the ui after all the changes

    }

    /**
     * displays the memory in the UI
     */
    private void displayMemory() {
        StringBuilder output = new StringBuilder();
        output.append("\n");

        for (int i = 1; i <= mainMem.length; i++) {
            if (i % 10 == 0) {
                output.append(mainMem[i-1]);
                output.append("\n");
            } else {
                output.append(mainMem[i-1]);
            }
        }
        ui.append(output.toString());
    }

    /**
     * updates the memory. removes completed waitingProcesses
     */
    private void updateMemory() {
        processesInMemory.forEach(
                process -> {
                    if (process.getDuration() <= 0) {
                        toBeRemoved.add(process);
                        deleteFromMemory(process);
                    }
                }
        );

        toBeRemoved.forEach(
                process -> {
                    processesInMemory.remove(process);
                }
        );

        toBeRemoved = new ArrayList<>();
    }
}
