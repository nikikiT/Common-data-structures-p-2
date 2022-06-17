package dictionary.open;

import java.util.Arrays;

public class OpenHashDictionary {

    public static class Element {
        protected char[] name;
        protected Element next;

        public Element() {
            this.name = null;
            this.next = null;
        }

        public Element(char[] name, Element next) {
            this.name = new char[10];
            copyName(name, this.name);
            this.next = next;
        }

        public void setNext(Element next) {
            this.next = next;
        }

        public void setName(char[] name) {
            if(name ==null){
                this.name =null;
                return;
            }
            copyName(name,this.name);
        }

        public void copyName(char[] source, char[] destination){
            for (int i = 0; i < source.length; i++)
                destination[i]=source[i];
            /*for (int i = source.length; i < destination.length-1; i++) {
                destination[i]=0;
            }*/
        }

        @Override
        public String toString() {
            return "Element{" +
                    "name=" + Arrays.toString(name) +
                    ", next=" + next +
                    '}';
        }
    }

    private final int SIZE = 10;

    public OpenHashDictionary(int b){
        segmentTable = new Element[SIZE];
        for (int i = 0; i < segmentTable.length; i++) {
            segmentTable[i] = new Element();
        }
    }

    private Element[] segmentTable;

    public void delete(char[] name) {
        if (name == null || name.length > SIZE)
            return;

        int hash = hashFunction(name);

        //Это нужно поскольку пользователь может ввести массив размером меньше 10
        name = copyArrayWithEmptySpace(name);

        //В ячейке и так ничего нет
        if (segmentTable[hash].name == null)
            return;

        //Если это первый и единственный элемент в списке сегмента
        if (segmentTable[hash].next == null) {
            segmentTable[hash].name = null;
            return;
        }

        //Если это не первый элемент в списке сегмента
        Element cur = segmentTable[hash];

        if (compareNames(cur.name, name)) {
            if (cur.next == null) { //Если вначале списка и дальше ничего нет
                cur.name = null;
                return;
            } else { //Если дальше идут элементы в списке
                segmentTable[hash] = cur.next;
                return;
            }
        }
        while (cur != null) {
            if (compareNames(cur.name,name)) {
                Element prev;
                if(cur.next!=null){ //Когда и перед и после удаляемого есть и другие элементы
                    prev = getPrevious(cur.name,hash);
                    cur.name =null;
                    prev.next=cur.next;
                    return;
                }
                // Если удаляемый элемент в конце
                prev= getPrevious(cur.name,hash);
                prev.next=null;
                return;
            }
            cur = cur.next;
        }
    }

    private Element getPrevious(char[] name, int hash){
        Element a = segmentTable[hash];
        Element b = null;

        while (a!=null){
            if(compareNames(a.name,name))
                return b;
            b=a;
            a=a.next;
        }
        return null;
    }

    private boolean compareNames(char[] a, char[] b){
        if (a==null || b ==null)
            return false;

        for (int i = 0; i < SIZE; i++)
            if(a[i]!=b[i])
                return false;
        return true;
    }

    //т.к. пользователь явно может ввести {a,b,c} а не {a,b,c, , , , , , , }
    private char[] copyArrayWithEmptySpace(char[] name){
        char[] nameWithEmpties = new char[SIZE];
        for (int i = 0; i < name.length; i++)
            nameWithEmpties[i]=name[i];
        for (int i = name.length; i < nameWithEmpties.length-1; i++)
            nameWithEmpties[i]=0;
        return nameWithEmpties;
    }

    public void insert(char[] name) {
        if (name == null || name.length>SIZE)
            return;

        int hash = hashFunction(name); //Вычисляем ячейку в таблице сегментов

        if (segmentTable[hash].name == null) {     //Добавляем если ячейка хэш таблицы была пуста
            segmentTable[hash].name = new char[10];
            segmentTable[hash].setName(name);
            return;
        }


        //P.S. Здесь мы должны не допустить добавления повторяющихся элементов
        if (segmentTable[hash] != null) { //Если в ячейке хэш таблицы есть элемент

            boolean hasAlready = false; //Для выявления повторяющихся
            Element cur = segmentTable[hash];

            while (cur != null) {
                if (cur.name == name) {
                    hasAlready = true;
                    break;
                }
                cur = cur.next;
            }

            if (!hasAlready) {
                if (segmentTable[hash].next == null) //Если следующий после головы пустой
                    segmentTable[hash].next = new Element(name, null);
                else  //Если следующий после головы не пустой
                    cur = new Element(name, null);
            }
        }
    }

    public boolean member(char[] name){
        name = copyArrayWithEmptySpace(name);
        if(name==null || name.length!=SIZE)
            return false;

        int hash = hashFunction(name);

        if(compareNames(segmentTable[hash].name,name))
            return true;
        else {
            Element cur = segmentTable[hash];
            while (cur!=null){
                if(compareNames(cur.name, name))
                    return true;
                cur=cur.next;
            }
        }

        return false;
    }

    public void makeNull(){
        for (int i = 0; i < segmentTable.length; i++) {
            segmentTable[i].setName(null);
            segmentTable[i].next=null;
        }
    }

    //Идея - представить символы в виде целых чисел, используя для этого машинные коды символов
    public int hashFunction(char[] name) {
        int sum = 0;
        for (int i = 0; i < name.length; i++)
            sum += name[i];

        return sum % 10;
    }

    public void printDictionary(){
        for (Element element : segmentTable) {
            System.out.println(element.toString());
        }
    }
}
