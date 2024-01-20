package mortgage;

import java.util.HashMap;
import java.util.Map;

import static mortgage.MortgageUtils.formatMoney;

class Mortgage {
    private static int counter = 0;

    private String name;
    private String currency;
    private double monthRate;
    private double initialDebt;
    private double currentDebt;
    private double initialMonthPayment;
    private int initialDuration;
    private int currentDuration;
    private double totalPayments = 0.0;
    private Map<Integer, Double> earlyRepayments = new HashMap<>();

    public Mortgage(String currency, double yearRate, double initialDebt, int initialDuration, Map<Integer, Double> earlyRepayments) {
        counter++;
        this.name = "Mortgage #" + counter;
        this.currency = currency;
        this.monthRate = yearRate / 12 / 100;
        this.initialDebt = initialDebt;
        this.currentDebt = initialDebt;
        this.initialDuration = initialDuration;
        this.earlyRepayments = earlyRepayments;
    }

    public void addRegularPayment(double sum, boolean logging) {
        totalPayments += sum;

        //rate part:
        double ratePart = currentDebt * monthRate;
        double basePart = sum - ratePart;   //is subtracted from the restDebt
        currentDebt -= basePart;
        if (logging) {
            System.out.println("month # " + currentDuration +
                    "\t basePart = " + (int) basePart +
                    "\t ratePart = " + (int) ratePart +
                    "\t monthPayment = " + (int) sum +
                    "\t currentDebt = " + (int) currentDebt);
        }
    }

    /**
     * returns 1 if currentDebt = 0
     * returns 0 otherwise
     */
    public int addEarlyPayment(double sum, boolean logging) {
        if (sum >= currentDebt) {
            if (logging) {
                System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("NOTE: early payment = " + sum + " is greater or equals currentDebt = " + currentDebt + "!");
                System.out.println("This payment finishes the mortgage!");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            totalPayments += currentDebt;
            currentDebt = 0;
            return 1;
        } else {
            totalPayments += sum;
            currentDebt -= sum;
            if (logging) {
                System.out.println(">>>>> add Early Payment = " + sum);
            }
            return 0;
        }
    }

    public double getOverpayment() {
        if (totalPayments == 0) return 0;
        return totalPayments - initialDebt;
    }

    public double getOverpaymentPercents() {
        if (totalPayments == 0) return 0;
        return Math.round(100 * (Math.round(100 * totalPayments / initialDebt) / 100.0 - 1));
    }

    /**
     * Setters and Getters - START
     */
    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getMonthRate() {
        return monthRate;
    }

    public void setMonthRate(double monthRate) {
        this.monthRate = monthRate;
    }

    public double getInitialDebt() {
        return initialDebt;
    }

    public double getCurrentDebt() {
        return currentDebt;
    }

    public double getInitialMonthPayment() {
        return initialMonthPayment;
    }

    public void setInitialMonthPayment(double initialMonthPayment) {
        this.initialMonthPayment = initialMonthPayment;
    }

    public int getInitialDuration() {
        return initialDuration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public Map<Integer, Double> getEarlyRepayments() {
        return earlyRepayments;
    }

    /**
     * Setters and Getters - END
     */

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\n\tname = ").append(name)
                .append("\n\tyearRate = ").append(Math.round(10 * monthRate * 12 * 100) / 10.0).append(" %")
                .append("\n\tinitialDebt = ").append(formatMoney(initialDebt)).append(" ").append(currency)
                .append("\n\tinitialDuration = ").append(initialDuration).append(" months")
                .append("\n\tinitialMonthPayment = ").append(formatMoney(initialMonthPayment)).append(" ").append(currency)
                .append("\n\ttotalPayments = ").append(formatMoney(totalPayments)).append(" ").append(currency)
                .append("\n\toverpayment = ").append(formatMoney(getOverpayment())).append(" ").append(currency)
                .append("\n\toverpayment = ").append(getOverpaymentPercents()).append(" %")
                .append("\n\tduration = ").append(currentDuration).append(" months")
                .append("\n\tearlyRepayments = ").append(earlyRepaymentsToString())
                .append("\n}");

        return sb.toString();
    }

    private String earlyRepaymentsToString() {
        if (earlyRepayments.isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (Map.Entry<Integer, Double> e : earlyRepayments.entrySet()) {
            sb.append("\n\t\tmonth #").append(e.getKey()).append(": ").append(formatMoney(e.getValue())).append(" ").append(currency);
        }
        sb.append("\n\t]");

        return sb.toString();
    }
}