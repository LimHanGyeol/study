package vendingmachine;

public class VendingMachine {

    private int changes;

    public VendingMachine() {
        this(0);
    }

    public VendingMachine(int changes) {
        this.changes = changes;
    }

    public void put(int money) {
        this.changes += money;
    }

    public void withdraw(int money) {
        int change = this.changes - money;
        if (change < 0) {
            throw new IllegalStateException();
        }
        this.changes -= money;
    }

    public int getChanges() {
        return changes;
    }
}
