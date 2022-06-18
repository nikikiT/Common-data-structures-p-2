package dictionary.close;

public class CloseHashDictionary {

    private Element[] dictPool;
    final int NAME_SIZE=10;

    public CloseHashDictionary(int poolSize){
        dictPool = new Element[poolSize];
    }

    class Element{
        boolean isDeleted;

        char[] name;

        public void setName(char[] name) {
            for (int i = 0; i < name.length; i++) {
                this.name[i]=name[i];
            }
        }

        public void setDeleteName(){
            for (int i = 0; i < this.name.length; i++)
                name[i]='\u0000';
            this.isDeleted=true;
        }

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

        if (dictPool[hash] == null) {
            dictPool[hash] = new Element(name);
            copyCharArray(name,dictPool[hash].name);
            return;
        }

        if(dictPool[hash].isDeleted){ //Если элемент есть но удалены данные из него
            dictPool[hash].isDeleted=false;
            copyCharArray(name, dictPool[hash].name);
            return;
        }

        int newHash = hashFunction(name, hash+1);
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
            newHash = hashFunction(name,newHash+1);
        }
        System.out.println("Словарь полон! Невозможно вставить");
    }

    public void delete(char[] name) {
        if (name == null || name.length > NAME_SIZE)
            return;
        int code = search(name);
        if (code != -1)
            dictPool[code].setDeleteName(); //Для каждого элемента имени устанавливает символ '\u0000' и isDeleted = true
    }

    public boolean member(char[] name){
        if(name==null||name.length>NAME_SIZE)
            return false;
        return search(name) != -1;
    }

    // -1 - нет в словаре. Если больше -1 - найден
    private int search(char[] name){
        int hash = hashFunction(name,0);
        int start = hash;
        int counter = 0;
        hash = hashFunction(name, ++counter);

        while (dictPool[hash] != null || hash != start){
            if (compareNames(dictPool[hash].name, name)) {
                return hash;
            }
            hash = hashFunction(name, ++counter);
        }
        return -1;
    }

    private boolean compareNames(char[] a, char[] b){
        for (int i = 0; i < a.length; i++)
            if(a[i]!=b[i])
                return false;
        return true;
    }

    public void makeNull(){
        for (int i = 0; i < dictPool.length; i++)
            dictPool[i]=null;
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
