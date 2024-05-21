package com.qiyi.video.qys;

import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import com.qiyi.video.client.IQYSConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes4.dex */
public class RequestExecutorHelper {
    private final Map<MethodKey, MethodHolder> mMethodMap = new ConcurrentHashMap();
    private final Object mObject;

    public RequestExecutorHelper(Object object) {
        this.mObject = object;
    }

    public void executeAsync(IQYSRequest request, IQYSCallback callback) throws RemoteException {
        MethodHolder holder = getMethodHolder(request, false);
        if (holder == null) {
            if (callback != null) {
                IQYSResponse response = new IQYSResponse();
                response.mCode = IQYSConstants.RESPONSE_CODE_PARAMS_ERROR;
                response.mMsg = new IQYSString("need command param");
                callback.onResponse(response);
            }
        } else if (holder.mMethod == null) {
            if (callback != null) {
                IQYSResponse response2 = new IQYSResponse();
                response2.mCode = IQYSConstants.RESPONSE_CODE_CMD_NOT_EXIT;
                response2.mMsg = new IQYSString("command is not found");
                callback.onResponse(response2);
            }
        } else if (holder.mSync) {
            try {
                Class<?> returnType = holder.mMethod.getReturnType();
                if (returnType == IQYSResponse.class) {
                    Object invoke = holder.mMethod.invoke(this.mObject, holder.mHasRequest ? new Object[]{request} : new Object[0]);
                    if (callback != null) {
                        callback.onResponse((IQYSResponse) invoke);
                    }
                    return;
                }
                if (returnType != Void.class && returnType != Void.TYPE) {
                    if (callback != null) {
                        IQYSResponse response3 = new IQYSResponse();
                        response3.mCode = IQYSConstants.RESPONSE_CODE_UNKNOWN_ERROR;
                        response3.mMsg = new IQYSString("remote error, return type is " + returnType);
                        callback.onResponse(response3);
                        return;
                    }
                    return;
                }
                holder.mMethod.invoke(this.mObject, holder.mHasRequest ? new Object[]{request} : new Object[0]);
                if (callback != null) {
                    IQYSResponse response4 = new IQYSResponse();
                    response4.mCode = 200;
                    response4.mMsg = new IQYSString("SUCCESS");
                    callback.onResponse(response4);
                }
            } catch (Exception e) {
                if (callback != null) {
                    Throwable exception = getTargetException(e);
                    IQYSResponse response5 = new IQYSResponse();
                    response5.mCode = IQYSConstants.RESPONSE_CODE_UNKNOWN_ERROR;
                    response5.mMsg = new IQYSString("UNKNOWN-" + exception.getMessage());
                    callback.onResponse(response5);
                }
            }
        } else {
            try {
                holder.mMethod.invoke(this.mObject, holder.mHasRequest ? new Object[]{request, callback} : new Object[]{callback});
            } catch (Exception e2) {
                if (callback != null) {
                    Throwable exception2 = getTargetException(e2);
                    IQYSResponse response6 = new IQYSResponse();
                    response6.mCode = IQYSConstants.RESPONSE_CODE_UNKNOWN_ERROR;
                    response6.mMsg = new IQYSString("UNKNOWN-" + exception2.getMessage());
                    callback.onResponse(response6);
                }
            }
        }
    }

    public IQYSResponse executeSync(IQYSRequest request) {
        MethodHolder holder = getMethodHolder(request, false);
        if (holder == null) {
            IQYSResponse response = new IQYSResponse();
            response.mCode = IQYSConstants.RESPONSE_CODE_PARAMS_ERROR;
            response.mMsg = new IQYSString("need command param");
            return response;
        } else if (holder.mMethod == null) {
            IQYSResponse response2 = new IQYSResponse();
            response2.mCode = IQYSConstants.RESPONSE_CODE_CMD_NOT_EXIT;
            response2.mMsg = new IQYSString("command is not found");
            return response2;
        } else if (holder.mSync) {
            try {
                Class<?> returnType = holder.mMethod.getReturnType();
                if (returnType == IQYSResponse.class) {
                    Object invoke = holder.mMethod.invoke(this.mObject, holder.mHasRequest ? new Object[]{request} : new Object[0]);
                    return (IQYSResponse) invoke;
                }
                if (returnType != Void.class && returnType != Void.TYPE) {
                    IQYSResponse response3 = new IQYSResponse();
                    response3.mCode = IQYSConstants.RESPONSE_CODE_UNKNOWN_ERROR;
                    response3.mMsg = new IQYSString("remote error, return type is " + returnType);
                    return response3;
                }
                holder.mMethod.invoke(this.mObject, holder.mHasRequest ? new Object[]{request} : new Object[0]);
                IQYSResponse response4 = new IQYSResponse();
                response4.mCode = 200;
                response4.mMsg = new IQYSString("SUCCESS");
                return response4;
            } catch (Exception e) {
                Throwable exception = getTargetException(e);
                IQYSResponse response5 = new IQYSResponse();
                response5.mCode = IQYSConstants.RESPONSE_CODE_UNKNOWN_ERROR;
                response5.mMsg = new IQYSString("UNKNOWN-" + exception.getMessage());
                return response5;
            }
        } else {
            IQYSResponse response6 = new IQYSResponse();
            response6.mCode = IQYSConstants.RESPONSE_CODE_CMD_NOT_EXIT;
            response6.mMsg = new IQYSString("method is async,please use executeAsync");
            return response6;
        }
    }

    private Throwable getTargetException(Throwable e) {
        Throwable exception = e;
        while (exception instanceof InvocationTargetException) {
            InvocationTargetException ie = (InvocationTargetException) exception;
            exception = ie.getTargetException() != null ? ie.getTargetException() : ie;
        }
        while (exception.getCause() != null && exception.getCause() != exception) {
            exception = exception.getCause();
        }
        return exception;
    }

    private MethodHolder getMethodHolder(IQYSRequest request, boolean isSync) {
        IQYSStringWrapper cmd;
        String command = null;
        if (request == null) {
            cmd = null;
        } else {
            try {
                cmd = request.mCmd;
            } catch (Exception e) {
            }
        }
        String cmdStr = cmd == null ? null : cmd.get();
        command = cmdStr == null ? null : Uri.parse(cmdStr).getQueryParameter("command");
        if (command == null) {
            return null;
        }
        MethodKey obtain = MethodKey.obtain();
        obtain.mCommand = command;
        obtain.mSync = isSync;
        MethodHolder methodHolder = this.mMethodMap.get(obtain);
        if (methodHolder == null) {
            if (!isSync) {
                obtain.recycle();
                return getMethodHolder(request, true);
            }
            MethodHolder methodHolder2 = MethodHolder.create(this.mObject, command);
            this.mMethodMap.put(obtain, methodHolder2);
            return methodHolder2;
        }
        obtain.recycle();
        return methodHolder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class MethodKey {
        private static final LinkedList<MethodKey> sMethodKeys = new LinkedList<>();
        String mCommand;
        boolean mSync;

        private MethodKey() {
        }

        public static MethodKey obtain() {
            synchronized (sMethodKeys) {
                if (sMethodKeys.size() > 0) {
                    return sMethodKeys.pollFirst();
                }
                return new MethodKey();
            }
        }

        public void recycle() {
            synchronized (sMethodKeys) {
                this.mCommand = null;
                this.mSync = false;
                sMethodKeys.addLast(this);
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MethodKey methodKey = (MethodKey) o;
            if (this.mSync == methodKey.mSync && TextUtils.equals(this.mCommand, methodKey.mCommand)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return hashCode(new Object[]{this.mCommand, Boolean.valueOf(this.mSync)});
        }

        private static int hashCode(Object[] a) {
            if (a == null) {
                return 0;
            }
            int length = a.length;
            int result = 1;
            for (int result2 = 0; result2 < length; result2++) {
                Object element = a[result2];
                result = (result * 31) + (element == null ? 0 : element.hashCode());
            }
            return result;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class MethodHolder {
        boolean mHasCallback;
        boolean mHasRequest;
        Method mMethod;
        boolean mSync;

        private MethodHolder() {
        }

        static MethodHolder create(Object obj, String command) {
            MethodHolder holder = new MethodHolder();
            Class<?> aClass = obj.getClass();
            Method method = getMethod(command, aClass, IQYSRequest.class, IQYSCallback.class);
            if (method != null) {
                holder.mSync = false;
                holder.mMethod = method;
                holder.mHasRequest = true;
                holder.mHasCallback = true;
                return holder;
            }
            Method method2 = getMethod(command, aClass, IQYSCallback.class);
            if (method2 != null) {
                holder.mSync = true;
                holder.mMethod = method2;
                holder.mHasRequest = false;
                holder.mHasCallback = true;
                return holder;
            }
            Method method3 = getMethod(command, aClass, IQYSRequest.class);
            if (method3 != null) {
                holder.mSync = true;
                holder.mMethod = method3;
                holder.mHasRequest = true;
                holder.mHasCallback = false;
                return holder;
            }
            Method method4 = getMethod(command, aClass, new Class[0]);
            if (method4 != null) {
                holder.mSync = true;
                holder.mMethod = method4;
                holder.mHasRequest = false;
                holder.mHasCallback = false;
                return holder;
            }
            return holder;
        }

        private static Method getMethod(String command, Class<?> aClass, Class<?>... params) {
            try {
                return aClass.getMethod(command, params);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
