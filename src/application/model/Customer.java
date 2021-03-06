package application.model;

import application.ANSIColor;
import application.Supermarket;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

import static application.Supermarket.*;

/**
 * Created by Long laptop on 9/24/2018.
 */
public class Customer extends Thread {

    private int customerId;
    private static int idCount;
    private List<Product> productList;
    private int maxPeopleCanWait; //The number which this customer can wait
    private boolean enteredQueue = false;
    private int lookTimes;


    private int totalCheckingTime;
    private StackPane stackPane;

    /**
     * Create a customer with random product list and random maximum people he can wait in a queue
     */
    public Customer() {
        productList = new ArrayList<Product>();
        idCount++;
        customerId = idCount;
        int  n = RANDOM.nextInt(8) + 3;
        for (int i = 0; i < n; i++) {
            Product product = new Product();
            productList.add(product);
            totalCheckingTime = totalCheckingTime + product.getCheckoutProcessingTime();

        }
        maxPeopleCanWait = RANDOM.nextInt(7) +3;

        //Begin create UI
        StackPane stackPane = new StackPane();
        Label label = new Label("Customer: "+ customerId+ "\n"+productList.size());
        Rectangle rectangle = new Rectangle(80,50);
        rectangle.setFill(Color.ORANGE);
        stackPane.getChildren().addAll(rectangle,label);
        stackPane.setAccessibleText("Customer: "+ customerId);
        this.setStackPane(stackPane);
        //End UI
    }

    @Override
    public void run(){
        try {
            look();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Look for a suitable till to queue. A Suitable queue is OPEN-status, less than a maximum number of people.
     * Customers will leave supermarket if they can not find a suitable queue for [lookTimes]
     */
    public void look() throws InterruptedException {
        //Begin UI: stay at Waiting line
        Platform.runLater(new MyRunnable(this){
            @Override
            public void run() {
                WAITING_AREA_FLOWPANE.getChildren().add(this.getCustomer().getStackPane());
            }
        });
        //End UI

        //Calculate look time
        Thread.sleep(FIRST_LOOK_TIME); //First look time
        TOTAL_WAIT_TIME = TOTAL_WAIT_TIME + FIRST_LOOK_TIME;

        //Customer keeps looking when he hasn't enterd and he can endure for @MAXIMUM_LOOK_TIMES
        while(enteredQueue == false && lookTimes < MAXIMUM_LOOK_TIMES){
            synchronized (WORKING_OBJECT){
                while(WORKING_FLAG == false){
                    try {
                        WORKING_OBJECT.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    WORKING_OBJECT.notifyAll();

                }
            }
            lookTimes ++;
            TOTAL_WAIT_TIME = TOTAL_WAIT_TIME + LOOK_AGAIN_TIME;
            Platform.runLater(new MyRunnable(this){
                @Override
                public void run() {
                    LABEL_TOTAL_WAIT_TIME.setText("Total wait time "+String.valueOf(TOTAL_WAIT_TIME/1000) +"s");
                }
            });

            Thread.sleep(Supermarket.LOOK_AGAIN_TIME);
            if(lookTimes > 3){
                System.out.println(this.customerId+ " look again for "+ lookTimes + "times");
            }
            enteredQueue = tryEnterQueue();

        }

        //Leave if looktimes is exceeded
        if(lookTimes == Supermarket.MAXIMUM_LOOK_TIMES && !enteredQueue) {
            leaveSupermarket();
        }
    }

    /**
     * try to enter the queue if the queue
     * @return true if sucessfully entered queue
     * @throws InterruptedException
     */
    public boolean tryEnterQueue() throws InterruptedException {
        boolean queueResult = false;

        if(enteredQueue== false){
            List<Integer> numOfQueuingList = new ArrayList<>();

            //enter a queue with a smallest number of customers
            //Algorithms: loop through each queue, check if it is working.
            //then get the size (people) of each queue, then TRY to enter the smallest queue

            //Option 1: he has less than 6 products
            if(this.productList.size() <=5){
                for (CheckoutTill checkoutTill: CHECKOUT_TILL_LIST) {
                    if(checkoutTill.isWorkingStatus()){
                        int sizeOfThisQueue = 0;
                        if(checkoutTill.isLessThanFiveItems()){
                            sizeOfThisQueue = checkoutTill.getCustomerQueueList().size();
                        }
                        else{
                            sizeOfThisQueue = 99;
                        }
                        numOfQueuingList.add(sizeOfThisQueue);
                    }
                }
                int smallestQueueIndex = numOfQueuingList.indexOf(Collections.min(numOfQueuingList));
                queueResult = CHECKOUT_TILL_LIST.get(smallestQueueIndex)
                        .enqueue(this, maxPeopleCanWait);
            }


            //Option 2: he has more than 6 products
            else {
                for (CheckoutTill  checkoutTill: CHECKOUT_TILL_LIST) {
                    if(checkoutTill.isWorkingStatus()){
                        int sizeOfThisQueue = checkoutTill.getCustomerQueueList().size();
                        if(checkoutTill.isLessThanFiveItems()){
                            sizeOfThisQueue = 99;
                        }
                        numOfQueuingList.add(sizeOfThisQueue);
                    }
                }
                queueResult = CHECKOUT_TILL_LIST.get(numOfQueuingList.indexOf(Collections.min(numOfQueuingList)))
                        .enqueue(this, maxPeopleCanWait);
            }
        }

        return queueResult;
    }

    /**
     * Customer leave supermarket if he/she can not find a suitable queue
     */
    public void leaveSupermarket() throws InterruptedException {
        LEFT_CUSTOMER_NUMBER++;
        System.out.println(ANSIColor.ANSI_RED+ this.customerId+" left supermarket"
                + " due to long waiting" + ANSIColor.ANSI_RESET);
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setByX(800);
        translateTransition.setNode(this.getStackPane());
        translateTransition.setDuration(Duration.millis(2000));
        translateTransition.play();
        //To ensure this node has 2s to move before delete in UI
        Thread.sleep(2000);

        //Begin deletion in UI
        Platform.runLater(new MyRunnable(this){
            @Override
            public void run() {
                LABEL_LEFT_CUSTOMER.setText("Left customer: " + String.valueOf(LEFT_CUSTOMER_NUMBER));
                Supermarket.GROUP_ROOT.getChildren().remove(this.getCustomer().getStackPane());
                Supermarket.WAITING_AREA_FLOWPANE.getChildren().remove(this.getCustomer().getStackPane());
            }
        });
        //End UI deletion
//
    }



//Getter & Setter


    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }
    public int getMaxPeopleCanWait() {
        return maxPeopleCanWait;
    }

    public void setMaxPeopleCanWait(int maxPeopleCanWait) {
        this.maxPeopleCanWait = maxPeopleCanWait;
    }

    public int getLookTimes() {
        return lookTimes;
    }

    public void setLookTimes(int lookTimes) {
        this.lookTimes = lookTimes;
    }

    public boolean isEnteredQueue() {
        return enteredQueue;
    }

    public void setEnteredQueue(boolean enteredQueue) {
        this.enteredQueue = enteredQueue;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }


    public int getTotalCheckingTime() {
        return totalCheckingTime;
    }

    public void setTotalCheckingTime(int totalCheckingTime) {
        this.totalCheckingTime = totalCheckingTime;
    }

}
