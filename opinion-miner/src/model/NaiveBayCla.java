package model;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class NaiveBayCla extends controller.Controller {

    private static int k;                      //For k-fold Cross Validation
    private static DecimalFormat Pre8Form;
    private static boolean random;
    private static ArrayList<String> inputList = null;
    private static Hashtable<String, ArrayList<Integer>> featureCount;
    public static String[] featureslist = new String[100];
    public static String[] runInfo = new String[5];
    public static String[] accuracy = new String[5];
    public static int s;
    public static double[] kyes = new double[10];
    public static double[] kno = new double[10];

    public static void main() {
        s = 0;
        int y = 0;
        Pre8Form = new DecimalFormat("#.###");
        ArrayList<String> features = readDataFile(str + ".ft");
        opinionminerfx.OpinionMinerFX.lbl10.setText("Classification in Progress...");
        String ft = features.toString();
        ft = ft.replace("[", "");
        ft = ft.replace("]", "");
        String[] stopWords = ft.split(",");
        inputList = new ArrayList<String>();
        ArrayList<Integer> counts = null;
        //input file
        File inputFile = new File(str + ".res");
        BufferedReader buffReader;
        try {
            buffReader = new BufferedReader(new FileReader(inputFile));
            String str1 = "";
            //stop words array
            while ((str1 = buffReader.readLine()) != null) {
                inputList.add(str1);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        featureCount = new Hashtable<String, ArrayList<Integer>>();
        for (int i = 0; i < stopWords.length; i++) {
            int positiveCount = 0;
            int negativeCount = 0;
            int neutralCount = 0;
            for (String str1 : inputList) {
                if (str1.contains("Positive") && str1.contains(stopWords[i])) {
                    positiveCount++;
                }
                if (str1.contains("Negative") && str1.contains(stopWords[i])) {
                    negativeCount++;
                }
                if (str1.contains("Neutral") && str1.contains(stopWords[i])) {
                    neutralCount++;
                }
            }
            counts = new ArrayList<Integer>();
            counts.add(positiveCount);
            counts.add(negativeCount);
            counts.add(neutralCount);
            featureCount.put(stopWords[i], counts);
        }
        FileWriter outputData;
        try {
            outputData = new FileWriter(str + ".sum");
            BufferedWriter out = new BufferedWriter(outputData);
            Enumeration e = featureCount.keys();
            //iterate through Hashtable keys Enumeration
            while (e.hasMoreElements()) {
                String str1 = "";
                String key = (String) e.nextElement();
                ArrayList<Integer> values = featureCount.get(key);
                str1 += key + ",";
                for (Integer val : values) {
                    str1 += Integer.toString(val) + ",";
                }
                String[] val = str1.split(",");
                int val1 = Integer.parseInt(val[1]);
                int val2 = Integer.parseInt(val[2]);
                int val3 = Integer.parseInt(val[3]);
                featureslist[y] = val[0];
                y++;
                if (val1 >= val2 && val1 >= val3) {
                    str1 = val[0] + ": Positive -> [" + val[1] + "," + val[2] + "," + val[3] + "]";
                }

                if (val2 >= val1 && val2 >= val3) {
                    str1 = val[0] + ": Negative -> [" + val[1] + "," + val[2] + "," + val[3] + "]";
                }

                if (val3 >= val1 && val3 >= val2) {
                    str1 = val[0] + ": Neutral -> [" + val[1] + "," + val[2] + "," + val[3] + "]";
                }
                out.write(str1);
                out.write("\r\n");
                //Spitting the output
            }
            out.close();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //NaiveBayCla nbc = new NaiveBayCla();
        for (s = 0; s < 10; s++) {
            ArrayList<String> ars = readDataFile(str + ".arff");
            Iterator iter = ars.iterator();         //Iterator for arraylist to read lines
            int numOfAttr;
            int numOfInstances;
            String[][] A;                     //Matrix to store file data
            ArrayList indexOfRealAttri = new ArrayList();
            if (s < 5) {
                k = 1;
                random = false;
            } else {
                k = tejk;
                random = true;
            }
            if (iter.hasNext()) {               //Make sure file is not empty!!
                numOfInstances = ars.size();
                runInfo[0] = "\n Scheme:    Naive Bayesian";
                runInfo[1] = "\n Relation:  " + opinionminerfx.OpinionMinerFX.filename1;
                runInfo[2] = "\n Instances: " + numOfInstances;
                runInfo[3] = "\n Test mode: " + k + "-fold cross-validation";
                double finalmae = 0.0;
                double finalcorrect = 0.0;
                double finalincorrect = 0.0;
                double finalcorrecres = 0.0;
                double finalincorrecres = 0.0;
                for (int t = 0; t < k; t++) {
                    //Separate Training and Testing set
                    ArrayList<String> trainars = new ArrayList<String>();
                    ArrayList<String> testars = new ArrayList<String>();
                    if (random) {
                        Collections.shuffle(ars);
                    }
                    int min = 0;
                    int max = ars.size() / 10;
                    for (int i = 0; i < ars.size(); i++) {
                        if (i >= min && i < max) {
                            testars.add((String) ars.get(i));
                        } else {
                            trainars.add((String) ars.get(i));
                        }
                    }
                    //Count classifiers
                    String[] classifiers = new String[trainars.size()];
                    int countClassPos = 0, countClassNeg = 0, countClassNeu = 0;
                    for (int i = 0; i < trainars.size(); i++) {
                        String[] line = trainars.get(i).split(",");
                        classifiers[i] = line[line.length - 1];
                        if (classifiers[i].equalsIgnoreCase("Positive")) {
                            countClassPos++;
                        } else if (classifiers[i].equalsIgnoreCase("Negative")) {
                            countClassNeg++;
                        } else if (classifiers[i].equalsIgnoreCase("Neutral")) {
                            countClassNeu++;
                        }
                    }
                    String[] resArr = new String[testars.size()];
                    for (int i = 0; i < testars.size(); i++) {
                        String[] attr = testars.get(i).split(",");
                        double[][][] countArr = new double[3][attr.length][testars.size()];
                        double[] Pxc = {1.0, 1.0, 1.0};
                        for (int j = 0; j < attr.length; j++) {
                            String[] attribute;
                            attribute = new String[1];
                            attribute[0] = attr[j].toLowerCase();
                            int cPos = 0, cNeg = 0, cNeu = 0;
                            for (int k = 0; k < trainars.size(); k++) {
                                for (int a = 0; a < attribute.length; a++) {
                                    if (trainars.get(k).toLowerCase().contains(attribute[a])) {
                                        if (trainars.get(k).toLowerCase().contains("positive")) {
                                            cPos++;
                                        } else if (trainars.get(k).toLowerCase().contains("negative")) {
                                            cNeg++;
                                        } else if (trainars.get(k).toLowerCase().contains("neutral")) {
                                            cNeu++;
                                        }
                                        a = attribute.length;
                                    }
                                }
                            }
                            //1 is added to avoid getting zero probability - Laplace
                            countArr[0][j][i] = (double) (cPos + 1) / (double) (countClassPos + 1);
                            countArr[1][j][i] = (double) (cNeg + 1) / (double) (countClassNeg + 1);
                            countArr[2][j][i] = (double) (cNeu + 1) / (double) (countClassNeu + 1);
                            Pxc[0] *= (countArr[0][j][i]);
                            Pxc[1] *= (countArr[1][j][i]);
                            Pxc[2] *= (countArr[2][j][i]);
                        }
                        if (Pxc[0] > Pxc[1] && Pxc[0] > Pxc[2]) {
                            resArr[i] = "Positive";
                        } else if (Pxc[1] > Pxc[0] && Pxc[1] > Pxc[2]) {
                            resArr[i] = "Negative";
                        } else if (Pxc[2] > Pxc[0] && Pxc[2] > Pxc[1]) {
                            resArr[i] = "Neutral";
                        }
                    }
                    //Accuracy
                    int correct = 0;
                    int incorrect = 0;
                    for (int j = 0; j < testars.size(); j++) {
                        String[] attr = testars.get(j).split(",");
                        if (attr[attr.length - 1].equalsIgnoreCase(resArr[j])) {
                            correct++;
                        } else {
                            incorrect++;
                        }
                    }
                    double correcres = (double) correct * 100 / testars.size();
                    double incorrecres = (double) 100.0 - correcres;
                    double mae = (double) incorrect / testars.size();
                    finalcorrect += correct;
                    finalincorrect += testars.size() - correct;
                    finalcorrecres += correcres;
                    finalincorrecres += incorrecres;
                    finalmae += mae;
                }
                finalmae = finalmae / k;
                finalcorrect = finalcorrect / k;
                finalincorrect = finalincorrect / k;
                finalcorrecres = finalcorrecres / k;
                finalincorrecres = finalincorrecres / k;
                /*
                 * accuracy[0] = "\n Correctly Classified Instances " +
                 * Pre8Form.format(finalcorrecres) + "%"; accuracy[1] = "\n
                 * Inorrectly Classified Instances " +
                 * Pre8Form.format(finalincorrecres) + "%"; accuracy[2] = "\n
                 * Mean Absolute Error " + Pre8Form.format(finalmae);
                 *
                 */
                kno[s] = Double.parseDouble(Pre8Form.format(finalcorrecres));
            } else {
                System.out.println("File is empty");
                System.exit(0);
            }
            if(s>4)
            {
            accuracy[0] = "\n Correctly Classified Instances   " +Pre8Form.format((kno[5]+kno[6]+kno[7]+kno[8]+kno[9])/5)+ "%";
            accuracy[1] = "\n Inorrectly Classified Instances  " +Pre8Form.format(100-((kno[5]+kno[6]+kno[7]+kno[8]+kno[9])/5))+ "%";
            accuracy[2] = "\n Mean Absolute Error              " +Pre8Form.format((kno[5]+kno[6]+kno[7]+kno[8]+kno[9])/500);
            }
        }
        opinionminerfx.OpinionMinerFX.lbl10.setText("DONE");
    }
}