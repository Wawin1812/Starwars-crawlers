package ass2;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 27/04/2015
 * Time: 11:50 AM
 * Created for ass2 in package ass2
 * @author abx
 * @author (your name and id)
 * @see ass2.crawlreader.CrawlReader
 */

import ass2.crawlreader.CrawlReader;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class StarWarsCrawler extends Application {

    private Group root;
    private CrawlReader model;

    static final double shrinkFactorX = 0.99;
    static final double shrinkFactorY = 0.99;
    static final int numberOfStepsTillRecede = 50;
    static double upperStep;

    private static final String propFileName = "ass2.properties";

    // TODO crawlFileName must be chosen using the menu "Episode"
    // TODO after the application has started (part of Task 2)

    //private static final String crawlFileName = "StarWars-3";

    public static void main(String[] args)
            throws IOException {

        Application.launch(args);
    }


    /** override start method which creates the scene
     *  and all nodes and shapes in it (main window only),
     *  and redefines how the nodes react to user inputs
     *  and other events;
     *  @param primaryStage Stage (the top level container)
     *  */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Star Wars");

//        Parameters parameters = getParameters(); // for picking args
        Properties props = new Properties(System.getProperties());
        try {
            props.load(new FileReader(new File(propFileName)));
            System.setProperties(props);
        } catch (IOException e) {
            System.out.printf("properties file cannot be read%n");
            System.exit(1);
        }

        String crawlFileName = "StarWars-1";
        String fontFileName = System.getProperty(crawlFileName);
        System.out.println(fontFileName);

        try {
            model = new CrawlReader(fontFileName);
        } catch (IOException e) {
            System.err.printf("cannot open the file %s%n", fontFileName);
            System.exit(2);
        }

        /* the main window */
        root = new Group();
        final Scene scene = new Scene(root,700, 500, Color.BLACK);
        double centreX = scene.getX() + 0.5 * scene.getWidth();
        double centreY = scene.getY() + 0.5 * scene.getHeight();
        double bottom = centreY + 0.5*scene.getHeight();
        upperStep = - scene.getHeight() / numberOfStepsTillRecede;
        double top = scene.getY();
        System.out.println("Parameters of the main window:");
        System.out.printf("  width -- %.0f%n  height -- %.0f%n centre -- (%.0f,%.0f)%n",
                scene.getWidth(), scene.getHeight(), centreX, centreY);

        Group title_grp = new Group();
        Text title = new Text(100, 250, "A long time ago, in a \n"+" galaxy far, far away....");
        title.setFont(new Font(60));
        title.setFill(Color.YELLOW);
        title_grp.getChildren().add(title);
        root.getChildren().addAll(title_grp);

        FadeTransition f = new FadeTransition(Duration.millis(3000), title);
        f.setFromValue(0);
        f.setToValue(1);
        f.setCycleCount(2);
        f.setAutoReverse(true);
        root.getChildren().addAll(title);//updating the scene builder
        
        
        //Creating star and twinkle it
        Polygon star = new Polygon();
        FadeTransition ft = new FadeTransition(Duration.millis(1000), star);
        for(int i = 0; i < 30; i++ ){

            Random rand = new Random();

            int  a = rand.nextInt(500) + 1;
            int  b = rand.nextInt(500) + 1;

            star = new Polygon();
            star.getPoints().addAll(new Double[]{
                    a + i + 50.0 , b + i + 50.0,
                    a + i + 61.0 , b + i + 58.0,
                    a + i + 57.0 , b + i + 45.0,
                    a + i + 53.0 , b + i + 58.0,
                    a + i + 64.0 , b + i + 50.0
            });
            star.setFill(Color.LIGHTBLUE);
            Group g1 = new Group();
            ft = new FadeTransition(Duration.millis(1000), star);
            ft.setFromValue(1.0);
            ft.setToValue(0.1);
            ft.setCycleCount(1000);
            ft.setAutoReverse(true);
            ft.play();
            Group g = new Group();
            g1.getChildren().add(star);//updating the stars
            root.getChildren().addAll(g1);

        }
        
        Text text = new Text();
        String start = String.format("%s%n%s%n%s%n%s",
                model.getTitle(),
                model.getSubTitle(),
                model.getPrefix(),
                model.getmainPart());
        text.setText(start);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(new Font("Verdana Bold", 30));
        text.setFill(Color.YELLOW);
        Group group = new Group();
        group.getChildren().addAll(text);
        group.setLayoutX(centreX - 0.5*group.getLayoutBounds().getWidth());
        group.setLayoutY(bottom);

        // rotation angle must be adjusted (reduced) at every step to make
        // the effect of receding view more realistic; right now, it's flawed;
        // TODO if you decide to exclude turning effect remove the next line
        // TODO to implement it properly, the rotation angle (1st arg in Rotate)
        // TODO must be replaced by a method call to make turning angle decrease
        // TODO at every time step (part of Task "bonus")
        group.getTransforms().add(new Rotate(5, new Point3D(1,0,0)));
        root.getChildren().addAll(group);

        SequentialTransition s_trans = new SequentialTransition(title,f);
        s_trans.play();
        
        
        ///////////////////////MASTER CHECKBOX//////////////////////////////////
        
        
        StackPane master = new StackPane();
        CheckBox cb = new CheckBox("Master");
        cb.setStyle("-fx-text-fill:YELLOW; -fx-font-weight:bold");
        cb.setIndeterminate(false);
        cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent a) {
                
                       
                }
            });

        master.setLayoutX(320);
        master.setLayoutY(5);
        master.getChildren().addAll(cb);
        root.getChildren().addAll(master);

        //----------------------MENU BAR--------------------------------------//
        MenuBar menuBar = new MenuBar();
        //menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu Episodemenu = new Menu("Episode");
        Episodemenu.setStyle("-fx-font-weight:bold");

        ////////////////////////EPISODE 1///////////////////////////////////////
        MenuItem Episode1 = new MenuItem("Episode1");
        Episode1.setStyle("-fx-font-weight:bold");
        Episode1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                String crawlFileName;
                if(cb.isSelected()){
                    crawlFileName = "StarWars-1_nonversed";
                    group.setLayoutX(centreX - 1.2*group.getLayoutBounds().getWidth());
                }else{
                    crawlFileName = "StarWars-1";
                }
                String fontFileName = System.getProperty(crawlFileName);
                System.out.println(fontFileName);

                try {
                    model = new CrawlReader(fontFileName);
                } catch (IOException e) {
                    System.err.printf("cannot open the file %s%n", fontFileName);
                    System.exit(2);
                };
                //Reading the Episode text file from crawlreader
                String start = String.format("%s%n%s%n%s%n%s",
                        model.getTitle(),
                        model.getSubTitle(),
                        model.getPrefix(),
                        model.getmainPart());
                text.setText(start);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(new Font("Verdana Bold", 30));
                text.setFill(Color.YELLOW);

            }
        });
        /////////////////////////EPISODE 2///////////////////////////////////////////
        MenuItem Episode2 = new MenuItem("Episode2");
        Episode2.setStyle("-fx-font-weight:bold");
        Episode2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                String crawlFileName;
                if(cb.isSelected()){
                    crawlFileName = "StarWars-2_nonversed";
                    group.setLayoutX(centreX - 1.2*group.getLayoutBounds().getWidth());
                }else{
                    crawlFileName = "StarWars-2";
                }
                String fontFileName = System.getProperty(crawlFileName);
                System.out.println(fontFileName);

                try {
                    model = new CrawlReader(fontFileName);
                } catch (IOException e) {
                    System.err.printf("cannot open the file %s%n", fontFileName);
                    System.exit(2);
                };
                //Reading the Episode text file from crawlreader
                String start = String.format("%s%n%s%n%s%n%s",
                        model.getTitle(),
                        model.getSubTitle(),
                        model.getPrefix(),
                        model.getmainPart());
                text.setText(start);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(new Font("Verdana Bold", 30));
                text.setFill(Color.YELLOW);

            }
        });
        ////////////////////////EPISODE 3///////////////////////////////////////
        MenuItem Episode3 = new MenuItem("Episode3");
        Episode3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                String crawlFileName;
                if(cb.isSelected()){
                    crawlFileName = "StarWars-3_nonversed";
                    group.setLayoutX(centreX - 1.2*group.getLayoutBounds().getWidth());
                }else{
                    crawlFileName = "StarWars-3";
                }
                Episode3.setStyle("-fx-font-weight:bold");
                String fontFileName = System.getProperty(crawlFileName);
                System.out.println(fontFileName);

                try {
                    model = new CrawlReader(fontFileName);
                } catch (IOException e) {
                    System.err.printf("cannot open the file %s%n", fontFileName);
                    System.exit(2);
                };
                //Reading the Episode text file from crawlreader
                String start = String.format("%s%n%s%n%s%n%s",
                        model.getTitle(),
                        model.getSubTitle(),
                        model.getPrefix(),
                        model.getmainPart());
                text.setText(start);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(new Font("Verdana Bold", 30));
                text.setFill(Color.YELLOW);

            }
        });
        ////////////////////////EPISODE 4///////////////////////////////////////
        MenuItem Episode4 = new MenuItem("Episode4");
        Episode4.setStyle("-fx-font-weight:bold");
        Episode4.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                String crawlFileName;
                if(cb.isSelected()){
                    crawlFileName = "StarWars-4_nonversed";
                    group.setLayoutX(centreX - 1.2*group.getLayoutBounds().getWidth());
                }else{
                    crawlFileName = "StarWars-4";
                }
                String fontFileName = System.getProperty(crawlFileName);
                System.out.println(fontFileName);

                try {
                    model = new CrawlReader(fontFileName);
                } catch (IOException e) {
                    System.err.printf("cannot open the file %s%n", fontFileName);
                    System.exit(2);
                };
                //Reading the Episode text file from crawlreader
                String start = String.format("%s%n%s%n%s%n%s",
                        model.getTitle(),
                        model.getSubTitle(),
                        model.getPrefix(),
                        model.getmainPart());
                text.setText(start);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(new Font("Verdana Bold", 30));
                text.setFill(Color.YELLOW);

            }
        });
        ////////////////////////EPISODE 5///////////////////////////////////////
        MenuItem Episode5 = new MenuItem("Episode5");
        Episode5.setStyle("-fx-font-weight:bold");
        Episode5.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                String crawlFileName;
                if(cb.isSelected()){
                    crawlFileName = "StarWars-5_nonversed";
                    group.setLayoutX(centreX - 1.2*group.getLayoutBounds().getWidth());
                }else{
                    crawlFileName = "StarWars-5";
                }
                String fontFileName = System.getProperty(crawlFileName);
                System.out.println(fontFileName);

                try {
                    model = new CrawlReader(fontFileName);
                } catch (IOException e) {
                    System.err.printf("cannot open the file %s%n", fontFileName);
                    System.exit(2);
                };
                //Reading the Episode text file from crawlreader
                String start = String.format("%s%n%s%n%s%n%s",
                        model.getTitle(),
                        model.getSubTitle(),
                        model.getPrefix(),
                        model.getmainPart());
                text.setText(start);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(new Font("Verdana Bold", 30));
                text.setFill(Color.YELLOW);

            }
        });
        ////////////////////////EPISODE 6///////////////////////////////////////
        MenuItem Episode6 = new MenuItem("Episode6");
        Episode6.setStyle("-fx-font-weight:bold");
        Episode6.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                String crawlFileName;
                if(cb.isSelected()){
                    crawlFileName = "StarWars-6_nonversed";
                    group.setLayoutX(centreX - 1.2*group.getLayoutBounds().getWidth());
                }else{
                    crawlFileName = "StarWars-6";
                }
                String fontFileName = System.getProperty(crawlFileName);
                System.out.println(fontFileName);

                try {
                    model = new CrawlReader(fontFileName);
                } catch (IOException e) {
                    System.err.printf("cannot open the file %s%n", fontFileName);
                    System.exit(2);
                };
                //Reading the Episode text file from crawlreader
                String start = String.format("%s%n%s%n%s%n%s",
                        model.getTitle(),
                        model.getSubTitle(),
                        model.getPrefix(),
                        model.getmainPart());
                text.setText(start);
                text.setTextAlignment(TextAlignment.CENTER);
                text.setFont(new Font("Verdana Bold", 30));
                text.setFill(Color.YELLOW);

            }
        });
        Episodemenu.getItems().addAll(Episode1,Episode2,Episode3,Episode4,Episode5,Episode6);
        menuBar.getMenus().addAll(Episodemenu);//updating the menu bar
        root.getChildren().addAll(menuBar);

        ///////////////////////// PLAY AND PAUSE BUTTON/////////////////////////
        StackPane play = new StackPane();
        Button play_button = new Button();
        play_button.setText("Play");
        play_button.setStyle("-fx-font-weight:bold");
        play_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent a) {

                double w, h;
                w = group.getLayoutBounds().getWidth();
                h = 0.5 * (1 - shrinkFactorX) * w;
                RotateTransition r_trans = new RotateTransition(Duration.millis(9000),text);
                r_trans.setByAngle(90);
                r_trans.setAutoReverse(false);
                r_trans.setAxis(new Point3D(1,0,0));
                TranslateTransition t_trans = new TranslateTransition(Duration.millis(9000),text);
                t_trans.setFromY(0f);
                t_trans.setToY(-1200);
                t_trans.setCycleCount(1);
                t_trans.setAutoReverse(false);
                ScaleTransition s_trans = new ScaleTransition(Duration.millis(10000),text);
                s_trans.setByX(-shrinkFactorX);
                s_trans.setByY(-.7);
                s_trans.setCycleCount(1);
                s_trans.setAutoReverse(false);
                ParallelTransition p_trans;
                p_trans = new ParallelTransition(text,t_trans,r_trans,s_trans);
                p_trans.playFromStart();
                //pt.play();
                
                StackPane pause = new StackPane();
                Button pause_button = new Button();
                pause_button.setText("Pause");
                pause_button.setStyle("-fx-font-weight:bold");
                pause_button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent a) {
                
                        p_trans.pause();
            }
        });
            pause.setLayoutX(114);
            pause.setLayoutY(0);
            pause.getChildren().addAll(pause_button);//updating pause button
            root.getChildren().addAll(pause);
            StackPane resume = new StackPane();
                Button resume_button = new Button();
                resume_button.setText("Resume");
                resume_button.setStyle("-fx-font-weight:bold");
                resume_button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent a) {
                        p_trans.play();
                    }
                });
            resume.setLayoutX(160);
            resume.setLayoutY(0);
            resume.getChildren().addAll(resume_button);//updating the resume button
            root.getChildren().addAll(resume);    
            
                
            }
        });
        play.setLayoutX(76);
        play.setLayoutY(0);
        play.getChildren().addAll(play_button);//updating the play button
        root.getChildren().addAll(play);
        
        ///////////////////////// REVERSE BUTTON////////////////////////////////      
        StackPane reverse = new StackPane();
        Button reverse_button = new Button();
        reverse_button.setText("Reverse");
        reverse_button.setStyle("-fx-font-weight:bold");
        reverse_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent a) {
                       
                double w, h;
                w = group.getLayoutBounds().getWidth();
                h = 0.5 * (1 - shrinkFactorX) * w;
                RotateTransition r_trans = new RotateTransition(Duration.millis(9000),text);
                r_trans.setByAngle(-120);
                r_trans.setAutoReverse(false);
                r_trans.setAxis(new Point3D(1,0,0));
                TranslateTransition t_trans = new TranslateTransition(Duration.millis(9000),text);
                t_trans.setFromY(-1000);
                t_trans.setToY(0f);
                t_trans.setCycleCount(1);
                t_trans.setAutoReverse(false);
                ScaleTransition s_trans = new ScaleTransition(Duration.millis(10000),text);
                s_trans.setByX(shrinkFactorX);
                s_trans.setByY(0.6);
                s_trans.setCycleCount(1);
                s_trans.setAutoReverse(false);
                ParallelTransition p_trans;
                p_trans = new ParallelTransition(text,t_trans,r_trans,s_trans);
                p_trans.play();
                }
            });
        
        reverse.setLayoutX(215);
        reverse.setLayoutY(0);
        reverse.getChildren().addAll(reverse_button);
        root.getChildren().addAll(reverse);
        
        ///////////////////////////CLEAR BUTTON///////////////////////////////// 
        
        StackPane clear = new StackPane();
        Button clr_b = new Button();
        clr_b.setText("Clear");
        clr_b.setStyle("-fx-font-weight:bold");
        clr_b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent a) {
                
                text.setVisible(false);
                      
                }
            });
        
        clear.setLayoutX(270);
        clear.setLayoutY(0);
        clear.getChildren().addAll(clr_b);
        root.getChildren().addAll(clear);
        
        scene.onKeyPressedProperty().set(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent key) {

                if (key.getCode() == KeyCode.UP) {

                    double w, h;
                    w = group.getLayoutBounds().getWidth();
                    h = 0.5 * (1 - shrinkFactorX) * w;
                    shiftAndScale(group, h, upperStep, shrinkFactorX, shrinkFactorY);
                    group.getTransforms().add(new Rotate(0.75, new Point3D(1, 0, 0)));
                    return;
                }
            }
        });

        // finalise and display the whole scene graph
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** an auxiliary method to reduce and move a node (which may contain a text line)
     * as as result one one time-step animation
     * @param node is the node to shift and scale
     * @param shiftX horizontal shift
     * @param shiftY vertical shift (negative values mean upwards)
     * @param scaleX horizontal shrinkage coefficient
     * @param scaleY vertical shrinkage
     */
    private void shiftAndScale(Node node,
                               double shiftX, double shiftY,
                               double scaleX, double scaleY) {
        Translate t = new Translate();
        Scale s = new Scale();
        t.setX(shiftX);
        t.setY(shiftY);
        s.setX(scaleX);
        s.setY(scaleY);
        node.getTransforms().addAll(t,s);
    }

}
