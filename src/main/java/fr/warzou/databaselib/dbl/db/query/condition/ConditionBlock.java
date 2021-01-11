package fr.warzou.databaselib.dbl.db.query.condition;

public interface ConditionBlock extends Condition {

    Condition and(ConditionBlock condition);

    Condition or(ConditionBlock condition);

}
