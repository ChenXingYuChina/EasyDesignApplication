package cn.edu.hebut.easydesign.Activity.UserInformation.Editor;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity.CachedIdentity;
import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;

public abstract class IdentityEditor<T extends CachedIdentity> extends FrameLayout {
    public IdentityEditor(@NonNull Context context) {
        super(context);
    }

    protected abstract void setData(T cachedIdentity);

    protected abstract void setToCache(UserInformation userInformation);
}
