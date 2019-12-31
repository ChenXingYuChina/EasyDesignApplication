package cn.edu.hebut.easydesign.ComplexString;

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
import java.io.Serializable;
import java.util.ArrayList;

public class ComplexString implements Serializable {
    private int[] position;
    private int[] width;
    private int[] resourcesId;
    private ArrayList<byte[]> data;
    protected CharSequence content;
    /*
    do not run it in the UI thread.
     */
    public ComplexString(SpannableString string) throws Exception {
        content = string.subSequence(0, string.length());
        Object[] spans = string.getSpans(0, string.length(), null);
        int length = spans.length;
        position = new int[length];
        width = new int[length];
        resourcesId = new int[length];
        data = new ArrayList<>(length/5);
        for (int i = 0; i < length; i ++) {
            Object span = spans[i];
            position[i] = string.getSpanStart(span);
            width[i] = string.getSpanEnd(span) - position[i];
            if (span instanceof RelativeSizeSpan) {
                RelativeSizeSpan sizeSpan = (RelativeSizeSpan)span;
                if (sizeSpan.getSizeChange() == 0.5f) {
                    resourcesId[i] = ComplexStringFactor.SMALL_FONT_SIZE + ComplexStringFactor.FONT_SIZE_BASE;
                } else if (sizeSpan.getSizeChange() == 1.5f) {
                    resourcesId[i] = ComplexStringFactor.BIG_FONT_SIZE + ComplexStringFactor.FONT_SIZE_BASE;
                } else if (sizeSpan.getSizeChange() == 2.0f) {
                    resourcesId[i] = ComplexStringFactor.HUGE_FONT_SIZE + ComplexStringFactor.FONT_SIZE_BASE;
                }
                throw new Exception();
            } else if (span instanceof ImageSpan) {
                ImageSpan imageSpan = (ImageSpan)span;
                if (imageSpan.getSource() == null) {
                    Log.i("PASS", "ComplexString: pass an image at" + position[i]);
                    continue;
                }
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(imageSpan.getSource()));
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                this.data.add(data);
                resourcesId[i] = ComplexStringFactor.IMAGE;
            } else if (span instanceof URLSpan) {
                data.add(((URLSpan)span).getURL().getBytes());
                resourcesId[i] = ComplexStringFactor.HYPERLINK;
            } else if (span instanceof UnderlineSpan) {
                resourcesId[i] = ComplexStringFactor.UNDERLINE;
            } else if (span instanceof ForegroundColorSpan) {
                ForegroundColorSpan colorSpan = (ForegroundColorSpan)span;
                if (colorSpan.getForegroundColor() == 0xff0000) {
                    resourcesId[i] = ComplexStringFactor.TEXT_COLOR_BASE + ComplexStringFactor.RED_COLOR;
                } else if (colorSpan.getForegroundColor() == 0x00ff00) {
                    resourcesId[i] = ComplexStringFactor.TEXT_COLOR_BASE + ComplexStringFactor.GREEN_COLOR;
                } else if (colorSpan.getForegroundColor() == 0x0000ff) {
                    resourcesId[i] = ComplexStringFactor.TEXT_COLOR_BASE + ComplexStringFactor.BLUE_COLOR;
                } else if (colorSpan.getForegroundColor() == 0xffff00) {
                    resourcesId[i] = ComplexStringFactor.TEXT_COLOR_BASE + ComplexStringFactor.YELLOW_COLOR;
                } else if (colorSpan.getForegroundColor() == 0xff00ff) {
                    resourcesId[i] = ComplexStringFactor.TEXT_COLOR_BASE + ComplexStringFactor.PURPLE_COLOR;
                }
            } else if (span instanceof BackgroundColorSpan) {
                BackgroundColorSpan colorSpan = (BackgroundColorSpan)span;
                if (colorSpan.getBackgroundColor() == 0xff0000) {
                    resourcesId[i] = ComplexStringFactor.BACKGROUND_BASE + ComplexStringFactor.RED_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0x00ff00) {
                    resourcesId[i] = ComplexStringFactor.BACKGROUND_BASE + ComplexStringFactor.GREEN_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0x0000ff) {
                    resourcesId[i] = ComplexStringFactor.BACKGROUND_BASE + ComplexStringFactor.BLUE_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0xffff00) {
                    resourcesId[i] = ComplexStringFactor.BACKGROUND_BASE + ComplexStringFactor.YELLOW_COLOR;
                } else if (colorSpan.getBackgroundColor() == 0xff00ff) {
                    resourcesId[i] = ComplexStringFactor.BACKGROUND_BASE + ComplexStringFactor.PURPLE_COLOR;
                }
            } else if (span instanceof SuperscriptSpan) {
                resourcesId[i] = ComplexStringFactor.SUPERSCRIPT;
            } else if (span instanceof SubscriptSpan) {
                resourcesId[i] = ComplexStringFactor.SUBSCRIPT;
            } else if (span instanceof StrikethroughSpan) {
                resourcesId[i] = ComplexStringFactor.STRIKE_THROUGH;
            } else {
                throw new Exception();
            }
        }
    }

    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("content", content.toString());
        goal.put("position", toJsonArray(position));
        goal.put("width", toJsonArray(width));
        goal.put("resources", toJsonArray(resourcesId));
        return goal;
    }

    private JSONArray toJsonArray(int[] list) {
        JSONArray array = new JSONArray();
        for (int one : list) {
            array.put(one);
        }
        return array;
    }

    public ArrayList<byte[]> getData() {
        return data;
    }
}
