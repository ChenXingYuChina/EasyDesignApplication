package cn.edu.hebut.easydesign.Comment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
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

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomSheetDialog dialog;
    private Adapter adapter;
    private TextView bt_comment;
    private ExpandableListView expandableListView;
    private TextView detail_page_story;
    private TextView show_all_comments;
    private List<Comment> commentsList;
    private ArrayList<ArrayList<SubComment>> subComments;
    private Passage passage;

    private String json = "{\"body\":{\"content\":\"content\",\"position\":[],\"width\":[],\"resources\":[]},\"id\":3,\"media\":null," +
            "\"com\":[{\"content\":{\"content\":\"RealComment1\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":0,\"position\":0,\"sub_com_number\":2}," +
            "{\"content\":{\"content\":\"RealComment2\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":1,\"position\":0,\"sub_com_number\":2}," +
            "{\"content\":{\"content\":\"RealComment3\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":2,\"position\":0,\"sub_com_number\":2}," +
            "{\"content\":{\"content\":\"RealComment4\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":3,\"position\":0,\"sub_com_number\":2}," +
            "{\"content\":{\"content\":\"RealComment5\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":4,\"position\":0,\"sub_com_number\":2}," +
            "{\"content\":{\"content\":\"RealComment6\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":5,\"position\":0,\"sub_com_number\":0}," +
            "{\"content\":{\"content\":\"RealComment7\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":6,\"position\":0,\"sub_com_number\":0}," +
            "{\"content\":{\"content\":\"RealComment8\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":7,\"position\":0,\"sub_com_number\":0}," +
            "{\"content\":{\"content\":\"RealComment9\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":8,\"position\":0,\"sub_com_number\":1}," +
            "{\"content\":{\"content\":\"RealComment10\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":9,\"position\":0,\"sub_com_number\":0}," +
            "{\"content\":{\"content\":\"RealComment11\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":5,\"position\":0,\"sub_com_number\":0}," +
            "{\"content\":{\"content\":\"RealComment12\",\"position\":[],\"width\":[],\"resources\":[]},\"passage\":3,\"owner\":48,\"like\":156,\"position\":0,\"sub_com_number\":4}]," +
            "\"sub_com\":[" +
            "[" +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment11\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment12\",\"like\":0,\"father\":1,\"position\":0}" +
            "]," +
            "[" +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment21\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment22\",\"like\":0,\"father\":1,\"position\":0}" +
            "]," +
            "[{\"passage\":3,\"owner\":48,\"content\":\"subcomment31\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment32\",\"like\":0,\"father\":1,\"position\":0}" +
            "]," +
            "[" +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment41\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment42\",\"like\":0,\"father\":1,\"position\":0}" +
            "]," +
            "[{\"passage\":3,\"owner\":48,\"content\":\"subcomment51\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment52\",\"like\":0,\"father\":1,\"position\":0}" +
            "]," +
            "[],[],[]," +
            "[" +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment91\",\"like\":1,\"father\":1,\"position\":0}" +
            "]," +
            "[],[]," +
            "[" +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment12_1\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment12_2\",\"like\":0,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment12_3\",\"like\":1,\"father\":1,\"position\":0}," +
            "{\"passage\":3,\"owner\":48,\"content\":\"subcomment12_4\",\"like\":0,\"father\":1,\"position\":0}" +
            "]],\"full\":true}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_main);
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() throws Exception {
        show_all_comments = findViewById(R.id.show_all_comments);
        expandableListView = findViewById(R.id.detail_page_lv_comment);
        bt_comment = findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        commentsList = generateTestData();
        subComments = new ArrayList<>();
        for (Comment comment : commentsList) {
            subComments.add(comment.getReplayList());
        }
        show_all_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivity.this, AllComment.class);
                intent.putExtra("passage", json);
                startActivity(intent);
            }
        });
        initExpandableListView(commentsList, subComments);
    }

    private List<Comment> generateTestData() throws Exception {
        try {
            detail_page_story = findViewById(R.id.detail_page_story);
            passage = new Passage(new JSONObject(json), true);
            detail_page_story.setText(passage.GetContent().GetSpannableString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Comment> commentList = passage.GetCommentList().subList(0, 3);
        Log.i("Test", String.valueOf(commentList.size()));
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
                    SpannableString spannableContent = new ComplexString(commentContent).GetSpannableString();
                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    JSONObject CommentObject = new JSONObject();
                    JSONObject ContentObject = new JSONObject();
                    JSONArray SubComments = new JSONArray();
                    JSONArray position = new JSONArray();
                    JSONArray width = new JSONArray();
                    JSONArray resources = new JSONArray();

                    try {
                        ContentObject.put("content", spannableContent);
                        ContentObject.put("position", position);
                        ContentObject.put("width", width);
                        ContentObject.put("resources", resources);

                        CommentObject.put("content", ContentObject);
                        CommentObject.put("passage", passage.GetId());
                        CommentObject.put("position", 0);
                        CommentObject.put("owner", 0);
                        CommentObject.put("like", 0);
                        CommentObject.put("sub_com_number", 0);
                        Comment mComment = new Comment(CommentObject, SubComments);
                        adapter.addTheCommentData(mComment);
                        Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
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
                        System.out.println("wrong here");
                        expandableListView.expandGroup(position);
                        Toast.makeText(CommentActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CommentActivity.this, "点击了回复", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(CommentActivity.this, "展开第" + groupPosition + "个分组", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
