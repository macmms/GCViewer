package com.tagtraum.perf.gcviewer.model;

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
    CONCURRENCY_HELPER
}
