package sets.cycled_list;

public class CycledList {

    private static class Node{
        int num;
        Node next;

        public Node(int num, Node next){
            this.num=num;
            this.next=next;
        }

        public Node(int num){
            this.num=num;
            this.next=null;
        }
    }

    private Node tail;

    public CycledList(){
        tail = null;
    }

    public CycledList(CycledList cl){
        tail = new Node(cl.tail.num, cl.tail.next); //Создаем новый элемент с данными из хвоста
        Node tempA = cl.tail.next; //Ссылка на первый
        Node tempSelf = tail; //Ссылка на хвост
        while (tempA != cl.tail){ //Пока не пройдемся по всему списку
            tempSelf.next = new Node(tempA.num, tempA.next);    // Добавляем в хвост новый эл-т
            tempA = tempA.next; //Идем дальше от первого
            tempSelf = tempSelf.next; //Ссылка на хвост идет дальше
        }
        tempSelf.next = tail;  //Следующий от хвоста становится новым созданным элементом
    }

    public void delete(int n){
        if (tail == null)  //если пустое множество - ретерн
            return;
        if (n == tail.num) { //Надо удалить эл-т в хвосте
            if (tail != tail.next){ //В хвосте и мн-ве не один эл-т
                tail = tail.next;
                return;
            }

            tail = null; //в хвосте и мн-ве один эл-т
            return;
        }

        Node tailPrev = searchPrev(tail, n);

        if (tailPrev != null) //если элемент не в хвосте = ищем предыдущий и перекижываем ссылки
            tailPrev.next = tailPrev.next.next;
    }

    public void insert(int n){
        /*
        Если список пустой
        Если в списке только 1 элемент и этот элемент больше тейла
        Если в списке только 1 элемент и этот элемент меньше тейла
        Если элемент больше хвоста, не меняя хвост вставляем элемент
         */
        if (tail == null){  //Список пустой
            tail = new Node(n, null);
            tail.next = tail;
            return;
        }

        if (tail.next == tail){ //Если в списке только 1 элемент
            if (n == tail.num) //И он равен тому что в хвосте
                return;
            if (n > tail.num){  //Элемент больше тейла
                tail.next = new Node(tail.num, tail);
                tail.num = n;
                return;
            }

            tail.next = new Node(n, tail);
            return;
        }

        if (n < tail.num){//Элемент меньше тейла
            Node tailPrev = searchPrev(tail, tail.num);
            if (tailPrev == null) return;
            tailPrev.next = new Node(tail.num, tail);
            tail.num = n;
            return;
        }

        Node temp = searchPlaceForInsert(tail.next, n);

        if (temp == null) {
            tail.next = new Node(n, tail.next);
            return;
        }

        if (temp.next.num == n)
            return;

        Node temporary = temp.next;
        temp.next = new Node(n, temporary);
    }

    private Node searchPrev(Node pos, int n){
        Node prev = pos;
        Node cur = pos.next;
        if (cur.num == n) //если текущий равен искомому элементу - возвращает предыдущий
            return prev;

        while (cur != pos){
            if (cur.num == n) //Условие поиска
                return prev;
            prev = cur;
            cur = cur.next;
        }

        if (cur.num == n)  //если в конце цикла текущий равен нашей искомой позиции - возвращаем предыдущий
            return prev;

        return null;
    }

    private Node searchPlaceForInsert(Node pos, int n){

        if (pos.num == n)  // если искомая позиция уже есть в нашей позиции - возвращаем ее
            return pos;
        Node prev = null;
        Node cur = pos.next;
        while (cur != pos){
            if (cur.num >= n) //если нет - проходим по циклу с запаздывающим указателем
                return prev;

            prev = cur;
            cur = cur.next;
        }
        return null;
    }



}
