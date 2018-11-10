/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package us.nineworlds.serenity.core;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.bumptech.glide.Glide;
import java.io.IOException;
import us.nineworlds.serenity.injection.BaseInjector;

/*
 * This class builds recommendations as notifications with videos as inputs.
 */
@SuppressLint("InlinedApi") public class RecommendationBuilder extends BaseInjector {

  private static final String TAG = "RecommendationBuilder";

  private Context mContext;
  private NotificationManager mNotificationManager;

  private int mId;
  private int mPriority;
  private int mSmallIcon;
  private String mTitle;
  private String mDescription;
  private String mImageUri;
  private String mBackgroundUri;
  private String backgroundContentUri;
  private PendingIntent mIntent;
  private int cardColor;

  public RecommendationBuilder() {
  }

  public RecommendationBuilder setBackgroundContentUri(String contentUri) {
    backgroundContentUri = contentUri;
    return this;
  }

  public RecommendationBuilder setContext(Context context) {
    mContext = context;
    return this;
  }

  public RecommendationBuilder setId(int id) {
    mId = id;
    return this;
  }

  public RecommendationBuilder setPriority(int priority) {
    mPriority = priority;
    return this;
  }

  public RecommendationBuilder setTitle(String title) {
    mTitle = title;
    return this;
  }

  public RecommendationBuilder setDescription(String description) {
    mDescription = description;
    return this;
  }

  public RecommendationBuilder setImage(String uri) {
    mImageUri = uri;
    return this;
  }

  public RecommendationBuilder setBackground(String uri) {
    mBackgroundUri = uri;
    return this;
  }

  public RecommendationBuilder setIntent(PendingIntent intent) {
    mIntent = intent;
    return this;
  }

  public RecommendationBuilder setSmallIcon(int resourceId) {
    mSmallIcon = resourceId;
    return this;
  }

  public RecommendationBuilder setColor(int colorValue) {
    cardColor = colorValue;
    return this;
  }

  public Notification build() throws IOException {

    Log.d(TAG, "Building notification - " + this.toString());

    if (mNotificationManager == null) {
      mNotificationManager =
          (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    Bundle extras = new Bundle();
    if (mBackgroundUri != null) {
      if (backgroundContentUri != null) {
        extras.putString(Notification.EXTRA_BACKGROUND_IMAGE_URI, backgroundContentUri);
      }
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
    builder = builder.setContentTitle(mTitle)
        .setContentText(mDescription)
        .setPriority(mPriority)
        .setOngoing(true)
        .setLocalOnly(true)
        .setColor(cardColor)
        .setSmallIcon(mSmallIcon)
        .setCategory(Notification.CATEGORY_RECOMMENDATION)
        .setContentIntent(mIntent)
        .setExtras(extras);

    try {
      Bitmap image = Glide.with(mContext).load(mImageUri).asBitmap().into(176, 313).get();

      builder = builder.setLargeIcon(image);
    } catch (Exception ex) {
    }

    Notification notification = new NotificationCompat.BigPictureStyle(builder).build();
    try {
      mNotificationManager.notify(mId, notification);
    } catch (Exception ex) {
      Log.e(getClass().getName(), ex.getMessage(), ex);
    }

    mNotificationManager = null;
    return notification;
  }

  @Override public String toString() {
    return "RecommendationBuilder{"
        + ", mId="
        + mId
        + ", mPriority="
        + mPriority
        + ", mSmallIcon="
        + mSmallIcon
        + ", mTitle='"
        + mTitle
        + '\''
        + ", mDescription='"
        + mDescription
        + '\''
        + ", mImageUri='"
        + mImageUri
        + '\''
        + ", mBackgroundUri='"
        + mBackgroundUri
        + '\''
        + ", mIntent="
        + mIntent
        + '}';
  }
}
