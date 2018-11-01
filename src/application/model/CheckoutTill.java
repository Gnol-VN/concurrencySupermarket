package application.model;


import application.ANSIColor;
import application.Supermarket;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static application.Supermarket.*;

/**
 * Created by Long laptop on 9/24/2018.
 */
public class CheckoutTill {
    private static int idCount;
    private int checkoutId;
    private boolean workingStatus;
    private boolean lessThanFiveItems;
    private Scanner scanner;
    private List<Customer> customerQueueList;

    public CheckoutTill(Scanner scanner) {
        this.scanner = scanner;
        idCount++;
        checkoutId = idCount;
        customerQueueList = new ArrayList<>();
        System.out.println("Added checkout till with checkoutId: " + checkoutId);
        StackPane stackPane = new StackPane();
        Label label = new Label("Till "+idCount);
        Rectangle rectangle = new Rectangle(80,50);
        rectangle.setFill(Color.WHITE);
        stackPane.getChildren().addAll(rectangle,label);
        stackPane.setAccessibleText("Till "+idCount);
        //Begin UI change

        Platform.runLater(new MyRunnable(this){
            @Override
            public void run() {
                Supermarket.GROUP_ROOT.add(stackPane,0, getCheckoutId());

            }
        });
        //End UI change
    }

    /**
     * Add a customer in a queue if this till is working and customer can wait.
     * This is a producer pattern, which keep adding people to the queue.
     * If the queue is full, producer keeps waiting until there is a slot available.
     * @param customer the customer trying to queue
     * @param maxNumberCanWait maximum number which this customer can wait
     * @return true if this customer has queued successfully
     */
    public synchronized boolean enqueue(Customer customer, int maxNumberCanWait) throws InterruptedException {
        if(workingStatus == true && customerQueueList.size() < maxNumberCanWait){
            while(customerQueueList.size() >= Supermarket.TILL_LENGTH ){ // if till is full, so wait to put in
                System.out.println("Called wait in enqueue method");
                wait();
            }
            customerQueueList.add(customer);
            System.out.println(ANSIColor.ANSI_GREEN+ "---------->"+customer.getCustomerId()
                    + " entered till "+ this.getCheckoutId() + ANSIColor.ANSI_RESET );

            //Begin UI change INCLUDING increasing LABEL_ENQUEUE_REQUESTED
            Platform.runLater(new MyRunnable(this){
                @Override
                public void run() {
                    LABEL_ENQUEUE_REQUESTED.setText(String.valueOf(ENQUEUE_REQUESTED));
                    StackPane customerIcon = customer.getStackPane();
                    int col = this.getCheckoutTill().checkoutId;
                    int row =this.getCheckoutTill().customerQueueList.size();
                    Supermarket.GROUP_ROOT.add(customerIcon,row,col);
                }
            });
            //End UI change
            notifyAll();
            return true;
        }

        return false;
    }

    /**
     * Remove a customer from a queue
     * This is a consumer pattern, which keeps removing people from the queue
     * If there is no one, consumer keeps waiting for some one to remove
     * @throws InterruptedException
     */
    public void dequeue() throws InterruptedException {
        synchronized (this){

            while (customerQueueList.isEmpty()){ //if empty, wait for something to kick out
                System.out.println("Called wait in dequeue method");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Customer customer = customerQueueList.get(0);
            int customerToBeRemoved = customerQueueList.get(0).getCustomerId();
            DEQUEUE_SUCCESSED++;

            //Begin UI change
            Platform.runLater(new MyRunnable(customer){
                @Override
                public void run() {
                    LABEL_DEQUEUE_SUCCESSED.setText(String.valueOf(DEQUEUE_SUCCESSED));
                    Supermarket.GROUP_ROOT.getChildren().remove(this.getCustomer().getStackPane());
                    Supermarket.FLOW_PANE.getChildren().remove(this.getCustomer().getStackPane());
                }
            });
            //End UI change
            customerQueueList.remove(0);
            System.out.println(ANSIColor.ANSI_BLACK + customerToBeRemoved
                    + " finished at till "+ this.getCheckoutId()+ ANSIColor.ANSI_RESET );
            notifyAll();
        }

    }

    //Getter & Setter

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public List<Customer> getCustomerQueueList() {
        return customerQueueList;
    }

    public void setCustomerQueueList(List<Customer> customerQueueList) {
        this.customerQueueList = customerQueueList;
    }

    public int getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(int checkoutId) {
        this.checkoutId = checkoutId;
    }

    public boolean isWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(boolean workingStatus) {
        this.workingStatus = workingStatus;
    }

    public boolean isLessThanFiveItems() {
        return lessThanFiveItems;
    }

    public void setLessThanFiveItems(boolean lessThanFiveItems) {
        this.lessThanFiveItems = lessThanFiveItems;
    }


}
