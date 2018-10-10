package application.model;

import application.ANSIColor;
import application.Supermarket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static application.Supermarket.CHECKOUT_TILL_LIST;

/**
 * Created by Long laptop on 9/24/2018.
 */
public class Customer {


    private int customerId;
    private List<Product> productList;
    private int waitTime;
    private int maxPeopleCanWait; //The number which this customer can wait
    private boolean enteredQueue =false;
    private int lookTimes;

    /**
     * Create a customer with random product list and random maximum people he can wait in a queue
     */
    public Customer() {
        productList = new ArrayList<Product>();
        customerId= (int )(Math. random() * 1000 + 1);

        Random rand = new Random();
        int  n = rand.nextInt(10) + 2;
        for (int i = 0; i < n; i++) {
            productList.add(new Product());
        }
//        maxPeopleCanWait = rand.nextInt(3) + 1;
        maxPeopleCanWait = Supermarket.MAXIMUM_PEOPLE_CAN_WAIT;
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
                System.out.println(this.customerId+ " look again for "+ lookTimes);
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
        if(lookTimes == Supermarket.MAXIMUM_LOOK_TIMES && !enteredQueue) {
            leaveSupermarket();
        }
    }

    //after enter, must go out
    public boolean tryEnterQueue() throws InterruptedException {
//        for (int i = 0; i < CHECKOUT_TILL_LIST.size() && enteredQueue == false; i++) {
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


        boolean queueResult = CHECKOUT_TILL_LIST.get(0).enqueue(this, maxPeopleCanWait);
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