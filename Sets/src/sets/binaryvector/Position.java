package sets.binaryvector;

public class Position {
    private int indexInBlock; //относительно start
    private int blockNumber; //Номер самого инта или блока в массиве

    public Position( int blockNumber, int indexInBlock) {
        this.indexInBlock = indexInBlock;
        this.blockNumber = blockNumber;
    }

    public void setIndexInBlock(int indexInBlock) {
        this.indexInBlock = indexInBlock;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getIndexInBlock() {
        return indexInBlock;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    @Override
    public String toString() {
        return "Position{" +
                "indexInBlock=" + indexInBlock +
                ", blockNumber=" + blockNumber +
                '}';
    }
}
