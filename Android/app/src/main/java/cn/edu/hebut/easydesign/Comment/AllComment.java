package cn.edu.hebut.easydesign.Comment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.Resources.Passage.Passage;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;

public class AllComment extends AppCompatActivity implements View.OnClickListener {

    private BottomSheetDialog dialog;
    private Adapter adapter;
    private TextView do_comment;
    private ExpandableListView expandableListView;
    private TextView back_to_passage;
    private List<Comment> commentsList;
    private Passage passage;
    private ArrayList<ArrayList<SubComment>> subComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_comment);
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() throws Exception {
        back_to_passage = findViewById(R.id.back_to_passage);
        expandableListView = findViewById(R.id.all_comments);
        do_comment = findViewById(R.id.write_comment);
        do_comment.setOnClickListener(this);
        back_to_passage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllComment.this, CommentActivity.class);

                startActivity(intent);
            }
        });
        commentsList = generateTestData();
        subComments = new ArrayList<>();
        for (Comment comment : commentsList) {
            subComments.add(comment.getReplayList());
        }
        initExpandableListView(commentsList, subComments);

    }

    private List<Comment> generateTestData() throws Exception {
        try {
            Intent intent = getIntent();
            String json = intent.getStringExtra("passage");
            passage = new Passage(new JSONObject(json), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Comment> commentList = passage.GetCommentList();
        return commentList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.detail_page_do_comment) {
            showCommentDialog();
        }
    }

    private void showCommentDialog() {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);

        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);

        commentView.measure(0, 0);

        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();

                if (!TextUtils.isEmpty(commentContent)) {

                    ComplexString complexContent = new ComplexString(commentContent);
                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    JSONObject CommentObject = new JSONObject();
                    JSONArray SubComments = new JSONArray();
                    try {

                        CommentObject.put("content", complexContent);
                        CommentObject.put("passage", passage.GetId());
                        CommentObject.put("position", 0);
                        CommentObject.put("owner", 0);
                        CommentObject.put("like", 0);
                        CommentObject.put("subCommentNumber", 0);
                        Comment mComment = new Comment(CommentObject, SubComments);
                        adapter.addTheCommentData(mComment);
                        Toast.makeText(AllComment.this, "评论成功", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AllComment.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();

                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {

                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private void showReplyDialog(final int position) {
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {
                    dialog.dismiss();
                    JSONObject ReplyObject = new JSONObject();
                    try {
                        ReplyObject.put("content", replyContent);
                        ReplyObject.put("passage", passage.GetId());
                        ReplyObject.put("position", 0);
                        ReplyObject.put("owner", 0);
                        ReplyObject.put("like", 0);
                        ReplyObject.put("father", passage.GetCommentList().get(position).position);
                        SubComment subcomment = new SubComment(ReplyObject);
                        adapter.addTheReplyData(subcomment, position);
                        expandableListView.expandGroup(position);
                        Toast.makeText(AllComment.this, "回复成功", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AllComment.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private void initExpandableListView(final List<Comment> commentList, ArrayList<ArrayList<SubComment>> subComments) {
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new Adapter(this, commentList, subComments);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < commentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.i("Test", "onGroupClick: 当前的评论id>>>" + commentList.get(groupPosition).getPosition());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(AllComment.this, "点击了回复", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(AllComment.this, "展开第" + groupPosition + "个分组", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
