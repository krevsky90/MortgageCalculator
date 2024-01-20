package mortgage;

import java.util.List;

public class MortgageInit {
    private static final MortgageController mortgageController = new MortgageController();
    private static final MortgageLoader mortgageLoader = new MortgageLoader();
    private static final boolean loggingEnabled = false;

    /**
     * info: https://mortgage-calculator.ru/%D1%84%D0%BE%D1%80%D0%BC%D1%83%D0%BB%D0%B0-%D1%80%D0%B0%D1%81%D1%87%D0%B5%D1%82%D0%B0-%D0%B8%D0%BF%D0%BE%D1%82%D0%B5%D0%BA%D0%B8/
     */
    public static void main(String[] args) {
        List<Mortgage> mortgages = mortgageLoader.loadAllMortgages();
        mortgageController.processBulk(mortgages, loggingEnabled);
        for (Mortgage mortgage : mortgages) {
            System.out.println(mortgage);
        }
    }
}