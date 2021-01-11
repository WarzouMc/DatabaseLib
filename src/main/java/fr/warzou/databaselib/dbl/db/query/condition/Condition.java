package fr.warzou.databaselib.dbl.db.query.condition;

import fr.warzou.databaselib.dbl.db.columns.Column;

import java.util.List;

// ((x & y) | z & a) | f
public interface Condition {

    <T> Condition in(Column<T> column, T... in);

    <T> Condition in(Column<T> column, List<T> in);

    /**
     * Not support yet
     * Check server doc to implement that correctly
     */
    <T> Condition between(Column<T> column, T bound1, T bound2);

    <T extends String> Condition like(Column<T> column, String model);

    <T extends String> Condition likeStartedBy(Column<T> column, String start);

    <T extends String> Condition likeEndedBy(Column<T> column, String end);

    <T extends String> Condition likeContain(Column<T> column, String contain);

    <T extends String> Condition likeBoundedBy(Column<T> column, String start, String end);

    <T extends String> Condition likeWithUnknownCharacter(Column<T> column, String before, String after);

    Condition and(Condition condition);

    Condition and(boolean condition);

    Condition or(Condition condition);

    Condition or(boolean condition);

}
