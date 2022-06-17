import dictionary.close.CloseHash;
import sets.binaryvector.ArraySet;
import sets.binaryvector.ArraySetException;
import dictionary.open.OpenHashDictionary;

public class Main {
    public static void main(String[] args){

        char[] an = new char[]{'a','n'};
        char[] na = new char[]{'n','a'};

        char[] xy = new char[]{'x','y'};
        char[] yx = new char[]{'y','x'};

        CloseHash dict = new CloseHash(10);

        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);
        dict.insert(an);

        dict.printDict();

    }
}
