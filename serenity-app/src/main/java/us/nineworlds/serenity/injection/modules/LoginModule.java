package us.nineworlds.serenity.injection.modules;

import dagger.Module;
import dagger.Provides;
import us.nineworlds.serenity.ui.activity.login.LoginUserPresenter;

@Module(library = true)
public class LoginModule {

  @Provides LoginUserPresenter providesLoginUserPresenter() {
    return new LoginUserPresenter();
  }
}
