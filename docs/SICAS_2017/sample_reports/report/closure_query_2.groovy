/** A second closure query example */
report(title: "Invalid Summit Queries") {
  data_generator closure: {
    column(name: 'id', label: "ID");
    column(name: 'sql_query', label: "SQL Query");
    column(name: 'error', label: "Error");
    def queries = sql.rows("select * from summit_sql order by id");
    queries.each {
      q ->
        query_row = [id: q.id,
                     sql_query: q.sql_query,
                     error: ''];
        try {
          // Beware of Groovy's GString 
          String explainQuery = "explain plan for ${q.sql_query}"
          sql.execute(explainQuery);
        } catch (java.sql.SQLException e) {
          query_row.error = e.getMessage();
        }
        if (query_row.error?.size()) {
          row(query_row);
        }
    }
  }
}
