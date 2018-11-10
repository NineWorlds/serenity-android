package us.nineworlds.serenity.events.users;

import us.nineworlds.serenity.common.rest.SerenityUser;

public class AuthenticatedUserEvent {

  private SerenityUser serenityUser;

  public AuthenticatedUserEvent(SerenityUser serenityUser) {
    this.serenityUser = serenityUser;
  }

  public SerenityUser getUser() {
    return serenityUser;
  }
}
