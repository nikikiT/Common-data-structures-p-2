//package dictionary.close;
//
//public class CloseHashDictionary {
//
//    private final int SIZE_OF_NAME = 10;
//
//    private char[][] dictPool; // Это массив массивов - то есть массив которых хранит только имена.
//
//    public CloseHashDictionary(int poolSize){
//        dictPool = new char[poolSize][];
//    }
//
//    private int hashFunction(char[] name, int probeNumber){ //probeNumber - зависит от insert в зависимости от того пусто ли в позиции (нужно ли последующее повторное хэширование). Если не нужно, то probeNumber=0;
//        int sum = probeNumber;
//        for (int i = 0; i < name.length; i++)
//            sum += name[i];   //Достаем код символа
//
//        sum = sum % dictPool.length;
//        return  sum;
//    }
//
//    public void insert(char[] name) {
//        if (name == null || name.length > SIZE_OF_NAME)
//            return;
//
//        int hash = hashFunction(name, 0); // 0 - (поскольку неизвестно нужно повторное или нет)
//        int probeCount = 0;
//        int deletedPos = -1;
//
//        if (dictPool[hash] == null) { //Если в массиве нет массива
//            dictPool[hash] = new char[SIZE_OF_NAME];
//            copyCharArray(name, dictPool[hash]);
//            return;
//
//        } else { //Если в массиве либо было что-то удалено либо что-то еще есть
//
//            if(isDeleted(dictPool[hash])){ //Если уже есть элемент, то
//                copyCharArray(name, dictPool[hash]);
//            } else {
//
//                while(){
//
//
//                }
//            }
//
//
//        }
//
////        if (deletedPos == -1 && isDeleted(dictPool[hash]))
////            deletedPos = hash;
////        else hash = hashFunction(name, probeCount++);
////
////        if (deletedPos != -1) {
////            copyCharArray(name, dictPool[deletedPos]);
////        }
//    }
//
//    private boolean isDeleted(char[] name){
//        return name[0] == '\u0000';
//    }
//
//    private void copyCharArray(char[] from, char[] into){
//        for (int i = 0; i < from.length; i++)
//            into[i]=from[i];
//    }
//
//    public void printDictionary(){
//        for (int i = 0; i < dictPool.length; i++)
//            printName(dictPool[i]);
//    }
//
//    private void printName(char[] name) {
//        if(name == null)
//            return;
//
//        int counter = 0;
//        System.out.print("Element: ");
//        for (int i = 0; i < name.length; i++)
//            if (name[i] != '\u0000')
//                System.out.print(name[i]);
//            else counter++;
//
//        if (counter != 10) System.out.println();
//    }
//
//}
