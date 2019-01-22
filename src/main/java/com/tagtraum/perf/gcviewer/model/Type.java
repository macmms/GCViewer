package com.tagtraum.perf.gcviewer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of an event type
 */
public class Type implements Serializable {
    private String name;
    private AbstractGCEvent.Generation generation;
    private AbstractGCEvent.Concurrency concurrency;
    /** pattern this event has in the logfile */
    private AbstractGCEvent.GcPattern pattern;
    private AbstractGCEvent.CollectionType collectionType;
    private static final Map<String, Type> TYPE_MAP = new HashMap<String, Type>();

    Type(String name, AbstractGCEvent.Generation generation) {
        this(name, generation, AbstractGCEvent.Concurrency.SERIAL);
    }

    Type(String name, AbstractGCEvent.Generation generation, AbstractGCEvent.Concurrency concurrency) {
        this(name, generation, concurrency, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    }

    Type(String name, AbstractGCEvent.Generation generation, AbstractGCEvent.Concurrency concurrency, AbstractGCEvent.GcPattern pattern) {
        this(name, generation, concurrency, pattern, AbstractGCEvent.CollectionType.COLLECTION);
    }

    Type(String name, AbstractGCEvent.Generation generation, AbstractGCEvent.Concurrency concurrency, AbstractGCEvent.GcPattern pattern, AbstractGCEvent.CollectionType collectionType) {
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

    public AbstractGCEvent.Generation getGeneration() {
        return generation;
    }

    public AbstractGCEvent.Concurrency getConcurrency() {
        return concurrency;
    }

    public AbstractGCEvent.GcPattern getPattern() {
        return pattern;
    }

    public AbstractGCEvent.CollectionType getCollectionType() {
        return collectionType;
    }

    @Override
    public String toString() {
        return name;
    }

    public static final Type UNDEFINED = new Type("undefined", AbstractGCEvent.Generation.YOUNG);

    // TODO: is jrockit GC really of type Generation.ALL or rather Generation.TENURED ?
    public static final Type JROCKIT_GC = new Type("jrockit.GC", AbstractGCEvent.Generation.TENURED);
    public static final Type JROCKIT_NURSERY_GC = new Type("jrockit.Nursery GC", AbstractGCEvent.Generation.YOUNG);
    public static final Type JROCKIT_PARALLEL_NURSERY_GC = new Type("jrockit.parallel nursery GC", AbstractGCEvent.Generation.YOUNG);

    public static final Type JROCKIT_16_OLD_GC = new Type("jrockit.OC", AbstractGCEvent.Generation.TENURED);
    public static final Type JROCKIT_16_YOUNG_GC = new Type("jrockit.YC", AbstractGCEvent.Generation.YOUNG);
    public static final Type JROCKIT_16_PARALLEL_NURSERY_GC = new Type("jrockit.parallel nursery GC", AbstractGCEvent.Generation.YOUNG);

    // Sun JDK 1.5
    public static final Type SCAVENGE_BEFORE_REMARK = new Type("Scavenge-Before-Remark", AbstractGCEvent.Generation.ALL);

    public static final Type FULL_GC = new Type("Full GC", AbstractGCEvent.Generation.ALL);
    public static final Type GC = new Type("GC", AbstractGCEvent.Generation.YOUNG);
    public static final Type DEF_NEW = new Type("DefNew", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL); // single threaded
    public static final Type PAR_NEW = new Type("ParNew", AbstractGCEvent.Generation.YOUNG); // parallel
    public static final Type ASPAR_NEW = new Type("ASParNew", AbstractGCEvent.Generation.YOUNG); // parallel (CMS AdaptiveSizePolicy)
    public static final Type PAR_OLD_GEN = new Type("ParOldGen", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);
    public static final Type PS_YOUNG_GEN = new Type("PSYoungGen", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);
    public static final Type PS_OLD_GEN = new Type("PSOldGen", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);
    public static final Type PS_PERM_GEN = new Type("PSPermGen", AbstractGCEvent.Generation.PERM, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);
    public static final Type TENURED = new Type("Tenured", AbstractGCEvent.Generation.TENURED);
    public static final Type INC_GC = new Type("Inc GC", AbstractGCEvent.Generation.YOUNG);
    public static final Type TRAIN = new Type("Train", AbstractGCEvent.Generation.TENURED);
    public static final Type TRAIN_MSC = new Type("Train MSC", AbstractGCEvent.Generation.TENURED);
    public static final Type PERM = new Type("Perm", AbstractGCEvent.Generation.PERM, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);
    // since about java 7_u45 these have a time stamp prepended
    public static final Type APPLICATION_STOPPED_TIME = new Type("Total time for which application threads were stopped", AbstractGCEvent.Generation.OTHER, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE, AbstractGCEvent.CollectionType.VM_OPERATION);
    // java 8: perm gen is moved to metaspace
    public static final Type Metaspace = new Type("Metaspace", AbstractGCEvent.Generation.PERM, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);

    // CMS types
    public static final Type CMS = new Type("CMS", AbstractGCEvent.Generation.TENURED);
    public static final Type CMS_PERM = new Type("CMS Perm", AbstractGCEvent.Generation.PERM, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY);

    // Parnew (promotion failed)
     public static final Type PAR_NEW_PROMOTION_FAILED = new Type("ParNew (promotion failed)", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL);

    // CMS (concurrent mode failure / interrupted)
     public static final Type CMS_CMF = new Type("CMS (concurrent mode failure)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL);
     public static final Type CMS_CMI = new Type("CMS (concurrent mode interrupted)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL);

    // CMS (Concurrent Mark Sweep) Event Types
    public static final Type CMS_CONCURRENT_MARK_START = new Type("CMS-concurrent-mark-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type CMS_CONCURRENT_MARK = new Type("CMS-concurrent-mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_PRECLEAN_START = new Type("CMS-concurrent-preclean-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type CMS_CONCURRENT_PRECLEAN = new Type("CMS-concurrent-preclean", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_SWEEP_START = new Type("CMS-concurrent-sweep-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type CMS_CONCURRENT_SWEEP = new Type("CMS-concurrent-sweep", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_RESET_START = new Type("CMS-concurrent-reset-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type CMS_CONCURRENT_RESET = new Type("CMS-concurrent-reset", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type CMS_CONCURRENT_ABORTABLE_PRECLEAN_START = new Type("CMS-concurrent-abortable-preclean-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type CMS_CONCURRENT_ABORTABLE_PRECLEAN = new Type("CMS-concurrent-abortable-preclean", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);

    public static final Type CMS_INITIAL_MARK = new Type("CMS-initial-mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    public static final Type CMS_REMARK = new Type("CMS-remark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);

    // CMS (Concurrent Mark Sweep) AdaptiveSizePolicy Event Types
    public static final Type ASCMS = new Type("ASCMS", AbstractGCEvent.Generation.TENURED);

    // Parnew (promotion failed) AdaptiveSizePolicy
     public static final Type ASPAR_NEW_PROMOTION_FAILED = new Type("ASParNew (promotion failed)", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL);

    // CMS (concurrent mode failure / interrupted) AdaptiveSizePolicy
    public static final Type ASCMS_CMF = new Type("ASCMS (concurrent mode failure)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL);
    public static final Type ASCMS_CMI = new Type("ASCMS (concurrent mode interrupted)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL);

    public static final Type ASCMS_CONCURRENT_MARK_START = new Type("ASCMS-concurrent-mark-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_MARK = new Type("ASCMS-concurrent-mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_PRECLEAN_START = new Type("ASCMS-concurrent-preclean-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_PRECLEAN = new Type("ASCMS-concurrent-preclean", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_SWEEP_START = new Type("ASCMS-concurrent-sweep-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_SWEEP = new Type("ASCMS-concurrent-sweep", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_RESET_START = new Type("ASCMS-concurrent-reset-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_RESET = new Type("ASCMS-concurrent-reset", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);
    public static final Type ASCMS_CONCURRENT_ABORTABLE_PRECLEAN_START = new Type("ASCMS-concurrent-abortable-preclean-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type ASCMS_CONCURRENT_ABORTABLE_PRECLEAN = new Type("ASCMS-concurrent-abortable-preclean", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE_DURATION);

    public static final Type ASCMS_INITIAL_MARK = new Type("ASCMS-initial-mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    public static final Type ASCMS_REMARK = new Type("ASCMS-remark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);

    // only young collection
    public static final Type G1_YOUNG = new Type("GC pause (young)", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_MARK_STACK_FULL = new Type("GC pause (young)Mark stack is full.", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_TO_SPACE_OVERFLOW = new Type("GC pause (young) (to-space overflow)", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    // java 7 (>u25) / 8 renamed "to-space overflow" to "to-space exhausted"
    public static final Type G1_YOUNG_TO_SPACE_EXHAUSTED = new Type("GC pause (young) (to-space exhausted)", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    // partially young collection (
    public static final Type G1_PARTIAL = new Type("GC pause (partial)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_PARTIAL_TO_SPACE_OVERFLOW = new Type("GC pause (partial) (to-space overflow)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    // mixed collection (might have replaced "partial" collection in jdk1.7.0_u5)
    public static final Type G1_MIXED = new Type("GC pause (mixed)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_MIXED_TO_SPACE_OVERFLOW = new Type("GC pause (mixed) (to-space overflow)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_MIXED_TO_SPACE_EXHAUSTED = new Type("GC pause (mixed) (to-space exhausted)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);

    // TODO: Generation: young and tenured!
    public static final Type G1_YOUNG_INITIAL_MARK = new Type("GC pause (young) (initial-mark)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_INITIAL_MARK_TO_SPACE_OVERFLOW = new Type("GC pause (young) (to-space overflow) (initial-mark)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    // The following two Types are basically the same but in a different order. In JDK 6 the order was defined, no longer the case with JDK 7 (see: https://github.com/chewiebug/GCViewer/issues/100)
    public static final Type G1_YOUNG_INITIAL_MARK_TO_SPACE_EXHAUSTED = new Type("GC pause (young) (initial-mark) (to-space exhausted)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_YOUNG_TO_SPACE_EXHAUSTED_INITIAL_MARK = new Type("GC pause (young) (to-space exhausted) (initial-mark)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_PARTIAL_INITIAL_MARK = new Type("GC pause (partial) (initial-mark)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_PARTIAL_INITIAL_MARK_TO_SPACE_OVERFLOW = new Type("GC pause (partial) (to-space overflow) (initial-mark)", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type G1_REMARK = new Type("GC remark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    // Java 7; detail event inside G1_REMARK
    public static final Type G1_GC_REFPROC = new Type("GC ref-proc", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    public static final Type G1_CLEANUP = new Type("GC cleanup", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    // Java 7_u2; detailed info in all detailed events
    public static final Type G1_EDEN = new Type("Eden", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);

    // G1 concurrent types
    public static final Type G1_CONCURRENT_ROOT_REGION_SCAN_START = new Type("GC concurrent-root-region-scan-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type G1_CONCURRENT_ROOT_REGION_SCAN_END = new Type("GC concurrent-root-region-scan-end", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type G1_CONCURRENT_MARK_START = new Type("GC concurrent-mark-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type G1_CONCURRENT_MARK_END = new Type("GC concurrent-mark-end", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type G1_CONCURRENT_MARK_ABORT = new Type("GC concurrent-mark-abort", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type G1_CONCURRENT_MARK_RESET_FOR_OVERFLOW = new Type("GC concurrent-mark-reset-for-overflow", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type G1_CONCURRENT_COUNT_START = new Type("GC concurrent-count-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type G1_CONCURRENT_COUNT_END = new Type("GC concurrent-count-end", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type G1_CONCURRENT_CLEANUP_START = new Type("GC concurrent-cleanup-start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC);
    public static final Type G1_CONCURRENT_CLEANUP_END = new Type("GC concurrent-cleanup-end", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);

    // unified jvm logging generic event types
    public static final Type UJL_PAUSE_YOUNG = new Type("Pause Young", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_PAUSE_FULL = new Type("Pause Full", AbstractGCEvent.Generation.ALL, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);

    // unified jvm logging cms / g1 event types
    public static final Type UJL_PAUSE_INITIAL_MARK = new Type("Pause Initial Mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    public static final Type UJL_PAUSE_REMARK = new Type("Pause Remark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);

    // unified jvm logging cms event types
    public static final Type UJL_CMS_CONCURRENT_MARK = new Type("Concurrent Mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_PRECLEAN = new Type("Concurrent Preclean", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_ABORTABLE_PRECLEAN = new Type("Concurrent Abortable Preclean", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_SWEEP = new Type("Concurrent Sweep", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_RESET = new Type("Concurrent Reset", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_CMS_CONCURRENT_OLD = new Type("Old", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_MEMORY);

    // unified jvm logging g1 event types
    public static final Type UJL_G1_PAUSE_MIXED = new Type("Pause Mixed", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_G1_TO_SPACE_EXHAUSTED = new Type("To-space exhausted", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC);
    public static final Type UJL_G1_CONCURRENT_CYCLE = new Type("Concurrent Cycle", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_G1_PAUSE_CLEANUP = new Type("Pause Cleanup", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE, AbstractGCEvent.CollectionType.CONCURRENCY_HELPER);
    public static final Type UJL_G1_EDEN = new Type("Eden regions", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_REGION);
    public static final Type UJL_G1_SURVIVOR = new Type("Survivor regions", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_REGION);
    public static final Type UJL_G1_OLD = new Type("Old regions", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_REGION);
    public static final Type UJL_G1_HUMongous = new Type("Humongous regions", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_REGION);

    // unified jvm logging shenandoah event types
    public static final Type UJL_SHEN_INIT_MARK = new Type("Pause Init Mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_SHEN_FINAL_MARK = new Type("Pause Final Mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_INIT_UPDATE_REFS = new Type("Pause Init Update Refs", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_SHEN_FINAL_UPDATE_REFS = new Type("Pause Final Update Refs", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_MARK = new Type("Concurrent marking", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_EVAC = new Type("Concurrent evacuation", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CANCEL_CONC_MARK = new Type("Cancel concurrent mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_RESET_BITMAPS = new Type("Concurrent reset bitmaps", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_CONC_UPDATE_REFS = new Type("Concurrent update references", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);
    public static final Type UJL_SHEN_CONCURRENT_PRECLEANING = new Type("Concurrent precleaning", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_MEMORY_PAUSE);

    // unified jvm logging ZGC event types
    public static final Type UJL_ZGC_GARBAGE_COLLECTION = new Type("Garbage Collection", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_MEMORY_PERCENTAGE);
    public static final Type UJL_ZGC_PAUSE_MARK_START = new Type("Pause Mark Start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_PAUSE_MARK_END = new Type("Pause Mark End", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_PAUSE_RELOCATE_START = new Type("Pause Relocate Start", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_MARK = new Type("Concurrent Mark", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_NONREF = new Type("Concurrent Process Non-Strong References", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_RESET_RELOC_SET = new Type("Concurrent Reset Relocation Set", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_DETATCHED_PAGES = new Type("Concurrent Destroy Detached Pages", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_SELECT_RELOC_SET = new Type("Concurrent Select Relocation Set", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_PREPARE_RELOC_SET = new Type("Concurrent Prepare Relocation Set", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_CONCURRENT_RELOCATE = new Type("Concurrent Relocate", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.CONCURRENT, AbstractGCEvent.GcPattern.GC_PAUSE);
    public static final Type UJL_ZGC_HEAP_CAPACITY = new Type("Capacity", AbstractGCEvent.Generation.TENURED, AbstractGCEvent.Concurrency.SERIAL, AbstractGCEvent.GcPattern.GC_HEAP_MEMORY_PERCENTAGE);

    // IBM Types
    // TODO: are scavenge always young only??
    public static final Type IBM_AF = new Type("af", AbstractGCEvent.Generation.YOUNG);
    public static final Type IBM_SYS = new Type("sys explicit", AbstractGCEvent.Generation.ALL);
    public static final Type IBM_AF_SCAVENGE = new Type("af scavenge", AbstractGCEvent.Generation.YOUNG);
    public static final Type IBM_AF_GLOBAL = new Type("af global", AbstractGCEvent.Generation.TENURED);
    public static final Type IBM_SYS_GLOBAL = new Type("sys global", AbstractGCEvent.Generation.ALL);
    public static final Type IBM_SYS_EXPLICIT_GLOBAL = new Type("sys explicit global", AbstractGCEvent.Generation.ALL);
    public static final Type IBM_SCAVENGE = new Type("scavenge", AbstractGCEvent.Generation.YOUNG, AbstractGCEvent.Concurrency.SERIAL);
    public static final Type IBM_GLOBAL = new Type("global", AbstractGCEvent.Generation.ALL, AbstractGCEvent.Concurrency.SERIAL);
    public static final Type IBM_NURSERY = new Type("nursery", AbstractGCEvent.Generation.YOUNG);
    public static final Type IBM_TENURE = new Type("tenure", AbstractGCEvent.Generation.TENURED);

    public static final Type IBM_CONCURRENT_COLLECTION_START = new Type("concurrent-collection-start", AbstractGCEvent.Generation.ALL, AbstractGCEvent.Concurrency.CONCURRENT);
}
