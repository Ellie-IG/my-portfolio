package se.kth.iv1350.bikerepairshop.exeptionhandler;

/** Indicates the customer is not found in the database
 * 
 */
public class ObjectNotFoundException extends Exception{
    private String errorMessage;
    private String wrongData;
    private String objectName;
    
    /** Creates a new instance
     * 
     * @param msg The error message. Why the exception was thrown
     * @param wrongData The data that was wrong and thus cause the exception.
     */
    public ObjectNotFoundException(String msg, String wrongData, String objectName){
        this.errorMessage = msg;
        this.wrongData = wrongData;
        this.objectName = objectName;
    }

    /** @return the error message string */
    public String getErrorMessage(){
        return errorMessage;
    }

    /** @return the wrong data string */
    public String getWrongData(){
        return wrongData;
    }

    /** @return the object name as a string */
    public String getObjectName(){
        return objectName;
    }

}
