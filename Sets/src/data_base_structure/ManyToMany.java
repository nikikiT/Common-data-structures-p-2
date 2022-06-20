package data_base_structure;

public class ManyToMany {

    private StudentSet studentSet;
    private CourseSet courseSet;

    public ManyToMany(StudentSet studentSet, CourseSet courseSet){
        this.courseSet = courseSet;
        this.studentSet = studentSet;
    }

    public void add(String studentName, int courseNumber) {
        StudentSet.Student student = studentSet.getStudent(studentName.toCharArray());
        CourseSet.Course course = courseSet.getCourse(courseNumber);

        if (student == null || course == null)
            return;

        //такой записи еще нет, создаем запись
        Registry reg = new Registry();
        Registry studentReg = student.getRegistry();
        Registry courseReg = course.getRegistry();

        //если у студента нет ни одной регистрации - в запись добавляем студента
        if (studentReg == null)
            reg.setStudent(student);
        else     //если у студента есть регистрации - в запись добавляем первую регистрацию
            reg.setStudent(studentReg);
        student.setRegistry(reg); //Добавляем студенту новую регистрацию


        if (courseReg == null)
            reg.setCourse(course);
        else
            reg.setCourse(course);
        course.setRegistry(reg);
    }

    private boolean searchReg(StudentSet.Student student, CourseSet.Course course){
        Pointer reg = student.getRegistry();

        if (reg == null)
            return false; //Нет ни одной записи студента

        while (reg.isRegistry()) { //Проходимся по всем записям рег-м.
            if (course.equals(getEndCourse(reg))) //проверяем на совпадение
                return true;
            reg = ((Registry) reg).getStudNext(); //Переходим на эту запись и проверяем ее курс
        }
        return false;
    }

    public void remove(String studentName, int courseNumber){
        StudentSet.Student student = studentSet.getStudent(studentName.toCharArray());
        CourseSet.Course course = courseSet.getCourse(courseNumber);

        if(student == null || course == null)
            return;

        //поиск предидущего возвращает Pointer со стороны студента или null если нет
        Pointer prev = searchStudentReg(student, course);

        //такой записи нет
        if (prev == null)
            return;

        deleteStudentReg(student, prev);

        //поиск предидущего возвращает Pointer со стороны записи или null если нет
        prev = searchCourseReg(student, course);
        deleteCourseReg(course, prev);
    }

    //поиск предыдущего возвращает Pointer со стороны студента или null если не нашел
    private Pointer searchStudentReg(StudentSet.Student student, CourseSet.Course course){
        Pointer reg = student.getRegistry();  //первая запись студента

        if (reg == null)    //есть ли хоть одна запись у студента
            return null;

        Pointer prev = student;
        while (reg.isRegistry()) { //пока второй указатель - рег. запись
            if (course.equals(getEndCourse(reg)))  //проверяем второй указатель на совпадение
                return prev;
            prev = reg;
            reg = ((Registry) reg).getStudNext();
        }

        return null; //такого курса нет у студента
    }

    private Pointer searchCourseReg(StudentSet.Student student, CourseSet.Course course) {
        //первая запись курса
        Pointer reg = course.getRegistry();

        //есть ли хоть одна запись у курса
        if (reg == null)
            return null;

        //пока второй указатель - рег. запись
        //проверяем второй указатель на совпадение
        Pointer prev = course;
        while (reg.isRegistry()) {
            if (student.equals(getEndStudent(reg)))
                return prev;
            prev = reg;
            reg = ((Registry) reg).getCourse();
        }

        return null;
    }

    //удаление записи со стороны студента. Передается предидущая запись
    private void deleteStudentReg(StudentSet.Student student, Pointer prev) {
        //эта запись - сам студент?
        if (!prev.isRegistry()) {
            Pointer next = student.getRegistry();
            next = ((Registry) next).getStudNext();
            if (!next.isRegistry())
                student.setRegistry(null);
            else
                student.setRegistry((Registry) next);
        }
        //стандартное удаление
        else {
            Pointer next = ((Registry) prev).getStudNext();
            next = ((Registry) next).getStudNext();
            ((Registry) prev).setStudent(next);
        }
    }

    private void deleteCourseReg(CourseSet.Course course, Pointer prev) {
        //эта запись - сам студент?
        if (!prev.isRegistry()) {
            Pointer next = course.getRegistry();
            next = ((Registry) next).getCourse();
            if (!next.isRegistry())
                course.setRegistry(null);
            else
                course.setRegistry((Registry) next);
        }
        //стандартное удаление
        else {
            Pointer next = ((Registry) prev).getCourse();
            next = ((Registry) next).getCourse();
            ((Registry) prev).setCourse(next);
        }
    }

    private Pointer findStudentRegistration(){
        return null;
    }

    private Pointer findCourseRegistration(){
        return null;
    }

    public void removeAllCourses(String studentName){
        StudentSet.Student student = studentSet.getStudent(studentName.toCharArray());

        if (student == null) return;

        Pointer reg = student.getRegistry();
        if (reg == null) return;

        //пока следующий указатель - рег. запись переходим на эту запись и удаляем связь с регистрацией со стороны курса
        Pointer prev;
        CourseSet.Course course;
        while (reg.isRegistry()) {
            course = getEndCourse(reg);
            prev = searchCourseReg(student, course);
            deleteCourseReg(course, prev);
            reg = ((Registry) reg).getStudNext();
        }

        student.setRegistry(null); //удаляем связь студента со всей цепочкой курсов
    }

    public void removeAllStudents(int courseNumber){
        CourseSet.Course course  = courseSet.getCourse(courseNumber);

        if (course == null) return;

        Pointer reg = course.getRegistry();
        //Есть ли студент у курса
        if (reg == null) return;

        //пока следующий указатель - рег. запись переходим на эту запись и удаляем связь с регистрацией со стороны студента
        Pointer prev;
        StudentSet.Student student;
        while (reg.isRegistry()) {
            student = getEndStudent(reg);
            prev = searchStudentReg(student, course);
            deleteStudentReg(student, prev);
            reg = ((Registry) reg).getCourse();
        }
        course.setRegistry(null); //удаляем связь курса со всей цепочкой курсов
    }

    public void printStudentsOnCourse(int courseNumber) {
        CourseSet.Course course = courseSet.getCourse(courseNumber);

        if (course == null) return; //есть ли такой курс

        Pointer reg = course.getRegistry();
        if (reg == null) return; //есть ли хоть одна запись у курса

        //пока следующий указатель - рег. запись - переходим на эту запись и выводим ее студента
        while (reg.isRegistry()){
            getEndStudent(reg).printStudentName();
            reg = ((Registry) reg).getCourse();
        }
    }

    public void printCoursesOfStudent(String studentName) {
        StudentSet.Student student = studentSet.getStudent(studentName.toCharArray());

        if (student == null) return;            //есть ли такой студент

        Pointer reg = student.getRegistry();
        if (reg == null) return;                //есть ли хоть одна запись у студента

        //пока следующий указатель - рег. запись - переходим на эту запись и выводим ее курс
        while (reg.isRegistry()){
            getEndCourse(reg).printCourseNumber();
            reg = ((Registry) reg).getStudNext();
        }
    }

    private CourseSet.Course getEndCourse(Pointer reg){
        while (reg.isRegistry())
            reg=((Registry)reg).getCourse();
        return (CourseSet.Course) reg;
    }

    private StudentSet.Student getEndStudent(Pointer reg){
        while (reg.isRegistry())
            reg = ((Registry) reg).getStudNext();
        return (StudentSet.Student) reg;
    }

}
