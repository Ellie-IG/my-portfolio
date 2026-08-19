package se.kth.iv1350.bikerepairshop.exeptionhandler;

/** Thrown if the Registry are empty
 */
public class EmptyRegistryException extends Exception{
    private String errorMessage;
    private String connectionRegistry;
    
    /** Creates a new instance
     * 
     * @param msg The error message. Why the exception was thrown
     * @param connectionRegistry The data that was wrong and thus cause the exception.
     */
    public EmptyRegistryException(String msg, String connectionRegistry){
        this.errorMessage = msg;
        this.connectionRegistry = connectionRegistry;
    }

    /** @return the error message string */
    public String getErrorMessage(){
        return errorMessage;
    }

    /** @return the wrong data string */
    public String getConnectionRegistry(){
        return connectionRegistry;
    }
}
