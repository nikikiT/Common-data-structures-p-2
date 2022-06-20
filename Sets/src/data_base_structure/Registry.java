package data_base_structure;

public class Registry extends Pointer{

    private Pointer student;
    private Pointer course;

    public boolean isRegistry() {
        return true;
    }

    public Pointer getStudNext() {
        return student;
    }

    public void setStudent(Pointer student) {
        this.student = student;
    }

    public Pointer getCourse() {
        return course;
    }

    public void setCourse(Pointer course) {
        this.course = course;
    }
}
