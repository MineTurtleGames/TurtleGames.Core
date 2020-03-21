package co.turtlegames.core.util;

import java.util.function.Function;

public class Call {

    public interface SingleArgProcedure<I> {
        public void invoke(I a);
    }

    public static <I> Function<Throwable, I> exceptionPassthrough(SingleArgProcedure<Throwable> toCall) {

        return (Throwable throwable) -> {
            toCall.invoke(throwable);
            return null;
        };

    }

}
