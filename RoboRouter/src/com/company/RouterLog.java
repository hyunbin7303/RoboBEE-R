package com.company;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RouterLog {



    boolean append;
    public RouterLog()
    {
        append = true;
    }

    public void WriteLogFile() throws IOException {

        FileHandler handler = new FileHandler("Router.log", append);
        Logger logger = Logger.getLogger("com.company");
        logger.severe("severe message");
        logger.fine("Fine Message");
        logger.finer("Finer Message");
        logger.setLevel(Level.FINE);

        logger.log(Level.INFO, "Msg" );
    }


}
