package application.producer_consumer;

import application.model.Customer;
import static application.Supermarket.*;

/**
 * Created by Long laptop on 10/9/2018.
 */
public class Producer extends Thread {

    /**
     * Sleep 100ms to stimulate the entering people
     * Create customer and make this customer look for a queue
     */
    public void run(){
//        for (int i = 0; i < Supermarket.NUMBER_OF_CUSTOMER; i++) {
        this.setPriority(1);
        while (true){
            try {
                Thread.sleep(SPAWN_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//              Create customer and make this customer look for a queue
            Customer customer = new Customer();
            customer.start();
        }
    }

}
