package model;

import java.util.ArrayList;

public class PreProNBC extends controller.Controller {

    public static void main(String[] args) {
        ArrayList<String> line = readDataFile(str + ".out"); //Read File specified in str Data file name
        ArrayList<String> origline = readDataFile(str + ".res"); //Read File specified in str Data file name
        ArrayList<String> AdjList = new ArrayList<String>();
        ArrayList<String> negList = new ArrayList<String>();
        ArrayList<String> CSList = new ArrayList<String>();
        ArrayList<String> AdvList = new ArrayList<String>();
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> finalres = new ArrayList<String>();
        int maxAdj = 0;
        int maxCS = 0;
        int maxAdv = 0;
        int maxNeg = 0;
        for (int i = 0; i < line.size(); i++) {
            String adj = "";
            int countAdj = 0;
            int countCS = 0;
            int countAdv = 0;
            int countNeg = 0;
            String[] wordwithTag = line.get(i).split("\\s");    //Split to get word with tags

            for (int j = 0; j < wordwithTag.length; j++) // find ADJECTIVES
            {
                String[] word = wordwithTag[j].split("/");
                if (word[1].contains("JJ") && word.length > 1) {
                    adj += word[0].toLowerCase() + ",";
                    countAdj++;
                }
            }
            if (maxAdj < countAdj) {
                maxAdj = countAdj;
            }

            // Check Negation of the adjective
            String neg = "";
            for (int j = 0; j < wordwithTag.length; j++) {
                for (int l = 0; l < negWords.length; l++) {
                    if (wordwithTag[j].split("/")[0].equalsIgnoreCase(negWords[l])) {
                        neg += negWords[l] + ",";
                    }
                }
            }
            if (maxNeg < countNeg) {
                maxNeg = countNeg;
            }

            //Context Shifter
            String contextShifters = "";
            for (int j = 0; j < wordwithTag.length; j++) {
                String[] word = wordwithTag[j].split("/");      //Split to get Tags
                for (int k = 0; k < contextshifters.length; k++) {
                    if (contextshifters[k].equalsIgnoreCase(word[0])) {
                        if (word[1].contains("JJ") && word.length > 1) //Check if the word tag is Noun
                        {
                            contextShifters += word[0].toLowerCase() + ",";     //To note-j is the position of the feature
                        } else if (word[1].contains("NN") && word.length > 1) {
                            contextShifters += word[0].toLowerCase() + ",";
                        } else if (word[1].contains("RB") && word.length > 1) {
                            contextShifters += word[0].toLowerCase() + ",";
                        } else if (word[1].contains("VB") && word.length > 1) {
                            contextShifters += word[0].toLowerCase() + ",";
                        } else {
                            contextShifters += word[0].toLowerCase() + ",";
                        }
                        countCS++;
                    }
                }
            }
            if (maxCS < countCS) {
                maxCS = countCS;
            }
            //Adverbs
            String adverbs = "";
            for (int j = 0; j < wordwithTag.length; j++) {
                String[] word = wordwithTag[j].split("/");      //Split to get Tags
                if (word[1].contains("RB") && word.length > 1) {
                    adverbs += word[0].toLowerCase() + ",";
                }
                countAdv++;
            }
            if (maxAdv < countAdv) {
                maxAdv = countAdv;
            }
            CSList.add(contextShifters);
            AdvList.add(adverbs);
            negList.add(neg);
            AdjList.add(adj);
            //System.out.println("orig line: "+origline.get(i));
            finalres.add(adj + adverbs + contextShifters + neg + origline.get(i).split("\\s")[0]);
        }
        //String result = printArrayList(finalres);
        String result = "";
        for (int i = 0; i < finalres.size(); i++) {
            result += finalres.get(i) + "\n";
        }

        writeDataFile(str + ".arff", result);
        //NaiveBayCla nbc = new NaiveBayCla();
        NaiveBayCla.main();
    }
}
