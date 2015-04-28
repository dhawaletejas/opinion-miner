package model;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.FileReader;

public class postagger extends controller.FileMgmt {

    public postagger(String fname) {
        try {
            MaxentTagger tagger = new MaxentTagger("src/models/left3words-wsj-0-18.tagger");
            BufferedReader stdInput = new BufferedReader(new FileReader(fname));	// Reads Data file from given path
            String s = null;
            String taggedString = "";
            while ((s = stdInput.readLine()) != null) {
                int last = s.lastIndexOf(".");
                int l = s.length();
                if (last != l - 1) {
                    s += ".";	// Adds fullstop to end of line, if it's missing
                }
                taggedString += tagger.tagString(s) + "\n";	// Tagging done
            }
            writeDataFile(fname + ".out", taggedString);
        } catch (Exception e) {
        }
    }
}
