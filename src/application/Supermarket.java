package application;

import application.model.CheckoutTill;
import application.model.Scanner;
import application.producer_consumer.Consumer;
import application.producer_consumer.Producer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long laptop on 9/23/2018.
 */
public class Supermarket extends Application {
    //Constant
//    public static int NUMBER_OF_CHECKOUT_TILL = 2;
//    public static int NUMBER_OF_CUSTOMER = 10;
//    public static int TILL_LENGTH = 5;
//    public static int MAXIMUM_LOOK_TIMES = 5;
//    public static int MAXIMUM_PEOPLE_CAN_WAIT = 5;

    public static int NUMBER_OF_CHECKOUT_TILL = 4;
    public static int NUMBER_OF_CUSTOMER = 25;
    public static int TILL_LENGTH = 7;
    public static int MAXIMUM_LOOK_TIMES = 5;
    public static int MAXIMUM_PEOPLE_CAN_WAIT = 5;
//
//
    public static List<CheckoutTill> CHECKOUT_TILL_LIST = new ArrayList<CheckoutTill>();
    public static List<Consumer> consumerList = new ArrayList<>();
    public static GridPane groupRoot = new GridPane();
    public static FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 5, 5);

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
//        groupRoot.setVgap(10);
        groupRoot.setHgap(80);
        Supermarket.groupRoot.setGridLinesVisible(true);
        groupRoot.setPadding(new Insets(5));
//        Supermarket.groupRoot.add(new Line(),0,0, 12,12);
        final int numCols = 12 ;
        final int numRows = 12 ;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
//            colConst.setPercentWidth(100.0 / numCols);
            colConst.setPrefWidth(10);
            groupRoot.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
//            rowConst.setPercentHeight(100.0 / numRows);
            rowConst.setPrefHeight(50);
            groupRoot.getRowConstraints().add(rowConst);
        }

        StackPane shoppingAreaStackPane = new StackPane();
        Label label = new Label("Shopping");
        Rectangle rectangle = new Rectangle(80*5,50);
        rectangle.setFill(Color.ORANGE);
        shoppingAreaStackPane.getChildren().addAll(rectangle,label);
        shoppingAreaStackPane.setAccessibleText("Shopping area");
        groupRoot.add(shoppingAreaStackPane,6,10);

        flowPane.setPadding(new Insets(5));

        for (int i=1; i<=20; i++) {
            flowPane.getChildren().add(new Button(String.valueOf(i)));
        }
        //columnindex: 6th column, colspan: add 4 cols more
        groupRoot.add(flowPane,6,8,4,1);


        Scene scene = new Scene(groupRoot, 1200,768);
        primaryStage.setTitle("Sample Long");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
