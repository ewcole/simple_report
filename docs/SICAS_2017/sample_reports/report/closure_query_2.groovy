/** A second closure query example */
report(title: "Invalid Summit Queries") {
  
  // Specify a closure to return the report data.
  data_generator closure: {
    // We have to define the columns explicitly 
    column(name: 'id', label: "ID");
    column(name: 'sql_query', label: "SQL Query");
    column(name: 'error', label: "Error");

    // "sql" is a pre-defined groovy.sql.SQL object
    def queries = sql.rows("select * from summit_sql order by id");
    // Loop through all the rows in the table and test each query
    queries.each {
      q ->
        query_row = [id: q.id,
                     sql_query: q.sql_query,
                     error: ''];
        // Test the query from the table by invoking explain_plan.  If
        //     the query fails to compile, it will throw a SQLException.
        try {
          // Beware of Groovy's GString 
          String explainQuery = "explain plan for ${q.sql_query}"
          sql.execute(explainQuery);
        } catch (java.sql.SQLException e) {
          query_row.error = e.getMessage();
        }
        if (query_row.error?.size()) {
          // "row" is a predefined method that add a row
          //     to the result set.
          row(query_row);
        }
    }
  }
}
