package cn.edu.hebut.easydesign.Comment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Passage;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;

public class Adapter extends BaseExpandableListAdapter {
    private List<Comment> CommentList;
    private Context context;
    private ArrayList<ArrayList<SubComment>> SubCommentList;

    public Adapter(Context context, List<Comment> commentBeanList, ArrayList<ArrayList<SubComment>> subComments)               {
        this.context = context;
        this.CommentList = commentBeanList;
        this.SubCommentList = subComments;
    }

    @Override
    public int getGroupCount() {
        if(this.CommentList == null){
            return 0;
        }
        else {
        return this.CommentList.size();
    }}

    @Override
    public int getChildrenCount(int i) {
        if(CommentList.get(i).getSubComments() == null){
            return 0;
        }else {
            return CommentList.get(i).getSubComments().size()>0 ? CommentList.get(i).getSubComments().size():0;
        }

    }

    @Override
    public Object getGroup(int i) {
        return CommentList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return CommentList.get(i).getSubComments().get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    boolean isLike = false;

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.comment_content.setText(CommentList.get(groupPosition).getContent().GetSpannableString());
        groupHolder.comment_like_number.setText(String.valueOf(CommentList.get(groupPosition).getLikeNumber()));
        groupHolder.comment_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike){
                    isLike = false;
                    Integer number_dislike = Integer.parseInt(groupHolder.comment_like_number.getText().toString().trim());
                    groupHolder.comment_like.setColorFilter(Color.parseColor("#aaaaaa"));
                    groupHolder.comment_like_number.setText(String.valueOf(--number_dislike));
                }else {
                    isLike = true;
                    Integer number_like = Integer.parseInt(groupHolder.comment_like_number.getText().toString().trim());
                    groupHolder.comment_like.setColorFilter(Color.parseColor("#FF5C5C"));
                    groupHolder.comment_like_number.setText(String.valueOf(++number_like));
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.replay_like_number.setText(String.valueOf(CommentList.get(groupPosition).getSubComments().get(childPosition).getLikeNumber()));
        if(CommentList.get(groupPosition).getSubComments() != null){
            childHolder.reply_content.setText(CommentList.get(groupPosition).getSubComments().get(childPosition).getContent());
        }
        childHolder.reply_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike){
                    isLike = false;
                    Integer number_dislike = Integer.parseInt(childHolder.replay_like_number.getText().toString().trim());
                    childHolder.replay_like_number.setText(String.valueOf(--number_dislike));
                    childHolder.reply_like.setColorFilter(Color.parseColor("#aaaaaa"));

                }else {
                    isLike = true;
                    Integer number_like = Integer.parseInt(childHolder.replay_like_number.getText().toString().trim());
                    childHolder.replay_like_number.setText(String.valueOf(++number_like));
                    childHolder.reply_like.setColorFilter(Color.parseColor("#FF5C5C"));

                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder{
        private TextView  comment_content, comment_like_number;
        private ImageView comment_like;
        public GroupHolder(View view) {
            comment_content = view.findViewById(R.id.comment_item_content);
            comment_like = view.findViewById(R.id.comment_item_like);
            comment_like_number = view.findViewById(R.id.comment_like_number);
        }
    }

    private class ChildHolder{
        private TextView  reply_content,replay_like_number;
        private ImageView reply_like;
        public ChildHolder(View view) {
            reply_content = (TextView) view.findViewById(R.id.reply_item_content);
            reply_like = view.findViewById(R.id.reply_item_like);
            replay_like_number = view.findViewById(R.id.replay_like_number);
        }
    }

    public void addTheCommentData(Comment comment){
        if(comment!=null){

            CommentList.add(comment);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }
    }

    public void addTheReplyData(SubComment subComment, int groupPosition){
        if(subComment!=null){
            ArrayList<SubComment> SubList;
            SubList = SubCommentList.get(groupPosition);
            SubList.add(subComment);
            notifyDataSetChanged();
            }
        else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }
}

