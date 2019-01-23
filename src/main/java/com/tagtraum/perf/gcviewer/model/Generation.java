package com.tagtraum.perf.gcviewer.model;

public enum Generation {
    YOUNG,
    TENURED,
    /** also used for "metaspace" that is introduced with java 8 */
    PERM,
    ALL,
    /** special value for vm operations that are not collections */
    OTHER
}
