package us.nineworlds.serenity.events.video;

public class OnScreenDisplayEvent {

  private boolean isShowing;

  public OnScreenDisplayEvent(boolean displayed) {
    isShowing = displayed;
  }

  public boolean isShowing() {
    return isShowing;
  }

  public void setShowing(boolean showing) {
    isShowing = showing;
  }
}
