package model;

import java.util.ArrayList;

public class OpinionMining extends controller.Controller {

    public OpinionMining() {
        SWN3 swn = new SWN3();
        ArrayList<String> res = new ArrayList();
        for (int i = 0; i < line.size(); i++) {
            String sentFeat = "";
            String[] wordwithTag = line.get(i).split("\\s");    //Split to get word with tags
            for (int j = 0; j < wordwithTag.length; j++) {
                String[] word = wordwithTag[j].split("/");      //Split to get Tags
                if (word.length > 1 && word[1].contains("NN")) //Check if the word tag is Noun
                {
                    String[] feature = features.get(0).toLowerCase().split(",");
                    for (int k = 0; k < feature.length; k++) {
                        if (feature[k].equalsIgnoreCase(word[0])) {
                            sentFeat += word[0] + "=" + j + ";";     //j is the position of the feature
                            break;
                        }
                    }
                }
            }
            if (sentFeat.equals("")) {
                sentFeat = str + "=-1;"; //Put Product Name here
            }
            //Looking for Adjective of Noun
            String OW = "";
            if (adjDist != -1) {
                String[] totFeat = sentFeat.split(";");
                for (int k = 0; k < totFeat.length; k++) {
                    for (int j = 0; j < wordwithTag.length; j++) {
                        String[] wordPos = totFeat[k].split("=");
                        int pos = Integer.parseInt(wordPos[1]);
                        if ((j >= pos - adjDist && j <= pos + adjDist) || pos == -1) {   //pos -1 if Product name is explicitly now mentioned.
                            String[] word = wordwithTag[j].split("/");
                            if (word.length > 1 && word[1].contains("JJ")) {
                                String str = word[0] + ":" + j + "=" + swn.extract(word[0].toLowerCase(), "a") + ";";
                                if (!OW.equalsIgnoreCase(str)) {
                                    OW += str; // To calculate semantic orientation of the Adjective
                                }
                            }
                        }
                    }
                }
                //Repeat search for Adjective if you do not find any adjective in the restricted search
                if (OW.equals("")) {
                    for (int j = 0; j < wordwithTag.length; j++) {
                        String[] word = wordwithTag[j].split("/");
                        if (word.length > 1 && word[1].contains("JJ")) {
                            String str = word[0] + ":" + j + "=" + swn.extract(word[0].toLowerCase(), "a") + ";";
                            if (!OW.equalsIgnoreCase(str)) {
                                OW += str; // To calculate semantic orientation of the Adjective
                            }
                        }
                    }
                }
            }
            //Check Negation of the adjective
            String NcheckOW = "";
            if (!OW.equals("")) {
                String[] totOW = OW.split(";");
                for (int k = 0; k < totOW.length; k++) {
                    String[] wordPos = totOW[k].split(":")[1].split("=");
                    for (int j = 0; j < wordwithTag.length; j++) {
                        int pos = Integer.parseInt(wordPos[0]);
                        if (j >= pos - 3 && j <= pos + 3) {   //pos -1 if Product name is explicitly now mentioned.
                            for (int l = 0; l < negWords.length; l++) {
                                if (wordwithTag[j].split("/")[0].equalsIgnoreCase(negWords[l])) {
                                    String[] origStr = totOW[k].split("=");
                                    Double word = 0.0;
                                    try {
                                        word = Double.parseDouble(origStr[1]) * -1.0;
                                    } catch (Exception e) {
                                        word = 0.0;
                                    }
                                    totOW[k] = origStr[0] + "=" + word;
                                }
                            }
                        }
                    }
                    NcheckOW += totOW[k] + ";";
                }
            }
            //Context Shifter
            String contextShifters = "";
            for (int j = 0; j < wordwithTag.length; j++) {
                String[] word = wordwithTag[j].split("/");      //Split to get Tags
                for (int k = 0; k < contextshifters.length; k++) {
                    if (contextshifters[k].equalsIgnoreCase(word[0])) {
                        if (word.length > 1 && word[1].contains("JJ") && word.length > 1) //Check if the word tag is Noun
                        {
                            contextShifters += word[0] + "=" + swn.extract(word[0].toLowerCase(), "a") + ";";     //To note-j is the position of the feature
                        } else if (word.length > 1 && word[1].contains("NN") && word.length > 1) {
                            contextShifters += word[0] + "=" + swn.extract(word[0].toLowerCase(), "n") + ";";
                        } else if (word.length > 1 && word[1].contains("RB") && word.length > 1) {
                            contextShifters += word[0] + "=" + swn.extract(word[0].toLowerCase(), "r") + ";";
                        } else if (word.length > 1 && word[1].contains("VB") && word.length > 1) {
                            contextShifters += word[0] + "=" + swn.extract(word[0].toLowerCase(), "v") + ";";
                        } else {
                            contextShifters += word[0] + "=0;";
                        }
                    }
                }
            }
            //Adverbs
            String adverbs = "";
            for (int j = 0; j < wordwithTag.length; j++) {
                String[] word = wordwithTag[j].split("/");      //Split to get Tags
                if (word.length > 1 && word[1].contains("RB") && word.length > 1) {
                    adverbs += word[0] + "=" + swn.extract(word[0].toLowerCase(), "r") + ";";
                }
            }
            //Calculate Average
            String calcavgof = adverbs + contextShifters + NcheckOW;
            String[] feat = calcavgof.split(";");
            Double sum = 0.0;
            for (int j = 0; j < feat.length; j++) {
                try {
                    sum += Double.parseDouble(feat[j].split("=")[1]);
                } catch (Exception e) {
                    sum += 0.0;
                }
            }
            Double avg = sum / feat.length;
            String polarity = "Neutral";
            if (avg > 0) {
                polarity = "Positive";
            } else if (avg < 0) {
                polarity = "Negative";
            }
            res.add(polarity + " || " + origline.get(i).toString());
        }
        writeDataFile(str + ".res", printArrayList(res));
    }
}
