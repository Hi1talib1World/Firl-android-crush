package com.Denzo.firl.contract;

public interface IIssueTimelineContract {

    interface View extends IBaseContract.View, IBaseListContract.View {
        void showTimeline(ArrayList<IssueEvent> events);
        void showEditCommentPage(String commentId, String body);
    }

    interface Presenter extends IBaseContract.Presenter<IIssueTimelineContract.View> {
        void loadTimeline(int page, boolean isReload);
        boolean isEditAndDeleteEnable(int position);
        void deleteComment(String commentId);
        void editComment(String commentId, String body);
    }

}
