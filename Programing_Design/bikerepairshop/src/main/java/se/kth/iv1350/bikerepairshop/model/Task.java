package se.kth.iv1350.bikerepairshop.model;

/** A gathering of values connected to the task
 * 
 */
public class Task {
    private final String nameTask;
    private final int cost;
    private final int timeMin; 

    /** Creates a new instance
     * 
     * @param nameTask what the task is
     * @param cost the cost of the task for customer
     * @param timeMin the time total it takes to do this
     */
    public Task(String nameTask, int cost, int timeMin){
        this.nameTask = nameTask;
        this.cost = cost;
        this.timeMin = timeMin;
    }

    /** @return the task name
     */
    public String getNameTask() {
        return nameTask;
    }

    /** @return the cost associated with the task.
     */
    public int getCost() {
        return cost;
    }

    /** @return the time required to complete the task in minutes.
     */
    public int getTimeMin() {
        return timeMin;
    }


    
}
