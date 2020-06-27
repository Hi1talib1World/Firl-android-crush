package com.Denzo.firl.feed;


import android.content.Context;

import com.Denzo.firl.ApplicationHelper;
import com.Denzo.firl.feed.listeners.OnDataChangedListener;
import com.Denzo.firl.feed.listeners.OnTaskCompleteListener;
import com.Denzo.firl.feed.model.Comment;
import com.Denzo.firl.feed.utils.LogUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentInteractor {

    private static final String TAG = CommentInteractor.class.getSimpleName();
    private static CommentInteractor instance;

    private DatabaseHelper databaseHelper;
    private Context context;

    public static CommentInteractor getInstance(Context context) {
        if (instance == null) {
            instance = new CommentInteractor(context);
        }

        return instance;
    }

    private CommentInteractor(Context context) {
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
    }

    public void createComment(String commentText, final String postId, final OnTaskCompleteListener onTaskCompleteListener) {
        try {
            String authorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mCommentsReference = databaseHelper.getDatabaseReference().child(DatabaseHelper.POST_COMMENTS_DB_KEY + "/" + postId);
            String commentId = mCommentsReference.push().getKey();
            Comment comment = new Comment(commentText);
            comment.setId(commentId);
            comment.setAuthorId(authorId);

            mCommentsReference.child(commentId).setValue(comment, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        incrementCommentsCount(postId);
                    } else {
                        LogUtil.logError(TAG, databaseError.getMessage(), databaseError.toException());
                    }
                }

                private void incrementCommentsCount(String postId) {
                    DatabaseReference postRef = databaseHelper.getDatabaseReference().child(DatabaseHelper.POSTS_DB_KEY + "/" + postId + "/commentsCount");
                    postRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Integer currentValue = mutableData.getValue(Integer.class);
                            if (currentValue == null) {
                                mutableData.setValue(1);
                            } else {
                                mutableData.setValue(currentValue + 1);
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            LogUtil.logInfo(TAG, "Updating comments count transaction is completed.");
                            if (onTaskCompleteListener != null) {
                                onTaskCompleteListener.onTaskComplete(true);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            LogUtil.logError(TAG, "createComment()", e);
        }
    }

    public void updateComment(String commentId, String commentText, String postId, final OnTaskCompleteListener onTaskCompleteListener) {
        DatabaseReference mCommentReference = databaseHelper.getDatabaseReference().child(DatabaseHelper.POST_COMMENTS_DB_KEY).child(postId).child(commentId).child("text");
        mCommentReference.setValue(commentText).addOnSuccessListener(aVoid -> {
            if (onTaskCompleteListener != null) {
                onTaskCompleteListener.onTaskComplete(true);
            }
        }).addOnFailureListener(e -> {
            if (onTaskCompleteListener != null) {
                onTaskCompleteListener.onTaskComplete(false);
            }
            LogUtil.logError(TAG, "updateComment", e);
        });
    }

    public void decrementCommentsCount(String postId, final OnTaskCompleteListener onTaskCompleteListener) {
        DatabaseReference postRef = databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.POSTS_DB_KEY + "/" + postId + "/commentsCount");
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue != null && currentValue >= 1) {
                    mutableData.setValue(currentValue - 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                LogUtil.logInfo(TAG, "Updating comments count transaction is completed.");
                if (onTaskCompleteListener != null) {
                    onTaskCompleteListener.onTaskComplete(true);
                }
            }
        });
    }

    public void removeComment(String commentId, final String postId, final OnTaskCompleteListener onTaskCompleteListener) {
        DatabaseReference reference = databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.POST_COMMENTS_DB_KEY)
                .child(postId)
                .child(commentId);
        reference.removeValue().addOnSuccessListener(aVoid -> decrementCommentsCount(postId, onTaskCompleteListener)).addOnFailureListener(e -> {
            onTaskCompleteListener.onTaskComplete(false);
            LogUtil.logError(TAG, "removeComment()", e);
        });
    }

    public ValueEventListener getCommentsList(String postId, final OnDataChangedListener<Comment> onDataChangedListener) {
        DatabaseReference databaseReference = databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.POST_COMMENTS_DB_KEY)
                .child(postId);
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    list.add(comment);
                }

                Collections.sort(list, (lhs, rhs) -> ((Long) rhs.getCreatedDate()).compareTo((Long) lhs.getCreatedDate()));

                onDataChangedListener.onListChanged(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logError(TAG, "getCommentsList(), onCancelled", new Exception(databaseError.getMessage()));
            }
        });

        databaseHelper.addActiveListener(valueEventListener, databaseReference);
        return valueEventListener;
    }

    public Task<Void> removeCommentsByPost(String postId) {
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.POST_COMMENTS_DB_KEY)
                .child(postId)
                .removeValue();
    }

}