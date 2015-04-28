package twitterbackup;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import org.apache.log4j.Logger;

/**
 * The TwitterBackup UI. Uses Thinlet so most of the GUI stuff is in ui.xml.
 */
public class TwitterBackupUI {

    private static final long serialVersionUID = -8310719070327363473L;
    
    private TwitterBackupController controller;
    private Thread controllerThread;

    @SuppressWarnings("unused")
    public TwitterBackupUI() throws IOException {
        super();
        controller = new TwitterBackupController();
        //controller.restore();
    }

    public void setUsername(String username) {
        controller.setUsername(username);
    }

    public void setPassword(String password) {
        controller.setPassword(password);
    }

    public void setFile(String file) {
        controller.setFile(file);
    }

    public void action() {
        if (controllerThread == null) {
            controllerThread = new Thread(controller);
            controllerThread.setDaemon(true);
            controllerThread.start();

            Thread t = new Thread(new Runnable() {

                /**
                 * @see java.lang.Runnable#run()
                 */
                public void run() {
                    try {
                        controllerThread.join();
                    } catch (InterruptedException ex) {
                        // Ignored.
                    }
                    controllerThread = null;
                }
            });
            t.start();
            try{
            t.setDaemon(true);
            }catch(IllegalThreadStateException itse){}
        } else {
            controllerThread.interrupt();
        }
    }

    public static void main(String[] args) throws IOException {
        final TwitterBackupUI ui = new TwitterBackupUI();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    ui.controller.save();
                } catch (BackingStoreException ex) {
                    // Ignored
                }
            }
        });
        ui.action();
    }
}