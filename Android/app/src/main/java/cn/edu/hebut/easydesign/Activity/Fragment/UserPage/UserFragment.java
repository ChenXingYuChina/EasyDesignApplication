package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.UserLastByType;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.PassageListPage;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageMultiListView;
import cn.edu.hebut.easydesign.Activity.commonComponents.HalfAboveDialog;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.Session.User.LoadUserLDTask;
import cn.edu.hebut.easydesign.Session.User.LoadUserTask;
import cn.edu.hebut.easydesign.Session.User.User;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class UserFragment extends Fragment implements UserDescriptionPage.reloadDescription {
    private long userID;
    private User user;
    private ComplexString longDescription;
    private UserDescriptionPage descriptionPage;
    private UserFixedPart fixedPart;
    private PassageMultiListView list;
    private HalfAboveDialog dialog;
    private FollowList followList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        descriptionPage = new UserDescriptionPage();
        View page = inflater.inflate(R.layout.user_fragment, container, false);
        list = page.findViewById(R.id.user_page_list);
        dialog = page.findViewById(R.id.user_dialog);
        fixedPart = new UserFixedPart(ContextHolder.getContext());
        List<Page> pages = new ArrayList<>(4);
        pages.add(descriptionPage);
        Log.i("UPF", userID + " ");
        pages.add(new PassageListPage(new UserLastByType((short) 0, userID)));
        pages.add(new PassageListPage(new UserStar(userID)));
        pages.add(new PassageListPage(new UserLastByType((short) 1, userID)));
        list.init(pages, fixedPart);
        if (user == null) {
            loadUser();
        }
        if (longDescription == null) {
            loadLongDescription();
        }
        initData(list);
        return page;
    }

    public final static int NOW_USER = -1;

    public static UserFragment getInstance(long id) {
        UserFragment goal = new UserFragment();
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
        return goal;
    }

    private void loadUser() {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadUserTask(userID) {
            @Override
            protected void onSuccess(User user) {
                UserFragment.this.user = user;
            }

            @Override
            protected void onError(int errCode) {
                user = null;
                descriptionPage.setErrorCode(errCode, UserFragment.this);
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadLongDescription() {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadUserLDTask(userID) {

            @Override
            protected void setLongDescription(ComplexString longDescription) {
                UserFragment.this.longDescription = longDescription;
                if (Session.getSession().isTheLoginUser(userID)) {
                    Session.getSession().longDescription = longDescription;
                }
            }

            @Override
            protected void onError(int errCode) {
                longDescription = null;
                descriptionPage.setErrorCode(errCode, UserFragment.this);
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initData(final PassageMultiListView list) {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new Task() {
            @Override
            protected void doOnMain() {
                if (longDescription != null && user != null) {
                    UserTop top = (UserTop) list.getHead();
                    top.setUser(user);
                    descriptionPage.setLongDescription(new UserDescriptionPage.userFull(user, longDescription));
                    top.setFollowOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (followList == null) {
                                followList = new FollowList(ContextHolder.getContext());
                            }
                            followList.setWho(userID, new Condition<>(false));

                            dialog.show(followList);
                            dialog.setOnClose(new HalfAboveDialog.onClose() {
                                @Override
                                public void onClose(View content) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            protected boolean doOnService() {
                return true;
            }
        });
    }

    @Override
    public void retry() {
        loadUser();
        loadLongDescription();
        initData(list);
        list.closeRefresh();
    }
}
