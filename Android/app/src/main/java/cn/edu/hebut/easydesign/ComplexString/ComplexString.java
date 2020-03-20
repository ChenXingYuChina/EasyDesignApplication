package cn.edu.hebut.easydesign.ComplexString;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem.EditableProxy;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.Tools.ResourcesTools;

public class ComplexString implements Serializable {
    private int[] start = null;
    private int[] width = null;
    private long[] resourcesId = null;
    private String[] urls = null;
    private String content = null;

    public long id;

//    private transient List<String> imageUrls = null;
//    private transient List<byte[]> data = null;
    private transient Map<Integer, String> imagePath = null;
    private transient Condition<Boolean> cancelLoadImage = null;
    protected transient Editable string = null;

    public ComplexString(String string) {
        this.string = new SpannableStringBuilder(string);
    }

    public ComplexString(Editable string) {
        this.string = string;
    }

    public ComplexString(JSONObject data) throws Exception {
        content = data.getString("content");
        JSONArray positions, widths, resources;
        try {
            positions = data.getJSONArray("position");
            widths = data.getJSONArray("width");
            resources = data.getJSONArray("resources");
        } catch (Exception e) {
            return ;
        }
        int length = positions.length();
        start = new int[length];
        width = new int[length];
        resourcesId = new long[length];
        urls = new String[length];
        for (int i = 0; i < length; i++) {
            start[i] = positions.getInt(i);
            width[i] = widths.getInt(i);
            JSONObject res = resources.getJSONObject(i);
            resourcesId[i] = res.getLong("id");
            String url;
            try {
                url = res.getString("url");
            } catch (JSONException e) {
                url = null;
            }
            urls[i] = url;
        }
    }


    private void makeUp() {
        int length = start.length;
        cancelLoadImage = new Condition<>(false);
        for (int i = 0; i < length; i++) {
            AddSpan(start[i], width[i], resourcesId[i], urls[i]);
        }
    }

    /*
    don't run it in the UI thread
     */
    public void parseToServerFormat() throws Exception {
        if (content != null) {
            return;
        }

        content = string.subSequence(0, string.length()).toString();
        Object[] spans = string.getSpans(0, string.length(), Object.class);
        int length = spans.length;
        ArrayList<Integer> startMid = new ArrayList<>();
        List<Integer> widthMid = new LinkedList<>();
        List<Long> resourcesIdMid = new LinkedList<>();
        List<String> urlsMid = new LinkedList<>();
        imagePath = new HashMap<>();

        for (int i = 0; i < length; i++) {
            Object span = spans[i];
            String url = null;
            long id = -1;
            if (span instanceof RelativeSizeSpan) {
                RelativeSizeSpan sizeSpan = (RelativeSizeSpan) span;
                float size = sizeSpan.getSizeChange();
                if (size == 0.5f) {
                    id = SMALL_FONT_SIZE + FONT_SIZE_BASE;
                } else if (size == 1.5f) {
                    id =BIG_FONT_SIZE + FONT_SIZE_BASE;
                } else if (size == 2.0f) {
                    id = HUGE_FONT_SIZE + FONT_SIZE_BASE;
                } else {
                    Log.i("cs", "parseToServerFormat: " + size);
                    throw new Exception();
                }
            } else if (span instanceof myImageSpan) {
                myImageSpan imageSpan = (myImageSpan) span;
                imagePath.put(i, imageSpan.path);
                id = IMAGE + i;
            } else if (span instanceof URLSpan) {
                url = ((URLSpan) span).getURL();
                id = HYPERLINK;
            } else if (span instanceof UnderlineSpan) {
                id = UNDERLINE;
            } else if (span instanceof ForegroundColorSpan) {
                ForegroundColorSpan colorSpan = (ForegroundColorSpan) span;
                if (colorSpan.getForegroundColor() == 0xff0000) {
                    id = TEXT_COLOR_BASE + RED_COLOR;
                } else if (colorSpan.getForegroundColor() == 0x00ff00) {
                    id = TEXT_COLOR_BASE + GREEN_COLOR;
                } else if (colorSpan.getForegroundColor() == 0x0000ff) {
                    id = TEXT_COLOR_BASE + BLUE_COLOR;
                } else if (colorSpan.getForegroundColor() == 0xffff00) {
                    id = TEXT_COLOR_BASE + YELLOW_COLOR;
                } else if (colorSpan.getForegroundColor() == 0xff00ff) {
                    id = TEXT_COLOR_BASE + PURPLE_COLOR;
                }
            } else if (span instanceof BackgroundColorSpan) {
                BackgroundColorSpan colorSpan = (BackgroundColorSpan) span;
                if (colorSpan.getBackgroundColor() == 0xff0000) {
                    id = BACKGROUND_BASE + RED_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0x00ff00) {
                    id = BACKGROUND_BASE + GREEN_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0x0000ff) {
                    id = BACKGROUND_BASE + BLUE_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0xffff00) {
                    id = BACKGROUND_BASE + YELLOW_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0xff00ff) {
                    id = BACKGROUND_BASE + PURPLE_COLOR;
                }
            } else if (span instanceof SuperscriptSpan) {
                id = SUPERSCRIPT;
            } else if (span instanceof SubscriptSpan) {
                id = SUBSCRIPT;
            } else if (span instanceof StrikethroughSpan) {
                id = STRIKE_THROUGH;
            } else {
                continue;
            }
            int startPosition = string.getSpanStart(span);
            startMid.add(startPosition);
            widthMid.add(string.getSpanEnd(span) - startPosition);
            resourcesIdMid.add(id);
            urlsMid.add(url);
        }
        start = fromIntegerListToIntArray(startMid);
        width = fromIntegerListToIntArray(widthMid);
        resourcesId = fromLongListTo_longArray(resourcesIdMid);
        urls = urlsMid.toArray(new String[0]);
    }

    private int[] fromIntegerListToIntArray(List<Integer> list) {
        int[] goal = new int[list.size()];
        for (int i = 0; i < list.size() ; i++) {
            goal[i] = list.get(i);
        }
        return goal;
    }
    private long[] fromLongListTo_longArray(List<Long> list) {
        long[] goal = new long[list.size()];
        for (int i = 0; i < list.size() ; i++) {
            goal[i] = list.get(i);
        }
        return goal;
    }

    public JSONObject toJson() throws Exception {
        if (content == null) {
            parseToServerFormat();
        }
        JSONObject goal = new JSONObject();
        goal.put("content", content);
        goal.put("position", listToJsonArray(start));
        goal.put("width", listToJsonArray(width));
        JSONObject resources = new JSONObject();
        resources.put("res", listToJsonArray(resourcesId));
        resources.put("urls", listToJsonArray(urls));
        goal.put("resources", resources);
        return goal;
    }

    private JSONArray listToJsonArray(int[] list) {
        JSONArray array = new JSONArray();
        for (int one : list) {
            array.put(one);
        }
        return array;
    }

    private JSONArray listToJsonArray(long[] list) {
        JSONArray array = new JSONArray();
        for (long one : list) {
            array.put(one);
        }
        return array;
    }

    private <T> JSONArray listToJsonArray(T[] list) {
        JSONArray array = new JSONArray();
        for (T one : list) {
            array.put(one);
        }
        return array;
    }

    public Map<Integer, String> getImagePath() {
        if (content == null) {
            try {
                parseToServerFormat();
            } catch (Exception e) {
                return null;
            }
        }
        return imagePath;
    }


    public static final int UNDERLINE = 0;
    public static final int STRIKE_THROUGH = 1;
    public static final int SUPERSCRIPT = 2;
    public static final int SUBSCRIPT = 3;
    public static final int FONT_SIZE_BASE = 4;
    public static final int TEXT_COLOR_BASE = FONT_SIZE_BASE + 3;
    public static final int BACKGROUND_BASE = TEXT_COLOR_BASE + 5;
    public static final int HYPERLINK = BACKGROUND_BASE + 5;
    public static final int IMAGE = HYPERLINK + 1;
    public static final int SMALL_FONT_SIZE = 0;
    public static final int BIG_FONT_SIZE = 1;
    public static final int HUGE_FONT_SIZE = 2;
    public static final int RED_COLOR = 0;
    public static final int BLUE_COLOR = 1;
    public static final int YELLOW_COLOR = 2;
    public static final int GREEN_COLOR = 3;
    public static final int PURPLE_COLOR = 4;

    public static Object getSpanFromId(long id, final String url) {
        Context ctx = ContextHolder.getContext();
        if (ctx == null) {
            return null;
        }
        if (id <= HYPERLINK) {
            switch ((int) id) {
                case HYPERLINK:
                    return new myUrlSpan(url);
                case UNDERLINE:
                    return new UnderlineSpan();
                case FONT_SIZE_BASE + SMALL_FONT_SIZE:
                    return new RelativeSizeSpan(0.5f);
                case FONT_SIZE_BASE + BIG_FONT_SIZE:
                    return new RelativeSizeSpan(1.5f);
                case FONT_SIZE_BASE + HUGE_FONT_SIZE:
                    return new RelativeSizeSpan(2.0f);
                case TEXT_COLOR_BASE + RED_COLOR:
                    return new ForegroundColorSpan(ctx.getResources().getColor(R.color.redForComplexString));
                case TEXT_COLOR_BASE + BLUE_COLOR:
                    return new ForegroundColorSpan(ctx.getResources().getColor(R.color.blueForComplexString));
                case TEXT_COLOR_BASE + YELLOW_COLOR:
                    return new ForegroundColorSpan(ctx.getResources().getColor(R.color.yellowForComplexString));
                case TEXT_COLOR_BASE + GREEN_COLOR:
                    return new ForegroundColorSpan(ctx.getResources().getColor(R.color.greenForComplexString));
                case TEXT_COLOR_BASE + PURPLE_COLOR:
                    return new ForegroundColorSpan(ctx.getResources().getColor(R.color.purpleForComplexString));
                case BACKGROUND_BASE + RED_COLOR:
                    return new BackgroundColorSpan(ctx.getResources().getColor(R.color.redForComplexString));
                case BACKGROUND_BASE + BLUE_COLOR:
                    return new BackgroundColorSpan(ctx.getResources().getColor(R.color.blueForComplexString));
                case BACKGROUND_BASE + YELLOW_COLOR:
                    return new BackgroundColorSpan(ctx.getResources().getColor(R.color.yellowForComplexString));
                case BACKGROUND_BASE + GREEN_COLOR:
                    return new BackgroundColorSpan(ctx.getResources().getColor(R.color.greenForComplexString));
                case BACKGROUND_BASE + PURPLE_COLOR:
                    return new BackgroundColorSpan(ctx.getResources().getColor(R.color.purpleForComplexString));
                case STRIKE_THROUGH:
                    return new StrikethroughSpan();
                case SUPERSCRIPT:
                    return new SuperscriptSpan();
                case SUBSCRIPT:
                    return new SubscriptSpan();
            }
        }
        return null;
    }

    private void AddSpan(int start, int width, long id, String url) throws IllegalArgumentException {
        if (id >= IMAGE) {
            addImageSpanOfHost(id, start, start + width);
        }
        string.setSpan(getSpanFromId(id, url), start, width + start, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private static int maxWidth = -1;

    private void addImageSpanOfHost(long id, final int start, final int end) {
        ContextHolder.getBinder().PutTask(new ImageHostLoadTask(id, cancelLoadImage) {
            @Override
            protected void setImage(Bitmap bitmap) {
                Context ctx = ContextHolder.getContext();
                Log.i("CS", "the context: " + ctx);
                if (ctx == null) {
                    return;
                }
                string.setSpan(new ImageSpan(ctx, scaleBitMap(bitmap)), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//                    complexString.refresh();
                Log.i("CS", "start " + start + " size " + bitmap.getByteCount());
            }
        });
    }

    private Bitmap scaleBitMap(Bitmap bitmap) {
        int oWith = bitmap.getWidth();
        int oHeight = bitmap.getHeight();
        int height = oHeight;
        int width = oWith;
        if (maxWidth == -1) {
            synchronized (ComplexString.class) {
                if (maxWidth == -1) {
                    maxWidth = ResourcesTools.dp2px(300);
                }
            }
        }
        if (width > maxWidth) {
            height = height * maxWidth / width;
            width = maxWidth;
        }
        if (width != oWith) {
            Matrix translate = new Matrix();
            translate.preScale(((float) width) / oWith, ((float) height) / oHeight);
//                        Log.i("cs", width + " " +height);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, oWith, oHeight, translate, false);
        }
        return bitmap;
    }


    private final static String loading = "\r\n图片正在加载中\r\n";
    public void addImageSpan(int start, int end, Bitmap bitmap, String path) {
        SpannableStringBuilder builder = new SpannableStringBuilder(loading);
        ImageSpan imageSpan = new myImageSpan(ContextHolder.getContext(), scaleBitMap(bitmap), path);
        builder.setSpan(imageSpan, 2, 9, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        Log.i(EditableProxy.TAG, "addImageSpan: ");

        string.replace(start, end, builder);
    }

    public void addSpan(int id, int start, int end, String url) {
        string.setSpan(getSpanFromId(id, url), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private transient TextView textView;

    public void SetToTextView(TextView textView) {
        if (textView == this.textView) {
            return;
        }
        if (string != null) {
            textView.setText(string);
        } else {
            this.textView = textView;
            textView.setText(content, TextView.BufferType.EDITABLE);
            string = textView.getEditableText();
            makeUp();
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void cancel() {
        if (cancelLoadImage != null)
            cancelLoadImage.condition = true;
    }

    static class myUrlSpan extends URLSpan {

        public myUrlSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
            // TODO: 2020/2/17 增加其他可行的协议，直接导到对应位置
            Log.i("cs", "onClick: " + widget);
            super.onClick(widget);
        }
    }

    static class myImageSpan extends ImageSpan {
        String path;
        public myImageSpan(@NonNull Context context, Bitmap bitmap, String path) {
            super(context, bitmap);
            this.path = path;
        }
    }

}
