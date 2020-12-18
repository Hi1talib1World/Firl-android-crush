package com.Denzo.firl.contract;

public interface IBookmarkContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showBookmarks(ArrayList<BookmarkExt> bookmarks);
        void notifyItemAdded(int position);
    }

    interface Presenter extends IBaseContract.Presenter<IBookmarkContract.View>{
        void loadBookmarks(int page);
        void removeBookmark(int position);
        void undoRemoveBookmark();
    }

}