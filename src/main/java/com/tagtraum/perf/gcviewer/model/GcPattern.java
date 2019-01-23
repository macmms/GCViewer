package com.tagtraum.perf.gcviewer.model;

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
