// Test case for Issue 329:
// https://code.google.com/p/checker-framework/issues/detail?id=329

import org.checkerframework.checker.nullness.qual.Nullable;

abstract class Issue329 {
    interface Flag<T> {}

    abstract <X> void setExtension(X value);
    abstract <T> T getValue(Flag<T> flag);

    void f(Flag<String> flag) {
        String s = getValue(flag);
        setExtension(s);

        setExtension(getValue(flag));
    }
}

abstract class Issue329NN {
    interface Flag<T> {}

    // Explicit bound makes it NonNull
    abstract <X extends Object> void setExtension(X value);
    abstract <T> T getValue(Flag<T> flag);

    void f1(Flag<@Nullable String> flag) {
        String s = getValue(flag);
        //:: error: (type.argument.type.incompatible)
        setExtension(s);
    }

    void f2(Flag<@Nullable String> flag) {
        // This error is missed because BaseTypeVisitor.checkTypeArguments
        // ignores wildcards.
        //TODO:: error: (type.argument.type.incompatible)
        setExtension(getValue(flag));
    }
}
