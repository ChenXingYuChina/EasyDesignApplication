package cn.edu.hebut.easydesign.ComplexString;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.Tools.ResourcesTools;

public class ComplexStringLoader {
    private static ComplexStringLoader instance = new ComplexStringLoader();

    public static ComplexStringLoader getInstance() {
        return instance;
    }

    private ComplexStringLoader() {

    }

    public static ComplexString LoadFromNet(JSONObject complexString) throws Exception {
        return new ComplexString(complexString);
    }


}
