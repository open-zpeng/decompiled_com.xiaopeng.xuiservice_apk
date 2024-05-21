package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
/* loaded from: classes4.dex */
public final class SpanUtils {
    public static final int ALIGN_BASELINE = 1;
    public static final int ALIGN_BOTTOM = 0;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_TOP = 3;
    private static final int COLOR_DEFAULT = -16777217;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private int alignImage;
    private int alignLine;
    private Layout.Alignment alignment;
    private int backgroundColor;
    private float blurRadius;
    private int bulletColor;
    private int bulletGapWidth;
    private int bulletRadius;
    private ClickableSpan clickSpan;
    private int first;
    private int flag;
    private String fontFamily;
    private int fontSize;
    private boolean fontSizeIsDp;
    private int foregroundColor;
    private Bitmap imageBitmap;
    private Drawable imageDrawable;
    private int imageResourceId;
    private Uri imageUri;
    private boolean isBold;
    private boolean isBoldItalic;
    private boolean isCreated;
    private boolean isItalic;
    private boolean isStrikethrough;
    private boolean isSubscript;
    private boolean isSuperscript;
    private boolean isUnderline;
    private int lineHeight;
    private SerializableSpannableStringBuilder mBuilder;
    private CharSequence mText;
    private TextView mTextView;
    private int mType;
    private final int mTypeCharSequence;
    private final int mTypeImage;
    private final int mTypeSpace;
    private float proportion;
    private int quoteColor;
    private int quoteGapWidth;
    private int rest;
    private Shader shader;
    private int shadowColor;
    private float shadowDx;
    private float shadowDy;
    private float shadowRadius;
    private int spaceColor;
    private int spaceSize;
    private Object[] spans;
    private int stripeWidth;
    private BlurMaskFilter.Blur style;
    private Typeface typeface;
    private String url;
    private int verticalAlign;
    private float xProportion;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes4.dex */
    public @interface Align {
    }

    public static SpanUtils with(TextView textView) {
        return new SpanUtils(textView);
    }

    private SpanUtils(TextView textView) {
        this();
        this.mTextView = textView;
    }

    public SpanUtils() {
        this.mTypeCharSequence = 0;
        this.mTypeImage = 1;
        this.mTypeSpace = 2;
        this.mBuilder = new SerializableSpannableStringBuilder();
        this.mText = "";
        this.mType = -1;
        setDefault();
    }

    private void setDefault() {
        this.flag = 33;
        this.foregroundColor = COLOR_DEFAULT;
        this.backgroundColor = COLOR_DEFAULT;
        this.lineHeight = -1;
        this.quoteColor = COLOR_DEFAULT;
        this.first = -1;
        this.bulletColor = COLOR_DEFAULT;
        this.fontSize = -1;
        this.proportion = -1.0f;
        this.xProportion = -1.0f;
        this.isStrikethrough = false;
        this.isUnderline = false;
        this.isSuperscript = false;
        this.isSubscript = false;
        this.isBold = false;
        this.isItalic = false;
        this.isBoldItalic = false;
        this.fontFamily = null;
        this.typeface = null;
        this.alignment = null;
        this.verticalAlign = -1;
        this.clickSpan = null;
        this.url = null;
        this.blurRadius = -1.0f;
        this.shader = null;
        this.shadowRadius = -1.0f;
        this.spans = null;
        this.imageBitmap = null;
        this.imageDrawable = null;
        this.imageUri = null;
        this.imageResourceId = -1;
        this.spaceSize = -1;
    }

    public SpanUtils setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public SpanUtils setForegroundColor(@ColorInt int color) {
        this.foregroundColor = color;
        return this;
    }

    public SpanUtils setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    public SpanUtils setLineHeight(@IntRange(from = 0) int lineHeight) {
        return setLineHeight(lineHeight, 2);
    }

    public SpanUtils setLineHeight(@IntRange(from = 0) int lineHeight, int align) {
        this.lineHeight = lineHeight;
        this.alignLine = align;
        return this;
    }

    public SpanUtils setQuoteColor(@ColorInt int color) {
        return setQuoteColor(color, 2, 2);
    }

    public SpanUtils setQuoteColor(@ColorInt int color, @IntRange(from = 1) int stripeWidth, @IntRange(from = 0) int gapWidth) {
        this.quoteColor = color;
        this.stripeWidth = stripeWidth;
        this.quoteGapWidth = gapWidth;
        return this;
    }

    public SpanUtils setLeadingMargin(@IntRange(from = 0) int first, @IntRange(from = 0) int rest) {
        this.first = first;
        this.rest = rest;
        return this;
    }

    public SpanUtils setBullet(@IntRange(from = 0) int gapWidth) {
        return setBullet(0, 3, gapWidth);
    }

    public SpanUtils setBullet(@ColorInt int color, @IntRange(from = 0) int radius, @IntRange(from = 0) int gapWidth) {
        this.bulletColor = color;
        this.bulletRadius = radius;
        this.bulletGapWidth = gapWidth;
        return this;
    }

    public SpanUtils setFontSize(@IntRange(from = 0) int size) {
        return setFontSize(size, false);
    }

    public SpanUtils setFontSize(@IntRange(from = 0) int size, boolean isSp) {
        this.fontSize = size;
        this.fontSizeIsDp = isSp;
        return this;
    }

    public SpanUtils setFontProportion(float proportion) {
        this.proportion = proportion;
        return this;
    }

    public SpanUtils setFontXProportion(float proportion) {
        this.xProportion = proportion;
        return this;
    }

    public SpanUtils setStrikethrough() {
        this.isStrikethrough = true;
        return this;
    }

    public SpanUtils setUnderline() {
        this.isUnderline = true;
        return this;
    }

    public SpanUtils setSuperscript() {
        this.isSuperscript = true;
        return this;
    }

    public SpanUtils setSubscript() {
        this.isSubscript = true;
        return this;
    }

    public SpanUtils setBold() {
        this.isBold = true;
        return this;
    }

    public SpanUtils setItalic() {
        this.isItalic = true;
        return this;
    }

    public SpanUtils setBoldItalic() {
        this.isBoldItalic = true;
        return this;
    }

    public SpanUtils setFontFamily(@NonNull String fontFamily) {
        if (fontFamily == null) {
            throw new NullPointerException("Argument 'fontFamily' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.fontFamily = fontFamily;
        return this;
    }

    public SpanUtils setTypeface(@NonNull Typeface typeface) {
        if (typeface == null) {
            throw new NullPointerException("Argument 'typeface' of type Typeface (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.typeface = typeface;
        return this;
    }

    public SpanUtils setHorizontalAlign(@NonNull Layout.Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("Argument 'alignment' of type Alignment (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.alignment = alignment;
        return this;
    }

    public SpanUtils setVerticalAlign(int align) {
        this.verticalAlign = align;
        return this;
    }

    public SpanUtils setClickSpan(@NonNull ClickableSpan clickSpan) {
        if (clickSpan == null) {
            throw new NullPointerException("Argument 'clickSpan' of type ClickableSpan (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setMovementMethodIfNeed();
        this.clickSpan = clickSpan;
        return this;
    }

    public SpanUtils setClickSpan(@ColorInt final int color, final boolean underlineText, final View.OnClickListener listener) {
        setMovementMethodIfNeed();
        this.clickSpan = new ClickableSpan() { // from class: com.blankj.utilcode.util.SpanUtils.1
            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(@NonNull TextPaint paint) {
                if (paint == null) {
                    throw new NullPointerException("Argument 'paint' of type TextPaint (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
                paint.setColor(color);
                paint.setUnderlineText(underlineText);
            }

            @Override // android.text.style.ClickableSpan
            public void onClick(@NonNull View widget) {
                if (widget == null) {
                    throw new NullPointerException("Argument 'widget' of type View (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
                }
                View.OnClickListener onClickListener = listener;
                if (onClickListener != null) {
                    onClickListener.onClick(widget);
                }
            }
        };
        return this;
    }

    public SpanUtils setUrl(@NonNull String url) {
        if (url == null) {
            throw new NullPointerException("Argument 'url' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        setMovementMethodIfNeed();
        this.url = url;
        return this;
    }

    private void setMovementMethodIfNeed() {
        TextView textView = this.mTextView;
        if (textView != null && textView.getMovementMethod() == null) {
            this.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public SpanUtils setBlur(@FloatRange(from = 0.0d, fromInclusive = false) float radius, BlurMaskFilter.Blur style) {
        this.blurRadius = radius;
        this.style = style;
        return this;
    }

    public SpanUtils setShader(@NonNull Shader shader) {
        if (shader == null) {
            throw new NullPointerException("Argument 'shader' of type Shader (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        this.shader = shader;
        return this;
    }

    public SpanUtils setShadow(@FloatRange(from = 0.0d, fromInclusive = false) float radius, float dx, float dy, int shadowColor) {
        this.shadowRadius = radius;
        this.shadowDx = dx;
        this.shadowDy = dy;
        this.shadowColor = shadowColor;
        return this;
    }

    public SpanUtils setSpans(@NonNull Object... spans) {
        if (spans == null) {
            throw new NullPointerException("Argument 'spans' of type Object[] (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (spans.length > 0) {
            this.spans = spans;
        }
        return this;
    }

    public SpanUtils append(@NonNull CharSequence text) {
        if (text == null) {
            throw new NullPointerException("Argument 'text' of type CharSequence (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        apply(0);
        this.mText = text;
        return this;
    }

    public SpanUtils appendLine() {
        apply(0);
        this.mText = LINE_SEPARATOR;
        return this;
    }

    public SpanUtils appendLine(@NonNull CharSequence text) {
        if (text == null) {
            throw new NullPointerException("Argument 'text' of type CharSequence (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        apply(0);
        this.mText = ((Object) text) + LINE_SEPARATOR;
        return this;
    }

    public SpanUtils appendImage(@NonNull Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("Argument 'bitmap' of type Bitmap (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return appendImage(bitmap, 0);
    }

    public SpanUtils appendImage(@NonNull Bitmap bitmap, int align) {
        if (bitmap == null) {
            throw new NullPointerException("Argument 'bitmap' of type Bitmap (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        apply(1);
        this.imageBitmap = bitmap;
        this.alignImage = align;
        return this;
    }

    public SpanUtils appendImage(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new NullPointerException("Argument 'drawable' of type Drawable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return appendImage(drawable, 0);
    }

    public SpanUtils appendImage(@NonNull Drawable drawable, int align) {
        if (drawable == null) {
            throw new NullPointerException("Argument 'drawable' of type Drawable (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        apply(1);
        this.imageDrawable = drawable;
        this.alignImage = align;
        return this;
    }

    public SpanUtils appendImage(@NonNull Uri uri) {
        if (uri == null) {
            throw new NullPointerException("Argument 'uri' of type Uri (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return appendImage(uri, 0);
    }

    public SpanUtils appendImage(@NonNull Uri uri, int align) {
        if (uri == null) {
            throw new NullPointerException("Argument 'uri' of type Uri (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        apply(1);
        this.imageUri = uri;
        this.alignImage = align;
        return this;
    }

    public SpanUtils appendImage(@DrawableRes int resourceId) {
        return appendImage(resourceId, 0);
    }

    public SpanUtils appendImage(@DrawableRes int resourceId, int align) {
        apply(1);
        this.imageResourceId = resourceId;
        this.alignImage = align;
        return this;
    }

    public SpanUtils appendSpace(@IntRange(from = 0) int size) {
        return appendSpace(size, 0);
    }

    public SpanUtils appendSpace(@IntRange(from = 0) int size, @ColorInt int color) {
        apply(2);
        this.spaceSize = size;
        this.spaceColor = color;
        return this;
    }

    private void apply(int type) {
        applyLast();
        this.mType = type;
    }

    public SpannableStringBuilder get() {
        return this.mBuilder;
    }

    public SpannableStringBuilder create() {
        applyLast();
        TextView textView = this.mTextView;
        if (textView != null) {
            textView.setText(this.mBuilder);
        }
        this.isCreated = true;
        return this.mBuilder;
    }

    private void applyLast() {
        if (this.isCreated) {
            return;
        }
        int i = this.mType;
        if (i == 0) {
            updateCharCharSequence();
        } else if (i == 1) {
            updateImage();
        } else if (i == 2) {
            updateSpace();
        }
        setDefault();
    }

    private void updateCharCharSequence() {
        if (this.mText.length() == 0) {
            return;
        }
        int start = this.mBuilder.length();
        if (start == 0 && this.lineHeight != -1) {
            this.mBuilder.append((CharSequence) Character.toString((char) 2)).append((CharSequence) "\n").setSpan(new AbsoluteSizeSpan(0), 0, 2, 33);
            start = 2;
        }
        this.mBuilder.append(this.mText);
        int end = this.mBuilder.length();
        int i = this.verticalAlign;
        if (i != -1) {
            this.mBuilder.setSpan(new VerticalAlignSpan(i), start, end, this.flag);
        }
        int i2 = this.foregroundColor;
        if (i2 != COLOR_DEFAULT) {
            this.mBuilder.setSpan(new ForegroundColorSpan(i2), start, end, this.flag);
        }
        int i3 = this.backgroundColor;
        if (i3 != COLOR_DEFAULT) {
            this.mBuilder.setSpan(new BackgroundColorSpan(i3), start, end, this.flag);
        }
        int i4 = this.first;
        if (i4 != -1) {
            this.mBuilder.setSpan(new LeadingMarginSpan.Standard(i4, this.rest), start, end, this.flag);
        }
        int i5 = this.quoteColor;
        if (i5 != COLOR_DEFAULT) {
            this.mBuilder.setSpan(new CustomQuoteSpan(i5, this.stripeWidth, this.quoteGapWidth), start, end, this.flag);
        }
        int i6 = this.bulletColor;
        if (i6 != COLOR_DEFAULT) {
            this.mBuilder.setSpan(new CustomBulletSpan(i6, this.bulletRadius, this.bulletGapWidth), start, end, this.flag);
        }
        int i7 = this.fontSize;
        if (i7 != -1) {
            this.mBuilder.setSpan(new AbsoluteSizeSpan(i7, this.fontSizeIsDp), start, end, this.flag);
        }
        float f = this.proportion;
        if (f != -1.0f) {
            this.mBuilder.setSpan(new RelativeSizeSpan(f), start, end, this.flag);
        }
        float f2 = this.xProportion;
        if (f2 != -1.0f) {
            this.mBuilder.setSpan(new ScaleXSpan(f2), start, end, this.flag);
        }
        int i8 = this.lineHeight;
        if (i8 != -1) {
            this.mBuilder.setSpan(new CustomLineHeightSpan(i8, this.alignLine), start, end, this.flag);
        }
        if (this.isStrikethrough) {
            this.mBuilder.setSpan(new StrikethroughSpan(), start, end, this.flag);
        }
        if (this.isUnderline) {
            this.mBuilder.setSpan(new UnderlineSpan(), start, end, this.flag);
        }
        if (this.isSuperscript) {
            this.mBuilder.setSpan(new SuperscriptSpan(), start, end, this.flag);
        }
        if (this.isSubscript) {
            this.mBuilder.setSpan(new SubscriptSpan(), start, end, this.flag);
        }
        if (this.isBold) {
            this.mBuilder.setSpan(new StyleSpan(1), start, end, this.flag);
        }
        if (this.isItalic) {
            this.mBuilder.setSpan(new StyleSpan(2), start, end, this.flag);
        }
        if (this.isBoldItalic) {
            this.mBuilder.setSpan(new StyleSpan(3), start, end, this.flag);
        }
        String str = this.fontFamily;
        if (str != null) {
            this.mBuilder.setSpan(new TypefaceSpan(str), start, end, this.flag);
        }
        Typeface typeface = this.typeface;
        if (typeface != null) {
            this.mBuilder.setSpan(new CustomTypefaceSpan(typeface), start, end, this.flag);
        }
        Layout.Alignment alignment = this.alignment;
        if (alignment != null) {
            this.mBuilder.setSpan(new AlignmentSpan.Standard(alignment), start, end, this.flag);
        }
        ClickableSpan clickableSpan = this.clickSpan;
        if (clickableSpan != null) {
            this.mBuilder.setSpan(clickableSpan, start, end, this.flag);
        }
        String str2 = this.url;
        if (str2 != null) {
            this.mBuilder.setSpan(new URLSpan(str2), start, end, this.flag);
        }
        float f3 = this.blurRadius;
        if (f3 != -1.0f) {
            this.mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(f3, this.style)), start, end, this.flag);
        }
        Shader shader = this.shader;
        if (shader != null) {
            this.mBuilder.setSpan(new ShaderSpan(shader), start, end, this.flag);
        }
        float f4 = this.shadowRadius;
        if (f4 != -1.0f) {
            this.mBuilder.setSpan(new ShadowSpan(f4, this.shadowDx, this.shadowDy, this.shadowColor), start, end, this.flag);
        }
        Object[] objArr = this.spans;
        if (objArr != null) {
            for (Object span : objArr) {
                this.mBuilder.setSpan(span, start, end, this.flag);
            }
        }
    }

    private void updateImage() {
        int start = this.mBuilder.length();
        this.mText = "<img>";
        updateCharCharSequence();
        int end = this.mBuilder.length();
        Bitmap bitmap = this.imageBitmap;
        if (bitmap != null) {
            this.mBuilder.setSpan(new CustomImageSpan(bitmap, this.alignImage), start, end, this.flag);
            return;
        }
        Drawable drawable = this.imageDrawable;
        if (drawable != null) {
            this.mBuilder.setSpan(new CustomImageSpan(drawable, this.alignImage), start, end, this.flag);
            return;
        }
        Uri uri = this.imageUri;
        if (uri != null) {
            this.mBuilder.setSpan(new CustomImageSpan(uri, this.alignImage), start, end, this.flag);
            return;
        }
        int i = this.imageResourceId;
        if (i != -1) {
            this.mBuilder.setSpan(new CustomImageSpan(i, this.alignImage), start, end, this.flag);
        }
    }

    private void updateSpace() {
        int start = this.mBuilder.length();
        this.mText = "< >";
        updateCharCharSequence();
        int end = this.mBuilder.length();
        this.mBuilder.setSpan(new SpaceSpan(this.spaceSize, this.spaceColor), start, end, this.flag);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class VerticalAlignSpan extends ReplacementSpan {
        static final int ALIGN_CENTER = 2;
        static final int ALIGN_TOP = 3;
        final int mVerticalAlignment;

        VerticalAlignSpan(int verticalAlignment) {
            this.mVerticalAlignment = verticalAlignment;
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            if (paint == null) {
                throw new NullPointerException("Argument 'paint' of type Paint (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return (int) paint.measureText(text.subSequence(start, end).toString());
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            if (canvas == null) {
                throw new NullPointerException("Argument 'canvas' of type Canvas (#0 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (paint == null) {
                throw new NullPointerException("Argument 'paint' of type Paint (#8 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            CharSequence text2 = text.subSequence(start, end);
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            canvas.drawText(text2.toString(), x, y - (((((fm.descent + y) + y) + fm.ascent) / 2) - ((bottom + top) / 2)), paint);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class CustomLineHeightSpan implements LineHeightSpan {
        static final int ALIGN_CENTER = 2;
        static final int ALIGN_TOP = 3;
        static Paint.FontMetricsInt sfm;
        private final int height;
        final int mVerticalAlignment;

        CustomLineHeightSpan(int height, int verticalAlignment) {
            this.height = height;
            this.mVerticalAlignment = verticalAlignment;
        }

        @Override // android.text.style.LineHeightSpan
        public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
            Paint.FontMetricsInt fontMetricsInt = sfm;
            if (fontMetricsInt == null) {
                sfm = new Paint.FontMetricsInt();
                sfm.top = fm.top;
                sfm.ascent = fm.ascent;
                sfm.descent = fm.descent;
                sfm.bottom = fm.bottom;
                sfm.leading = fm.leading;
            } else {
                fm.top = fontMetricsInt.top;
                fm.ascent = sfm.ascent;
                fm.descent = sfm.descent;
                fm.bottom = sfm.bottom;
                fm.leading = sfm.leading;
            }
            int need = this.height - (((fm.descent + v) - fm.ascent) - spanstartv);
            if (need > 0) {
                int i = this.mVerticalAlignment;
                if (i == 3) {
                    fm.descent += need;
                } else if (i == 2) {
                    fm.descent += need / 2;
                    fm.ascent -= need / 2;
                } else {
                    fm.ascent -= need;
                }
            }
            int need2 = this.height - (((fm.bottom + v) - fm.top) - spanstartv);
            if (need2 > 0) {
                int i2 = this.mVerticalAlignment;
                if (i2 == 3) {
                    fm.bottom += need2;
                } else if (i2 == 2) {
                    fm.bottom += need2 / 2;
                    fm.top -= need2 / 2;
                } else {
                    fm.top -= need2;
                }
            }
            if (end == ((Spanned) text).getSpanEnd(this)) {
                sfm = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class SpaceSpan extends ReplacementSpan {
        private final Paint paint;
        private final int width;

        private SpaceSpan(int width) {
            this(width, 0);
        }

        private SpaceSpan(int width, int color) {
            this.paint = new Paint();
            this.width = width;
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.FILL);
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
            if (paint == null) {
                throw new NullPointerException("Argument 'paint' of type Paint (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return this.width;
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            if (canvas == null) {
                throw new NullPointerException("Argument 'canvas' of type Canvas (#0 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (paint == null) {
                throw new NullPointerException("Argument 'paint' of type Paint (#8 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            canvas.drawRect(x, top, x + this.width, bottom, this.paint);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class CustomQuoteSpan implements LeadingMarginSpan {
        private final int color;
        private final int gapWidth;
        private final int stripeWidth;

        private CustomQuoteSpan(int color, int stripeWidth, int gapWidth) {
            this.color = color;
            this.stripeWidth = stripeWidth;
            this.gapWidth = gapWidth;
        }

        @Override // android.text.style.LeadingMarginSpan
        public int getLeadingMargin(boolean first) {
            return this.stripeWidth + this.gapWidth;
        }

        @Override // android.text.style.LeadingMarginSpan
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
            Paint.Style style = p.getStyle();
            int color = p.getColor();
            p.setStyle(Paint.Style.FILL);
            p.setColor(this.color);
            c.drawRect(x, top, (this.stripeWidth * dir) + x, bottom, p);
            p.setStyle(style);
            p.setColor(color);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class CustomBulletSpan implements LeadingMarginSpan {
        private final int color;
        private final int gapWidth;
        private final int radius;
        private Path sBulletPath;

        private CustomBulletSpan(int color, int radius, int gapWidth) {
            this.sBulletPath = null;
            this.color = color;
            this.radius = radius;
            this.gapWidth = gapWidth;
        }

        @Override // android.text.style.LeadingMarginSpan
        public int getLeadingMargin(boolean first) {
            return (this.radius * 2) + this.gapWidth;
        }

        @Override // android.text.style.LeadingMarginSpan
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
            if (((Spanned) text).getSpanStart(this) == start) {
                Paint.Style style = p.getStyle();
                int oldColor = p.getColor();
                p.setColor(this.color);
                p.setStyle(Paint.Style.FILL);
                if (c.isHardwareAccelerated()) {
                    if (this.sBulletPath == null) {
                        this.sBulletPath = new Path();
                        this.sBulletPath.addCircle(0.0f, 0.0f, this.radius, Path.Direction.CW);
                    }
                    c.save();
                    c.translate((this.radius * dir) + x, (top + bottom) / 2.0f);
                    c.drawPath(this.sBulletPath, p);
                    c.restore();
                } else {
                    int i = this.radius;
                    c.drawCircle((dir * i) + x, (top + bottom) / 2.0f, i, p);
                }
                p.setColor(oldColor);
                p.setStyle(style);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"ParcelCreator"})
    /* loaded from: classes4.dex */
    public static class CustomTypefaceSpan extends TypefaceSpan {
        private final Typeface newType;

        private CustomTypefaceSpan(Typeface type) {
            super("");
            this.newType = type;
        }

        @Override // android.text.style.TypefaceSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            apply(textPaint, this.newType);
        }

        @Override // android.text.style.TypefaceSpan, android.text.style.MetricAffectingSpan
        public void updateMeasureState(TextPaint paint) {
            apply(paint, this.newType);
        }

        private void apply(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }
            int fake = (~tf.getStyle()) & oldStyle;
            if ((fake & 1) != 0) {
                paint.setFakeBoldText(true);
            }
            if ((fake & 2) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.getShader();
            paint.setTypeface(tf);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class CustomImageSpan extends CustomDynamicDrawableSpan {
        private Uri mContentUri;
        private Drawable mDrawable;
        private int mResourceId;

        private CustomImageSpan(Bitmap b, int verticalAlignment) {
            super(verticalAlignment);
            this.mDrawable = new BitmapDrawable(Utils.getApp().getResources(), b);
            Drawable drawable = this.mDrawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mDrawable.getIntrinsicHeight());
        }

        private CustomImageSpan(Drawable d, int verticalAlignment) {
            super(verticalAlignment);
            this.mDrawable = d;
            Drawable drawable = this.mDrawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mDrawable.getIntrinsicHeight());
        }

        private CustomImageSpan(Uri uri, int verticalAlignment) {
            super(verticalAlignment);
            this.mContentUri = uri;
        }

        private CustomImageSpan(@DrawableRes int resourceId, int verticalAlignment) {
            super(verticalAlignment);
            this.mResourceId = resourceId;
        }

        @Override // com.blankj.utilcode.util.SpanUtils.CustomDynamicDrawableSpan
        public Drawable getDrawable() {
            Drawable drawable = null;
            if (this.mDrawable != null) {
                return this.mDrawable;
            }
            if (this.mContentUri != null) {
                try {
                    InputStream is = Utils.getApp().getContentResolver().openInputStream(this.mContentUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    drawable = new BitmapDrawable(Utils.getApp().getResources(), bitmap);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    if (is != null) {
                        is.close();
                        return drawable;
                    }
                    return drawable;
                } catch (Exception e) {
                    Log.e("sms", "Failed to loaded content " + this.mContentUri, e);
                    return drawable;
                }
            }
            try {
                drawable = ContextCompat.getDrawable(Utils.getApp(), this.mResourceId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            } catch (Exception e2) {
                Log.e("sms", "Unable to find resource: " + this.mResourceId);
                return drawable;
            }
        }
    }

    /* loaded from: classes4.dex */
    static abstract class CustomDynamicDrawableSpan extends ReplacementSpan {
        static final int ALIGN_BASELINE = 1;
        static final int ALIGN_BOTTOM = 0;
        static final int ALIGN_CENTER = 2;
        static final int ALIGN_TOP = 3;
        private WeakReference<Drawable> mDrawableRef;
        final int mVerticalAlignment;

        public abstract Drawable getDrawable();

        private CustomDynamicDrawableSpan() {
            this.mVerticalAlignment = 0;
        }

        private CustomDynamicDrawableSpan(int verticalAlignment) {
            this.mVerticalAlignment = verticalAlignment;
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            int lineHeight;
            if (paint == null) {
                throw new NullPointerException("Argument 'paint' of type Paint (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            Drawable d = getCachedDrawable();
            Rect rect = d.getBounds();
            if (fm != null && (lineHeight = fm.bottom - fm.top) < rect.height()) {
                int i = this.mVerticalAlignment;
                if (i == 3) {
                    fm.top = fm.top;
                    fm.bottom = rect.height() + fm.top;
                } else if (i == 2) {
                    fm.top = ((-rect.height()) / 2) - (lineHeight / 4);
                    fm.bottom = (rect.height() / 2) - (lineHeight / 4);
                } else {
                    fm.top = (-rect.height()) + fm.bottom;
                    fm.bottom = fm.bottom;
                }
                fm.ascent = fm.top;
                fm.descent = fm.bottom;
            }
            return rect.right;
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            float transY;
            if (canvas == null) {
                throw new NullPointerException("Argument 'canvas' of type Canvas (#0 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (paint == null) {
                throw new NullPointerException("Argument 'paint' of type Paint (#8 out of 9, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            Drawable d = getCachedDrawable();
            Rect rect = d.getBounds();
            canvas.save();
            int lineHeight = bottom - top;
            if (rect.height() < lineHeight) {
                int i = this.mVerticalAlignment;
                if (i == 3) {
                    transY = top;
                } else if (i == 2) {
                    transY = ((bottom + top) - rect.height()) / 2;
                } else if (i == 1) {
                    transY = y - rect.height();
                } else {
                    transY = bottom - rect.height();
                }
                canvas.translate(x, transY);
            } else {
                float transY2 = top;
                canvas.translate(x, transY2);
            }
            d.draw(canvas);
            canvas.restore();
        }

        private Drawable getCachedDrawable() {
            WeakReference<Drawable> wr = this.mDrawableRef;
            Drawable d = null;
            if (wr != null) {
                Drawable d2 = wr.get();
                d = d2;
            }
            if (d == null) {
                Drawable d3 = getDrawable();
                this.mDrawableRef = new WeakReference<>(d3);
                return d3;
            }
            return d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class ShaderSpan extends CharacterStyle implements UpdateAppearance {
        private Shader mShader;

        private ShaderSpan(Shader shader) {
            this.mShader = shader;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint tp) {
            tp.setShader(this.mShader);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class ShadowSpan extends CharacterStyle implements UpdateAppearance {
        private float dx;
        private float dy;
        private float radius;
        private int shadowColor;

        private ShadowSpan(float radius, float dx, float dy, int shadowColor) {
            this.radius = radius;
            this.dx = dx;
            this.dy = dy;
            this.shadowColor = shadowColor;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint tp) {
            tp.setShadowLayer(this.radius, this.dx, this.dy, this.shadowColor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SerializableSpannableStringBuilder extends SpannableStringBuilder implements Serializable {
        private static final long serialVersionUID = 4909567650765875771L;

        private SerializableSpannableStringBuilder() {
        }
    }
}
