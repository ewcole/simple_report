/* List all objects that reference this one. */
select d.referenced_owner r_owner,
       d.referenced_name  r_name,
       d.referenced_type  r_type,
       d.owner, d.name, d.type
    from all_dependencies d
    where d.referenced_name like upper(:object_name)
    order by referenced_owner, referenced_name,
             referenced_type, name, type
