The files in the `generated` package are generated based on the JOOQ tutorial: 
[Step 3: Code generation](https://www.jooq.org/doc/3.16/manual/getting-started/tutorials/jooq-in-7-steps/jooq-in-7-steps-step3/)

There were a few adjustments made before doing so:
1. Used a hosted PG database instance instead of MySQL
2. library.xml modified appropriately
3. Below placeholder function created on database before code generation, for demonstrating [#6359](https://github.com/jOOQ/jOOQ/issues/6359) 

```
create or replace function donothing_array(
    p_bigints bigint[]
    )
language plpgsql
as $$
begin
    null;
end;
$$;
```