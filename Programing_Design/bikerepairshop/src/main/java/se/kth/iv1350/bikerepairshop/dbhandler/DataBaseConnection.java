package se.kth.iv1350.bikerepairshop.dbhandler;

import se.kth.iv1350.bikerepairshop.exeptionhandler.DataBaseConnectionException;

/** Handles connections to databases
 * 
 */
public interface DataBaseConnection {
    
    /** Connects the system to the database
     * 
     * @throws DataBaseConnectionException if connection to the database could not be established
     */
    public void connect() throws DataBaseConnectionException;

    /** Disconnects the system from the database
     * 
     * @throws DataBaseConnectionException if connection to the database is not established
     */
    public void disconnect() throws DataBaseConnectionException;

    /** Returns if the database is connected.
     * 
     * @return <code> true </code> if connected to the Database, otherwise <code> false </code> 
     */
    public boolean isConnect() ;
}
