package simulator;

public class Process {
    /**
     * the process id
     */
    private String pid;

    /**
     * the process size
     */
    private int size;

    /**
     * process duration
     */
    private int duration;

    /**
     * starting index
     */
    private int start;

    /**
     * ending index
     */
    private int end;

    /**
     * the remaining duration
     */
    private int timeLeft;

    public Process(String pid, int size, int duration) {
        this.pid = pid;
        this.size = size;
        this.duration = duration;
        timeLeft = duration;
    }

    public String getPid() {
        return pid;
    }

    /**
     * return the size
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * return the duration
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * sets the duration
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * returns if the process is completed
     * @return if the process is completed
     */
    public boolean isDone() {
        return timeLeft <= 0;
    }

    /**
     * sets the starting position for the process
     * @param start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * sets the end position
     * @param end
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * return start
     * @return start
     */
    public int getStart() {
        return start;
    }

    /**
     * return end
     * @return end
     */
    public int getEnd() {
        return end;
    }

    /**
     * return time left
     * @return timeLeft
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * set time left
     * @param timeLeft time left
     */
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
