package com.suncreate.pressuresensor.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.ListBaseAdapter;
import com.suncreate.pressuresensor.bean.Post;
import com.suncreate.pressuresensor.bean.PostList;
import com.suncreate.pressuresensor.util.HTMLUtil;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.ThemeSwitchUtils;
import com.suncreate.pressuresensor.widget.AvatarView;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * post（讨论区帖子）适配器
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:22:54
 */
public class PostAdapter extends ListBaseAdapter<Post> {

    static class ViewHolder {

        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tv_description)
        TextView description;
        @Bind(R.id.tv_author)
        TextView author;
        @Bind(R.id.tv_date)
        TextView time;
        @Bind(R.id.tv_count)
        TextView comment_count;

        @Bind(R.id.iv_face)
        public AvatarView face;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_post, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Post post = (Post) mDatas.get(position);

        vh.face.setUserInfo(post.getAuthorId(), post.getAuthor());
        vh.face.setAvatarUrl(post.getPortrait());
        vh.title.setText(post.getTitle());
        String body = post.getBody();
        vh.description.setVisibility(View.GONE);
        if (null != body || !StringUtils.isEmpty(body)) {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(HTMLUtil.replaceTag(post.getBody()).trim());
        }

        if (AppContext.isOnReadedPostList(PostList.PREF_READED_POST_LIST,
                post.getId() + "")) {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        vh.author.setText(post.getAuthor());
        vh.time.setText(StringUtils.friendly_time(post.getPubDate()));
        vh.comment_count.setText(post.getAnswerCount() + "回 / "
                + post.getViewCount() + "阅");

        return convertView;
    }
}
