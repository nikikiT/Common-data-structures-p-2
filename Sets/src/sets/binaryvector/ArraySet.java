package sets.binaryvector;

import java.util.ArrayList;

public class ArraySet {

    private int start, end, zeroPosition; //zeroPosition - номер инта в котором находится начала оси X
    static final int leftBit = 0b10000000000000000000000000000000;
    private int[] dataPool;

    public ArraySet(int start, int end) {
        if (start >= end)
            return;

        this.start = start;
        this.end = end;

        if ((start >= 0)) {
            dataPool = new int[end / 32 - start / 32 + 1];// подробнее будет так (end/32 + 1) - (start/32 + 1) + 1, то есть количество интов до end минус количество интов до старта + 1
            zeroPosition = -1;
        } else if (end < 0) {
            dataPool = new int[-start / 32 - -end / 32 + 1];
            zeroPosition = -1;
        } else {
            int posLen = end / 32 + 1; // количество блоков памяти(интов) для положительных чисел
            int negLen = -start / 32 + 1; // количество блоков памяти(интов) для отрицательных чисел
            dataPool = new int[posLen + negLen];
            zeroPosition = negLen;
        }
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int[] getDataPool() {
        return dataPool;
    }

    public ArraySet() {
        this(-1, 1);
    }

    public void insert(int x) {
        if (x < start || x > end)
            return;
        Position position = findPosition(x);
        System.out.println("Element in " + position + " was inserted");
        dataPool[position.getBlockNumber()] |= (leftBit >>> position.getIndexInBlock());
    }

    public void delete(int x) {
        if (x < start || x > end)
            return;
        Position position = findPosition(x);
        System.out.println("Element in " + position + " was deleted");
        dataPool[position.getBlockNumber()] &= ~(leftBit >>> position.getIndexInBlock());
    }

    public ArraySet union(ArraySet b){
        ArraySet c;

        if(b==null){
            c = new ArraySet(start,end);
            for (int i = 0; i < c.dataPool.length; i++) {
                c.dataPool[i]=dataPool[i];
            }
        }
        c = new ArraySet(start<b.start ? start:b.start, end>b.end?end:b.end);

        for (int i = 0; i < c.dataPool.length; i++)
                c.dataPool[i] = dataPool[i] | b.dataPool[i];

        return c;
    }

    public ArraySet merge(ArraySet param) throws ArraySetException {

        if(param==null)
            throw new ArraySetException("Вы не подали множество");

        if((start< param.start && end < param.end) || (start>param.start && end>param.end))
            throw new ArraySetException("Множества не пересекаются");

        ArraySet c, a, b; //a - промежуток с большим стартом

        if (start <= param.start && end >= param.end) {
            c = new ArraySet(start, end);
            a=param;
            b=this;
        }else
        if (param.start <= start && param.end >= end) {
            c = new ArraySet(param.start, param.end);
            a=this;
            b=param;
        }else
        if (start < param.start) {
            c = new ArraySet(start, param.end);
            a = this;
            b = param;
        } else {
            c = new ArraySet(param.start, end);
            a = param;
            b = this;
        }


        int dif = (a.start < 0 ? (a.start / 32 - 1) : (b.start / 32)) - (b.start < 0 ? (b.start / 32 - 1) : (b.start / 32));

        for (int i = 0; i < c.dataPool.length; i++) {
            int operand = (i + dif) > 0 && (i + dif) < b.dataPool.length ? b.dataPool[i + dif] : 0;
            System.out.println(operand);
            c.dataPool[i] = a.dataPool[i] | operand;
        }
        return c;
    }

    public ArraySet intersection(ArraySet param) {
        if (start > param.end || param.start > end)
            return null;

        ArraySet c, a, b; //a - промежуток с большим стартом

        if (start <= param.start && end >= param.end) {
           c = new ArraySet(param.start, param.end);
           a=param;
           b=this;
        }else
        if (param.start <= start && param.end >= end) {
            c = new ArraySet(start, end);
            a = this;
            b = param;
        }else
        if (start < param.start) {
            c = new ArraySet(param.start, end);
            a = param;
            b = this;
        } else {
            c = new ArraySet(start, param.end);
            a = this;
            b = param;
        }

        int dif = (a.start < 0 ? (a.start / 32 - 1) : (b.start / 32)) - (b.start < 0 ? (b.start / 32 - 1) : (b.start / 32));

        for (int i = 0; i < c.dataPool.length; i++) {
            int operand = (i + dif) > 0 && (i + dif) < b.dataPool.length ? b.dataPool[i + dif] : 0;
            c.dataPool[i] = a.dataPool[i] & operand;
        }
        return c;
    }

    public ArraySet differences(ArraySet b) {
        ArraySet c = new ArraySet(start, end);
        int dif = (start < 0 ? (start / 32 - 1) : (start / 32)) - (b.start < 0 ? (b.start / 32 - 1) : (b.start / 32));

        for (int i = 0; i < c.dataPool.length; i++) {
            int operand = (i + dif) > 0
                       && (i + dif) < b.dataPool.length ? b.dataPool[i + dif] : 0;
            c.dataPool[i] = dataPool[i] & ~operand;
        }
        return c;
    }

    public int min() throws ArraySetException {
        for (int i = 0; i < dataPool.length; i++)
            if (dataPool[i] != 0) {
                int mask;
                for (int j = 0; j < 32; j++) {
                    mask = leftBit >> j; //Создание маски для сравнения
                    if ((dataPool[i] & mask) != 0) {
                        if (zeroPosition != -1)
                            return (((i - zeroPosition) * 32) + j);
                        else
                            return i * 32 + j - (start % 32) + start;
                    }
                }
            }
        throw new ArraySetException("Set is empty");
    }

    public int max() throws ArraySetException {
        for (int i = dataPool.length - 1; i >= 0; i--) {
            if (dataPool[i] != 0) {
                int mask;
                for (int j = 31; j >= 0; j--) {
                    mask = 1 << j;
                    if ((dataPool[i] & mask) != 0) {
                        if (zeroPosition != -1) {
                            return (((i - zeroPosition) * 32) + j);
                        }//start%32 - отстоит от нуля на
                        return i * 32 + j - (start % 32) + start;
                    }
                }
            }
        }
        throw new ArraySetException("Set is empty");
    }

    private Position findPosition(int x) {
        if (start > 0 || end < 0)
            return new Position(x / 32 - start / 32, x % 32);
        if (x >= 0)
            return new Position(x / 32 + zeroPosition, x % 32);

        return new Position(x / 32 + zeroPosition - 1, 32 + x % 32);
    }

    public void makeNull() {
        for (int i = 0; i < dataPool.length; i++)
            dataPool[i] = 0;
    }

    public boolean equal(ArraySet b) {
        if (b == null || b.getStart() != start || b.getEnd() != end)
            return false;
        for (int i = 0; i < dataPool.length; i++)
            if (dataPool[i] != b.dataPool[i])
                return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(super.toString() + " {" + "start=").append(start).append(", end=").append(end).append(", zeroPosition=").append(zeroPosition).append(", dataPool= ");

        for (int j : dataPool)
            b.append(getFormattedData(j)).append(" ");

        return b.toString();
    }

    public boolean member(int x) {
        if (x < start || x > end || isEmptySet())
            return false;
        return isInSet(findPosition(x));
    }

    private boolean isInSet(Position position) {
        int mask = leftBit >>> position.getIndexInBlock();
        return (dataPool[position.getBlockNumber()] & mask) != 0;
    }

    private boolean isEmptySet() {
        for (int j : dataPool) if (j != 0) return false;
        return true;
    }

    public ArraySet find(ArraySet b, int x) {
        if (isEmptySet() && b.isEmptySet())
            return null;

        if (!(x < start || x > end))
            if (isInSet(findPosition(x)))
                return this;

        if (!(x < b.start || x > b.end))
            if (b.isInSet(findPosition(x)))
                return b;

        return null;
    }

    public void assign(ArraySet b) {
        if (b == null || start != b.start || end != b.end) return;

        for (int i = 0; i < dataPool.length; i++)
            dataPool[i] |= b.dataPool[i];

    }

    private String getFormattedData(int n) {
        String zeroes = "00000000000000000000000000000000";
        String binary = Integer.toBinaryString(n);
        return zeroes.substring(0, 32 - binary.length()) + binary;
    }
}
