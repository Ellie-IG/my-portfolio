package se.kth.iv1350.bikerepairshop.model;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.bikerepairshop.payment.DiscountStrategy;
import se.kth.iv1350.bikerepairshop.payment.PaymentProcessor;

public class ProposedRepairs {
    private List<Task> taskList = new ArrayList<>();
    private PaymentProcessor payProc = new PaymentProcessor();
    private int[] individualTaskCost;
    private int totalCost;


    /** Creates a new instance 
     */
    public ProposedRepairs(){
    }

    /** @return the task list
     */
    public List<Task> getTaskList() {
        return taskList;
    }

     /** @return the individual task costs list
     */
    public int[] getIndividualTaskCost() {
        return individualTaskCost;
    }

     /** @return the the total cost of the repair
     */
    public int getTotalCost() {
        return totalCost;
    }

    /** Adds a fault to the end of the list if it is not in the list yet.
     * 
     * @param fault
     */
    public void addTaskToList(String nameTask, int cost, int timeMin) {
        if(taskList.size() == 0) taskList.add(new Task(nameTask, cost, timeMin));
        
        boolean isInList = false;
        for(Task task : taskList) {
            if (task.getNameTask() == nameTask) {
                isInList = true;
            }
        }
        if(!isInList){
            taskList.add(new Task(nameTask, cost, timeMin));
        }
        propRepairsListDone();
    }


    /** @return a string of the tasks in the list
     */
    public String taskListToString() {
        if (taskList.isEmpty()) {
            return "No faults recorded.";
        }

        String message = "Current bike faults: \n";

        for (Task task : taskList) {
            message = message.concat("- " + task.getNameTask() + "\n");
        }
        return message;
    }  

    /** @return a string of all the costs and the total cost 
     */
    public String taskCostToString() {
        if (taskList.isEmpty()) {
            return "No faults recorded.";
        }

        String message = "Current recommended bike repairs: \n";

        for (Task task : taskList) {
            message = message.concat(" - " + task.getNameTask() + ": \t" + task.getCost() + "\n");
        }

        message = message.concat("\t total: \t" + totalCost);
        return message;
    }  
    
    /** Fills the Indiviual task cost with all costs from tasks in the list
     */
    private void setIndividualTaskCost(){
        individualTaskCost = new int[taskList.size()];
        int index = 0;
        for (Task task : taskList){
            individualTaskCost[index++] = task.getCost();
        }
    }

    /** Gets the total cost from the individual task costs
     */
    private void setTotalCost(){
        int totCostBefore = 0;
        for (int i = 0; i < individualTaskCost.length; i++){
            totCostBefore += individualTaskCost[i];
        }
        totalCost = payProc.applyDiscount(totCostBefore);
    }

    
    /** Sets the discount based on strategy
     * 
     * @param strategy the discount strategy to be set
     */
    public void setDiscount(DiscountStrategy strategy){
        payProc.setDiscountStrategy(strategy);
        setTotalCost();
    }

    /** Initiates the final calculations for the Proposed Repairs
    */
    public void propRepairsListDone(){
        setIndividualTaskCost();
        setTotalCost();
    }
    
}
