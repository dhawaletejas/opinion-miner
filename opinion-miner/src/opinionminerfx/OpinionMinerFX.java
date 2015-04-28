/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opinionminerfx;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 *
 * @author Tejas Dhawale
 */
public class OpinionMinerFX extends Application {

    public static JFileChooser chooser;
    public static String filename, filename1, featsum;
    public static double adjDist, kFold;
    public ArrayList<String> reviews;
    public ArrayList<String> features;
    public ArrayList<String> summary;
    public ArrayList<String> reviewsPol;
    private int i;
    private int p, q, r;
    public static DecimalFormat Pre8Format = new DecimalFormat("#.##");
    public static Vector<String> vct = new Vector(30);
    public static TextField tf1;
    public static PasswordField pf1;
    public static ProgressIndicator pi2;
    public static Text lbl10 = new Text();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Opinion Miner");
        Image image = new Image("resources/Banner.jpg");
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setPreserveRatio(true);
        
        pi2 = new ProgressIndicator();
        final Slider slider1 = new Slider();
        final Slider slider2 = new Slider();
        final TextArea txtArea1 = new TextArea();
        final TextArea txtArea2 = new TextArea();
        final TextField txtField1 = new TextField();
        final ProgressIndicator pi1 = new ProgressIndicator();
        final HBox hb1 = new HBox();
        final HBox hb2 = new HBox();
        final HBox hb4 = new HBox();
        BorderPane bp = new BorderPane();
        bp.setTop(iv1);
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(5, 5, 5, 5));

        //LEFT BORDER PANE

        //TWITTER LIVE FEEDS
        GridPane gp3 = new GridPane();
        gp3.setHgap(10);
        gp3.setVgap(10);
        gp3.setPadding(new Insets(5 , 5 , 5 , 5 ));
        
        Image image1 = new Image("resources/Twitter-Logo.png");
        ImageView iv2 = new ImageView();
        iv2.setImage(image1);
        iv2.setPreserveRatio(true);
        gp3.add(iv2, 1, 1,1,3);
        
        Text lbl8 = new Text();
        lbl8.setText("User Name");
        gp3.add(lbl8,2,1);
        
        tf1 = new TextField();
        tf1.setText("swapbehere");
        gp3.add(tf1,3,1);
        
        Text lbl9 = new Text();
        lbl9.setText("Password");
        gp3.add(lbl9,2,2);
        
        pf1 = new PasswordField();
        pf1.setText("9869194463");
        gp3.add(pf1,3,2);
        
        Button btn4 = new Button();
        btn4.setText("Login");
        pi2.setPrefSize(20,20);
        
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    twitterbackup.TwitterBackupUI.main(null);
                    //pi2.setProgress(1.0);
                } catch (IOException ex) {
                    Logger.getLogger(OpinionMinerFX.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Button btn5 = new Button();
        btn5.setText("Retrieve");
        btn5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                twitter.Twitter.main(null);
            }
        });
        
        hb4.setSpacing(5);
        hb4.setAlignment(Pos.CENTER_LEFT);
        hb4.getChildren().addAll(btn4, pi2, btn5);
        gp3.add(hb4,3,3);
        //gp3.add(lbl10,1,4,3,1);
        
        TitledPane tp4 = new TitledPane();
        tp4.expandedProperty().set(false);
        tp4.setText("Twitter Feeds");
        tp4.setContent(gp3);
        gp.add(tp4,1,0,2,1);
        
        //FILE BROWSE
        Text lbl1 = new Text();
        lbl1.setText("Select Reviews Dataset:");
        gp.add(lbl1, 1, 1);

        Button btn1 = new Button();
        btn1.setText("Browse");
        gp.add(btn1, 2, 1);
        
        //SLIDERS
        Text lbl2 = new Text();
        lbl2.setText("k-Fold Value:");
        gp.add(lbl2, 1, 3);

        slider1.setMin(1);
        slider1.setMax(26);
        slider1.setValue(10);
        slider1.setShowTickLabels(true);
        slider1.setShowTickMarks(true);
        slider1.setMajorTickUnit(5);
        slider1.setMinorTickCount(1);
        slider1.setBlockIncrement(1);
        slider1.snapToTicksProperty();
        gp.add(slider1, 2, 3);

        Text lbl3 = new Text();
        lbl3.setText("Adjective Distance:");
        gp.add(lbl3, 1, 4);

        slider2.setMin(5);
        slider2.setMax(11);
        slider2.setValue(5);
        slider2.setShowTickLabels(true);
        slider2.setShowTickMarks(true);
        slider2.setMajorTickUnit(2);
        slider2.setMinorTickCount(1);
        slider2.setBlockIncrement(1);
        slider2.snapToTicksProperty();
        gp.add(slider2, 2, 4);

        //SUBMIT
        final Button btn2 = new Button();
        btn2.setText("Submit");
        
        hb1.setSpacing(15);
        hb1.setAlignment(Pos.TOP_LEFT);
        hb1.getChildren().addAll(btn2, pi1);
        gp.add(hb1, 2, 5);

        //PROGRESS BAR

        //SEPARATOR HORZ1
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.HORIZONTAL);
        gp.add(separator1, 1, 6, 2, 1);

        //REVIEWS LIST
        Text lbl4 = new Text();
        lbl4.setText("Reviews:");
        gp.add(lbl4, 1, 7);

        txtArea1.setPrefWidth(100);
        txtArea1.setPrefRowCount(15);
        gp.add(txtArea1, 1, 8, 2, 1);

        //ADD REVIEWS
        Button btn3 = new Button();
        btn3.setText("Add");
        btn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                pi1.setProgress(-1.0);
                reviews.add(txtField1.getText());
                controller.FileMgmt.readwriteArrayList(filename, reviews.get(i).toString());
                txtField1.setText("");
                i++;
            }
        });
        txtField1.setPrefColumnCount(20);
        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER_LEFT);
        hb2.getChildren().addAll(txtField1, btn3);

        gp.add(hb2, 1, 10, 2, 1);
        
        //SEPARATOR VERT1
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        gp.add(separator2, 3, 0, 1, 11);

        bp.setLeft(gp);


        //RIGHT BORDER PANE
        final GridPane gp1 = new GridPane();
        gp1.setHgap(10);
        gp1.setVgap(10);
        gp1.setPadding(new Insets(0, 10, 0, 0));

        final Text lbl5 = new Text();
        lbl5.setText("Overall Sentiment");
        gp1.add(lbl5, 1, 1);

        //PIE CHART
        final PieChart chart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new PieChart.Data("", 100));
        chart.setData(pieChartData);
        chart.setMaxSize(280, 300);
        chart.setLabelsVisible(false);
        chart.setLegendSide(Side.BOTTOM);
        chart.setLegendVisible(true);
        
        gp1.add(chart, 1, 3);

        final Text lbl6 = new Text();
        gp1.add(lbl6, 1, 4);

        //SEPARATOR HORZ2
        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.HORIZONTAL);
        gp1.add(separator3, 1, 5);

        bp.setRight(gp1);


        // DROP DOWN LIST - SUB FEATURES

        final Text lbl7 = new Text();
        lbl7.setText("Select Feature:");
        gp1.add(lbl7, 1, 6);

        final ChoiceBox cb = new ChoiceBox();
        cb.setPrefWidth(200);
        gp1.add(cb, 1, 7);


        // BAR CHART NULL

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number, String> bc = new BarChart<Number, String>(xAxis, yAxis);
        bc.setTitle("");
        bc.setPrefWidth(280);
        bc.setPrefHeight(220);
        bc.setBarGap(0.05);
        
        xAxis.setLabel("Value");
        xAxis.setTickLabelRotation(90);
        gp1.add(bc, 1, 8);

        //SEPARATOR VERT2
        Separator separator4 = new Separator();
        separator4.setOrientation(Orientation.VERTICAL);
        gp1.add(separator4, 0, 0, 1, 9);

        // BORDER CENTER PANE
        GridPane gp2 = new GridPane();
        gp2.setHgap(10);
        gp2.setVgap(10);
        gp2.setPadding(new Insets(0, 10, 0, 10));

        // REVIEWS POLARITY TITLED PANE

        txtArea2.setPrefWidth(375);
        txtArea2.setPrefRowCount(15);
        
        TitledPane tp = new TitledPane();
        tp.expandedProperty().set(false);
        tp.setText("Reviews Polarity");
        tp.setContent(txtArea2);
        //gp2.add(tp, 1, 1);
        
        // COMPARISON TITLED PANE - LINE CHART

        final NumberAxis xAxis1 = new NumberAxis();
        final NumberAxis yAxis1 = new NumberAxis();
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis1,yAxis1);
        lineChart.setTitle("Classifier Accuracy");
        xAxis1.setLabel("Iteration");
        yAxis1.setLabel("Percentage");
        
        //xAxis1.setUpperBound(5);
        //xAxis1.setTickUnit(1);
        //yAxis1.setUpperBound(100);
        yAxis1.autoRangingProperty().set(false);
        lineChart.setPrefSize(375, 350);
        TitledPane tp1 = new TitledPane();
        tp1.expandedProperty().set(true);
        tp1.setText("Accuracy Comparison");
        tp1.setContent(lineChart);
        //gp2.add(tp1, 1, 2);
        
        final Accordion accordion = new Accordion ();
        accordion.getPanes().addAll(tp,tp1);
        accordion.setExpandedPane(tp1);
        
        final Text txt8 = new Text();
        txt8.setText("Classifier Accuracy:");
        gp2.add(txt8,1,2);
        gp2.add(accordion, 1, 1);
        final Text txt9 = new Text();
        
        TitledPane tp2 = new TitledPane();
        tp2.setExpanded(false);
        tp2.expandedProperty().set(true);
        tp2.setText("Run Information");
        tp2.setContent(txt9);

        gp2.add(tp2, 1, 3);
        
        bp.setCenter(gp2);
        
        // BORDER BOTTOM PANE
        GridPane gp4 = new GridPane();
        Separator separator5 = new Separator();
        separator5.setOrientation(Orientation.HORIZONTAL);
        separator5.setPrefWidth(1100);
        gp4.add(separator5,0,0);
        gp4.add(lbl10,0,1);
        gp4.setPadding(new Insets(10,10,10,10));
        bp.setBottom(gp4);
        /*
        hb3.setSpacing(5);
        hb3.setAlignment(Pos.CENTER_LEFT);
        hb3.getChildren().addAll(txt9,txt10,btn4);
        */
        //bp.setBottom(hb3);

        //BROWSE BUTTON EVENT

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                pi1.setProgress(-1.0);
                FileChooser fileChooser = new FileChooser();
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DATA files (*.data)", "*.data");
                fileChooser.getExtensionFilters().add(extFilter);
                //Show open file dialog
                /*
                 * if(file != null){ File existDirectory = file.getParentFile();
                 * fileChooser.setInitialDirectory(existDirectory); }
                 */
                File file = fileChooser.showOpenDialog(null);
                filename = file.getPath();
                filename1 = file.getName();
                }catch(NullPointerException npe){}
            }
        });
        
        //SUBMIT BUTTON EVENT

        try{
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                adjDist = slider2.getValue();
                kFold = slider1.getValue();
                controller.Controller.main(null);
                reviews = controller.Controller.returnReviews();
                int rewsize = reviews.size();
                String review[] = new String[rewsize + 100];
                txtArea1.setText("");
                for (i = 0; i < rewsize; i++) {
                    review[i] = reviews.get(i).toString();
                    txtArea1.appendText(review[i]);
                    txtArea1.appendText("\n");
                }

                // FEATURES RETRIEVE

                features = null;
                features = controller.Controller.returnFeatures();
                String[] feat2 = new String[100];
                feat2 = model.NaiveBayCla.featureslist;
                vct.removeAllElements();
                for (int x = 0; x < features.size(); x++) {
                    //feat2[x]=feat1.get(x).toString();
                    vct.add(x, feat2[x]);
                    //System.out.println("\n" + feat2[x]);
                }
                //cb.setItems(null);
                cb.getItems().clear();
                cb.setItems(FXCollections.observableArrayList(vct));
                

                //REVIEWS POLARITY DISPLAY
                reviewsPol = null;
                reviewsPol = controller.Controller.returnReviewsPol();
                int j = reviewsPol.size();
                String reviewPol[] = new String[j + 100];
                txtArea2.setText("");
                for (int l = 0; l < j; l++) {
                    reviewPol[l] = reviewsPol.get(l).toString();
                    txtArea2.appendText(reviewPol[l]);
                    txtArea2.appendText("\n");
                }

                // COMPARISON LINE CHART UPDATE
                
                lineChart.getData().clear();
                XYChart.Series series = new XYChart.Series();
                XYChart.Series series1 = new XYChart.Series();
                series.setName("Normal Technique");
                series1.setName("K-Folding Technique");
                series.getData().add(new XYChart.Data(1, model.NaiveBayCla.kno[0]));
                series.getData().add(new XYChart.Data(2, model.NaiveBayCla.kno[1]));
                series.getData().add(new XYChart.Data(3, model.NaiveBayCla.kno[2]));
                series.getData().add(new XYChart.Data(4, model.NaiveBayCla.kno[3]));
                series.getData().add(new XYChart.Data(5, model.NaiveBayCla.kno[4]));
                series1.getData().add(new XYChart.Data(1, model.NaiveBayCla.kno[5]));
                series1.getData().add(new XYChart.Data(2, model.NaiveBayCla.kno[6]));
                series1.getData().add(new XYChart.Data(3, model.NaiveBayCla.kno[7]));
                series1.getData().add(new XYChart.Data(4, model.NaiveBayCla.kno[8]));
                series1.getData().add(new XYChart.Data(5, model.NaiveBayCla.kno[9]));
                
                System.out.println(model.NaiveBayCla.kno[0]+"\t"+model.NaiveBayCla.kno[1]+"\t"+model.NaiveBayCla.kno[2]+"\t"+model.NaiveBayCla.kno[3]+"\t"+model.NaiveBayCla.kno[4]);
                System.out.println(model.NaiveBayCla.kno[5]+"\t"+model.NaiveBayCla.kno[6]+"\t"+model.NaiveBayCla.kno[7]+"\t"+model.NaiveBayCla.kno[8]+"\t"+model.NaiveBayCla.kno[9]);
                
                lineChart.getData().addAll(series, series1);
                
                //PIE CHART EVENT UPDATE

                String featsumfinal1 = "";
                String[] featsumfinal2 = new String[3];
                String featsumfinal = "";
                String featsumfinal3 = "";
                double summaryFinal = 0.00;
                p = 0;
                q = 0;
                r = 0;
                summary = controller.Controller.returnSum();
                
                for (int a = 0; a < summary.size(); a++) {
                    featsumfinal = summary.get(a).toString();
                    featsumfinal1 = featsumfinal.substring(featsumfinal.indexOf("[") + 1, featsumfinal.indexOf("]"));
                    featsumfinal3 = featsumfinal1.toString();
                    featsumfinal2 = featsumfinal3.split(",");
                    p += Integer.parseInt(featsumfinal2[0]);
                    q += Integer.parseInt(featsumfinal2[1]);
                    r += Integer.parseInt(featsumfinal2[2]);
                    summaryFinal += Double.parseDouble(featsumfinal2[0]) / (Double.parseDouble(featsumfinal2[0]) + Double.parseDouble(featsumfinal2[1]) + Double.parseDouble(featsumfinal2[2]));
                }
                int tmp = p+q+r;
                p=(p*100)/tmp;
                q=(q*100)/tmp;
                r=(r*100)/tmp;
                
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data(p+"% Positve", p),
                        new PieChart.Data(q+"% Negative", q),
                        new PieChart.Data(r+"% Neutral", r));
                chart.setData(pieChartData);
                
                lbl5.textAlignmentProperty().set(TextAlignment.CENTER);
                lbl5.setText("Overall Sentiment : "+filename1);
                //chart.setTitle("");
                //chart.setTitle(filename1);

                if (p == q) {
                    lbl6.setText("MAIN FEATURE IS NEUTRAL");
                } else {
                    if(p>q)
                        lbl6.setText("MAIN FEATURE IS "+p+ "% POSITIVE");
                    else
                        lbl6.setText("MAIN FEATURE IS "+q+ "% NEGATIVE");
                }


                //BAR CHART

                summary = controller.Controller.returnSum();
                
                //ACCURACY UPDATE
                
                txt8.setText("Classifier Accuracy: \n"+model.NaiveBayCla.accuracy[0]+model.NaiveBayCla.accuracy[1]+model.NaiveBayCla.accuracy[2]);
                txt9.setText(model.NaiveBayCla.runInfo[0]+model.NaiveBayCla.runInfo[1]+model.NaiveBayCla.runInfo[2]+model.NaiveBayCla.runInfo[3]);
                pi1.setProgress(1.0);
            }
        });
        }catch(Exception e){}

        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue ov, Number value, Number new_value) {

                try{
                int z = new_value.intValue();
                //featsum = null
                featsum = summary.get(z).toString();
                String featsum1 = featsum.substring(featsum.indexOf("[") + 1, featsum.indexOf("]"));
                String[] featsum2 = featsum1.split(",");

                //BAR CHART DATA

                XYChart.Series series1 = new XYChart.Series();
                series1.setName(Integer.parseInt(featsum2[0])+" Positive");
                series1.getData().add(new XYChart.Data(Integer.parseInt(featsum2[0]), ""));
                

                XYChart.Series series2 = new XYChart.Series();
                series2.setName(Integer.parseInt(featsum2[1])+" Negative");
                series2.getData().add(new XYChart.Data(Integer.parseInt(featsum2[1]), ""));
                
                
                XYChart.Series series3 = new XYChart.Series();
                series3.setName(Integer.parseInt(featsum2[2])+" Neutral");
                series3.getData().add(new XYChart.Data(Integer.parseInt(featsum2[2]), ""));
                
                
                //bc.setTitle(vct.get(z));
                bc.getData().clear();
                bc.getData().addAll(series1, series2, series3);

                }catch(ArrayIndexOutOfBoundsException aoe){
                    aoe.getMessage();
                }
            }
        });

        primaryStage.setScene(new Scene(bp, 1100, 750));
        primaryStage.show();
    }
}