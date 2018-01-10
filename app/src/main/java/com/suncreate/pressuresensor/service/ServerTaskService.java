package com.suncreate.pressuresensor.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.suncreate.pressuresensor.R;

import java.util.ArrayList;
import java.util.List;

public class ServerTaskService extends IntentService {
    private static final String SERVICE_NAME = "ServerTaskService";
    public static final String ACTION_PUB_BLOG_COMMENT = "com.suncreate.pressuresensor.ACTION_PUB_BLOG_COMMENT";
    public static final String ACTION_PUB_COMMENT = "com.suncreate.pressuresensor.ACTION_PUB_COMMENT";
    public static final String ACTION_PUB_POST = "com.suncreate.pressuresensor.ACTION_PUB_POST";
    public static final String ACTION_PUB_TWEET = "com.suncreate.pressuresensor.ACTION_PUB_TWEET";
    public static final String ACTION_PUB_SOFTWARE_TWEET = "com.suncreate.pressuresensor" +
            ".ACTION_PUB_SOFTWARE_TWEET";

    public static final String KEY_ADAPTER = "adapter";

    public static final String BUNDLE_PUB_COMMENT_TASK = "BUNDLE_PUB_COMMENT_TASK";
    public static final String BUNDLE_PUB_POST_TASK = "BUNDLE_PUB_POST_TASK";
    public static final String BUNDLE_PUB_TWEET_TASK = "BUNDLE_PUB_TWEET_TASK";
    public static final String BUNDLE_PUB_SOFTWARE_TWEET_TASK = "BUNDLE_PUB_SOFTWARE_TWEET_TASK";
    public static final String KEY_SOFTID = "soft_id";

    private static final String KEY_COMMENT = "comment_";
    private static final String KEY_TWEET = "tweet_";
    private static final String KEY_SOFTWARE_TWEET = "software_tweet_";
    private static final String KEY_POST = "post_";

    public static List<String> penddingTasks = new ArrayList<String>();

    public ServerTaskService() {
        this(SERVICE_NAME);
    }

    private synchronized void tryToStopServie() {
        if (penddingTasks == null || penddingTasks.size() == 0) {
            stopSelf();
        }
    }

    private synchronized void addPenddingTask(String key) {
        penddingTasks.add(key);
    }

    private synchronized void removePenddingTask(String key) {
        penddingTasks.remove(key);
    }

    public ServerTaskService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

//        if (ACTION_PUB_BLOG_COMMENT.equals(action)) {
//            PublicCommentTask task = intent
//                    .getParcelableExtra(BUNDLE_PUB_COMMENT_TASK);
//            if (task != null) {
//                publicBlogComment(task);
//            }
//        } else if (ACTION_PUB_COMMENT.equals(action)) {
//            PublicCommentTask task = intent
//                    .getParcelableExtra(BUNDLE_PUB_COMMENT_TASK);
//            if (task != null) {
//                publicComment(task);
//            }
//        }
    }

    private void notifySimpleNotifycation(int id, String ticker, String title,
                                          String content, boolean ongoing, boolean autoCancel) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setSmallIcon(R.drawable.ic_notification);

        // if (AppContext.isNotificationSoundEnable()) {
        // builder.setDefaults(Notification.DEFAULT_SOUND);
        // }

        Notification notification = builder.build();

        NotificationManagerCompat.from(this).notify(id, notification);
    }

    private void cancellNotification(int id) {
        NotificationManagerCompat.from(this).cancel(id);
    }
}
