package cn.edu.hebut.easydesign.Resources.Passage;

import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;

public abstract class LoadPassageTask extends Task<Passage, Condition<Boolean>> {
    private long id;
    private short type;

    public LoadPassageTask(long id, short type, Condition<Boolean> cancel) {
        this.id = id;
        this.type = type;
        data2 = cancel;
    }

    @Override
    protected boolean doOnService() {
        if (!data2.condition) {
            data1 = PassageLoader.getInstance().load(id, type);
            return true;
        }
        return false;
    }

    @Override
    protected void doOnMain() {
        if (data2.condition) {
            return;
        }
        if (data1 == null) {
            onError();
        } else {
            onSuccess(data1);
        }
    }

    protected abstract void onError();

    protected abstract void onSuccess(Passage passage);
}
