package az.edu.turing;

import az.edu.turing.log.Log;
import az.edu.turing.service.LoggerService;

public class Application {
    public static void main(String[] args) {
        LoggerService.logger.info("This is an info message");
        LoggerService.logger.warn("This is a warning message");
        LoggerService.logger.error("This is an error message");
    }
}
