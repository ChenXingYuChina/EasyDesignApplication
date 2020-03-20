package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

public class UndoManager {
    private ArrayList<TextChangeAction> actionStack = new ArrayList<>();
    private int undoHead = -1;
    public void clear() {
        actionStack.clear();
        undoHead = -1;
    }


    private boolean selfAction = false;
    public void addAction(TextChangeAction action) {
        if (selfAction) {
            return;
        }
        Log.i("undoManager", "addAction: " + undoHead + " " + action);
        undoHead++;
        if (undoHead != actionStack.size()) {
            actionStack = new ArrayList<>(actionStack.subList(0, undoHead));
        }
        actionStack.add(action);
    }

    public void redo(Editable editable) {
        if (undoHead == actionStack.size() - 1) {
            return;
        }
        selfAction = true;
        undoHead++;
        actionStack.get(undoHead).redo(editable);
        selfAction = false;
    }

    public void undo(Editable editable) {
        if (undoHead == -1) {
            return;
        }
        selfAction = true;
        actionStack.get(undoHead).undo(editable);
        undoHead--;
        selfAction = false;
    }

    public void disable() {
        selfAction = true;
    }
    public void enable() {
        selfAction = false;
    }
}
