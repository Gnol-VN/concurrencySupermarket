package application.model;
import application.Supermarket;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import static application.Supermarket.*;

/**
 * Till watcher is an object to capture the number of in-out customer
 */
public class TillWatcher extends Thread{
    /**
     * Till watcher will loop for each 4000ms to capture the number of in-out customer
     * If the @TRADE_BALANCE is less than 1,2, till watcher will disable a till
     * else, till watchcer will open a disabled till.
     */
    @Override
    public void run(){
        int indexOfTill;
        //Wait for 5 seconds for the supermarket to run properly, and loop for each 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        indexOfTill = NUMBER_OF_CHECKOUT_TILL - 1;

        while (true){
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TRADE_BALANCE = ENQUEUE_REQUESTED/DEQUEUE_SUCCESSED;
            //Begin UI change
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    LABEL_SCALE.setText(String.valueOf(TRADE_BALANCE));
                }
            });
            //End UI change

            if(TRADE_BALANCE < 1.2 && indexOfTill > 1){
                Supermarket.CHECKOUT_TILL_LIST.get(indexOfTill).setWorkingStatus(false);
                Shape tillRectangle = (Shape) Supermarket.CHECKOUT_TILL_LIST.get(indexOfTill).getTillStackPane().getChildren().get(0);
                tillRectangle.setFill(Color.RED);
                System.out.println("Till " + indexOfTill+ "closed");
                indexOfTill--;
            }
            if(TRADE_BALANCE >= 1.2){
                if(indexOfTill<NUMBER_OF_CHECKOUT_TILL -1){

                    indexOfTill++ ;
                    Supermarket.CHECKOUT_TILL_LIST.get(indexOfTill).setWorkingStatus(true);

                    Shape shape = (Shape) Supermarket.CHECKOUT_TILL_LIST.get(indexOfTill).getTillStackPane().getChildren().get(0);
                    shape.setFill(Color.GREEN);
                    System.out.println("Till " + indexOfTill+ "Open");
                }
            }

        }
    }
}
