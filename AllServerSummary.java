package cs2030.simulator;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;


class AllServerSummary {

    public static final int AVALIABLE = -1;
    public static final int FULL = Integer.MAX_VALUE;

    private final ArrayList<Integer> listOfServerStatus;

    AllServerSummary() {
        this.listOfServerStatus = new ArrayList<Integer>();
    }


    void addStatus(int status) {
        listOfServerStatus.add(status);
    }

    int getFirstAvaliableIndex() {
        int result = this.listOfServerStatus.indexOf(AllServerSummary.AVALIABLE);

        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

    int getFirstWaitingIndex() {
        int result = this.listOfServerStatus.indexOf(this.listOfServerStatus.stream().filter(x -> x != AllServerSummary.FULL).findFirst().get());

        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

    int getShortestWaitingIndex() {
        int result = this.listOfServerStatus.indexOf(this.listOfServerStatus.stream().filter(x -> x != AllServerSummary.FULL).min((x, y) -> x - y).get());
        if (result == -1) {
            throw new NoSuchElementException("No avaliable Server");
        }
        return result;
    }

}