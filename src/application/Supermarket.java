package application;

import application.model.CheckoutTill;
import application.model.Scanner;
import application.producer_consumer.Consumer;
import application.producer_consumer.Producer;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long laptop on 9/23/2018.
 */
public class Supermarket extends Application {
    //Constant
    public static int NUMBER_OF_CHECKOUT_TILL = 2;
    public static int NUMBER_OF_CUSTOMER = 10;
    public static int TILL_LENGTH = 5;
    public static int MAXIMUM_LOOK_TIMES = 5;
    public static int MAXIMUM_PEOPLE_CAN_WAIT = 5;

//    public static int NUMBER_OF_CHECKOUT_TILL = 4;
//    public static int NUMBER_OF_CUSTOMER = 25;
//    public static int TILL_LENGTH = 7;
//    public static int MAXIMUM_LOOK_TIMES = 5;
//    public static int MAXIMUM_PEOPLE_CAN_WAIT = 5;
//
//
    public static List<CheckoutTill> CHECKOUT_TILL_LIST = new ArrayList<CheckoutTill>();
    public static List<Consumer> consumerList = new ArrayList<>();
    public static GridPane groupRoot = new GridPane();

    public static void main(String[] args)  throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        prepareUI(primaryStage);

    }

    public static void createTIll(){
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            CheckoutTill checkoutTill = new CheckoutTill(new Scanner());
            checkoutTill.setWorkingStatus(true);
            CHECKOUT_TILL_LIST.add(checkoutTill);
        }
    }

    public static void prepareUI(Stage primaryStage){
        groupRoot.setVgap(10);
        groupRoot.setHgap(80);
        Supermarket.groupRoot.setGridLinesVisible(true);
        groupRoot.setPadding(new Insets(5));
        Supermarket.groupRoot.add(new Line(),0,0, 12,12);

        Scene scene = new Scene(groupRoot, 1024,768);
        primaryStage.setTitle("Sample Long");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
