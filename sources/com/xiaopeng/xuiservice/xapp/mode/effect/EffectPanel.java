package com.xiaopeng.xuiservice.xapp.mode.effect;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaopeng.xui.widget.XLinearLayout;
import com.xiaopeng.xui.widget.XSwitch;
import com.xiaopeng.xui.widget.slider.XSlider;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.xapp.mode.effect.EffectAdapter;
import com.xiaopeng.xuiservice.xapp.mode.effect.EffectManager;
/* loaded from: classes5.dex */
public class EffectPanel extends XLinearLayout implements CompoundButton.OnCheckedChangeListener, XSlider.SliderProgressListener {
    private static final String TAG = "EffectPanel";
    EffectManager.Callback mCallback;
    private EffectAdapter mEffectAdapter;
    private EffectManager mEffectManager;
    private RecyclerView mEffectSetView;
    private TextView mEffectSetsTextView;
    private LinearLayout mMusicEffectLayout;
    private XSwitch mMusicEffectSwitch;
    private XSlider mReverberationSlide;
    private LinearLayout.LayoutParams mSliderLayoutParams;
    private LinearLayout.LayoutParams mTextViewLayoutParams;

    public EffectPanel(Context context) {
        this(context, null);
    }

    public EffectPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EffectPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EffectPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCallback = new EffectManager.Callback() { // from class: com.xiaopeng.xuiservice.xapp.mode.effect.EffectPanel.2
            @Override // com.xiaopeng.xuiservice.xapp.mode.effect.EffectManager.Callback
            public void onEffectSet(int index) {
                EffectPanel.this.refreshSelected(index);
            }
        };
        init();
    }

    @Override // com.xiaopeng.xui.widget.slider.XSlider.SliderProgressListener
    public void onProgressChanged(XSlider xSlider, float v, String s) {
    }

    @Override // com.xiaopeng.xui.widget.slider.XSlider.SliderProgressListener
    public void onStartTrackingTouch(XSlider xSlider) {
    }

    @Override // com.xiaopeng.xui.widget.slider.XSlider.SliderProgressListener
    public void onStopTrackingTouch(XSlider xSlider) {
        if (xSlider == this.mReverberationSlide && this.mEffectManager != null) {
            int reverberationValue = (int) xSlider.getIndicatorValue();
            LogUtil.d(TAG, "onStopTrackingTouch reverberationValue:" + reverberationValue);
            this.mEffectManager.setEchoValue(reverberationValue);
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == this.mMusicEffectSwitch) {
            this.mEffectManager.setMusicEffect(isChecked);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mEffectSetView = (RecyclerView) findViewById(R.id.recyclerView_effect);
        this.mMusicEffectSwitch = (XSwitch) findViewById(R.id.switch_music_effect);
        this.mMusicEffectSwitch.setOnCheckedChangeListener(this);
        this.mReverberationSlide = (XSlider) findViewById(R.id.slide_reverberation);
        this.mReverberationSlide.setSliderProgressListener(this);
        this.mSliderLayoutParams = (LinearLayout.LayoutParams) this.mReverberationSlide.getLayoutParams();
        this.mMusicEffectLayout = (LinearLayout) findViewById(R.id.ll_music_effect);
        this.mEffectSetsTextView = (TextView) findViewById(R.id.effect_sets_tv);
        this.mTextViewLayoutParams = (LinearLayout.LayoutParams) this.mEffectSetsTextView.getLayoutParams();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initRecyclerView();
        refreshEffectSelected();
        refreshReverberationSlide();
        refreshMusicSwitch();
        this.mEffectManager.registerCallback(this.mCallback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xiaopeng.xui.widget.XLinearLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEffectManager.unregisterCallback(this.mCallback);
    }

    private void init() {
        this.mEffectManager = EffectManager.getInstance();
    }

    private void refreshEffectSelected() {
        int selectIndex = this.mEffectManager.getEffect();
        refreshSelected(selectIndex);
    }

    private void refreshReverberationSlide() {
        int value = this.mEffectManager.getEchoValue();
        LogUtil.d(TAG, "refreshReverberationSlide  reverberationValue :" + value);
        this.mReverberationSlide.setCurrentIndex(value);
    }

    private void refreshMusicSwitch() {
        boolean isMusicEffectEnable = this.mEffectManager.isMusicEffectEnable();
        boolean isMusicEffectSupport = this.mEffectManager.isMusicEffectSupport();
        LogUtil.d(TAG, "refreshMusicSwitch isMusicEffectEnable" + isMusicEffectEnable + " &isMusicEffectSupport:" + isMusicEffectSupport);
        this.mMusicEffectSwitch.setChecked(isMusicEffectEnable);
        refreshMusicSwitchUI(isMusicEffectSupport);
    }

    private void refreshMusicSwitchUI(boolean isMusicEffectSupport) {
        if (isMusicEffectSupport) {
            this.mMusicEffectLayout.setVisibility(0);
            this.mSliderLayoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.effect_severberation_dist_normal);
            this.mTextViewLayoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.effect_set_textview_dist_normal);
        } else {
            this.mMusicEffectLayout.setVisibility(8);
            this.mSliderLayoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.effect_severberation_dist_stretch);
            this.mTextViewLayoutParams.topMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.effect_set_textview_dist_stretch);
        }
        this.mReverberationSlide.setLayoutParams(this.mSliderLayoutParams);
        this.mEffectSetsTextView.setLayoutParams(this.mTextViewLayoutParams);
    }

    private void initRecyclerView() {
        this.mEffectAdapter = new EffectAdapter(getContext(), this.mEffectManager.getEffectList());
        this.mEffectAdapter.setSelectIndex(this.mEffectManager.getEffect());
        this.mEffectAdapter.setOnItemClickListener(new EffectAdapter.OnItemClickListener() { // from class: com.xiaopeng.xuiservice.xapp.mode.effect.EffectPanel.1
            @Override // com.xiaopeng.xuiservice.xapp.mode.effect.EffectAdapter.OnItemClickListener
            public void onItemClick(int index) {
                EffectPanel.this.mEffectManager.setEffect(index);
                EffectPanel.this.mEffectManager.setBIData(index);
            }
        });
        this.mEffectSetView.setAdapter(this.mEffectAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSelected(int index) {
        this.mEffectAdapter.setSelectIndex(index);
        this.mEffectAdapter.notifyDataSetChanged();
    }
}
