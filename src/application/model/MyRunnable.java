package application.model;

public abstract class MyRunnable implements Runnable {
    private CheckoutTill checkoutTill;
    private Customer customer;

    public MyRunnable(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MyRunnable(CheckoutTill checkoutTill){
        this.checkoutTill = checkoutTill;
    }

    public CheckoutTill getCheckoutTill() {
        return checkoutTill;
    }
}
