package controller;

import controller.FileMgmt;
import java.util.ArrayList;
import model.PreProNBC;
import model.postagger;
import opinionminerfx.OpinionMinerFX.*;

public class Controller extends FileMgmt {

    public static ArrayList<String> origline;
    public static ArrayList<String> line;
    public static ArrayList<String> features;
    public static ArrayList<String> reviewspol;
    public static ArrayList<String> summary;
    public static String[] negWords = {"not", "never", "n't", "doesn't", "cannot", "can't", "cannt", "nor", "don't", "dont", "no", "wouldn't", "shouldn't", "couldn't", "ain't"};
    public static String[] contextshifters = {"but", "expect", "however", "only", "although", "though", "while", "whereas", "although", "despite", "would", "should", "miss", "refused", "assumed", "hard", "harder", "less"};
    public static int adjDist, tejk;
    public static String str, tejrand;

    public static void main(String[] args) {
        str = opinionminerfx.OpinionMinerFX.filename;
        adjDist = (int)opinionminerfx.OpinionMinerFX.adjDist;   //5
        tejk = (int)opinionminerfx.OpinionMinerFX.kFold;        //10
        tejrand = "random";
        //str="src/data/garmin_nuvi_255W_gps.txt.data";
        opinionminerfx.OpinionMinerFX.lbl10.setText("Loading Parts of Speech Tagger...");
        postagger ptagger = new postagger(str);
        origline = readDataFile(str);
        line = readDataFile(str + ".out");
        features = readDataFile(str + ".ft");
        opinionminerfx.OpinionMinerFX.lbl10.setText("Performing Lexical Analysis...");
        model.OpinionMining om = new model.OpinionMining();
        opinionminerfx.OpinionMinerFX.lbl10.setText("PreProcessing Dataset...");
        PreProNBC.main(args);
        summary = readDataFile(str + ".sum");
        reviewspol = readDataFile(str + ".res");
    }

    public static ArrayList returnReviews() {
        return origline;
    }

    public static ArrayList returnFeatures() {
        return features;
    }

    public static ArrayList returnReviewsPol() {
        return reviewspol;
    }

    public static ArrayList returnSum() {
        return summary;
    }
}
