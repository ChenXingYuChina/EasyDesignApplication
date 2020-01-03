package cn.edu.hebut.easydesign.ComplexString;

import android.app.job.JobScheduler;
import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;

public class ComplexString implements Data {
    transient int[] position = null;
    transient int[] width = null;
    transient int[] resourcesId = null;
    transient ArrayList<String> urls = null;
    transient private ArrayList<byte[]> data = null;
    transient CharSequence content = null;
    private long id;
    private SpannableString string;

    public ComplexString(SpannableString string, long id) {
        this.string = string;
        this.id = id;
    }

    /*
    don't run it in the UI thread
     */
    public void parseToServerFormat() throws Exception {
        if (content != null) {
            return;
        }
        content = string.subSequence(0, string.length());
        Object[] spans = string.getSpans(0, string.length(), null);
        int length = spans.length;
        position = new int[length];
        width = new int[length];
        resourcesId = new int[length];
        data = new ArrayList<>(length / 5);
        for (int i = 0; i < length; i++) {
            Object span = spans[i];
            position[i] = string.getSpanStart(span);
            width[i] = string.getSpanEnd(span) - position[i];
            if (span instanceof RelativeSizeSpan) {
                RelativeSizeSpan sizeSpan = (RelativeSizeSpan) span;
                if (sizeSpan.getSizeChange() == 0.5f) {
                    resourcesId[i] = SMALL_FONT_SIZE + FONT_SIZE_BASE;
                } else if (sizeSpan.getSizeChange() == 1.5f) {
                    resourcesId[i] = BIG_FONT_SIZE + FONT_SIZE_BASE;
                } else if (sizeSpan.getSizeChange() == 2.0f) {
                    resourcesId[i] = HUGE_FONT_SIZE + FONT_SIZE_BASE;
                }
                throw new Exception();
            } else if (span instanceof ImageSpan) {
                ImageSpan imageSpan = (ImageSpan) span;
                if (imageSpan.getSource() == null) {
                    Log.i("PASS", "ComplexString: pass an image at" + position[i]);
                    continue;
                }
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(imageSpan.getSource()));
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                this.data.add(data);
                resourcesId[i] = IMAGE;
            } else if (span instanceof URLSpan) {
                if (urls == null) {
                    urls = new ArrayList<>();
                }
                urls.add(((URLSpan) span).getURL());
                resourcesId[i] = HYPERLINK;
            } else if (span instanceof UnderlineSpan) {
                resourcesId[i] = UNDERLINE;
            } else if (span instanceof ForegroundColorSpan) {
                ForegroundColorSpan colorSpan = (ForegroundColorSpan) span;
                if (colorSpan.getForegroundColor() == 0xff0000) {
                    resourcesId[i] = TEXT_COLOR_BASE + RED_COLOR;
                } else if (colorSpan.getForegroundColor() == 0x00ff00) {
                    resourcesId[i] = TEXT_COLOR_BASE + GREEN_COLOR;
                } else if (colorSpan.getForegroundColor() == 0x0000ff) {
                    resourcesId[i] = TEXT_COLOR_BASE + BLUE_COLOR;
                } else if (colorSpan.getForegroundColor() == 0xffff00) {
                    resourcesId[i] = TEXT_COLOR_BASE + YELLOW_COLOR;
                } else if (colorSpan.getForegroundColor() == 0xff00ff) {
                    resourcesId[i] = TEXT_COLOR_BASE + PURPLE_COLOR;
                }
            } else if (span instanceof BackgroundColorSpan) {
                BackgroundColorSpan colorSpan = (BackgroundColorSpan) span;
                if (colorSpan.getBackgroundColor() == 0xff0000) {
                    resourcesId[i] = BACKGROUND_BASE + RED_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0x00ff00) {
                    resourcesId[i] = BACKGROUND_BASE + GREEN_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0x0000ff) {
                    resourcesId[i] = BACKGROUND_BASE + BLUE_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0xffff00) {
                    resourcesId[i] = BACKGROUND_BASE + YELLOW_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0xff00ff) {
                    resourcesId[i] = BACKGROUND_BASE + PURPLE_COLOR;
                }
            } else if (span instanceof SuperscriptSpan) {
                resourcesId[i] = SUPERSCRIPT;
            } else if (span instanceof SubscriptSpan) {
                resourcesId[i] = SUBSCRIPT;
            } else if (span instanceof StrikethroughSpan) {
                resourcesId[i] = STRIKE_THROUGH;
            } else {
                throw new Exception();
            }
        }
    }

    public SpannableString GetSpannableString() {
        return this.string;
    }

    public JSONObject toJson() throws Exception {
        if (content == null) {
            parseToServerFormat();
        }
        JSONObject goal = new JSONObject();
        goal.put("content", content.toString());
        goal.put("position", listToJsonArray(position));
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

    private JSONArray listToJsonArray(List<?> list) {
        JSONArray array = new JSONArray();
        for (Object one : list) {
            array.put(one);
        }
        return array;
    }

    public ArrayList<byte[]> getData() {
        if (content == null) {
            try {
                parseToServerFormat();
            } catch (Exception e) {
                return null;
            }
        }
        return data;
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

    public static Object getSpanFromId(Context ctx, long id, String url) {
        if (id <= HYPERLINK) {
            switch ((int) id) {
                case HYPERLINK:
                    return new URLSpan(url);
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
        return new ImageSpan(ctx, DataManagement.getInstance().GetUriOf(DataType.Image, id));
    }

    @Override
    public long GetId() {
        return id;
    }

    @Override
    public DataType GetType() {
        return DataType.Passage;
    }
}
