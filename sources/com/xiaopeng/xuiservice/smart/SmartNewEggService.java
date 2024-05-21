package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.content.Context;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.xuimanager.XUIManager;
import com.xiaopeng.xuimanager.operation.IOperationListener;
import com.xiaopeng.xuimanager.operation.OperationManager;
import com.xiaopeng.xuimanager.operation.OperationResource;
import com.xiaopeng.xuiservice.debug.DumpDispatcher;
import com.xiaopeng.xuiservice.egg.Egg;
import com.xiaopeng.xuiservice.egg.EggLog;
import com.xiaopeng.xuiservice.egg.EggUtil;
import com.xiaopeng.xuiservice.smart.SmartNewEggService;
import com.xiaopeng.xuiservice.smart.action.Action;
import com.xiaopeng.xuiservice.smart.condition.Condition;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionIG;
import com.xiaopeng.xuiservice.utils.DateTimeFormatUtil;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes5.dex */
public class SmartNewEggService extends BaseSmartService {
    private static final String DUMP_CMD_DISABLE_LOCAL_EGG = "-disable-local-egg";
    private static final String DUMP_CMD_ENABLE_LOCAL_EGG = "-enable-local-egg";
    private static final String DUMP_CMD_STATUS = "-status";
    private static final String DUMP_CMD_SYNC = "-sync";
    private static volatile boolean LOAD_FROM_DISK = false;
    private static final String LOCAL_EGG_PATH = "/data/operation/resource/egg/local/";
    private Condition conditionIG;
    private final ScheduledExecutorService executorService;
    private final IOperationListener operationListener;
    private final ConcurrentHashMap<Egg, Action> runningEggs;
    private final ConcurrentHashMap<Egg, ScheduledFuture<?>> scheduledExpireMap;
    private final ConcurrentHashMap<Egg, ScheduledFuture<?>> scheduledStartMap;
    private final List<Egg> watchingEggs;

    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService, com.xiaopeng.xuiservice.debug.DumpDispatcher.DebugDumper
    public /* bridge */ /* synthetic */ void debugDump(PrintWriter printWriter, String[] strArr) {
        super.debugDump(printWriter, strArr);
    }

    public SmartNewEggService(Context context) {
        super(context);
        this.executorService = Executors.newScheduledThreadPool(0);
        this.watchingEggs = Collections.synchronizedList(new ArrayList());
        this.runningEggs = new ConcurrentHashMap<>();
        this.scheduledStartMap = new ConcurrentHashMap<>();
        this.scheduledExpireMap = new ConcurrentHashMap<>();
        this.operationListener = new AnonymousClass1();
        DumpDispatcher.registerDump("egg", this);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaopeng.xuiservice.smart.BaseSmartService
    public void dump(final PrintWriter pw, String[] args) {
        boolean z;
        super.dump(pw, args);
        if (args == null || args.length == 0) {
            pw.println("egg service is running.");
            printHelp(pw);
            return;
        }
        String cmd = args[0];
        char c = 65535;
        switch (cmd.hashCode()) {
            case 1499:
                if (cmd.equals("-h")) {
                    z = false;
                    break;
                }
                z = true;
                break;
            case 44757230:
                if (cmd.equals("-help")) {
                    z = true;
                    break;
                }
                z = true;
                break;
            case 45104200:
                if (cmd.equals(DUMP_CMD_SYNC)) {
                    z = true;
                    break;
                }
                z = true;
                break;
            case 390478431:
                if (cmd.equals(DUMP_CMD_STATUS)) {
                    z = true;
                    break;
                }
                z = true;
                break;
            case 1376744710:
                if (cmd.equals(DUMP_CMD_ENABLE_LOCAL_EGG)) {
                    z = true;
                    break;
                }
                z = true;
                break;
            case 2076406129:
                if (cmd.equals(DUMP_CMD_DISABLE_LOCAL_EGG)) {
                    z = true;
                    break;
                }
                z = true;
                break;
            default:
                z = true;
                break;
        }
        if (!z || z) {
            printHelp(pw);
        } else if (z || z) {
            if (BuildInfoUtils.isDebuggableVersion()) {
                int hashCode = cmd.hashCode();
                if (hashCode != 1376744710) {
                    if (hashCode == 2076406129 && cmd.equals(DUMP_CMD_DISABLE_LOCAL_EGG)) {
                        c = 1;
                    }
                } else if (cmd.equals(DUMP_CMD_ENABLE_LOCAL_EGG)) {
                    c = 0;
                }
                if (c == 0) {
                    if (!LOAD_FROM_DISK) {
                        pw.println("Egg service will load eggs from local path");
                        LOAD_FROM_DISK = true;
                        XuiWorkHandler.getInstance().post(new $$Lambda$SmartNewEggService$pk8KhbU2IjcSL4VuqvtKsG2G7I(this));
                        return;
                    }
                    return;
                } else if (c == 1 && LOAD_FROM_DISK) {
                    pw.println("Egg service will load eggs from resource server [default]");
                    LOAD_FROM_DISK = false;
                    XuiWorkHandler.getInstance().post(new $$Lambda$SmartNewEggService$pk8KhbU2IjcSL4VuqvtKsG2G7I(this));
                    return;
                } else {
                    return;
                }
            }
            pw.println("Command: " + cmd + " is not supported in release build");
        } else if (z) {
            XuiWorkHandler.getInstance().post(new $$Lambda$SmartNewEggService$pk8KhbU2IjcSL4VuqvtKsG2G7I(this));
        } else if (z) {
            pw.println("Running eggs:");
            if (this.runningEggs.isEmpty()) {
                pw.println("<EMPTY>");
            } else {
                this.runningEggs.forEach(new BiConsumer() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$i6ZFz3PVVeUt4lPLWqEIZSuH_po
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        SmartNewEggService.this.lambda$dump$0$SmartNewEggService(pw, (Egg) obj, (Action) obj2);
                    }
                });
            }
            pw.println();
            pw.println("Watching eggs:");
            if (this.watchingEggs.isEmpty()) {
                pw.println("<EMPTY>");
            } else {
                this.watchingEggs.forEach(new Consumer() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$eMYaCHxqrNtz3LvbAh4uZKyVWlg
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        SmartNewEggService.this.lambda$dump$1$SmartNewEggService(pw, (Egg) obj);
                    }
                });
            }
            pw.println();
            pw.println("ScheduledStartMap");
            if (this.scheduledStartMap.isEmpty()) {
                pw.println("<EMPTY>");
            } else {
                this.scheduledStartMap.forEach(new BiConsumer() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$vnO7rg7bW3PlJyHQ0NEINCW3-aY
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        SmartNewEggService.this.lambda$dump$2$SmartNewEggService(pw, (Egg) obj, (ScheduledFuture) obj2);
                    }
                });
            }
            pw.println();
            pw.println("scheduledExpireMap");
            if (this.scheduledExpireMap.isEmpty()) {
                pw.println("<EMPTY>");
            } else {
                this.scheduledExpireMap.forEach(new BiConsumer() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$N_hueqqTNFlYJQbMTpfkSExcw2E
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        SmartNewEggService.this.lambda$dump$3$SmartNewEggService(pw, (Egg) obj, (ScheduledFuture) obj2);
                    }
                });
            }
            pw.println();
            pw.println("Summary:");
            pw.println("Running: " + this.runningEggs.size() + ", Watching: " + this.watchingEggs.size() + ", ScheduledStart: " + this.scheduledStartMap.size() + ", ScheduledExpire: " + this.scheduledExpireMap.size());
        } else {
            pw.println("unknown command: " + cmd);
        }
    }

    public /* synthetic */ void lambda$dump$0$SmartNewEggService(PrintWriter pw, Egg egg, Action action) {
        lambda$dump$1$SmartNewEggService(pw, egg);
        pw.println("Action:");
        pw.println(action);
    }

    public /* synthetic */ void lambda$dump$2$SmartNewEggService(PrintWriter pw, Egg egg, ScheduledFuture f) {
        lambda$dump$1$SmartNewEggService(pw, egg);
    }

    public /* synthetic */ void lambda$dump$3$SmartNewEggService(PrintWriter pw, Egg egg, ScheduledFuture f) {
        lambda$dump$1$SmartNewEggService(pw, egg);
    }

    private void printHelp(PrintWriter pw) {
        pw.println("Usages:");
        pw.print("\t");
        pw.print(DUMP_CMD_ENABLE_LOCAL_EGG);
        pw.println(": Load eggs from path: /data/operation/resource/egg/local/");
        pw.print("\t");
        pw.print(DUMP_CMD_DISABLE_LOCAL_EGG);
        pw.println(": Load eggs from resource server");
        pw.print("\t");
        pw.print(DUMP_CMD_SYNC);
        pw.println(": Reload egg");
        pw.print("\t");
        pw.print(DUMP_CMD_STATUS);
        pw.println(": Print current egg status");
    }

    /* renamed from: printEgg */
    public void lambda$dump$1$SmartNewEggService(PrintWriter pw, Egg egg) {
        pw.println("Egg info:");
        pw.println(egg);
        pw.println();
        pw.println("Run record:");
        pw.print("Last run: ");
        long time = EggUtil.getLastRunInMemory(egg);
        pw.print("InMemory = " + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(time)));
        long time2 = EggUtil.getLastRunInStorage(egg);
        pw.print(", InStorage = " + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(time2)));
        pw.println();
        pw.print("Run count: ");
        pw.print("InMemory = " + EggUtil.getRunCountInMemory(egg) + ",  InStorage = " + EggUtil.getRunCountInStorage(egg));
        pw.println();
        pw.println();
        pw.println("Condition:");
        pw.println(egg.getConditionJsonObject());
        pw.println("StopCondition:");
        pw.println(egg.getStopConditionJsonObject());
        pw.println();
        pw.println("Action:");
        pw.println(egg.getActionJsonObject());
        pw.println();
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected boolean needConnectXUI() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.xuiservice.smart.SmartNewEggService$1 */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements IOperationListener {
        AnonymousClass1() {
            SmartNewEggService.this = this$0;
        }

        public void onOperationSourceAdd(int type, OperationResource resource) {
        }

        public void onOperationSourceExpire(int type, OperationResource resource) {
        }

        public void onOperationSourceDelete(int type, OperationResource resource) {
        }

        public void onRemoteSourceQuerySuccess(int type, List<OperationResource> resources) {
        }

        public void onOperationResourceSyncCompleted(int resourceType) {
            EggLog.INFO("onOperationResourceSyncCompleted, " + this);
            if (!SmartNewEggService.LOAD_FROM_DISK) {
                XuiWorkHandler xuiWorkHandler = XuiWorkHandler.getInstance();
                final SmartNewEggService smartNewEggService = SmartNewEggService.this;
                xuiWorkHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$1$YwrSkiwxAmJixTWCOo9g-yAfzuc
                    @Override // java.lang.Runnable
                    public final void run() {
                        SmartNewEggService.this.installEgg();
                    }
                });
            }
        }

        public void onOperationResourceSyncFailed(int resourceType) {
            EggLog.INFO("onOperationResourceSyncFailed, " + this);
            if (!SmartNewEggService.LOAD_FROM_DISK) {
                XuiWorkHandler xuiWorkHandler = XuiWorkHandler.getInstance();
                final SmartNewEggService smartNewEggService = SmartNewEggService.this;
                xuiWorkHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$1$PcQmtC08D4-43AIZuQRJ856GoNo
                    @Override // java.lang.Runnable
                    public final void run() {
                        SmartNewEggService.this.installEgg();
                    }
                });
            }
        }

        public void onOperationResourceSyncAborted(int resourceType) {
            EggLog.INFO("onOperationResourceSyncAborted, " + this);
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onConnectXUI() {
        super.onConnectXUI();
        EggLog.INFO("SmartNewEggService onConnectXUI");
        OperationManager operationManager = (OperationManager) XUIManager.getInstance().getService("operation");
        if (operationManager != null) {
            operationManager.registerListener(1080200, this.operationListener);
            operationManager.setAutoSync(1080200);
        }
        this.conditionIG = new ConditionIG(1);
        this.conditionIG.addConditionChangeListener(new Condition.ConditionChangeListener() { // from class: com.xiaopeng.xuiservice.smart.SmartNewEggService.2
            {
                SmartNewEggService.this = this;
            }

            @Override // com.xiaopeng.xuiservice.smart.condition.Condition.ConditionChangeListener
            public void onConditionChange(Condition condition) {
                if (condition.isMatch()) {
                    EggUtil.cleanMemoryEggRunRecord();
                }
            }
        });
        this.conditionIG.startWatch();
        XuiWorkHandler.getInstance().post(new $$Lambda$SmartNewEggService$pk8KhbU2IjcSL4VuqvtKsG2G7I(this));
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.IServiceConn
    public void onDisconnectXUI() {
        super.onDisconnectXUI();
        EggLog.INFO("SmartNewEggService onDisconnectXUI");
        Condition condition = this.conditionIG;
        if (condition != null) {
            condition.stopWatch();
            this.conditionIG = null;
        }
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
        EggLog.INFO("SmartNewEggService onInit");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
        EggLog.INFO("SmartNewEggService onRelease");
    }

    public void installEgg() {
        EggLog.INFO("######################### INSTALL EGG START #########################");
        stopWatching();
        List<Egg> eggs = getEggs();
        if (eggs == null) {
            EggLog.ERROR("Load egg failed!");
            return;
        }
        EggLog.INFO("Load eggs done, count = " + eggs.size());
        int scheduledStart = 0;
        int started = 0;
        int expired = 0;
        for (final Egg egg : eggs) {
            EggLog.INFO(">>> try start or schedule egg, id = " + egg.getId());
            if (egg.getStartTime() > System.currentTimeMillis()) {
                EggLog.INFO("Scheduled start watching at " + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(egg.getStartTime())));
                ScheduledFuture<?> scheduledFuture = this.executorService.schedule(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$RZswEY6L64EtVMSGz9k2muDN81w
                    @Override // java.lang.Runnable
                    public final void run() {
                        SmartNewEggService.this.lambda$installEgg$4$SmartNewEggService(egg);
                    }
                }, egg.getStartTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                this.scheduledStartMap.put(egg, scheduledFuture);
                scheduledStart++;
            } else if (egg.getExpireTime() > 0 && egg.getExpireTime() < System.currentTimeMillis()) {
                EggLog.INFO("Expired at " + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(egg.getExpireTime())));
                expired++;
            } else {
                if (egg.getExpireTime() > System.currentTimeMillis()) {
                    ScheduledFuture<?> scheduledFuture2 = this.executorService.schedule(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$ZAVOipNG9mUd7bUwr_kZhrRyK7w
                        @Override // java.lang.Runnable
                        public final void run() {
                            SmartNewEggService.this.lambda$installEgg$5$SmartNewEggService(egg);
                        }
                    }, egg.getExpireTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    this.scheduledExpireMap.put(egg, scheduledFuture2);
                }
                startEgg(egg);
                started++;
            }
        }
        EggLog.INFO("######################### INSTALL EGG END #########################");
        EggLog.INFO(String.format(Locale.getDefault(), "Install egg completed. Total = %d (Started = %d, Scheduled = %d, Expired = %d)", Integer.valueOf(eggs.size()), Integer.valueOf(started), Integer.valueOf(scheduledStart), Integer.valueOf(expired)));
    }

    public /* synthetic */ void lambda$installEgg$4$SmartNewEggService(Egg egg) {
        EggLog.INFO("Scheduled start egg, id = " + egg.getId());
        this.scheduledStartMap.remove(egg);
        startEgg(egg);
    }

    public /* synthetic */ void lambda$installEgg$5$SmartNewEggService(Egg egg) {
        EggLog.INFO("Scheduled expire egg, id = " + egg.getId());
        this.scheduledExpireMap.remove(egg);
        this.watchingEggs.remove(egg);
        stopEgg(egg);
    }

    private void stopWatching() {
        for (Egg egg : this.scheduledStartMap.keySet()) {
            ScheduledFuture<?> scheduledFuture = this.scheduledStartMap.get(egg);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
                EggLog.INFO("Stop scheduled start egg, id = " + egg.getId() + ", startTime = " + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(egg.getStartTime())));
            }
        }
        this.scheduledStartMap.clear();
        for (Egg egg2 : this.watchingEggs) {
            stopEgg(egg2);
        }
        this.watchingEggs.clear();
        for (Egg egg3 : this.scheduledExpireMap.keySet()) {
            ScheduledFuture<?> scheduledFuture2 = this.scheduledExpireMap.get(egg3);
            if (scheduledFuture2 != null) {
                scheduledFuture2.cancel(false);
                EggLog.INFO("Stop scheduled expire egg, id = " + egg3.getId() + ", expireTime = " + DateTimeFormatUtil.format(DateTimeFormatUtil.FULL_DATE_TIME_FORMAT, new Date(egg3.getExpireTime())));
            }
        }
        this.scheduledExpireMap.clear();
    }

    private List<Egg> getEggs() {
        if (LOAD_FROM_DISK) {
            EggLog.INFO("Load eggs from path: /data/operation/resource/egg/local/");
            File eggDir = new File(LOCAL_EGG_PATH);
            File[] files = eggDir.listFiles(new FileFilter() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$k1LMnpJLlrYtcSsQvSbPW-daMgg
                @Override // java.io.FileFilter
                public final boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            if (files == null) {
                return null;
            }
            List<Egg> eggs = (List) Arrays.stream(files).map(new Function() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$3xy2pEpZCLpbc4VBcI41SgPnG0c
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    Egg parseFromFile;
                    parseFromFile = EggUtil.parseFromFile(((File) obj).getAbsolutePath());
                    return parseFromFile;
                }
            }).filter(new Predicate() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$A15tM0Y45EtTreK0JqVLVJGPur8
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull((Egg) obj);
                }
            }).collect(Collectors.toList());
            return eggs;
        }
        List<OperationResource> resources = getNormalResources();
        List<Egg> eggs2 = (List) resources.stream().map(new Function() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$ICKR2dLUbLPQN1kGWjOomH05Lss
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Egg parseFromFile;
                parseFromFile = EggUtil.parseFromFile(((OperationResource) obj).getTargetPath());
                return parseFromFile;
            }
        }).filter(new Predicate() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$A15tM0Y45EtTreK0JqVLVJGPur8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((Egg) obj);
            }
        }).collect(Collectors.toList());
        return eggs2;
    }

    private List<OperationResource> getNormalResources() {
        OperationManager operationManager = (OperationManager) XUIManager.getInstance().getService("operation");
        if (operationManager != null) {
            return (List) operationManager.getResourceList(1080200).stream().filter(new Predicate() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$k_suJ2329k4Sv9M-FONzgYfnaqQ
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return SmartNewEggService.lambda$getNormalResources$8((OperationResource) obj);
                }
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static /* synthetic */ boolean lambda$getNormalResources$8(OperationResource resource) {
        return resource.getStatus() == 0;
    }

    private void startEgg(final Egg egg) {
        egg.createCondition();
        Condition condition = egg.getCondition();
        if (condition == null) {
            EggLog.ERROR("Start egg error, id = " + egg.getId() + " no condition");
            return;
        }
        EggLog.INFO("Start egg: " + egg);
        condition.addConditionChangeListener(new Condition.ConditionChangeListener() { // from class: com.xiaopeng.xuiservice.smart.SmartNewEggService.3
            {
                SmartNewEggService.this = this;
            }

            @Override // com.xiaopeng.xuiservice.smart.condition.Condition.ConditionChangeListener
            public void onConditionChange(Condition condition2) {
                if (condition2.isMatch()) {
                    SmartNewEggService.this.startAction(egg);
                }
            }
        });
        condition.startWatch();
        this.watchingEggs.add(egg);
    }

    private void stopEgg(Egg egg) {
        Condition condition = egg.getCondition();
        if (condition != null) {
            condition.stopWatch();
        }
        EggLog.INFO("Stop running egg, id = " + egg.getId());
    }

    public void startAction(Egg egg) {
        Action action = this.runningEggs.get(egg);
        if (action != null && !action.isDone()) {
            EggLog.INFO("Egg's action is running, ignore. egg id = " + egg.getId());
            return;
        }
        final Action action2 = egg.createAction();
        if (action2 == null) {
            EggLog.ERROR("condition matched, but not action, egg = " + egg.getId());
            return;
        }
        action2.addActionChangeListener(new AnonymousClass4(egg));
        this.runningEggs.put(egg, action2);
        XuiWorkHandler xuiWorkHandler = XuiWorkHandler.getInstance();
        Objects.requireNonNull(action2);
        xuiWorkHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$ugmuCTpo5TbTSg7WGALGuEX7kOE
            @Override // java.lang.Runnable
            public final void run() {
                Action.this.start();
            }
        });
        EggUtil.recordLastRun(egg, System.currentTimeMillis());
        EggUtil.increaseRunCount(egg);
    }

    /* renamed from: com.xiaopeng.xuiservice.smart.SmartNewEggService$4 */
    /* loaded from: classes5.dex */
    public class AnonymousClass4 implements Action.ActionChangeListener {
        Condition.ConditionChangeListener stopConditionListener;
        final /* synthetic */ Egg val$egg;

        AnonymousClass4(Egg egg) {
            SmartNewEggService.this = this$0;
            this.val$egg = egg;
        }

        @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
        public void onActionStarted(final Action action) {
            this.val$egg.createStopCondition();
            Condition stopCondition = this.val$egg.getStopCondition();
            if (stopCondition == null) {
                return;
            }
            EggLog.INFO("Start watch stop condition for egg id = " + this.val$egg.getId());
            final Egg egg = this.val$egg;
            this.stopConditionListener = new Condition.ConditionChangeListener() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$SmartNewEggService$4$3VJCiBUBtL4hXoCB_iRW_BiaWko
                @Override // com.xiaopeng.xuiservice.smart.condition.Condition.ConditionChangeListener
                public final void onConditionChange(Condition condition) {
                    SmartNewEggService.AnonymousClass4.lambda$onActionStarted$0(Egg.this, action, condition);
                }
            };
            stopCondition.addConditionChangeListener(this.stopConditionListener);
            stopCondition.startWatch();
        }

        public static /* synthetic */ void lambda$onActionStarted$0(Egg egg, final Action action, Condition condition) {
            if (condition.isMatch()) {
                EggLog.INFO("Stop action caused by stopCondition is matched, egg id = " + egg.getId());
                XuiWorkHandler xuiWorkHandler = XuiWorkHandler.getInstance();
                Objects.requireNonNull(action);
                xuiWorkHandler.post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.-$$Lambda$lo5LiohUZmmch6zgPTwAu99F1XY
                    @Override // java.lang.Runnable
                    public final void run() {
                        Action.this.stop();
                    }
                });
            }
        }

        @Override // com.xiaopeng.xuiservice.smart.action.Action.ActionChangeListener
        public void onActionDone(Action action) {
            Condition.ConditionChangeListener conditionChangeListener;
            EggLog.INFO("egg action is DONE, egg id = " + this.val$egg.getId());
            SmartNewEggService.this.runningEggs.remove(this.val$egg);
            Condition stopCondition = this.val$egg.getStopCondition();
            if (stopCondition == null || (conditionChangeListener = this.stopConditionListener) == null) {
                return;
            }
            stopCondition.removeConditionChangeListener(conditionChangeListener);
            stopCondition.stopWatch();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class InstanceHolder {
        private static final SmartNewEggService sService = new SmartNewEggService(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public static SmartNewEggService getInstance() {
        return InstanceHolder.sService;
    }
}
