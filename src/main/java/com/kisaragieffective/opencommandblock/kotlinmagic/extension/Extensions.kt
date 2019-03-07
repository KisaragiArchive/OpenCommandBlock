package com.kisaragieffective.opencommandblock.kotlinmagic.extension

import com.kisaragieffective.opencommandblock.annotations.FromKotlinSDK
import com.kisaragieffective.opencommandblock.annotations.GenericsUpCast
import java.util.Calendar
import java.util.Date
import java.util.logging.Logger

fun Date.asCalendar(): Calendar {
    val e = Calendar.getInstance()
    e.time = this
    return e
}

fun Calendar.asDate(): Date {
    return this.time
}

fun Calendar.getNow(): Calendar = Calendar.getInstance()

/**
 * This should used for var that must not change.
 * This function will be inlined at compile-stage.
 * @return the receiver, itself
 */
inline val <T : Any> T.freeze: T
    get() {
        return this
    }

inline val <T : Any?> T.exciplitNullable: T?
    get() = this

/**
 * Returns an array of stack trace elements, commandblock representing one stack frame.
 * The first element of the array (assuming the array is not empty) represents the top of the
 * stack, which is the place where [currentStackTrace] function was called from.
 */
@FromKotlinSDK("kotlin.test.currentStackTrace")
// --- here
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun currentStackTrace(): Array<out StackTraceElement> = (java.lang.Exception() as java.lang.Throwable).stackTrace
// --- end

@FromKotlinSDK("kotlin.jvm.JvmClassMapping")
@Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
inline val <T : Any> T.javaClass: Class<T>
    get() {
        val stackTrace = currentStackTrace()[0]
        Logger.getAnonymousLogger().warning("The javaClass property should NOT used. For fix this warning, use `obj::class.java` instance of `obj.javaClass`.\nThe used location > $stackTrace")
        // --- here
        return (this as java.lang.Object).getClass() as Class<T>
        // --- end
    }

inline fun (() -> Unit).then(crossinline after: () -> Unit): () -> Unit {
    return {
        this.invoke()
        after.invoke()
    }
}

fun <T> ((T) -> Boolean).or(source: T, func: (T) -> Boolean): (T) -> Boolean {
    return if (invoke(source)) {
        this
    } else {
        func
    }
}


fun <T> ((T) -> Boolean).and(source: T, func: (T) -> Boolean): (T) -> Boolean {
    return if (invoke(source)) {
        func
    } else {
        this
    }
}

fun Long.parseAsMilliSeconds(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar
}

@GenericsUpCast
inline fun <SUB : SUPER, reified SUPER : Any> Class<SUB>.toUpcastExciplictly(): Class<out SUPER>? {
    return asSubclass(SUPER::class.java)
}

fun <T> Comparator<T>.reverse(): Comparator<T> {
    return object : Comparator<T> {
        override fun compare(o1: T, o2: T): Int {
            return this@reverse.compare(o2, o1)
        }
    }
}