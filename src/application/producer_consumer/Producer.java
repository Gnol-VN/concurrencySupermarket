package application.producer_consumer;

import application.Supermarket;
import application.model.CheckoutTill;
import application.model.Customer;

/**
 * Created by Long laptop on 10/9/2018.
 */
public class Producer extends Thread {

    /**
     * Sleep 300ms to stimulate the entering people
     */
    public void run(){
        for (int i = 0; i < Supermarket.NUMBER_OF_CUSTOMER; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
//                checkoutTill.enqueue(new Customer(), Customer.MAXIMUM_PEOPLE_CAN_WAIT);
                Customer customer = new Customer();
                customer.look();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
