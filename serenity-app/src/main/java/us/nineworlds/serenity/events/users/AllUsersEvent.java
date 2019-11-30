package us.nineworlds.serenity.events.users;

import androidx.annotation.NonNull;
import java.util.List;
import us.nineworlds.serenity.common.rest.SerenityUser;

public class AllUsersEvent {

  private List<SerenityUser> users;

  public AllUsersEvent(@NonNull List<SerenityUser> users) {
    this.users = users;
  }

  @NonNull public List<SerenityUser> getUsers() {
    return users;
  }

}
