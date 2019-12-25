package us.nineworlds.serenity;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideMultipleUsersOption();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showMultipleUsersOption();
}
