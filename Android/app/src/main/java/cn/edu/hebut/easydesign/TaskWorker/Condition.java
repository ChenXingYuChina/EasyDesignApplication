package cn.edu.hebut.easydesign.TaskWorker;

public class Condition<T> {
    volatile public T condition;
    public Condition(T c) {
        condition = c;
    }
    public Condition(){}
}
