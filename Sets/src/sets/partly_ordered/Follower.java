package sets.partly_ordered;

public class Follower {

    Node id;
    Follower next;

    public Follower(Node id, Follower next) {
        this.id = id;
        this.next = next;
    }

    public Follower(Node id) {
        this.id = id;
        this.next=null;
    }


}
