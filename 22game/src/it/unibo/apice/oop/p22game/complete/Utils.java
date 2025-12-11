package it.unibo.apice.oop.p22game.complete;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Utils {

    public static double fitIntoRange(double val, double min, double max){
        return Math.min(Math.max(val, min), max);
    }

    public static <T> void updateList(List<T> list, UnaryOperator<T> op){
        for (int i = 0; i < list.size(); i++){
            list.set(i, op.apply(list.get(i)));
        }
    }

    public static <T> void removeFromList(List<T> list, Predicate<T> op){
        for (int i = 0; i < list.size(); ){
            if (op.test(list.get(i))){
                list.remove(i);
            } else {
                i++;
            }
        }
    }

}
