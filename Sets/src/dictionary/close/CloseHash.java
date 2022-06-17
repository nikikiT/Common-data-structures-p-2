package dictionary.close;

public class CloseHash {

    private Element[] dictPool;
    final int NAME_SIZE=10;

    public CloseHash(int poolSize){
        dictPool = new Element[poolSize];
    }

    class Element{
        boolean isDeleted;

        char[] name;

        public Element(char[] name){
            this.name = new char[NAME_SIZE];
            copyCharArray(name, this.name);
            isDeleted=false;
        }

        public void printElement() {
            for (int i = 0; i < NAME_SIZE; i++)
                System.out.print(this.name[i]);
        }
    }

    public void insert(char[] name){
        if(name == null || name.length > NAME_SIZE)
            return;

        int hash = hashFunction(name, 0); // 0 - (поскольку неизвестно нужно повторное или нет)
        int probeNumber = 0;

        if (dictPool[hash] == null) {
            dictPool[hash] = new Element(name);
            copyCharArray(name,dictPool[hash].name);
            return;
        }

        int newHash = hashFunction(name, hash+2);

        while(newHash!=hash){
            if (dictPool[newHash] == null) {
                dictPool[newHash] = new Element(name);
                copyCharArray(name,dictPool[newHash].name);
                return;
            }
            if(dictPool[newHash].isDeleted){ //Если элемент есть но удалены данные из него
                dictPool[newHash].isDeleted=false;
                copyCharArray(name, dictPool[newHash].name);
                return;
            }
            newHash = hashFunction(name,newHash+2);
        }
        System.out.println("Словарь полон! Невозможно вставить");
    }

    private void copyCharArray(char[] from, char[] into){
        for (int i = 0; i < from.length; i++)
            into[i]=from[i];
    }

    private int hashFunction(char[] name, int probeNumber){ //probeNumber - зависит от insert в зависимости от того пусто ли в позиции (нужно ли последующее повторное хэширование). Если не нужно, то probeNumber=0;
        int sum = probeNumber;
        for (int i = 0; i < name.length; i++)
            sum += name[i];   //Достаем код символа

        sum = sum % dictPool.length;
        return  sum;
    }

    public void printDict(){
        for (int i = 0; i < dictPool.length; i++) {
            if (dictPool[i]==null){
                System.out.println("Element "+i+" : NULL");
                continue;
            }
            System.out.print("Element "+i+" : ");
            dictPool[i].printElement();
        }
    }

}
