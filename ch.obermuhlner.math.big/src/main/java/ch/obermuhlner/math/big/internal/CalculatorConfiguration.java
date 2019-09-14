package ch.obermuhlner.math.big.internal;

import java.util.concurrent.atomic.AtomicBoolean;

public final class CalculatorConfiguration {

    private final static AtomicBoolean ASIN_AS_SINGLETON = new AtomicBoolean(true);

    private final static AtomicBoolean EXP_AS_SINGLETON = new AtomicBoolean(true);

    private final static AtomicBoolean SINH_AS_SINGLETON = new AtomicBoolean(true);

    private final static AtomicBoolean SIN_AS_SINGLETON = new AtomicBoolean(true);

    private final static AtomicBoolean ATANH_AS_SINGLETON = new AtomicBoolean(true);

    private final static AtomicBoolean COS_AS_SINGLETON = new AtomicBoolean(true);

    private final static AtomicBoolean COSH_AS_SINGLETON = new AtomicBoolean(true);

    private CalculatorConfiguration() {
        //No instance is allowed
    }

    public static void asinAsSingleton(boolean asSingleton){
        ASIN_AS_SINGLETON.set(asSingleton);
    }

    static boolean isAsinSingleton(){
       return ASIN_AS_SINGLETON.get();
    }

    public static void expAsSingleton(boolean asSingleton){
        EXP_AS_SINGLETON.set(asSingleton);
    }

    static boolean isExpSingleton(){
        return EXP_AS_SINGLETON.get();
    }

    public static void sinhAsSingleton(boolean asSingleton){
        SINH_AS_SINGLETON.set(asSingleton);
    }

    static boolean isSinhSingleton(){
        return SINH_AS_SINGLETON.get();
    }

    public static void sinAsSingleton(boolean asSingleton){
        SIN_AS_SINGLETON.set(asSingleton);
    }

    static boolean isSinSingleton(){
        return SIN_AS_SINGLETON.get();
    }

    public static void atanhAsSingleton(boolean asSingleton){
        ATANH_AS_SINGLETON.set(asSingleton);
    }

    static boolean isAtanhSingleton(){
        return ATANH_AS_SINGLETON.get();
    }

    public static void cosAsSingleton(boolean asSingleton){
        COS_AS_SINGLETON.set(asSingleton);
    }

    static boolean isCosSingleton(){
        return COS_AS_SINGLETON.get();
    }

    public static void coshAsSingleton(boolean asSingleton){
        COSH_AS_SINGLETON.set(asSingleton);
    }

    static boolean isCoshSingleton(){
        return COSH_AS_SINGLETON.get();
    }
}
