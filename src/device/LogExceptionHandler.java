package device;

import java.lang.Thread.UncaughtExceptionHandler;

public class LogExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(e.getMessage() + " " + e);
    }

}
