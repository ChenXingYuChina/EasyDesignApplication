package cn.edu.hebut.easydesign.TaskWorker;

public abstract class Task<T, K> implements Runnable {
    protected T data1;
    protected K data2;

    protected abstract void doOnMain();

    public void run() {
        doOnMain();
    }

    /*
    return value: if true returned means it will call the doOnMain();
    if false means some Exception happened and cancel;
     */
    protected abstract boolean doOnService();

}
