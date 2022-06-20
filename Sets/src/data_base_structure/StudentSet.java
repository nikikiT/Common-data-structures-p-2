package data_base_structure;

public class StudentSet {

    private int setSize;
    private int maxNameSize;
    private Student[] set;

    public class Student extends Pointer {

        private char[] name;
        private Registry registry;

        public Student(char[] name) {
            this.name = name;
            registry = null;
        }

        public void printStudentName() {
            String str = name.toString();
            System.out.println(str);
        }

        public void setRegistry(Registry registry) {
            this.registry = registry;
        }

        public Registry getRegistry() {
            return registry;
        }
    }

    public void setStudent(int listSize, int nameSize) {
        this.setSize = listSize;
        this.maxNameSize = nameSize;
        set = new Student[listSize];
    }

    public Student getStudent(char[] name) {
        if (name == null || name.length > maxNameSize)
            return null;
        int index = findIndex(name);
        if (index == -1)
            return null;
        return set[index];
    }

    public void insert(char[] name) {
        if (name == null || name.length > maxNameSize)
            return;

        int index = findIndex(name);

        if (index == -1) //нет ни свободных, ни deleted, ни искомого имени
            return;

        set[index] = new Student(copyName(name)); //вставляется только если индекс пустой или удаленный
    }

    public void delete(char[] name){
        if (name==null || name.length> maxNameSize)
            return;

        int index = findIndex(name);

        if (index == -1) //Если элемент не найден
            return;

        set[index].name = new char[1];
        set[index].name[0] = '\0';
    }

    public boolean member(char[] name) {
        if (name == null || name.length > maxNameSize)
            return false;
        return findIndex(name) == -1;
    }

    public void makeNull() {
        for (int i = 0; i < setSize; i++)
            set[i] = null;
    }

    //Возвращаем index найденного студента, или -1
    private int findIndex(char[] name) {
        int hash = hashFunc(name);
        int firstHash = hash;
        int counter = 1;

        if (set[hash] == null)
            return -1;
        else if (set[hash].name == name)
            return hash;

        hash = hashFunc(firstHash, counter);

        while (hash != firstHash) {
            if (set[hash] == null)
                return -1;
            else if (set[hash].name == name)
                return hash;
            hash = hashFunc(firstHash, ++counter);
        }
        return -1;
    }

    private int hashFunc(char[] name) {
        int hash = 0;
        for (int i = 0; i < name.length; i++) {
            hash += name[i];
        }
        return hash % setSize;
    }

    private int hashFunc(int index, int probe) {
        return (index + probe) % setSize;
    }

    //возвращает index пустого или, если не найден элемент такой же, как name, первого удаленного элемента.
    private int findFreeIndex(char[] name) {
        int deleted = -1;
        int hash = hashFunc(name);
        int firstHash = hash;
        int counter = 1;

        if (set[hash] == null)
            return hash;
        else if (equals(set[hash].name, name))
            return -1;
        else if (isDeleted(hash))
            deleted = hash;

        hash = hashFunc(firstHash, counter);

        while (hash != firstHash) {
            if (set[hash] == null)
                return hash;
            else if (equals(set[hash].name, name))
                return -1;
            else if (isDeleted(hash) && deleted == -1)
                deleted = hash;
            hash = hashFunc(firstHash, ++counter);
        }
        return deleted;
    }

    private boolean equals(char[] a, char[] b) {
        if (a == b)
            return true;

        if (b.length != a.length)
            return false;

        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    private boolean isDeleted(int index){
        return set[index].name.length== 1 && set[index].name[0] == '\0';
    }

    private char[] copyName(char[] name) {
        char[] newName = new char[name.length];
        for (int i = 0; i < name.length; i++)
            newName[i] = name[i];
        return newName;
    }

    public void printStudent() {
        for (int i = 0; i < setSize; i++) {
            System.out.printf("%6s", (i + 1) + " student:  ");
            if (set[i] != null)
                set[i].printStudentName();
            else
                System.out.println();
        }
        System.out.println();
    }
}
