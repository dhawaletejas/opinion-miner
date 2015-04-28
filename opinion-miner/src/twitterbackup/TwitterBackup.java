package twitterbackup;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Stores and reads tweets from the Twitter XML stream.
 */
public class TwitterBackup extends TreeSet<status> {

    public static final String VERSION = "TwitterBackup 3.1.8";
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 8561364223228785513L;

    /**
     * Constructor for TwitterBackup.
     *
     */
    public TwitterBackup() {
        super();
    }

    /**
     * Reads tweets from a reader in XML format.
     *
     * @param r the reader
     * @return how many tweets were read
     */
    public int read(Reader r) {
        int read = 0;
        statuses s = (statuses) createXStream().fromXML(r);
        if (s != null && s.statuses != null) {
            for (status status : s.statuses) {
                if (!contains(status)) {
                    add(status);
                    ++read;
                }
            }
        }
        return read;
    }

    /**
     * Writes the internal state (tweets) to a Writer.
     *
     * @param w the writer
     */
    public void write(Writer w) {
        statuses out = new statuses();
        out.statuses = new ArrayList<status>();
        for (status s : this) {
            out.statuses.add(s);
        }
        createXStream().toXML(out, w);
    }

    private XStream createXStream() {
        XStream x = new XStream(new XppDriver(new XmlFriendlyReplacer() {

            public String escapeName(String name) {
                return name;
            }

            public String unescapeName(String name) {
                return name;
            }
        }));
        x.alias("statuses", statuses.class);
        x.alias("status", status.class);
        x.addImplicitCollection(statuses.class, "statuses");
        x.alias("user", user.class);
        x.aliasField("protected", user.class, "_protected");
        return x;
    }
}
