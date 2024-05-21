package com.loostone.libtuning.inf.extern;

import com.xiaopeng.xuiservice.xapp.speech.SpeechConstants;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000/\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0003\b»\u0001\n\u0002\u0010\u0006\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\t\bf\u0018\u00002\u00020\u0001J\u0019\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0005\u0010\u0006J\u000f\u0010\u0007\u001a\u00020\u0002H&¢\u0006\u0004\b\u0007\u0010\bJ\u0019\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0004\b\u000b\u0010\fJ\u000f\u0010\r\u001a\u00020\tH&¢\u0006\u0004\b\r\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u000f\u0010\u0006J\u000f\u0010\u0010\u001a\u00020\u0002H&¢\u0006\u0004\b\u0010\u0010\bJ\u0019\u0010\u0011\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0011\u0010\u0006J\u000f\u0010\u0012\u001a\u00020\u0002H&¢\u0006\u0004\b\u0012\u0010\bJ\u0019\u0010\u0014\u001a\u00020\u00042\b\b\u0002\u0010\u0013\u001a\u00020\u0002H&¢\u0006\u0004\b\u0014\u0010\u0006J\u000f\u0010\u0015\u001a\u00020\u0002H&¢\u0006\u0004\b\u0015\u0010\bJ\u0019\u0010\u0016\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0004\b\u0016\u0010\fJ\u000f\u0010\u0017\u001a\u00020\tH&¢\u0006\u0004\b\u0017\u0010\u000eJ\u0019\u0010\u0018\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0018\u0010\u0006J\u000f\u0010\u0019\u001a\u00020\u0002H&¢\u0006\u0004\b\u0019\u0010\bJ\u0019\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0004\b\u001a\u0010\fJ\u000f\u0010\u001b\u001a\u00020\tH&¢\u0006\u0004\b\u001b\u0010\u000eJ\u0019\u0010\u001c\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0004\b\u001c\u0010\fJ\u000f\u0010\u001d\u001a\u00020\tH&¢\u0006\u0004\b\u001d\u0010\u000eJ\u0017\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b\u001f\u0010\u0006J\u0017\u0010 \u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b \u0010\u0006J\u0017\u0010!\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b!\u0010\u0006J\u0017\u0010\"\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b\"\u0010\u0006J\u0017\u0010#\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b#\u0010\u0006J\u0017\u0010$\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b$\u0010\u0006J\u0017\u0010%\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b%\u0010\u0006J\u0017\u0010&\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b&\u0010\u0006J\u0017\u0010'\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b'\u0010\u0006J\u0017\u0010)\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b)\u0010\u0006J\u0017\u0010*\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b*\u0010\u0006J\u0017\u0010+\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b+\u0010\u0006J\u0017\u0010,\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b,\u0010\u0006J\u0017\u0010-\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b-\u0010\u0006J\u0017\u0010.\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b.\u0010\u0006J\u0017\u0010/\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b/\u0010\u0006J\u0017\u00100\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b0\u0010\u0006J\u0017\u00101\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b1\u0010\u0006J\u0017\u00102\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b2\u0010\u0006J\u0017\u00103\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b3\u0010\u0006J\u0017\u00104\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b4\u0010\u0006J\u0017\u00105\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b5\u0010\u0006J\u0017\u00106\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b6\u0010\u0006J\u0017\u00107\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b7\u0010\u0006J\u0017\u00108\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b8\u0010\u0006J\u0017\u00109\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b9\u0010\u0006J\u0017\u0010:\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\b:\u0010\u0006J\u0017\u0010;\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b;\u0010\u0006J\u0017\u0010<\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b<\u0010\u0006J\u0017\u0010=\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b=\u0010\u0006J\u0017\u0010>\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b>\u0010\u0006J\u0017\u0010?\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b?\u0010\u0006J\u0017\u0010@\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\b@\u0010\u0006J\u0017\u0010A\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\bA\u0010\u0006J\u0017\u0010B\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\bB\u0010\u0006J\u0017\u0010C\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u0002H&¢\u0006\u0004\bC\u0010\u0006J\u0017\u0010D\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bD\u0010\u0006J\u0017\u0010E\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bE\u0010\u0006J\u0017\u0010F\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bF\u0010\u0006J\u0017\u0010G\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bG\u0010\u0006J\u0017\u0010H\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bH\u0010\u0006J\u0017\u0010I\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bI\u0010\u0006J\u0017\u0010J\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bJ\u0010\u0006J\u0017\u0010K\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bK\u0010\u0006J\u0017\u0010L\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0002H&¢\u0006\u0004\bL\u0010\u0006J\u0017\u0010N\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bN\u0010\u0006J\u0017\u0010O\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bO\u0010\u0006J\u0017\u0010P\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bP\u0010\u0006J\u0017\u0010Q\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bQ\u0010\u0006J\u0017\u0010R\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bR\u0010\u0006J\u0017\u0010S\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bS\u0010\u0006J\u0017\u0010T\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bT\u0010\u0006J\u0017\u0010U\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bU\u0010\u0006J\u0017\u0010V\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0004\bV\u0010\u0006J\u000f\u0010W\u001a\u00020\u0002H&¢\u0006\u0004\bW\u0010\bJ\u000f\u0010X\u001a\u00020\u0002H&¢\u0006\u0004\bX\u0010\bJ\u000f\u0010Y\u001a\u00020\u0002H&¢\u0006\u0004\bY\u0010\bJ\u000f\u0010Z\u001a\u00020\u0002H&¢\u0006\u0004\bZ\u0010\bJ\u000f\u0010[\u001a\u00020\u0002H&¢\u0006\u0004\b[\u0010\bJ\u000f\u0010\\\u001a\u00020\u0002H&¢\u0006\u0004\b\\\u0010\bJ\u000f\u0010]\u001a\u00020\u0002H&¢\u0006\u0004\b]\u0010\bJ\u000f\u0010^\u001a\u00020\u0002H&¢\u0006\u0004\b^\u0010\bJ\u000f\u0010_\u001a\u00020\u0002H&¢\u0006\u0004\b_\u0010\bJ\u000f\u0010`\u001a\u00020\u0002H&¢\u0006\u0004\b`\u0010\bJ\u000f\u0010a\u001a\u00020\u0002H&¢\u0006\u0004\ba\u0010\bJ\u000f\u0010b\u001a\u00020\u0002H&¢\u0006\u0004\bb\u0010\bJ\u000f\u0010c\u001a\u00020\u0002H&¢\u0006\u0004\bc\u0010\bJ\u000f\u0010d\u001a\u00020\u0002H&¢\u0006\u0004\bd\u0010\bJ\u000f\u0010e\u001a\u00020\u0002H&¢\u0006\u0004\be\u0010\bJ\u000f\u0010f\u001a\u00020\u0002H&¢\u0006\u0004\bf\u0010\bJ\u000f\u0010g\u001a\u00020\u0002H&¢\u0006\u0004\bg\u0010\bJ\u000f\u0010h\u001a\u00020\u0002H&¢\u0006\u0004\bh\u0010\bJ\u000f\u0010i\u001a\u00020\u0002H&¢\u0006\u0004\bi\u0010\bJ\u000f\u0010j\u001a\u00020\u0002H&¢\u0006\u0004\bj\u0010\bJ\u000f\u0010k\u001a\u00020\u0002H&¢\u0006\u0004\bk\u0010\bJ\u000f\u0010l\u001a\u00020\u0002H&¢\u0006\u0004\bl\u0010\bJ\u000f\u0010m\u001a\u00020\u0002H&¢\u0006\u0004\bm\u0010\bJ\u000f\u0010n\u001a\u00020\u0002H&¢\u0006\u0004\bn\u0010\bJ\u000f\u0010o\u001a\u00020\u0002H&¢\u0006\u0004\bo\u0010\bJ\u000f\u0010p\u001a\u00020\u0002H&¢\u0006\u0004\bp\u0010\bJ\u000f\u0010q\u001a\u00020\u0002H&¢\u0006\u0004\bq\u0010\bJ\u000f\u0010r\u001a\u00020\u0002H&¢\u0006\u0004\br\u0010\bJ\u000f\u0010s\u001a\u00020\u0002H&¢\u0006\u0004\bs\u0010\bJ\u000f\u0010t\u001a\u00020\u0002H&¢\u0006\u0004\bt\u0010\bJ\u000f\u0010u\u001a\u00020\u0002H&¢\u0006\u0004\bu\u0010\bJ\u000f\u0010v\u001a\u00020\u0002H&¢\u0006\u0004\bv\u0010\bJ\u000f\u0010w\u001a\u00020\u0002H&¢\u0006\u0004\bw\u0010\bJ\u000f\u0010x\u001a\u00020\u0002H&¢\u0006\u0004\bx\u0010\bJ\u000f\u0010y\u001a\u00020\u0002H&¢\u0006\u0004\by\u0010\bJ\u000f\u0010z\u001a\u00020\u0002H&¢\u0006\u0004\bz\u0010\bJ\u000f\u0010{\u001a\u00020\u0002H&¢\u0006\u0004\b{\u0010\bJ\u000f\u0010|\u001a\u00020\u0002H&¢\u0006\u0004\b|\u0010\bJ\u000f\u0010}\u001a\u00020\u0002H&¢\u0006\u0004\b}\u0010\bJ\u000f\u0010~\u001a\u00020\u0002H&¢\u0006\u0004\b~\u0010\bJ\u000f\u0010\u007f\u001a\u00020\u0002H&¢\u0006\u0004\b\u007f\u0010\bJ\u0011\u0010\u0080\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0080\u0001\u0010\bJ\u0011\u0010\u0081\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0081\u0001\u0010\bJ\u0011\u0010\u0082\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0082\u0001\u0010\bJ\u0011\u0010\u0083\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0083\u0001\u0010\bJ\u0011\u0010\u0084\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0084\u0001\u0010\bJ\u0011\u0010\u0085\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0085\u0001\u0010\bJ\u0011\u0010\u0086\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0086\u0001\u0010\bJ\u0011\u0010\u0087\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0087\u0001\u0010\bJ\u0011\u0010\u0088\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0088\u0001\u0010\bJ\u0011\u0010\u0089\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0089\u0001\u0010\bJ\u0011\u0010\u008a\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u008a\u0001\u0010\bJ\u0011\u0010\u008b\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u008b\u0001\u0010\bJ\u0011\u0010\u008c\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u008c\u0001\u0010\bJ\u0019\u0010\u008d\u0001\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\tH&¢\u0006\u0005\b\u008d\u0001\u0010\fJ\u0011\u0010\u008e\u0001\u001a\u00020\tH&¢\u0006\u0005\b\u008e\u0001\u0010\u000eJ\u001c\u0010\u0090\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u008f\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0090\u0001\u0010\u0006J\u0011\u0010\u0091\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0091\u0001\u0010\bJ\u001c\u0010\u0093\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0092\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0093\u0001\u0010\u0006J\u0011\u0010\u0094\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0094\u0001\u0010\bJ\u001c\u0010\u0096\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0096\u0001\u0010\u0006J\u0011\u0010\u0097\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0097\u0001\u0010\bJ\u001c\u0010\u0099\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u0099\u0001\u0010\u0006J\u0011\u0010\u009a\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u009a\u0001\u0010\bJ\u001a\u0010\u009b\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u009b\u0001\u0010\u0006J\u0011\u0010\u009c\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u009c\u0001\u0010\bJ\u001a\u0010\u009d\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u009d\u0001\u0010\u0006J\u0011\u0010\u009e\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u009e\u0001\u0010\bJ\u001a\u0010\u009f\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u009f\u0001\u0010\u0006J\u0011\u0010 \u0001\u001a\u00020\u0002H&¢\u0006\u0005\b \u0001\u0010\bJ\u001b\u0010¡\u0001\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0005\b¡\u0001\u0010\fJ\u0011\u0010¢\u0001\u001a\u00020\tH&¢\u0006\u0005\b¢\u0001\u0010\u000eJ\u001a\u0010£\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b£\u0001\u0010\u0006J\u0011\u0010¤\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¤\u0001\u0010\bJ\u001c\u0010¦\u0001\u001a\u00020\u00042\t\b\u0002\u0010¥\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¦\u0001\u0010\u0006J\u0011\u0010§\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b§\u0001\u0010\bJ\u001b\u0010¨\u0001\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0005\b¨\u0001\u0010\u0006J\u0011\u0010©\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b©\u0001\u0010\bJ\u001c\u0010«\u0001\u001a\u00020\u00042\t\b\u0002\u0010ª\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b«\u0001\u0010\u0006J\u0011\u0010¬\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¬\u0001\u0010\bJ\u001a\u0010\u00ad\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b\u00ad\u0001\u0010\u0006J\u0011\u0010®\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b®\u0001\u0010\bJ\u001a\u0010¯\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¯\u0001\u0010\u0006J\u0011\u0010°\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b°\u0001\u0010\bJ\u001a\u0010±\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b±\u0001\u0010\u0006J\u0011\u0010²\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b²\u0001\u0010\bJ\u001a\u0010³\u0001\u001a\u00020\u00042\u0007\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b³\u0001\u0010\u0006J\u0011\u0010´\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b´\u0001\u0010\bJ\u001b\u0010µ\u0001\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0005\bµ\u0001\u0010\fJ\u0011\u0010¶\u0001\u001a\u00020\tH&¢\u0006\u0005\b¶\u0001\u0010\u000eJ\u001c\u0010·\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b·\u0001\u0010\u0006J\u0011\u0010¸\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¸\u0001\u0010\bJ\u001c\u0010¹\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¹\u0001\u0010\u0006J\u0011\u0010º\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bº\u0001\u0010\bJ\u001c\u0010»\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b»\u0001\u0010\u0006J\u0011\u0010¼\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¼\u0001\u0010\bJ\u001c\u0010½\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b½\u0001\u0010\u0006J\u0011\u0010¾\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¾\u0001\u0010\bJ\u001c\u0010¿\u0001\u001a\u00020\u00042\t\b\u0002\u0010\u0098\u0001\u001a\u00020\u0002H&¢\u0006\u0005\b¿\u0001\u0010\u0006J\u0011\u0010À\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÀ\u0001\u0010\bJ\u001b\u0010Á\u0001\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\tH&¢\u0006\u0005\bÁ\u0001\u0010\fJ\u0011\u0010Â\u0001\u001a\u00020\tH&¢\u0006\u0005\bÂ\u0001\u0010\u000eJ\u001c\u0010Ã\u0001\u001a\u00020\u00042\t\b\u0002\u0010¥\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÃ\u0001\u0010\u0006J\u0011\u0010Ä\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÄ\u0001\u0010\bJ)\u0010Ç\u0001\u001a\u00020\u00042\t\b\u0002\u0010¥\u0001\u001a\u00020\u00022\n\b\u0002\u0010Æ\u0001\u001a\u00030Å\u0001H&¢\u0006\u0006\bÇ\u0001\u0010È\u0001J\u0011\u0010É\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÉ\u0001\u0010\bJ\u001c\u0010Ê\u0001\u001a\u00020\u00042\t\b\u0002\u0010¥\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÊ\u0001\u0010\u0006J\u0011\u0010Ë\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bË\u0001\u0010\bJ\u001b\u0010Ì\u0001\u001a\u00020\u00042\b\b\u0002\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0005\bÌ\u0001\u0010\u0006J\u0011\u0010Í\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÍ\u0001\u0010\bJ\u001b\u0010Î\u0001\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\u0002H&¢\u0006\u0005\bÎ\u0001\u0010\u0006J\u0011\u0010Ï\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÏ\u0001\u0010\bJ\u0011\u0010Ð\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÐ\u0001\u0010\bJ\u001c\u0010Ó\u0001\u001a\u00020\u00042\b\u0010Ò\u0001\u001a\u00030Ñ\u0001H&¢\u0006\u0006\bÓ\u0001\u0010Ô\u0001J\u0011\u0010Õ\u0001\u001a\u00020\u0002H&¢\u0006\u0005\bÕ\u0001\u0010\bJ\u0012\u0010Ö\u0001\u001a\u00020\u0004H&¢\u0006\u0006\bÖ\u0001\u0010×\u0001J\u0019\u0010Ø\u0001\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0005\bØ\u0001\u0010\u0006J\u0019\u0010Ù\u0001\u001a\u00020\u00042\u0006\u0010M\u001a\u00020\u0002H&¢\u0006\u0005\bÙ\u0001\u0010\u0006¨\u0006Ú\u0001"}, d2 = {"Lcom/loostone/libtuning/inf/extern/IVolumeAdjustment;", "", "", "volume", "", "setMicVolume", "(I)V", "getMicVolume", "()I", "", "enable", "setMicVolumeEnable", "(Z)V", "isMicVolumeEnable", "()Z", "setSystemVolume", "getSystemVolume", "setMusicVolume", "getMusicVolume", "balance", "setBalance", "getBalance", "setAef1ReverbEnable", "isAef1ReverbEnable", "setAef1ReverbOutGain", "getAef1ReverbOutGain", "setAef2EQEnable", "isAef2EQEnable", "setAef2MusicEQEnable", "isAef2MusicEQEnable", "gain", "setAef2EQ1", "setAef2EQ2", "setAef2EQ3", "setAef2EQ4", "setAef2EQ5", "setAef2EQ6", "setAef2EQ7", "setAef2EQ8", "setAef2EQ9", "frequency", "setAef2EQ1Frequency", "setAef2EQ2Frequency", "setAef2EQ3Frequency", "setAef2EQ4Frequency", "setAef2EQ5Frequency", "setAef2EQ6Frequency", "setAef2EQ7Frequency", "setAef2EQ8Frequency", "setAef2EQ9Frequency", "setAef2MusicEQ1", "setAef2MusicEQ2", "setAef2MusicEQ3", "setAef2MusicEQ4", "setAef2MusicEQ5", "setAef2MusicEQ6", "setAef2MusicEQ7", "setAef2MusicEQ8", "setAef2MusicEQ9", "setAef2MusicEQ1Frequency", "setAef2MusicEQ2Frequency", "setAef2MusicEQ3Frequency", "setAef2MusicEQ4Frequency", "setAef2MusicEQ5Frequency", "setAef2MusicEQ6Frequency", "setAef2MusicEQ7Frequency", "setAef2MusicEQ8Frequency", "setAef2MusicEQ9Frequency", "setAef3EqQ1", "setAef3EqQ2", "setAef3EqQ3", "setAef3EqQ4", "setAef3EqQ5", "setAef3EqQ6", "setAef3EqQ7", "setAef3EqQ8", "setAef3EqQ9", SpeechConstants.KEY_COMMAND_TYPE, "setAef3EQ1Type", "setAef3EQ2Type", "setAef3EQ3Type", "setAef3EQ4Type", "setAef3EQ5Type", "setAef3EQ6Type", "setAef3EQ7Type", "setAef3EQ8Type", "setAef3EQ9Type", "getAef2EQ1", "getAef2EQ2", "getAef2EQ3", "getAef2EQ4", "getAef2EQ5", "getAef2EQ6", "getAef2EQ7", "getAef2EQ8", "getAef2EQ9", "getAef2EQ1Frequency", "getAef2EQ2Frequency", "getAef2EQ3Frequency", "getAef2EQ4Frequency", "getAef2EQ5Frequency", "getAef2EQ6Frequency", "getAef2EQ7Frequency", "getAef2EQ8Frequency", "getAef2EQ9Frequency", "getAef2MusicEQ1", "getAef2MusicEQ2", "getAef2MusicEQ3", "getAef2MusicEQ4", "getAef2MusicEQ5", "getAef2MusicEQ6", "getAef2MusicEQ7", "getAef2MusicEQ8", "getAef2MusicEQ9", "getAef2MusicEQ1Frequency", "getAef2MusicEQ2Frequency", "getAef2MusicEQ3Frequency", "getAef2MusicEQ4Frequency", "getAef2MusicEQ5Frequency", "getAef2MusicEQ6Frequency", "getAef2MusicEQ7Frequency", "getAef2MusicEQ8Frequency", "getAef2MusicEQ9Frequency", "getAef3EqQ1", "getAef3EqQ2", "getAef3EqQ3", "getAef3EqQ4", "getAef3EqQ5", "getAef3EqQ6", "getAef3EqQ7", "getAef3EqQ8", "getAef3EqQ9", "getAef3EQ1Type", "getAef3EQ2Type", "getAef3EQ3Type", "getAef3EQ4Type", "getAef3EQ5Type", "getAef3EQ6Type", "getAef3EQ7Type", "getAef3EQ8Type", "getAef3EQ9Type", "setAef2EchoEnable", "isAef2EchoEnable", "echoGain", "setAef2EchoGain", "getAef2EchoGain", "echoType", "setAef2EchoType", "getAef2EchoType", "echoTime", "setAef2EchoTime", "getAef2EchoTime", "value", "setAef2EchoFeedBack", "getAef2EchoFeedBack", "setAef3EchoHicut", "getAef3EchoHicut", "setAef3EchoLowcut", "getAef3EchoLowcut", "setAef3EchoStwide", "getAef3EchoStwide", "setAef2ReverbEnable", "isAef2ReverbEnable", "setAef3ReverbFsEnable", "getAef3ReverbFsEnable", SpeechConstants.KEY_MODE, "setAef2ReverbMode", "getAef2ReverbMode", "setAef2ReverbVolume", "getAef2ReverbVolume", "time", "setAef2ReverbTime", "getAef2ReverbTime", "setAef3ReverbHicut", "getAef3ReverbHicut", "setAef3ReverbHidamp", "getAef3ReverbHidamp", "setAef3ReverbLowcut", "getAef3ReverbLowcut", "setAef3ReverbStwide", "getAef3ReverbStwide", "setAef2CompressorEnable", "isAef2CompressorEnable", "setAef2CompressorRatio", "getAef2CompressorRatio", "setAef2CompressorThreshold", "getAef2CompressorThreshold", "setAef2CompressorAttack", "getAef2CompressorAttack", "setAef2CompressorRelease", "getAef2CompressorRelease", "setAef2CompressorPregain", "getAef2CompressorPregain", "setAef2VoiceChangeEnable", "isAef2VoiceChangeEnable", "setAef2VoiceMode", "getAef2VoiceMode", "", "fixGain", "setEnvironmentalSound", "(ID)V", "getEnvironmentalSound", "setAef4SceneEffectMode", "getAef4SceneEffectMode", "setAef4ReverbVolume", "getAef4ReverbVolume", "setAef4BypassEnable", "getAef4BypassEnable", "getRecordVocalOffset", "", "deviceCard", "setDeviceCard", "(Ljava/lang/String;)V", "getCarOutPregainAdd", "recoveryFromJson", "()V", "volumeUp", "volumeDown", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public interface IVolumeAdjustment {

    @Metadata(bv = {1, 0, 3}, d1 = {}, d2 = {}, k = 3, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class DefaultImpls {
        public static /* synthetic */ void setAef1ReverbEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef1ReverbEnable");
            }
            if ((i & 1) != 0) {
                z = false;
            }
            iVolumeAdjustment.setAef1ReverbEnable(z);
        }

        public static /* synthetic */ void setAef1ReverbOutGain$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef1ReverbOutGain");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef1ReverbOutGain(i);
        }

        public static /* synthetic */ void setAef2CompressorAttack$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2CompressorAttack");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2CompressorAttack(i);
        }

        public static /* synthetic */ void setAef2CompressorEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2CompressorEnable");
            }
            if ((i & 1) != 0) {
                z = true;
            }
            iVolumeAdjustment.setAef2CompressorEnable(z);
        }

        public static /* synthetic */ void setAef2CompressorPregain$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2CompressorPregain");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2CompressorPregain(i);
        }

        public static /* synthetic */ void setAef2CompressorRatio$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2CompressorRatio");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2CompressorRatio(i);
        }

        public static /* synthetic */ void setAef2CompressorRelease$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2CompressorRelease");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2CompressorRelease(i);
        }

        public static /* synthetic */ void setAef2CompressorThreshold$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2CompressorThreshold");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2CompressorThreshold(i);
        }

        public static /* synthetic */ void setAef2EQEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2EQEnable");
            }
            if ((i & 1) != 0) {
                z = true;
            }
            iVolumeAdjustment.setAef2EQEnable(z);
        }

        public static /* synthetic */ void setAef2EchoFeedBack$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2EchoFeedBack");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2EchoFeedBack(i);
        }

        public static /* synthetic */ void setAef2EchoGain$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2EchoGain");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2EchoGain(i);
        }

        public static /* synthetic */ void setAef2EchoTime$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2EchoTime");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2EchoTime(i);
        }

        public static /* synthetic */ void setAef2EchoType$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2EchoType");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2EchoType(i);
        }

        public static /* synthetic */ void setAef2MusicEQEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2MusicEQEnable");
            }
            if ((i & 1) != 0) {
                z = true;
            }
            iVolumeAdjustment.setAef2MusicEQEnable(z);
        }

        public static /* synthetic */ void setAef2ReverbEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2ReverbEnable");
            }
            if ((i & 1) != 0) {
                z = true;
            }
            iVolumeAdjustment.setAef2ReverbEnable(z);
        }

        public static /* synthetic */ void setAef2ReverbMode$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2ReverbMode");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2ReverbMode(i);
        }

        public static /* synthetic */ void setAef2ReverbTime$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2ReverbTime");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2ReverbTime(i);
        }

        public static /* synthetic */ void setAef2ReverbVolume$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2ReverbVolume");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2ReverbVolume(i);
        }

        public static /* synthetic */ void setAef2VoiceChangeEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2VoiceChangeEnable");
            }
            if ((i & 1) != 0) {
                z = true;
            }
            iVolumeAdjustment.setAef2VoiceChangeEnable(z);
        }

        public static /* synthetic */ void setAef2VoiceMode$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef2VoiceMode");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef2VoiceMode(i);
        }

        public static /* synthetic */ void setAef4BypassEnable$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef4BypassEnable");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef4BypassEnable(i);
        }

        public static /* synthetic */ void setAef4ReverbVolume$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef4ReverbVolume");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef4ReverbVolume(i);
        }

        public static /* synthetic */ void setAef4SceneEffectMode$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setAef4SceneEffectMode");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setAef4SceneEffectMode(i);
        }

        public static /* synthetic */ void setBalance$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setBalance");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setBalance(i);
        }

        public static /* synthetic */ void setEnvironmentalSound$default(IVolumeAdjustment iVolumeAdjustment, int i, double d, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setEnvironmentalSound");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            if ((i2 & 2) != 0) {
                d = 1.0d;
            }
            iVolumeAdjustment.setEnvironmentalSound(i, d);
        }

        public static /* synthetic */ void setMicVolume$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setMicVolume");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setMicVolume(i);
        }

        public static /* synthetic */ void setMicVolumeEnable$default(IVolumeAdjustment iVolumeAdjustment, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setMicVolumeEnable");
            }
            if ((i & 1) != 0) {
                z = false;
            }
            iVolumeAdjustment.setMicVolumeEnable(z);
        }

        public static /* synthetic */ void setMusicVolume$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setMusicVolume");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setMusicVolume(i);
        }

        public static /* synthetic */ void setSystemVolume$default(IVolumeAdjustment iVolumeAdjustment, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setSystemVolume");
            }
            if ((i2 & 1) != 0) {
                i = 0;
            }
            iVolumeAdjustment.setSystemVolume(i);
        }
    }

    int getAef1ReverbOutGain();

    int getAef2CompressorAttack();

    int getAef2CompressorPregain();

    int getAef2CompressorRatio();

    int getAef2CompressorRelease();

    int getAef2CompressorThreshold();

    int getAef2EQ1();

    int getAef2EQ1Frequency();

    int getAef2EQ2();

    int getAef2EQ2Frequency();

    int getAef2EQ3();

    int getAef2EQ3Frequency();

    int getAef2EQ4();

    int getAef2EQ4Frequency();

    int getAef2EQ5();

    int getAef2EQ5Frequency();

    int getAef2EQ6();

    int getAef2EQ6Frequency();

    int getAef2EQ7();

    int getAef2EQ7Frequency();

    int getAef2EQ8();

    int getAef2EQ8Frequency();

    int getAef2EQ9();

    int getAef2EQ9Frequency();

    int getAef2EchoFeedBack();

    int getAef2EchoGain();

    int getAef2EchoTime();

    int getAef2EchoType();

    int getAef2MusicEQ1();

    int getAef2MusicEQ1Frequency();

    int getAef2MusicEQ2();

    int getAef2MusicEQ2Frequency();

    int getAef2MusicEQ3();

    int getAef2MusicEQ3Frequency();

    int getAef2MusicEQ4();

    int getAef2MusicEQ4Frequency();

    int getAef2MusicEQ5();

    int getAef2MusicEQ5Frequency();

    int getAef2MusicEQ6();

    int getAef2MusicEQ6Frequency();

    int getAef2MusicEQ7();

    int getAef2MusicEQ7Frequency();

    int getAef2MusicEQ8();

    int getAef2MusicEQ8Frequency();

    int getAef2MusicEQ9();

    int getAef2MusicEQ9Frequency();

    int getAef2ReverbMode();

    int getAef2ReverbTime();

    int getAef2ReverbVolume();

    int getAef2VoiceMode();

    int getAef3EQ1Type();

    int getAef3EQ2Type();

    int getAef3EQ3Type();

    int getAef3EQ4Type();

    int getAef3EQ5Type();

    int getAef3EQ6Type();

    int getAef3EQ7Type();

    int getAef3EQ8Type();

    int getAef3EQ9Type();

    int getAef3EchoHicut();

    int getAef3EchoLowcut();

    int getAef3EchoStwide();

    int getAef3EqQ1();

    int getAef3EqQ2();

    int getAef3EqQ3();

    int getAef3EqQ4();

    int getAef3EqQ5();

    int getAef3EqQ6();

    int getAef3EqQ7();

    int getAef3EqQ8();

    int getAef3EqQ9();

    int getAef3ReverbFsEnable();

    int getAef3ReverbHicut();

    int getAef3ReverbHidamp();

    int getAef3ReverbLowcut();

    int getAef3ReverbStwide();

    int getAef4BypassEnable();

    int getAef4ReverbVolume();

    int getAef4SceneEffectMode();

    int getBalance();

    int getCarOutPregainAdd();

    int getEnvironmentalSound();

    int getMicVolume();

    int getMusicVolume();

    int getRecordVocalOffset();

    int getSystemVolume();

    boolean isAef1ReverbEnable();

    boolean isAef2CompressorEnable();

    boolean isAef2EQEnable();

    boolean isAef2EchoEnable();

    boolean isAef2MusicEQEnable();

    boolean isAef2ReverbEnable();

    boolean isAef2VoiceChangeEnable();

    boolean isMicVolumeEnable();

    void recoveryFromJson();

    void setAef1ReverbEnable(boolean z);

    void setAef1ReverbOutGain(int i);

    void setAef2CompressorAttack(int i);

    void setAef2CompressorEnable(boolean z);

    void setAef2CompressorPregain(int i);

    void setAef2CompressorRatio(int i);

    void setAef2CompressorRelease(int i);

    void setAef2CompressorThreshold(int i);

    void setAef2EQ1(int i);

    void setAef2EQ1Frequency(int i);

    void setAef2EQ2(int i);

    void setAef2EQ2Frequency(int i);

    void setAef2EQ3(int i);

    void setAef2EQ3Frequency(int i);

    void setAef2EQ4(int i);

    void setAef2EQ4Frequency(int i);

    void setAef2EQ5(int i);

    void setAef2EQ5Frequency(int i);

    void setAef2EQ6(int i);

    void setAef2EQ6Frequency(int i);

    void setAef2EQ7(int i);

    void setAef2EQ7Frequency(int i);

    void setAef2EQ8(int i);

    void setAef2EQ8Frequency(int i);

    void setAef2EQ9(int i);

    void setAef2EQ9Frequency(int i);

    void setAef2EQEnable(boolean z);

    void setAef2EchoEnable(boolean z);

    void setAef2EchoFeedBack(int i);

    void setAef2EchoGain(int i);

    void setAef2EchoTime(int i);

    void setAef2EchoType(int i);

    void setAef2MusicEQ1(int i);

    void setAef2MusicEQ1Frequency(int i);

    void setAef2MusicEQ2(int i);

    void setAef2MusicEQ2Frequency(int i);

    void setAef2MusicEQ3(int i);

    void setAef2MusicEQ3Frequency(int i);

    void setAef2MusicEQ4(int i);

    void setAef2MusicEQ4Frequency(int i);

    void setAef2MusicEQ5(int i);

    void setAef2MusicEQ5Frequency(int i);

    void setAef2MusicEQ6(int i);

    void setAef2MusicEQ6Frequency(int i);

    void setAef2MusicEQ7(int i);

    void setAef2MusicEQ7Frequency(int i);

    void setAef2MusicEQ8(int i);

    void setAef2MusicEQ8Frequency(int i);

    void setAef2MusicEQ9(int i);

    void setAef2MusicEQ9Frequency(int i);

    void setAef2MusicEQEnable(boolean z);

    void setAef2ReverbEnable(boolean z);

    void setAef2ReverbMode(int i);

    void setAef2ReverbTime(int i);

    void setAef2ReverbVolume(int i);

    void setAef2VoiceChangeEnable(boolean z);

    void setAef2VoiceMode(int i);

    void setAef3EQ1Type(int i);

    void setAef3EQ2Type(int i);

    void setAef3EQ3Type(int i);

    void setAef3EQ4Type(int i);

    void setAef3EQ5Type(int i);

    void setAef3EQ6Type(int i);

    void setAef3EQ7Type(int i);

    void setAef3EQ8Type(int i);

    void setAef3EQ9Type(int i);

    void setAef3EchoHicut(int i);

    void setAef3EchoLowcut(int i);

    void setAef3EchoStwide(int i);

    void setAef3EqQ1(int i);

    void setAef3EqQ2(int i);

    void setAef3EqQ3(int i);

    void setAef3EqQ4(int i);

    void setAef3EqQ5(int i);

    void setAef3EqQ6(int i);

    void setAef3EqQ7(int i);

    void setAef3EqQ8(int i);

    void setAef3EqQ9(int i);

    void setAef3ReverbFsEnable(int i);

    void setAef3ReverbHicut(int i);

    void setAef3ReverbHidamp(int i);

    void setAef3ReverbLowcut(int i);

    void setAef3ReverbStwide(int i);

    void setAef4BypassEnable(int i);

    void setAef4ReverbVolume(int i);

    void setAef4SceneEffectMode(int i);

    void setBalance(int i);

    void setDeviceCard(@NotNull String str);

    void setEnvironmentalSound(int i, double d);

    void setMicVolume(int i);

    void setMicVolumeEnable(boolean z);

    void setMusicVolume(int i);

    void setSystemVolume(int i);

    void volumeDown(int i);

    void volumeUp(int i);
}
