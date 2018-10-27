package application.model;
import application.producer_consumer.Consumer;

import static application.Supermarket.*;
public class TillWatcher extends Thread{
    @Override
    public void run(){
        while (true){
//            if(SP)
            //Open till
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            CheckoutTill checkoutTill = new CheckoutTill(new Scanner());
//            checkoutTill.setWorkingStatus(true);
//            CHECKOUT_TILL_LIST.add(checkoutTill);
//            Consumer consumer = new Consumer();
//            consumerList.add(consumer);
//            consumer.start();

        }
    }
}
