/** A report that lists the tables belonging to a particular schema */
report(title: 'List User Tables') {
  
  // Create a parameter for the owner of the table
  param(name: 'owner', label: 'Owner') {
    
    // Add a list of values that excludes Oracle schemas and
    //     schemas that don't own any tables
    list_of_values query: """select username 
                                 from all_users 
                                 where oracle_maintained = 'N'
                             intersect
                             select owner from all_tables"""
  }

  // A second parameter for the table name.  Make it default to '%'
  param(name: 'table_name',
        label: 'Table Name',
        description: 'List tables matching this value, with Oracle wildcards.',
        'default': '%')

  // Get the data from an SQL query.  This references the parameters with
  //    a colon.
  sql query: """select table_name, comments
                    from all_tab_comments
                    where owner = :owner 
                      and table_name like upper(nvl(:table_name,'%'))
                      and comments is not null
                order by table_name"""
}
