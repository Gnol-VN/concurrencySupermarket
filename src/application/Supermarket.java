package application;

import application.model.CheckoutTill;
import application.model.Scanner;
import application.producer_consumer.Consumer;
import application.producer_consumer.Producer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
    public static int TILL_LENGTH = 7;
    public static int MAXIMUM_LOOK_TIMES = 5;
    public static int MAXIMUM_PEOPLE_CAN_WAIT = 7;
    public static Random RANDOM = new Random();


    //Time
        //For Customer object
    public static int LOOK_AGAIN_TIME = 1500;
    public static int FIRST_LOOK_TIME = 1000;
        //For Consumer and Producer objects
    public static int SPAWN_TIME = 220; //200-240
    public static int CONSUMER_START_AFTER_PRODUCER_TIME = 4000;

    //Metrics
    public static int ENQUEUE_REQUESTED = 0;
    public static int DEQUEUE_SUCCESSED = 0;
    public static int SPAWN_CHECK_RATE = 0;

    public static List<CheckoutTill> CHECKOUT_TILL_LIST = new ArrayList<CheckoutTill>();
    public static List<Consumer> consumerList = new ArrayList<>();

    //Common UI
    public static GridPane GROUP_ROOT = new GridPane();
    public static FlowPane FLOW_PANE = new FlowPane(Orientation.HORIZONTAL, 5, 5);
    public static Label LABEL_ENQUEUE_REQUESTED = new Label(String.valueOf(ENQUEUE_REQUESTED));
    public static Label LABEL_DEQUEUE_SUCCESSED = new Label(String.valueOf(DEQUEUE_SUCCESSED));

    public static void main(String[] args)  throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Prepare UI
        prepareUI(primaryStage);
        //Create checkout till
        createTIll();

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
            consumer.setName("Consumer " + i);
            consumer.start();
        }

    }

    public static void createTIll(){
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            CheckoutTill checkoutTill = new CheckoutTill(new Scanner());
            checkoutTill.setWorkingStatus(true);
            CHECKOUT_TILL_LIST.add(checkoutTill);
        }
    }

    public static void prepareUI(Stage primaryStage){
//        GROUP_ROOT.setVgap(10);
        //Init the properties and constrains of GROUP_ROOT
        GROUP_ROOT.setHgap(80);
//        Supermarket.GROUP_ROOT.setGridLinesVisible(true);
        GROUP_ROOT.setPadding(new Insets(5));
        final int numCols = 12 ;
        final int numRows = 12 ;
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
        rectangle.setFill(Color.GOLD);
        shoppingAreaStackPane.getChildren().addAll(rectangle,label);
        shoppingAreaStackPane.setAccessibleText("Waiting area");
        GROUP_ROOT.add(shoppingAreaStackPane,6,7);

        //Add metrics nodes
        GROUP_ROOT.add(LABEL_ENQUEUE_REQUESTED,1,10,2,1);
        GROUP_ROOT.add(LABEL_DEQUEUE_SUCCESSED,1,11,2,1);

        //Add FLOW_PANE node
        GROUP_ROOT.add(FLOW_PANE,6,10,4,1);


        Scene scene = new Scene(GROUP_ROOT, 1024,768);
        primaryStage.setTitle("Sample Long");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
