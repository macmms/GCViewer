package com.tagtraum.perf.gcviewer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of an event type
 */
public class Type implements Serializable {
    private String name;
    private Generation generation;
    private Concurrency concurrency;
    /** pattern this event has in the logfile */
    private GcPattern pattern;
    private CollectionType collectionType;
    private static final Map<String, Type> TYPE_MAP = new HashMap<String, Type>();

    private Type(String name, Generation generation) {
        this(name, generation, Concurrency.SERIAL);
    }

    private Type(String name, Generation generation, Concurrency concurrency) {
        this(name, generation, concurrency, GcPattern.GC_MEMORY_PAUSE);
    }

    private Type(String name, Generation generation, Concurrency concurrency, GcPattern pattern) {
        this(name, generation, concurrency, pattern, CollectionType.COLLECTION);
    }

    private Type(String name, Generation generation, Concurrency concurrency, GcPattern pattern, CollectionType collectionType) {
        this.name = name.intern();
        this.generation = generation;
        this.concurrency = concurrency;
        this.pattern = pattern;
        this.collectionType = collectionType;

        TYPE_MAP.put(this.name, this);
    }

    public static Type lookup(String type) {
        return TYPE_MAP.get(type.trim());
    }

    public String getName() {
        return name;
    }

    public Generation getGeneration() {
        return generation;
    }

    public Concurrency getConcurrency() {
        return concurrency;
    }

    public GcPattern getPattern() {
        return pattern;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    @Override
    public String toString() {
        return name;
    }

    public static final Type UNDEFINED = new Type("undefined", Generation.YOUNG);

    // TODO: is jrockit GC really of type Generation.ALL or rather Generation.TENURED ?
    public static final Type JROCKIT_GC = new Type("jrockit.GC", Generation.TENURED);
    public static final Type JROCKIT_NURSERY_GC = new Type("jrockit.Nursery GC", Generation.YOUNG);
    public static final Type JROCKIT_PARALLEL_NURSERY_GC = new Type("jrockit.parallel nursery GC", Generation.YOUNG);

    public static final Type JROCKIT_16_OLD_GC = new Type("jrockit.OC", Generation.TENURED);
    public static final Type JROCKIT_16_YOUNG_GC = new Type("jrockit.YC", Generation.YOUNG);
    public static final Type JROCKIT_16_PARALLEL_NURSERY_GC = new Type("jrockit.parallel nursery GC", Generation.YOUNG);

    // Sun JDK 1.5
    public static final Type SCAVENGE_BEFORE_REMARK = new Type("Scavenge-Before-Remark", Generation.ALL);

    public static final Type FULL_GC = new Type("Full GC", Generation.ALL);
    public static final Type GC = new Type("GC", Generation.YOUNG);
    public static final Type DEF_NEW = new Type("DefNew", Generation.YOUNG, Concurrency.SERIAL); // single threaded
    public static final Type PAR_NEW = new Type("ParNew", Generation.YOUNG); // parallel
    public static final Type ASPAR_NEW = new Type("ASParNew", Generation.YOUNG); // parallel (CMS AdaptiveSizePolicy)
    public static final Type PAR_OLD_GEN = new Type("ParOldGen", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY);
    public static final Type PS_YOUNG_GEN = new Type("PSYoungGen", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY);
    public static final Type PS_OLD_GEN = new Type("PSOldGen", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY);
    public static final Type PS_PERM_GEN = new Type("PSPermGen", Generation.PERM, Concurrency.SERIAL, GcPattern.GC_MEMORY);
    public static final Type TENURED = new Type("Tenured", Generation.TENURED);
    public static final Type INC_GC = new Type("Inc GC", Generation.YOUNG);
    public static final Type TRAIN = new Type("Train", Generation.TENURED);
    public static final Type TRAIN_MSC = new Type("Train MSC", Generation.TENURED);
    public static final Type PERM = new Type("Perm", Generation.PERM, Concurrency.SERIAL, GcPattern.GC_MEMORY);
    // since about java 7_u45 these have a time stamp prepended
    public static final Type APPLICATION_STOPPED_TIME = new Type("Total time for which application threads were stopped", Generation.OTHER, Concurrency.SERIAL, GcPattern.GC_PAUSE, CollectionType.VM_OPERATION);
    // java 8: perm gen is moved to metaspace
    public static final Type Metaspace = new Type("Metaspace", Generation.PERM, Concurrency.SERIAL, GcPattern.GC_MEMORY);

    // CMS types
    public static final Type CMS = new Type("CMS", Generation.TENURED);
    public static final Type CMS_PERM = new Type("CMS Perm", Generation.PERM, Concurrency.SERIAL, GcPattern.GC_MEMORY);

    // Parnew (promotion failed)
     public static final Type PAR_NEW_PROMOTION_FAILED = new Type("ParNew (promotion failed)", Generation.YOUNG, Concurrency.SERIAL);

    // CMS (concurrent mode failure / interrupted)
     public static final Type CMS_CMF = new Type("CMS (concurrent mode failure)", Generation.TENURED, Concurrency.SERIAL);
     public static final Type CMS_CMI = new Type("CMS (concurrent mode interrupted)", Generation.TENURED, Concurrency.SERIAL);

    // CMS (Concurrent Mark Sweep) Event Types
    public static final Type CMS_CONCURRENT_MARK_START = new Type("CMS-concurrent-mark-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type CMS_CONCURRENT_MARK = new Type("CMS-concurrent-mark", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_PRECLEAN_START = new Type("CMS-concurrent-preclean-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type CMS_CONCURRENT_PRECLEAN = new Type("CMS-concurrent-preclean", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_SWEEP_START = new Type("CMS-concurrent-sweep-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type CMS_CONCURRENT_SWEEP = new Type("CMS-concurrent-sweep", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_RESET_START = new Type("CMS-concurrent-reset-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type CMS_CONCURRENT_RESET = new Type("CMS-concurrent-reset", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_ABORTABLE_PRECLEAN_START = new Type("CMS-concurrent-abortable-preclean-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type CMS_CONCURRENT_ABORTABLE_PRECLEAN = new Type("CMS-concurrent-abortable-preclean", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);

    public static final Type CMS_INITIAL_MARK = new Type("CMS-initial-mark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY, CollectionType.CONCURRENCY_HELPER);
    public static final Type CMS_REMARK = new Type("CMS-remark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY, CollectionType.CONCURRENCY_HELPER);

    // CMS (Concurrent Mark Sweep) AdaptiveSizePolicy Event Types
    public static final Type ASCMS = new Type("ASCMS", Generation.TENURED);

    // Parnew (promotion failed) AdaptiveSizePolicy
     public static final Type ASPAR_NEW_PROMOTION_FAILED = new Type("ASParNew (promotion failed)", Generation.YOUNG, Concurrency.SERIAL);

    // CMS (concurrent mode failure / interrupted) AdaptiveSizePolicy
    public static final Type ASCMS_CMF = new Type("ASCMS (concurrent mode failure)", Generation.TENURED, Concurrency.SERIAL);
    public static final Type ASCMS_CMI = new Type("ASCMS (concurrent mode interrupted)", Generation.TENURED, Concurrency.SERIAL);

    public static final Type ASCMS_CONCURRENT_MARK_START = new Type("ASCMS-concurrent-mark-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_MARK = new Type("ASCMS-concurrent-mark", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_PRECLEAN_START = new Type("ASCMS-concurrent-preclean-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_PRECLEAN = new Type("ASCMS-concurrent-preclean", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_SWEEP_START = new Type("ASCMS-concurrent-sweep-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_SWEEP = new Type("ASCMS-concurrent-sweep", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_RESET_START = new Type("ASCMS-concurrent-reset-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_RESET = new Type("ASCMS-concurrent-reset", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_ABORTABLE_PRECLEAN_START = new Type("ASCMS-concurrent-abortable-preclean-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_ABORTABLE_PRECLEAN = new Type("ASCMS-concurrent-abortable-preclean", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE_DURATION);

    public static final Type ASCMS_INITIAL_MARK = new Type("ASCMS-initial-mark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE, CollectionType.CONCURRENCY_HELPER);
    public static final Type ASCMS_REMARK = new Type("ASCMS-remark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY, CollectionType.CONCURRENCY_HELPER);

    // only young collection
    public static final Type G1_YOUNG = new Type("GC pause (young)", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_MARK_STACK_FULL = new Type("GC pause (young)Mark stack is full.", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_TO_SPACE_OVERFLOW = new Type("GC pause (young) (to-space overflow)", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    // java 7 (>u25) / 8 renamed "to-space overflow" to "to-space exhausted"
    public static final Type G1_YOUNG_TO_SPACE_EXHAUSTED = new Type("GC pause (young) (to-space exhausted)", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    // partially young collection (
    public static final Type G1_PARTIAL = new Type("GC pause (partial)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_PARTIAL_TO_SPACE_OVERFLOW = new Type("GC pause (partial) (to-space overflow)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    // mixed collection (might have replaced "partial" collection in jdk1.7.0_u5)
    public static final Type G1_MIXED = new Type("GC pause (mixed)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_MIXED_TO_SPACE_OVERFLOW = new Type("GC pause (mixed) (to-space overflow)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_MIXED_TO_SPACE_EXHAUSTED = new Type("GC pause (mixed) (to-space exhausted)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);

    // TODO: Generation: young and tenured!
    public static final Type G1_YOUNG_INITIAL_MARK = new Type("GC pause (young) (initial-mark)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_INITIAL_MARK_TO_SPACE_OVERFLOW = new Type("GC pause (young) (to-space overflow) (initial-mark)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    // The following two Types are basically the same but in a different order. In JDK 6 the order was defined, no longer the case with JDK 7 (see: https://github.com/chewiebug/GCViewer/issues/100)
    public static final Type G1_YOUNG_INITIAL_MARK_TO_SPACE_EXHAUSTED = new Type("GC pause (young) (initial-mark) (to-space exhausted)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_TO_SPACE_EXHAUSTED_INITIAL_MARK = new Type("GC pause (young) (to-space exhausted) (initial-mark)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_PARTIAL_INITIAL_MARK = new Type("GC pause (partial) (initial-mark)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_PARTIAL_INITIAL_MARK_TO_SPACE_OVERFLOW = new Type("GC pause (partial) (to-space overflow) (initial-mark)", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_REMARK = new Type("GC remark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE, CollectionType.CONCURRENCY_HELPER);
    // Java 7; detail event inside G1_REMARK
    public static final Type G1_GC_REFPROC = new Type("GC ref-proc", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE, CollectionType.CONCURRENCY_HELPER);
    public static final Type G1_CLEANUP = new Type("GC cleanup", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE, CollectionType.CONCURRENCY_HELPER);
    // Java 7_u2; detailed info in all detailed events
    public static final Type G1_EDEN = new Type("Eden", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);

    // G1 concurrent types
    public static final Type G1_CONCURRENT_ROOT_REGION_SCAN_START = new Type("GC concurrent-root-region-scan-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type G1_CONCURRENT_ROOT_REGION_SCAN_END = new Type("GC concurrent-root-region-scan-end", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type G1_CONCURRENT_MARK_START = new Type("GC concurrent-mark-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type G1_CONCURRENT_MARK_END = new Type("GC concurrent-mark-end", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type G1_CONCURRENT_MARK_ABORT = new Type("GC concurrent-mark-abort", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type G1_CONCURRENT_MARK_RESET_FOR_OVERFLOW = new Type("GC concurrent-mark-reset-for-overflow", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type G1_CONCURRENT_COUNT_START = new Type("GC concurrent-count-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type G1_CONCURRENT_COUNT_END = new Type("GC concurrent-count-end", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type G1_CONCURRENT_CLEANUP_START = new Type("GC concurrent-cleanup-start", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC);
    public static final Type G1_CONCURRENT_CLEANUP_END = new Type("GC concurrent-cleanup-end", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);

    // unified jvm logging generic event types
    public static final Type UJL_PAUSE_YOUNG = new Type("Pause Young", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_PAUSE_FULL = new Type("Pause Full", Generation.ALL, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);

    // unified jvm logging cms / g1 event types
    public static final Type UJL_PAUSE_INITIAL_MARK = new Type("Pause Initial Mark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE, CollectionType.CONCURRENCY_HELPER);
    public static final Type UJL_PAUSE_REMARK = new Type("Pause Remark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE, CollectionType.CONCURRENCY_HELPER);

    // unified jvm logging cms event types
    public static final Type UJL_CMS_CONCURRENT_MARK = new Type("Concurrent Mark", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_PRECLEAN = new Type("Concurrent Preclean", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_ABORTABLE_PRECLEAN = new Type("Concurrent Abortable Preclean", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_SWEEP = new Type("Concurrent Sweep", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_RESET = new Type("Concurrent Reset", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_OLD = new Type("Old", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_MEMORY);

    // unified jvm logging g1 event types
    public static final Type UJL_G1_PAUSE_MIXED = new Type("Pause Mixed", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_G1_TO_SPACE_EXHAUSTED = new Type("To-space exhausted", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC);
    public static final Type UJL_G1_CONCURRENT_CYCLE = new Type("Concurrent Cycle", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_G1_PAUSE_CLEANUP = new Type("Pause Cleanup", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE, CollectionType.CONCURRENCY_HELPER);
    public static final Type UJL_G1_EDEN = new Type("Eden regions", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_REGION);
    public static final Type UJL_G1_SURVIVOR = new Type("Survivor regions", Generation.YOUNG, Concurrency.SERIAL, GcPattern.GC_REGION);
    public static final Type UJL_G1_OLD = new Type("Old regions", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_REGION);
    public static final Type UJL_G1_HUMongous = new Type("Humongous regions", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_REGION);

    // unified jvm logging shenandoah event types
    public static final Type UJL_SHEN_INIT_MARK = new Type("Pause Init Mark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE);
    public static final Type UJL_SHEN_FINAL_MARK = new Type("Pause Final Mark", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_INIT_UPDATE_REFS = new Type("Pause Init Update Refs", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE);
    public static final Type UJL_SHEN_FINAL_UPDATE_REFS = new Type("Pause Final Update Refs", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_MARK = new Type("Concurrent marking", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_EVAC = new Type("Concurrent evacuation", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CANCEL_CONC_MARK = new Type("Cancel concurrent mark", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_RESET_BITMAPS = new Type("Concurrent reset bitmaps", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_UPDATE_REFS = new Type("Concurrent update references", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_PRECLEANING = new Type("Concurrent precleaning", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_MEMORY_PAUSE);

    // unified jvm logging ZGC event types
    public static final Type UJL_ZGC_GARBAGE_COLLECTION = new Type("Garbage Collection", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_MEMORY_PERCENTAGE);
    public static final Type UJL_ZGC_PAUSE_MARK_START = new Type("Pause Mark Start", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_PAUSE_MARK_END = new Type("Pause Mark End", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_PAUSE_RELOCATE_START = new Type("Pause Relocate Start", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_MARK = new Type("Concurrent Mark", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_NONREF = new Type("Concurrent Process Non-Strong References", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_RESET_RELOC_SET = new Type("Concurrent Reset Relocation Set", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_DETATCHED_PAGES = new Type("Concurrent Destroy Detached Pages", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_SELECT_RELOC_SET = new Type("Concurrent Select Relocation Set", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_PREPARE_RELOC_SET = new Type("Concurrent Prepare Relocation Set", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_RELOCATE = new Type("Concurrent Relocate", Generation.TENURED, Concurrency.CONCURRENT, GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_HEAP_CAPACITY = new Type("Capacity", Generation.TENURED, Concurrency.SERIAL, GcPattern.GC_HEAP_MEMORY_PERCENTAGE);

    // IBM Types
    // TODO: are scavenge always young only??
    public static final Type IBM_AF = new Type("af", Generation.YOUNG);
    public static final Type IBM_SYS = new Type("sys explicit", Generation.ALL);
    public static final Type IBM_AF_SCAVENGE = new Type("af scavenge", Generation.YOUNG);
    public static final Type IBM_AF_GLOBAL = new Type("af global", Generation.TENURED);
    public static final Type IBM_SYS_GLOBAL = new Type("sys global", Generation.ALL);
    public static final Type IBM_SYS_EXPLICIT_GLOBAL = new Type("sys explicit global", Generation.ALL);
    public static final Type IBM_SCAVENGE = new Type("scavenge", Generation.YOUNG, Concurrency.SERIAL);
    public static final Type IBM_GLOBAL = new Type("global", Generation.ALL, Concurrency.SERIAL);
    public static final Type IBM_NURSERY = new Type("nursery", Generation.YOUNG);
    public static final Type IBM_TENURE = new Type("tenure", Generation.TENURED);

    public static final Type IBM_CONCURRENT_COLLECTION_START = new Type("concurrent-collection-start", Generation.ALL, Concurrency.CONCURRENT);
}
