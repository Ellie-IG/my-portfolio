package se.kth.iv1350.bikerepairshop.logger;

/*
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;*/

/**
 * This class is responsible for showing error messages to the user.
 */ 
public class ErrorMessageUserLogger implements LoggerBike{

    @Override
    /**
     * Displaysthespecifiederrormessage.
     *
     * @param msg The error message.
     */
    public void log(String msg){
        /* 
        StringBuilder errorMsgBuilder = new StringBuilder();
        errorMsgBuilder.append(createTime());
        errorMsgBuilder.append(" ,ERROR:");
        errorMsgBuilder.append(msg); */
        System.out.println(msg);
    }

    /*
    private String createTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return now.format(formatter);
    }*/
}
