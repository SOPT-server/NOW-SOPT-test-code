package christmas.application.info;


public abstract class EventResultInfo {

    public record DateInfo(
            int month,
            int date
    ) {
    }


    public record ReceiveMenu(
            String name,
            Integer quantity
    ) {
    }

    public record Amount(
        int amount
    ) {
    }

    public record Benefit(

            String name,
            Integer discountAmount
    ) {
    }


    public record Badge(
            String badgeName
    ) {
    }

}


