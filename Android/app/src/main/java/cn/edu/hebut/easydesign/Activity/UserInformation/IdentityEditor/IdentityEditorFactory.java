package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;

import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.Session.User.Identity;

public class IdentityEditorFactory {
    public static IdentityEditor getInstance(Context context) {
        return getInstance(context, Session.getSession().user.identity);
    }

    public static IdentityEditor getInstance(Context context, Identity identity) {
        IdentityEditor goal;
        switch (identity.getType()) {
            case 0:
                goal = new StudentEditor(context);
                break;
            case 1:
                goal = new DesignerEditor(context);
                break;
            case 2:
                goal = new PublicEditor(context);
                break;
            default:
                throw new IllegalArgumentException("incorrect type");
        }
        goal.setDate(identity);
        return goal;
    }
}

