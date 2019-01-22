package com.tagtraum.perf.gcviewer.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * The abstract gc event is the base class for all types of events. All sorts of general
 * information can be queried from it and it provides the possibility to add detail events.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 */
public abstract class AbstractGCEvent<T extends AbstractGCEvent<T>> implements Serializable, Cloneable {
    /** Used before GC in KB */
    private int preUsed;
    /** Used after GC in KB */
    private int postUsed;
    /** Percentage used before GC */
    private int preUsedPercent;
    /** Percentage used after GC */
    private int postUsedPercent;
    /** Capacity in KB */
    private int total;
    /** end of gc event (after pause) */
    private ZonedDateTime datestamp;
    /** end of gc event (after pause) */
    private double timestamp;
    private ExtendedType extendedType = ExtendedType.UNDEFINED;
    private String typeAsString;
    protected Generation generation;
    protected List<T> details;
    private double pause;
    private int number = -1;

    public Iterator<T> details() {
        if (details == null) return Collections.emptyIterator();
        return details.iterator();
    }

    public void add(T detail) {
        // most events have only one detail event
        if (details == null) {
        	details = new ArrayList<T>(2);
        }
        details.add(detail);
        typeAsString += "; " + detail.getExtendedType().getName();

        // reset cached value, which will be recalculated upon call to "getGeneration()"
        generation = null;
    }

    public boolean hasDetails() {
        return details != null
                && details.size() > 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AbstractGCEvent<T> clonedEvent = (AbstractGCEvent<T>)super.clone();
        if (getDatestamp() != null) {
            clonedEvent.setDateStamp(ZonedDateTime.from(this.getDatestamp()));
        }
        if (getExtendedType() != null) {
            clonedEvent.setExtendedType(new ExtendedType(getExtendedType().getType(), getExtendedType().fullName));
        }
        if (details != null) {
            List<T> detailClones = new ArrayList<>();
            for (T t : details) {
                detailClones.add((T)t.clone());
            }
            clonedEvent.details = detailClones;
        }

        // don't need to do anything with "generation", because that value is reset by the "add()" method

        return clonedEvent;
    }

    public AbstractGCEvent<T> cloneAndMerge(AbstractGCEvent<T> otherEvent) {
        try {
            AbstractGCEvent<T> clonedEvent = (AbstractGCEvent<T>)otherEvent.clone();
            clonedEvent.setExtendedType(new ExtendedType(getExtendedType().getType(), getExtendedType().fullName + "+" + clonedEvent.getExtendedType().fullName));
            clonedEvent.setPreUsed(clonedEvent.getPreUsed() + getPreUsed());
            clonedEvent.setPostUsed(clonedEvent.getPostUsed() + getPostUsed());
            clonedEvent.setTotal(clonedEvent.getTotal() + getTotal());
            clonedEvent.setPause(clonedEvent.getPause() + getPause());
            return clonedEvent;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("hmm, clone was not supported, that's unexpected...", e);
        }
    }

    public void setDateStamp(ZonedDateTime datestamp) {
    	this.datestamp = datestamp;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(Type type) {
        setExtendedType(ExtendedType.lookup(type));
    }

    public void setExtendedType(ExtendedType extendedType) {
        this.extendedType = extendedType;
        this.typeAsString = extendedType.getName();
        if (details != null && details.size() > 0) {
            this.typeAsString = buildTypeAsString();
        }
    }

    private String buildTypeAsString() {
        StringBuilder sb = new StringBuilder(getExtendedType().getName());
        if (details != null) {
            for (T event : details) {
                sb.append("; ").append(event.getExtendedType().getName());
            }
        }

        return sb.toString();
    }

    public ExtendedType getExtendedType() {
        return extendedType;
    }

    public int getNumber() {
        return number;
    }

    public String getTypeAsString() {
    	return typeAsString;
    }

    public boolean isStopTheWorld() {
        boolean isStopTheWorld = getExtendedType().getConcurrency() == Concurrency.SERIAL;
        if (details != null) {
            for (T detailEvent : details) {
                if (!isStopTheWorld) {
                    isStopTheWorld = detailEvent.getExtendedType().getConcurrency() == Concurrency.SERIAL;
                }
            }
        }

        return isStopTheWorld;
    }

    /**
     * Returns the generation of the event including generation of detail events if present.
     * @return generation of event including generation of detail events
     */
    public Generation getGeneration() {
        if (generation == null) {
            if (!hasDetails()) {
                generation = getExtendedType().getGeneration();
            }
            else {
                // find out, what generations the detail events contain
                Set<Generation> generationSet = new TreeSet<Generation>();
                for (T detailEvent : details) {
                    generationSet.add(detailEvent.getExtendedType().getGeneration());
                }

                if (generationSet.size() > 1 || generationSet.contains(Generation.ALL)) {
                    generation = Generation.ALL;
                }
                else if (generationSet.size() == 1) {
                    generation  = generationSet.iterator().next();
                }
                else {
                    // default
                    generation = Generation.YOUNG;
                }
            }
        }

        return generation;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public ZonedDateTime getDatestamp() {
        return datestamp;
    }

    public boolean hasMemoryInformation() {
        return getPreUsed() > 0
                || getPostUsed() > 0
                || getTotal() > 0;
    }

    public void setPreUsed(int preUsed) {
        this.preUsed = preUsed;
    }

    public void setPostUsed(int postUsed) {
        this.postUsed = postUsed;
    }

    public void setPreUsedPercent(int preUsedPercent) {
        this.preUsedPercent = preUsedPercent;
    }

    public void setPostUsedPercent(int postUsedPercent) {
        this.postUsedPercent = postUsedPercent;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPreUsed() {
        return preUsed;
    }

    public int getPostUsed() {
        return postUsed;
    }

    public int getPreUsedPercent() {
        return preUsedPercent;
    }

    public int getPostUsedPercent() {
        return postUsedPercent;
    }

    public int getTotal() {
        return total;
    }

    public abstract void toStringBuffer(StringBuffer sb);

    @Override
	public String toString() {
        StringBuffer sb = new StringBuffer(128);
        toStringBuffer(sb);
        return sb.toString();
    }

    public boolean isFull() {
        if (getExtendedType().getGeneration().compareTo(Generation.ALL) == 0) {
            return true;
        }

        if (details != null) {
            // the assumption is, that a full collection is everything, that collects from more
            // than one generation.
            // Probably this is not always strictly right, but often enough a good assumption
            return details.size() > 1;
        }
        return false;
    }

    /**
     * Returns true, if this event was triggered by a call to "System.gc()"
     * @return <code>true</code> if triggered by "System.gc()"
     */
    public boolean isSystem() {
        return getExtendedType().getName().contains("System");
    }

    public boolean isInc() {
        return getExtendedType().getType() == Type.INC_GC;
    }

    public boolean isConcurrent() {
        return getExtendedType().getConcurrency().equals(Concurrency.CONCURRENT);
    }

    public boolean isConcurrencyHelper() {
        return getExtendedType().getCollectionType().equals(CollectionType.CONCURRENCY_HELPER);
    }

    public boolean isConcurrentCollectionStart() {
        return getExtendedType().getName().equals(Type.CMS_CONCURRENT_MARK_START.getName()) // CMS
                || getExtendedType().getName().equals(Type.ASCMS_CONCURRENT_MARK_START.getName()) // CMS AdaptiveSizePolicy
                || (getExtendedType().getName().equals(Type.UJL_CMS_CONCURRENT_MARK.getName()) && getPause() > 0.000001) // Universal jvm logging, CMS
                || getExtendedType().getName().equals(Type.G1_CONCURRENT_MARK_START.getName()) // G1
                || (getExtendedType().getName().equals(Type.UJL_G1_CONCURRENT_CYCLE.getName()) && getPause() < 0.00001) // Universal jvm logging, G1
                || getExtendedType().getName().equals(Type.UJL_SHEN_CONCURRENT_CONC_MARK.getName()); // Universal jvm logging, Shenandoah
    }

    public boolean isConcurrentCollectionEnd() {
        return getExtendedType().getName().equals(Type.CMS_CONCURRENT_RESET.getName()) // CMS
                || getExtendedType().getName().equals(Type.ASCMS_CONCURRENT_RESET.getName()) // CMS AdaptiveSizePolicy
                || (getExtendedType().getName().equals(Type.UJL_CMS_CONCURRENT_RESET.getName()) && getPause() > 0.0000001) // Universal jvm logging, CMS
                || getExtendedType().getName().equals(Type.G1_CONCURRENT_CLEANUP_END.getName()) // G1
                || (getExtendedType().getName().equals(Type.UJL_G1_CONCURRENT_CYCLE.getName()) && getPause() > 0.0000001) // Universal jvm logging, G1
                || getExtendedType().getName().equals(Type.UJL_SHEN_CONCURRENT_CONC_RESET_BITMAPS.getName()); // Universal jvm logging, Shenandoah
    }

    public boolean isInitialMark() {
        return getTypeAsString().indexOf("initial-mark") >= 0      // all others
                || getTypeAsString().indexOf("Initial Mark") >= 0 // Unified jvm logging, CMS + G1
                || getTypeAsString().indexOf("Init Mark") >= 0; // Unified jvm logging, Shenandoah
    }

    public boolean isRemark() {
        return getTypeAsString().indexOf(Type.CMS_REMARK.getName()) >= 0
                || getTypeAsString().indexOf(Type.ASCMS_REMARK.getName()) >= 0
                || getTypeAsString().indexOf(Type.G1_REMARK.getName()) >= 0
                || getTypeAsString().indexOf(Type.UJL_PAUSE_REMARK.getName()) >= 0
                || getTypeAsString().indexOf(Type.UJL_SHEN_FINAL_MARK.getName()) >= 0;
    }

    public boolean isCycleStart() {
        return Type.UJL_ZGC_GARBAGE_COLLECTION.equals(getExtendedType().getType());
    }

    public double getPause() {
        return pause;
    }

    public void setPause(double pause) {
        this.pause = pause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AbstractGCEvent))
            return false;
        AbstractGCEvent<?> that = (AbstractGCEvent<?>) o;
        return Double.compare(that.timestamp, timestamp) == 0 &&
                Double.compare(that.pause, pause) == 0 &&
                Objects.equals(datestamp, that.datestamp) &&
                Objects.equals(extendedType, that.extendedType) &&
                Objects.equals(typeAsString, that.typeAsString) &&
                generation == that.generation &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datestamp, timestamp, extendedType, typeAsString, generation, details);
    }

    /**
     * Wrapper for the {@link Type} class adding a field for the full type name. That name may
     * be different from the name in <code>Type</code>. Since all other attributes of
     * <code>Type</code> are shared, only this attribute is additionally held.
     *
     * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
     */
    public static class ExtendedType implements Serializable {
        private static final Map<String, ExtendedType> WRAPPER_MAP = new HashMap<>();
        static {
            WRAPPER_MAP.put(Type.UNDEFINED.getName(), new ExtendedType(Type.UNDEFINED));
        }
        public static final ExtendedType UNDEFINED = WRAPPER_MAP.get(Type.UNDEFINED);

        private String fullName;
        private Type type;

        private ExtendedType(Type type) {
            this(type, type.getName());
        }

        private ExtendedType(Type type, String fullName) {
            this.type = type;
            this.fullName = fullName.intern();
        }

        public static ExtendedType lookup(Type type) {
            return lookup(type, type.getName());
        }

        public static ExtendedType lookup(Type type, String fullName) {
            ExtendedType extType = WRAPPER_MAP.get(fullName);
            if (extType == null) {
                extType = new ExtendedType(type, fullName);
                WRAPPER_MAP.put(fullName, extType);
            }

            return extType;
        }

        public String getName() {
            return fullName;
        }

        public Type getType() {
            return type;
        }

        public GcPattern getPattern() {
            return type.getPattern();
        }

        public Generation getGeneration() {
            return type.getGeneration();
        }

        public CollectionType getCollectionType() {
            return type.getCollectionType();
        }

        public Concurrency getConcurrency() {
            return type.getConcurrency();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ExtendedType that = (ExtendedType) o;
            return Objects.equals(fullName, that.fullName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fullName);
        }

        @Override
        public String toString() {
            return fullName;
        }
	}

    /**
     * Patterns of additional information for gc events.
     * <ul>
     *     <li><code>GC</code>: just the name of the event type</li>
     *     <li><code>PAUSE</code>: length of a pause</li>
     *     <li><code>DURATION</code>: cycle time of a (usually concurrent) event</li>
     *     <li><code>MEMORY</code>: information about heap changes</li>
     *     <li><code>REGION</code>: information about number of regions used (only G1 up to now)</li>
     * </ul>
     */
    public enum GcPattern {
        /** "GC type" (just the name, no additional information) */
        GC,
        /** "GC type": "pause" */
        GC_PAUSE,
        /** "GC type": "pause"/"duration" */
        GC_PAUSE_DURATION,
        /** "GC type": "memory current"("memory total") */
        GC_MEMORY,
        /** "GC type": "memory before"-&gt;"memory after"("memory total"), "pause" */
    	GC_MEMORY_PAUSE,
        /** "GC type": "# regions before"-&gt;"# regions after"[("#total regions")] ("total regions" is optional; needs a region size to calculate memory usage)*/
        GC_REGION,
        /** "Garbage Collection (Reason)" "memory before"("percentage of total")-&gt;"memory after"("percentage of total") */
        GC_MEMORY_PERCENTAGE,
        /** "Heap memory type" "memory current"("memory percentage") */
        GC_HEAP_MEMORY_PERCENTAGE
    }

    public enum Concurrency { CONCURRENT, SERIAL }

    public enum Generation { YOUNG,
        TENURED,
        /** also used for "metaspace" that is introduced with java 8 */
        PERM,
        ALL,
        /** special value for vm operations that are not collections */
        OTHER }

    public enum CollectionType {
        /** plain GC pause collection garbage */
        COLLECTION,
        /**
         * Not really a collection, but some other event that stops the vm.
         *
         * @see <a href="http://stackoverflow.com/questions/2850514/meaning-of-message-operations-coalesced-during-safepoint">
         * Stackoverflow: meaning of message operations coalesced during safepoint</a>
         */
        VM_OPERATION,
        /** stop the world pause but used to prepare concurrent collection, might not collect garbage */
        CONCURRENCY_HELPER }
}
