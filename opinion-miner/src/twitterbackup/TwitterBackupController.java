package twitterbackup;

import com.thoughtworks.xstream.XStreamException;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.stage.Popup;
import javax.swing.JOptionPane;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.lang.StringUtils;
import static twitterbackup.Resource.close;

/**
 * Twitter backup controller should be run in a separate thread to fetch twitter updates and write them to an output
 * file when done.
 * 
 * @author burkardj
 * @version $Id: TwitterBackupController.java 4002 2011-06-26 09:45:15Z johann $
 */
public class TwitterBackupController implements Runnable {
    private TwitterBackup backup;
    private String username, token, tokenSecret, password, file;
    private long timeout = 10500;
    private final OAuthConsumer consumer = new DefaultOAuthConsumer("neibRfmUvzUnqjKL3rDkpA", "Hq02kO6znwoHiJOwnQAVApBgZH8m4JuHRIbB3EILU");
    private final OAuthProvider provider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");
    public Popup pop = new Popup();
    
    public TwitterBackupController() {
        this(new TwitterBackup());
        file = "C:/data/tb.xml";
        username = "swapbehere";
        password = "9869194463";
    }

    public TwitterBackupController(TwitterBackup backup) {
        super();
        this.backup = backup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = StringUtils.trimToNull(username);
        this.token = this.tokenSecret = null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = StringUtils.trimToNull(password);
        this.token = this.tokenSecret = null;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (getToken() == null || getTokenSecret() == null) {
            try {
                String authURL = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
                try {
                    Desktop.getDesktop().browse(new URI(authURL));
                    opinionminerfx.OpinionMinerFX.lbl10.setText("opening\n" + authURL + "\nin your browser");
                }
                catch (IOException ex) {
                    opinionminerfx.OpinionMinerFX.lbl10.setText("please open\n" + authURL + "\nin your browser");
                }
                catch (URISyntaxException ex) {
                    opinionminerfx.OpinionMinerFX.lbl10.setText("please open\n" + authURL + "\nin your browser");
                }
                catch (UnsupportedOperationException ex) {
                    opinionminerfx.OpinionMinerFX.lbl10.setText("please open\n" + authURL + "\nin your browser");
                }
                String pin = StringUtils.trimToEmpty(JOptionPane.showInputDialog("Please enter your PIN"));
                provider.retrieveAccessToken(consumer, pin);
                
                token = consumer.getToken();
                tokenSecret = consumer.getTokenSecret();
            }
            catch (OAuthException ex) {
                opinionminerfx.OpinionMinerFX.lbl10.setText(ex.getLocalizedMessage() + ", aborting");
                return;
            }
        }
        else {
            consumer.setTokenWithSecret(token, tokenSecret);
        }

        long then = System.currentTimeMillis();

        readExistingBackup();

        boolean changed = false;
        int page = 1;
        int read = 0;
        BufferedWriter writer = null;
        URL u = null;

        do {
            try {
                u = new URL("http://api.twitter.com/1/statuses/user_timeline.xml?screen_name=" + username + "&page=" + page + "&count=200");
                opinionminerfx.OpinionMinerFX.lbl10.setText("loading "+u);
                read = -1;
                read = backup.read(openURL(u));
                if (read > 0) {
                    opinionminerfx.OpinionMinerFX.lbl10.setText(read + " new tweets downloaded, " + backup.size() + " total");
                    changed = true;
                }
                else {
                    //LOG.info("no new tweets found");
                    opinionminerfx.OpinionMinerFX.lbl10.setText("no new tweets found");
                }
            }
            catch (IllegalStateException ex) {
                opinionminerfx.OpinionMinerFX.lbl10.setText(ex.getLocalizedMessage() + ", will try again");
            }
            catch (MalformedURLException ex) {
                opinionminerfx.OpinionMinerFX.lbl10.setText(ex.getLocalizedMessage() + ", aborting");
                break;
            }
            catch (IOException ex) {
                opinionminerfx.OpinionMinerFX.lbl10.setText(ex.getLocalizedMessage() + ", will try again");
            }
            catch (OAuthException ex) {
                opinionminerfx.OpinionMinerFX.lbl10.setText(ex.getLocalizedMessage() + ", aborting");
                break;
            }
            catch (XStreamException ex) {
                opinionminerfx.OpinionMinerFX.lbl10.setText("Twitter has changed their API again. Please email johann@johannburkard.de, thanks.");
                ex.printStackTrace();
                break;
            }
            if (read == -1 || read > 0) {
                opinionminerfx.OpinionMinerFX.lbl10.setText("waiting " + HumanTime.approximately(timeout));
                try {
                    Thread.sleep(timeout);
                }
                catch (InterruptedException ex) {
                    break;
                }
            }
            if (read > 0) {
                ++page;
            }
        }
        while (read != 0);

        try {
            if (changed) {
                opinionminerfx.OpinionMinerFX.lbl10.setText("saving backup to " + getFile());
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile()), "UTF-8"));
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                backup.write(writer);
            }
        }
        catch (IOException ex) {
            opinionminerfx.OpinionMinerFX.lbl10.setText("couldn't write to " + getFile() + ": " + ex.getLocalizedMessage());
        }
        finally {
            close(writer);
        }
        opinionminerfx.OpinionMinerFX.lbl10.setText("backup finished after " + HumanTime.approximately(System.currentTimeMillis() - then)+" to "+getFile());
        try{
        opinionminerfx.OpinionMinerFX.pi2.setProgress(1.0);
        }catch(IllegalStateException ise){}
    }

    private void readExistingBackup() {
        if (getFile() != null) {
            File existing = new File(getFile());
            if (existing.isFile() && existing.canRead()) {
                Reader reader = null;
                try {
                    reader = new InputStreamReader(new FileInputStream(existing), "UTF-8");
                    backup.clear();
                    int read = backup.read(reader);
                    opinionminerfx.OpinionMinerFX.lbl10.setText("read " + read + " tweets from " + getFile());
                }
                catch (IOException ex) {
                    opinionminerfx.OpinionMinerFX.lbl10.setText("could not read " + getFile() + ": " + ex.getLocalizedMessage());
                }
                finally {
                    close(reader);
                }
            }
        }
    }

    public void save() throws BackingStoreException {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put("username", StringUtils.defaultString(username));
        prefs.put("password", StringUtils.defaultString(password));
        prefs.put("file", StringUtils.defaultString(file));
        prefs.remove("maxPages"); // From TwitterBackup 2, unused now.
        prefs.remove("timeout");
        if (token == null) {
            prefs.remove("token");
        }
        else {
            prefs.put("token", token);
        }
        if (tokenSecret == null) {
            prefs.remove("tokenSecret");
        }
        else {
            prefs.put("tokenSecret", tokenSecret);
        }
        prefs.flush();
    }

    public void restore() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        username = opinionminerfx.OpinionMinerFX.tf1.getText();
        password = opinionminerfx.OpinionMinerFX.pf1.getText();
        file = "C:/data/tb.xml";
        token = prefs.get("token", null);
        tokenSecret = prefs.get("tokenSecret", null);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = StringUtils.trimToNull(file);
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    private Reader openURL(URL u) throws IOException, IllegalStateException, OAuthException {
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("User-Agent", TwitterBackup.VERSION + " by Johann Burkard; +http://is.gd/4ete");
        consumer.sign(conn);
        int response = conn.getResponseCode();
        if (response != 200) {
            throw new IllegalStateException(conn.getResponseCode() + ": " + conn.getResponseMessage());
        }
        return new InputStreamReader(conn.getInputStream(), "UTF-8");
    }
}
