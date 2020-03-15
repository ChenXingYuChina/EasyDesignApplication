package cn.edu.hebut.easydesign.RichTextEditor;

public class RichTextEditorHelper {
    private int start;
    private int end;

    public RichTextEditorHelper(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isValid(){
        return start < end;
    }
}
