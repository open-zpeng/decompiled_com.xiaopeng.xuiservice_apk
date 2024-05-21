package com.xiaopeng.xuiservice.xapp.mode.effect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaopeng.xuiservice.R;
import java.util.List;
/* loaded from: classes5.dex */
public class EffectAdapter extends RecyclerView.Adapter<EffectHolder> {
    private static final String TAG = "EffectAdapter";
    private Context mContext;
    private List<EffectItem> mEffectList;
    private OnItemClickListener mOnItemClickListener;
    private int mSelectedIndex;

    /* loaded from: classes5.dex */
    interface OnItemClickListener {
        void onItemClick(int i);
    }

    public EffectAdapter(Context context, List<EffectItem> itemList) {
        this.mContext = context;
        this.mEffectList = itemList;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public EffectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.item_effect, parent, false);
        EffectHolder holder = new EffectHolder(view);
        return holder;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull EffectHolder holder, final int position) {
        holder.setData(this.mEffectList.get(position));
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.effect.EffectAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (EffectAdapter.this.mOnItemClickListener != null) {
                    EffectAdapter.this.mOnItemClickListener.onItemClick(((EffectItem) EffectAdapter.this.mEffectList.get(position)).getIndex());
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<EffectItem> list = this.mEffectList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void setSelectIndex(int index) {
        this.mSelectedIndex = index;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public class EffectHolder extends RecyclerView.ViewHolder {
        private TextView mAvatarName;
        private ImageView mEffectBorder;
        private TextView mEffectName;
        private View mItemLayout;

        public EffectHolder(View view) {
            super(view);
            this.mItemLayout = view;
            this.mAvatarName = (TextView) view.findViewById(R.id.tv_avatar);
            this.mEffectName = (TextView) view.findViewById(R.id.tv_effect_name);
            this.mEffectBorder = (ImageView) view.findViewById(R.id.iv_effect_border);
        }

        public void setData(EffectItem effectItem) {
            this.mEffectName.setText(effectItem.getName());
            this.mEffectName.setBackgroundResource(EffectResources.getEffectBg(effectItem.getIndex()));
            this.mEffectBorder.setImageResource(EffectResources.getEffectBorder(effectItem.getIndex()));
            int index = effectItem.getIndex();
            if (index == 14) {
                this.mAvatarName.setVisibility(0);
                this.mAvatarName.setBackgroundResource(EffectResources.getAvatarBg(index));
                this.mAvatarName.setText(EffectResources.getAvatarDes(index));
            } else {
                this.mAvatarName.setVisibility(4);
            }
            this.mEffectBorder.setVisibility(effectItem.getIndex() != EffectAdapter.this.mSelectedIndex ? 8 : 0);
        }
    }
}
