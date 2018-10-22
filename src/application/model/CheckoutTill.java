package application.model;


import application.ANSIColor;
import application.Supermarket;

import java.util.ArrayList;
import java.util.List;

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
    }

    /**
     * Add a customer in a queue if this till is working and customer can wait
     * This is a producer pattern, which keep adding people to the queue
     * If the queue is full, producer keeps waiting until there is a slot available
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
    public synchronized void dequeue() throws InterruptedException {
            while (customerQueueList.isEmpty()){ //if empty, wait for something to kick out
                System.out.println("Called wait in dequeue method");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        int customerToBeRemoved = customerQueueList.get(0).getCustomerId();
        customerQueueList.remove(0);
        System.out.println(ANSIColor.ANSI_BLACK + customerToBeRemoved
                + " finished at till "+ this.getCheckoutId()+ ANSIColor.ANSI_RESET );
        notifyAll();

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
