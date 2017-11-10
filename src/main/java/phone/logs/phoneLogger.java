package phone.logs;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.sip.Utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wujl on 2017/11/10.
 */
public class phoneLogger implements Logger{
    public final static String LOG_FILE = File.separator + "phoneLogs"
            + File.separator + "peers.log";
    public final static String NETWORK_FILE = File.separator + "phoneLogs"
            + File.separator + "transport.log";

    private PrintWriter logWriter;
    private PrintWriter networkWriter;
    private Object logMutex;
    private Object networkMutex;
    private SimpleDateFormat logFormatter;
    private SimpleDateFormat networkFormatter;

    public phoneLogger(String peersHome) {
        if (peersHome == null) {
            peersHome = Utils.DEFAULT_PEERS_HOME;
        }
        try {
            logWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(peersHome + LOG_FILE)));
            networkWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(peersHome + NETWORK_FILE)));
        } catch (IOException e) {
            System.out.println("logging to stdout");
            logWriter = new PrintWriter(System.out);
            networkWriter = new PrintWriter(System.out);
        }
        logMutex = new Object();
        networkMutex = new Object();
        logFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        networkFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    }

    @Override
    public final void debug(String message) {
        synchronized (logMutex) {
            logWriter.write(genericLog(message.toString(), "DEBUG"));
            logWriter.flush();
        }
    }

    @Override
    public final void info(String message) {
        synchronized (logMutex) {
            logWriter.write(genericLog(message.toString(), "INFO "));
            logWriter.flush();
        }
    }

    @Override
    public final void error(String message) {
        synchronized (logMutex) {
            logWriter.write(genericLog(message.toString(), "ERROR"));
            logWriter.flush();
        }
    }

    @Override
    public final void error(String message, Exception exception) {
        synchronized (logMutex) {
            logWriter.write(genericLog(message, "ERROR"));
            exception.printStackTrace(logWriter);
            logWriter.flush();
        }
    }

    private final String genericLog(String message, String level) {
        StringBuffer buf = new StringBuffer();
        buf.append(logFormatter.format(new Date()));
        buf.append(" ");
        buf.append(level);
        buf.append(" [");
        buf.append(Thread.currentThread().getName());
        buf.append("] ");
        buf.append(message);
        buf.append("\n");
        return buf.toString();
    }

    @Override
    public final void traceNetwork(String message, String direction) {
        synchronized (networkMutex) {
            StringBuffer buf = new StringBuffer();
            buf.append(networkFormatter.format(new Date()));
            buf.append(" ");
            buf.append(direction);
            buf.append(" [");
            buf.append(Thread.currentThread().getName());
            buf.append("]\n\n");
            buf.append(message);
            buf.append("\n");
            networkWriter.write(buf.toString());
            networkWriter.flush();
        }
    }
}
