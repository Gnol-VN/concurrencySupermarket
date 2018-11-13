package application.model;

import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

import static application.Supermarket.*;

public class PeriodCounter extends Thread {
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SUPERMARKET_WORKING_TIME++;
            for (CheckoutTill checkoutTill : CHECKOUT_TILL_LIST) {
                if (checkoutTill.isWorkingStatus() == false) {
                    if (checkoutTill.getStopWatch().isRunning() == false) {
                        checkoutTill.getStopWatch().start();
                    }
                } else {
                    try {
                        checkoutTill.getStopWatch().stop();
                    } catch (Exception e) {

                    }
                }

                Platform.runLater(new MyRunnable(checkoutTill) {
                    @Override
                    public void run() {
                        if (checkoutTill.getCheckoutId() == 1) {
                            String runningTime = String.valueOf(checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS));
                            String utilization = String.valueOf(Integer.valueOf(runningTime)/SUPERMARKET_WORKING_TIME);
                            LABEL_UTIL_1.setText("Running for " + runningTime + " Utilization: "+ utilization);
                        }
                        if (checkoutTill.getCheckoutId() == 2) {
                            String runningTime = String.valueOf(checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS));
                            String utilization = String.valueOf(Integer.valueOf(runningTime)/SUPERMARKET_WORKING_TIME);
                            LABEL_UTIL_2.setText("Running for " + runningTime + " Utilization: "+ utilization);
                        }
                        if (checkoutTill.getCheckoutId() == 3) {
                            String runningTime = String.valueOf(checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS));
                            String utilization = String.valueOf(Integer.valueOf(runningTime)/SUPERMARKET_WORKING_TIME);
                            LABEL_UTIL_3.setText("Running for " + runningTime + " Utilization: "+ utilization);
                        }
                        if (checkoutTill.getCheckoutId() == 4) {
                            String runningTime = String.valueOf(checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS));
                            String utilization = String.valueOf(Integer.valueOf(runningTime)/SUPERMARKET_WORKING_TIME);
                            LABEL_UTIL_4.setText("Running for " + runningTime + " Utilization: "+ utilization);
                        }
                        if (checkoutTill.getCheckoutId() == 5) {
                            String runningTime = String.valueOf(checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS));
                            String utilization = String.valueOf(Integer.valueOf(runningTime)/SUPERMARKET_WORKING_TIME);
                            LABEL_UTIL_5.setText("Running for " + runningTime + " Utilization: "+ utilization);
                        }
                        if (checkoutTill.getCheckoutId() == 6) {
                            String runningTime = String.valueOf(checkoutTill.getStopWatch().elapsed(TimeUnit.SECONDS));
                            String utilization = String.valueOf(Integer.valueOf(runningTime)/SUPERMARKET_WORKING_TIME);
                            LABEL_UTIL_6.setText("Running for " + runningTime + " Utilization: "+ utilization);
                        }
                        LABEL_WORKING_TIME.setText(String.valueOf(SUPERMARKET_WORKING_TIME));
                    }
                });


            }
        }
    }
}
