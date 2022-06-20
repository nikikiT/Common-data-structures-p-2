package data_base_structure;

public class CourseSet {

    private int setSize;
    private Course set[];

    public class Course extends Pointer {

        private int number; //Номер курса
        private Registry registry;

        public Course(int number) {
            this.number = number;
        }

        public Registry getRegistry() {
            return this.registry;
        }

        public void setRegistry(Registry registry) {
            this.registry = registry;
        }

        public void printCourseNumber() {
            if (number != -1)
                System.out.println(number);
        }
    }

    public CourseSet(int setSize) {
        this.setSize = setSize;
        set = new Course[setSize];
    }

    public Course getCourse(int number) {
        if (number < 0)
            return null;
        int index = findIndex(number);
        if (index == -1)
            return null;
        return set[index];
    }

    public void insert(int number) {
        if (number < 0)
            return;
        int index = findFreeIndex(number);

        if (index == -1) //нет ни свободных, ни deleted, ни искомого имени
            return;

        set[index] = new Course(number); //вставляем только если индекс пустой или удаленный
    }

    public void delete(int number) {
        if (number < 0)
            return;

        int index = findIndex(number);

        if (index == -1) //Курс не найден
            return;

        set[index].number = -1;
    }

    public void makeNull() {
        for (int i = 0; i < setSize; i++)
            set[i] = null;
    }

    public boolean member(int number) {
        if (number < 0)
            return false;
        return findIndex(number) != -1;
    }

    //Возвращаем index найденного курса, или -1
    private int findIndex(int number) {
        int hash = hashFunc(number);
        int firstHash = hash;
        int counter = 1;

        if (set[hash] == null)
            return -1;
        else if (set[hash].number == number)
            return hash;

        hash = hashFunc(firstHash, counter);

        while (hash != firstHash) {
            if (set[hash] == null)
                return -1;
            else if (set[hash].number == number)
                return hash;
            hash = hashFunc(firstHash, ++counter);
        }
        return -1;
    }

    private int hashFunc(int index) {
        return index % setSize;
    }

    private int hashFunc(int index, int probe) {
        return (index + probe) % setSize;
    }

    //Воз-ет index пустого или если не найдет элемент такой же, как name, первого удаленного эл-та
    private int findFreeIndex(int number) {
        int deleted = -1;
        int hash = hashFunc(number);
        int firstHash = hash;
        int counter = 1;

        if (set[hash] == null) //Если свободен
            return hash;
        else if (set[hash].number == number) //Если занят
            return -1;
        else if (isDeleted(hash)) //Был удален
            deleted = hash;

        hash = hashFunc(firstHash, counter);

        while (hash != firstHash) {
            if (set[hash] == null)
                return hash;
            else if (set[hash].number == number)
                return -1;
            else if (isDeleted(hash) && deleted == -1)
                deleted = hash;
            hash = hashFunc(firstHash, ++counter);
        }
        return deleted;
    }

    private boolean isDeleted(int index) {
        return set[index].number == -1;
    }

    public void printCourse() {
        for (int i = 0; i < setSize; i++) {
            System.out.printf("%6s", (i + 1) + ":  ");
            if (set[i] != null)
                set[i].printCourseNumber();
            else
                System.out.println();
        }
        System.out.println();
    }
}
