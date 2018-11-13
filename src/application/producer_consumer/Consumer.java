package application.producer_consumer;

import application.ANSIColor;
import application.Supermarket;
import application.model.CheckoutTill;
import static application.Supermarket.*;
/**
 * Created by Long laptop on 10/9/2018.
 */
public class Consumer extends Thread {
    private CheckoutTill checkoutTill ;

    public Consumer(int tillIndex) {
        checkoutTill = Supermarket.CHECKOUT_TILL_LIST.get(tillIndex);
        this.setPriority(10);
    }

    /**
     * Sleep CONSUMER_START_AFTER_PRODUCER_TIMEms to make sure the Consumer starts after Producer
     * Sleep 700ms to stimulate the checking time
     */
    public void run(){
        try {
            Thread.sleep(CONSUMER_START_AFTER_PRODUCER_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while(!checkoutTill.getCustomerQueueList().isEmpty()){
        while(true){
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
            try {
                int checkingOutTime = 3000; //simulation
                checkingOutTime = checkoutTill.getCustomerQueueList().get(0).getTotalCheckingTime();
                if(NEW_SCANNER) checkingOutTime= checkingOutTime /3;
                Thread.sleep(checkingOutTime);
                checkoutTill.dequeue();

            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
//        System.out.println(ANSIColor.ANSI_YELLOW+Thread.currentThread().getName()+"finish"+ANSIColor.ANSI_RESET);
    }
}
