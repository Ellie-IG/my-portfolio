package se.kth.iv1350.bikerepairshop.exeptionhandler;

/** General Exeption to throw to the user.
 * 
 */
public class OperationFailedException extends Exception{
    private String errorMessage;
    private String wrongData;
    
    /** Creates a new instance
     * 
     * @param msg The error message. Why the exception was thrown
     * @param wrongData The data that was wrong and thus cause the exception.
     */
    public OperationFailedException(String msg){
        this.errorMessage = msg;
    }

    /** Creates a new instance
     * 
     * @param msg The error message. Why the exception was thrown
     * @param wrongData The data that was wrong and thus cause the exception.
     */
    public OperationFailedException(String msg, String wrongData){
        this.errorMessage = msg;
        this.wrongData = wrongData;
    }

    /** @return the error message string */
    public String getMessage(){
        return errorMessage;
    }

    /** @return the wrong data string */
    public String getWrongData(){
        return wrongData;
    }
}