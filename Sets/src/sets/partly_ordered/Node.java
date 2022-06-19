package sets.partly_ordered;

public class Node {

    private int key;
    private Node next;
    private int count;
    private Follower trail;

    Node(int key, Node next, int count, Follower trail) {
        this.count = count;
        this.key = key;
        this.next = next;
        this.trail = trail;
    }

    public Node(int key, Node next, int count) {
        this.key = key;
        this.next = next;
        this.count = count;
        this.trail = null;
    }


    public void increment(){
        this.count++;
    }

    public void decrement(){
        this.count--;
    }



    public void setKey(int key) {
        this.key = key;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTrail(Follower trail) {
        this.trail = trail;
    }

    public int getKey() {
        return key;
    }

    public Node getNext() {
        return next;
    }

    public int getCount() {
        return count;
    }

    public Follower getTrail() {
        return trail;
    }
}
