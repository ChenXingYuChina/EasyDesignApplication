package cn.edu.hebut.easydesign.ComplexString.RichTextEditor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.IdRes;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlbumImageByPath;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlumImageByImage;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem.EditableProxy;
import cn.edu.hebut.easydesign.R;

public class RichTextEditorHelper implements UseAlumImageByImage, UseAlbumImageByPath, View.OnClickListener, View.OnLongClickListener {
    // map view id to span id
    protected static Map<Integer, Integer> mapAdd = new HashMap<>();
    protected static Map<Integer, Class> mapDelete = new HashMap<>();
    protected static final int[] addViewIds = new int[]{
            R.id.set_underline,
            R.id.set_strike_through,
            R.id.set_superscript,
            R.id.set_subscript,
            R.id.small_font,
            R.id.big_font,
            R.id.huge_font,
            R.id.red_text,
            R.id.blue_text,
            R.id.yellow_text,
            R.id.green_text,
            R.id.purple_text,
            R.id.red_background,
            R.id.blue_background,
            R.id.yellow_background,
            R.id.green_background,
            R.id.purple_background,
            R.id.hyperlink,
            R.id.insert_image
    };
    protected static final int[] deleteViewIds = new int[]{
            R.id.set_underline,
            R.id.set_strike_through,
            R.id.set_superscript,
            R.id.set_subscript,
            R.id.text_color,
            R.id.background_color,
            R.id.text_font_size,
    };
    protected static Class[] spanClass = new Class[]{
            UnderlineSpan.class,
            StrikethroughSpan.class,
            SuperscriptSpan.class,
            SubscriptSpan.class,
            ForegroundColorSpan.class,
            BackgroundColorSpan.class,
            RelativeSizeSpan.class
    };

    protected View[] addViews = new View[addViewIds.length];

    protected View[] deleteView = new View[deleteViewIds.length];

    static {
        for (int i = 0; i <= ComplexString.IMAGE; i++) {
            mapAdd.put(addViewIds[i], i);
        }
        for (int i = 0; i < deleteViewIds.length; i++) {
            mapDelete.put(deleteViewIds[i], spanClass[i]);
        }
    }

    private RichTextEditorSimple editor;

    public RichTextEditorSimple getEditor() {
        return editor;
    }

    private View redo, undo;

    private ViewGroup cachedTarget;

    public RichTextEditorHelper(ViewGroup target) {
        cachedTarget = target;
        for (int i = 0; i < addViewIds.length ; i++) {
            View view = target.findViewById(addViewIds[i]);
            addViews[i] = view;
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
        for (int i = 0; i < deleteView.length; i++) {
            View view;
            if (deleteViewIds[i] == addViewIds[i]) {
                view = addViews[i];
            } else {
                view = target.findViewById(deleteViewIds[i]);
            }
            deleteView[i] = view;
            if (view != null) {
                view.setOnLongClickListener(this);
            }
        }
        editor = target.findViewById(R.id.complex_string_editor);
        if (editor == null) {
            throw new IllegalArgumentException();
        }
        redo = target.findViewById(R.id.redo_action);
        undo = target.findViewById(R.id.undo_action);
        if (redo != null) {
            redo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.redo();
                }
            });
        }
        if (undo != null) {
            undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.undo();
                }
            });
        }
    }

    public <T extends View> T getAddViewByViewId(@IdRes int id) {
        return (T) addViews[mapAdd.get(id)];
    }

    public <T extends View> T getAddViewBySpanId(int spanId) {
        return (T) addViews[spanId];
    }

    public <T extends View> T getDeleteViewByViewId(@IdRes int id) {
        for (int i = 0; i < deleteViewIds.length; i++) {
            if (deleteViewIds[i] == id) {
                return (T) deleteView[i];
            }
        }
        return null;
    }

    public <T extends View> T getDeleteViewBySpanClass(Class type) {
        for (int i = 0; i < spanClass.length; i++) {
            if (spanClass[i].isAssignableFrom(type)) {
                return (T) deleteView[i];
            }
        }
        return null;
    }

    private String path = null;
    private Bitmap image = null;

    @Override
    public void setImage(String path) {
        Log.i("setImage", "setImage: ");
        this.path = path;
    }

    @Override
    public void setImage(Bitmap image) {
        Log.i("setImage", "setImage: ");
        this.image = image;
        if (path != null && image != null) {
            editor.addImageSpan(image, path);
            this.path = null;
            this.image = null;
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("RTEH", "onClick: " + mapAdd.size());
        int spanId = mapAdd.get(v.getId());
        Log.i("RTEH", "onClick: " + spanId);
        if (spanId < ComplexString.HYPERLINK) {
            editor.addSpan(spanId, "");
        } else if (spanId == ComplexString.HYPERLINK) {
            showLinkDialog();
        } else if (spanId == ComplexString.IMAGE) {
            ContextHolder.selectAlbumImage(this);
        }
    }

    private void showLinkDialog() {
        Context context = ContextHolder.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.rich_text_editor_hyperlink_dialog, null, false);
        final EditText editText = view.findViewById(R.id.RichTextEditorHyperLinkDialogInput);
        builder.setView(view).setTitle("请输入网址")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String link = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(link)) {
                        return;
                    }
                    // When KnifeText lose focus, use this method
                    editor.addSpan(ComplexString.HYPERLINK, link);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
    }

    @Override
    public boolean onLongClick(View v) {
        Log.i(EditableProxy.TAG, "onLongClick: ");
        editor.delSpan(mapDelete.get(v.getId()));
        return true;
    }
}
