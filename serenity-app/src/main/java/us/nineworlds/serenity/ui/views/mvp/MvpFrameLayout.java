package us.nineworlds.serenity.ui.views.mvp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.MvpPresenter;

public abstract class MvpFrameLayout extends FrameLayout {

  protected MvpDelegate mvpDelegate;

  public MvpFrameLayout(@NonNull Context context) {
    super(context);
    initMvp();
  }

  public MvpFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initMvp();
  }

  public MvpFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initMvp();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public MvpFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr,
      @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initMvp();
  }

  protected void initMvp() {
    MvpPresenter presenter = getPresenter();
    if (presenter == null) {
      getMvpDelegate().onCreate();
      getMvpDelegate().onAttach();
    }
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    getMvpDelegate().onCreate();
    getMvpDelegate().onAttach();
  }

  @Override protected Parcelable onSaveInstanceState() {
    getMvpDelegate().onSaveInstanceState();
    return super.onSaveInstanceState();
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    getMvpDelegate().onDetach();
    getMvpDelegate().onDestroyView();
  }

  protected MvpDelegate getMvpDelegate() {
    if (mvpDelegate == null) {
      mvpDelegate = new MvpDelegate<>(this);
    }
    return mvpDelegate;
  }

  protected abstract MvpPresenter getPresenter();
}
