package application;

import application.model.CheckoutTill;
import application.model.Scanner;
import application.model.TillWatcher;
import application.producer_consumer.Consumer;
import application.producer_consumer.Producer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Long laptop on 9/23/2018.
 */
public class Supermarket extends Application {
    //CONSTANT
    public static int NUMBER_OF_CHECKOUT_TILL = 6;
//    public static int NUMBER_OF_CUSTOMER = 1000;
    public static int TILL_LENGTH = 6;
    public static int MAXIMUM_LOOK_TIMES = 6;
    public static Random RANDOM = new Random();


    //Time
        //For Customer object
    public static int FIRST_LOOK_TIME = 2000;
    public static int LOOK_AGAIN_TIME = 1000;
        //For Consumer and Producer objects
    public static int SPAWN_TIME = 600; //200-240
    public static int CONSUMER_START_AFTER_PRODUCER_TIME = 4000;

    //Metrics
    public static double ENQUEUE_REQUESTED = 0;
    public static double DEQUEUE_SUCCESSED = 0;
    public static double TRADE_BALANCE = 0;
    public static volatile int LEFT_CUSTOMER_NUMBER = 0;
    public static volatile int TOTAL_WAIT_TIME = 0;

    public static List<CheckoutTill> CHECKOUT_TILL_LIST = new ArrayList<CheckoutTill>();
    public static List<Consumer> consumerList = new ArrayList<>();

    //Common UI
    public static GridPane GROUP_ROOT = new GridPane();
    public static FlowPane WAITING_AREA_FLOWPANE = new FlowPane(Orientation.HORIZONTAL, 5, 5);
    public static Label LABEL_SCALE = new Label("Trade balance: "+String.valueOf(TRADE_BALANCE));
    public static Label LABEL_SPAWN_RATE = new Label("Spawn rate: " + String.valueOf(SPAWN_TIME));
    public static Label LABEL_ENQUEUE_REQUESTED = new Label("Enqueued customer: "+String.valueOf(ENQUEUE_REQUESTED));
    public static Label LABEL_DEQUEUE_SUCCESSED = new Label("Dequeued customer: " + String.valueOf(DEQUEUE_SUCCESSED));
    public static Label LABEL_LEFT_CUSTOMER = new Label("Left customer: "+String.valueOf(LEFT_CUSTOMER_NUMBER));
    public static Label LABEL_TOTAL_WAIT_TIME = new Label("Total wait time "+String.valueOf(TOTAL_WAIT_TIME));

    public static void main(String[] args)  throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Create checkout till
        createTIll();

        //Prepare UI
        prepareUI(primaryStage);


        //Create Till Watcher
        createTillWatcher();

        //Create producer and consumer
        Producer producer = new Producer();
        producer.setName("Producer 0");

        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            Consumer consumer = new Consumer(i);
            consumerList.add(consumer);
        }

        //Start producer
        producer.start();

        //Start consumer
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            Consumer consumer = consumerList.get(i);
            int consumerIndex = i+1;
            consumer.setName("Consumer " + consumerIndex);
            consumer.start();
        }

    }

    private void createTillWatcher() {
        TillWatcher tillWatcher = new TillWatcher();
        tillWatcher.setName("Till watcher");
        tillWatcher.setPriority(10);
        tillWatcher.start();

    }

    public static void createTIll(){
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            CheckoutTill checkoutTill = new CheckoutTill(new Scanner());
            checkoutTill.setWorkingStatus(true);
            CHECKOUT_TILL_LIST.add(checkoutTill);
        }
    }

    public static void prepareUI(Stage primaryStage){
        //Init the properties and constrains of GROUP_ROOT
        GROUP_ROOT.setHgap(80);
        GROUP_ROOT.setVgap(4);
        GROUP_ROOT.setPadding(new Insets(5));
        final int numCols = 15 ;
        final int numRows = 15 ;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(10);
            GROUP_ROOT.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(50);
            GROUP_ROOT.getRowConstraints().add(rowConst);
        }

        //Add nodes to GROUP_ROOT
        //Add Waiting area node
        StackPane shoppingAreaStackPane = new StackPane();
        Label label = new Label("Waiting area");
        Rectangle rectangle = new Rectangle(80*5,50);
        rectangle.setFill(Color.WHITE);
        shoppingAreaStackPane.getChildren().addAll(rectangle,label);
        shoppingAreaStackPane.setAccessibleText("Waiting area");
        GROUP_ROOT.add(shoppingAreaStackPane,6,8);

        //Add VBOX to store metric values in a vertical box
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().add(LABEL_ENQUEUE_REQUESTED);
        vBox.getChildren().add(LABEL_DEQUEUE_SUCCESSED);
        vBox.getChildren().add(LABEL_SCALE);
        vBox.getChildren().add(LABEL_SPAWN_RATE);
        vBox.getChildren().add(LABEL_LEFT_CUSTOMER);
        vBox.getChildren().add(LABEL_TOTAL_WAIT_TIME);
        GROUP_ROOT.add(vBox,1,10,4,4);
        //Add metrics nodes
//        GROUP_ROOT.add(LABEL_ENQUEUE_REQUESTED,1,10,4,1);
//        GROUP_ROOT.add(LABEL_DEQUEUE_SUCCESSED,1,11,4,1);
//        GROUP_ROOT.add(LABEL_SCALE,1,12,4,1);
//        GROUP_ROOT.add(LABEL_SPAWN_RATE,1,13,4,1);
//        GROUP_ROOT.add(LABEL_LEFT_CUSTOMER,1,14,4,1);

        //Add spawn rate slider
        Slider spawnSlider = new Slider();
        spawnSlider.setMin(200);
        spawnSlider.setMax(1000);
        spawnSlider.setValue(600);
        spawnSlider.setShowTickLabels(true);
        spawnSlider.setShowTickMarks(true);
        spawnSlider.setMinorTickCount(0);
        spawnSlider.setMajorTickUnit(200);
        spawnSlider.setBlockIncrement(100);
        GROUP_ROOT.add(spawnSlider, 1,15, 6, 1);

        spawnSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                SPAWN_TIME = newValue.intValue();
                LABEL_SPAWN_RATE.setText("Spawn rate: " +String.valueOf(SPAWN_TIME));

            }
        });

        //Add WAITING_AREA_FLOWPANE node
        GROUP_ROOT.add(WAITING_AREA_FLOWPANE,6,10,6,6);

        //Add TILL FLOWPANE for each till
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            CheckoutTill checkoutTill = CHECKOUT_TILL_LIST.get(i);
            GROUP_ROOT.add(checkoutTill.getTillFlowPane(),
                    1, checkoutTill.getCheckoutId(),10,1 );
        }

        Scene scene = new Scene(GROUP_ROOT, 1024,768);
        primaryStage.setTitle("Sample Long");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
