package sets.partly_ordered;

public class PartlyOrderedSetLinkedList {

    private Node head;

    public PartlyOrderedSetLinkedList(){
        this.head=null;
    }

    public PartlyOrderedSetLinkedList(int[][] table) {
        if (table == null || table.length == 0 || table[0][0] == table[0][1]) //Последняя проверка - это проверка на то чтобы не могло быть {1,1} только для первого элемента. (т.к. числа первое всегда должно быть меньше следующего 1<2)
            return;

        head = new Node(table[0][0], null, 0, null);//Добавляем начальный элемент
        head.setNext(new Node(table[0][1], null, head.getCount() + 1, null));//Назначаем ему ссылку на следующий ведущий
        head.setTrail(new Follower(head.getNext(), null));//Создаем ведомый элемент


        for (int i = 1; i < table.length; i++) {//Проходимся по остальным значениям кроме первого
            if (table[i][0] == table[i][1]) //Проверка на {n,n} т.к. числа первое всегда должно быть меньше следующего
                return;

            //Ищем значение по колонкам (их всего две т.к. данные имеют вид {1,2})
            Node cur = search(table[i][0]);//Поиск предшествующего
            Node tmp = search(table[i][1]);//Поиск по ведомому

            if (cur.getKey() != table[i][0]) { //Искомый не совпадает с ведомым {,2} в первой строке
                cur.setNext(new Node(table[i][0], null, 0, null)); //Задаем ссылку на следующий последнему элементу.
                cur = cur.getNext();
            }

            if (tmp.getKey() != table[i][1]) {
                tmp.setNext(new Node(table[i][1], null, 0, null));
                tmp = tmp.getNext();
            }

            tmp.increment(); //т.к. нашли связь

            Follower tmpTrail = cur.getTrail();
            cur.setTrail(new Follower(tmp, tmpTrail)); //Задаем ссылку на ведомого найденному элементу
        }
    }

    public void sort() {
        PartlyOrderedSetLinkedList sortedSet = new PartlyOrderedSetLinkedList();
        Node cur = head;
        Node tmp = null;
        Node lastInNew = null;

        while (cur != null) { //Идем по списку

            if (cur.getCount() == 0) {
                Follower trail = cur.getTrail();

                while (trail != null) { //Проходимся по ведомым
                    trail.id.decrement(); //Уменьшаем число связей на 1
                    trail = trail.next;
                }
                cur.setNext(null); //Удаляем связь

                if (cur == head) //Если мы попали в голову, то идем дальше т.к. мы не можем сбросить голову (слетит весь список)
                    head = head.getNext();
                else
                    tmp.setNext(cur.getNext()); //Если не попали, то идем запишем следующий

                //Если есть голова
                if (sortedSet.head != null) {
                    Node temp = lastInNew.getNext(); //Запоминаем
                    lastInNew.setNext(cur);   //Переподвязываем в хвост текущий cur
                    lastInNew.setNext(temp); //Возвращаем конец
                    lastInNew = lastInNew.getNext(); //Идем дальше
                }
                else {
                    //У нас нет головы
                    sortedSet.head = cur;
                    sortedSet.head.setNext(null);
                    lastInNew = sortedSet.head; //т.к. у нас 1 эл-т
                }
                //Сброс неактуальной инф.
                cur = head;
                tmp = null;
                continue;
            }
            //Если count != 0 то идем дальше:
            tmp = cur;
            cur = cur.getNext();
        }
        //Когда отсортировали новый список
        this.head = sortedSet.head; //Меняем список на отсортированный
    }

    private Node search(int key){
        Node cur = head;
        Node tmp = null;

        while(cur != null){
            if(key == cur.getKey())
                return cur;         //Возвращаем если нашел
            tmp=cur;
            cur=cur.getNext();
        }

        return tmp; //Последний если не нашел
    }

    public void print(){
        Node cur = head;
        while (cur != null){
            System.out.print("key: " + cur.getKey() + " | Count : " + cur.getCount() + " | Trail : ");

            Follower trail = cur.getTrail();
            while (trail != null){
                System.out.print(trail.id.getKey() + " ");
                trail = trail.next;
            }

            System.out.println();
            cur = cur.getNext();
        }
    }

}
