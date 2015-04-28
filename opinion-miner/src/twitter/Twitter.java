package twitter;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Twitter {

    public static void main(String argv[]) {
        try {
            String result = "";
            String feat = "";
            String res1 = "";
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("C:/data/tb.xml"));
            // normalize text representation
            doc.getDocumentElement().normalize();
            opinionminerfx.OpinionMinerFX.lbl10.setText("Reading xml dataset...");
            
            NodeList totalTweets = doc.getElementsByTagName("status");
            int totalTweets1 = totalTweets.getLength();
            opinionminerfx.OpinionMinerFX.lbl10.setText("Total no of tweets : " + totalTweets1);

            for (int s = 0; s < totalTweets.getLength(); s++) {


                Node firstTweet = totalTweets.item(s);
                if (firstTweet.getNodeType() == Node.ELEMENT_NODE) {


                    Element firstTweetElement = (Element) firstTweet;

                    //-------
                    NodeList firstTweetList = firstTweetElement.getElementsByTagName("text");
                    Element firstTweetElement1 = (Element) firstTweetList.item(0);

                    NodeList textFNList = firstTweetElement1.getChildNodes();
                    res1 = ((Node) textFNList.item(0)).getNodeValue().trim();
                    for (int i = 0; i < res1.length(); i++) {
                        String[] word = res1.split("#");
                        if (word.length > 1) {
                            String[] word1 = word[1].split(" ");
                            if(word1[0].contains("."))
                            {
                                word1=word1[0].split(".");
                            }
                            else if(word1[0].contains(","))
                            {
                                word1=word1[0].split(",");
                            }
                            if(word1.length>1)
                            {
                                if(feat.contains(word1[0])==false)
                                {
                                    feat+="#"+word1[0]+"\n";
                                }
                            }
                        }
                    }
                    result += ((Node) textFNList.item(0)).getNodeValue().trim() + "\n";
                }//end of if clause
            }//end of for loop with s var
            controller.FileMgmt.writeDataFile("C:/data/twitterfeeds.txt.data", result);
            controller.FileMgmt.writeDataFile("C:/data/twitterfeeds.txt.data.ft", feat);
            opinionminerfx.OpinionMinerFX.lbl10.setText("Dataset & Features List created");
            
        } catch (SAXParseException err) {
            opinionminerfx.OpinionMinerFX.lbl10.setText("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            opinionminerfx.OpinionMinerFX.lbl10.setText(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //System.exit (0);

    }//end of main
}