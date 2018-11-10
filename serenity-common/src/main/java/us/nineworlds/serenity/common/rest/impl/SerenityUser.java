package us.nineworlds.serenity.common.rest.impl;

import us.nineworlds.serenity.common.Server;

public class SerenityUser implements us.nineworlds.serenity.common.rest.SerenityUser {

  private final String userName;
  private final String userId;
  private final boolean hasPassword;
  private final Server serverInfo;
  private final String accessToken;

  private SerenityUser(Builder builder) {
    this.userName = builder.userName;
    this.userId = builder.userId;
    this.hasPassword = builder.hasPassword;
    this.serverInfo = builder.serverInfo;
    this.accessToken = builder.accessToken;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override public String getUserName() {
    return userName;
  }

  @Override public String getUserId() {
    return userId;
  }

  @Override public String getAccessToken() {
    return accessToken;
  }

  @Override public boolean hasPassword() {
    return hasPassword;
  }

  @Override public Server getUserServer() {
    return serverInfo;
  }

  public static class Builder {
    private String userName;
    private String userId;
    private boolean hasPassword;
    private Server serverInfo;
    private String accessToken;

    public Builder userName(String userName) {
      this.userName = userName;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder hasPassword(boolean hasPassword) {
      this.hasPassword = hasPassword;
      return this;
    }

    public Builder serverInfo(Server serverInfo) {
      this.serverInfo = serverInfo;
      return this;
    }

    public Builder accessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public SerenityUser build() {
      return new SerenityUser(this);
    }
  }
}
