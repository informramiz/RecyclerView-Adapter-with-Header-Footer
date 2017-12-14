import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by ramiz on 12/4/17.
 */

public abstract class RecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int VIEW_TYPE_HEADER = 0;
    protected static final int VIEW_TYPE_ITEM = 1;
    protected static final int VIEW_TYPE_FOOTER = 2;

    private boolean isHeaderAdded = false;
    private boolean isFooterAdded = false;
    @NonNull
    protected List<T> items = new ArrayList<>();
    @Nullable
    protected OnItemClickListener onItemClickListener;

    public RecyclerViewBaseAdapter(@NonNull List<T> itemsToAdd) {
        items.addAll(itemsToAdd);
    }

    @CallSuper
    @Override
    public int getItemViewType(int position) {
        if (isHeaderAdded && isFirstPosition(position)) {
            return VIEW_TYPE_HEADER;
        } else if (isFooterAdded && isLastPosition(position)) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    final protected boolean isLastPosition(int position) {
        return items.size() - 1 == position;
    }

    final protected boolean isFirstPosition(int position) {
        return position == 0;
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return createHeaderViewHolder(parent);
            case VIEW_TYPE_ITEM:
                return createItemViewHolder(parent);
            case VIEW_TYPE_FOOTER:
                return createFooterViewHolder(parent);
        }

        return null;
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                onBindHeaderViewHolder(holder, position);
                break;
            case VIEW_TYPE_ITEM:
                onBindItemViewHolder(holder, position);
                break;
            case VIEW_TYPE_FOOTER:
                onBindFooterViewHolder(holder, position);
                break;
        }
    }

    protected abstract RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent);
    protected abstract RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent);
    protected abstract RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent);
    protected abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);
    protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);
    protected abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position);
    protected abstract T getHeaderObject();
    protected abstract T getFooterObject();

    final public void addHeader() {
        if (isHeaderAdded) {
            return;
        }

        isHeaderAdded = true;
        items.add(0, getHeaderObject());
        notifyItemInserted(0);
    }

    final public void addFooter() {
        if (isFooterAdded) {
            return;
        }

        isFooterAdded = true;
        items.add(getFooterObject());
        notifyItemInserted(items.size() - 1);
    }

    final public void removeHeader() {
        if (!isHeaderAdded || items.isEmpty()) {
            return;
        }

        isHeaderAdded = false;
        int position = 0;
        removeItem(position);
    }

    final public void removeFooter() {
        if (!isFooterAdded || items.isEmpty()) {
            return;
        }

        isFooterAdded = false;
        int position = items.size() - 1;
        removeItem(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.size() == 0;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public List<T> getAllItems() {
        List<T> itemsCopy = new ArrayList<>();
        Collections.copy(itemsCopy, items);
        return itemsCopy;
    }

    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addAll(List<T> items) {
        int startPosition = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(startPosition, items.size());
    }

    public void removeItem(T item) {
        if (isEmpty()) {
            return;
        }

        int position = this.items.indexOf(item);
        if (isValidPosition(position)) {
            this.items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        if (isEmpty() || !isValidPosition(position)) {
            return;
        }

        items.remove(position);
        notifyItemRemoved(position);
    }

    private boolean isValidPosition(int position) {
        return position >= 0 && position < items.size();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public boolean isFooterAdded() {
        return isFooterAdded;
    }

    public boolean isHeaderAdded() {
        return isHeaderAdded;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
