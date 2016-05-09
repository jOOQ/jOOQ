package org.jooq.example.checker;

import org.jooq.Allow;
import org.jooq.impl.DSL;

// This class allows for using PlainSQL API
@Allow.PlainSQL
class PlainSQLCheckerTests1 {
    public static void compiles() {
        DSL.field("plain SQL");
    }
}

// This class doesn't allow for using PlainSQL API
class PlainSQLCheckerTests2 {
    public static void doesntCompile() {
        DSL.field("plain SQL");
    }

    @Allow.PlainSQL
    public static void compiles() {
        DSL.field("plain SQL");
    }
}