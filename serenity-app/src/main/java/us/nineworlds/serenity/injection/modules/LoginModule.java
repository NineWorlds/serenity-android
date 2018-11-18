package us.nineworlds.serenity.injection.modules;

import toothpick.config.Module;
import us.nineworlds.serenity.ui.activity.login.LoginUserPresenter;

public class LoginModule extends Module {

  public LoginModule() {
    bind(LoginUserPresenter.class).to(LoginUserPresenter.class);
  }

}
