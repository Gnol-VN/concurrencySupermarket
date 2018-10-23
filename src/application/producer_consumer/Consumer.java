package application.producer_consumer;

import application.Supermarket;
import application.model.CheckoutTill;

/**
 * Created by Long laptop on 10/9/2018.
 */
public class Consumer extends Thread {
    private CheckoutTill checkoutTill ;

    public Consumer(int tillIndex) {
        checkoutTill = Supermarket.CHECKOUT_TILL_LIST.get(tillIndex);
    }

    /**
     * Sleep 2000ms to make sure the Consumer starts after Producer
     * Sleep 700ms to stimulate the checking time
     */
    public void run(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(!checkoutTill.getCustomerQueueList().isEmpty()){
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                checkoutTill.dequeue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
