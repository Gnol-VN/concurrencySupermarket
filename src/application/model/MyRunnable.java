package application.model;

public abstract class MyRunnable implements Runnable {
    private CheckoutTill checkoutTill;
    public MyRunnable(CheckoutTill checkoutTill){
        this.checkoutTill = checkoutTill;
    }

    public CheckoutTill getCheckoutTill() {
        return checkoutTill;
    }
}
