package com.tehmou.rxbookapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tehmou.rxbookapp.data.DataStore;
import com.tehmou.rxbookapp.utils.SubscriptionUtils;
import com.tehmou.rxbookapp.viewmodels.BookViewModel;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ttuo on 19/03/14.
 */
public class BookFragment extends Fragment {
    final private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private BookViewModel bookViewModel;

    private TextView bookNameTextView;
    private TextView bookAuthorTextView;
    private TextView bookPriceTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookViewModel = new BookViewModel(DataStore.getInstance(), "436346");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookNameTextView = (TextView) getView().findViewById(R.id.book_name);
        bookAuthorTextView = (TextView) getView().findViewById(R.id.book_author);
        bookPriceTextView = (TextView) getView().findViewById(R.id.book_price);
    }

    private void subscribeTextView(Observable<String> observable,
                                   final TextView textView) {
        compositeSubscription.add(SubscriptionUtils.subscribeTextViewText(observable, textView));
    }

    @Override
    public void onResume() {
        super.onResume();
        bookViewModel.subscribeToDataStore();

        subscribeTextView(bookViewModel.getBookName(), bookNameTextView);
        subscribeTextView(bookViewModel.getAuthorName(), bookAuthorTextView);
        subscribeTextView(bookViewModel.getBookPrice(), bookPriceTextView);
    }

    @Override
    public void onPause() {
        super.onPause();
        bookViewModel.unsubscribeFromDataStore();
        compositeSubscription.clear();
    }
}
