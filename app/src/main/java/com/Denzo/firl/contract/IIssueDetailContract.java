package com.Denzo.firl.contract;

public interface IIssueDetailContract {

    interface View extends IBaseContract.View{
        void showIssue(@NonNull Issue issue);
        void showAddedComment(@NonNull IssueEvent event);
        void showAddCommentPage(@Nullable String text);
    }

    interface Presenter extends IBaseContract.Presenter<IIssueDetailContract.View> {
        void addComment(@NonNull String text);
        void toggleIssueState();
    }

}
