package com.loostone.libtuning.data.config;

import com.loostone.libtuning.data.bean.EQItem;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b!\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b$\n\u0002\u0010\u0013\n\u0002\b6\n\u0002\u0010\t\n\u0002\br\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\u0015\n\u0002\bj\u0018\u0000 ì\u00022\u00020\u0001:\u0002í\u0002B\t¢\u0006\u0006\bê\u0002\u0010ë\u0002R\"\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\t\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\"\u0010\f\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u0010\u0004\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR$\u0010\u000f\u001a\u0004\u0018\u00010\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\"\u0010\u0015\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010\u0004\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\"\u0010\u0018\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0018\u0010\u0004\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\bR\"\u0010\u001b\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001b\u0010\u0004\u001a\u0004\b\u001c\u0010\u0006\"\u0004\b\u001d\u0010\bR\"\u0010\u001e\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001e\u0010\u0004\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\bR\"\u0010!\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b!\u0010\u0004\u001a\u0004\b\"\u0010\u0006\"\u0004\b#\u0010\bR(\u0010&\u001a\b\u0012\u0004\u0012\u00020%0$8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b&\u0010'\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\"\u0010,\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b,\u0010\u0004\u001a\u0004\b-\u0010\u0006\"\u0004\b.\u0010\bR\"\u0010/\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b/\u0010\u0004\u001a\u0004\b0\u0010\u0006\"\u0004\b1\u0010\bR\"\u00102\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b2\u0010\u0004\u001a\u0004\b3\u0010\u0006\"\u0004\b4\u0010\bR\"\u00105\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b5\u0010\u0004\u001a\u0004\b6\u0010\u0006\"\u0004\b7\u0010\bR\"\u00108\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b8\u0010\u0004\u001a\u0004\b9\u0010\u0006\"\u0004\b:\u0010\bR\"\u0010;\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b;\u0010\u0004\u001a\u0004\b<\u0010\u0006\"\u0004\b=\u0010\bR\"\u0010>\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b>\u0010\u0004\u001a\u0004\b?\u0010\u0006\"\u0004\b@\u0010\bR\"\u0010A\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bA\u0010\u0004\u001a\u0004\bB\u0010\u0006\"\u0004\bC\u0010\bR\"\u0010D\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bD\u0010\u0004\u001a\u0004\bE\u0010\u0006\"\u0004\bF\u0010\bR\"\u0010G\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bG\u0010\u0004\u001a\u0004\bH\u0010\u0006\"\u0004\bI\u0010\bR\"\u0010K\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bK\u0010L\u001a\u0004\bM\u0010N\"\u0004\bO\u0010PR\"\u0010Q\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bQ\u0010\u0004\u001a\u0004\bR\u0010\u0006\"\u0004\bS\u0010\bR\"\u0010T\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bT\u0010\u0004\u001a\u0004\bU\u0010\u0006\"\u0004\bV\u0010\bR\"\u0010W\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bW\u0010\u0004\u001a\u0004\bX\u0010\u0006\"\u0004\bY\u0010\bR\"\u0010Z\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bZ\u0010\u0004\u001a\u0004\b[\u0010\u0006\"\u0004\b\\\u0010\bR\"\u0010]\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b]\u0010\u0004\u001a\u0004\b^\u0010\u0006\"\u0004\b_\u0010\bR\"\u0010`\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b`\u0010\u0004\u001a\u0004\ba\u0010\u0006\"\u0004\bb\u0010\bR$\u0010c\u001a\u0004\u0018\u00010\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bc\u0010\u0010\u001a\u0004\bd\u0010\u0012\"\u0004\be\u0010\u0014R\"\u0010f\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bf\u0010\u0004\u001a\u0004\bg\u0010\u0006\"\u0004\bh\u0010\bR\"\u0010i\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bi\u0010\u0004\u001a\u0004\bj\u0010\u0006\"\u0004\bk\u0010\bR$\u0010l\u001a\u0004\u0018\u00010\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bl\u0010\u0010\u001a\u0004\bm\u0010\u0012\"\u0004\bn\u0010\u0014R\"\u0010o\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bo\u0010\u0004\u001a\u0004\bp\u0010\u0006\"\u0004\bq\u0010\bR\"\u0010r\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\br\u0010\u0004\u001a\u0004\bs\u0010\u0006\"\u0004\bt\u0010\bR$\u0010u\u001a\u0004\u0018\u00010\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bu\u0010\u0010\u001a\u0004\bv\u0010\u0012\"\u0004\bw\u0010\u0014R\"\u0010x\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\bx\u0010\u0004\u001a\u0004\by\u0010\u0006\"\u0004\bz\u0010\bR\"\u0010{\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b{\u0010\u0004\u001a\u0004\b|\u0010\u0006\"\u0004\b}\u0010\bR#\u0010~\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0013\n\u0004\b~\u0010\u0004\u001a\u0004\b\u007f\u0010\u0006\"\u0005\b\u0080\u0001\u0010\bR*\u0010\u0082\u0001\u001a\u00030\u0081\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0082\u0001\u0010\u0083\u0001\u001a\u0006\b\u0084\u0001\u0010\u0085\u0001\"\u0006\b\u0086\u0001\u0010\u0087\u0001R&\u0010\u0088\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0088\u0001\u0010\u0004\u001a\u0005\b\u0089\u0001\u0010\u0006\"\u0005\b\u008a\u0001\u0010\bR&\u0010\u008b\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u008b\u0001\u0010\u0004\u001a\u0005\b\u008c\u0001\u0010\u0006\"\u0005\b\u008d\u0001\u0010\bR&\u0010\u008e\u0001\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u008e\u0001\u0010L\u001a\u0005\b\u008f\u0001\u0010N\"\u0005\b\u0090\u0001\u0010PR&\u0010\u0091\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0091\u0001\u0010\u0004\u001a\u0005\b\u0092\u0001\u0010\u0006\"\u0005\b\u0093\u0001\u0010\bR&\u0010\u0094\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0094\u0001\u0010\u0004\u001a\u0005\b\u0095\u0001\u0010\u0006\"\u0005\b\u0096\u0001\u0010\bR&\u0010\u0097\u0001\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0097\u0001\u0010L\u001a\u0005\b\u0098\u0001\u0010N\"\u0005\b\u0099\u0001\u0010PR&\u0010\u009a\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u009a\u0001\u0010\u0004\u001a\u0005\b\u009b\u0001\u0010\u0006\"\u0005\b\u009c\u0001\u0010\bR&\u0010\u009d\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u009d\u0001\u0010\u0004\u001a\u0005\b\u009e\u0001\u0010\u0006\"\u0005\b\u009f\u0001\u0010\bR&\u0010 \u0001\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b \u0001\u0010L\u001a\u0005\b¡\u0001\u0010N\"\u0005\b¢\u0001\u0010PR&\u0010£\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b£\u0001\u0010\u0004\u001a\u0005\b¤\u0001\u0010\u0006\"\u0005\b¥\u0001\u0010\bR&\u0010¦\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¦\u0001\u0010\u0004\u001a\u0005\b§\u0001\u0010\u0006\"\u0005\b¨\u0001\u0010\bR&\u0010©\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b©\u0001\u0010\u0004\u001a\u0005\bª\u0001\u0010\u0006\"\u0005\b«\u0001\u0010\bR&\u0010¬\u0001\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¬\u0001\u0010L\u001a\u0005\b\u00ad\u0001\u0010N\"\u0005\b®\u0001\u0010PR&\u0010¯\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¯\u0001\u0010\u0004\u001a\u0005\b°\u0001\u0010\u0006\"\u0005\b±\u0001\u0010\bR&\u0010²\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b²\u0001\u0010\u0004\u001a\u0005\b³\u0001\u0010\u0006\"\u0005\b´\u0001\u0010\bR&\u0010µ\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bµ\u0001\u0010\u0004\u001a\u0005\b¶\u0001\u0010\u0006\"\u0005\b·\u0001\u0010\bR&\u0010¸\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¸\u0001\u0010\u0004\u001a\u0005\b¹\u0001\u0010\u0006\"\u0005\bº\u0001\u0010\bR&\u0010»\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b»\u0001\u0010\u0004\u001a\u0005\b¼\u0001\u0010\u0006\"\u0005\b½\u0001\u0010\bR&\u0010¾\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¾\u0001\u0010\u0004\u001a\u0005\b¿\u0001\u0010\u0006\"\u0005\bÀ\u0001\u0010\bR&\u0010Á\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÁ\u0001\u0010\u0004\u001a\u0005\bÂ\u0001\u0010\u0006\"\u0005\bÃ\u0001\u0010\bR&\u0010Ä\u0001\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÄ\u0001\u0010L\u001a\u0005\bÅ\u0001\u0010N\"\u0005\bÆ\u0001\u0010PR&\u0010Ç\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÇ\u0001\u0010\u0004\u001a\u0005\bÈ\u0001\u0010\u0006\"\u0005\bÉ\u0001\u0010\bR&\u0010Ê\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÊ\u0001\u0010\u0004\u001a\u0005\bË\u0001\u0010\u0006\"\u0005\bÌ\u0001\u0010\bR&\u0010Í\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÍ\u0001\u0010\u0004\u001a\u0005\bÎ\u0001\u0010\u0006\"\u0005\bÏ\u0001\u0010\bR&\u0010Ð\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÐ\u0001\u0010\u0004\u001a\u0005\bÑ\u0001\u0010\u0006\"\u0005\bÒ\u0001\u0010\bR&\u0010Ó\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÓ\u0001\u0010\u0004\u001a\u0005\bÔ\u0001\u0010\u0006\"\u0005\bÕ\u0001\u0010\bR&\u0010Ö\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÖ\u0001\u0010\u0004\u001a\u0005\b×\u0001\u0010\u0006\"\u0005\bØ\u0001\u0010\bR&\u0010Ù\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÙ\u0001\u0010\u0004\u001a\u0005\bÚ\u0001\u0010\u0006\"\u0005\bÛ\u0001\u0010\bR&\u0010Ü\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÜ\u0001\u0010\u0004\u001a\u0005\bÝ\u0001\u0010\u0006\"\u0005\bÞ\u0001\u0010\bR&\u0010ß\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bß\u0001\u0010\u0004\u001a\u0005\bà\u0001\u0010\u0006\"\u0005\bá\u0001\u0010\bR&\u0010â\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bâ\u0001\u0010\u0004\u001a\u0005\bã\u0001\u0010\u0006\"\u0005\bä\u0001\u0010\bR&\u0010å\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bå\u0001\u0010\u0004\u001a\u0005\bæ\u0001\u0010\u0006\"\u0005\bç\u0001\u0010\bR&\u0010è\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bè\u0001\u0010\u0004\u001a\u0005\bé\u0001\u0010\u0006\"\u0005\bê\u0001\u0010\bR&\u0010ë\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bë\u0001\u0010\u0004\u001a\u0005\bì\u0001\u0010\u0006\"\u0005\bí\u0001\u0010\bR&\u0010î\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bî\u0001\u0010\u0004\u001a\u0005\bï\u0001\u0010\u0006\"\u0005\bð\u0001\u0010\bR&\u0010ñ\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bñ\u0001\u0010\u0004\u001a\u0005\bò\u0001\u0010\u0006\"\u0005\bó\u0001\u0010\bR*\u0010õ\u0001\u001a\u00030ô\u00018\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\bõ\u0001\u0010ö\u0001\u001a\u0006\b÷\u0001\u0010ø\u0001\"\u0006\bù\u0001\u0010ú\u0001R&\u0010û\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bû\u0001\u0010\u0004\u001a\u0005\bü\u0001\u0010\u0006\"\u0005\bý\u0001\u0010\bR&\u0010þ\u0001\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bþ\u0001\u0010\u0004\u001a\u0005\bÿ\u0001\u0010\u0006\"\u0005\b\u0080\u0002\u0010\bR&\u0010\u0081\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0081\u0002\u0010\u0004\u001a\u0005\b\u0082\u0002\u0010\u0006\"\u0005\b\u0083\u0002\u0010\bR*\u0010\u0085\u0002\u001a\u00030\u0084\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0085\u0002\u0010\u0086\u0002\u001a\u0006\b\u0087\u0002\u0010\u0088\u0002\"\u0006\b\u0089\u0002\u0010\u008a\u0002R&\u0010\u008b\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u008b\u0002\u0010\u0004\u001a\u0005\b\u008c\u0002\u0010\u0006\"\u0005\b\u008d\u0002\u0010\bR&\u0010\u008e\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u008e\u0002\u0010\u0004\u001a\u0005\b\u008f\u0002\u0010\u0006\"\u0005\b\u0090\u0002\u0010\bR&\u0010\u0091\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0091\u0002\u0010\u0004\u001a\u0005\b\u0092\u0002\u0010\u0006\"\u0005\b\u0093\u0002\u0010\bR&\u0010\u0094\u0002\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0094\u0002\u0010L\u001a\u0005\b\u0095\u0002\u0010N\"\u0005\b\u0096\u0002\u0010PR&\u0010\u0097\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u0097\u0002\u0010\u0004\u001a\u0005\b\u0098\u0002\u0010\u0006\"\u0005\b\u0099\u0002\u0010\bR&\u0010\u009a\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u009a\u0002\u0010\u0004\u001a\u0005\b\u009b\u0002\u0010\u0006\"\u0005\b\u009c\u0002\u0010\bR&\u0010\u009d\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b\u009d\u0002\u0010\u0004\u001a\u0005\b\u009e\u0002\u0010\u0006\"\u0005\b\u009f\u0002\u0010\bR&\u0010 \u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b \u0002\u0010\u0004\u001a\u0005\b¡\u0002\u0010\u0006\"\u0005\b¢\u0002\u0010\bR(\u0010£\u0002\u001a\u0004\u0018\u00010\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b£\u0002\u0010\u0010\u001a\u0005\b¤\u0002\u0010\u0012\"\u0005\b¥\u0002\u0010\u0014R&\u0010¦\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¦\u0002\u0010\u0004\u001a\u0005\b§\u0002\u0010\u0006\"\u0005\b¨\u0002\u0010\bR&\u0010©\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b©\u0002\u0010\u0004\u001a\u0005\bª\u0002\u0010\u0006\"\u0005\b«\u0002\u0010\bR&\u0010¬\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¬\u0002\u0010\u0004\u001a\u0005\b\u00ad\u0002\u0010\u0006\"\u0005\b®\u0002\u0010\bR&\u0010¯\u0002\u001a\u00020J8\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b¯\u0002\u0010L\u001a\u0005\b°\u0002\u0010N\"\u0005\b±\u0002\u0010PR&\u0010²\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b²\u0002\u0010\u0004\u001a\u0005\b³\u0002\u0010\u0006\"\u0005\b´\u0002\u0010\bR&\u0010µ\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bµ\u0002\u0010\u0004\u001a\u0005\bµ\u0002\u0010\u0006\"\u0005\b¶\u0002\u0010\bR&\u0010·\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b·\u0002\u0010\u0004\u001a\u0005\b¸\u0002\u0010\u0006\"\u0005\b¹\u0002\u0010\bR&\u0010º\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bº\u0002\u0010\u0004\u001a\u0005\b»\u0002\u0010\u0006\"\u0005\b¼\u0002\u0010\bR&\u0010½\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\b½\u0002\u0010\u0004\u001a\u0005\b¾\u0002\u0010\u0006\"\u0005\b¿\u0002\u0010\bR&\u0010À\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÀ\u0002\u0010\u0004\u001a\u0005\bÁ\u0002\u0010\u0006\"\u0005\bÂ\u0002\u0010\bR&\u0010Ã\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÃ\u0002\u0010\u0004\u001a\u0005\bÄ\u0002\u0010\u0006\"\u0005\bÅ\u0002\u0010\bR&\u0010Æ\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÆ\u0002\u0010\u0004\u001a\u0005\bÇ\u0002\u0010\u0006\"\u0005\bÈ\u0002\u0010\bR&\u0010É\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÉ\u0002\u0010\u0004\u001a\u0005\bÊ\u0002\u0010\u0006\"\u0005\bË\u0002\u0010\bR&\u0010Ì\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÌ\u0002\u0010\u0004\u001a\u0005\bÍ\u0002\u0010\u0006\"\u0005\bÎ\u0002\u0010\bR&\u0010Ï\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÏ\u0002\u0010\u0004\u001a\u0005\bÐ\u0002\u0010\u0006\"\u0005\bÑ\u0002\u0010\bR&\u0010Ò\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÒ\u0002\u0010\u0004\u001a\u0005\bÓ\u0002\u0010\u0006\"\u0005\bÔ\u0002\u0010\bR&\u0010Õ\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÕ\u0002\u0010\u0004\u001a\u0005\bÖ\u0002\u0010\u0006\"\u0005\b×\u0002\u0010\bR&\u0010Ø\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bØ\u0002\u0010\u0004\u001a\u0005\bÙ\u0002\u0010\u0006\"\u0005\bÚ\u0002\u0010\bR&\u0010Û\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÛ\u0002\u0010\u0004\u001a\u0005\bÜ\u0002\u0010\u0006\"\u0005\bÝ\u0002\u0010\bR&\u0010Þ\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bÞ\u0002\u0010\u0004\u001a\u0005\bß\u0002\u0010\u0006\"\u0005\bà\u0002\u0010\bR&\u0010á\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bá\u0002\u0010\u0004\u001a\u0005\bâ\u0002\u0010\u0006\"\u0005\bã\u0002\u0010\bR&\u0010ä\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bä\u0002\u0010\u0004\u001a\u0005\bå\u0002\u0010\u0006\"\u0005\bæ\u0002\u0010\bR&\u0010ç\u0002\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0015\n\u0005\bç\u0002\u0010\u0004\u001a\u0005\bè\u0002\u0010\u0006\"\u0005\bé\u0002\u0010\b¨\u0006î\u0002"}, d2 = {"Lcom/loostone/libtuning/data/config/DefaultConfig;", "", "", "aef2VoiceMode", "I", "getAef2VoiceMode", "()I", "setAef2VoiceMode", "(I)V", "logLevel", "getLogLevel", "setLogLevel", "aef2MusicEQEnable", "getAef2MusicEQEnable", "setAef2MusicEQEnable", "debugPlaySinEnable", "Ljava/lang/Integer;", "getDebugPlaySinEnable", "()Ljava/lang/Integer;", "setDebugPlaySinEnable", "(Ljava/lang/Integer;)V", "delayFluentThresholdPlayback", "getDelayFluentThresholdPlayback", "setDelayFluentThresholdPlayback", "playbackStopThreshold", "getPlaybackStopThreshold", "setPlaybackStopThreshold", "playbackStartThreshold", "getPlaybackStartThreshold", "setPlaybackStartThreshold", "capturePeriodCount", "getCapturePeriodCount", "setCapturePeriodCount", "playbackAvailMin", "getPlaybackAvailMin", "setPlaybackAvailMin", "", "Lcom/loostone/libtuning/data/bean/EQItem;", "eqList", "Ljava/util/List;", "getEqList", "()Ljava/util/List;", "setEqList", "(Ljava/util/List;)V", "aef2ReverbType", "getAef2ReverbType", "setAef2ReverbType", "playbackPeriodSize", "getPlaybackPeriodSize", "setPlaybackPeriodSize", "micVolume", "getMicVolume", "setMicVolume", "noiseSleepIntervalPlayback", "getNoiseSleepIntervalPlayback", "setNoiseSleepIntervalPlayback", "aef2CompressorThreshold", "getAef2CompressorThreshold", "setAef2CompressorThreshold", "aef2MicFFTCount", "getAef2MicFFTCount", "setAef2MicFFTCount", "audioTrack", "getAudioTrack", "setAudioTrack", "listenSystemVolume", "getListenSystemVolume", "setListenSystemVolume", "ringStartThreshold", "getRingStartThreshold", "setRingStartThreshold", "aef2EchoFeedback", "getAef2EchoFeedback", "setAef2EchoFeedback", "", "sysVolcurveX", "[D", "getSysVolcurveX", "()[D", "setSysVolcurveX", "([D)V", "aef2CompressorPregain", "getAef2CompressorPregain", "setAef2CompressorPregain", "jumpToMuteThresholdCapture", "getJumpToMuteThresholdCapture", "setJumpToMuteThresholdCapture", "keepMuteCountPlayback", "getKeepMuteCountPlayback", "setKeepMuteCountPlayback", "keepMuteCountCapture", "getKeepMuteCountCapture", "setKeepMuteCountCapture", "listenMicElectricity", "getListenMicElectricity", "setListenMicElectricity", "playbackRate", "getPlaybackRate", "setPlaybackRate", "aef2CompressorPregainOut", "getAef2CompressorPregainOut", "setAef2CompressorPregainOut", "volumeShiftL", "getVolumeShiftL", "setVolumeShiftL", "aef3EchoStwide", "getAef3EchoStwide", "setAef3EchoStwide", "aef4SurroundMusicVol", "getAef4SurroundMusicVol", "setAef4SurroundMusicVol", "capturePeriodSize", "getCapturePeriodSize", "setCapturePeriodSize", "sysVolCurveSwitch", "getSysVolCurveSwitch", "setSysVolCurveSwitch", "recordVocalOffset", "getRecordVocalOffset", "setRecordVocalOffset", "volSft", "getVolSft", "setVolSft", "resampleBufferSize", "getResampleBufferSize", "setResampleBufferSize", "aef1ReverbEnable", "getAef1ReverbEnable", "setAef1ReverbEnable", "", "channel", "J", "getChannel", "()J", "setChannel", "(J)V", "balanceVolume", "getBalanceVolume", "setBalanceVolume", "aef2CompressorEnable", "getAef2CompressorEnable", "setAef2CompressorEnable", "sysVolcurveSurX", "getSysVolcurveSurX", "setSysVolcurveSurX", "aef3ReverbStwide", "getAef3ReverbStwide", "setAef3ReverbStwide", "systemVolume", "getSystemVolume", "setSystemVolume", "micVolcurveY", "getMicVolcurveY", "setMicVolcurveY", "musicVolume", "getMusicVolume", "setMusicVolume", "playbackWaitSleepInterval", "getPlaybackWaitSleepInterval", "setPlaybackWaitSleepInterval", "sysVolcurveSurY", "getSysVolcurveSurY", "setSysVolcurveSurY", "reverbVolumeStep", "getReverbVolumeStep", "setReverbVolumeStep", "audioRecord", "getAudioRecord", "setAudioRecord", "aef2FreqShiftChangeEnable", "getAef2FreqShiftChangeEnable", "setAef2FreqShiftChangeEnable", "sysVolcurveY", "getSysVolcurveY", "setSysVolcurveY", "volumeShiftR", "getVolumeShiftR", "setVolumeShiftR", "keyMode", "getKeyMode", "setKeyMode", "checkDelayIntervalPlayback", "getCheckDelayIntervalPlayback", "setCheckDelayIntervalPlayback", "aef3EchoLowcut", "getAef3EchoLowcut", "setAef3EchoLowcut", "delayFluentThresholdCapture", "getDelayFluentThresholdCapture", "setDelayFluentThresholdCapture", "playbackFormat", "getPlaybackFormat", "setPlaybackFormat", "aef2MicFFTCalculateFrequency", "getAef2MicFFTCalculateFrequency", "setAef2MicFFTCalculateFrequency", "volumeCurveUI", "getVolumeCurveUI", "setVolumeCurveUI", "noiseSleepIntervalCapture", "getNoiseSleepIntervalCapture", "setNoiseSleepIntervalCapture", "hardwareMicVolume", "getHardwareMicVolume", "setHardwareMicVolume", "aef2EchoEnable", "getAef2EchoEnable", "setAef2EchoEnable", "captureFormat", "getCaptureFormat", "setCaptureFormat", "jumpToMuteThresholdPlayback", "getJumpToMuteThresholdPlayback", "setJumpToMuteThresholdPlayback", "aef3ReverbHicut", "getAef3ReverbHicut", "setAef3ReverbHicut", "aef3ReverbHidamp", "getAef3ReverbHidamp", "setAef3ReverbHidamp", "playbackChannel", "getPlaybackChannel", "setPlaybackChannel", "beta", "getBeta", "setBeta", "micLevelFollowSysVol", "getMicLevelFollowSysVol", "setMicLevelFollowSysVol", "aef1ReverbOutGain", "getAef1ReverbOutGain", "setAef1ReverbOutGain", "captureChannel", "getCaptureChannel", "setCaptureChannel", "playbackSilenceSize", "getPlaybackSilenceSize", "setPlaybackSilenceSize", "ringBufferLength", "getRingBufferLength", "setRingBufferLength", "aef2CompressorRatio", "getAef2CompressorRatio", "setAef2CompressorRatio", "", "workMode", "Ljava/lang/String;", "getWorkMode", "()Ljava/lang/String;", "setWorkMode", "(Ljava/lang/String;)V", "aef2EchoGain", "getAef2EchoGain", "setAef2EchoGain", "micVolumeStep", "getMicVolumeStep", "setMicVolumeStep", "captureStopThreshold", "getCaptureStopThreshold", "setCaptureStopThreshold", "", "volumeCurveMax", "[I", "getVolumeCurveMax", "()[I", "setVolumeCurveMax", "([I)V", "workModeInt", "getWorkModeInt", "setWorkModeInt", "aef2VoiceChangeEnable", "getAef2VoiceChangeEnable", "setAef2VoiceChangeEnable", "captureSilenceSize", "getCaptureSilenceSize", "setCaptureSilenceSize", "micVolcurveX", "getMicVolcurveX", "setMicVolcurveX", "aef2EchoTime", "getAef2EchoTime", "setAef2EchoTime", "aef2ReverbTime", "getAef2ReverbTime", "setAef2ReverbTime", "aef2CompressorAttack", "getAef2CompressorAttack", "setAef2CompressorAttack", "aef3EchoHicut", "getAef3EchoHicut", "setAef3EchoHicut", "aef4SurroundPregain", "getAef4SurroundPregain", "setAef4SurroundPregain", "aef2MusicFFTCount", "getAef2MusicFFTCount", "setAef2MusicFFTCount", "aef2ReverbEnable", "getAef2ReverbEnable", "setAef2ReverbEnable", "musicVolumeStep", "getMusicVolumeStep", "setMusicVolumeStep", "volumeCurveSystem", "getVolumeCurveSystem", "setVolumeCurveSystem", "dmaStopThreshold", "getDmaStopThreshold", "setDmaStopThreshold", "isMicEnable", "setMicEnable", "configVersion", "getConfigVersion", "setConfigVersion", "aef3ReverbLowcut", "getAef3ReverbLowcut", "setAef3ReverbLowcut", "captureStartThreshold", "getCaptureStartThreshold", "setCaptureStartThreshold", "delayAddition", "getDelayAddition", "setDelayAddition", "checkDelayIntervalCapture", "getCheckDelayIntervalCapture", "setCheckDelayIntervalCapture", "playbackSilenceThreshold", "getPlaybackSilenceThreshold", "setPlaybackSilenceThreshold", "aef2CompressorRelease", "getAef2CompressorRelease", "setAef2CompressorRelease", "playbackPeriodCount", "getPlaybackPeriodCount", "setPlaybackPeriodCount", "aef2EQEnable", "getAef2EQEnable", "setAef2EQEnable", "captureRate", "getCaptureRate", "setCaptureRate", "aef2MusicFFTCalculateFrequency", "getAef2MusicFFTCalculateFrequency", "setAef2MusicFFTCalculateFrequency", "dataPrintFrequency", "getDataPrintFrequency", "setDataPrintFrequency", "volStr", "getVolStr", "setVolStr", "captureAvailMin", "getCaptureAvailMin", "setCaptureAvailMin", "aef2ReverbVolume", "getAef2ReverbVolume", "setAef2ReverbVolume", "aef2EchoType", "getAef2EchoType", "setAef2EchoType", "captureSilenceThreshold", "getCaptureSilenceThreshold", "setCaptureSilenceThreshold", "<init>", "()V", "Companion", "OooO00o", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class DefaultConfig {
    @NotNull
    public static final OooO00o Companion = new OooO00o();
    @NotNull
    private static final String TAG = "DefaultConfig";
    private int aef2CompressorEnable;
    @Nullable
    private Integer aef2CompressorPregainOut;
    private int aef2FreqShiftChangeEnable;
    private int aef2VoiceChangeEnable;
    private int aef2VoiceMode;
    private int aef3ReverbLowcut;
    @Nullable
    private Integer aef4SurroundMusicVol;
    @Nullable
    private Integer aef4SurroundPregain;
    private int audioTrack;
    private int balanceVolume;
    private int beta;
    @Nullable
    private Integer debugPlaySinEnable;
    private int hardwareMicVolume;
    private int listenMicElectricity;
    private int micLevelFollowSysVol;
    private int playbackFormat;
    @Nullable
    private Integer recordVocalOffset;
    private long channel = 100001;
    private int configVersion = 1;
    private int workModeInt = 1;
    @NotNull
    private String workMode = "01002";
    private int logLevel = 1;
    private int volSft = 128;
    private int volStr = 1;
    private int captureChannel = 2;
    private int captureRate = 48000;
    private int capturePeriodSize = 256;
    private int capturePeriodCount = 4;
    private int captureFormat = 1;
    private int captureStartThreshold = 1;
    private int captureStopThreshold = 1;
    private int captureSilenceThreshold = 1;
    private int captureSilenceSize = 1;
    private int captureAvailMin = 1;
    private int playbackChannel = 2;
    private int playbackRate = 48000;
    private int playbackPeriodSize = 256;
    private int playbackPeriodCount = 4;
    private int playbackStartThreshold = 1;
    private int playbackStopThreshold = 1;
    private int playbackSilenceThreshold = 1;
    private int playbackSilenceSize = 1;
    private int playbackAvailMin = 1;
    private int micVolume = 90;
    private int musicVolume = 1;
    private int isMicEnable = 1;
    private int systemVolume = 1;
    private int aef1ReverbEnable = 1;
    private int aef1ReverbOutGain = 1;
    private int aef2EQEnable = 1;
    private int aef2MusicEQEnable = 1;
    @NotNull
    private List<EQItem> eqList = new ArrayList();
    private int aef2EchoEnable = 1;
    private int aef2EchoGain = 1;
    private int aef2EchoType = 1;
    private int aef2EchoTime = 1;
    private int aef2EchoFeedback = 1;
    private int aef3EchoHicut = 4000;
    private int aef3EchoLowcut = 50;
    private int aef3EchoStwide = 10;
    private int aef2ReverbEnable = 1;
    private int aef2ReverbType = 1;
    private int aef2ReverbVolume = 1;
    private int aef2ReverbTime = 1;
    private int aef3ReverbHicut = 22000;
    private int aef3ReverbHidamp = 127;
    private int aef3ReverbStwide = 10;
    private int aef2CompressorRatio = 1;
    private int aef2CompressorThreshold = 1;
    private int aef2CompressorAttack = 1;
    private int aef2CompressorRelease = 1;
    private int aef2CompressorPregain = 1;
    private int aef2MusicFFTCalculateFrequency = 1;
    private int aef2MusicFFTCount = 16;
    private int aef2MicFFTCalculateFrequency = 1;
    private int aef2MicFFTCount = 16;
    @NotNull
    private double[] volumeCurveUI = new double[0];
    @NotNull
    private double[] volumeCurveSystem = new double[0];
    @NotNull
    private int[] volumeCurveMax = new int[0];
    private int volumeShiftR = -1;
    private int volumeShiftL = 1;
    @NotNull
    private double[] sysVolcurveX = new double[0];
    @NotNull
    private double[] sysVolcurveY = new double[0];
    @NotNull
    private double[] sysVolcurveSurX = new double[0];
    @NotNull
    private double[] sysVolcurveSurY = new double[0];
    @NotNull
    private double[] micVolcurveX = new double[0];
    @NotNull
    private double[] micVolcurveY = new double[0];
    private int dmaStopThreshold = 2048;
    private int playbackWaitSleepInterval = 500;
    private int ringStartThreshold = 2048;
    private int dataPrintFrequency = 2000;
    private int delayFluentThresholdPlayback = 4000;
    private int delayFluentThresholdCapture = 500;
    private int checkDelayIntervalPlayback = 20;
    private int checkDelayIntervalCapture = 20;
    private int jumpToMuteThresholdPlayback = 15;
    private int jumpToMuteThresholdCapture = 15;
    private int keepMuteCountPlayback = 25;
    private int keepMuteCountCapture = 25;
    private int noiseSleepIntervalPlayback = 100000;
    private int noiseSleepIntervalCapture = 100000;
    private int ringBufferLength = 16384;
    private int resampleBufferSize = 32768;
    private int delayAddition = 5;
    private int audioRecord = 1;
    private int listenSystemVolume = 1;
    private int sysVolCurveSwitch = 1;
    private int micVolumeStep = 5;
    private int reverbVolumeStep = 5;
    private int musicVolumeStep = 5;
    private int keyMode = 1;

    /* loaded from: classes4.dex */
    public static final class OooO00o {
    }

    public final int getAef1ReverbEnable() {
        return this.aef1ReverbEnable;
    }

    public final int getAef1ReverbOutGain() {
        return this.aef1ReverbOutGain;
    }

    public final int getAef2CompressorAttack() {
        return this.aef2CompressorAttack;
    }

    public final int getAef2CompressorEnable() {
        return this.aef2CompressorEnable;
    }

    public final int getAef2CompressorPregain() {
        return this.aef2CompressorPregain;
    }

    @Nullable
    public final Integer getAef2CompressorPregainOut() {
        return this.aef2CompressorPregainOut;
    }

    public final int getAef2CompressorRatio() {
        return this.aef2CompressorRatio;
    }

    public final int getAef2CompressorRelease() {
        return this.aef2CompressorRelease;
    }

    public final int getAef2CompressorThreshold() {
        return this.aef2CompressorThreshold;
    }

    public final int getAef2EQEnable() {
        return this.aef2EQEnable;
    }

    public final int getAef2EchoEnable() {
        return this.aef2EchoEnable;
    }

    public final int getAef2EchoFeedback() {
        return this.aef2EchoFeedback;
    }

    public final int getAef2EchoGain() {
        return this.aef2EchoGain;
    }

    public final int getAef2EchoTime() {
        return this.aef2EchoTime;
    }

    public final int getAef2EchoType() {
        return this.aef2EchoType;
    }

    public final int getAef2FreqShiftChangeEnable() {
        return this.aef2FreqShiftChangeEnable;
    }

    public final int getAef2MicFFTCalculateFrequency() {
        return this.aef2MicFFTCalculateFrequency;
    }

    public final int getAef2MicFFTCount() {
        return this.aef2MicFFTCount;
    }

    public final int getAef2MusicEQEnable() {
        return this.aef2MusicEQEnable;
    }

    public final int getAef2MusicFFTCalculateFrequency() {
        return this.aef2MusicFFTCalculateFrequency;
    }

    public final int getAef2MusicFFTCount() {
        return this.aef2MusicFFTCount;
    }

    public final int getAef2ReverbEnable() {
        return this.aef2ReverbEnable;
    }

    public final int getAef2ReverbTime() {
        return this.aef2ReverbTime;
    }

    public final int getAef2ReverbType() {
        return this.aef2ReverbType;
    }

    public final int getAef2ReverbVolume() {
        return this.aef2ReverbVolume;
    }

    public final int getAef2VoiceChangeEnable() {
        return this.aef2VoiceChangeEnable;
    }

    public final int getAef2VoiceMode() {
        return this.aef2VoiceMode;
    }

    public final int getAef3EchoHicut() {
        return this.aef3EchoHicut;
    }

    public final int getAef3EchoLowcut() {
        return this.aef3EchoLowcut;
    }

    public final int getAef3EchoStwide() {
        return this.aef3EchoStwide;
    }

    public final int getAef3ReverbHicut() {
        return this.aef3ReverbHicut;
    }

    public final int getAef3ReverbHidamp() {
        return this.aef3ReverbHidamp;
    }

    public final int getAef3ReverbLowcut() {
        return this.aef3ReverbLowcut;
    }

    public final int getAef3ReverbStwide() {
        return this.aef3ReverbStwide;
    }

    @Nullable
    public final Integer getAef4SurroundMusicVol() {
        return this.aef4SurroundMusicVol;
    }

    @Nullable
    public final Integer getAef4SurroundPregain() {
        return this.aef4SurroundPregain;
    }

    public final int getAudioRecord() {
        return this.audioRecord;
    }

    public final int getAudioTrack() {
        return this.audioTrack;
    }

    public final int getBalanceVolume() {
        return this.balanceVolume;
    }

    public final int getBeta() {
        return this.beta;
    }

    public final int getCaptureAvailMin() {
        return this.captureAvailMin;
    }

    public final int getCaptureChannel() {
        return this.captureChannel;
    }

    public final int getCaptureFormat() {
        return this.captureFormat;
    }

    public final int getCapturePeriodCount() {
        return this.capturePeriodCount;
    }

    public final int getCapturePeriodSize() {
        return this.capturePeriodSize;
    }

    public final int getCaptureRate() {
        return this.captureRate;
    }

    public final int getCaptureSilenceSize() {
        return this.captureSilenceSize;
    }

    public final int getCaptureSilenceThreshold() {
        return this.captureSilenceThreshold;
    }

    public final int getCaptureStartThreshold() {
        return this.captureStartThreshold;
    }

    public final int getCaptureStopThreshold() {
        return this.captureStopThreshold;
    }

    public final long getChannel() {
        return this.channel;
    }

    public final int getCheckDelayIntervalCapture() {
        return this.checkDelayIntervalCapture;
    }

    public final int getCheckDelayIntervalPlayback() {
        return this.checkDelayIntervalPlayback;
    }

    public final int getConfigVersion() {
        return this.configVersion;
    }

    public final int getDataPrintFrequency() {
        return this.dataPrintFrequency;
    }

    @Nullable
    public final Integer getDebugPlaySinEnable() {
        return this.debugPlaySinEnable;
    }

    public final int getDelayAddition() {
        return this.delayAddition;
    }

    public final int getDelayFluentThresholdCapture() {
        return this.delayFluentThresholdCapture;
    }

    public final int getDelayFluentThresholdPlayback() {
        return this.delayFluentThresholdPlayback;
    }

    public final int getDmaStopThreshold() {
        return this.dmaStopThreshold;
    }

    @NotNull
    public final List<EQItem> getEqList() {
        return this.eqList;
    }

    public final int getHardwareMicVolume() {
        return this.hardwareMicVolume;
    }

    public final int getJumpToMuteThresholdCapture() {
        return this.jumpToMuteThresholdCapture;
    }

    public final int getJumpToMuteThresholdPlayback() {
        return this.jumpToMuteThresholdPlayback;
    }

    public final int getKeepMuteCountCapture() {
        return this.keepMuteCountCapture;
    }

    public final int getKeepMuteCountPlayback() {
        return this.keepMuteCountPlayback;
    }

    public final int getKeyMode() {
        return this.keyMode;
    }

    public final int getListenMicElectricity() {
        return this.listenMicElectricity;
    }

    public final int getListenSystemVolume() {
        return this.listenSystemVolume;
    }

    public final int getLogLevel() {
        return this.logLevel;
    }

    public final int getMicLevelFollowSysVol() {
        return this.micLevelFollowSysVol;
    }

    @NotNull
    public final double[] getMicVolcurveX() {
        return this.micVolcurveX;
    }

    @NotNull
    public final double[] getMicVolcurveY() {
        return this.micVolcurveY;
    }

    public final int getMicVolume() {
        return this.micVolume;
    }

    public final int getMicVolumeStep() {
        return this.micVolumeStep;
    }

    public final int getMusicVolume() {
        return this.musicVolume;
    }

    public final int getMusicVolumeStep() {
        return this.musicVolumeStep;
    }

    public final int getNoiseSleepIntervalCapture() {
        return this.noiseSleepIntervalCapture;
    }

    public final int getNoiseSleepIntervalPlayback() {
        return this.noiseSleepIntervalPlayback;
    }

    public final int getPlaybackAvailMin() {
        return this.playbackAvailMin;
    }

    public final int getPlaybackChannel() {
        return this.playbackChannel;
    }

    public final int getPlaybackFormat() {
        return this.playbackFormat;
    }

    public final int getPlaybackPeriodCount() {
        return this.playbackPeriodCount;
    }

    public final int getPlaybackPeriodSize() {
        return this.playbackPeriodSize;
    }

    public final int getPlaybackRate() {
        return this.playbackRate;
    }

    public final int getPlaybackSilenceSize() {
        return this.playbackSilenceSize;
    }

    public final int getPlaybackSilenceThreshold() {
        return this.playbackSilenceThreshold;
    }

    public final int getPlaybackStartThreshold() {
        return this.playbackStartThreshold;
    }

    public final int getPlaybackStopThreshold() {
        return this.playbackStopThreshold;
    }

    public final int getPlaybackWaitSleepInterval() {
        return this.playbackWaitSleepInterval;
    }

    @Nullable
    public final Integer getRecordVocalOffset() {
        return this.recordVocalOffset;
    }

    public final int getResampleBufferSize() {
        return this.resampleBufferSize;
    }

    public final int getReverbVolumeStep() {
        return this.reverbVolumeStep;
    }

    public final int getRingBufferLength() {
        return this.ringBufferLength;
    }

    public final int getRingStartThreshold() {
        return this.ringStartThreshold;
    }

    public final int getSysVolCurveSwitch() {
        return this.sysVolCurveSwitch;
    }

    @NotNull
    public final double[] getSysVolcurveSurX() {
        return this.sysVolcurveSurX;
    }

    @NotNull
    public final double[] getSysVolcurveSurY() {
        return this.sysVolcurveSurY;
    }

    @NotNull
    public final double[] getSysVolcurveX() {
        return this.sysVolcurveX;
    }

    @NotNull
    public final double[] getSysVolcurveY() {
        return this.sysVolcurveY;
    }

    public final int getSystemVolume() {
        return this.systemVolume;
    }

    public final int getVolSft() {
        return this.volSft;
    }

    public final int getVolStr() {
        return this.volStr;
    }

    @NotNull
    public final int[] getVolumeCurveMax() {
        return this.volumeCurveMax;
    }

    @NotNull
    public final double[] getVolumeCurveSystem() {
        return this.volumeCurveSystem;
    }

    @NotNull
    public final double[] getVolumeCurveUI() {
        return this.volumeCurveUI;
    }

    public final int getVolumeShiftL() {
        return this.volumeShiftL;
    }

    public final int getVolumeShiftR() {
        return this.volumeShiftR;
    }

    @NotNull
    public final String getWorkMode() {
        return this.workMode;
    }

    public final int getWorkModeInt() {
        return this.workModeInt;
    }

    public final int isMicEnable() {
        return this.isMicEnable;
    }

    public final void setAef1ReverbEnable(int i) {
        this.aef1ReverbEnable = i;
    }

    public final void setAef1ReverbOutGain(int i) {
        this.aef1ReverbOutGain = i;
    }

    public final void setAef2CompressorAttack(int i) {
        this.aef2CompressorAttack = i;
    }

    public final void setAef2CompressorEnable(int i) {
        this.aef2CompressorEnable = i;
    }

    public final void setAef2CompressorPregain(int i) {
        this.aef2CompressorPregain = i;
    }

    public final void setAef2CompressorPregainOut(@Nullable Integer num) {
        this.aef2CompressorPregainOut = num;
    }

    public final void setAef2CompressorRatio(int i) {
        this.aef2CompressorRatio = i;
    }

    public final void setAef2CompressorRelease(int i) {
        this.aef2CompressorRelease = i;
    }

    public final void setAef2CompressorThreshold(int i) {
        this.aef2CompressorThreshold = i;
    }

    public final void setAef2EQEnable(int i) {
        this.aef2EQEnable = i;
    }

    public final void setAef2EchoEnable(int i) {
        this.aef2EchoEnable = i;
    }

    public final void setAef2EchoFeedback(int i) {
        this.aef2EchoFeedback = i;
    }

    public final void setAef2EchoGain(int i) {
        this.aef2EchoGain = i;
    }

    public final void setAef2EchoTime(int i) {
        this.aef2EchoTime = i;
    }

    public final void setAef2EchoType(int i) {
        this.aef2EchoType = i;
    }

    public final void setAef2FreqShiftChangeEnable(int i) {
        this.aef2FreqShiftChangeEnable = i;
    }

    public final void setAef2MicFFTCalculateFrequency(int i) {
        this.aef2MicFFTCalculateFrequency = i;
    }

    public final void setAef2MicFFTCount(int i) {
        this.aef2MicFFTCount = i;
    }

    public final void setAef2MusicEQEnable(int i) {
        this.aef2MusicEQEnable = i;
    }

    public final void setAef2MusicFFTCalculateFrequency(int i) {
        this.aef2MusicFFTCalculateFrequency = i;
    }

    public final void setAef2MusicFFTCount(int i) {
        this.aef2MusicFFTCount = i;
    }

    public final void setAef2ReverbEnable(int i) {
        this.aef2ReverbEnable = i;
    }

    public final void setAef2ReverbTime(int i) {
        this.aef2ReverbTime = i;
    }

    public final void setAef2ReverbType(int i) {
        this.aef2ReverbType = i;
    }

    public final void setAef2ReverbVolume(int i) {
        this.aef2ReverbVolume = i;
    }

    public final void setAef2VoiceChangeEnable(int i) {
        this.aef2VoiceChangeEnable = i;
    }

    public final void setAef2VoiceMode(int i) {
        this.aef2VoiceMode = i;
    }

    public final void setAef3EchoHicut(int i) {
        this.aef3EchoHicut = i;
    }

    public final void setAef3EchoLowcut(int i) {
        this.aef3EchoLowcut = i;
    }

    public final void setAef3EchoStwide(int i) {
        this.aef3EchoStwide = i;
    }

    public final void setAef3ReverbHicut(int i) {
        this.aef3ReverbHicut = i;
    }

    public final void setAef3ReverbHidamp(int i) {
        this.aef3ReverbHidamp = i;
    }

    public final void setAef3ReverbLowcut(int i) {
        this.aef3ReverbLowcut = i;
    }

    public final void setAef3ReverbStwide(int i) {
        this.aef3ReverbStwide = i;
    }

    public final void setAef4SurroundMusicVol(@Nullable Integer num) {
        this.aef4SurroundMusicVol = num;
    }

    public final void setAef4SurroundPregain(@Nullable Integer num) {
        this.aef4SurroundPregain = num;
    }

    public final void setAudioRecord(int i) {
        this.audioRecord = i;
    }

    public final void setAudioTrack(int i) {
        this.audioTrack = i;
    }

    public final void setBalanceVolume(int i) {
        this.balanceVolume = i;
    }

    public final void setBeta(int i) {
        this.beta = i;
    }

    public final void setCaptureAvailMin(int i) {
        this.captureAvailMin = i;
    }

    public final void setCaptureChannel(int i) {
        this.captureChannel = i;
    }

    public final void setCaptureFormat(int i) {
        this.captureFormat = i;
    }

    public final void setCapturePeriodCount(int i) {
        this.capturePeriodCount = i;
    }

    public final void setCapturePeriodSize(int i) {
        this.capturePeriodSize = i;
    }

    public final void setCaptureRate(int i) {
        this.captureRate = i;
    }

    public final void setCaptureSilenceSize(int i) {
        this.captureSilenceSize = i;
    }

    public final void setCaptureSilenceThreshold(int i) {
        this.captureSilenceThreshold = i;
    }

    public final void setCaptureStartThreshold(int i) {
        this.captureStartThreshold = i;
    }

    public final void setCaptureStopThreshold(int i) {
        this.captureStopThreshold = i;
    }

    public final void setChannel(long j) {
        this.channel = j;
    }

    public final void setCheckDelayIntervalCapture(int i) {
        this.checkDelayIntervalCapture = i;
    }

    public final void setCheckDelayIntervalPlayback(int i) {
        this.checkDelayIntervalPlayback = i;
    }

    public final void setConfigVersion(int i) {
        this.configVersion = i;
    }

    public final void setDataPrintFrequency(int i) {
        this.dataPrintFrequency = i;
    }

    public final void setDebugPlaySinEnable(@Nullable Integer num) {
        this.debugPlaySinEnable = num;
    }

    public final void setDelayAddition(int i) {
        this.delayAddition = i;
    }

    public final void setDelayFluentThresholdCapture(int i) {
        this.delayFluentThresholdCapture = i;
    }

    public final void setDelayFluentThresholdPlayback(int i) {
        this.delayFluentThresholdPlayback = i;
    }

    public final void setDmaStopThreshold(int i) {
        this.dmaStopThreshold = i;
    }

    public final void setEqList(@NotNull List<EQItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.eqList = list;
    }

    public final void setHardwareMicVolume(int i) {
        this.hardwareMicVolume = i;
    }

    public final void setJumpToMuteThresholdCapture(int i) {
        this.jumpToMuteThresholdCapture = i;
    }

    public final void setJumpToMuteThresholdPlayback(int i) {
        this.jumpToMuteThresholdPlayback = i;
    }

    public final void setKeepMuteCountCapture(int i) {
        this.keepMuteCountCapture = i;
    }

    public final void setKeepMuteCountPlayback(int i) {
        this.keepMuteCountPlayback = i;
    }

    public final void setKeyMode(int i) {
        this.keyMode = i;
    }

    public final void setListenMicElectricity(int i) {
        this.listenMicElectricity = i;
    }

    public final void setListenSystemVolume(int i) {
        this.listenSystemVolume = i;
    }

    public final void setLogLevel(int i) {
        this.logLevel = i;
    }

    public final void setMicEnable(int i) {
        this.isMicEnable = i;
    }

    public final void setMicLevelFollowSysVol(int i) {
        this.micLevelFollowSysVol = i;
    }

    public final void setMicVolcurveX(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.micVolcurveX = dArr;
    }

    public final void setMicVolcurveY(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.micVolcurveY = dArr;
    }

    public final void setMicVolume(int i) {
        this.micVolume = i;
    }

    public final void setMicVolumeStep(int i) {
        this.micVolumeStep = i;
    }

    public final void setMusicVolume(int i) {
        this.musicVolume = i;
    }

    public final void setMusicVolumeStep(int i) {
        this.musicVolumeStep = i;
    }

    public final void setNoiseSleepIntervalCapture(int i) {
        this.noiseSleepIntervalCapture = i;
    }

    public final void setNoiseSleepIntervalPlayback(int i) {
        this.noiseSleepIntervalPlayback = i;
    }

    public final void setPlaybackAvailMin(int i) {
        this.playbackAvailMin = i;
    }

    public final void setPlaybackChannel(int i) {
        this.playbackChannel = i;
    }

    public final void setPlaybackFormat(int i) {
        this.playbackFormat = i;
    }

    public final void setPlaybackPeriodCount(int i) {
        this.playbackPeriodCount = i;
    }

    public final void setPlaybackPeriodSize(int i) {
        this.playbackPeriodSize = i;
    }

    public final void setPlaybackRate(int i) {
        this.playbackRate = i;
    }

    public final void setPlaybackSilenceSize(int i) {
        this.playbackSilenceSize = i;
    }

    public final void setPlaybackSilenceThreshold(int i) {
        this.playbackSilenceThreshold = i;
    }

    public final void setPlaybackStartThreshold(int i) {
        this.playbackStartThreshold = i;
    }

    public final void setPlaybackStopThreshold(int i) {
        this.playbackStopThreshold = i;
    }

    public final void setPlaybackWaitSleepInterval(int i) {
        this.playbackWaitSleepInterval = i;
    }

    public final void setRecordVocalOffset(@Nullable Integer num) {
        this.recordVocalOffset = num;
    }

    public final void setResampleBufferSize(int i) {
        this.resampleBufferSize = i;
    }

    public final void setReverbVolumeStep(int i) {
        this.reverbVolumeStep = i;
    }

    public final void setRingBufferLength(int i) {
        this.ringBufferLength = i;
    }

    public final void setRingStartThreshold(int i) {
        this.ringStartThreshold = i;
    }

    public final void setSysVolCurveSwitch(int i) {
        this.sysVolCurveSwitch = i;
    }

    public final void setSysVolcurveSurX(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.sysVolcurveSurX = dArr;
    }

    public final void setSysVolcurveSurY(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.sysVolcurveSurY = dArr;
    }

    public final void setSysVolcurveX(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.sysVolcurveX = dArr;
    }

    public final void setSysVolcurveY(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.sysVolcurveY = dArr;
    }

    public final void setSystemVolume(int i) {
        this.systemVolume = i;
    }

    public final void setVolSft(int i) {
        this.volSft = i;
    }

    public final void setVolStr(int i) {
        this.volStr = i;
    }

    public final void setVolumeCurveMax(@NotNull int[] iArr) {
        Intrinsics.checkNotNullParameter(iArr, "<set-?>");
        this.volumeCurveMax = iArr;
    }

    public final void setVolumeCurveSystem(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.volumeCurveSystem = dArr;
    }

    public final void setVolumeCurveUI(@NotNull double[] dArr) {
        Intrinsics.checkNotNullParameter(dArr, "<set-?>");
        this.volumeCurveUI = dArr;
    }

    public final void setVolumeShiftL(int i) {
        this.volumeShiftL = i;
    }

    public final void setVolumeShiftR(int i) {
        this.volumeShiftR = i;
    }

    public final void setWorkMode(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.workMode = str;
    }

    public final void setWorkModeInt(int i) {
        this.workModeInt = i;
    }
}
