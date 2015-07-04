package org.wordpress.android.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import org.json.JSONObject;
import org.wordpress.android.R;
import org.wordpress.android.util.StringUtils;

/**
 * additional data for "discover" posts in the reader - these are posts chosen by
 * Editorial which highlight other posts - in the reader we show an attribution
 * line for these posts, and when tapped they open the original post - the like
 * and comment counts come from the original post
 */
public class ReaderPostDiscoverData {
    private String authorName;
    private String authorUrl;
    private String blogName;
    private String blogUrl;
    private String avatarUrl;
    private String permaLink;

    private long blogId;
    private long postId;

    public int numLikes;
    public int numComments;

    /*
     * passed JSONObject is the "discover_metadata" section of a reader post
     */
    public ReaderPostDiscoverData(@NonNull JSONObject json) {
        setPermaLink(json.optString("permalink"));

        JSONObject jsonAttribution = json.optJSONObject("attribution");
        if (jsonAttribution != null) {
            setAuthorName(jsonAttribution.optString("author_name"));
            setAuthorUrl(jsonAttribution.optString("author_url"));
            setBlogName(jsonAttribution.optString("blog_name"));
            setBlogUrl(jsonAttribution.optString("blog_url"));
            setAvatarUrl(jsonAttribution.optString("avatar_url"));
        }

        JSONObject jsonWpcomData = json.optJSONObject("featured_post_wpcom_data");
        if (jsonWpcomData != null) {
            blogId = jsonWpcomData.optLong("blog_id");
            postId = jsonWpcomData.optLong("post_id");
            numLikes = jsonWpcomData.optInt("like_count");
            numComments = jsonWpcomData.optInt("comment_count");
        }
    }

    public long getBlogId() {
        return blogId;
    }

    public long getPostId() {
        return postId;
    }

    public String getAuthorName() {
        return StringUtils.notNullStr(authorName);
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return StringUtils.notNullStr(authorUrl);
    }
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getBlogName() {
        return StringUtils.notNullStr(blogName);
    }
    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogUrl() {
        return StringUtils.notNullStr(blogUrl);
    }
    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getAvatarUrl() {
        return StringUtils.notNullStr(avatarUrl);
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPermaLink() {
        return StringUtils.notNullStr(permaLink);
    }
    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    /*
     * returns the spanned html for the attribution line, "Originally posted by [AuthorName] in [BlogName]"
     */
    public Spanned getAttributionHtml(Context context) {
        // TODO: this means that clicking the blog link opens the blog in the browser, but we want
        // to open the blog preview inside the app instead
        String blogLink = "<a href='" + getBlogUrl() + "'>" + getBlogName()  + "</a>";
        String authorLink = "<a href='" + getAuthorUrl() + "'>" + getAuthorName()  + "</a>";
        String html = context.getString(R.string.reader_discover_attribution, authorLink, blogLink);
        return Html.fromHtml(html);
    }
}
