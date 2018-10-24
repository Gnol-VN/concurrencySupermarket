package application.model;

import application.ANSIColor;
import application.Supermarket;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static application.Supermarket.CHECKOUT_TILL_LIST;

/**
 * Created by Long laptop on 9/24/2018.
 */
public class Customer {


    private int customerId;
    private static int idCount;
    private List<Product> productList;
    private int waitTime;
    private int maxPeopleCanWait; //The number which this customer can wait
    private boolean enteredQueue =false;
    private int lookTimes;
    private StackPane stackPane;

    /**
     * Create a customer with random product list and random maximum people he can wait in a queue
     */
    public Customer() {
        productList = new ArrayList<Product>();
        idCount++;
//        customerId= (int )(Math. random() * 1000 + 1);
        customerId = idCount;
        Random rand = new Random();
        int  n = rand.nextInt(10) + 2;
        for (int i = 0; i < n; i++) {
            productList.add(new Product());
        }
//        maxPeopleCanWait = rand.nextInt(3) + 1;
        maxPeopleCanWait = Supermarket.MAXIMUM_PEOPLE_CAN_WAIT;

        //Begin create UI
        StackPane stackPane = new StackPane();
        Label label = new Label("Customer: "+ customerId);
        Rectangle rectangle = new Rectangle(80,50);
        rectangle.setFill(Color.ORANGE);
        stackPane.getChildren().addAll(rectangle,label);
        stackPane.setAccessibleText("Customer: "+ customerId);
        this.setStackPane(stackPane);
        //End UI
    }


    /**
     * Look for a suitable till to queue. A Suitable queue is OPEN-status, less than a maximum number of people.
     * Customers will leave supermarket if they can not find a suitable queue for [lookTimes]
     */
    public void look() throws InterruptedException {
        while(enteredQueue == false && lookTimes < Supermarket.MAXIMUM_LOOK_TIMES){
            lookTimes ++;
            if(lookTimes >3 ){
                Thread.sleep(100);
                System.out.println(this.customerId+ " look again for "+ lookTimes + "times");
            }
            enteredQueue = tryEnterQueue();
            //Look for a suitable queue
//            for (int i = 0; i < CHECKOUT_TILL_LIST.size() && enteredQueue == false; i++) {
//                boolean tillStatus = CHECKOUT_TILL_LIST.get(i).isWorkingStatus();
//                int currentNumberInTill = CHECKOUT_TILL_LIST.get(i).getCustomerQueueList().size();
//                if(tillStatus == true &&  currentNumberInTill < maxPeopleCanWait){
//                    CHECKOUT_TILL_LIST.get(i).getCustomerQueueList().add(this);
//                    Thread.sleep(1000);
//                    enteredQueue = true;
//                    tryEnterQueue();
//                    CHECKOUT_TILL_LIST.get(i).getCustomerQueueList().remove(this);
//                    break;
//                }
//            }
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
        for (int i = 0; i < CHECKOUT_TILL_LIST.size() && enteredQueue == false; i++) {
            queueResult = CHECKOUT_TILL_LIST.get(i).enqueue(this, maxPeopleCanWait);
            if(queueResult) return true;
        }

//        queueResult = CHECKOUT_TILL_LIST.get(0).enqueue(this, maxPeopleCanWait);
        return queueResult;
    }

    /**
     * Customer leave supermarket if he/she can not find a suitable queue
     */
    public void leaveSupermarket(){
        System.out.println(ANSIColor.ANSI_RED+ this.customerId+" left supermarket"
                + " due to long waiting" + ANSIColor.ANSI_RESET);
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

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }



}
