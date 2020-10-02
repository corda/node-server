package net.corda.explorer.constants;

import net.corda.explorer.model.response.MessageResponseEntity;
import org.springframework.http.HttpStatus;

public class MessageConstants {

    public static String SUCCESS = "SUCCESS";
    public static MessageResponseEntity<Void> UNAUTHORIZED = new MessageResponseEntity<>(null, HttpStatus.UNAUTHORIZED, false, "no valid client token");
}
