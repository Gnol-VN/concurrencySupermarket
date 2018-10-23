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
    public static int NUMBER_OF_CHECKOUT_TILL = 4;
    public static int NUMBER_OF_CUSTOMER = 25;
    public static int TILL_LENGTH = 7;
    public static int MAXIMUM_LOOK_TIMES = 5;
    public static int MAXIMUM_PEOPLE_CAN_WAIT = 5;
    public static List<CheckoutTill> CHECKOUT_TILL_LIST = new ArrayList<CheckoutTill>();
    public static List<Consumer> consumerList = new ArrayList<>();

    public static void main(String[] args)  throws InterruptedException {
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
        launch(args);
    }

    public static void createTIll(){
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            CheckoutTill checkoutTill = new CheckoutTill(new Scanner());
            checkoutTill.setWorkingStatus(true);
            CHECKOUT_TILL_LIST.add(checkoutTill);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane groupRoot = new GridPane();
        groupRoot.setVgap(8);
        groupRoot.setHgap(8);
        groupRoot.setPadding(new Insets(5));
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setHgrow(Priority.NEVER);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setHgrow(Priority.ALWAYS);
        groupRoot.getColumnConstraints().add(columnConstraints1);
        groupRoot.getColumnConstraints().add(columnConstraints2);

        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setVgrow(Priority.NEVER);
        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setVgrow(Priority.ALWAYS);
        groupRoot.getRowConstraints().addAll(rowConstraints1,rowConstraints2);

        TextField textField = new TextField("Text area");
        TextArea textArea = new TextArea();
        Rectangle rectangle = new Rectangle(50,50);

        groupRoot.add(textField,0,0);
        groupRoot.add(rectangle,2,0);
        groupRoot.add(textArea,0,1,4,1);


        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(rectangle);
        rotateTransition.setDuration(Duration.millis(2000));
        rotateTransition.setByAngle(90);
        rotateTransition.setCycleCount(5);
        rotateTransition.play();

        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setByX(1.5);
        scaleTransition.setByY(1.5);
        scaleTransition.setCycleCount(20);
        scaleTransition.setDuration(Duration.millis(2000));
        scaleTransition.setAutoReverse(true);
        scaleTransition.setNode(rectangle);
//        scaleTransition.play();

        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setByX(300);
        translateTransition.setByY(0);
        translateTransition.setCycleCount(5);
        translateTransition.setNode(groupRoot);
        translateTransition.setDuration(Duration.millis(2000));
//        translateTransition.play();



        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event click");
            }
        });
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println("pressed key");
            }
        });

//        groupRoot.getChildren().add(rectangle);
//        groupRoot.getChildren().add(textField);
        Scene scene = new Scene(groupRoot, 600,300);
        primaryStage.setTitle("Sameple Long");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
