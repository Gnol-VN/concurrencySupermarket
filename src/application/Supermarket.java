package application;

import application.model.CheckoutTill;
import application.model.Scanner;
import application.producer_consumer.Consumer;
import application.producer_consumer.Producer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long laptop on 9/23/2018.
 */
public class Supermarket {
    //Constant
    public static int NUMBER_OF_CHECKOUT_TILL = 1;
    public static int NUMBER_OF_CUSTOMER = 20;
    public static int TILL_LENGTH = 5;
    public static int MAXIMUM_LOOK_TIMES = 5;
    public static int MAXIMUM_PEOPLE_CAN_WAIT = 5;
    public static List<CheckoutTill> CHECKOUT_TILL_LIST = new ArrayList<CheckoutTill>();


    public static void main(String[] args) throws InterruptedException {
        //Create checkout till
        createTIll();

        //Create producer and consumer
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        //Start producer and consumer
        producer.start();
        consumer.start();

//        consumer.join();
    }

    public static void createTIll(){
        for (int i = 0; i < NUMBER_OF_CHECKOUT_TILL; i++) {
            CheckoutTill checkoutTill = new CheckoutTill(new Scanner());
            checkoutTill.setWorkingStatus(true);
            CHECKOUT_TILL_LIST.add(checkoutTill);
        }
    }

}
