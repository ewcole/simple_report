/* List all data dictionary tables matching the pattern */
select * from dictionary
    where table_name like nvl(upper(:table_name),'%')
    order by table_name
