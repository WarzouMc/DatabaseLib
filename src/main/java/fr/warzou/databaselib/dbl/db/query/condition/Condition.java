package fr.warzou.databaselib.dbl.db.query.condition;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.Column;

import java.util.List;

// ((x & y) | z & a) | f
public interface Condition {

    <T extends Data<?>> Condition in(Column<T> column, T... in);

    <T extends Data<?>> Condition in(Column<T> column, List<T> in);

    /**
     * Not support yet
     * Check server doc to implement that correctly
     */
    <T extends Data<?>> Condition between(Column<T> column, T bound1, T bound2);

    <T extends Data<String>> Condition like(Column<T> column, String model);

    <T extends Data<String>> Condition likeStartedBy(Column<T> column, String start);

    <T extends Data<String>> Condition likeEndedBy(Column<T> column, String end);

    <T extends Data<String>> Condition likeContain(Column<T> column, String contain);

    <T extends Data<String>> Condition likeBoundedBy(Column<T> column, String start, String end);

    <T extends Data<?>> Condition likeWithUnknownCharacter(Column<T> column, String before, String after);

    Condition and(Condition condition);

    Condition and(boolean condition);

    Condition or(Condition condition);

    Condition or(boolean condition);

}
