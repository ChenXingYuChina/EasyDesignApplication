package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageMultiListView;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.Session.User.Designer;
import cn.edu.hebut.easydesign.Session.User.Public;
import cn.edu.hebut.easydesign.Session.User.School;
import cn.edu.hebut.easydesign.Session.User.Student;
import cn.edu.hebut.easydesign.Session.User.User;
import cn.edu.hebut.easydesign.Session.User.Work;
import cn.edu.hebut.easydesign.Tools.ObjectHolder;

public class UserDescriptionPage implements Page<PassageMultiListView> {
    private ComplexString longDescription;
    private FrameLayout view;
    private User user;
    private reloadDescription retry;
    private boolean canRefresh = false;

    public UserDescriptionPage() {
        view = new FrameLayout(ContextHolder.getContext());
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public boolean canRefresh() {
        return canRefresh;
    }

    private UserTop top;
    @Override
    public void bind(@NonNull PassageMultiListView father) {
        top = (UserTop) father.getHead();
        top.editDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && longDescription != null) {
                    ObjectHolder.getInstance().put("editUser", new userFull(user, longDescription));
//                Intent intent = new Intent(ContextHolder.getContext(), )
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (retry != null) {
            if (view.getChildCount() != 0) {
                view.removeAllViews();
            }
            retry.retry();
        }
    }

    public void setLongDescription(@NonNull userFull userFull) {
        this.longDescription = userFull.longDescription;
        this.user = userFull.user;
        if (view.getChildCount() > 0) {
            view.removeAllViews();
        }
        TextView description;
        if (user.identity instanceof Designer) {
            description = setupDesigner((Designer) userFull.user.identity);
        } else if (user.identity instanceof Public) {
            description = setupPublic((Public) userFull.user.identity);
        } else if (user.identity instanceof Student) {
            description = setupStudent((Student) userFull.user.identity, user.id);
        } else {
            throw new IllegalArgumentException();
        }
        longDescription.SetToTextView(description);
        canRefresh = true;
    }

    @SuppressLint("SetTextI18n")
    public void setErrorCode(int errorCode, reloadDescription retry) {
        this.retry = retry;
        TextView textView = new TextView(ContextHolder.getContext());
        textView.setText("加载错误，错误代码：" + errorCode + " 点击重试。");
        view.removeAllViews();
        view.addView(textView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    public static interface reloadDescription {
        void retry();
    }

    public static class userFull {
        User user;
        ComplexString longDescription;

        public userFull(User u, ComplexString ld) {
            user = u;
            longDescription = ld;
        }
    }

    @NonNull
    private TextView setupDesigner(Designer designer) {
        Context context = ContextHolder.getContext();
        ViewGroup page = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.user_description_designer_page_layout, view);
        ViewGroup list = page.findViewById(R.id.workList);
        for (Work work : designer.works) {
            UserWorkCard workCard = new UserWorkCard(context);
            workCard.setWork(work);
            list.addView(workCard, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        return page.findViewById(R.id.description);
    }

    @NonNull
    private TextView setupPublic(Public p) {
        Context context = ContextHolder.getContext();
        ViewGroup page = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.user_description_public_page_layout, view);
        TextView industry = page.findViewById(R.id.industry);
        TextView position = page.findViewById(R.id.position);
        industry.setText(p.industry);
        position.setText(p.position);
        return page.findViewById(R.id.description);
    }

    @NonNull
    private TextView setupStudent(Student student, long id) {
        boolean showPrivate = false;
        if (Session.getSession().isTheLoginUser(id)) {
            showPrivate = true;
        }
        Context context = ContextHolder.getContext();
        ViewGroup page = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.user_description_student_page_layout, view);
        ViewGroup list = page.findViewById(R.id.school_list);
        for (School school : student.schools) {
            if (school.publicSchool || showPrivate) {
                UserSchoolCard card = new UserSchoolCard(context);
                card.setSchool(school);
                list.addView(card, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }
        return page.findViewById(R.id.description);
    }

}
