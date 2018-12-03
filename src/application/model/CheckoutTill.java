package application.model;


import application.ANSIColor;
import application.Supermarket;
import com.google.common.base.Stopwatch;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
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
    private List<Customer> customerQueueList;
    private StackPane tillStackPane;
    private FlowPane tillFlowPane;
    private Stopwatch stopWatch;
    private double utilization;

    public CheckoutTill() {
        idCount++;
        checkoutId = idCount;
        customerQueueList = new ArrayList<>();
        System.out.println("Added checkout till with checkoutId: " + checkoutId);
        stopWatch = new Stopwatch();

        //Set UI
        StackPane stackPane = new StackPane();
        Label label = new Label("Till " + idCount);
        label.setTextFill(Color.WHITE);
        Rectangle rectangle = new Rectangle(80, 50);
        rectangle.setFill(Color.GREEN);
        stackPane.getChildren().addAll(rectangle, label);
        stackPane.setAccessibleText("Till " + idCount);
        this.tillStackPane = stackPane;

        tillFlowPane = new FlowPane(Orientation.HORIZONTAL, 5, 5);
        //Begin UI change
        Platform.runLater(new MyRunnable(this) {
            @Override
            public void run() {
                Supermarket.GROUP_ROOT.add(stackPane, 0, getCheckoutId());

            }
        });
        //End UI change
    }

    /**
     * Add a customer in a queue if this till is working and customer can wait.
     * This is a producer pattern, which keep adding people to the queue.
     * If the queue is full, producer keeps WAITING until there is a slot available.
     *
     * @param customer         the customer trying to queue
     * @param maxNumberCanWait maximum number which this customer can wait
     * @return true if this customer has queued successfully
     */
    public synchronized boolean enqueue(Customer customer, int maxNumberCanWait) throws InterruptedException {
        if (workingStatus == true && customerQueueList.size() < maxNumberCanWait) {
            while (customerQueueList.size() >= Supermarket.TILL_LENGTH) { // if till is full, so wait to put in
                System.out.println("Called wait in enqueue method");
                wait();
            }
            customerQueueList.add(customer);
            System.out.println(ANSIColor.ANSI_GREEN + "---------->" + customer.getCustomerId()
                    + " entered till " + this.getCheckoutId() + ANSIColor.ANSI_RESET);

            //Begin UI change INCLUDING increasing LABEL_ENQUEUE_REQUESTED
            Platform.runLater(new MyRunnable(this) {
                @Override
                public void run() {
                    LABEL_ENQUEUE_REQUESTED.setText("Entered customer: " + String.valueOf(ENQUEUE_REQUESTED));
                    StackPane customerIcon = customer.getStackPane();
                    this.getCheckoutTill().tillFlowPane.getChildren().add(customerIcon);
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
     *
     * @throws InterruptedException
     */
    public synchronized void dequeue() throws InterruptedException {

        while (customerQueueList.isEmpty()) { //if empty, wait for something to kick out
//                System.out.println("Called wait in dequeue method");
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
        Platform.runLater(new MyRunnable(this, customer) {
            @Override
            public void run() {
                LABEL_DEQUEUE_SUCCESSED.setText("Dequeued customer: " + String.valueOf(DEQUEUE_SUCCESSED));
                Supermarket.GROUP_ROOT.getChildren().remove(this.getCustomer().getStackPane());
                this.getCheckoutTill().tillFlowPane.getChildren().remove(customer.getStackPane());
                Supermarket.WAITING_AREA_FLOWPANE.getChildren().remove(this.getCustomer().getStackPane());
            }
        });
        //End UI change
        customerQueueList.remove(0);
        System.out.println(ANSIColor.ANSI_BLACK + customerToBeRemoved
                + " finished at till " + this.getCheckoutId() + ANSIColor.ANSI_RESET);
        notifyAll();

    }

    //Getter & Setter


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

    public StackPane getTillStackPane() {
        return tillStackPane;
    }

    public void setTillStackPane(StackPane tillStackPane) {
        this.tillStackPane = tillStackPane;
    }

    public double getUtilization() {
        return utilization;
    }

    public void setUtilization(double utilization) {
        this.utilization = utilization;
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        CheckoutTill.idCount = idCount;
    }

    public FlowPane getTillFlowPane() {
        return tillFlowPane;
    }

    public void setTillFlowPane(FlowPane tillFlowPane) {
        this.tillFlowPane = tillFlowPane;
    }

    public Stopwatch getStopWatch() {
        return stopWatch;
    }

    public void setStopWatch(Stopwatch stopWatch) {
        this.stopWatch = stopWatch;
    }

}
