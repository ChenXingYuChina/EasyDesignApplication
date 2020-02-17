package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageMultiListView;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.Session.User.LoadUserLDTask;
import cn.edu.hebut.easydesign.Session.User.LoadUserTask;
import cn.edu.hebut.easydesign.Session.User.User;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class UserPageFragment extends Fragment {
    private long userID;
    private User user;
    private ComplexString longDescription;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PassageMultiListView list = (PassageMultiListView) inflater.inflate(R.layout.user_fragment, container, false);
        return list;
    }

    public final static int NOW_USER = -1;

    public static UserPageFragment getInstance(long id) {
        UserPageFragment goal = new UserPageFragment();
        if (id == NOW_USER) {
            goal.user = Session.getSession().user;
            if (goal.user == null) {
                throw new IllegalArgumentException();
            }
            goal.userID = goal.user.id;
            goal.longDescription = Session.getSession().longDescription;
        } else {
            goal.userID = id;
        }
        if (goal.user != null) {
            goal.loadUser();
        }
        if (goal.longDescription != null) {
            goal.loadLoadDescription();
        }
        return goal;
    }

    public void loadUser() {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadUserTask(userID) {
            @Override
            protected void onSuccess(User user) {
                UserPageFragment.this.user = user;
            }

            @Override
            protected void onError(int errCode) {
                user = null;
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadLoadDescription() {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadUserLDTask(userID) {

            @Override
            protected void setLongDescription(ComplexString longDescription) {
                UserPageFragment.this.longDescription = longDescription;
                if (Session.getSession().isTheLoginUser(userID)) {
                    Session.getSession().longDescription = longDescription;
                }
            }

            @Override
            protected void onError(int errCode) {
                longDescription = null;
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initData(final PassageMultiListView list) {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new Task() {
            @Override
            protected void doOnMain() {
                if (longDescription != null && user != null) {
//                    list.init();
                }
            }

            @Override
            protected boolean doOnService() {
                return true;
            }
        });
    }
}
