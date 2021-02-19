package fr.warzou.databaselib.dbl.db.query.condition;

@FunctionalInterface
public interface ConditionAssembler {

    ConditionBlock assemble(Condition condition);

}
