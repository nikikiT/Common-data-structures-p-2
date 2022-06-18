import dictionary.close.CloseHashDictionary;

public class Main {
    public static void main(String[] args){

        char[] an = new char[]{'a','n'};
        char[] na = new char[]{'n','a'};

        char[] xy = new char[]{'x','y'};
        char[] yx = new char[]{'y','x'};

        CloseHashDictionary dict = new CloseHashDictionary(10);

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
